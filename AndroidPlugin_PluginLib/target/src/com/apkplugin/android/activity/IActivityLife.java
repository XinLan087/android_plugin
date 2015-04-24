package com.apkplugin.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.WindowManager.LayoutParams;

/**
 * Activity 生命周期模拟接口
 * 
 * @author wangbx
 */
public interface IActivityLife {

	/**
	 * 取得对应的Proxy类的action
	 * 
	 * @return
	 */
	public String getProxyAction();

	public void onStart();

	public void onRestart();

	public void onActivityResult(int requestCode, int resultCode, Intent data);

	public void onResume();

	public void onPause();

	public void onStop();

	public void onDestroy();
	
	public void setIntent(Intent newIntent);

	public void onCreate(Bundle savedInstanceState);

	/**
	 * 设置代理的Activity，控制其生命周期
	 * 
	 * @param proxyActivity
	 *            代理Activity
	 * @param dexPath
	 *            dex路径
	 */
	public void setProxy(Activity proxyActivity, String dexPath);

	public void onSaveInstanceState(Bundle outState);

	public void onNewIntent(Intent intent);

	public void onRestoreInstanceState(Bundle savedInstanceState);

	public boolean onTouchEvent(MotionEvent event);

	public boolean onKeyUp(int keyCode, KeyEvent event);

	public void onWindowAttributesChanged(LayoutParams params);

	public void onWindowFocusChanged(boolean hasFocus);

	public void onConfigurationChanged(Configuration newConfig);
}
