package com.apkplugin.android.utils;

import java.util.HashMap;

import com.apkplugin.android.proxy.ProxyActivity;
import com.apkplugin.android.proxy.ProxyFragmentActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.widget.Toast;

public class DLUtils {

	public static PackageInfo getPackageInfo(Context context, String apkFilepath) {
		PackageManager pm = context.getPackageManager();
		PackageInfo pkgInfo = null;
		try {
			pkgInfo = pm.getPackageArchiveInfo(apkFilepath,
					PackageManager.GET_ACTIVITIES);
		} catch (Exception e) {
			// should be something wrong with parse
			e.printStackTrace();
		}

		return pkgInfo;
	}

	public static Drawable getAppIcon(Context context, String apkFilepath) {
		PackageManager pm = context.getPackageManager();
		PackageInfo pkgInfo = getPackageInfo(context, apkFilepath);
		if (pkgInfo == null) {
			return null;
		}

		// Workaround for http://code.google.com/p/android/issues/detail?id=9151
		ApplicationInfo appInfo = pkgInfo.applicationInfo;
		if (Build.VERSION.SDK_INT >= 8) {
			appInfo.sourceDir = apkFilepath;
			appInfo.publicSourceDir = apkFilepath;
		}

		return pm.getApplicationIcon(appInfo);
	}

	public static CharSequence getAppLabel(Context context, String apkFilepath) {
		PackageManager pm = context.getPackageManager();
		PackageInfo pkgInfo = getPackageInfo(context, apkFilepath);
		if (pkgInfo == null) {
			return null;
		}

		// Workaround for http://code.google.com/p/android/issues/detail?id=9151
		ApplicationInfo appInfo = pkgInfo.applicationInfo;
		if (Build.VERSION.SDK_INT >= 8) {
			appInfo.sourceDir = apkFilepath;
			appInfo.publicSourceDir = apkFilepath;
		}

		return pm.getApplicationLabel(appInfo);
	}

	

	public static void showDialog(Activity activity, String title,
			String message) {
		new AlertDialog.Builder(activity).setTitle(title).setMessage(message)
				.setPositiveButton("确定", null).show();
	}
}
