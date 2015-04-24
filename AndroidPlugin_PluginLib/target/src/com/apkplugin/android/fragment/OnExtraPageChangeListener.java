package com.apkplugin.android.fragment;

/**
 * page切换额外功能接口
 */
public class OnExtraPageChangeListener {
	/**
	 * 扩展ViewPager的滑动
	 * 
	 * @param i
	 *            当前页面，及你点击滑动的页面
	 * @param v
	 *            当前页面偏移的百分比
	 * @param i2
	 *            当前页面偏移的像素位置
	 */
	public void onExtraPageScrolled(int i, float v, int i2) {
	}

	/**
	 * 扩展ViewPager的选择,前选中的页面的Position（位置编号）
	 * 
	 * @param i
	 */
	public void onExtraPageSelected(int i) {
	}

	public void onExtraPageScrollStateChanged(int i) {
	}
}