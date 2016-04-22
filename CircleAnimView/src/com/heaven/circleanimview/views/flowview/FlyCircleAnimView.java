package com.heaven.circleanimview.views.flowview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Xfermode;
import android.graphics.Paint.Style;
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
 * 画布的宽度高度需要初始化<br>
 * 圆点坐标和图片的宽高也是需要初始化的，这个跟详情界面的图片位置有关<br>
 * maxRadius根据圆心点算出<br>
 */
public class FlyCircleAnimView  extends RelativeLayout{

    protected Handler handler;
    protected FlyBean fly;
    public ImageView iv_fly;
	private Canvas myCanvas; // 画布
    private Bitmap originalBitmap; // 画布bitmap

    /**
     * 画笔
     */
    protected Paint paint = null ;
    /**
	 * 画布重叠部分的显示模式
	 */
	private Xfermode xfermode ;
    
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
	private int canvaswWidth = 1080;
	/**
	 * 画布的高度
	 */
	private int canvasHeight = 1920;
	/**
	 * 遮罩层颜色
	 */
	private int maskColor = Color.RED;
    
	/**
	 * 放大还是收缩  true 放大，false 收缩
	 */
	boolean isBigZoom = true;
	/**
	 * 图片的点击位置
	 */
    protected Rect localRect = null;
   
    /**
     * false 不绘制圆。 true 绘制
     */
    protected boolean isCircleAnim = false;

    public FlyCircleAnimView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.flow_layout,this);
        iv_fly = (ImageView)findViewById(R.id.iv_fly);
        init(context);
		
    }

    public FlyCircleAnimView(Context context) {
        super(context);
        init(context);
    }

    public FlyCircleAnimView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
       
    }
    /**
     * 可以不用初始化，在外部初始化数据
     * @param context
     */
	private void init(Context context){
		this.setWillNotDraw(false);
        setClipChildren(false);
        
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setStyle(Style.FILL);
		
		canvaswWidth = context.getResources().getDisplayMetrics().widthPixels;
		canvasHeight = context.getResources().getDisplayMetrics().heightPixels;
		
		xfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_OUT);
		
		maskColor = Color.parseColor("#c8c8c8");
		originalBitmap = Bitmap.createBitmap(canvaswWidth, canvasHeight, Bitmap.Config.ARGB_4444);
		myCanvas = new Canvas(originalBitmap);
