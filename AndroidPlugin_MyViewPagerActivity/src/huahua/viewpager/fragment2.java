package huahua.viewpager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apkplugin.android.fragment.BasePluginFragment;

public class fragment2 extends BasePluginFragment {
	private View mMainView;

	@Override
	public void onAttach(Activity activity) {
		System.out.println("fragment2.onAttach()");
		super.onAttach(activity);
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		System.out.println("fragment2.onHiddenChanged() " + hidden);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.v("huahua", "fragment2-->onCreate()");

		LayoutInflater inflater = getActivity().getLayoutInflater();
		mMainView = inflater.inflate(R.layout.fragment2,
				(ViewGroup) getActivity().findViewById(R.id.viewpager), false);
	}

	@Override
	public View generateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Log.v("huahua", "fragment2-->onCreateView()");

		ViewGroup p = (ViewGroup) mMainView.getParent();
		if (p != null) {
			p.removeAllViewsInLayout();
			Log.v("huahua", "fragment2-->移除已存在的View");
		}

		return mMainView;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.v("huahua", "fragment2-->onDestroy()");
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.v("huahua", "fragment2-->onPause()");
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.v("huahua", "fragment2-->onResume()");
	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.v("huahua", "fragment2-->onStart()");
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.v("huahua", "fragment2-->onStop()");
	}

}
