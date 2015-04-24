package com.apkplugin.android;

import java.io.File;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.content.Intent;

import com.apkplugin.android.callback.InstallPluginCallBack;
import com.apkplugin.android.callback.InstalledPluginInfo;
import com.apkplugin.android.callback.LoadingPluginCallBack;
import com.apkplugin.android.callback.UnInstallPluginCallBack;
import com.apkplugin.android.model.PluginActivity;
import com.apkplugin.android.model.PluginApkInfo;
import com.apkplugin.android.utils.HostClassLoader;
import com.apkplugin.android.utils.PluginCache;
import com.apkplugin.android.utils.PluginInfoSWUtil;
import com.apkplugin.android.utils.PluginLog;

/**
 * 对外部的接口，此类不混淆
 * 
 * @author wangbx
 */
public class PluginManager {
	public static void setLogState(boolean r) {
		PluginLog.setLogState(r);
	}

	/**
	 * 启动插件中的某个Intent，可以用于插件 查询插件activity，如果查询多个，只会启动第一个 pluginIntent
	 * 用于查询插件中activity的intent，不是查询宿主的activity，
	 * 
	 * @param context
	 * @param pluginIntent
	 * @return
	 */
	public static boolean startPluginIntentActivity(Context context,
			Intent pluginIntent) {
		return PluginCache.getInstance(context).startPluginIntentActivity(
				pluginIntent);
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
	public static boolean startPluginIntentActivity(Context context,
			Intent pluginIntent, String apkPath) {
		return PluginCache.getInstance(context).startPluginIntentActivity(
				pluginIntent, apkPath);
	}

	/**
	 * 查询匹配到此intent上的插件Activity
	 * 
	 * @param intent
	 *            要匹配的Intent
	 * @return
	 */
	public List<PluginActivity> queryActivities(Context context, Intent intent) {
		return PluginCache.getInstance(context).queryActivities(intent);
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
	public List<PluginActivity> queryActivities(Context context, Intent intent,
			String apkPath) {
		return PluginCache.getInstance(context)
				.queryActivities(intent, apkPath);
	}

	/**
	 * 取得所有已经安装的插件信息集合
	 * 
	 * @param context
	 * @return
	 */
	public static List<InstalledPluginInfo> getAllInstalledPlugin(
			Context context) {
		return PluginInfoSWUtil.getInstance(context)
				.getAllInstalledPluginInfo();
	}

	/**
	 * 载入所有已经安装过的插件
	 * 
	 * @param context
	 * @param loadingPluginCallBack
	 *            载入回调函数
	 */
	public static void loadingAllPlugin(Context context,
			LoadingPluginCallBack loadingPluginCallBack) {
		String TAG = "PluginManager:loadingAllPlugin";
		// TODO 载入所有插件
		if (loadingPluginCallBack != null) {
			loadingPluginCallBack.onPreLoading();
		}
		Set<String> list = PluginInfoSWUtil.getInstance(context)
				.getPluginPackageNameList();
		if (list != null) {
			for (String packageName : list) {
				String path = PluginInfoSWUtil.getInstance(context)
						.getFilePathByPackageName(packageName);
				PluginLog.d(TAG, "loading plugin " + packageName + " " + path);
				if (path == null || !new File(path).exists()) {
					PluginLog.d(TAG, path + " not exsit");
					if (loadingPluginCallBack != null) {
						loadingPluginCallBack.onLoadingError(-1, "插件文件不存在");
					}
				} else {
					// 将插件载入到内存中
					PluginCache.getInstance(context).loadPlugin(path);
					if (loadingPluginCallBack != null) {
						loadingPluginCallBack.onLoadingSuccess(packageName);
					}
				}
			}
		}

	}

	/**
	 * 使用包名来启动某个插件，先查找此插件是否安装，然后进行启动
	 * 
	 * @param packageName
	 *            包名
	 * @return
	 */
	public static boolean startPluginWithPackageName(Context context,
			String packageName) {
		// TODO 用包名启动插件
		String path = PluginInfoSWUtil.getInstance(context)
				.getFilePathByPackageName(packageName);
		if (path == null) {
			return false;
		}
		return startPlugin(context, path);
	}

	/**
	 * 是否安装过，如果版本相同，则表示安装过，否则表示未安装过
	 * 
	 * @param context
	 *            上下文
	 * @param path
	 *            插件的路径
	 * @return
	 */
	public static boolean isInstalled(Context context, String path) {
		String TAG = "PluginConnect:isInstalled";
		File ff = new File(path);
		if (!ff.exists()) {
			return false;
		}
		PluginApkInfo apkInfo = PluginCache.getInstance(context)
				.getTempPluginApkInfo(path);
		if (apkInfo == null) {
			return false;
		}
		PluginInfoSWUtil infoSWUtil = PluginInfoSWUtil.getInstance(context);
		boolean r = infoSWUtil.isPluginInstalled(apkInfo.getPackageName());
		return r;
	}

	/**
	 * 启动插件
	 * 
	 * @param context
	 * @param apkPath
	 *            插件路径
	 */
	public static boolean startPlugin(Context context, String apkPath) {
		if (!isInstalled(context, apkPath)) {
			installPlugin(context, apkPath, null);
		}
		return PluginCache.getInstance(context).startPlugin(apkPath);
	}

	/**
	 * 安装插件
	 * 
	 * @param context
	 *            上下文
	 * @param path
	 *            插件路径
	 * @param installPluginCallBack
	 *            安装结果回调函数
	 */
	public static void installPlugin(Context context, String path,
			InstallPluginCallBack installPluginCallBack) {
		if (installPluginCallBack != null) {
			installPluginCallBack.onPreInstall();
		}
		PluginApkInfo apkInfo = PluginCache.getInstance(context)
				.getTempPluginApkInfo(path);
		if (apkInfo == null) {
			installPluginCallBack.onInstallError(-2, "无法解析插件");
			return;
		}
		PluginInfoSWUtil infoSWUtil = PluginInfoSWUtil.getInstance(context);
		// 判断是否安装过
		boolean bb = infoSWUtil.isPluginInstalled(apkInfo.getPackageName());
		if (apkInfo != null) {
			// 初始化一下DexClassLoader
			HostClassLoader.getClassLoader(path, context,
					context.getClassLoader());
			// 写入到文件中去
			boolean result = infoSWUtil.savePluginInfo(apkInfo);
			if (installPluginCallBack != null) {
				if (result) {
					// bb 安装过就是更新，否则就是安装
					installPluginCallBack.onInstallSuccess(bb);
				} else {
					installPluginCallBack.onInstallError(-1, "无法安装插件");
				}
			}
		}
	}

	/**
	 * 卸载插件，先安装包名查找，如果找不到，安装路径查找
	 * 
	 * @param context
	 * @param packageName
	 *            包名
	 * @param uninstallPluginCallBack
	 *            卸载完成回调
	 */
	public static void uninstallPlugin(Context context, String packageName,
			UnInstallPluginCallBack uninstallPluginCallBack) {
		if (uninstallPluginCallBack != null) {
			uninstallPluginCallBack.onPreUnInstall();
		}

		if (packageName == null || "".equals(packageName.trim())) {
			if (uninstallPluginCallBack != null) {
				uninstallPluginCallBack.onUnInstallError(-1, "要卸载的包名为空");
			}
			return;
		}
		PluginInfoSWUtil.getInstance(context).removePluginInfo(packageName);
		if (uninstallPluginCallBack != null) {
			uninstallPluginCallBack.onUnInstallSuccess(packageName);
		}

	}

}
