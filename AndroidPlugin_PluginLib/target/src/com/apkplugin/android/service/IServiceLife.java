package com.apkplugin.android.service;

import android.content.Intent;

public interface IServiceLife {

	public int onStartCommand(Intent intent, int flags, int startId);

	public void onCreate();

	public void onDestroy();
}