//		myCanvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG));
		myCanvas.drawColor(maskColor); // 默认背景
		/**
		 * 根据圆心点坐标算出  maxRadius  向上取整，保证圆的边界要在屏幕外。
		 * 有些底部导航栏可以隐藏的，获取的高度是不准确的，要有对应的高度调整逻辑。
		 */
		maxRadius = (int) Math.ceil(Math.sqrt((canvaswWidth - cx)*(canvaswWidth - cx) +(canvasHeight - cy)*(canvasHeight - cy)));
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
        /*if (isCircleAnim){
        	canvas.drawColor(Color.parseColor("#c8c8c8"));
        	LogUtil.e(this, "onDraw = ========== isCircleAnim = " +isCircleAnim + " w = "+ PhoneInfoUtil.widthPixels + " h = "+PhoneInfoUtil.heightPixels);
        	paint.setXfermode(null);
            paint.setColor(getContext().getResources().getColor(R.color.c5));
            if(radius < PhoneInfoUtil.widthPixels/2){
                canvas.drawCircle(PhoneInfoUtil.widthPixels/2,PhoneInfoUtil.heightPixels/2,radius,paint);
                radius +=10;
                invalidate();
            }else{
                paint.setColor(Color.parseColor("#ffff0000"));
                canvas.drawCircle(PhoneInfoUtil.widthPixels/2,PhoneInfoUtil.heightPixels/2,radius,paint);
                sendMessage(fly,100);
            }
        }else { 
        	zoomDraw(canvas);
        }*/
    	if(isCircleAnim){
    		zoomDraw(canvas);
    	}else{
    	}
    }

	/**
	 * 110 放大圆绘制完毕   111 缩小圆绘制完毕
	 */
	private void sendMessage(FlyBean fly,int what) {
		if(handler!=null){
			Message msg = handler.obtainMessage();
			msg.obj = fly;
			msg.what = what;
			handler.sendMessage(msg);
		}
	}

	/**
	 * @param canvas
	 */
	private void zoomDraw(Canvas canvas) {
		try {
			// 记得还原透明背景，否则绘制圆的操作不显示(背景一直是灰色的)
			setBackgroundColor(Color.parseColor("#00000000"));
//			canvas.drawColor(maskColor);
			LogUtil.e(this, "zoomDraw isBigZoom = "+isBigZoom + " radius ="+radius);
			if(isBigZoom){
				if(radius<=maxRadius){ // 放大
					paint.setXfermode(null);
					paint.setColor(maskColor);
					myCanvas.drawRect(0, 0, canvaswWidth, canvasHeight, paint);
					// 和之前的画布做交互处理。 在该步骤之前可以重绘画布
					paint.setXfermode(xfermode);
//					paint.setColor(Color.BLUE);//可以不用设置颜色
					myCanvas.drawCircle(cx, cy, radius, paint);
					
					radius+=step;
					invalidate();
				}else{
					sendMessage(fly,110);
				}
			}else{// 缩小
				if(radius<=maxRadius && radius >=0){
					paint.setXfermode(null); // 如果不设置为null，每次都会和之前的画布做重叠运算操作。 
					paint.setColor(maskColor); 
					myCanvas.drawRect(0, 0, canvaswWidth, canvasHeight, paint);
					// SRC_IN  取两层绘制交集，显示上层(后来画的)    DST_IN    取两层绘制交集。显示下层。
					// DST_OUT  取下层绘制非交集部分。    SRC_OUT  取上层绘制非交集部分(上层的非交集部分)
//					paint.setColor(Color.BLUE);  // out 下层颜色设置无效
					paint.setXfermode(xfermode);
					myCanvas.drawCircle(cx, cy, radius, paint);
					
					radius-=step;
//					Thread.sleep(1000);
					invalidate();
				}else{
					paint.setXfermode(null);
					paint.setColor(maskColor);
					myCanvas.drawRect(0, 0, canvaswWidth, canvasHeight, paint);
					paint.setXfermode(xfermode);
					myCanvas.drawCircle(cx, cy, maxRadius, paint);
//					myCanvas.drawColor(maskColor);
					sendMessage(fly,111);
				}
			}
//			Thread.sleep(1000);
			canvas.drawBitmap(originalBitmap, 0, 0, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        LogUtil.e(this, "onLayout = " + l + " = " + l + " t = " + t + " r=" + r + " b = " + b + " ");
        super.onLayout(changed, l, t, r, b);
        if (localRect != null) {
            ViewGroup vg = (ViewGroup) getChildAt(0);
            // 固定图片的位置
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
        isCircleAnim = false;
        radius = defaultRadius;
        LogUtil.e(this, "setVisibility rect = " + localRect.toString());
        setBackgroundColor(maskColor); 
        setVisibility(View.VISIBLE);
        
        FlowViewHelper.startAnim(getContext(),fly,this,handler);
        GlideUtil.displayImage(getContext(), fly.movie.movie_img_url,iv_fly);
        requestLayout();
        invalidate();
    }
    
    public void revertAnim(FlyBean fly,Handler handler){
    	isCircleAnim = false;
    	FlowViewHelper.revertAnim(getContext(),fly,this,handler);
    }

   
    public void startCircleAnim(Handler handler){
        this.handler = handler;
        isCircleAnim = false;
        invalidate();
    }
    
    /**
	 * 
	 * @param tag   1 放大    0 缩小
	 */
	public void startAnim(int tag){
		isCircleAnim = true;
		if(tag == 1){
			isBigZoom = true;
			System.out.println("startAnim==============放大");
			radius = defaultRadius;
			invalidate();
		}else{
			setVisibility(View.VISIBLE);
			isBigZoom = false;
			System.out.println("startAnim==============收缩");
			radius = maxRadius;
			invalidate();
		}
		
	}

	/**
	 * @return the imgLeft
	 */
	public int getImgLeft() {
		return imgLeft;
	}

	/**
	 * @param imgLeft the imgLeft to set
	 */
	public void setImgLeft(int imgLeft) {
		this.imgLeft = imgLeft;
	}

	/**
	 * @return the imgTop
	 */
	public int getImgTop() {
		return imgTop;
	}

	/**
	 * @param imgTop the imgTop to set
	 */
	public void setImgTop(int imgTop) {
		this.imgTop = imgTop;
	}
	
	
	
    
}
