package com.apkplugin.android.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;

import com.apkplugin.android.jar.BasePluginJar;
import com.apkplugin.android.jar.JarContext;
import com.apkplugin.android.model.PluginActivity;
import com.apkplugin.android.model.PluginApkInfo;
import com.apkplugin.android.model.PluginReceiver;
import com.apkplugin.android.model.PluginService;

/**
 * 插件缓存管理者，专门管理插件
 * 
 * @author XinLan
 * 
 */
public class PluginCache {
	String TAG = "PluginCache";

	/**
	 * 插件信息表 key : package name
	 */
	public HashMap<String, PluginApkInfo> PLUGIN_APK_INFO_MAP = null;
	/**
	 * 临时信息存储表, 信息到缓存中，省得解析，节省时间
	 */
	private HashMap<String, PluginApkInfo> temp_apkinfo_map = null;
	public static PluginCache cache = null;

	/**
	 * 上下文
	 */
	public Context context;

	/**
	 * 构造函数，初始化
	 * 
	 * @param context
	 */
	private PluginCache(Context context) {
		this.context = context;
		PLUGIN_APK_INFO_MAP = new HashMap<String, PluginApkInfo>();
		temp_apkinfo_map = new HashMap<String, PluginApkInfo>();
		initAllPlugin();
	}

	/**
	 * 初始化所有插件
	 */
	private void initAllPlugin() {
		Set<String> list = PluginInfoSWUtil.getInstance(context)
				.getPluginPackageNameList();
		if (list != null) {
			for (String packageName : list) {
				String path = PluginInfoSWUtil.getInstance(context)
						.getFilePathByPackageName(packageName);
				PluginLog.d(TAG, "loading plugin " + packageName + " " + path);
				if (path == null || !new File(path).exists()) {
					PluginLog.d(TAG, path + " not exsit");
				} else {
					// 将插件载入到内存中
					getPluginApkInfo(path);
				}
			}
		}
	}

	/**
	 * 取得插件缓存实例
	 * 
	 * @param context
	 * @return
	 */
	public static PluginCache getInstance(Context context) {
		if (cache == null) {
			cache = new PluginCache(context);
		}
		// 重新设置Context
		cache.context = context;
		return cache;
	}

	/**
	 * 查询匹配到此intent上的插件Activity
	 * 
	 * @param intent
	 *            要匹配的Intent
	 * @param apkPath
	 *            固定在某个apk中查找
	 * 
	 * @return
	 */
	public List<PluginActivity> queryActivities(Intent intent, String apkPath) {
		if (apkPath == null) {
			return null;
		}
		return queryComponent(intent, apkPath, "activity");
	}

	/**
	 * 查询匹配到此intent上的插件Activity
	 * 
	 * @param intent
	 *            要匹配的Intent
	 * @return
	 */
	public List<PluginActivity> queryActivities(Intent intent) {
		return queryComponent(intent, null, "activity");
	}

	/**
	 * 通过Intent查询receiver
	 * 
	 * @param intent
	 * @return
	 */
	public List<PluginReceiver> queryReceivers(Intent intent) {
		return queryComponent(intent, null, "receiver");
	}

	/**
	 * 通过intent查找单个插件中的receiver
	 * 
	 * @param intent
	 * @param apkPath
	 * @return
	 */
	public List<PluginReceiver> queryReceivers(Intent intent, String apkPath) {
		if (apkPath == null) {
			return null;
		}
		return queryComponent(intent, apkPath, "receiver");
	}

	/**
	 * 查询service
	 * 
	 * @param intent
	 * @return
	 */
	public List<PluginService> queryServices(Intent intent) {
		return queryComponent(intent, null, "service");
	}

	/**
	 * 查询service
	 * 
	 * @param intent
	 * @return
	 */
	public List<PluginService> queryServices(Intent intent, String apkPath) {
		return queryComponent(intent, apkPath, "service");
	}

