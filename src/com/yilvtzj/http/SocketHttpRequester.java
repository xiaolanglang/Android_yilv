package com.yilvtzj.http;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.Map;

import android.content.Context;

import com.yilvtzj.util.AccountUtil;
import com.yilvtzj.util.StringUtil;

public class SocketHttpRequester {
	private SocketListener socketListener;

	public SocketListener getSocketListener() {
		return socketListener;
	}

	public SocketHttpRequester setSocketListener(SocketListener socketListener) {
		this.socketListener = socketListener;
		return this;
	}

	/**
	 * 
	 * 多文件上传 直接通过HTTP协议提交数据到服务器,实现如下面表单提交功能:<br>
	 * <FORM METHOD=POST
	 * ACTION="http://192.168.1.101:8083/upload/servlet/UploadServlet"
	 * enctype="multipart/form-data"> <br>
	 * <INPUT TYPE="text" NAME="name"> <br>
	 * <INPUT TYPE="text" NAME="id"> <br>
	 * <input type="file" name="imagefile"/> <br>
	 * <input type="file" name="zip"/> <br>
	 * </FORM> <br>
	 * 
	 * @param path
	 *            上传路径(注：避免使用localhost或127.0.0.1这样的路径测试，因为它会指向手机模拟器，你可以使用http://
	 *            www.iteye.cn或http://192.168.1.101:8083这样的路径测试)
	 * @param params
	 *            请求参数 key为参数名,value为参数值
	 * @param file
	 *            上传文件
	 * @return
	 */
	public SocketHttpRequester post(Context context, String path, Map<String, String> params, FormFile[] files)
			throws Exception {
		long t1 = System.currentTimeMillis();

		final String BOUNDARY = "---------------------------7da2137580612"; // 数据分隔线
		final String endline = "--" + BOUNDARY + "--\r\n";// 数据结束标志
		int fileDataLength = 0;

		if (files != null) {
			for (FormFile uploadFile : files) {// 得到文件类型数据的总长度
				StringBuilder fileExplain = new StringBuilder();
				fileExplain.append("--");
				fileExplain.append(BOUNDARY);
				fileExplain.append("\r\n");
				fileExplain.append("Content-Disposition: form-data;name=\"" + uploadFile.getParameterName()
						+ "\";filename=\"" + uploadFile.getFilname() + "\"\r\n");
				fileExplain.append("Content-Type: " + uploadFile.getContentType() + "\r\n\r\n");
				fileExplain.append("\r\n");
				fileDataLength += fileExplain.length();
				if (uploadFile.getInStream() != null) {
					fileDataLength += uploadFile.getFile().length();
				} else {
					fileDataLength += uploadFile.getData().length;
				}
			}
		}
		StringBuilder textEntity = new StringBuilder();
		if (params != null) {
			for (Map.Entry<String, String> entry : params.entrySet()) {// 构造文本类型参数的实体数据
				textEntity.append("--");
				textEntity.append(BOUNDARY);
				textEntity.append("\r\n");
				textEntity.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"\r\n\r\n");
				textEntity.append(entry.getValue());
				textEntity.append("\r\n");
			}
		}
		// 计算传输给服务器的实体数据总长度
		int dataLength = textEntity.toString().getBytes().length + fileDataLength + endline.getBytes().length;

