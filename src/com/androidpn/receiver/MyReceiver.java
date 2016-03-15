package com.androidpn.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		// Toast.makeText(context, intent.getAction(), 1).show();
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mobileInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		NetworkInfo wifiInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		NetworkInfo activeInfo = manager.getActiveNetworkInfo();
		// 如果无网络连接activeInfo为null
		if (activeInfo == null) {
			Toast.makeText(context, "mobile:" + mobileInfo.isConnected() + "\n" + "wifi:" + wifiInfo.isConnected() + "\n", 1).show();
		} else {
			Toast.makeText(
					context,
					"mobile:" + mobileInfo.isConnected() + "\n" + "wifi:" + wifiInfo.isConnected() + "\n" + "active:"
							+ activeInfo.getTypeName(), 1).show();
		}
	}

}
