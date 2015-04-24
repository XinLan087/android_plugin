package com.apkplugin.android.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ProxyBrocastReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		// 查找所有的插件中的和此Intent相关的Receiver，进行调用onReceiver方法
	}
}
