package com.apkplugin.android.fragment;

import java.util.HashMap;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Implementation of {@link android.support.v4.view.PagerAdapter} that
 * represents each page as a {@link Fragment} that is persistently kept in the
 * fragment manager as long as the user can return to the page.
 * 
 * <p>
 * This version of the pager is best for use when there are a handful of
 * typically more static fragments to be paged through, such as a set of tabs.
 * The fragment of each page the user visits will be kept in memory, though its
 * view hierarchy may be destroyed when not visible. This can result in using a
 * significant amount of memory since fragment instances can hold on to an
 * arbitrary amount of state. For larger sets of pages, consider
 * {@link FragmentStatePagerAdapter}.
 * 
 * <p>
 * When using FragmentPagerAdapter the host ViewPager must have a valid ID set.
 * </p>
 * 
 * <p>
 * Subclasses only need to implement {@link #getItem(int)} and
 * {@link #getCount()} to have a working adapter.
 * 
 * <p>
 * Here is an example implementation of a pager containing fragments of lists:
 * 
 * {@sample
 * development/samples/Support4Demos/src/com/example/android/supportv4/app/
 * FragmentPagerSupport.java complete}
 * 
 * <p>
 * The <code>R.layout.fragment_pager</code> resource of the top-level fragment
 * is:
 * 
 * {@sample development/samples/Support4Demos/res/layout/fragment_pager.xml
 * complete}
 * 
 * <p>
 * The <code>R.layout.fragment_pager_list</code> resource containing each
 * individual fragment's layout is:
 * 
 * {@sample development/samples/Support4Demos/res/layout/fragment_pager_list.xml
 * complete}
 */
public abstract class FragmentPagerAdapter extends PagerAdapter {
	private static final String TAG = "FragmentPagerAdapter";
	private static final boolean DEBUG = false;

	private final FragmentManager mFragmentManager;

	private FragmentTransaction mCurTransaction = null;

	/**
	 * 当前的Frgament
	 */
	private Fragment mCurrentPrimaryItem = null;
	/**
	 * Fragment的缓存
	 */
	private HashMap<String, Fragment> cacheFragmentMap = null;
	/**
	 * 当前page索引（切换之前）
	 */
	private int currentPageIndex = 0;
	/**
	 * ViewPager切换页面时的额外功能添加接口
	 */
	private OnExtraPageChangeListener onExtraPageChangeListener;
	/**
	 * 自定义的PageChangeListener
	 */
	private MyPageChangeListener myPageChangeListener = null;

	/**
	 * 初始化一个FragmentPagerAdapter
	 * 
	 * @param fm
	 *            {@link FragmentManager}
	 */
	public FragmentPagerAdapter(FragmentManager fm) {
		mFragmentManager = fm;
		myPageChangeListener = new MyPageChangeListener();
		cacheFragmentMap = new HashMap<String, Fragment>();
	}

	/**
	 * 取得当前索引
	 * 
	 * @return
	 */
	public int getCurrentPageIndex() {
		return currentPageIndex;
	}

	public MyPageChangeListener getPageChangeListener() {
		return myPageChangeListener;
	}

	/**
	 * 取得和索引相关联的Fragent，抽象方法必须实现
	 */
	public abstract Fragment getItem(int position);

	@Override
	public void startUpdate(ViewGroup container) {
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		if (mCurTransaction == null) {
			mCurTransaction = mFragmentManager.beginTransaction();
		}

		final long itemId = getItemId(position);

		// Do we already have this fragment?
		String name = makeFragmentName(container.getId(), itemId);
		Fragment fragment = mFragmentManager.findFragmentByTag(name);
		if (fragment != null) {
			if (DEBUG)
				Log.v(TAG, "Attaching item #" + itemId + ": f=" + fragment);
			mCurTransaction.attach(fragment);
		} else {

			fragment = cacheFragmentMap.get("" + position);
			if (fragment == null) {
				// TODO 处理Fragment缓存
				fragment = getItem(position);
				cacheFragmentMap.put("" + position, fragment);
			}

			if (DEBUG)
				Log.v(TAG, "Adding item #" + itemId + ": f=" + fragment);
			mCurTransaction.add(container.getId(), fragment,
					makeFragmentName(container.getId(), itemId));
		}
		if (fragment != mCurrentPrimaryItem) {
			fragment.setMenuVisibility(false);
			fragment.setUserVisibleHint(false);
		}

		return fragment;
	}

	/**
	 * 取得当前的Fragment
	 * 
	 * @return
	 */
	public Fragment getCurrentPrimaryItem() {
		return mCurrentPrimaryItem;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		if (mCurTransaction == null) {
			mCurTransaction = mFragmentManager.beginTransaction();
		}
		if (DEBUG)
			Log.v(TAG, "Detaching item #" + getItemId(position) + ": f="
					+ object + " v=" + ((Fragment) object).getView());
		mCurTransaction.detach((Fragment) object);
	}

	@Override
	public void setPrimaryItem(ViewGroup container, int position, Object object) {
		Fragment fragment = (Fragment) object;
		if (fragment != mCurrentPrimaryItem) {
			if (mCurrentPrimaryItem != null) {
				mCurrentPrimaryItem.setMenuVisibility(false);
				mCurrentPrimaryItem.setUserVisibleHint(false);
			}
			if (fragment != null) {
				fragment.setMenuVisibility(true);
				fragment.setUserVisibleHint(true);
			}
			mCurrentPrimaryItem = fragment;
		}
	}

	@Override
	public void finishUpdate(ViewGroup container) {
		if (mCurTransaction != null) {
			mCurTransaction.commitAllowingStateLoss();
			mCurTransaction = null;
			mFragmentManager.executePendingTransactions();
		}
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return ((Fragment) object).getView() == view;
	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void restoreState(Parcelable state, ClassLoader loader) {
	}

	/**
	 * Return a unique identifier for the item at the given position.
	 * 
	 * <p>
	 * The default implementation returns the given position. Subclasses should
	 * override this method if the positions of items can change.
	 * </p>
	 * 
	 * @param position
	 *            Position within this adapter
	 * @return Unique identifier for the item at position
	 */
	public long getItemId(int position) {
		return position;
	}

	private static String makeFragmentName(int viewId, long id) {
		return "android:switcher:" + viewId + ":" + id;
	}

	/**
	 * 实现 OnPageChangeListener
	 * 
	 */
	class MyPageChangeListener implements OnPageChangeListener {
		/**
		 * 此方法是在状态改变的时候调用，其中arg0这个参数有三种状态（0，1，2）。 arg0
		 * ==1的时辰默示正在滑动，arg0==2的时辰默示滑动完毕了，arg0==0的时辰默示什么都没做。
		 */
		@Override
		public void onPageScrollStateChanged(int arg0) {
			if (null != onExtraPageChangeListener) { // 如果设置了额外功能接口
				onExtraPageChangeListener.onExtraPageScrollStateChanged(arg0);
			}
		}

		/**
		 * 当页面在滑动的时候会调用此方法，在滑动被停止之前，此方法回一直得到调用。 其中三个参数的含义分别为： arg0
		 * :当前页面，及你点击滑动的页面 arg1:当前页面偏移的百分比 arg2:当前页面偏移的像素位置
		 */
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			if (null != onExtraPageChangeListener) { // 如果设置了额外功能接口
				onExtraPageChangeListener.onExtraPageScrolled(arg0, arg1, arg2);
			}
		}

		/**
		 * 此方法是页面跳转完后得到调用，arg0是你当前选中的页面的Position（位置编号）。
		 */
		@Override
		public void onPageSelected(int arg0) {
			Fragment fragment = getItem(currentPageIndex);
			if (fragment instanceof BasePluginFragment) {
				BasePluginFragment basePluginFragment = (BasePluginFragment) fragment;
				basePluginFragment.onHidden();
			}
			currentPageIndex = arg0;
			if (null != onExtraPageChangeListener) { // 如果设置了额外功能接口
				onExtraPageChangeListener.onExtraPageSelected(arg0);
			}
		}
	}

	

	/**
	 * 得到页面切换额外功能监听器
	 * 
	 * @return onExtraPageChangeListener
	 */
	public OnExtraPageChangeListener getOnExtraPageChangeListener() {
		return onExtraPageChangeListener;
	}

	/**
	 * 设置页面切换额外功能监听器
	 * 
	 * @param onExtraPageChangeListener
	 */
	public void setOnExtraPageChangeListener(
			OnExtraPageChangeListener onExtraPageChangeListener) {
		this.onExtraPageChangeListener = onExtraPageChangeListener;
	}
}