package com.apkplugin.android.callback;

/**
 * 载入插件回调
 * 
 * @author XinLan
 * 
 */
public interface LoadingPluginCallBack {
	/**
	 * 开始载入
	 */
	public void onPreLoading();

	/**
	 * 载入成功
	 **/
	public void onLoadingSuccess(String packageName);

	/**
	 * 载入失败
	 * 
	 * @param errorCode
	 *            错误代码
	 * @param errorMsg
	 *            错误消息
	 */
	public void onLoadingError(int errorCode, String errorMsg);
}