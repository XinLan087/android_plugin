package com.apkplugin.android.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.os.IBinder;

import com.apkplugin.android.utils.RunTarget;

/**
 * 插件化的service，继承此类
 * 
 * @author XinLan
 * 
 */
public abstract class BasePluginService extends Service implements IServiceLife {

	protected String mDexPath = null;

	/**
	 * that指针指向的是当前插件的Context（由于是插件化开发，this指针绝对不能使用）
	 */
	protected Service proxyService; // 替代this指针

	public BasePluginService() {
		proxyService = this;
	}

	public Service getProxyService() {
		return proxyService;
	}

	@Override
	public String getProxyAction() {
		return "";
	}

	@Override
	public void setProxy(Service proxyService, String dexPath) {
		this.proxyService = proxyService;
		this.mDexPath = dexPath;
	}

	@Override
	public void startActivity(Intent intent) {
		if (proxyService == this) {
			super.startActivity(intent);
		} else {
			RunTarget.startActivity(intent, getProxyService(), mDexPath);
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	
	@Override
	public void onCreate() {
		if (proxyService == this) {
			super.onCreate();
		}
	}

	/**
	 * 停止的时候，请调用此函数
	 */
	public void stopSelfProxy() {
		if (proxyService == this) {
			stopSelf();
		} else {
			proxyService.stopSelf();
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (proxyService == this) {
			return super.onStartCommand(intent, flags, startId);
		} else {
			return 0;
		}
	}

	@Override
	public ComponentName startService(Intent intent) {
		if (proxyService == this) {
			return super.startService(intent);
		}
		return RunTarget.startService(intent, getProxyService(), mDexPath);
	}

	@Override
	public void onDestroy() {
		if (proxyService == this) {
			super.onDestroy();
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		if (proxyService == this) {
			super.onConfigurationChanged(newConfig);
		}
	}

	@Override
	public void onLowMemory() {
		if (proxyService == this) {
			super.onLowMemory();
		}
	}

	@Override
	public void onTrimMemory(int level) {
		if (proxyService == this) {
			super.onTrimMemory(level);
		}
	}

	@Override
	public boolean onUnbind(Intent intent) {
		if (proxyService == this) {
			return super.onUnbind(intent);
		} else {
			return false;
		}
	}

	@Override
	public void onRebind(Intent intent) {
		if (proxyService == this) {
			super.onRebind(intent);
		}
	}

	@Override
	public void onTaskRemoved(Intent rootIntent) {
		if (proxyService == this) {
			super.onTaskRemoved(rootIntent);
		}
	}

	@Override
	public AssetManager getAssets() {
		if (proxyService == this) {
			return super.getAssets();
		} else {
			return proxyService.getAssets();
		}
	}

	@Override
	public Theme getTheme() {
		if (proxyService == this) {
			return super.getTheme();
		} else {
			return proxyService.getTheme();
		}
	}

	@Override
	public Resources getResources() {
		if (proxyService == this) {
			return super.getResources();
		} else {
			return proxyService.getResources();
		}

	}

	@Override
	public Context getApplicationContext() {
		if (proxyService == this) {
			return super.getApplicationContext();
		} else {
			return proxyService.getApplicationContext();
		}
	}
	
	


}
