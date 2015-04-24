package com.apkplugin.android.callback;
/**
	 * 卸载回调函数
	 * 
	 * @author XinLan
	 */
	public  interface UnInstallPluginCallBack {

		/**
		 * 开始卸载
		 */
		public void onPreUnInstall();

		/**
		 * 卸载成功
		 * 
		 * @param packageName
		 *            包名
		 */
		public void onUnInstallSuccess(String packageName);

		/**
		 * 卸载失败
		 * 
		 * @param errorCode
		 *            错误代码
		 * @param errorMsg
		 *            错误消息
		 */
		public void onUnInstallError(int errorCode, String errorMsg);
	}

	