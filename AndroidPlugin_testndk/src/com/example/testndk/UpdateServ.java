package com.example.testndk;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

import com.apkplugin.android.service.BasePluginService;

public class UpdateServ extends BasePluginService {

	public void onCreate() {
		super.onCreate();
		System.out.println("UpdateServ.onCreate()=====");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		System.out.println("UpdateServ.onStartCommand()=====" + intent);
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		System.out.println("onBind.....");
		IBinder result = null;
		if (null == result)
			result = new Binder();
		Toast.makeText(getApplicationContext(), "插件UpdateServ的getOnBind方法被调用", Toast.LENGTH_LONG).show();
		return result;
	}
	
	@Override
	public boolean onUnbind(Intent intent) {
		Toast.makeText(getApplicationContext(), "插件UpdateServ的onUnbind方法被调用", Toast.LENGTH_LONG).show();
		System.out.println("UpdateServ.onUnbind()===");
		return false;
	}
}
