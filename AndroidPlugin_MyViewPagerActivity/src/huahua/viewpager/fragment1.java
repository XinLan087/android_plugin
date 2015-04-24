package huahua.viewpager;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.apkplugin.android.fragment.BasePluginFragment;


public class fragment1 extends BasePluginFragment{
	private View mMainView;
	private TextView tv;
	private Button btn;
	
	@Override
	public void onAttach(Activity activity) {
		System.out.println("fragment1.onAttach()");
		super.onAttach(activity);
	}
	
	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		System.out.println("fragment1.onHiddenChanged() " +hidden);
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.v("huahua", "fragment1-->onCreate()");
		
		LayoutInflater inflater = getActivity().getLayoutInflater();
		mMainView = inflater.inflate(R.layout.fragment1, (ViewGroup)getActivity().findViewById(R.id.viewpager), false);
		
		tv = (TextView)mMainView.findViewById(R.id.tv1);
		btn = (Button)mMainView.findViewById(R.id.btn1);
		btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				tv.setText("Hello Viewpager\"");
			}
		});
		
	}

	@Override
	public View generateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.v("huahua", "fragment1-->onCreateView()");
		
		ViewGroup p = (ViewGroup) mMainView.getParent(); 
        if (p != null) { 
            p.removeAllViewsInLayout(); 
            Log.v("huahua", "fragment1-->移除已存在的View");
        } 
		
		return mMainView;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.v("huahua", "fragment1-->onDestroy()");
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.v("huahua", "fragment1-->onPause()");
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.v("huahua", "fragment1-->onResume()");
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.v("huahua", "fragment1-->onStart()");
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.v("huahua", "fragment1-->onStop()");
	}


}
