package com.apkplugin.android.callback;

/**
 * 已经安装的插件信息模型
 * 
 * @author XinLan
 * 
 */
public class InstalledPluginInfo {
	private String path;
	private int version_code;
	private String version_name;
	private String dex_dir;
	private String so_dir;
	private String file_md5;
	private String package_name;

	public String getPackage_name() {
		return package_name;
	}

	public void setPackage_name(String package_name) {
		this.package_name = package_name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getVersion_code() {
		return version_code;
	}

	public void setVersion_code(int version_code) {
		this.version_code = version_code;
	}

	public String getVersion_name() {
		return version_name;
	}

	public void setVersion_name(String version_name) {
		this.version_name = version_name;
	}

	public String getDex_dir() {
		return dex_dir;
	}

	public void setDex_dir(String dex_dir) {
		this.dex_dir = dex_dir;
	}

	public String getSo_dir() {
		return so_dir;
	}

	public void setSo_dir(String so_dir) {
		this.so_dir = so_dir;
	}

	public String getFile_md5() {
		return file_md5;
	}

	public void setFile_md5(String file_md5) {
		this.file_md5 = file_md5;
	}

}