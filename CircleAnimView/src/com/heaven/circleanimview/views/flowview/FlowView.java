package com.heaven.circleanimview.views.flowview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.heaven.circleanimview.R;
import com.heaven.circleanimview.bean.FlyBean;
import com.heaven.circleanimview.utils.GlideUtil;
import com.heaven.circleanimview.utils.LogUtil;
import com.heaven.circleanimview.utils.PhoneInfoUtil;

/**
 * 无用
 */
public class FlowView  extends RelativeLayout{

    public ImageView iv_fly;
    protected View view;
    protected Paint paint = null ;

    protected float radius = 23;
    
    protected Rect localRect = null;
    protected Handler handler;
    protected FlyBean fly;
    protected boolean isCircleAnim = false;

    public FlowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setWillNotDraw(false);
        view = LayoutInflater.from(context).inflate(R.layout.flow_layout,this);
        iv_fly = (ImageView) view.findViewById(R.id.iv_fly);
        paint = new Paint();
        paint.setAntiAlias(true);
        setClipChildren(false);
    }

    public FlowView(Context context) {
        super(context);
    }

    public FlowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setWillNotDraw(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        LogUtil.e("onDraw", "onDraw = ========== isCircleAnim = " +isCircleAnim + " w = "+ PhoneInfoUtil.widthPixels + " h = "+PhoneInfoUtil.heightPixels);
        canvas.drawColor(getContext().getResources().getColor(R.color.c4));
        if (isCircleAnim){
            paint.setColor(getContext().getResources().getColor(R.color.c5));
            if(radius < PhoneInfoUtil.widthPixels/2){
                canvas.drawCircle(PhoneInfoUtil.widthPixels/2,PhoneInfoUtil.heightPixels/2,radius,paint);
                radius +=10;
                invalidate();
            }else{
                paint.setColor(Color.parseColor("#ffff0000"));
                canvas.drawCircle(PhoneInfoUtil.widthPixels/2,PhoneInfoUtil.heightPixels/2,radius,paint);
                Message msg = handler.obtainMessage();
                msg.obj = fly;
                msg.what = 100;
                handler.sendMessage(msg);
            }
        }
        super.onDraw(canvas);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 控件本身的大小
        final int selfWidthSize = getDefaultSize(0, widthMeasureSpec);
        final int selfHeightSize = getDefaultSize(0, heightMeasureSpec);
        LogUtil.e("onMeasure", "widthMeasureSpec = " + selfWidthSize+ " heightMeasureSpec=" + selfHeightSize);
//        setMeasuredDimension(selfWidthSize,2560);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        LogUtil.e(this, "onLayout = " + l + " = " + l + " t = " + t + " r=" + r + " b = " + b + " ");
//        super.onLayout(changed, l, t, r, b);
        if (localRect != null) {
            ViewGroup vg = (ViewGroup) getChildAt(0);
            vg.getChildAt(0).layout(localRect.left, localRect.top, localRect.right, localRect.bottom);
            LogUtil.e(this, "onLayout = " + l + " = " + l + " t = " + t + " r=" + r + " b = " + b + " iv = " + vg.getChildAt(0));
            LogUtil.e(this, "onLayout  width = " + vg.getChildAt(0).getWidth() + " height = "+ vg.getChildAt(0).getHeight());
        }
    }

 

    /**
     * 执行动画。 通过handler发送消息： what==2 添加fragment  what ==100 动画执行结束
     * @param fly
     * @param handler
     */
    public void setVisibility(FlyBean fly,Handler handler ) {
        this.handler = handler;
        this.localRect = fly.rect;
        this.fly = fly;
        LogUtil.e(this, "setVisibility rect = " + localRect.toString());
        setVisibility(View.VISIBLE);
        invalidate();
        FlowViewHelper.startAnim(getContext(),fly,this,handler);
        GlideUtil.displayImage(getContext(), fly.movie.movie_img_url,iv_fly);
        requestLayout();
    }

   
    public void startCircleAnim(Handler handler){
        this.handler = handler;
        isCircleAnim = true;
        invalidate();
    }


}
