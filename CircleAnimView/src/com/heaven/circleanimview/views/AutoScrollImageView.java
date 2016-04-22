package com.heaven.circleanimview.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import com.heaven.circleanimview.R;
import com.heaven.circleanimview.bean.FlyBean;
import com.heaven.circleanimview.utils.LogUtil;

/**
 *自动滚动的imageview<br>
 *目前的方案：两张图片交互设置显示位置。 保证单个图片的宽度>=屏幕的宽度。<br> <br> 
 */
public class AutoScrollImageView extends View {

    private Paint paint = null;
    private Bitmap bitmap,bitmap2;
    /**
     * 第一张图片的left坐标
     */
    private int leftone = 0;
    /**
     * 第二张图片的left坐标
     */
    private int lefttwo = 0;
    /**
     * 第一张图片的宽度
     */
    private int width1;
    /**
     * 第二张图片的宽度
     */
    private int width2;
    /**
     * 屏幕的宽度：如果屏幕的宽度大于图片的宽度，注意处理。
     */
    private int screenWidth = 1080;
    
    private int top = 0;
    /**
     * 越小越慢，保证是2张图片宽度的公约数
     */
    private int step = 1;
    /**
     * 移动值
     */
    private int allStep = 0;
    /**
     * 2张图片交替显示的判断
     */
    private boolean isRightFirst = true;
    
    /**
     * 0 left  1 right -1 不滚动<br>
     * 可以改成枚举enum<br>
     */
    private int direction = -1;
    
    
    public AutoScrollImageView(Context context) {
        super(context);
        init();
    }

