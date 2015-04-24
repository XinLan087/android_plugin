package com.apkplugin.android.service;

import android.app.Service;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.IBinder;

public interface IServiceLife {
	public IBinder onBind(Intent intent);

	/**
	 * 取得对应的Proxy类的action
	 * 
	 * @return
	 */
	public String getProxyAction();

	public void onCreate();

	public int onStartCommand(Intent intent, int flags, int startId);

	public void onDestroy();

	public void onConfigurationChanged(Configuration newConfig);

	public void onLowMemory();

	public void onTrimMemory(int level);

	public boolean onUnbind(Intent intent);

	public void onRebind(Intent intent);

	public void onTaskRemoved(Intent rootIntent);

	/**
	 * 设置托管Service，并将that指针指向那个托管的Service
	 * 
	 * @param proxyActivity
	 * @param dexPath
	 */
	public void setProxy(Service proxyService, String dexPath);
}
