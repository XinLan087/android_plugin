package com.apkplugin.android.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.content.Intent;

import com.apkplugin.android.utils.ApkFileParser;
import com.apkplugin.android.utils.PluginLog;
import com.apkplugin.android.utils.ProxyActionUtil;

/**
 * 插件APK 所有信息集合，通过{@link ApkFileParser}解析获得
 * 
 * @author wangbx
 * 
 */
/**
 * @author wangbx
 * 
 */
public class PluginApkInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1372263157965638468L;

	/**
	 * 普通的JAR包，启动类key
	 */
	private static final String PLUGIN_JAR_MAIN_CLASS = "plugin.jar.main.class";

	/**
	 * 插件类型 ： 普通的APK文件
	 */
	public static final int PLUGIN_TYPE_APK = 1;
	/**
	 * 插件类型：普通的JAR包，不含有主入口
	 */
	public static final int PLUIN_TYPE_JAR = 2;

	/**
	 * META DATA表
	 */
	private HashMap<String, String> metaDatas = new HashMap<String, String>();

	/**
	 * 插件类型
	 */
	private int pluginType = 0;
	/**
	 * 文件的MD5
	 */
	private String fileMd5 = null;
	/**
	 * 插件路径
	 */
	private String path = null;
	/**
	 * 包名
	 */
	private String packageName = null;
	/**
	 * 版本
	 */
	private String versionName = null;

	/**
	 * DEX load后的so目录
	 */
	private String destSODir = null;

	/**
	 * DEX load后的目录
	 */
	private String destDexDir = null;

	/**
	 * 版本识别码
	 */
	private int versionCode = 0;
	/**
	 * 插件的名称
	 */
	private String appName;
	/**
	 * 插件中的Activity对象集合
	 */
	private ArrayList<PluginActivity> activities = new ArrayList<PluginActivity>();
	/**
	 * 插件中的Receiver对象集合
	 */
	private ArrayList<PluginReceiver> receivers = new ArrayList<PluginReceiver>();
	/**
	 * 插件中的Service对象集合
	 */
	private ArrayList<PluginService> services = new ArrayList<PluginService>();

	/**
	 * 增加一个Activity对象进来
	 * 
	 * @param activity
	 */
	public void addActivity(PluginActivity activity) {
		if (activities == null) {
			activities = new ArrayList<PluginActivity>();
		}
		activities.add(activity);
	}

	/**
	 * 增加一个Service进来
	 * 
	 * @param pluginService
	 */
	public void addService(PluginService pluginService) {
		if (services == null) {
			services = new ArrayList<PluginService>();
		}
		services.add(pluginService);
	}

	/**
	 * 从插件中取得主插件Activity的类名
	 * 
	 * @return
	 */
	public String getMainClassName() {
		List<String> name = getClassNameByActionAndCategory(Intent.ACTION_MAIN,
				Intent.CATEGORY_LAUNCHER);
		if (name != null && !name.isEmpty()) {
			return name.get(0);
		}
		return null;
	}

	/**
	 * 通过action和category查找类名
	 * 
	 * @param action1
	 * @param category1
	 * @return
	 */
	public List<String> getClassNameByActionAndCategory(String action1,
			String category1) {
		List<PluginActivity> activities = searchPluginActivityByActionAndCategory(
				action1, category1);
		if (activities != null) {
			ArrayList<String> strings = new ArrayList<String>();
			for (PluginActivity activity : activities) {
				String name = activity.name;
				strings.add(name);
			}
			return strings;
		}
		return null;
	}

	/**
	 * 通过action和category查找对应的PluginActivity对象
	 * 
	 * @param action1
	 * @param category1
	 * @return
	 */
	protected List<PluginActivity> searchPluginActivityByActionAndCategory(
			String action1, String category1) {
		String tag = "PluginApkInfo:searchPluginActivityByActionAndCategory";
		PluginLog.d(tag, "search activity by action: " + action1
				+ ", category:" + category1);
		Intent i = new Intent(action1);
		i.addCategory(category1);
		return queryActivityByIntent(i);
	}

	/**
	 * 通过Intent查找到对应的activity
	 * 
	 * @param intent
	 * @return
	 */
	public List<PluginActivity> queryActivityByIntent(Intent intent) {
		if (intent == null) {
			return null;
		}
		String tag = "PluginApkInfo:queryActivityByIntent";
		long time = System.currentTimeMillis();

		// 要返回的信息集合
		ArrayList<PluginActivity> returnActivities = new ArrayList<PluginActivity>();
		for (PluginActivity activity : this.activities) {
			ArrayList<PluginIntentFilter2> arrayList = activity
					.getIntentFilter2();
			if (arrayList == null) {
				continue;
			}
			for (PluginIntentFilter2 intentFilter : arrayList) {
				boolean r = intentFilter.matchIntent(intent.getAction(),
						intent.getType(), intent.getScheme(), intent.getData(),
						intent.getCategories());
				if (r) {
					// 设置类绝对路径
					String name = activity.name;
					if (name.startsWith(".")) {
						name = packageName + name;
						activity.name = name;
					}
					returnActivities.add(activity);
				}
			}
		}
		PluginLog.d(tag,
				"search Activity fail, time is " + PluginLog.countTime(time));
		return returnActivities;

	}

	/**
	 * 增加一个Receiver
	 * 
	 * @param receiver
	 */
	public void addReceiver(PluginReceiver receiver) {
		if (receivers == null) {
			receivers = new ArrayList<PluginReceiver>();
		}
		receivers.add(receiver);
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
		destSODir = "playso_" + packageName;
		destDexDir = "playdex_" + packageName;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public int getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public int getPluginType() {
		return pluginType;
	}

	public void setPluginType(int pluginType) {
		this.pluginType = pluginType;
	}

	public String dump() {
		StringBuffer buffer = new StringBuffer();

		buffer.append(this).append(" ").append(getClass().getName())
				.append(" ").append("appName=").append(appName)
				.append(";path=").append(path).append(";packageName=")
				.append(packageName).append(";versionCode=")
				.append(versionCode).append(";versionName=")
				.append(versionName);
		buffer.append("\nActivity[");
		for (PluginActivity a : activities) {
			buffer.append(a.dump());
		}
		buffer.append("]");
		buffer.append("\nService[");
		for (PluginService a : services) {
			buffer.append(a.dump());
		}
		buffer.append("]");
		buffer.append("\nReceiver[");
		for (PluginReceiver a : receivers) {
			buffer.append(a.dump());
		}
		buffer.append("]");
		return buffer.toString();
	}

	/**
	 * 增加一个meta-data
	 * 
	 * @param name
	 *            对应的android:name
	 * @param value
	 *            对应的android:value
	 */
	public void addMetaData(String name, String value) {
		if (metaDatas == null) {
			metaDatas = new HashMap<String, String>();
		}
		if (name == null) {
			return;
		}
		metaDatas.put(name, value);
	}

	/**
	 * 如果是jar包的插件，取得启动类
	 * 
	 * @return
	 */
	public String getPluginJarStartMainClass() {
		return getMetaData(PLUGIN_JAR_MAIN_CLASS);
	}

	/**
	 * 通过名称取得meta-data值
	 * 
	 * @param name
	 *            android:name
	 * @return
	 */
	public String getMetaData(String name) {
		if (metaDatas == null) {
			return null;
		}
		if (metaDatas.containsKey(name)) {
			return metaDatas.get(name);
		}
		return null;
	}

	public String getDestSODir() {
		return destSODir;
	}

	public void setDestSODir(String destSODir) {
		this.destSODir = destSODir;
	}

	public String getDestDexDir() {
		return destDexDir;
	}

	public void setDestDexDir(String destDexDir) {
		this.destDexDir = destDexDir;
	}

	public String getFileMd5() {
		return fileMd5;
	}

	public void setFileMd5(String fileMd5) {
		this.fileMd5 = fileMd5;
	}
}
