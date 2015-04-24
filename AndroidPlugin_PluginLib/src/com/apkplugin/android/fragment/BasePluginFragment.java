package com.apkplugin.android.fragment;

import java.lang.reflect.Field;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 基础的插件Fragment，插件类中使用的Fragment需要继承此类，否则不会生效
 * 
 * @author XinLan
 * 
 */
public abstract class BasePluginFragment extends Fragment implements
		IFragmentLife {

	
	
	public BasePluginFragment() {
	}
	/**
	 * 根视图
	 */
	private View rootView = null;
	/**
	 * 视图是否准备好
	 */
	private boolean isViewPrepared = false;
	/**
	 * 当前Fragment是否可见状态
	 */
	private boolean isFragmentVisible = false;

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (getUserVisibleHint()) {
			isFragmentVisible = true;
			onShow();
		} else {
			isFragmentVisible = false;
		}
	}

	/**
	 * 显示此Fragment的时候调用
	 */
	public void onShow() {

	}

	/**
	 * 
	 * 隐藏的时候调用， 此方法在{@link FragmentPagerAdapter#MyPageChangeListener
	 * #onPageSelected(int)} 进行调用
	 */
	public void onHidden() {

	}

	/**
	 * 是否可以进行载入数据操作，此方法会判断View视图是否准备好 ，并且会判断当前Fragment是否为显示状态
	 * 
	 * @return
	 */
	public boolean isCanLoadingData() {
		if (!isViewPrepared || !isFragmentVisible) {
			return false;
		}
		return true;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (rootView != null) {
			// 删除老的parent，防止 报这个错误：
			// java.lang.IllegalStateException: The specified child already has
			// a parent. You must call removeView() on the child's parent first.
			if (rootView.getParent() != null) {
				ViewGroup p = (ViewGroup) rootView.getParent();
				if (p != null) {
					p.removeAllViewsInLayout();
				}
			}
			isViewPrepared = true;
			return rootView;
		}
		isViewPrepared = true;
		rootView = generateView(inflater, container, savedInstanceState);
		return rootView;
	}

	/**
	 * 此方法会被onCreateView 调用，生成的View将会被缓存
	 * 
	 * @param inflater
	 * @param container
	 * @param savedInstanceState
	 * @return
	 */
	public abstract View generateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState);

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onDetach() {
		super.onDetach();
		try {
			Field childFragmentManager = Fragment.class
					.getDeclaredField("mChildFragmentManager");
			childFragmentManager.setAccessible(true);
			childFragmentManager.set(this, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
