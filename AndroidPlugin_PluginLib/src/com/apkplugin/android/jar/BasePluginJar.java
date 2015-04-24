package com.apkplugin.android.jar;

import android.content.Context;

/**
 * 普通JAR插件的启动父类
 * 
 * @author XinLan
 * 
 */
public abstract class BasePluginJar {

	/**
	 * APK文件的路径
	 */
	public String mApkPath;

	/**
	 * context上下文
	 */
	public JarContext mContext;

	/**
	 * 启动JAR
	 * 
	 */
	public abstract void startJar();

	public Context getContext() {
		return mContext;
	}

	public void setContext(JarContext mContext) {
		this.mContext = mContext;
	}

	public void setApkPath(String mApkPath) {
		this.mApkPath = mApkPath;
	}

	public String getApkPath() {
		return mApkPath;
	}
}
