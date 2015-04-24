package com.example.testndk;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.Looper;
import android.widget.RemoteViews;

import com.apkplugin.android.service.BasePluginService;
import com.apkplugin.android.view.PluginRemoteView;

public class MyServ extends BasePluginService {

	private Timer timer = null;
	private void showNotice(Context context) {
		System.out.println("MyReceiver2.showNotice() " + context);
		Notification.Builder builder = new Builder(context);
		
		builder.setContent(new PluginRemoteView(null, R.layout.noti_layout));
		builder.setContentText("tesa").setTicker("aaaaaaa")
				.setSmallIcon(R.drawable.tic_launcher);
		NotificationManager nm = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		// 发出状态栏通知
		// The first parameter is the unique ID for the Notification
		// and the second is the Notification object.
		nm.notify(111, builder.build());
	}
	public void onCreate() {
		super.onCreate();
		System.out.println("MyServ.onCreate()=====");
		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				Looper.prepare();
				Intent intent=new Intent();
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.setClassName(getProxyService(), ResultCodeActivity.class.getName());
				startActivity(intent);
				timer.cancel();
				stopSelfProxy();
			}
		}, 3000, 2000);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		System.out.println("MyServ.onStartCommand()====="+intent);
		return super.onStartCommand(intent, flags, startId);
	}
	
}
