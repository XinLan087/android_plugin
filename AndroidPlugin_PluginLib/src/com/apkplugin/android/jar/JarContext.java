package com.apkplugin.android.jar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

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

import com.apkplugin.android.utils.RunTarget;

/**
 * JAR插件的Context实现
 * 
 * @author XinLan
 */
public class JarContext extends Context {

	private Context context;

	public JarContext(String apkPath, Context context) {
		this.mApkPath = apkPath;
		this.context = context;
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
		return RunTarget.startActivityByProxy(context, mApkPath, className,
				bundle);
	}

	@Override
	public AssetManager getAssets() {
		return this.context.getAssets();
	}

	@Override
	public Resources getResources() {
		return this.context.getResources();
	}

	@Override
	public PackageManager getPackageManager() {
		return this.context.getPackageManager();
	}

	@Override
	public ContentResolver getContentResolver() {
		return this.context.getContentResolver();
	}

	@Override
	public Looper getMainLooper() {
		return this.context.getMainLooper();
	}

	@Override
	public Context getApplicationContext() {
		return this.context.getApplicationContext();
	}

	@Override
	public void setTheme(int resid) {
		this.context.setTheme(resid);
	}

	@Override
	public Theme getTheme() {
		return this.context.getTheme();
	}

	@Override
	public ClassLoader getClassLoader() {
		return this.context.getClassLoader();
	}

	@Override
	public String getPackageName() {
		return this.context.getPackageName();
	}

	@Override
	public ApplicationInfo getApplicationInfo() {
		return this.context.getApplicationInfo();
	}

	@Override
	public String getPackageResourcePath() {
		return this.context.getPackageCodePath();
	}

	@Override
	public String getPackageCodePath() {
		return this.context.getPackageCodePath();
	}

	@Override
	public SharedPreferences getSharedPreferences(String name, int mode) {
		return this.context.getSharedPreferences(name, mode);
	}

	@Override
	public FileInputStream openFileInput(String name)
			throws FileNotFoundException {
		return this.context.openFileInput(name);
	}

	@Override
	public FileOutputStream openFileOutput(String name, int mode)
			throws FileNotFoundException {
		return this.context.openFileOutput(name, mode);
	}

	@Override
	public boolean deleteFile(String name) {
		return this.context.deleteFile(name);
	}

	@Override
	public File getFileStreamPath(String name) {
		return this.context.getFileStreamPath(name);
	}

	@Override
	public File getFilesDir() {
		return this.context.getFilesDir();
	}

	@Override
	public File getExternalFilesDir(String type) {
		return this.context.getExternalFilesDir(type);
	}

	@Override
	public File getObbDir() {
		return this.context.getObbDir();
	}

	@Override
	public File getCacheDir() {
		return this.context.getCacheDir();
	}

	@Override
	public File getExternalCacheDir() {
		return this.context.getExternalCacheDir();
	}

	@Override
	public String[] fileList() {
		return this.context.fileList();
	}

	@Override
	public File getDir(String name, int mode) {
		return this.context.getDir(name, mode);
	}

	@Override
	public SQLiteDatabase openOrCreateDatabase(String name, int mode,
			CursorFactory factory) {
		return this.context.openOrCreateDatabase(name, mode, factory);
	}

	@Override
	public SQLiteDatabase openOrCreateDatabase(String name, int mode,
			CursorFactory factory, DatabaseErrorHandler errorHandler) {
		return this.context.openOrCreateDatabase(name, mode, factory,
				errorHandler);
	}

	@Override
	public boolean deleteDatabase(String name) {
		return this.context.deleteDatabase(name);
	}

	@Override
	public File getDatabasePath(String name) {
		return this.context.getDatabasePath(name);
	}

	@Override
	public String[] databaseList() {
		return this.context.databaseList();
	}

	@Override
	public Drawable getWallpaper() {
		return this.context.getWallpaper();
	}

	@Override
	public Drawable peekWallpaper() {
		return this.context.peekWallpaper();
	}

	@Override
	public int getWallpaperDesiredMinimumWidth() {
		return this.context.getWallpaperDesiredMinimumWidth();
	}

	@Override
	public int getWallpaperDesiredMinimumHeight() {
		return this.context.getWallpaperDesiredMinimumHeight();
	}

	@Override
	public void setWallpaper(Bitmap bitmap) throws IOException {
		this.context.setWallpaper(bitmap);
	}

