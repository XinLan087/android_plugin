package com.apkplugin.android.receiver;

import java.io.File;
import java.lang.reflect.Method;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.view.LayoutInflater;

import com.apkplugin.android.PluginManager;
import com.apkplugin.android.model.PluginReceiver;
import com.apkplugin.android.proxy.ClassLoaderAdapter;
import com.apkplugin.android.utils.PluginLog;

/**
 * 广播代理启动类
 * 
 * @author XinLan
 */
public class ProxyBrocastReceiver extends BroadcastReceiver {

	private AssetManager mAssetManager;
	private Resources mResources;
	private Theme mTheme;
	

	@Override
	public void onReceive(Context context, Intent intent) {
		String TAG = "ProxyBrocastReceiver:onReceive";
		PluginLog.d(TAG, "receiver action " + intent.getAction());
		// 查找所有的插件中的和此Intent相关的Receiver，进行调用onReceiver方法
		List<PluginReceiver> list = PluginManager
				.queryReceiver(context, intent);
		if (list == null || list.isEmpty()) {
			return;
		}
		for (PluginReceiver pluginReceiver : list) {
			callPluginReceiver(context, pluginReceiver, intent);
		}
	}

	protected void loadResources(Context context, String mDexPath) {
		try {
			AssetManager assetManager = AssetManager.class.newInstance();
			Method addAssetPath = assetManager.getClass().getMethod(
					"addAssetPath", String.class);
			addAssetPath.invoke(assetManager, mDexPath);
			mAssetManager = assetManager;
		} catch (Exception e) {
			e.printStackTrace();
		}
		Resources superRes = context.getResources();
		mResources = new Resources(mAssetManager, superRes.getDisplayMetrics(),
				superRes.getConfiguration());
		mTheme = mResources.newTheme();
		mTheme.setTo(context.getTheme());
	}

	/**
	 * 调用插件中的receiver
	 * 
	 * @param pluginReceiver
	 */
	private void callPluginReceiver(Context context,
			PluginReceiver pluginReceiver, Intent intent) {
		if (pluginReceiver == null) {
			return;
		}
		String TAG = "ProxyBrocastReceiver:callPluginReceiver";
		String path = pluginReceiver.getApkPath();
		if (path == null || !new File(path).exists()) {
			PluginLog.d(TAG, "apk path is null or file not exsit,return");
			return;
		}
		
		ClassLoader classLoader = ClassLoaderAdapter.getPluginClassLoader(
				context, context.getClassLoader(), path);
		if (classLoader != null) {
			loadResources(context, path);
			try {
				Class clazz = classLoader.loadClass(pluginReceiver.getName());
				if (clazz != null) {
					Object o = clazz.newInstance();
					if (o instanceof IReceiverLife) {
						IReceiverLife gg = (IReceiverLife) o;
						// 设置代理，调用onReceiver方法
						gg.setProxy(this, path);
					/*	PluginContext pluginContext = new PluginContext(context, 0, path, classLoader);
						Smith<Context> mBase = new Smith<Context>(context, "mBase");
						mBase.set(pluginContext);
						Smith<LayoutInflater> mInflater = new Smith<LayoutInflater>(mBase, "mInflater");*/
						//mInflater.set(getLayoutInflater(context, path));
						//ContextThemeWrapper contextThemeWrapper =new ContextThemeWrapper(context, 0);
						gg.onReceive(context, intent);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public LayoutInflater getLayoutInflater(Context context, String apkPath) {
		return ClassLoaderAdapter.getLayoutInflater(
				context,
				ClassLoaderAdapter.getPluginClassLoader(context,
						context.getClassLoader(), apkPath));
	}
}
