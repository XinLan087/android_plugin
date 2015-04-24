package com.apkplugin.android.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import android.content.Context;

import com.apkplugin.android.HostApplication;
import com.apkplugin.android.model.PluginApkInfo;

import dalvik.system.DexClassLoader;

/**
 * 宿主程序类加载器
 * 
 * @author wangbx
 * 
 */
public class HostClassLoader extends DexClassLoader {
	/**
	 * loader的map
	 */
	private static final HashMap<String, HostClassLoader> loaders = new HashMap<String, HostClassLoader>();

	private String currentApkPath = null;
	private String packageName = null;

	private HostClassLoader(String dexPath, String optimizedDirectory,
			String libraryPath, ClassLoader parent) {
		super(dexPath, optimizedDirectory, libraryPath, parent);
	}

	/**
	 * 删除loader
	 * 
	 * @param path
	 */
	public static void removeLoader(String path) {
		if (path == null) {
			return;
		}
		if (loaders.containsKey(path)) {
			loaders.remove(path);
		}
	}

	/**
	 * 返回一个可用的类加载器
	 * 
	 * @param dexPath
	 *            插件的路径
	 * @param context
	 *            上下文
	 * @param classLoader
	 *            类载入器
	 * @return
	 */
	public static HostClassLoader getClassLoader(String dexPath,
			Context context, ClassLoader classLoader) {
		HostClassLoader hostClassLoader = loaders.get(dexPath);
		if (hostClassLoader != null) {
			HostApplication.CURRENT_PLUGIN_CLASS_LOADER = hostClassLoader;
			return hostClassLoader;
		}
		// 取得APK信息
		PluginApkInfo apkInfo = PluginCache.getInstance(context)
				.getPluginApkInfo(dexPath);
		classLoader = HostApplication.ORIGINAL_CLASS_LOADER;
		// 优化DEX文件到对应的文件夹中
		File dexOutputDir = context.getDir(apkInfo.getDestDexDir(),
				Context.MODE_PRIVATE);
		final String dexOutputPath = dexOutputDir.getAbsolutePath();
		String tmpSoDir = apkInfo.getDestSODir();
		File soOutPutDir = context.getDir(tmpSoDir, Context.MODE_PRIVATE);
		final String soOutputPath = soOutPutDir.getAbsolutePath();
		hostClassLoader = new HostClassLoader(dexPath, dexOutputPath,
				soOutputPath, classLoader);
		loaders.put(dexPath, hostClassLoader);
		HostApplication.CURRENT_PLUGIN_CLASS_LOADER = hostClassLoader;

		// 设置值
		hostClassLoader.currentApkPath = apkInfo.getPath();
		hostClassLoader.packageName = apkInfo.getPackageName();
		// 复制SO文件
		try {
			// 没有初始化完成
			if (!hostClassLoader.isSoInitOk(context)) {
				copySoFileByCpuType(context, dexPath, soOutputPath);
				// 初始化完成 保存标志位
				hostClassLoader.writeSoCopyOkInfo(context);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return hostClassLoader;
	}

	/**
	 * 保存SO写入成功标志
	 * 
	 * @param context
	 */
	private void writeSoCopyOkInfo(Context context) {
		SharedStore s = SharedStore.getPluginInfoStore(context);
		s.putBoolean(packageName + "_sofile", true);
		s.commit();
	}

	/**
	 * SO是否初始化完成
	 * 
	 * @param context
	 * @return
	 */
	private boolean isSoInitOk(Context context) {
		SharedStore s = SharedStore.getPluginInfoStore(context);
		return s.getBoolean(packageName + "_sofile", false);
	}

	/**
	 * 复制文件
	 * 
	 * @param file
	 * @param destDir
	 * @throws Exception
	 */
	private static void copySoFileByCpuType(Context context, String file,
			String destDir) throws Exception {
		String cpuType = CpuUtil.getCpuType();
		String TAG = "HostClassLoader:copySoFileByCpuType";
		// CPU文件夹类型
		if (cpuType == null || "".equals(cpuType)) {
			cpuType = "armeabi/";
		} else {
			if (cpuType.startsWith("x86")) {
				cpuType = "x86/";
			} else if (cpuType.startsWith("armv7")) {
				cpuType = "armeabi-v7a/";
			}
		}
		PluginLog.d(TAG, "cpu type :" + cpuType);
		ZipInputStream localZipInputStream = new ZipInputStream(
				new BufferedInputStream(new FileInputStream(file)));
		while (true) {
			ZipEntry localZipEntry = localZipInputStream.getNextEntry();
			if (localZipEntry == null) {
				localZipInputStream.close();
				break;
			}
			String name = localZipEntry.getName();
			// lib开头，so结束，并且包含了cpu类型的文件夹
			if (name.startsWith("lib/") && name.endsWith(".so")
					&& name.contains(cpuType)) {
				File storeFile = null;
				String tmpDir = destDir;
				storeFile = new File(tmpDir + "/"
						+ name.substring(name.lastIndexOf('/')));
				storeFile.createNewFile();
				PluginLog.d(TAG, "copy so file  " + name + " to " + destDir);
				FileOutputStream fos = new FileOutputStream(storeFile);
				byte[] arrayOfByte = new byte[1024];
				while (true) {
					int i = localZipInputStream.read(arrayOfByte);
					if (i == -1)
						break;
					fos.write(arrayOfByte, 0, i);
				}
				fos.flush();
				fos.close();
			}
			localZipInputStream.closeEntry();
		}
		localZipInputStream.close();
	}

	@Override
	public String findLibrary(String name) {
		String s = super.findLibrary(name);
		return s;
	}

	@Override
	public Class<?> loadClass(String className) throws ClassNotFoundException {
		return super.loadClass(className);
	}

	public String getCurrentApkPath() {
		return currentApkPath;
	}

	public void setCurrentApkPath(String currentApkPath) {
		this.currentApkPath = currentApkPath;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

}
