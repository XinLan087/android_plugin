package com.apkplugin.android.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;

import com.apkplugin.android.constanst.PluginConst;
import com.apkplugin.android.utils.ProxyActionUtil;

/**
 * 插件基础类Activity,插件中的Activity要继承此类
 * 
 * @author wangbx
 */
public abstract class BasePluginFragmentActivity extends FragmentActivity
		implements IActivityLife {

	private static final String TAG = "BasePluginFragmentActivity";

	/**
	 * 代理activity，可以当作Context来使用，会根据需要来决定是否指向this
	 */
	protected FragmentActivity mProxyActivity;

	/**
	 * 等同于mProxyActivity，可以当作Context来使用，会根据需要来决定是否指向this<br/>
	 * 可以当作this来使用
	 */
	public FragmentActivity mProxyThis;

	public int mFrom = PluginConst.FROM_INTERNAL;

	/**
	 * apk 路径
	 */
	protected String mDexPath;

	/**
	 * 设置代码 继承了{@link IActivityLife#setProxy(Activity, String)}
	 */
	public void setProxy(Activity proxyActivity, String dexPath) {
		Log.d(TAG, "setProxy: proxyActivity= " + proxyActivity + ", dexPath= "
				+ dexPath);
		mProxyActivity = (FragmentActivity) proxyActivity;
		mProxyThis = mProxyActivity;
		mDexPath = dexPath;
	}

	public Activity getRealActivity() {
		return mProxyThis;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			mFrom = savedInstanceState.getInt(PluginConst.FROM,
					PluginConst.FROM_INTERNAL);
		}
		if (mFrom == PluginConst.FROM_INTERNAL) {
			super.onCreate(savedInstanceState);
			mProxyActivity = this;
			mProxyThis = mProxyActivity;
		}
		Log.d(TAG, "onCreate: from= "
				+ (mFrom == PluginConst.FROM_INTERNAL ? "FROM_INTERNAL"
						: "FROM_EXTERNAL"));
	}

	@Override
	public void setTheme(int resid) {
		if (mProxyActivity == this) {
			super.setTheme(resid);
		} else {
			if (mProxyActivity != null) {
				mProxyActivity.setTheme(resid);
			} else {
				super.setTheme(resid);
			}
		}
	}

	@Override
	public Object getSystemService(String name) {
		if (mProxyActivity == this) {
			return super.getSystemService(name);
		} else {
			if (mProxyActivity != null) {
				return mProxyActivity.getSystemService(name);
			} else {
				return super.getSystemService(name);
			}

		}
	}

	@Override
	public Context getApplicationContext() {
		if (mProxyActivity == this) {
			return super.getApplicationContext();
		} else {
			if (mProxyActivity != null) {
				return mProxyActivity.getApplicationContext();
			} else {
				return super.getApplicationContext();
			}

		}
	}

	public String getProxyAction() {
		return PluginConst.PROXY_VIEW_FRAGMENT_ACTIVITY_ACTION;
	}

	@Override
	public PackageManager getPackageManager() {
		if (mProxyActivity == this) {
			return super.getPackageManager();
		} else {
			if (mProxyActivity != null) {
				return mProxyActivity.getPackageManager();
			} else {
				return super.getPackageManager();
			}
		}
	}

	@Override
	public SharedPreferences getSharedPreferences(String name, int mode) {
		if (mProxyActivity == this) {

			return super.getSharedPreferences(name, mode);
		} else {
			if (mProxyActivity != null) {
				return mProxyActivity.getSharedPreferences(name, mode);
			} else {
				return super.getSharedPreferences(name, mode);
			}
		}

	}

	@Override
	public Resources getResources() {
		if (mProxyActivity == this) {
			return super.getResources();
		} else {
			if (mProxyActivity != null) {
				return mProxyActivity.getResources();
			} else {
				return super.getResources();
			}
		}
	}

	/**
	 * /** 启动一个Activity，通过代理Activity进行启动
	 * 
	 * @param className
	 */
	public boolean startActivityByProxy(String className) {
		try {
			Intent intent = ProxyActionUtil.getInstance(getRealActivity())
					.getStartActivityIntent(mProxyActivity, this, mDexPath,
							className, null);
			mProxyActivity.startActivity(intent);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 启动一个Activity，通过代理Activity进行启动
	 * 
	 * @param className
	 */
	public boolean startActivityByProxy(String className, Bundle bundle) {
		try {
			Intent intent = ProxyActionUtil.getInstance(getRealActivity())
					.getStartActivityIntent(mProxyActivity, this, mDexPath,
							className, bundle);
			mProxyActivity.startActivity(intent);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 启动一个Activity，并且可以返回Result Code，通过代理Activity进行启动
	 * 
	 * @param className
	 */
	public boolean startActivityForResultByProxy(String className,
			int requestCode) {
		try {
			Intent intent = ProxyActionUtil.getInstance(getRealActivity())
					.getStartActivityIntent(mProxyActivity, this, mDexPath,
							className, null);
			mProxyActivity.startActivityForResult(intent, requestCode);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 启动一个Activity，并且可以返回Result Code，通过代理Activity进行启动
	 * 
	 * @param className
	 */
	public boolean startActivityForResultByProxy(String className,
			int requestCode, Bundle bundle) {
		try {
			Intent intent = ProxyActionUtil.getInstance(getRealActivity())
					.getStartActivityIntent(mProxyActivity, this, mDexPath,
							className, bundle);
			mProxyActivity.startActivityForResult(intent, requestCode);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public FragmentManager getSupportFragmentManager() {
		if (mProxyActivity == this) {
			return super.getSupportFragmentManager();
		} else {
			if (mProxyActivity != null) {
				return mProxyActivity.getSupportFragmentManager();
			} else {
				return super.getSupportFragmentManager();
			}
		}
	}

	/**
	 * 重新setContentView，判断View设置到Proxy还是this
	 */
	@Override
	public void setContentView(View view) {
		if (mProxyActivity == this) {
			super.setContentView(view);
		} else {
			if (mProxyActivity != null) {
				mProxyActivity.setContentView(view);
			} else {
				super.setContentView(view);
			}
		}
	}

	/**
	 * 重新setContentView，判断View设置到Proxy还是this
	 */
	@Override
	public void setContentView(View view, ViewGroup.LayoutParams params) {
		if (mProxyActivity == this) {
			super.setContentView(view, params);
		} else {
			if (mProxyActivity != null) {
				mProxyActivity.setContentView(view, params);
			} else {
				super.setContentView(view, params);
			}
		}
	}

	@Override
	public LayoutInflater getLayoutInflater() {
		if (mProxyActivity == this) {
			return super.getLayoutInflater();
		} else {
			if (mProxyActivity != null) {
				return mProxyActivity.getLayoutInflater();
			} else {
				return super.getLayoutInflater();
			}

		}
	}

	/**
	 * 重新setContentView，判断View设置到Proxy还是this
	 */
	@Override
	public void setContentView(int layoutResID) {
		if (mProxyActivity == this) {
			super.setContentView(layoutResID);
		} else {
			if (mProxyActivity != null) {
				LayoutInflater inflater = mProxyActivity.getLayoutInflater();
				if (inflater != null) {

					mProxyActivity.setContentView(inflater.inflate(layoutResID,
							null));
				} else {
					mProxyActivity.setContentView(layoutResID);
				}
			} else {
				super.setContentView(layoutResID);
			}
		}
	}

	/**
	 * 重新addContentView，判断View设置到Proxy还是this
	 */
	@Override
	public void addContentView(View view, ViewGroup.LayoutParams params) {
		if (mProxyActivity == this) {
			super.addContentView(view, params);
		} else {
			if (mProxyActivity != null) {
				mProxyActivity.addContentView(view, params);
			} else {
				super.addContentView(view, params);
			}
		}
	}

	/**
	 * 重新findViewById，判断取得View是从Proxy还是this
	 */
	@Override
	public View findViewById(int id) {
		if (mProxyActivity == this) {
			return super.findViewById(id);
		} else {
			if (mProxyActivity != null) {
				return mProxyActivity.findViewById(id);
			} else {
				return super.findViewById(id);
			}
		}
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (mProxyActivity == this) {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	@Override
	public void onStart() {
		if (mProxyActivity == this) {
			super.onStart();
		}
	}

	@Override
	public void onRestart() {
		if (mProxyActivity == this) {
			super.onRestart();
		}
	}

	@Override
	public void onStop() {
		if (mProxyActivity == this) {
			super.onStop();
		}
	}

	@Override
	public void onDestroy() {
		if (mProxyActivity == this) {
			super.onDestroy();
		}
	}

	@Override
	public void onPause() {
		if (mProxyActivity == this) {
			super.onPause();
		}
	}

	@Override
	public void onResume() {
		if (mProxyActivity == this) {
			super.onResume();
		}
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		if (mProxyActivity == this) {
			super.onRestoreInstanceState(savedInstanceState);
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		if (mProxyActivity == this) {
			super.onSaveInstanceState(outState);
		}
	}

	public void onNewIntent(Intent intent) {
		if (mProxyActivity == this) {
			super.onNewIntent(intent);
		}
	}

	public boolean onTouchEvent(MotionEvent event) {
		if (mProxyActivity == this) {
			super.onTouchEvent(event);
		}
		return false;
	}

	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (mProxyActivity == this) {
			return super.onKeyUp(keyCode, event);
		}
		return false;
	}

	public void onWindowAttributesChanged(LayoutParams params) {
		if (mProxyActivity == this) {
			super.onWindowAttributesChanged(params);
		}
	}

	public void onWindowFocusChanged(boolean hasFocus) {
		if (mProxyActivity == this) {
			super.onWindowFocusChanged(hasFocus);
		}
	}
}