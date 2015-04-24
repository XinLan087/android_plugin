package com.example.testndk;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.apkplugin.android.activity.BasePluginActivity;

public class ResultCodeActivity extends BasePluginActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.resultcode);
		findViewById(R.id.button1).setOnClickListener(
				new View.OnClickListener() {
					public void onClick(View v) {
						Intent i = new Intent();
						i.putExtra("data", "test data");
						setResult(-1, i);
						finish();
						//finishActivity(8888, RESULT_OK, i);
					}
				});

	}
	
	@Override
	public String getProxyAction() {
		return "com.apkplugin.dynamicloadhost.single_task_activity.VIEW";
	}
}
