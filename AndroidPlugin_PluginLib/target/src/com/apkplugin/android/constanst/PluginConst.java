package com.apkplugin.android.constanst;

public class PluginConst {

	/**
	 * INTENT KEY,来源
	 */
	public static final String FROM = "extra.from";

	/**
	 * 宿主自身
	 */
	public static final int FROM_INTERNAL = 0;
	/**
	 * 外部插件
	 */
	public static final int FROM_EXTERNAL = 1;
	/**
	 * APK 或者DEX路径
	 */
	public static final String EXTRA_DEX_PATH = "extra.dex.path";
	/**
	 * 启动的Class路径
	 */
	public static final String EXTRA_CLASS = "extra.class";

	/**
	 * 
	 * 启动Activity的action
	 */
	public static final String PROXY_VIEW_ACTIVITY_ACTION = "com.apkplugin.dynamicloadhost.activity.VIEW";

	/**
	 * single_instance_activity
	 */
	public static final String PROXY_VIEW_SINGLE_INSTANCE_ACTIVITY_ACTION = "com.apkplugin.dynamicloadhost.single_instance_activity.VIEW";

	/**
	 * single_task_activity
	 */
	public static final String PROXY_VIEW_SINGLE_TASK_ACTIVITY_ACTION = "com.apkplugin.dynamicloadhost.single_task_activity.VIEW";

	/**
	 * single_top_activity
	 */
	public static final String PROXY_VIEW_SINGLE_TOP_ACTIVITY_ACTION = "com.apkplugin.dynamicloadhost.single_top_activity.VIEW";

	/**
	 * 
	 * 启动ActivityGroup的action
	 */
	public static final String PROXY_VIEW_ACTIVITY_GROUP_ACTION = "com.apkplugin.dynamicloadhost.activitygroup.VIEW";

	/**
	 * 启动FragmentActivity
	 */
	public static final String PROXY_VIEW_FRAGMENT_ACTIVITY_ACTION = "com.apkplugin.dynamicloadhost.fragmentactivity.VIEW";
	
	
	
	
	/**
	 * single_instance_activity
	 */
	public static final String PROXY_VIEW_SINGLE_INSTANCE_FRAGMENT_ACTIVITY_ACTION = "com.apkplugin.dynamicloadhost.single_instance_fragment_activity.VIEW";

	/**
	 * single_task_activity
	 */
	public static final String PROXY_VIEW_SINGLE_TASK_FRAGMENT_ACTIVITY_ACTION = "com.apkplugin.dynamicloadhost.single_task_fragment_activity.VIEW";

	/**
	 * single_top_activity
	 */
	public static final String PROXY_VIEW_SINGLE_TOP_FRAGMENT_ACTIVITY_ACTION = "com.apkplugin.dynamicloadhost.single_top_fragment_activity.VIEW";

	
	/**
	 * 启动ListActivity
	 */
	public static final String PROXY_VIEW_LIST_ACTIVITY_ACTION = "com.apkplugin.dynamicloadhost.listactivity.VIEW";
}
