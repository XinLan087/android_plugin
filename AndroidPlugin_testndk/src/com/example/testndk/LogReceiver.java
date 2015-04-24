package com.example.testndk;

import android.content.Context;
import android.content.Intent;

import com.apkplugin.android.receiver.BasePluginReceiver;

public class LogReceiver extends BasePluginReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		System.out.println("LogReceiver.onReceive() " + intent.getAction());
	}

}
