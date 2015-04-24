package com.apkplugin.sample.mainhost;

import com.apkplugin.android.PluginManager;

import android.app.Application;

public class MyApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		PluginManager.setLogState(true);
		System.out.println("MyApplication.onCreate()");
	}
}
