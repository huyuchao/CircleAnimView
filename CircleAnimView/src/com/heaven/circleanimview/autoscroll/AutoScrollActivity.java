package com.heaven.circleanimview.autoscroll;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.heaven.circleanimview.CAnimXfermodeActivity;
import com.heaven.circleanimview.R;
import com.heaven.circleanimview.views.AutoScrollImageView;

public class AutoScrollActivity extends Activity implements OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.auto_scroll_main);
		((AutoScrollImageView)findViewById(R.id.asiv_left_top)).startScroll(0,1);
		((AutoScrollImageView)findViewById(R.id.asiv_right)).startScroll(1, 1);
//		((AutoScrollImageView)findViewById(R.id.asiv_left)).setDirection(0);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_circle_xfermode:
			startActivity(new Intent(this, CAnimXfermodeActivity.class));
			break;
		}
	}
}
