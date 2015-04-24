package com.apkplugin.android;

import android.app.Application;
import android.content.Context;

import com.apkplugin.android.utils.Smith;

public class HostApplication extends Application {
	/**
	 * 原来的ClassLoader
	 */
	public static ClassLoader ORIGINAL_CLASS_LOADER;
	/**
	 * 当前插件的ClassLoader
	 */
	public static ClassLoader CURRENT_PLUGIN_CLASS_LOADER = null;

	/**
	 * Application 实例对象
	 */
	private static Application hostApplication = null;

	public static Context getInstance() {
		return hostApplication;
	}

	@Override
	public Context getApplicationContext() {
		return super.getApplicationContext();
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		hostApplication = null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		hostApplication = this;
		try {
			Context mBase = new Smith<Context>(this, "mBase").get();

			Object mPackageInfo = new Smith<Object>(mBase, "mPackageInfo")
					.get();
			Smith<ClassLoader> sClassLoader = new Smith<ClassLoader>(
					mPackageInfo, "mClassLoader");
			ClassLoader mClassLoader = sClassLoader.get();
			// 保存老ClassLoader
			ORIGINAL_CLASS_LOADER = mClassLoader;
			// 设置新的ClassLoader
			MyClassLoader cl = new MyClassLoader(mClassLoader);
			sClassLoader.set(cl);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 自定义的ClassLoader
	 * 
	 * @author wangbx
	 */
	class MyClassLoader extends ClassLoader {
		public MyClassLoader(ClassLoader parent) {
			super(parent);
		}

		@Override
		public Class<?> loadClass(String className)
				throws ClassNotFoundException {
			if (CURRENT_PLUGIN_CLASS_LOADER != null) {
				try {
					Class<?> c = CURRENT_PLUGIN_CLASS_LOADER
							.loadClass(className);
					if (c != null)
						return c;
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
					Class<?> c = getSystemClassLoader().loadClass(className);
					if (c != null)
						return c;
					else
						return super.loadClass(className);
				}
			}
			return super.loadClass(className);
		}

	}

	/**
	 * 取得宿主程序版本
	 * @return
	 */
	public static String getHostInterfaceVersion() {
		return Version.HOST_INTERFACE_VERSION;
	}

}
