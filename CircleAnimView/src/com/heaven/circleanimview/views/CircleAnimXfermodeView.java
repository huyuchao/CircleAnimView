/**
 * 
 */
package com.heaven.circleanimview.views;

import com.heaven.circleanimview.R;
import com.heaven.circleanimview.utils.LogUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.util.AttributeSet;
import android.view.View;

/**
 * 遮罩层处理，放大效果：渐渐显示布局界面。 缩小效果：渐渐隐藏布局界面。<br>
 * 可以通过回调或者hander做逻辑处理<br>
 * 动画时间通过改变step的大小来改变<br>
 * 注意bitmap的回收
 * @date 2016年4月13日 下午2:45:38
 */
public class CircleAnimXfermodeView extends View {

	/**
	 * 画布重叠部分的显示模式
	 */
	private Xfermode xfermode ;
	
	private Paint paint;
	private Canvas myCanvas;
	private Bitmap originalBitmap; // 画布bitmap
	/**
	 * 圆的半径
	 */
	private int radius = 2100;
	
	/**
	 * 最大半径，根据圆心点坐标算出
	 */
	private int maxRadius = 2000;
	/**
	 * 默认半径
	 */
	private int defaultRadius = 200;
	/**
	 * 放大 step   值越大执行的时间越短
	 */
	private int step = 150;
	
	/**
	 * 放大还是收缩  true 放大，false 收缩
	 */
	boolean isBigZoom = true;
	/**
	 * 图片的宽度
	 */
	private int imgWidth = 312;
	/**
	 * 图盘的高度
	 */
	private int imgHeight = 420;
	/**
	 * 图片距离左边的距离
	 */
	private int imgLeft = 36; // 和FlowViewHelper中的left值一样
	/**
	 * 图片距离上边的距离
	 */
	private int imgTop = 624;
	
	/**
	 * 圆的中心点 x坐标
	 */
	private int cx=imgLeft+imgWidth/2; // 32  312
	/**
	 * 圆的中心点 y坐标
	 */
	private int cy=imgTop+imgHeight/2; // 624  420
	/**
	 * 画布的宽度
	 */
	private int width = 1080;
	/**
	 * 画布的高度
	 */
	private int height = 1920;
	/**
	 * 遮罩层颜色
	 */
	private int maskColor = Color.RED;
	
	/**
	 * @param context
	 * @param attrs
	 * @param defStyleAttr
	 */
	public CircleAnimXfermodeView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public CircleAnimXfermodeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	/**
	 * @param context
	 */
	public CircleAnimXfermodeView(Context context) {
		super(context);
		init(context);
	}
	
	private void init(Context context){
		setWillNotDraw(false);
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setStyle(Style.FILL);
		
		xfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_OUT);
		
		originalBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
		myCanvas = new Canvas(originalBitmap);
//		myCanvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG));
//		myCanvas.drawColor(Color.TRANSPARENT); // 默认背景
		/*
		 * 根据圆心点坐标算出  maxRadius  向上取整，保证圆的边界要在屏幕外。
		 * 有些底部导航栏可以隐藏的，获取的高度是不准确的，要有对应的高度调整逻辑。
		 */
		maxRadius = (int) Math.ceil(Math.sqrt((width - cx)*(width - cx) +(height - cy)*(height - cy)));
		System.out.println("maxRadius = "+ maxRadius);
		radius = maxRadius+10; // 只是为了开始的时候不绘制
		
		
		 
		// 初始化图片的宽度和高度，图片的左上点坐标
		imgWidth = (int) context.getResources().getDimension(R.dimen.flycircle_img_width);
		imgHeight = (int) context.getResources().getDimension(R.dimen.flycircle_img_height);
		imgLeft = (int) context.getResources().getDimension(R.dimen.flycircle_img_left);;
		imgTop = (int) context.getResources().getDimension(R.dimen.flycircle_img_top);;
		cx=imgLeft+imgWidth/2; // 32  312
		cy=imgTop+imgHeight/2; // 624  420
		LogUtil.e(this, "imgWidth = "+ imgWidth + " imgHeight="+imgHeight 
				+ " imgLeft"+imgLeft + " imgTop="+imgTop
				+ " cx"+cx + " cy="+cy
				);
		
	}
	
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		try {
			if(isBigZoom){
				if(radius<=maxRadius){
					paint.setXfermode(null);
					paint.setColor(maskColor);
					myCanvas.drawRect(0, 0, width, height, paint);
					// 和之前的画布做交互处理。 在该步骤之前可以重绘画布
					paint.setXfermode(xfermode);
//					paint.setColor(Color.BLUE);//可以不用设置颜色
					myCanvas.drawCircle(cx, cy, radius, paint);
					
					radius+=step;
					invalidate();
				}
			}else{
				paint.setXfermode(null); // 如果不设置为null，每次都会和之前的画布做重叠运算操作。 
				paint.setColor(maskColor); 
				myCanvas.drawRect(0, 0, width, height, paint);
				// SRC_IN  取两层绘制交集，显示上层(后来画的)    DST_IN    取两层绘制交集。显示下层。
				// DST_OUT  取下层绘制非交集部分。    SRC_OUT  取上层绘制非交集部分(上层的非交集部分)
//				paint.setColor(Color.BLUE);  // out 下层颜色设置无效
				paint.setXfermode(xfermode);
				
				myCanvas.drawCircle(cx, cy, radius, paint);
				if(radius<=maxRadius && radius >=0){
					radius-=step;
//					Thread.sleep(1000);
					invalidate();
				}
			}
			System.out.println("end-------------------end");
			canvas.drawBitmap(originalBitmap, 0, 0, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		System.out.println("onAttachedToWindow = ");
	}
	
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		System.out.println("onDetachedFromWindow = ");
	}
	
	/**
	 * 
	 * @param tag   1 放大    0 缩小
	 */
	public void startAnim(int tag){
		if(tag == 1){
			isBigZoom = true;
			System.out.println("startAnim==============放大");
			radius = defaultRadius;
			invalidate();
		}else{
			isBigZoom = false;
			System.out.println("startAnim==============收缩");
			radius = maxRadius;
			invalidate();
		}
		
	}
	
}
