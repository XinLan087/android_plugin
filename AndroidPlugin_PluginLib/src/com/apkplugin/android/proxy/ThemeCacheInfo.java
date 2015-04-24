package com.apkplugin.android.proxy;

import java.lang.reflect.Method;
import java.util.HashMap;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;


/**
 * 对加载进来得 APK资源进行缓存
 * @author XinLan
 *
 */
public class ThemeCacheInfo {
	private String currentApkPath = null;
	private AssetManager mAssetManager;
	private Resources mResources;
	private Theme mTheme;

	private static final HashMap<String, ThemeCacheInfo> INFOS = new HashMap<String, ThemeCacheInfo>();

	private ThemeCacheInfo(Context context, String path) {
		this.currentApkPath = path;
		loadResources(context);
	}

	public static ThemeCacheInfo getThemeCacheInfo(Context context, String path) {
		if (!INFOS.containsKey(path)) {
			ThemeCacheInfo cache = new ThemeCacheInfo(context, path);
			INFOS.put(path, cache);
			return cache;
		}
		return INFOS.get(path);

	}

	/**
	 * 载入资源
	 */
	protected void loadResources(Context context) {
		try {
			AssetManager assetManager = AssetManager.class.newInstance();
			Method addAssetPath = assetManager.getClass().getMethod(
					"addAssetPath", String.class);
			addAssetPath.invoke(assetManager, currentApkPath);
			mAssetManager = assetManager;
		} catch (Exception e) {
			e.printStackTrace();
		}
		Resources superRes = context.getResources();
		mResources = new Resources(mAssetManager, superRes.getDisplayMetrics(),
				superRes.getConfiguration());
		mTheme = mResources.newTheme();
		mTheme.setTo(context.getTheme());
	}

	public void setTheme(Context context, int resid) {
		if (mTheme == null) {
			mTheme = getResources(context).newTheme();
			Resources.Theme theme = context.getTheme();
			if (theme != null) {
				mTheme.setTo(theme);
			}
		}
		mTheme.applyStyle(resid, true);
	}

	public AssetManager getAssets(Context context) {
		return mAssetManager == null ? context.getAssets() : mAssetManager;
	}

	public Resources getResources(Context context) {
		return mResources == null ? context.getResources() : mResources;
	}

	public Theme getTheme(Context context) {
		return mTheme == null ? context.getTheme() : mTheme;
	}

	class MyResource extends Resources {
		private Context context;

		public void setContext(Context context) {
			this.context = context;
		}

		public MyResource(Context context, AssetManager assets,
				DisplayMetrics metrics, Configuration config) {
			super(assets, metrics, config);
			this.context = context;
		}

		@Override
		public Drawable getDrawableForDensity(int id, int density)
				throws NotFoundException {
			return super.getDrawableForDensity(id, density);
		}

		@Override
		public Drawable getDrawable(int id) throws NotFoundException {
			try {
				return super.getDrawable(id);
			} catch (Exception e) {
				e.printStackTrace();
				return context.getResources().getDrawable(id);
			}
		}
	}
}
