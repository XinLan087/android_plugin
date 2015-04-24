package com.apkplugin.android.service;

import java.lang.reflect.Constructor;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.IBinder;
import android.view.LayoutInflater;

import com.apkplugin.android.constanst.PluginConst;
import com.apkplugin.android.proxy.ClassLoaderAdapter;
import com.apkplugin.android.proxy.ThemeCacheInfo;
import com.apkplugin.android.utils.ProxyActionUtil;

/**
 * 代理service运行类
 * 
 * @author XinLan
 * 
 */
public class BaseProxyService extends Service {

	private String mClass;

	private String mDexPath = null;

	/**
	 * 生命周期代理器
	 */
	protected IServiceLife mPluginService;

	private ThemeCacheInfo cacheInfo = null;

	/**
	 * 是否初始化过
	 */
	private boolean isInited = false;

	@Override
	public IBinder onBind(Intent intent) {
		init(intent);
		return mPluginService.onBind(intent);
	}

	private void init(Intent itFromApp) {
		// 已经初始化过了
		if (isInited) {
			return;
		}

		mClass = itFromApp.getStringExtra(PluginConst.EXTRA_CLASS);
		mDexPath = itFromApp.getStringExtra(PluginConst.EXTRA_DEX_PATH);
		cacheInfo = ThemeCacheInfo.getThemeCacheInfo(getApplicationContext(),
				mDexPath);
		ProxyActionUtil.getInstance(getApplicationContext())
				.addRuningProxyService(mDexPath, mClass, this);
		Object instance = null;
		try {
			Class<?> serviceClass;
			serviceClass = this.getClassLoader().loadClass(mClass);
			Constructor<?> serviceConstructor = serviceClass
					.getConstructor(new Class[] {});
			instance = serviceConstructor.newInstance(new Object[] {});
		} catch (Exception e) {
		}
		setRemoteService(instance);
		mPluginService.setProxy(this, mDexPath);
		mPluginService.onCreate();
	}

	/**
	 * 保留一份插件Service对象
	 */
	protected void setRemoteService(Object service) {
		if (service instanceof IServiceLife) {
			mPluginService = (IServiceLife) service;
			isInited = true;
		} else {
			throw new ClassCastException(
					"plugin service must implements IServiceLife");
		}
	}

	@Override
	public ClassLoader getClassLoader() {
		return ClassLoaderAdapter.getPluginClassLoader(getApplicationContext(),
				super.getClassLoader(), mDexPath);
	}

	public LayoutInflater getLayoutInflater(Context context, String apkPath) {
		return ClassLoaderAdapter.getLayoutInflater(
				context,
				ClassLoaderAdapter.getPluginClassLoader(context,
						context.getClassLoader(), apkPath));
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		init(intent);
		if (mPluginService != null) {
			mPluginService.onStartCommand(intent, flags, startId);
		}
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		if (mPluginService != null) {
			mPluginService.onDestroy();
		}
		super.onDestroy();
		ProxyActionUtil.getInstance(getApplicationContext())
				.removeRuningProxyService(mDexPath, mClass);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		if (mPluginService != null) {
			mPluginService.onConfigurationChanged(newConfig);
		}
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onLowMemory() {
		if (mPluginService != null) {
			mPluginService.onLowMemory();
		}
		super.onLowMemory();
	}

	@Override
	public void onTrimMemory(int level) {
		if (mPluginService != null) {
			mPluginService.onTrimMemory(level);
		}
		super.onTrimMemory(level);
	}

	@Override
	public boolean onUnbind(Intent intent) {
		if (mPluginService != null) {
			return mPluginService.onUnbind(intent);
		}
		return super.onUnbind(intent);
	}

	@Override
	public void onRebind(Intent intent) {
		if (mPluginService != null)
			mPluginService.onRebind(intent);
		super.onRebind(intent);
	}

	@Override
	public void onTaskRemoved(Intent rootIntent) {
		if (mPluginService != null)
			mPluginService.onTaskRemoved(rootIntent);
		super.onTaskRemoved(rootIntent);
	}

	@Override
	public void setTheme(int resid) {
		cacheInfo.setTheme(getApplicationContext(), resid);
	}

	@Override
	public Resources getResources() {
		return cacheInfo.getResources(getApplicationContext());
	}

	@Override
	public AssetManager getAssets() {
		return cacheInfo.getAssets(getApplicationContext());
	}

}
