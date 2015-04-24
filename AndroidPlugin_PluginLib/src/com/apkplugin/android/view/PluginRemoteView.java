package com.apkplugin.android.view;

import android.content.Context;
import android.os.Parcel;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RemoteViews;


/**
 * 
 * 
 * @author wangbx
 */
public class PluginRemoteView extends RemoteViews {

	public PluginRemoteView(Parcel parcel) {
		super(parcel);
	}
	public PluginRemoteView(String packageName, int layoutId) {
		super(packageName, layoutId);
	}
	
	

	@Override
	public void reapply(Context context, View v) {
		super.reapply(context, v);
	}
	@Override
	public View apply(Context context, ViewGroup parent) {
		return super.apply(context, parent);
	}
	
}
