package com.apkplugin.android.proxy;

import java.lang.reflect.Constructor;

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
import android.view.WindowManager.LayoutParams;
import android.widget.Toast;

import com.apkplugin.android.activity.IActivityLife;
import com.apkplugin.android.constanst.PluginConst;
import com.apkplugin.android.model.PluginApkInfo;
import com.apkplugin.android.utils.CompatibleUtil;
import com.apkplugin.android.utils.PluginCache;

/**
 * 基础代理类，所有插件的Activity都是通过这个Activity进行启动，并且加载资源
 * 
 * @author XinLan
 */
public abstract class BaseProxyActivity extends Activity {

	private static final String TAG = "BaseProxyActivity";

	/**
	 * 启动的类名,真实的类名
	 */
	private String mRealClass;
	/**
	 * APK路径
	 */
	private String mDexPath;

	/**
	 * 当前运行的Activity对象生命周期模拟接口
	 */
	protected IActivityLife mRemoteActivity;

	private ThemeCacheInfo cacheInfo = null;

	/**
	 * 取得自定义的LayoutInflater
	 * 
	 * @return
	 */
	public LayoutInflater getLayoutInflater() {
		if (mDexPath == null) {
			return super.getLayoutInflater();
		}
		return ClassLoaderAdapter.getLayoutInflater(this, getClassLoader());
	}

	@Override
	public void setTheme(int resid) {
		if (cacheInfo != null) {
			cacheInfo.setTheme(this, resid);
		} else {
			super.setTheme(resid);
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 取得APK路径和类路径
		mDexPath = getIntent().getStringExtra(PluginConst.EXTRA_DEX_PATH);
		mRealClass = getIntent().getStringExtra(PluginConst.EXTRA_CLASS);

		Log.d(TAG, "mClass=" + mRealClass + " mDexPath=" + mDexPath);
		cacheInfo = ThemeCacheInfo.getThemeCacheInfo(this, mDexPath);
		CompatibleUtil.fixActionBarBug(this);
		if (mRealClass == null) {
			launchTargetActivity();
		} else {
			launchTargetActivity(mRealClass);
		}
	}
	/**
	 * 取得真实运行的类名
	 * 
	 * @return
	 */
	public String getRealClass() {
		if(mRealClass==null){
			return this.getClass().getName();
		}
		return mRealClass;
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
		try {
			Class<?> localClass = getClassLoader().loadClass(className);
			Constructor<?> localConstructor = localClass
					.getConstructor(new Class[] {});
			Object instance = localConstructor.newInstance(new Object[] {});
			setRemoteActivity(instance);

			// 设置代理类
			mRemoteActivity.setProxy(this, mDexPath);
			Bundle bundle = new Bundle(getIntent().getExtras());
			bundle.putInt(PluginConst.FROM, PluginConst.FROM_EXTERNAL);
			// 调用onCreate方法
			mRemoteActivity.setIntent(getIntent());
			mRemoteActivity.onCreate(bundle);
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG + "launchTargetActivity", "启动插件失败:" + mDexPath + " "
					+ mRealClass);
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
		if (cacheInfo != null) {
			return cacheInfo.getAssets(this);
		} else {
			return super.getAssets();
		}
	}

	@Override
	public Resources getResources() {
		if (cacheInfo != null) {
			return cacheInfo.getResources(this);
		} else {
			return super.getResources();
		}
	}

	@Override
	public Theme getTheme() {
		if (cacheInfo != null) {
			return cacheInfo.getTheme(this);
		} else {
			return super.getTheme();
		}
	}

	@Override
	public ClassLoader getClassLoader() {
		return ClassLoaderAdapter.getPluginClassLoader(this,
				super.getClassLoader(), mDexPath);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (mRemoteActivity != null) {
			mRemoteActivity.onActivityResult(requestCode, resultCode, data);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onStart() {
		if (mRemoteActivity != null) {
			mRemoteActivity.onStart();
		}
		super.onStart();
	}

	@Override
	protected void onRestart() {
		if (mRemoteActivity != null) {
			mRemoteActivity.onRestart();
		}
		super.onRestart();
	}

	@Override
	protected void onResume() {
		if (mRemoteActivity != null) {
			mRemoteActivity.onResume();
		}
		super.onResume();
	}

	@Override
	protected void onPause() {
		if (mRemoteActivity != null) {
			mRemoteActivity.onPause();
		}
		super.onPause();
	}

	@Override
	protected void onStop() {
		if (mRemoteActivity != null) {
			mRemoteActivity.onStop();
		}
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		if (mRemoteActivity != null) {
			mRemoteActivity.onDestroy();
		}
		super.onDestroy();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		if (mRemoteActivity != null) {
			mRemoteActivity.onSaveInstanceState(outState);
		}
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		if (mRemoteActivity != null) {
			mRemoteActivity.onRestoreInstanceState(savedInstanceState);
		}
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		if (mRemoteActivity != null) {
			mRemoteActivity.onNewIntent(intent);
		}
		super.onNewIntent(intent);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);
		if (mRemoteActivity != null) {
			return mRemoteActivity.onTouchEvent(event);
		} else {
			return false;
		}
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		super.onKeyUp(keyCode, event);
		if (mRemoteActivity != null) {
			return mRemoteActivity.onKeyUp(keyCode, event);
		} else {
			return false;
		}
	}

	@Override
	public void onWindowAttributesChanged(LayoutParams params) {
		if (mRemoteActivity != null) {
			mRemoteActivity.onWindowAttributesChanged(params);
		}
		super.onWindowAttributesChanged(params);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		if (mRemoteActivity != null) {
			mRemoteActivity.onWindowFocusChanged(hasFocus);
		}
		super.onWindowFocusChanged(hasFocus);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		if (mRemoteActivity != null) {
			mRemoteActivity.onConfigurationChanged(newConfig);
		}
		super.onConfigurationChanged(newConfig);
	}
	
	

}
