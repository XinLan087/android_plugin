package com.apkplugin.android.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import android.content.Context;

import com.apkplugin.android.callback.InstalledPluginInfo;
import com.apkplugin.android.model.PluginApkInfo;

/**
 * 插件信息操作类，用于保存，读取等
 * 
 * @author XinLan
 * 
 */
public class PluginInfoSWUtil {
	public static final String DIR = "pplugin";
	private Context context;
	private static PluginInfoSWUtil infoSWUtil = null;
	/**
	 * 已经安装插件信息表
	 */
	private HashMap<String, Properties> infoMap = null;

	public static PluginInfoSWUtil getInstance(Context context) {
		if (infoSWUtil == null) {
			infoSWUtil = new PluginInfoSWUtil(context);
		}
		return infoSWUtil;

	}

	private PluginInfoSWUtil(Context context) {
		this.context = context;
		infoMap = new HashMap<String, Properties>();
		initAllProperties();
	}

	/**
	 * 取得包名列表
	 * 
	 * @return
	 */
	public Set<String> getPluginPackageNameList() {
		if (infoMap != null) {
			return infoMap.keySet();
		}
		return null;
	}

	/**
	 * 取得插件的路径集合列表
	 * 
	 * @return
	 */
	public List<String> getPluginPathList() {
		if (infoMap != null) {
			Collection<Properties> collection = infoMap.values();
			if (collection != null) {
				ArrayList<String> arrayList = new ArrayList<String>();
				for (Properties properties : collection) {
					arrayList.add(properties.getProperty("path"));
				}
				return arrayList;
			}
		}
		return null;
	}

	/**
	 * 初始化所有信息
	 */
	private void initAllProperties() {
		String TAG = "PluginInfoSWUtil:initAllProperties";
		File dirF = context.getDir(DIR, Context.MODE_WORLD_WRITEABLE
				+ Context.MODE_WORLD_READABLE);
		if (!dirF.exists()) {
			return;
		}
		File[] fs = dirF.listFiles();
		if (fs == null) {
			return;
		}
		for (File file : fs) {
			Properties properties = new Properties();
			FileInputStream nef = null;
			try {
				nef = new FileInputStream(file);
				properties.load(nef);
				PluginLog.d(TAG,
						"load plugin info from file :" + file.getName());
				infoMap.put(file.getName(), properties);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 通过包名取得文件的路径
	 * 
	 * @param packageName
	 *            包名
	 * @return
	 */
	public String getFilePathByPackageName(String packageName) {
		if (packageName == null) {
			return null;
		}
		if (infoMap.containsKey(packageName)) {
			Properties properties = infoMap.get(packageName);
			if (properties != null) {
				return properties.getProperty("path");
			}
		}
		return null;
	}

	/**
	 * 取得所有安装的插件集合
	 * 
	 * @return
	 */
	public List<InstalledPluginInfo> getAllInstalledPluginInfo() {
		if (infoMap == null || infoMap.isEmpty()) {
			return null;
		}

		Set<String> ss = infoMap.keySet();
		List<InstalledPluginInfo> list = new ArrayList<InstalledPluginInfo>();
		for (String string : ss) {
			Properties pp = infoMap.get(string);
			InstalledPluginInfo info = new InstalledPluginInfo();
			info.setPath(pp.getProperty("path"));
			info.setDex_dir(pp.getProperty("dex_dir"));
			info.setFile_md5(pp.getProperty("file_md5"));
			info.setSo_dir(pp.getProperty("so_dir"));
			info.setVersion_name(pp.getProperty("version_name"));
			info.setPackage_name(pp.getProperty("package_name"));
			try {
				String versionCode = pp.getProperty("version_code");
				info.setVersion_code(Integer.parseInt(versionCode));
			} catch (Exception e) {
			}
			list.add(info);
		}
		return list;

	}

	/**
	 * 保存插件信息
	 * 
	 * @param apkInfo
	 *            插件信息
	 * @return 返回1 ，代表正常安装，返回2，代表是更新信息
	 */
	public boolean savePluginInfo(PluginApkInfo apkInfo) {
		if (apkInfo == null) {
			return false;
		}
		Properties properties = new Properties();
		properties.put("path", apkInfo.getPath());
		properties.put("package_name", apkInfo.getPackageName());
		properties.put("version_code", apkInfo.getVersionCode() + "");
		properties.put("version_name", apkInfo.getVersionName());
		properties.put("dex_dir", apkInfo.getDestDexDir());
		properties.put("so_dir", apkInfo.getDestSODir());
		properties.put("file_md5", apkInfo.getFileMd5());
		// 文件信息
		File file = getFile(apkInfo.getPackageName());
		FileOutputStream fileOutputStream = null;
		try {
			fileOutputStream = new FileOutputStream(file);
			properties.store(fileOutputStream, "plugin info properties");
			infoMap.put(apkInfo.getPackageName(), properties);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (fileOutputStream != null) {
					fileOutputStream.close();
				}
			} catch (Exception e2) {
			}
		}
		return true;
	}

	/**
	 * 删除插件信息
	 * 
	 * @param context
	 * @param pacakgeName
	 */
	public void removePluginInfo(String pacakgeName) {
		// 删除信息文件
		File file = getFile(pacakgeName);
		if (file.exists()) {
			file.delete();
		}
		Properties properties = infoMap.get(pacakgeName);
		// 从缓存中删除
		if (properties != null) {
			infoMap.remove(pacakgeName);
			String dex_dir = properties.getProperty("dex_dir");
			String so_dir = properties.getProperty("so_dir");
			String path = properties.getProperty("path");

			File dexdir = context.getDir(dex_dir, Context.MODE_WORLD_READABLE
					+ Context.MODE_WORLD_WRITEABLE);

			File sodir = context.getDir(so_dir, Context.MODE_WORLD_READABLE
					+ Context.MODE_WORLD_WRITEABLE);
			deleteDirAndFile(dexdir);
			deleteDirAndFile(sodir);
			// 删除原文件
			HostClassLoader.removeLoader(path);
			/*
			 * File ff = new File(path); deleteDirAndFile(ff);
			 */
			infoMap.remove(pacakgeName);
		}

	}

	/**
	 * 循环遍历删除文件和目录
	 * 
	 * @param dd
	 */
	private void deleteDirAndFile(File dd) {
		if (dd == null) {
			return;
		}
		if (dd.exists()) {
			return;
		}
		if (dd.isFile()) {
			dd.delete();
			return;
		}
		if (dd.isDirectory()) {
			File[] dds = dd.listFiles();
			if (dds == null) {
				dd.delete();
			} else {
				for (File file : dds) {
					deleteDirAndFile(file);
				}
			}
		}

	}

	/**
	 * 取得文件
	 * 
	 * @param md5
	 * @return
	 */
	private File getFile(String name) {
		File dirF = context.getDir(DIR, Context.MODE_WORLD_WRITEABLE
				+ Context.MODE_WORLD_READABLE);
		if (!dirF.exists()) {
			dirF.mkdir();
		}
		File destFile = new File(dirF, name);
		return destFile;
	}

	/**
	 * 通过文件的packageName值判断是否被安装过
	 * 
	 * @param packageName
	 *            packageName
	 * @return
	 */
	public boolean isPluginInstalled(String packageName) {
		if (packageName == null || "".equals(packageName.trim())) {
			return false;
		}
		File destFile = getFile(packageName);
		if (!destFile.exists()) {
			return false;
		}
		return true;
	}

}
