package com.heaven.circleanimview.bean;

import java.io.Serializable;

/**
 * 影片对象
 */
public class Movie implements Serializable {
	private static final long serialVersionUID = -1356498005337543429L;
	public String movie_name;//	影片名称
	public String movie_id;//	影片id
	public String movie_desc;//	影片简短描述
	public String cinema_num;//	当天多少影院上映
	public String show_num;//	当天上映多少场
	public String movie_director;//	导演
	public String movie_cast;//	演员
	public String movie_show_date;//	上映日期
	public String movie_want_see_num;//	想看人数
	/**是否是预售*/
	public String movie_presale;//	是否是预售	1：是	0：不是
	public String movie_score;//	影片评分
	/**
	 * 		1	2D；
			2	3D；
			3	IMAX 3D；
			4	中国巨幕；
			5	中国巨幕 3D；
	 */
	public String movie_format;//	影片制式  
	public String movie_img_url	;//影片海报图
	/**
	 * 1	抢券；
		2	抢票；
		3	立减；
		4	抽奖；
	 */
	public String promotion_type;//	活动类型
	public String is_new;//是否新片	1-是 0-不是

	

}
