package com.example.testndk;

import com.apkplugin.android.activity.BasePluginActivity;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends BasePluginActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main2);
		System.out.println("MainActivity.onCreate()" + new Good().testx());
	}

}

