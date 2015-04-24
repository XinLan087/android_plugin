package com.apkplugin.android.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.apkplugin.android.PluginManager;

/**
 * 插件广播类，插件中的广播需要继承此类
 * 
 * @author XinLan
 * 
 */
public abstract class BasePluginReceiver extends BroadcastReceiver implements
		IReceiverLife {

	public String mDexPath;
	public BroadcastReceiver proxyReceiver;

	@Override
	public abstract void onReceive(Context context, Intent intent);

	@Override
	public void setProxy(BroadcastReceiver proxyReceiver, String dexPath) {
		this.proxyReceiver = proxyReceiver;
		this.mDexPath = dexPath;
	}

	/**
	 * 启动activity
	 * 
	 * @param context
	 * @param intent
	 */
	public void startActivity(Context context, Intent intent) {
		if (proxyReceiver == this) {
			context.startActivity(intent);
		} else {
			PluginManager.startPluginIntentActivity(context, intent, mDexPath);
		}
	}

}
