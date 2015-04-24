package com.apkplugin.android.proxy;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.Toast;

import com.apkplugin.android.activity.IActivityLife;
import com.apkplugin.android.constanst.PluginConst;
import com.apkplugin.android.model.PluginApkInfo;
import com.apkplugin.android.utils.HostClassLoader;
import com.apkplugin.android.utils.PluginCache;

/**
 * 基础代理类，所有插件的Activity都是通过这个Activity进行启动，并且加载资源
 * 
 * @author wangbx
 */
public abstract class BaseProxyActivity extends Activity {

	private static final String TAG = "BaseProxyActivity";

	/**
	 * 启动的类名
	 */
	private String mClass;
	/**
	 * APK路径
	 */
	private String mDexPath;

	private AssetManager mAssetManager;
	private Resources mResources;
	private Theme mTheme;
	private ClassLoader mLocalClassLoader;

	/**
	 * 当前运行的Activity对象生命周期模拟接口
	 */
	protected IActivityLife mRemoteActivity;

	/**
	 * 载入资源
	 */
	protected void loadResources() {
		try {
			AssetManager assetManager = AssetManager.class.newInstance();
			Method addAssetPath = assetManager.getClass().getMethod(
					"addAssetPath", String.class);
			addAssetPath.invoke(assetManager, mDexPath);
			mAssetManager = assetManager;
		} catch (Exception e) {
			e.printStackTrace();
		}
		Resources superRes = super.getResources();
		mResources = new Resources(mAssetManager, superRes.getDisplayMetrics(),
				superRes.getConfiguration());
		mTheme = mResources.newTheme();
		mTheme.setTo(super.getTheme());
	}

	/**
	 * 取得自定义的LayoutInflater
	 * 
	 * @return
	 */
	public LayoutInflater getLayoutInflater() {
		if(mDexPath==null){
			return super.getLayoutInflater();
		}
		return ClassLoaderAdapter.getLayoutInflater(this, getClassLoader());

	}

	@Override
	public void setTheme(int resid) {
		if (mTheme == null) {
			mTheme = getResources().newTheme();
			Resources.Theme theme = super.getTheme();
			if (theme != null) {
				mTheme.setTo(theme);
			}
		}
		mTheme.applyStyle(resid, true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 取得APK路径和类路径
		mDexPath = getIntent().getStringExtra(PluginConst.EXTRA_DEX_PATH);
		mClass = getIntent().getStringExtra(PluginConst.EXTRA_CLASS);
		
		Log.d(TAG, "mClass=" + mClass + " mDexPath=" + mDexPath);

		loadResources();
		if (mClass == null) {
			launchTargetActivity();
		} else {
			launchTargetActivity(mClass);
		}
	}

	/**
	 * 运行目标Activity
	 */
	protected void launchTargetActivity() {
		PluginApkInfo apkInfo = PluginCache
				.getInstance(getApplicationContext())
				.getPluginApkInfo(mDexPath);
		String mClass = null;
		if (apkInfo != null) {
			mClass = apkInfo.getMainClassName();
		}
		if (mClass != null) {
			launchTargetActivity(mClass);
		} else {
			Toast.makeText(this, "未找到插件的入口", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 运行类名
	 * 
	 * @param className
	 */
	protected void launchTargetActivity(final String className) {
		Log.d(TAG, "start launchTargetActivity, className=" + className);
		if (mLocalClassLoader == null) {
			mLocalClassLoader = HostClassLoader.getClassLoader(mDexPath,
					BaseProxyActivity.this, getClassLoader());
		}
		try {
			Class<?> localClass = mLocalClassLoader.loadClass(className);
			Constructor<?> localConstructor = localClass
					.getConstructor(new Class[] {});
			Object instance = localConstructor.newInstance(new Object[] {});
			setRemoteActivity(instance);
			Log.d(TAG, "instance = " + instance);

			// 设置代理类
			mRemoteActivity.setProxy(this, mDexPath);
			Bundle bundle = new Bundle(getIntent().getExtras());
			bundle.putInt(PluginConst.FROM, PluginConst.FROM_EXTERNAL);
			// 调用onCreate方法
			mRemoteActivity.setIntent(getIntent());
			mRemoteActivity.onCreate(bundle);
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(this, "启动插件失败", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 设置远程插件当前运行的Activity，进行转型为{@link IActivityLife}
	 * 
	 * @param activity
	 */
	protected void setRemoteActivity(Object activity) {
		mRemoteActivity = (IActivityLife) activity;
	}

	@Override
	public AssetManager getAssets() {
		return mAssetManager == null ? super.getAssets() : mAssetManager;
	}

	@Override
	public Resources getResources() {
		return mResources == null ? super.getResources() : mResources;
	}

	@Override
	public Theme getTheme() {
		return mTheme == null ? super.getTheme() : mTheme;
	}

	@Override
	public ClassLoader getClassLoader() {
		return ClassLoaderAdapter.getActivityClassLoader(this,
				super.getClassLoader(), mDexPath);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		mRemoteActivity.onActivityResult(requestCode, resultCode, data);
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onStart() {
		mRemoteActivity.onStart();
		super.onStart();
	}

	@Override
	protected void onRestart() {
		mRemoteActivity.onRestart();
		super.onRestart();
	}

	@Override
	protected void onResume() {
		mRemoteActivity.onResume();
		super.onResume();
	}

	@Override
	protected void onPause() {
		mRemoteActivity.onPause();
		super.onPause();
	}

	@Override
	protected void onStop() {
		mRemoteActivity.onStop();
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		mRemoteActivity.onDestroy();
		super.onDestroy();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		mRemoteActivity.onSaveInstanceState(outState);
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		mRemoteActivity.onRestoreInstanceState(savedInstanceState);
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		mRemoteActivity.onNewIntent(intent);
		super.onNewIntent(intent);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);
		return mRemoteActivity.onTouchEvent(event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		super.onKeyUp(keyCode, event);
		return mRemoteActivity.onKeyUp(keyCode, event);
	}

	@Override
	public void onWindowAttributesChanged(LayoutParams params) {
		mRemoteActivity.onWindowAttributesChanged(params);
		super.onWindowAttributesChanged(params);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		mRemoteActivity.onWindowFocusChanged(hasFocus);
		super.onWindowFocusChanged(hasFocus);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		mRemoteActivity.onConfigurationChanged(newConfig);
		super.onConfigurationChanged(newConfig);
	}

	/**
	 * 模拟{link ActivityGroup#getLocalActivityManager().startActivity(name,
	 * intent);}
	 * 
	 * @param intent
	 * @param key
	 * @return
	 */
	public View startPluginActivityView(Intent intent, String key) {
		throw new UnsupportedOperationException("未实现");
	}

}