	/**
	 * 查询组件
	 * 
	 * @param intent
	 * @param path
	 * @param type
	 *            值为 service , activity,receiver
	 * @return
	 */
	private <T> List<T> queryComponent(Intent intent, String path, String type) {
		String TAG = "PluginCache:queryComponent";
		if (PLUGIN_APK_INFO_MAP == null || PLUGIN_APK_INFO_MAP.isEmpty()) {
			return null;
		}
		List<T> receivers = new ArrayList<T>();
		// 如果路径不为空，说明是在本路径下查询
		if (path != null && !"".equals(path)) {
			PluginApkInfo apkInfo = PLUGIN_APK_INFO_MAP.get(path);
			PluginLog.d(TAG, "search "+type+" in plugin " + path);
			if (apkInfo != null) {
				List<T> activities2 = apkInfo.queryComponetByIntent(intent,
						type);
				if (activities2 == null) {
					PluginLog.d(TAG, "search "+type+" result in  " + path
							+ " ,number is 0");
				} else {
					PluginLog.d(TAG, "search "+type+" result in  " + path
							+ " ,number is " + activities2.size());
					receivers.addAll(activities2);
				}
			}
		} else {
			// 在全部插件中查找
			Set<String> keys = PLUGIN_APK_INFO_MAP.keySet();
			for (String string : keys) {
				System.out.println("PluginCache.queryComponent() " +string);
				PluginApkInfo apkInfo = PLUGIN_APK_INFO_MAP.get(string);
				PluginLog.d(TAG, "search "+type+" in plugin " + string);
				if (apkInfo != null) {
					List<T> activities2 = apkInfo.queryComponetByIntent(intent,
							type);
					if (activities2 == null) {
						PluginLog.d(TAG, "search "+type+" result in  " + string
								+ " ,number is 0");
					} else {
						PluginLog.d(TAG, "search "+type+" result in  " + string
								+ " ,number is " + activities2.size());
						receivers.addAll(activities2);
					}
				}
			}
		}

		return receivers;
	}

	/**
	 * 启动某个apk插件中的Activity
	 * 
	 * @param pluginIntent
	 *            intent
	 * @param apkPath
	 *            插件路径
	 * @return
	 */
	public boolean startPluginIntentActivity(Intent pluginIntent, String apkPath) {
		List<PluginActivity> activities = queryActivities(pluginIntent, apkPath);
		if (activities != null && !activities.isEmpty()) {
			PluginActivity activity = activities.get(0);
			return startPluginActivity(activity, pluginIntent, false, 0);
		}
		return false;
	}

	public boolean startPluginIntentActivityForResult(Intent pluginIntent,
			String apkPath, int requestCode) {
		List<PluginActivity> activities = queryActivities(pluginIntent, apkPath);
		if (activities != null) {
			PluginActivity activity = activities.get(0);
			return startPluginActivity(activity, pluginIntent, false,
					requestCode);
		}
		return false;
	}

	/**
	 * 启动插件中的某个Intent，可以用于插件 查询插件activity，如果查询多个，只会启动第一个
	 * 
	 * @param pluginIntent
	 *            用于查询插件中activity的intent，不是查询宿主的activity，
	 * @return
	 */
	public boolean startPluginIntentActivity(Intent pluginIntent) {
		List<PluginActivity> activities = queryActivities(pluginIntent);
		if (activities != null) {
			PluginActivity activity = activities.get(0);
			return startPluginActivity(activity, pluginIntent, false, 0);
		}
		return false;
	}

	public boolean startPluginIntentActivityForResult(Intent pluginIntent,
			int requestCode) {
		List<PluginActivity> activities = queryActivities(pluginIntent);
		if (activities != null) {
			PluginActivity activity = activities.get(0);
			return startPluginActivity(activity, pluginIntent, false,
					requestCode);
		}
		return false;
	}

