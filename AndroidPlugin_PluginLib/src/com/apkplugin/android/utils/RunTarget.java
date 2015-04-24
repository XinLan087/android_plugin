package com.apkplugin.android.utils;

import java.util.List;

import com.apkplugin.android.PluginManager;
import com.apkplugin.android.model.PluginActivity;
import com.apkplugin.android.model.PluginService;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;

/**
 * 运行目标程序 <br/>
 * 1、插件如果启动宿主的service、activity等只能用action 进行启动 <br/>
 * 2、插件启动自己的activity、service，可以通过action和class name <br/>
 * 3、机制遵循 Android 本身的 Intent 4、如果一个action对应的组件，存在于插件和宿主，以宿主优先 5、
 * 
 * @author wangbx
 * 
 */
public class RunTarget {

	public static final String EXTRAL_CLASS_NAME = "class_name";

	/**
	 * 运行代理activity
	 * 
	 * @param proxyContext
	 *            代理activity或者context
	 * @param path
	 *            插件路径
	 * @param className
	 *            类名
	 * @param bundle
	 *            数据
	 * @return
	 */
	public static boolean startActivityByProxy(Context proxyContext,
			String path, String className, Bundle bundle) {
		try {
			Intent i = ProxyActionUtil.getInstance(proxyContext)
					.getStartActivityIntent(proxyContext, path, className,
							bundle);
			proxyContext.startActivity(i);
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	/**
	 * 运行插件activity，并且返回result code
	 * 
	 * @param proxyContext
	 *            代理的activity
	 * @param path
	 *            插件路径
	 * @param className
	 *            类名
	 * @param bundle
	 *            数据
	 * @param requestCode
	 *            请求码
	 * @return
	 */
	public static boolean startActivityForResultByProxy(Activity proxyContext,
			String path, String className, Bundle bundle, int requestCode) {
		try {
			Intent intent = ProxyActionUtil.getInstance(proxyContext)
					.getStartActivityIntent(proxyContext, path, className,
							bundle);
			proxyContext.startActivityForResult(intent, requestCode);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 通过代理启动service
	 * 
	 * @param context
	 * @param path
	 * @param className
	 * @param bundle
	 * @return
	 */
	public static ComponentName startServiceByProxy(Context context,
			String path, String className, Bundle bundle) {
		try {
			Intent i = ProxyActionUtil.getInstance(context)
					.getStartServiceIntent(context, path, className, bundle);
			if (i == null) {
				return null;
			}
			return context.startService(i);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 绑定服务
	 * 
	 * @param context
	 * @param path
	 * @param className
	 * @param bundle
	 * @param conn
	 * @param flags
	 * @return
	 */
	public static boolean bindServiceByProxy(Context context, String path,
			String className, Bundle bundle, ServiceConnection conn, int flags) {
		try {
			Intent i = ProxyActionUtil.getInstance(context)
					.getStartServiceIntent(context, path, className, bundle);
			if (i == null) {
				return false;
			}
			return context.bindService(i, conn, flags);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 启动activity
	 * 
	 * @param intent
	 * @param context
	 * @param dexPath
	 * @return
	 */
	public static boolean startActivity(Intent intent, Context context,
			String dexPath) {
		String TAG = "RunTarget:startActivity";
		List<ResolveInfo> list = context.getPackageManager()
				.queryIntentActivities(intent,
						PackageManager.GET_INTENT_FILTERS);
		if (list != null && !list.isEmpty()) {
			PluginLog.d(TAG,
					"start activity ,query host, activity in host, start it");
			context.startActivity(intent);
		} else {
			String className = intent.getStringExtra(EXTRAL_CLASS_NAME);
			PluginLog.d(TAG,
					"start activity ,get class name from intent ,className:"
							+ className);
			// 看看是否可以到class name
			if (className == null || "".equals(className)) {
				ComponentName componentName = intent.getComponent();
				if (componentName != null) {
					className = componentName.getClassName();
				}
				PluginLog.d(TAG,
						"start activity ,get class name from intent ComponentName ,className:"
								+ className);
			}
			// 从intent取不到类名，利用查找的方式进行
			if (className == null || "".equals(className)) {
				List<PluginActivity> pls = PluginManager.queryActivities(
						context, intent);
				if (pls != null && !pls.isEmpty()) {
					PluginActivity activity = pls.get(0);
					return PluginManager.startPluginActivity(context, activity,
							intent);
				} else {
					return false;
				}
			} else {
				return startActivityByProxy(context, dexPath, className,
						intent.getExtras());
			}
		}
		return false;
	}

	/**
	 * 启动界面，并且返回request code
	 * 
	 * @param intent
	 * @param requestCode
	 * @param context
	 * @param dexPath
	 * @return
	 */
	public static boolean startActivityForResult(Intent intent,
			int requestCode, Activity context, String dexPath) {
		String TAG = "RunTarget:startActivityForResult";
		List<ResolveInfo> list = context.getPackageManager()
				.queryIntentActivities(intent,
						PackageManager.GET_INTENT_FILTERS);
		if (list != null && !list.isEmpty()) {
			PluginLog.d(TAG,
					"start activity ,query host, activity in host, start it");
			context.startActivityForResult(intent, requestCode);
		} else {
			String className = intent.getStringExtra(EXTRAL_CLASS_NAME);
			PluginLog.d(TAG,
					"start activity ,get class name from intent ,className:"
							+ className);
			// 看看是否可以到class name
			if (className == null || "".equals(className)) {
				ComponentName componentName = intent.getComponent();
				if (componentName != null) {
					className = componentName.getClassName();
				}
				PluginLog.d(TAG,
						"start activity ,get class name from intent ComponentName ,className:"
								+ className);
			}
			// 从intent取不到类名，利用查找的方式进行
			if (className == null || "".equals(className)) {
				List<PluginActivity> pls = PluginManager.queryActivities(
						context, intent);
				if (pls != null && !pls.isEmpty()) {
					PluginActivity activity = pls.get(0);
					return PluginManager.startPluginActivityForResult(context,
							activity, intent, requestCode);
				} else {
					return false;
				}
			} else {
				return startActivityForResultByProxy(context, dexPath,
						className, intent.getExtras(), requestCode);
			}
		}
		return false;
	}

	/**
	 * 启动service
	 * 
	 * @param intent
	 * @param context
	 * @param mDexPath
	 * @return
	 */
	public static ComponentName startService(Intent intent, Context context,
			String mDexPath) {
		String TAG = "RunTarget:startService";
		List<ResolveInfo> list = context.getPackageManager()
				.queryIntentServices(intent, PackageManager.GET_INTENT_FILTERS);
		if (list != null && !list.isEmpty()) {
			PluginLog
					.d(TAG, "start service , service config in host, start it");
			return context.startService(intent);
		} else {
			String className = intent.getStringExtra(EXTRAL_CLASS_NAME);
			PluginLog.d(TAG,
					"start service ,get class name from intent ,className:"
							+ className);
			// 看看是否可以到class name
			if (className == null || "".equals(className)) {
				ComponentName componentName = intent.getComponent();
				if (componentName != null) {
					className = componentName.getClassName();
				}
				PluginLog.d(TAG,
						"start service ,get class name from intent ComponentName ,className:"
								+ className);
			}// 从intent取不到类名，利用查找的方式进行
			if (className == null || "".equals(className)) {
				List<PluginService> pls = PluginManager.queryService(context,
						intent);
				if (pls != null && !pls.isEmpty()) {
					PluginService service = pls.get(0);
					return PluginManager.startPluginService(context, service,
							intent);
				} else {
					return null;
				}
			} else {
				return startServiceByProxy(context, mDexPath, className,
						intent.getExtras());
			}
		}
	}

	/**
	 * 绑定service
	 * 
	 * @param intent
	 * @param context
	 * @param conn
	 * @param flags
	 * @param mDexPath
	 * @return
	 */

	public static boolean bindService(Intent intent, Context context,
			ServiceConnection conn, int flags, String mDexPath) {
		String TAG = "RunTarget:bindService";
		List<ResolveInfo> list = context.getPackageManager()
				.queryIntentServices(intent, PackageManager.GET_INTENT_FILTERS);
		if (list != null && !list.isEmpty()) {
			PluginLog
					.d(TAG, "bind service , service config in host, start it");
			return context.bindService(intent, conn, flags);
		} else {
			String className = intent.getStringExtra(EXTRAL_CLASS_NAME);
			PluginLog.d(TAG,
					"bind service ,get class name from intent ,className:"
							+ className);
			// 看看是否可以到class name
			if (className == null || "".equals(className)) {
				ComponentName componentName = intent.getComponent();
				if (componentName != null) {
					className = componentName.getClassName();
				}
				PluginLog.d(TAG,
						"bind service ,get class name from intent ComponentName ,className:"
								+ className);
			}// 从intent取不到类名，利用查找的方式进行
			if (className == null || "".equals(className)) {
				List<PluginService> pls = PluginManager.queryService(context,
						intent);
				if (pls != null && !pls.isEmpty()) {
					PluginService service = pls.get(0);
					return PluginManager.bindPluginService(context, service,
							intent, conn, flags);
				} else {
					return false;
				}
			} else {
				return bindServiceByProxy(context, mDexPath, className,
						intent.getExtras(), conn, flags);
			}
		}
	}
}
