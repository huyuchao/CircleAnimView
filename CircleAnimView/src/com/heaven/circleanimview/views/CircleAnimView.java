package com.heaven.circleanimview.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import com.heaven.circleanimview.R;
import com.heaven.circleanimview.bean.FlyBean;
import com.heaven.circleanimview.utils.LogUtil;

/**
 *
 */
public class CircleAnimView extends View {

    private Paint paint = null ;

    /**
     * 图片位置的x坐标
     */
    public static  int left = 32;

    /**
     * 图片位置的y坐标
     */
    public static int top = 624;

    public int width = 312;
    public int height = 624;

    /**
     * 放大 or 收缩  step   值越大执行的时间越短
     */
    public int step = 80;

    private float radius = 600;

    /**
     * 开始半径
     */
    private float defaultRadius = 10;

    /**
     * 最大半径，根据圆心点坐标算出
     */
    private float maxRadius = 4000;

    /**
     * 放大还是收缩  true 放大，false 收缩
     */
    private boolean isBigAnim =true ;

    /**
     * 画笔的宽度
     */
    private int strokeWidth = 2500;


    public CircleAnimView(Context context) {
        super(context);
        init();
    }

    public CircleAnimView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircleAnimView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setWillNotDraw(false);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);// 空心圆
        paint.setStrokeWidth(strokeWidth); // 以中心点为坐标的直角三角形的斜边长度
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(getContext().getResources().getColor(R.color.c4));
        if (isBigAnim){ // 放大动画
            if (radius< maxRadius){
                //绘制空心圆，半径渐渐增大
                if(fly!=null){
                    canvas.drawCircle((left + fly.width/2),top+fly.height/2,radius,paint);
                }else{
                    canvas.drawCircle((left+width/2),(top+height/2),radius,paint);
                }
                radius+=step;
                invalidate();
            }else{
                canvas.drawCircle((left+width/2),(top+height/2),0,paint);
            }
        }else{ // 缩小动画
            if (radius > 10){
                //绘制空心圆，半径渐渐减小
                if(fly!=null){
                    canvas.drawCircle((left + fly.width/2),top+fly.height/2,radius,paint);
                }else{
                    canvas.drawCircle((left+width/2),(top+height/2),radius,paint);
                }
                radius-=step;
                invalidate();
            }else{
                canvas.drawCircle((left+width/2),(top+height/2),0,paint);
                onDrawAfter();
            }
        }
    }

    /**
     * 绘制完成后的操作
     */
    private void onDrawAfter() {
        handler.sendEmptyMessage(101);
    }

    /**
     * 放大或者缩小动画
     * @param isBigAnim  true 放大  false 缩小
     */
    public void startCircleAnim(boolean isBigAnim,Handler handler){
        this.isBigAnim = isBigAnim;
        this.handler = handler;
        if (isBigAnim){
            radius = defaultRadius;
        }else {
            radius = maxRadius;
        }
        invalidate();
    }

    public FlyBean fly;
    private  Handler handler;
    public void startCircleAnim(FlyBean fly,boolean isBigAnim,Handler handler){
        this.fly = fly;
        this.handler = handler;
        LogUtil.e("startCircleAnim", "========== fly = "+fly  + "isBigAnim = "+isBigAnim );
        this.isBigAnim = isBigAnim;
        if (isBigAnim){
            radius = defaultRadius;
        }else {
            radius = maxRadius;
        }
        invalidate();
    }

}
