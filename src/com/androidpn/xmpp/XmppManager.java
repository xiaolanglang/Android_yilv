/*
 * Copyright (C) 2010 Moduad Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.androidpn.xmpp;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Future;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Registration;
import org.jivesoftware.smack.provider.ProviderManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.util.Log;

import com.androidpn.service.NotificationService;
import com.androidpn.util.Constants;
import com.common.util.LogUtil;

/**
 * This class is to manage the XMPP connection between client and server.
 * 
 * @author Sehwan Noh (devnoh@gmail.com)
 */
public class XmppManager {

	private static final String TAG = XmppManager.class.getSimpleName();

	private static final String XMPP_RESOURCE_NAME = "AndroidpnClient";

	private Context context;

	private NotificationService.TaskSubmitter taskSubmitter;

	private NotificationService.TaskTracker taskTracker;

	private SharedPreferences sharedPrefs;

	private String xmppHost;

	private int xmppPort;

	private XMPPConnection connection;

	private String username;

	private String password;

	private ConnectionListener connectionListener;

	private PacketListener notificationPacketListener;

	private Handler handler;

	private List<Runnable> taskList;

	private boolean running = false;

	private Future<?> futureTask;

	private Thread reconnection;

	private LoginListener loginListener;

	public XmppManager(NotificationService notificationService) {
		context = notificationService;
		taskSubmitter = notificationService.getTaskSubmitter();
		taskTracker = notificationService.getTaskTracker();
		sharedPrefs = notificationService.getSharedPreferences();

		xmppHost = sharedPrefs.getString(Constants.XMPP_HOST, "localhost");
		xmppPort = sharedPrefs.getInt(Constants.XMPP_PORT, 5222);
		username = sharedPrefs.getString(Constants.XMPP_USERNAME, "");
		password = sharedPrefs.getString(Constants.XMPP_PASSWORD, "");

		connectionListener = new PersistentConnectionListener(this);
		notificationPacketListener = new NotificationPacketListener(this);

		handler = new Handler();
		taskList = new ArrayList<Runnable>();
		reconnection = new ReconnectionThread(this);
	}

	public Context getContext() {
		return context;
	}

	public void connect() {
		Log.d(TAG, "connect()...");
		submitLoginTask();
	}

	public void disconnect() {
		Log.d(TAG, "disconnect()...");
		terminatePersistentConnection();
	}

	public void terminatePersistentConnection() {
		Log.d(TAG, "terminatePersistentConnection()...");
		Runnable runnable = new Runnable() {

			final XmppManager xmppManager = XmppManager.this;

			public void run() {
				if (xmppManager.isConnected()) {
					Log.d(TAG, "terminatePersistentConnection()... run()");
					xmppManager.getConnection().removePacketListener(xmppManager.getNotificationPacketListener());
					xmppManager.getConnection().disconnect();
				}
				xmppManager.runTask();
			}

		};
		addTask(runnable);
	}

	public XMPPConnection getConnection() {
		return connection;
	}

