package com.apkplugin.android.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public interface IReceiverLife {
	public void onReceive(Context context, Intent intent);

	public void setProxy(BroadcastReceiver proxyReceiver, String dexPath);

}
