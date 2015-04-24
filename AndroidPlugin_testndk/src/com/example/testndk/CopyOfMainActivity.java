package com.example.testndk;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;

import com.apkplugin.android.activity.BasePluginActivity;

public class CopyOfMainActivity extends BasePluginActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		findViewById(R.id.button1).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						startActivity(new Intent("test_host_activity"));

					}
				});

		findViewById(R.id.Button02).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent i = new Intent();
						i.setClassName(getRealActivity(),
								MainActivity.class.getName());
						startActivity(i);

					}
				});
		findViewById(R.id.Button03).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent i = new Intent();
						i.putExtra("class_name", MainActivity.class.getName());
						startActivity(i);
					}
				});
		findViewById(R.id.Button04).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent();
						intent.setClassName(getRealActivity(),
								ResultCodeActivity.class.getName());
						startActivityForResult(intent, 8888);
					}
				});
		findViewById(R.id.Button05).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent();
						intent.setClassName(getRealActivity(),
								MyServ.class.getName());
						startService(intent);
					}
				});

		findViewById(R.id.Button06).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent();
						intent.setClassName(getRealActivity(),
								UpdateServ.class.getName());
						startService(intent);
					}
				});
		findViewById(R.id.Button07).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent();
						intent.setClassName(getRealActivity(),
								UpdateServ.class.getName());
						bindService((intent), conn, Context.BIND_AUTO_CREATE);
					}
				});
		findViewById(R.id.Button08).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if (cname != null)
							unbindService(conn);
					}
				});

		findViewById(R.id.Button09).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent("bindservice");
						bindService(intent, conn, Context.BIND_AUTO_CREATE);
					}
				});

	}

	ComponentName cname;
	private ServiceConnection conn = new ServiceConnection() {
		/** 获取服务对象时的操作 */
		public void onServiceConnected(ComponentName name, IBinder service) {
			cname = name;
			System.out
					.println("CopyOfMainActivity.conn.new ServiceConnection() {...}.onServiceConnected()"
							+ cname);
		}

		/** 无法获取到服务对象时的操作 */
		public void onServiceDisconnected(ComponentName name) {
			cname = null;
			System.out
					.println("CopyOfMainActivity.conn.new ServiceConnection() {...}.onServiceDisconnected()");
		}

	};

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		System.out.println("CopyOfMainActivity.onActivityResult()requestCode："
				+ requestCode + " resultCode: " + resultCode + " data:" + data);
	}
}
