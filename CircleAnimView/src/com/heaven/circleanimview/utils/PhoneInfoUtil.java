package com.heaven.circleanimview.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

/**
 * 获取手机信息
 * 
 * 1  240 1.5    100dp = 150px;<br>
 * 
 * @time 2014-2-19 下午1:57:53
 * @version 1.0
 * @author h-y-c
 */
public class PhoneInfoUtil {
	
	private static float scaledDensity;
	/**
	  * 屏幕密度：  160  240  320
	  */
	public static int densityDpi;
	/**
	 * 屏幕密度： 1  1.5  2
	 */
	public static  int density = 0;

	 /** 手机制造商 */
   public static String brand;
    /** 手机型号 */
   public static String model;
    /** imei   设备唯一标识id*/
   public static  String imei;
    /** 系统版本 */
   public static  String release;
    /** 网络名称（中国移动 联通 电信） */
   public static  String networkoperatorname;
    /** 屏幕像素 */
   public static String screensize;
   
   public static int widthPixels = 720;
   public static int heightPixels = 1280;
	
    public static String getPhoneInfo(Activity mActivity) {
        StringBuffer stringBuffer = new StringBuffer();
        try {
            brand = Build.BRAND; // 手机制造商
        } catch (Exception e) {
            brand = "NULL";
        }

        try {
            model = Build.MODEL; // 手机型号
        } catch (Exception e) {
            model = "NULL";
        }
        TelephonyManager tm = (TelephonyManager) mActivity.getSystemService(Context.TELEPHONY_SERVICE);
        try {
            imei = tm.getDeviceId(); // imei
        } catch (Exception e) {
            imei = "NULL";
        }
        try {
            networkoperatorname = tm.getNetworkOperatorName();
        } catch (Exception e) {
            networkoperatorname = "NULL";
        }
        try {
            release = Build.VERSION.RELEASE; // 手机系统版本
        } catch (Exception e) {
            release = "NULL";
        }
        
        
        
        
        
        
        try {
            DisplayMetrics dm = new DisplayMetrics();
            mActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
            widthPixels = dm.widthPixels;
            heightPixels = dm.heightPixels;
            screensize = Integer.toString(widthPixels) + "*"
                    + Integer.toString(heightPixels); // 屏幕分配率
            
            
            density = (int) dm.density;
            densityDpi =  dm.densityDpi;
            scaledDensity= dm.scaledDensity;
            
        } catch (Exception e) {
            screensize = "NULL";
        }
        
       
        stringBuffer.append("手机制造商:" + brand + "\n手机型号:" + model + "\n系统版本:"
                + release + "\nimei:" + imei + "\n网络名称:" + networkoperatorname
                + "\n屏幕像素:" + screensize + "\n 密度   density:"+ density+ "\n 密度   densityDpi:"+ densityDpi+"\n 字体大小:"+scaledDensity);
        LogUtil.e("手机信息", stringBuffer.toString());
        px2sp(mActivity, 44);
        return stringBuffer.toString();
    }

    
    
    /**
	 * @MethodName: px2dip
	 * @Description: 像素px转化为dip
	 * @param context
	 * @param pxValue
	 * @return int
	 */
	public static int px2dip(Context context, float pxValue) {
		final float density = context.getResources().getDisplayMetrics().density;
		 LogUtil.e("phoneinfoutil","come in pxValue =  " + pxValue + " out  dip == "+ (int) (pxValue / density + 0.5f));
		return (int) (pxValue / density + 0.5f);
	}

	/**
	 * @MethodName: dip2px
	 * @Description: dip转化为px
	 * @param context
	 * @param dipValue
	 * @return int
	 */
	public static int dip2px(Context context, float dipValue) {
		final float density = context.getResources().getDisplayMetrics().density;
		
		 LogUtil.e("phoneinfoutil","come in  dip = "+ dipValue + "  out  = "+ (int) (dipValue * density + 0.5f));
		return (int) (dipValue * density + 0.5f);
	}
	
	/** 
     * 将px值转换为sp值，保证文字大小不变 
     *  
     * @param pxValue 
     * @param fontScale 
     *            （DisplayMetrics类中属性scaledDensity） 
     * @return 
     */  
    public static int px2sp(Context context, float pxValue) {  
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;  
        LogUtil.e("phoneinfoutil","px2sp come in  pxValue = "+ pxValue + "  out  = "+ (int) (pxValue / fontScale + 0.5f));
        return (int) (pxValue / fontScale + 0.5f);  
    }  
  
    /** 
     * 将sp值转换为px值，保证文字大小不变 
     *  
     * @param spValue 
     * @param fontScale 
     *            （DisplayMetrics类中属性scaledDensity） 
     * @return 
     */  
    public static int sp2px(Context context, float spValue) {  
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;  
        LogUtil.e("phoneinfoutil","sp2px come in  spValue = "+ fontScale + "  out  = "+ (int) (spValue * fontScale + 0.5f));
        return (int) (spValue * fontScale + 0.5f);  
    }  
	
	

}
