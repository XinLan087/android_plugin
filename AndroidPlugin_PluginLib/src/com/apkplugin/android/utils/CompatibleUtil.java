package com.apkplugin.android.utils;

import java.lang.reflect.Method;

import android.app.ActionBar;
import android.app.Activity;

public class CompatibleUtil {
	/**
	 * fixed bug for actionbar in 5.0
	 * 
	 * @param activity
	 * @param actionBar
	 */
	public static void fixActionBarBug(Activity activity) {
		try {

			Method dd = activity.getClass().getMethod("getActionBar",
					new Class[] {});
			if (dd == null) {
				return;
			}
			Object actionBar = dd.invoke(activity, new Object[] {});
			if (actionBar == null) {
				return;
			}
			Smith<Object> mActionView = new Smith<Object>(actionBar,
					"mActionView");
			if (mActionView == null || mActionView.get() == null) {
				return;
			}
			Smith<Object> mContext = new Smith<Object>(mActionView, "mContext");
			if (mContext == null || mContext.get() == null) {
				return;
			}
			mContext.set(activity);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
