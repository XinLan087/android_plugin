package huahua.viewpager;

import java.util.ArrayList;
import java.util.List;

import com.apkplugin.android.activity.BasePluginFragmentActivity;
import com.apkplugin.android.proxy.BaseProxyFragmentActivity;

import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.ViewGroup;

public class MainActivity extends BasePluginFragmentActivity {
	private ViewPager m_vp;
	private fragment1 mfragment1;
	private fragment2 mfragment2;
	private fragment3 mfragment3;
	// 页面列表
	private ArrayList<Fragment> fragmentList;
	// 标题列表
	ArrayList<String> titleList = new ArrayList<String>();
	// 通过pagerTabStrip可以设置标题的属性
	private PagerTabStrip pagerTabStrip;

	private PagerTitleStrip pagerTitleStrip;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setTheme(android.R.style.Theme_Light);
		m_vp = (ViewPager) findViewById(R.id.viewpager);
		pagerTabStrip = (PagerTabStrip) findViewById(R.id.pagertab);
		// 设置下划线的颜色
		pagerTabStrip.setTabIndicatorColor(getResources().getColor(
				android.R.color.holo_green_dark));
		// 设置背景的颜色
		pagerTabStrip.setBackgroundColor(getResources().getColor(
				android.R.color.holo_blue_dark));

		pagerTitleStrip = (PagerTitleStrip) findViewById(R.id.pagertab);
		// 设置背景的颜色
		pagerTitleStrip.setBackgroundColor(getResources().getColor(
				android.R.color.holo_blue_dark));

		mfragment1 = new fragment1();
		mfragment2 = new fragment2();
		mfragment3 = new fragment3();

		fragmentList = new ArrayList<Fragment>();
		fragmentList.add(mfragment1);
		fragmentList.add(mfragment2);
		fragmentList.add(mfragment3);

		titleList.add("第一页 ");
		titleList.add("第二页");
		titleList.add("第三页 ");

		m_vp.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager()));
		m_vp.setOffscreenPageLimit(1);
	}

	public class MyViewPagerAdapter extends FragmentPagerAdapter {
		public MyViewPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int arg0) {
			return fragmentList.get(arg0);
		}

		@Override
		public int getCount() {
			return fragmentList.size();
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return titleList.get(position);
		}

	}

}