	public void setConnection(XMPPConnection connection) {
		this.connection = connection;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public ConnectionListener getConnectionListener() {
		return connectionListener;
	}

	public PacketListener getNotificationPacketListener() {
		return notificationPacketListener;
	}

	public void startReconnectionThread() {
		synchronized (reconnection) {
			if (!reconnection.isAlive()) {
				reconnection.setName("Xmpp Reconnection Thread");
				reconnection.start();
			}
		}
	}

	public Handler getHandler() {
		return handler;
	}

	public void reregisterAccount() {
		removeAccount();
		submitLoginTask();
		runTask();
	}

	public List<Runnable> getTaskList() {
		return taskList;
	}

	public Future<?> getFutureTask() {
		return futureTask;
	}

	public void runTask() {
		Log.d(TAG, "runTask()...");
		synchronized (taskList) {
			System.out.println(">>>>>>>>>>>>>>>>>>runTask>>>>>" + taskList.size());
			running = false;
			futureTask = null;
			if (!taskList.isEmpty()) {
				Runnable runnable = (Runnable) taskList.get(0);
				taskList.remove(0);
				running = true;
				futureTask = taskSubmitter.submit(runnable);
				if (futureTask == null) {
					taskTracker.decrease();
				}
			}
		}
		taskTracker.decrease();
		Log.d(TAG, "runTask()...done");
	}

	private String newRandomUUID() {
		String uuidRaw = UUID.randomUUID().toString();
		return uuidRaw.replaceAll("-", "");
	}

	private boolean isConnected() {
		return connection != null && connection.isConnected();
	}

	private boolean isAuthenticated() {
		return connection != null && connection.isConnected() && connection.isAuthenticated();
	}

	private boolean isRegistered() {
		return sharedPrefs.contains(Constants.XMPP_USERNAME) && sharedPrefs.contains(Constants.XMPP_PASSWORD);
	}

	private void submitLoginTask() {
		Log.d(TAG, "submitLoginTask()...");
		addTask(new ConnectTask());
		addTask(new RegisterTask());
		addTask(new LoginTask());
	}

	private void addTask(Runnable runnable) {
		Log.d(TAG, "addTask(runnable)...");
		taskTracker.increase();
		synchronized (taskList) {
			if (taskList.isEmpty() && !running) {
				System.out.println(">>>>>>>>>>>>>>>>>>addTask>>>>submit");
				running = true;
				futureTask = taskSubmitter.submit(runnable);
				if (futureTask == null) {
					taskTracker.decrease();
				}
			} else {
				taskList.add(runnable);
			}
			System.out.println(">>>>>>>>>>>>>>>>>>addTask>>>>" + taskList.size());
		}
		Log.d(TAG, "addTask(runnable)... done");
	}

	private void removeAccount() {
		Editor editor = sharedPrefs.edit();
		editor.remove(Constants.XMPP_USERNAME);
		editor.remove(Constants.XMPP_PASSWORD);
		editor.commit();
	}

	public void sendPacket(final Packet packet) {
		if (isAuthenticated()) {
			LogUtil.e(TAG, "发送消息");
			connection.sendPacket(packet);
		} else {
			LogUtil.e(TAG, "您没有连接到服务器");
			loginListener = new LoginListener() {

				@Override
				public void loginSuccess() {
					sendPacket(packet);
				}
			};
			connect();
		}
	}

	/**
	 * A runnable task to connect the server.
	 */
	private class ConnectTask implements Runnable {

		final XmppManager xmppManager;

		private ConnectTask() {
			this.xmppManager = XmppManager.this;
		}

		public void run() {
			Log.i(TAG, "ConnectTask.run()...");
			LogUtil.e(TAG, "连接到服务器");

			if (!xmppManager.isConnected()) {
				// Create the configuration for this new connection
				ConnectionConfiguration connConfig = new ConnectionConfiguration(xmppHost, xmppPort);
				// connConfig.setSecurityMode(SecurityMode.disabled);
				connConfig.setSecurityMode(SecurityMode.required);
				connConfig.setSASLAuthenticationEnabled(false);
				connConfig.setCompressionEnabled(false);

				XMPPConnection connection = new XMPPConnection(connConfig);
				xmppManager.setConnection(connection);

				try {
					// Connect to the server
					connection.connect();
					Log.i(TAG, "XMPP connected successfully");
					LogUtil.e(TAG, "连接成功");

					// packet provider
					ProviderManager.getInstance().addIQProvider("notification", "androidpn:iq:notification",
							new NotificationIQProvider());

				} catch (XMPPException e) {
					Log.e(TAG, "XMPP connection failed", e);
					LogUtil.e(TAG, "连接服务器失败");
				}

				xmppManager.runTask();

			} else {
				Log.i(TAG, "XMPP connected already");
				LogUtil.e(TAG, "已经和服务器链接");
				xmppManager.runTask();
			}
		}
	}

	/**
	 * A runnable task to register a new user onto the server.
	 */
	private class RegisterTask implements Runnable {

		final XmppManager xmppManager;

		private RegisterTask() {
			xmppManager = XmppManager.this;
		}

		public void run() {
			Log.i(TAG, "RegisterTask.run()...");
			LogUtil.e(TAG, "开始注册");

			if (!xmppManager.isConnected()) {
				LogUtil.e(TAG, "没有连接到服务器，注册失败");
				xmppManager.runTask();
				return;
			}
			if (!xmppManager.isRegistered()) {
				final String newUsername = newRandomUUID();
				final String newPassword = newRandomUUID();

				Registration registration = new Registration();

				PacketFilter packetFilter = new AndFilter(new PacketIDFilter(registration.getPacketID()),
						new PacketTypeFilter(IQ.class));

				PacketListener packetListener = new PacketListener() {

					public void processPacket(Packet packet) {
						Log.d("RegisterTask.PacketListener", "processPacket().....");
						Log.d("RegisterTask.PacketListener", "packet=" + packet.toXML());

						if (packet instanceof IQ) {
							IQ response = (IQ) packet;
							if (response.getType() == IQ.Type.ERROR) {
								if (!response.getError().toString().contains("409")) {
									Log.e(TAG, "Unknown error while registering XMPP account! "
											+ response.getError().getCondition());
								}
								LogUtil.e(TAG, "注册失败");
							} else if (response.getType() == IQ.Type.RESULT) {
								xmppManager.setUsername(newUsername);
								xmppManager.setPassword(newPassword);
								Log.d(TAG, "username=" + newUsername);
								Log.d(TAG, "password=" + newPassword);

								Editor editor = sharedPrefs.edit();
								editor.putString(Constants.XMPP_USERNAME, newUsername);
								editor.putString(Constants.XMPP_PASSWORD, newPassword);
								editor.commit();
								Log.i(TAG, "Account registered successfully");
								LogUtil.e(TAG, "注册成功");
							}
						}

						xmppManager.runTask();
					}
				};

				connection.addPacketListener(packetListener, packetFilter);

				registration.setType(IQ.Type.SET);
				registration.addAttribute("username", newUsername);
				registration.addAttribute("password", newPassword);
				connection.sendPacket(registration);

			} else {
				Log.i(TAG, "Account registered already");
				LogUtil.e(TAG, "已经注册");
				xmppManager.runTask();
			}
		}
	}

	/**
	 * A runnable task to log into the server.
	 */
	private class LoginTask implements Runnable {

		final XmppManager xmppManager;

		private LoginTask() {
			this.xmppManager = XmppManager.this;
		}

		public void run() {
			Log.i(TAG, "LoginTask.run()...");
			LogUtil.e(TAG, "开始登录");

			if (!xmppManager.isAuthenticated()) {
				Log.d(TAG, "username=" + username);
				Log.d(TAG, "password=" + password);

				try {
					xmppManager.getConnection().login(xmppManager.getUsername(), xmppManager.getPassword(),
							XMPP_RESOURCE_NAME);
					Log.d(TAG, "登录成功");

					// connection listener
					if (xmppManager.getConnectionListener() != null) {
						xmppManager.getConnection().addConnectionListener(xmppManager.getConnectionListener());
					}

					// packet filter
					PacketFilter packetFilter = new PacketTypeFilter(NotificationIQ.class);
					// packet listener
					PacketListener packetListener = xmppManager.getNotificationPacketListener();
					connection.addPacketListener(packetListener, packetFilter);

					getConnection().startKeepAliveThread();

					loginSuccess();
				} catch (XMPPException e) {
					LogUtil.e(TAG, "登录失败:XMPPException");
					String INVALID_CREDENTIALS_ERROR_CODE = "401";
					String errorMessage = e.getMessage();
					if (errorMessage != null && errorMessage.contains(INVALID_CREDENTIALS_ERROR_CODE)) {
						xmppManager.reregisterAccount();
						return;
					}
					xmppManager.startReconnectionThread();

				} catch (Exception e) {
					LogUtil.e(TAG, "登录失败:Exception");
					Log.e(TAG, "LoginTask.run()... other error");
					Log.e(TAG, "Failed to login to xmpp server. Caused by: " + e.getMessage());
					xmppManager.startReconnectionThread();
				} finally {
					xmppManager.runTask();
				}

			} else {
				Log.i(TAG, "Logged in already");
				LogUtil.e(TAG, "已经登录");
				xmppManager.runTask();
				loginSuccess();
			}

		}
	}

	private void loginSuccess() {
		if (loginListener != null) {
			loginListener.loginSuccess();
			loginListener = null;
		}
	}

	private interface LoginListener {
		void loginSuccess();
	}

}
