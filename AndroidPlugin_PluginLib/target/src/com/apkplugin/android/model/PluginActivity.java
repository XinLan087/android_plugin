package com.apkplugin.android.model;


/**
 * AndroidManifest.xml中配置的Activity节点解析
 * 
 * @author wangbx
 */
public class PluginActivity extends BaseARSPluginComponent {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7207128787186711395L;
	/**
	 * 对应节点:android:launchMode
	 */
	public int launchMode;

	public int getLaunchMode() {
		return launchMode;
	}

	public void setLaunchMode(int launchMode) {
		this.launchMode = launchMode;
	}

}