    public AutoScrollImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AutoScrollImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setWillNotDraw(false);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);// 空心圆
        
        screenWidth = getContext().getResources().getDisplayMetrics().widthPixels;
        initBitmap();
        
        direction = -1;
        if(direction==1){// right  (0,-1080)
        	leftone = screenWidth-width1;
            lefttwo = leftone+(-width2);
        }else  if(direction==0){// left  (0,1080)
        	leftone = screenWidth-width1;
            lefttwo = leftone+width2;
        }else{
        	leftone = 0;
        }
        LogUtil.e(this, "init leftone = "+ leftone + " lefttwo="+lefttwo + " screenWidth = "+screenWidth);
    }

	private void initBitmap() {
		try {
			//如果图片过大，等比压缩下。 但是要保证，图片的宽度==屏幕宽度 。 否则onDraw中就只能通过clipRect来处理图片了
			/*BitmapFactory.Options bfoptions = new BitmapFactory.Options();
			bfoptions.inJustDecodeBounds = true;
			bitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.asiv1,bfoptions);
			bitmap2 = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.asiv2,bfoptions);
			bfoptions.inSampleSize = 2;
			bfoptions.inPreferredConfig = Bitmap.Config.RGB_565;
			bfoptions.inJustDecodeBounds = false;
			LogUtil.e(this, "initBitmap outWidth = "+ bfoptions.outWidth + " outHeight="+bfoptions.outHeight );
			bitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.asiv1,bfoptions);
			bitmap2 = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.asiv2,bfoptions);
			*/
			bitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.as1);
			bitmap2 = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.as3);
			if(bitmap!=null){
				width1 = bitmap.getWidth();
			}
			if(bitmap2!=null){
				width2 = bitmap2.getWidth();
			}
			// 图片初始化完，放到图片框架中，保存到内存和sdcard
			
			
			LogUtil.e(this, "initBitmap width1 = "+ width1 + " width2="+width2 );
		} catch (OutOfMemoryError e) { // 有可能内存溢出,如果内存溢出，清理内存(图片框架中)，再次初始化
			LogUtil.e(this, "内存溢出。请添加逻辑：清理内存，再次初始化    OutOfMemoryError" );
			e.printStackTrace();
		}
	}
   
    
    @Override
    protected void onDraw(Canvas canvas) {
        if(direction==1){// 右滑
        	 if(bitmap!=null){
             	// 像右移动，交互显示(0,-1080) (-1080,0)
             	canvas.drawBitmap(bitmap, leftone, top, paint);
             	canvas.drawBitmap(bitmap2,lefttwo, top, paint);
             	leftone+=step;
             	lefttwo+=step;
             	allStep+=step;
//             	LogUtil.e(this, "leftone = "+ leftone + " lefttwo="+lefttwo + " width1="+width1);
             	if(allStep == width1 && isRightFirst){
             		//第二张图片在前，第一张图片在后
             		lefttwo = screenWidth-width2;
             		leftone = lefttwo+(-width1);
             		LogUtil.e(this, allStep+"===== leftone = "+ leftone + " lefttwo="+lefttwo + " width1="+width1);
             		allStep = 0;
             		isRightFirst = false;
             	}else if(allStep == width2 && !isRightFirst){
             	    //第1张图片在前，第2张图片在后
             		leftone = screenWidth-width1;
             		lefttwo = leftone+(-width2);
             		LogUtil.e(this, allStep+"****** leftone = "+ leftone + " lefttwo="+lefttwo + " width1="+width1);
             		allStep = 0;
             		isRightFirst = true;
             	}
             	invalidate();
             }
        }else if(direction==0){// 左滑
        	 if(bitmap!=null){
              	// 像左移动，交互显示(0,1080) (1080,0)
              	canvas.drawBitmap(bitmap, leftone, top, paint);
              	canvas.drawBitmap(bitmap2,lefttwo, top, paint);
              	leftone-=step;
              	lefttwo-=step;
              	allStep+=step;
//              	LogUtil.e(this, "left ----leftone = "+ leftone + " lefttwo="+lefttwo + " width1="+width1);
              	if(allStep == width1 && isRightFirst){
              		lefttwo = screenWidth-width2;
             		leftone = lefttwo+width1;
                    allStep = 0;
             		isRightFirst = false;
              		LogUtil.e(this, "left ----leftone = "+ leftone + " lefttwo="+lefttwo + " width1="+width1);
              	}else if(allStep == width2 && !isRightFirst){
              		leftone = screenWidth-width1;
                    lefttwo = leftone+width2;
             		allStep = 0;
              		isRightFirst = true;
              		LogUtil.e(this, "left ----leftone = "+ leftone + " lefttwo="+lefttwo + " width1="+width1);
              	}
              	invalidate();
              }
        }else{// 不滑动
        	 if(bitmap!=null){
        		 canvas.drawBitmap(bitmap, leftone, top, paint);
        	 }
        }
    }

	/**
	 * @return the direction
	 */
	public int getDirection() {
		return direction;
	}

	/**
	 * 1,right  0 left -1 不滚动
	 * @param direction 
	 */
	public void setDirection(int direction) {
		this.direction = direction;
		if(direction==1){
        	leftone = screenWidth-width1;
            lefttwo = screenWidth-width1-width2;
        }else if(direction==0){
        	leftone = screenWidth-width1;
            lefttwo = screenWidth-width1+width2;
        }else{
        	leftone = 0;
        }
		invalidate();
	}
	
	
	/**
	 * 
	 * @param direction   1,right  0 left -1 不滚动
	 * @param step  越小越慢，保证是屏幕宽度的整数倍
	 */
	public void startScroll(int direction,int step){
		this.direction = direction;
		this.step = step;
		if(screenWidth%step!=0){
			throw new RuntimeException("step 是2张图片的公约数");
		}
		if(direction==1){
        	leftone = screenWidth-width1;
            lefttwo = screenWidth-width1-width2;
        }else if(direction==0){
        	leftone = screenWidth-width1;
            lefttwo = screenWidth-width1+width2;
        }else{
        	leftone = 0;
        }
		invalidate();
	}

	
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		LogUtil.e(this, "onDetachedFromWindow----");
		if(bitmap!=null && !bitmap.isRecycled()){
			bitmap.recycle();
		}
		if(bitmap2!=null && !bitmap2.isRecycled()){
			bitmap2.recycle();
		}
		System.gc();
//		D/dalvikvm: <GC_Reason> <Amount_freed>, <Heap_stats>, <Pause_time>, <Total_time>
//		           GC_FOR_ALLOC freed 4691K, 10% free 49047K/54412K, paused 21ms, total 21ms

	}
    
}
