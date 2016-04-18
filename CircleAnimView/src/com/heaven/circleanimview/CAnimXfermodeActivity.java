package com.heaven.circleanimview;

import com.heaven.circleanimview.views.CircleAnimXfermodeView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
/**
 * 
 * 通过xfermode模式做透明遮罩圆<br>
 * 
 */
public class CAnimXfermodeActivity extends Activity {

	CircleAnimXfermodeView cavx_circle;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.circle_anim_xfermode_main);
		cavx_circle = (CircleAnimXfermodeView) findViewById(R.id.cavx_circle);
		cavx_circle.setTag(1);
		cavx_circle.setVisibility(View.VISIBLE);
		cavx_circle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int tag = (Integer) v.getTag();
				if(tag == 1){
					v.setTag(0);
				}else{
					v.setTag(1);
				}
				cavx_circle.startAnim(tag);
			}
		});
	}
}
