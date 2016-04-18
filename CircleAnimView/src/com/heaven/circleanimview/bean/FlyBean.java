package com.heaven.circleanimview.bean;

import android.graphics.Rect;

/**
 * 转场动画bean
 */
public class FlyBean {

	/**
	 * 图片的位置
	 */
    public Rect rect = null;
    /**
     * 图片的宽度
     */
    public int width = -1;
    /**
     * 图片的高度
     */
    public int height = -1;
    /**
     * 状态栏高度
     */
    public int statusBarHeigh = -1;
    public Movie movie ;
    
    /**
	 * 图片的原始位置
	 */
    public Rect originalRect = null;

    public FlyBean(int width, int height, Rect rect, int statusBarHeigh, Movie movie) {
        this.width = width;
        this.height = height;
        this.rect = rect;
        originalRect = new Rect(rect.left, rect.top, rect.right, rect.bottom);
        this.statusBarHeigh = statusBarHeigh;
        this.movie = movie;
    }

    @Override
	public String toString() {
		return "FlyBean [rect=" + rect + ", width=" + width + ", height="
				+ height + ", statusBarHeigh=" + statusBarHeigh + ", movie="
				+ movie + ", originalRect=" + originalRect + "]";
	}
}
