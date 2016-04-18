package com.heaven.circleanimview;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.heaven.circleanimview.utils.LogUtil;

/**
 * 
 *
 * @author HYC
 * @date 2015年9月21日 上午11:38:04
 */
public abstract class BaseFragment extends Fragment implements  View.OnClickListener{
	
	protected String tag = this.getClass().getSimpleName();
	public View view = null;
	
	/*@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		LogUtil.e(tag, "onAttach");
	}*/
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		LogUtil.e(tag, "onAttach");
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		LogUtil.e(tag, "onCreate");
		super.onCreate(savedInstanceState);
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		LogUtil.e(tag, "onActivityCreated");
		super.onActivityCreated(savedInstanceState);
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		LogUtil.e(tag, "onCreateView");
		view = inflater.inflate(layoutId(), container, false);
		init();
//		this.onHiddenChanged(false);
		return view;
	}

	/**
	 * 可以用base中的，也可以自己写。
	 * @param id
	 * @return
	 */
	protected View findViewById(int id) {
		return view.findViewById(id);
	}

	public void init() {
		findView();
		setOnClickListener();
		initData();
	}
	protected abstract void initData();

	/**
	 * 返回xml布局的layoutid
	 * @return
	 */
	protected abstract int layoutId();

	protected abstract void findView();

	protected abstract void setOnClickListener();

	@Override
	public void onClick(View v) {

	}
	
	@Override
	public void onStart() {
		LogUtil.e(tag, "onStart");
		super.onStart();
	}
	
	@Override
	public void onResume() {
		LogUtil.e(tag, "onResume");
		super.onResume();
	}

	@Override
	public void onPause() {
		LogUtil.e(tag, "onPause");
		super.onPause();
	}
	
	@Override
	public void onStop() {
		LogUtil.e(tag, "onStop");
		super.onStop();
	}
	
	@Override
	public void onDestroyView() {
		LogUtil.e(tag, "onDestroyView");
		super.onDestroyView();
	}
	@Override
	public void onDestroy() {
		LogUtil.e(tag, "onDestroy");
		super.onDestroy();
	}
	
	public void onDetach() {
		super.onDetach();
		LogUtil.e(tag, "onDetach");
	};
	
	@Override
	public void onLowMemory() {
		LogUtil.e(tag, "onLowMemory");
		super.onLowMemory();
	}
}
