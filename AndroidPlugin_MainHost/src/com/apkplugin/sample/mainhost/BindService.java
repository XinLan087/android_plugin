package com.apkplugin.sample.mainhost;

import java.io.FileDescriptor;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.IBinder.DeathRecipient;
import android.widget.Toast;

public class BindService extends Service {
	@Override
	public void onCreate() {
		System.out.println("BindService.onCreate()");
		super.onCreate();
	}

	@Override
	public IBinder onBind(Intent intent) {
		System.out.println("BindService.onBind()");
		Toast.makeText(getApplicationContext(), "宿主的onBind方法被调用", Toast.LENGTH_LONG).show();
		return new Binder();
	}
	
	@Override
	public boolean onUnbind(Intent intent) {
		Toast.makeText(getApplicationContext(), "宿主的onUnbind方法被调用", Toast.LENGTH_LONG).show();
		return super.onUnbind(intent);
	}

}