		URL url = new URL(path);
		int port = url.getPort() == -1 ? 80 : url.getPort();
		Socket socket = new Socket(InetAddress.getByName(url.getHost()), port);
		OutputStream outStream = socket.getOutputStream();
		// 下面完成HTTP请求头的发送
		String requestmethod = "POST " + url.getPath() + " HTTP/1.1\r\n";
		outStream.write(requestmethod.getBytes());
		String accept = "Accept: image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*\r\n";
		outStream.write(accept.getBytes());
		String language = "Accept-Language: zh-CN\r\n";
		outStream.write(language.getBytes());
		String contenttype = "Content-Type: multipart/form-data; boundary=" + BOUNDARY + "\r\n";
		outStream.write(contenttype.getBytes());
		String contentlength = "Content-Length: " + dataLength + "\r\n";
		outStream.write(contentlength.getBytes());
		String alive = "Connection: Keep-Alive\r\n";
		outStream.write(alive.getBytes());
		String cookie = "Cookie: " + AccountUtil.getCookie(context) + "\r\n";
		outStream.write(cookie.getBytes());
		String host = "Host: " + url.getHost() + ":" + port + "\r\n";
		outStream.write(host.getBytes());
		// 写完HTTP请求头后根据HTTP协议再写一个回车换行
		outStream.write("\r\n".getBytes());
		// 把所有文本类型的实体数据发送出来
		outStream.write(textEntity.toString().getBytes());
		if (files != null) {
			// 把所有文件类型的实体数据发送出来
			for (FormFile uploadFile : files) {
				StringBuilder fileEntity = new StringBuilder();
				fileEntity.append("--");
				fileEntity.append(BOUNDARY);
				fileEntity.append("\r\n");
				fileEntity.append("Content-Disposition: form-data;name=\"" + uploadFile.getParameterName()
						+ "\";filename=\"" + uploadFile.getFilname() + "\"\r\n");
				fileEntity.append("Content-Type: " + uploadFile.getContentType() + "\r\n\r\n");
				outStream.write(fileEntity.toString().getBytes());
				if (uploadFile.getInStream() != null) {
					byte[] buffer = new byte[1024];
					int len = 0;
					while ((len = uploadFile.getInStream().read(buffer, 0, 1024)) != -1) {
						outStream.write(buffer, 0, len);
					}
					uploadFile.getInStream().close();
				} else {
					outStream.write(uploadFile.getData(), 0, uploadFile.getData().length);
				}
				outStream.write("\r\n".getBytes());
			}
		}
		// 下面发送数据结束标志，表示数据已经结束
		outStream.write(endline.getBytes());
		long t2 = System.currentTimeMillis();
		System.out.println(">>>>>>>>>>>>>>拼装结束，耗时：" + (t2 - t1) + "ms");

		BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		StringBuilder builder = new StringBuilder();
		String str = reader.readLine();
		int i = 0;
		while (str != null) {
			if (i == 2) {
				break;
			}
			builder.append(str);
			str = reader.readLine();
			System.out.println("?????????????????[" + str + "]");
			if (StringUtil.isEmpty(str)) {
				i++;
			}
		}
		long t3 = System.currentTimeMillis();
		System.out.println(">>>>>>>>>>>>>>读取数据，耗时：" + (t3 - t2) + "ms");

		outStream.flush();
		outStream.close();
		socket.close();
		reader.close();

		str = builder.toString();
		int first = str.indexOf("{");
		int last = str.lastIndexOf("}");
		if (first != -1 && last != -1) {
			str = str.substring(first, last + 1);
		} else {
			str = "{500:\"failed\"}";
		}
		long t4 = System.currentTimeMillis();
		System.out.println(">>>>>>>>>>>>>>截取json部分，耗时：" + (t4 - t3) + "ms");

		if (socketListener != null) {
			socketListener.result(str);
		}

		return this;
	}

	/**
	 * 单文件上传 提交数据到服务器
	 * 
	 * @param path
	 *            上传路径(注：避免使用localhost或127.0.0.1这样的路径测试，因为它会指向手机模拟器，你可以使用http://
	 *            www.itcast.cn或http://192.168.1.10:8080这样的路径测试)
	 * @param params
	 *            请求参数 key为参数名,value为参数值
	 * @param file
	 *            上传文件
	 * @return
	 */
	public SocketHttpRequester post(String path, Context context, Map<String, String> params, FormFile file)
			throws Exception {
		return post(context, path, params, new FormFile[] { file });
	}

	public SocketHttpRequester post(String path, Context context, Map<String, String> params) throws Exception {
		return post(context, path, params, null);
	}

	public interface SocketListener {
		void result(String JSON);
	}
}
