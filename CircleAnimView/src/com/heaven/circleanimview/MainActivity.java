package com.heaven.circleanimview;

import com.heaven.circleanimview.flowview.FlowViewActivity;
import com.heaven.circleanimview.flycircleanim.FlyCircleActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends Activity implements OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_circle_xfermode:
			startActivity(new Intent(this, CAnimXfermodeActivity.class));
			break;
		case R.id.btn_flow_view:
			startActivity(new Intent(this, FlowViewActivity.class));
			break;
		case R.id.btn_fly_circle:
			startActivity(new Intent(this, FlyCircleActivity.class));
			break;
		}
	}
}
