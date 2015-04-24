package com.apkplugin.android.utils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.os.Build;
import android.util.DisplayMetrics;

import com.apkplugin.android.model.BaseARSPluginComponent;
import com.apkplugin.android.model.PluginActivity;
import com.apkplugin.android.model.PluginApkInfo;
import com.apkplugin.android.model.PluginIntentFilter2;
import com.apkplugin.android.model.PluginReceiver;
import com.apkplugin.android.model.PluginService;

/**
 * APK文件解析器，转换成{@link PluginApkInfo} 对象
 * 
 * @author wangbx
 * 
 */
public class ApkFileParser {

	/**
	 * 上下文
	 */
	private Context context;
	/**
	 * 插件apk路径
	 */
	private String apkPath;
	/**
	 * 插件信息对象
	 */
	private PluginApkInfo apkInfo = null;

	/**
	 * 包管理类
	 */
	private PackageManager packageManager = null;
	/**
	 * android xml的namespace前缀
	 */
	private static final String prefix = "http://schemas.android.com/apk/res/android";

	/**
	 * 实例化一个apk解析器
	 * 
	 * @param conext
	 *            上下文
	 * @param apkPath
	 *            插件文件路径
	 */
	public ApkFileParser(Context conext, String apkPath) {
		this.context = conext;
		this.apkPath = apkPath;
		packageManager = context.getPackageManager();
	}

	/**
	 * 解析apk文件
	 * 
	 * @return 返回插件对象信息类
	 */
	public PluginApkInfo parse() {
		String tag = "ApkFileParser:parse";
		long time = System.currentTimeMillis();
		apkInfo = new PluginApkInfo();
		PackageInfo info = packageManager.getPackageArchiveInfo(apkPath,
				PackageManager.GET_ACTIVITIES);
		if (info != null) {
			ApplicationInfo appInfo = info.applicationInfo;
			apkInfo.setAppName(packageManager.getApplicationLabel(appInfo)
					.toString());
			apkInfo.setPackageName(appInfo.packageName);
			apkInfo.setPath(apkPath);
			apkInfo.setVersionCode(info.versionCode);
			apkInfo.setVersionName(info.versionName);
			apkInfo.setFileMd5(Md5Util.getFileMD5(new File(apkPath)));
		}
		apkInfo = parseApk(apkPath, apkInfo);
		PluginLog.d(tag,
				"parse " + apkPath + " time is " + PluginLog.countTime(time));
		return apkInfo;
	}

	/***
	 * 解析apk
	 * 
	 * @param archiveFilePath
	 *            文件路径
	 * @param apkInfo
	 *            插件信息对象
	 * @return
	 */
	private PluginApkInfo parseApk(String archiveFilePath, PluginApkInfo apkInfo) {
		File sourceFile = new File(archiveFilePath);
		if (!sourceFile.isFile()) {
			return null;
		}
		DisplayMetrics metrics = new DisplayMetrics();
		metrics.setToDefaults();

		XmlResourceParser parser = null;
		AssetManager assetManager = null;
		Resources resource = null;
		try {
			// 通过反射将APK的资源加入到当前的AssetManager中
			assetManager = AssetManager.class.newInstance();
			Method addAssetPath = AssetManager.class.getMethod("addAssetPath",
					new Class[] { String.class });
			int cookie = (Integer) addAssetPath.invoke(assetManager,
					archiveFilePath);

			resource = new Resources(assetManager, metrics, null);
			int resourceSDKInt = 0;
			try {
				Method setConfiguration = AssetManager.class.getMethod(
						"setConfiguration", new Class[] { int.class, int.class,
								String.class, int.class, int.class, int.class,
								int.class, int.class, int.class, int.class,
								int.class, int.class, int.class, int.class,
								int.class, int.class, int.class });
				Field field = Build.VERSION.class
						.getDeclaredField("RESOURCES_SDK_INT");
				resourceSDKInt = field.getInt(null);

				setConfiguration.invoke(assetManager, new Object[] { 0, 0,
						null, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
						resourceSDKInt });
			} catch (Exception e) {
				e.printStackTrace();
			}

			// 通过反射取得openXmlResourceParser方法，进行调用和解析AndroidManifest.xml文件
			Method openXmlResourceParser = AssetManager.class
					.getDeclaredMethod("openXmlResourceParser", new Class[] {
							int.class, String.class });
			parser = (XmlResourceParser) openXmlResourceParser.invoke(
					assetManager,
					new Object[] { cookie, "AndroidManifest.xml" });

			return starParse(parser, apkInfo);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (parser != null) {
				parser.close();
			}
		}

		return null;
	}

	/**
	 * 开始解析了
	 * 
	 * @param xmlParser
	 * @param apkInfo
	 * @return
	 */
	private PluginApkInfo starParse(XmlResourceParser xmlParser,
			PluginApkInfo apkInfo) {
		String tag = "getLaunchActivityFromParser";
		try {
			BaseARSPluginComponent component = null;
			PluginIntentFilter2 filter2 = null;
			while (xmlParser.getEventType() != XmlResourceParser.END_DOCUMENT) {
				String tagName = xmlParser.getName();
				// 开始标签
				if (xmlParser.getEventType() == XmlResourceParser.START_TAG) {
					// 处理一级节点
					if ("activity".equals(tagName)) {
						component = new PluginActivity();
						component.setApkPath(apkInfo.getPath());
						component.addAttr(prefix, xmlParser);
						// 加入新的activity
						apkInfo.addActivity((PluginActivity) component);
					} else if ("receiver".equals(tagName)) {
						component = new PluginReceiver();
						component.setApkPath(apkInfo.getPath());
						component.addAttr(prefix, xmlParser);
						// 加入新的receiver
						apkInfo.addReceiver((PluginReceiver) component);
					}
					if ("service".equals(tagName)) {
						component = new PluginService();
						component.setApkPath(apkInfo.getPath());
						component.addAttr(prefix, xmlParser);
						// 加入新的service
						apkInfo.addService((PluginService) component);
					}
					// 处理二三级节点
					if ("intent-filter".equals(tagName)) {
						// 加入新的intent-filter
						filter2 = new PluginIntentFilter2();
						filter2.readFromXml(xmlParser);
						component.addIntentFilter(filter2);
					}

					if ("meta-data".equals(tagName)) {
						// 加入新的meta-data
						String name = xmlParser.getAttributeValue(prefix,
								"name");
						String value = xmlParser.getAttributeValue(prefix,
								"value");
						apkInfo.addMetaData(name, value);
					}

				} else if (xmlParser.getEventType() == XmlResourceParser.END_TAG) {

				}
				xmlParser.next();
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (xmlParser != null) {
				xmlParser.close();
			}
		}
		return apkInfo;
	}
}
