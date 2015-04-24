package com.apkplugin.android.jar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.apkplugin.android.HostApplication;
import com.apkplugin.android.utils.ProxyActionUtil;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.IntentSender.SendIntentException;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.view.Display;

/**
 * JAR插件的Context实现
 * 
 * @author wangbx
 */
public class JarContext extends Context {

	public JarContext(String apkPath) {
		this.mApkPath = apkPath;
	}

	/**
	 * 插件的路径
	 */
	private String mApkPath;

	public void setApkPath(String mApkPath) {
		this.mApkPath = mApkPath;
	}

	/**
	 * 启动插件中的activity
	 * 
	 * @param className
	 * @param bundle
	 * @return
	 */
	public boolean startActivityByProxy(String className, Bundle bundle) {
		try {
			HostApplication.getInstance().startActivity(
					getStartActivityIntent(className, bundle));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 取得要启动插件的activity
	 * 
	 * @param className
	 * @param bundle
	 * @return
	 */
	private Intent getStartActivityIntent(String className, Bundle bundle) {
		Intent intent = ProxyActionUtil.getInstance(HostApplication.getInstance())
				.getProxyIntent(mApkPath, className);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		return intent;
	}

	@Override
	public AssetManager getAssets() {
		return HostApplication.getInstance().getAssets();
	}

	@Override
	public Resources getResources() {
		return HostApplication.getInstance().getResources();
	}

	@Override
	public PackageManager getPackageManager() {
		return HostApplication.getInstance().getPackageManager();
	}

	@Override
	public ContentResolver getContentResolver() {
		return HostApplication.getInstance().getContentResolver();
	}

	@Override
	public Looper getMainLooper() {
		return HostApplication.getInstance().getMainLooper();
	}

	@Override
	public Context getApplicationContext() {
		return HostApplication.getInstance().getApplicationContext();
	}

	@Override
	public void setTheme(int resid) {
		HostApplication.getInstance().setTheme(resid);
	}

	@Override
	public Theme getTheme() {
		return HostApplication.getInstance().getTheme();
	}

	@Override
	public ClassLoader getClassLoader() {
		return HostApplication.getInstance().getClassLoader();
	}

	@Override
	public String getPackageName() {
		return HostApplication.getInstance().getPackageName();
	}

	@Override
	public ApplicationInfo getApplicationInfo() {
		return HostApplication.getInstance().getApplicationInfo();
	}

	@Override
	public String getPackageResourcePath() {
		return HostApplication.getInstance().getPackageCodePath();
	}

	@Override
	public String getPackageCodePath() {
		return HostApplication.getInstance().getPackageCodePath();
	}

	@Override
	public SharedPreferences getSharedPreferences(String name, int mode) {
		return HostApplication.getInstance().getSharedPreferences(name, mode);
	}

	@Override
	public FileInputStream openFileInput(String name)
			throws FileNotFoundException {
		return HostApplication.getInstance().openFileInput(name);
	}

	@Override
	public FileOutputStream openFileOutput(String name, int mode)
			throws FileNotFoundException {
		return HostApplication.getInstance().openFileOutput(name, mode);
	}

	@Override
	public boolean deleteFile(String name) {
		return HostApplication.getInstance().deleteFile(name);
	}

	@Override
	public File getFileStreamPath(String name) {
		return HostApplication.getInstance().getFileStreamPath(name);
	}

	@Override
	public File getFilesDir() {
		return HostApplication.getInstance().getFilesDir();
	}

	@Override
	public File getExternalFilesDir(String type) {
		return HostApplication.getInstance().getExternalFilesDir(type);
	}

	@Override
	public File getObbDir() {
		return HostApplication.getInstance().getObbDir();
	}

	@Override
	public File getCacheDir() {
		return HostApplication.getInstance().getCacheDir();
	}

	@Override
	public File getExternalCacheDir() {
		return HostApplication.getInstance().getExternalCacheDir();
	}

	@Override
	public String[] fileList() {
		return HostApplication.getInstance().fileList();
	}

	@Override
	public File getDir(String name, int mode) {
		return HostApplication.getInstance().getDir(name, mode);
	}

	@Override
	public SQLiteDatabase openOrCreateDatabase(String name, int mode,
			CursorFactory factory) {
		return HostApplication.getInstance().openOrCreateDatabase(name, mode,
				factory);
	}

	@Override
	public SQLiteDatabase openOrCreateDatabase(String name, int mode,
			CursorFactory factory, DatabaseErrorHandler errorHandler) {
		return HostApplication.getInstance().openOrCreateDatabase(name, mode,
				factory, errorHandler);
	}

	@Override
	public boolean deleteDatabase(String name) {
		return HostApplication.getInstance().deleteDatabase(name);
	}

	@Override
	public File getDatabasePath(String name) {
		return HostApplication.getInstance().getDatabasePath(name);
	}

	@Override
	public String[] databaseList() {
		return HostApplication.getInstance().databaseList();
	}

	@Override
	public Drawable getWallpaper() {
		return HostApplication.getInstance().getWallpaper();
	}

	@Override
	public Drawable peekWallpaper() {
		return HostApplication.getInstance().peekWallpaper();
	}

	@Override
	public int getWallpaperDesiredMinimumWidth() {
		return HostApplication.getInstance().getWallpaperDesiredMinimumWidth();
	}

	@Override
	public int getWallpaperDesiredMinimumHeight() {
		return HostApplication.getInstance().getWallpaperDesiredMinimumHeight();
	}

	@Override
	public void setWallpaper(Bitmap bitmap) throws IOException {
		HostApplication.getInstance().setWallpaper(bitmap);
	}

	@Override
	public void setWallpaper(InputStream data) throws IOException {
		HostApplication.getInstance().setWallpaper(data);
	}

	@Override
	public void clearWallpaper() throws IOException {
		HostApplication.getInstance().clearWallpaper();
	}

	@Override
	public void startActivity(Intent intent) {
		HostApplication.getInstance().startActivity(intent);
	}

	@Override
	public void startActivity(Intent intent, Bundle options) {
		HostApplication.getInstance().startActivity(intent, options);
	}

	@Override
	public void startActivities(Intent[] intents) {
		HostApplication.getInstance().startActivities(intents);
	}

	@Override
	public void startActivities(Intent[] intents, Bundle options) {
		HostApplication.getInstance().startActivities(intents, options);
	}

	@Override
	public void startIntentSender(IntentSender intent, Intent fillInIntent,
			int flagsMask, int flagsValues, int extraFlags)
			throws SendIntentException {
		HostApplication.getInstance().startIntentSender(intent, fillInIntent,
				flagsMask, flagsValues, extraFlags);
	}

	@Override
	public void startIntentSender(IntentSender intent, Intent fillInIntent,
			int flagsMask, int flagsValues, int extraFlags, Bundle options)
			throws SendIntentException {
		HostApplication.getInstance().startIntentSender(intent, fillInIntent,
				flagsMask, flagsValues, extraFlags, options);
	}

	@Override
	public void sendBroadcast(Intent intent) {
		HostApplication.getInstance().sendBroadcast(intent);
	}

	@Override
	public void sendBroadcast(Intent intent, String receiverPermission) {
		HostApplication.getInstance().sendBroadcast(intent, receiverPermission);
	}

	@Override
	public void sendOrderedBroadcast(Intent intent, String receiverPermission) {
		HostApplication.getInstance().sendOrderedBroadcast(intent,
				receiverPermission);
	}

	@Override
	public void sendOrderedBroadcast(Intent intent, String receiverPermission,
			BroadcastReceiver resultReceiver, Handler scheduler,
			int initialCode, String initialData, Bundle initialExtras) {
		HostApplication.getInstance().sendOrderedBroadcast(intent,
				receiverPermission, resultReceiver, scheduler, initialCode,
				initialData, initialExtras);
	}

	@Override
	public void sendBroadcastAsUser(Intent intent, UserHandle user) {
		HostApplication.getInstance().sendBroadcastAsUser(intent, user);
	}

	@Override
	public void sendBroadcastAsUser(Intent intent, UserHandle user,
			String receiverPermission) {
		HostApplication.getInstance().sendBroadcastAsUser(intent, user,
				receiverPermission);
	}

	@Override
	public void sendOrderedBroadcastAsUser(Intent intent, UserHandle user,
			String receiverPermission, BroadcastReceiver resultReceiver,
			Handler scheduler, int initialCode, String initialData,
			Bundle initialExtras) {
		HostApplication.getInstance().sendOrderedBroadcastAsUser(intent, user,
				receiverPermission, resultReceiver, scheduler, initialCode,
				initialData, initialExtras);
	}

	@Override
	public void sendStickyBroadcast(Intent intent) {
		HostApplication.getInstance().sendStickyBroadcast(intent);
	}

	@Override
	public void sendStickyOrderedBroadcast(Intent intent,
			BroadcastReceiver resultReceiver, Handler scheduler,
			int initialCode, String initialData, Bundle initialExtras) {
		HostApplication.getInstance().sendStickyOrderedBroadcast(intent,
				resultReceiver, scheduler, initialCode, initialData,
				initialExtras);
	}

	@Override
	public void removeStickyBroadcast(Intent intent) {
		HostApplication.getInstance().removeStickyBroadcast(intent);
	}

	@Override
	public void sendStickyBroadcastAsUser(Intent intent, UserHandle user) {
		HostApplication.getInstance().sendStickyBroadcastAsUser(intent, user);
	}

	@Override
	public void sendStickyOrderedBroadcastAsUser(Intent intent,
			UserHandle user, BroadcastReceiver resultReceiver,
			Handler scheduler, int initialCode, String initialData,
			Bundle initialExtras) {
		HostApplication.getInstance().sendStickyOrderedBroadcastAsUser(intent,
				user, resultReceiver, scheduler, initialCode, initialData,
				initialExtras);
	}

	@Override
	public void removeStickyBroadcastAsUser(Intent intent, UserHandle user) {
		HostApplication.getInstance().removeStickyBroadcastAsUser(intent, user);
	}

	@Override
	public Intent registerReceiver(BroadcastReceiver receiver,
			IntentFilter filter) {
		return HostApplication.getInstance().registerReceiver(receiver, filter);
	}

	@Override
	public Intent registerReceiver(BroadcastReceiver receiver,
			IntentFilter filter, String broadcastPermission, Handler scheduler) {
		return HostApplication.getInstance().registerReceiver(receiver, filter,
				broadcastPermission, scheduler);
	}

	@Override
	public void unregisterReceiver(BroadcastReceiver receiver) {
		HostApplication.getInstance().unregisterReceiver(receiver);
	}

	@Override
	public ComponentName startService(Intent service) {
		return HostApplication.getInstance().startService(service);
	}

	@Override
	public boolean stopService(Intent service) {
		return HostApplication.getInstance().stopService(service);
	}

	@Override
	public boolean bindService(Intent service, ServiceConnection conn, int flags) {
		return HostApplication.getInstance().bindService(service, conn, flags);
	}

	@Override
	public void unbindService(ServiceConnection conn) {
		HostApplication.getInstance().unbindService(conn);
	}

	@Override
	public boolean startInstrumentation(ComponentName className,
			String profileFile, Bundle arguments) {
		return HostApplication.getInstance().startInstrumentation(className,
				profileFile, arguments);
	}

	@Override
	public Object getSystemService(String name) {
		return HostApplication.getInstance().getSystemService(name);
	}

	@Override
	public int checkPermission(String permission, int pid, int uid) {
		return HostApplication.getInstance().checkPermission(permission, pid,
				uid);
	}

	@Override
	public int checkCallingPermission(String permission) {
		return HostApplication.getInstance().checkCallingPermission(permission);
	}

	@Override
	public int checkCallingOrSelfPermission(String permission) {
		return HostApplication.getInstance().checkCallingOrSelfPermission(
				permission);
	}

	@Override
	public void enforcePermission(String permission, int pid, int uid,
			String message) {
		HostApplication.getInstance().enforcePermission(permission, pid, uid,
				message);
	}

	@Override
	public void enforceCallingPermission(String permission, String message) {
		HostApplication.getInstance().enforceCallingPermission(permission,
				message);

	}

	@Override
	public void enforceCallingOrSelfPermission(String permission, String message) {
		HostApplication.getInstance().enforceCallingOrSelfPermission(
				permission, message);
	}

	@Override
	public void grantUriPermission(String toPackage, Uri uri, int modeFlags) {
		HostApplication.getInstance().grantUriPermission(toPackage, uri,
				modeFlags);
	}

	@Override
	public void revokeUriPermission(Uri uri, int modeFlags) {
		HostApplication.getInstance().revokeUriPermission(uri, modeFlags);
	}

	@Override
	public int checkUriPermission(Uri uri, int pid, int uid, int modeFlags) {
		return HostApplication.getInstance().checkUriPermission(uri, pid, uid,
				modeFlags);
	}

	@Override
	public int checkCallingUriPermission(Uri uri, int modeFlags) {
		return HostApplication.getInstance().checkCallingUriPermission(uri,
				modeFlags);
	}

	@Override
	public int checkCallingOrSelfUriPermission(Uri uri, int modeFlags) {
		return HostApplication.getInstance().checkCallingOrSelfUriPermission(
				uri, modeFlags);
	}

	@Override
	public int checkUriPermission(Uri uri, String readPermission,
			String writePermission, int pid, int uid, int modeFlags) {
		return HostApplication.getInstance().checkUriPermission(uri,
				readPermission, writePermission, pid, uid, modeFlags);
	}

	@Override
	public void enforceUriPermission(Uri uri, int pid, int uid, int modeFlags,
			String message) {
		HostApplication.getInstance().enforceUriPermission(uri, pid, uid,
				modeFlags, message);
	}

	@Override
	public void enforceCallingUriPermission(Uri uri, int modeFlags,
			String message) {
		HostApplication.getInstance().enforceCallingUriPermission(uri,
				modeFlags, message);
	}

	@Override
	public void enforceCallingOrSelfUriPermission(Uri uri, int modeFlags,
			String message) {
		HostApplication.getInstance().enforceCallingOrSelfUriPermission(uri,
				modeFlags, message);
	}

	@Override
	public void enforceUriPermission(Uri uri, String readPermission,
			String writePermission, int pid, int uid, int modeFlags,
			String message) {
		HostApplication.getInstance().enforceUriPermission(uri, readPermission,
				writePermission, pid, uid, modeFlags, message);
	}

	@Override
	public Context createPackageContext(String packageName, int flags)
			throws NameNotFoundException {
		return HostApplication.getInstance().createPackageContext(packageName,
				flags);
	}

	@Override
	public Context createConfigurationContext(
			Configuration overrideConfiguration) {
		return HostApplication.getInstance().createConfigurationContext(
				overrideConfiguration);
	}

	@Override
	public Context createDisplayContext(Display display) {
		return HostApplication.getInstance().createDisplayContext(display);
	}

}
