package com.apkplugin.android.callback;

/**
 * 安装回调接口
 * 
 * @author wangbx
 * 
 */
public interface InstallPluginCallBack {
	/**
	 * 开始安装
	 */
	public void onPreInstall();

	/**
	 * 安装成功
	 * 
	 * @param isUpdate
	 *            是否是更新，如果是，表示之前安装过
	 */
	public void onInstallSuccess(boolean isUpdate);

	/**
	 * 安装失败
	 * 
	 * @param errorCode
	 *            错误代码
	 * @param errorMsg
	 *            错误消息
	 */
	public void onInstallError(int errorCode, String errorMsg);
}