	@Override
	public void setWallpaper(InputStream data) throws IOException {
		this.context.setWallpaper(data);
	}

	@Override
	public void clearWallpaper() throws IOException {
		this.context.clearWallpaper();
	}

	@Override
	public void startActivity(Intent intent) {
		this.context.startActivity(intent);
	}

	@Override
	public void startActivity(Intent intent, Bundle options) {
		this.context.startActivity(intent, options);
	}

	@Override
	public void startActivities(Intent[] intents) {
		this.context.startActivities(intents);
	}

	@Override
	public void startActivities(Intent[] intents, Bundle options) {
		this.context.startActivities(intents, options);
	}

	@Override
	public void startIntentSender(IntentSender intent, Intent fillInIntent,
			int flagsMask, int flagsValues, int extraFlags)
			throws SendIntentException {
		this.context.startIntentSender(intent, fillInIntent, flagsMask,
				flagsValues, extraFlags);
	}

	@Override
	public void startIntentSender(IntentSender intent, Intent fillInIntent,
			int flagsMask, int flagsValues, int extraFlags, Bundle options)
			throws SendIntentException {
		this.context.startIntentSender(intent, fillInIntent, flagsMask,
				flagsValues, extraFlags, options);
	}

	@Override
	public void sendBroadcast(Intent intent) {
		this.context.sendBroadcast(intent);
	}

	@Override
	public void sendBroadcast(Intent intent, String receiverPermission) {
		this.context.sendBroadcast(intent, receiverPermission);
	}

	@Override
	public void sendOrderedBroadcast(Intent intent, String receiverPermission) {
		this.context.sendOrderedBroadcast(intent, receiverPermission);
	}

	@Override
	public void sendOrderedBroadcast(Intent intent, String receiverPermission,
			BroadcastReceiver resultReceiver, Handler scheduler,
			int initialCode, String initialData, Bundle initialExtras) {
		this.context.sendOrderedBroadcast(intent, receiverPermission,
				resultReceiver, scheduler, initialCode, initialData,
				initialExtras);
	}

	@Override
	public void sendBroadcastAsUser(Intent intent, UserHandle user) {
		this.context.sendBroadcastAsUser(intent, user);
	}

	@Override
	public void sendBroadcastAsUser(Intent intent, UserHandle user,
			String receiverPermission) {
		this.context.sendBroadcastAsUser(intent, user, receiverPermission);
	}

	@Override
	public void sendOrderedBroadcastAsUser(Intent intent, UserHandle user,
			String receiverPermission, BroadcastReceiver resultReceiver,
			Handler scheduler, int initialCode, String initialData,
			Bundle initialExtras) {
		this.context.sendOrderedBroadcastAsUser(intent, user,
				receiverPermission, resultReceiver, scheduler, initialCode,
				initialData, initialExtras);
	}

	@Override
	public void sendStickyBroadcast(Intent intent) {
		this.context.sendStickyBroadcast(intent);
	}

	@Override
	public void sendStickyOrderedBroadcast(Intent intent,
			BroadcastReceiver resultReceiver, Handler scheduler,
			int initialCode, String initialData, Bundle initialExtras) {
		this.context.sendStickyOrderedBroadcast(intent, resultReceiver,
				scheduler, initialCode, initialData, initialExtras);
	}

	@Override
	public void removeStickyBroadcast(Intent intent) {
		this.context.removeStickyBroadcast(intent);
	}

	@Override
	public void sendStickyBroadcastAsUser(Intent intent, UserHandle user) {
		this.context.sendStickyBroadcastAsUser(intent, user);
	}

	@Override
	public void sendStickyOrderedBroadcastAsUser(Intent intent,
			UserHandle user, BroadcastReceiver resultReceiver,
			Handler scheduler, int initialCode, String initialData,
			Bundle initialExtras) {
		this.context.sendStickyOrderedBroadcastAsUser(intent, user,
				resultReceiver, scheduler, initialCode, initialData,
				initialExtras);
	}

	@Override
	public void removeStickyBroadcastAsUser(Intent intent, UserHandle user) {
		this.context.removeStickyBroadcastAsUser(intent, user);
	}

	@Override
	public Intent registerReceiver(BroadcastReceiver receiver,
			IntentFilter filter) {
		return this.context.registerReceiver(receiver, filter);
	}

