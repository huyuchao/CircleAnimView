package com.heaven.circleanimview.views.flowview;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import com.heaven.circleanimview.R;
import com.heaven.circleanimview.bean.FlyBean;
import com.heaven.circleanimview.utils.LogUtil;


public class FlowViewHelper {

    private static String tag =FlowViewHelper.class.getSimpleName();

    /**
     * 移动结束位置的x坐标
     */
    public static  int left = 36;

    /**
    * 移动结束位置的y坐标
    */
    public static int top = 624;

    /**
     * 执行动画
     * @param context
     * @param flyBean
     * @param flowView  可以直接写成FlyCircleAnimView。 left和top直接引用 FlyCircleAnimView中的值
     * @param handler
     */
    public static void startAnim(Context context,final FlyBean flyBean ,final ViewGroup flowView, final Handler handler){
    	if(flowView instanceof FlyCircleAnimView){
    		FlyCircleAnimView flycircle = (FlyCircleAnimView) flowView;
    		left = flycircle.getImgLeft();
    		top = flycircle.getImgTop();
    	}
    	// 调整坐标
        int flyTop = flyBean.rect.top;
        if(flyTop>top){ // 当前view在目标view的下方  向上移动    left top  right 正确  bottom值不正确
        	flyBean.rect.bottom =  flyBean.rect.top+ flyBean.height;
            flyBean.rect.top -= flyBean.statusBarHeigh;
            flyBean. rect.bottom -= flyBean.statusBarHeigh;
        }else{// 当前view在目标view的上方 向下移动    bottom right 正确  left top值不正确
            flyBean.rect.top =  flyBean.rect.bottom - flyBean.height;
            flyBean.rect.top -= flyBean.statusBarHeigh;
            flyBean. rect.bottom -= flyBean.statusBarHeigh;
        }
        LogUtil.e(tag, "背景动画开始================");
        AlphaAnimation alpha = new AlphaAnimation(0.0f,1.0f);
        alpha.setDuration(800);
        flowView.startAnimation(alpha);// 在动画执行结束的时候，add fragment
        alpha.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                LogUtil.e(tag, "背景动画结束  add Fragment================");
                handler.sendEmptyMessage(2);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        LogUtil.e(tag, "图片位移动画开始================");
        TranslateAnimation translateAnimation = new TranslateAnimation(0,left- flyBean.rect.left,0,top- flyBean.rect.top);
        translateAnimation.setDuration(1200);
        translateAnimation.setFillEnabled(true);
        translateAnimation.setFillAfter(true);
        flowView.findViewById(R.id.iv_fly).startAnimation(translateAnimation);
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                LogUtil.e(tag, "图片位移动画结束 隐藏 flowview================");
//                flowView.setVisibility(View.GONE);
//                flowView.startCircleAnim(handler);

                Message msg = handler.obtainMessage();
                msg.obj = flyBean;
                msg.what = 100;
                handler.sendMessage(msg);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

	/**
	 * 图片移动到原来位置
	 * @param context
	 * @param fly
	 * @param flyCircleAnimView
	 * @param handler
	 */
	public static void revertAnim(Context context, FlyBean flyBean,final ViewGroup flowView, Handler handler) {
		  // 调整坐标
         int flyTop = flyBean.originalRect.top;
         if(flyTop > top){ // 原始图片在下方，向下移动
        	 flyBean.originalRect.bottom =  flyBean.originalRect.top+ flyBean.height;
             flyBean.originalRect.top -= flyBean.statusBarHeigh;
             flyBean.originalRect.bottom -= flyBean.statusBarHeigh;
         }else{// 原始图片在上方，向上移动
        	 flyBean.originalRect.top =  flyBean.originalRect.bottom - flyBean.height;
             flyBean.originalRect.top -= flyBean.statusBarHeigh;
             flyBean.originalRect.bottom -= flyBean.statusBarHeigh;
         }
		  LogUtil.e(tag, "revertAnim 背景动画开始================");
	      //float fromXDelta, float toXDelta, float fromYDelta, float toYDelta
	      TranslateAnimation translateAnimation = 
	    		  new TranslateAnimation(left-flyBean.originalRect.left,0,top-flyBean.originalRect.top,0);
	      translateAnimation.setDuration(1000);
	      translateAnimation.setFillEnabled(true);
	      translateAnimation.setFillAfter(true);
	      flowView.findViewById(R.id.iv_fly).startAnimation(translateAnimation);
	      
	      translateAnimation.setAnimationListener(new Animation.AnimationListener() {
	            @Override
	            public void onAnimationStart(Animation animation) {
	            }
	            @Override
	            public void onAnimationEnd(Animation animation) {
	                LogUtil.e(tag, "revertAnim  图片位移动画结束 隐藏 flowview================");
	                flowView.setVisibility(View.GONE);
	            }
	            @Override
	            public void onAnimationRepeat(Animation animation) {

	            }
	        });
	}
}











