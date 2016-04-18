package com.heaven.circleanimview.flowview;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;

import com.heaven.circleanimview.BaseFragment;
import com.heaven.circleanimview.R;
import com.heaven.circleanimview.R.id;
import com.heaven.circleanimview.R.layout;
import com.heaven.circleanimview.utils.LogUtil;
import com.heaven.circleanimview.views.CircleAnimView;

/**
 *详情界面
 */
public class MovieDetailsFragment extends BaseFragment{

    public CircleAnimView cav_anim;
    public Handler handler;

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int layoutId() {
        return R.layout.movie_details_fragment;
    }

    @Override
    protected void findView() {
        cav_anim = (CircleAnimView)view.findViewById(R.id.cav_anim);

    }

    @Override
    protected void setOnClickListener() {
        view.findViewById(R.id.btn_anim).setOnClickListener(this);
        view.findViewById(R.id.btn_back).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_anim:
                cav_anim.startCircleAnim(true,handler);
                break;
            case R.id.btn_back:
                cav_anim.startCircleAnim(false,handler);
                break;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        View v = getView();
        LogUtil.e(tag, " v = " + ((ViewGroup) v.getParent()).toString() + " tag = " + ((ViewGroup) v.getParent()).getTag());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LogUtil.e(tag, " parent  = " + ((ViewGroup) view.getParent()) + " v = " + view.toString());
        if (view!=null && ((ViewGroup) view.getParent())!=null ){
            String viewInfo = ((ViewGroup) view.getParent()).toString();
            LogUtil.e(tag,  " parent viewInfo = "+ viewInfo + " v = "+ view.toString());
        }
    }
}