	@Override
	public Intent registerReceiver(BroadcastReceiver receiver,
			IntentFilter filter, String broadcastPermission, Handler scheduler) {
		return this.context.registerReceiver(receiver, filter,
				broadcastPermission, scheduler);
	}

	@Override
	public void unregisterReceiver(BroadcastReceiver receiver) {
		this.context.unregisterReceiver(receiver);
	}

	@Override
	public ComponentName startService(Intent service) {
		return this.context.startService(service);
	}

	@Override
	public boolean stopService(Intent service) {
		return this.context.stopService(service);
	}

	@Override
	public boolean bindService(Intent service, ServiceConnection conn, int flags) {
		return this.context.bindService(service, conn, flags);
	}

	@Override
	public void unbindService(ServiceConnection conn) {
		this.context.unbindService(conn);
	}

	@Override
	public boolean startInstrumentation(ComponentName className,
			String profileFile, Bundle arguments) {
		return this.context.startInstrumentation(className, profileFile,
				arguments);
	}

	@Override
	public Object getSystemService(String name) {
		return this.context.getSystemService(name);
	}

	@Override
	public int checkPermission(String permission, int pid, int uid) {
		return this.context.checkPermission(permission, pid, uid);
	}

	@Override
	public int checkCallingPermission(String permission) {
		return this.context.checkCallingPermission(permission);
	}

	@Override
	public int checkCallingOrSelfPermission(String permission) {
		return this.context.checkCallingOrSelfPermission(permission);
	}

	@Override
	public void enforcePermission(String permission, int pid, int uid,
			String message) {
		this.context.enforcePermission(permission, pid, uid, message);
	}

	@Override
	public void enforceCallingPermission(String permission, String message) {
		this.context.enforceCallingPermission(permission, message);

	}

	@Override
	public void enforceCallingOrSelfPermission(String permission, String message) {
		this.context.enforceCallingOrSelfPermission(permission, message);
	}

	@Override
	public void grantUriPermission(String toPackage, Uri uri, int modeFlags) {
		this.context.grantUriPermission(toPackage, uri, modeFlags);
	}

	@Override
	public void revokeUriPermission(Uri uri, int modeFlags) {
		this.context.revokeUriPermission(uri, modeFlags);
	}

	@Override
	public int checkUriPermission(Uri uri, int pid, int uid, int modeFlags) {
		return this.context.checkUriPermission(uri, pid, uid, modeFlags);
	}

	@Override
	public int checkCallingUriPermission(Uri uri, int modeFlags) {
		return this.context.checkCallingUriPermission(uri, modeFlags);
	}

	@Override
	public int checkCallingOrSelfUriPermission(Uri uri, int modeFlags) {
		return this.context.checkCallingOrSelfUriPermission(uri, modeFlags);
	}

	@Override
	public int checkUriPermission(Uri uri, String readPermission,
			String writePermission, int pid, int uid, int modeFlags) {
		return this.context.checkUriPermission(uri, readPermission,
				writePermission, pid, uid, modeFlags);
	}

	@Override
	public void enforceUriPermission(Uri uri, int pid, int uid, int modeFlags,
			String message) {
		this.context.enforceUriPermission(uri, pid, uid, modeFlags, message);
	}

	@Override
	public void enforceCallingUriPermission(Uri uri, int modeFlags,
			String message) {
		this.context.enforceCallingUriPermission(uri, modeFlags, message);
	}

	@Override
	public void enforceCallingOrSelfUriPermission(Uri uri, int modeFlags,
			String message) {
		this.context.enforceCallingOrSelfUriPermission(uri, modeFlags, message);
	}

	@Override
	public void enforceUriPermission(Uri uri, String readPermission,
			String writePermission, int pid, int uid, int modeFlags,
			String message) {
		this.context.enforceUriPermission(uri, readPermission, writePermission,
				pid, uid, modeFlags, message);
	}

	@Override
	public Context createPackageContext(String packageName, int flags)
			throws NameNotFoundException {
		return this.context.createPackageContext(packageName, flags);
	}

	@Override
	public Context createConfigurationContext(
			Configuration overrideConfiguration) {
		return this.context.createConfigurationContext(overrideConfiguration);
	}

	@Override
	public Context createDisplayContext(Display display) {
		return this.context.createDisplayContext(display);
	}

}
