package com.heaven.circleanimview.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.heaven.circleanimview.R;

public class GlideUtil {


    public static void displayImage(Context context,String url,ImageView iv){
        Glide.with(context)
                .load(url)// 加载图片资源
//                .skipMemoryCache(false)//是否将图片放到内存中
                .diskCacheStrategy(DiskCacheStrategy.ALL)//磁盘图片缓存策略
                .dontAnimate()//不执行淡入淡出动画
//                .crossFade(100)// 默认淡入淡出动画300ms
//                .override(300,300)//图片大小
                .placeholder(R.drawable.shouye_haibao)// 占位图片
//                .error(R.drawable.shouye_haibao)//图片加载错误显示
//                .centerCrop()//  fitCenter()
//                .fitCenter()
//                 .listener(null)
//                .animate(0)// 执行的动画
//                  .transform()
//                .bitmapTransform(null)// bitmap操作
//                .priority(Priority.HIGH)// 当前线程的优先级
//                .signature(new StringSignature("ssss"))
//                .thumbnail(0.1f)
               /* .animate(new ViewPropertyAnimation.Animator() {
                            @Override
                            public void animate(View view) {

                            }
                        })*/
//                .into(250,250)
                .into(iv);
    }


   
}