	/**
	 * 启动插件activity
	 * 
	 * @param activity
	 *            {@link PluginActivity}
	 * @param dataIntent
	 *            数据传输intent
	 * 
	 * @return 失败或者成功
	 */
	public boolean startPluginActivity(PluginActivity activity,
			Intent dataIntent, boolean hasRequestCode, int requestCode) {
		if (activity == null) {
			return false;
		}
		String apkPath = activity.getApkPath();
		Intent i = ProxyActionUtil.getInstance(context).getProxyActivityIntent(
				apkPath, activity.getName());
		if (i != null && dataIntent != null) {
			Bundle bundle = dataIntent.getExtras();
			if (bundle != null) {
				// 设置原始数据
				i.putExtras(bundle);
			}
			i.setDataAndType(dataIntent.getData(), dataIntent.getType());
		}
		try {
			// 如果含有是for result的，调用startActivityForResult
			if (hasRequestCode && context instanceof Activity) {
				Activity aa = (Activity) context;
				aa.startActivityForResult(i, requestCode);
			} else {
				context.startActivity(i);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 启动插件中的service
	 * 
	 * @param service
	 * @param dataIntent
	 * @return
	 */
	public ComponentName startPluginService(PluginService service,
			Intent dataIntent) {
		if (service == null) {
			return null;
		}
		String apkPath = service.getApkPath();
		Intent i = ProxyActionUtil.getInstance(context).getProxyServiceIntent(
				apkPath, service.getName());
		if (i != null && dataIntent != null) {
			Bundle bundle = dataIntent.getExtras();
			if (bundle != null) {
				// 设置原始数据
				i.putExtras(bundle);
			}
			i.setDataAndType(dataIntent.getData(), dataIntent.getType());
		}
		try {
			return context.startService(i);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 绑定到插件的Service中
	 * 
	 * @param service
	 * @param dataIntent
	 * @param conn
	 * @param flags
	 * @return
	 */
	public boolean bindPluginService(PluginService service, Intent dataIntent,
			ServiceConnection conn, int flags) {
		if (service == null) {
			return false;
		}
		String apkPath = service.getApkPath();
		Intent i = ProxyActionUtil.getInstance(context).getProxyServiceIntent(
				apkPath, service.getName());
		if (i != null && dataIntent != null) {
			Bundle bundle = dataIntent.getExtras();
			if (bundle != null) {
				// 设置原始数据
				i.putExtras(bundle);
			}
			i.setDataAndType(dataIntent.getData(), dataIntent.getType());
		}
		try {
			return context.bindService(i, conn, flags);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 启动某个插件
	 * 
	 * @param apkPath
	 * @return
	 */
	public boolean startPlugin(String apkPath) {
		PluginApkInfo apkInfo = getPluginApkInfo(apkPath);
		if (apkInfo == null) {
			return false;
		}
		Intent pluginIntent = new Intent(Intent.ACTION_MAIN);
		pluginIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		// 启动activity
		boolean r = startPluginIntentActivity(pluginIntent, apkPath);
		// 启动失败，尝试JAR插件模式
		if (!r) {
			String className = apkInfo.getPluginJarStartMainClass();
			if (className != null && !"".equals(className.trim())) {
				// TODO 启动jar
				return startJar(className, apkPath);
			}
		}
		return true;
	}

	/**
	 * 启动一个JAR包
	 * 
	 * @param className
	 * @return
	 */
	private boolean startJar(String className, String apkPath) {
		try {
			HostClassLoader classLoader = HostClassLoader.getClassLoader(
					apkPath, context, context.getClassLoader());
			Object cls = classLoader.loadClass(className).newInstance();
			BasePluginJar basePluginJar = (BasePluginJar) cls;
			// 设置插件路径
			basePluginJar.setContext(new JarContext(apkPath, context));
			basePluginJar.setApkPath(apkPath);
			basePluginJar.startJar();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 通过路径初始化插件信息，此方法会正式将插件信息存放到可用表中，如果不准备启动插件，不调用此方法
	 * 
	 * @param path
	 *            插件路径
	 * @return
	 */
	public PluginApkInfo getPluginApkInfo(String path) {
		if (PLUGIN_APK_INFO_MAP.containsKey(path)) {
			return PLUGIN_APK_INFO_MAP.get(path);
		}
		// 从临时表中查找
		if (temp_apkinfo_map.containsKey(path)) {
			PluginApkInfo apkInfo = temp_apkinfo_map.get(path);
			temp_apkinfo_map.remove(path);
			putPluginApkInfo(apkInfo);
			return apkInfo;
		}
		ApkFileParser apkFileParser = new ApkFileParser(context, path);
		PluginApkInfo apkInfo = apkFileParser.parse();
		if (apkInfo != null) {
			PLUGIN_APK_INFO_MAP.put(path, apkInfo);
		}
		ProxyActionUtil.getInstance(context).initHostClassInAndroidManifest();
		return apkInfo;
	}

	/**
	 * 预载入插件
	 * 
	 * @param path
	 */
	public void loadPlugin(String path) {
		getPluginApkInfo(path);
		HostClassLoader.getClassLoader(path, context, context.getClassLoader());
	}

	/**
	 * 取得临时的 插件信息，放到临时表中
	 * 
	 * @param path
	 * @return
	 */
	public PluginApkInfo getTempPluginApkInfo(String path) {
		if (PLUGIN_APK_INFO_MAP.containsKey(path)) {
			return PLUGIN_APK_INFO_MAP.get(path);
		}
		// 从临时表中查找
		if (temp_apkinfo_map.containsKey(path)) {
			PluginApkInfo apkInfo = temp_apkinfo_map.get(path);
			temp_apkinfo_map.remove(path);
			putPluginApkInfo(apkInfo);
			return apkInfo;
		}
		ApkFileParser apkFileParser = new ApkFileParser(context, path);
		PluginApkInfo apkInfo = apkFileParser.parse();
		if (apkInfo != null) {
			temp_apkinfo_map.put(path, apkInfo);
		}
		return apkInfo;
	}

	public void removePluginApkInfo(String packageName) {

	}

	/**
	 * 将PluginApkInfo 存放到临时表中
	 * 
	 * @param apkInfo
	 */
	public void putTempTable(PluginApkInfo apkInfo) {
		if (apkInfo != null) {
			temp_apkinfo_map.put(apkInfo.getPath(), apkInfo);
		}
	}

	/**
	 * 存入plugin apk info
	 * 
	 * @param apkInfo
	 */
	public void putPluginApkInfo(PluginApkInfo apkInfo) {
		PLUGIN_APK_INFO_MAP.put(apkInfo.getPath(), apkInfo);
		ProxyActionUtil.getInstance(context).initHostClassInAndroidManifest();
	}

}
