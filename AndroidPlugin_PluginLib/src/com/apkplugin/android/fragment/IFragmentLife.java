package com.apkplugin.android.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public interface IFragmentLife {
	public void onAttach(Activity activity);

	
	public void onHiddenChanged(boolean hidden);

	public void onCreate(Bundle savedInstanceState);

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState);

	public void onViewCreated(View view, Bundle savedInstanceState);

	public void onDestroy();

	public void onPause();

	public void onDetach();

	public void onResume();

	public void onStart();

	public void onStop();

	public void onDestroyView();
}
