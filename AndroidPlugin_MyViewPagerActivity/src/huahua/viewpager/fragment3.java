package huahua.viewpager;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apkplugin.android.fragment.BasePluginFragment;

public class fragment3 extends BasePluginFragment{
	private View mMainView;
	@Override
	public void onAttach(Activity activity) {
		System.out.println("fragment3.onAttach()");
		super.onAttach(activity);
	}
	
	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		System.out.println("fragment3.onHiddenChanged() " +hidden);
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.v("huahua", "fragment3-->onCreate()");
		
		LayoutInflater inflater = getActivity().getLayoutInflater();
		mMainView = inflater.inflate(R.layout.fragment3, (ViewGroup)getActivity().findViewById(R.id.viewpager), false);
	}

	@Override
	public View generateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Log.v("huahua", "fragment3-->onCreateView()");
		
		ViewGroup p = (ViewGroup) mMainView.getParent(); 
        if (p != null) { 
            p.removeAllViewsInLayout(); 
            Log.v("huahua", "fragment3-->移除已存在的View");
        } 
		
		return mMainView;
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.v("huahua", "fragment3-->onDestroy()");
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.v("huahua", "fragment3-->onPause()");
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.v("huahua", "fragment3-->onResume()");
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.v("huahua", "fragment3-->onStart()");
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.v("huahua", "fragment3-->onStop()");
	}

}

