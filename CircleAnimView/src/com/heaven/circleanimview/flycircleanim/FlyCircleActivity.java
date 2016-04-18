package com.heaven.circleanimview.flycircleanim;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.heaven.circleanimview.R;
import com.heaven.circleanimview.adapter.MoviesAdapter;
import com.heaven.circleanimview.bean.FlyBean;
import com.heaven.circleanimview.bean.Movie;
import com.heaven.circleanimview.flowview.MovieDetailsFragment;
import com.heaven.circleanimview.utils.LogUtil;
import com.heaven.circleanimview.views.flowview.FlyCircleAnimView;
/**
 *在执行平移动画期间  add fragment
 */
public class FlyCircleActivity extends FragmentActivity{

	private GridView gv_movies;
	private ArrayList<Movie> movieList;
	
	private FlyCircleAnimView fv_fly;
	private RelativeLayout rl_movie_details_content;
	private FlyCircleDetailsFragment detailsFragment;
	 
	private  Handler handler = new Handler(){
			@Override
	        public void handleMessage(Message msg) {
				LogUtil.e("handleMessage"," msg what = "+ msg.what);
		        switch (msg.what){
		            case 1:// 执行平移动画
	                    FlyBean flyBean = (FlyBean)msg.obj;
	                    LogUtil.e("handleMessage",flyBean.toString());
	                    fv_fly.setVisibility(flyBean,handler);
	                    break;
		            case 2:// 添加fragment
	                    detailsFragment = new FlyCircleDetailsFragment();
	                    detailsFragment.setHandler(this);
	                    getSupportFragmentManager().beginTransaction().add(R.id.rl_movie_details_content, detailsFragment)
//	                            .addToBackStack(null)
	                            .commit();
		            	break;
		            case 100: // 当图片位移动画执行完毕，显示详情fragment，同时执行放大遮罩动画
//	                    fv_fly.setVisibility(View.VISIBLE);// 正式环境注释掉
	                    if (detailsFragment !=null){
	                    	fv_fly.startAnim(1);
	//                        detailsFragment.cav_anim.startCircleAnim((FlyBean) msg.obj,true,this);
	                    }
	                    break;
		            case 101:// remove fragment and 隐藏 flowview
	                    getSupportFragmentManager()
	                            .beginTransaction()
	                            .remove(detailsFragment)
	                            .commit();
	                    break;
		            case 110:// 放大圆绘制完毕   调整图片位置，进行缩放动画
		            	fv_fly.setVisibility(View.GONE);
		            	break;
		            case 111:// 收缩绘制完毕，1，remove fragment。2，图片移动到原来位置
		            	 getSupportFragmentManager()
	                     .beginTransaction()
	                     .remove(detailsFragment)
	                     .commit();
		            	 fv_fly.revertAnim((FlyBean)msg.obj, this);
//		            	fv_fly.setVisibility(View.GONE);
		            	break;
		            case 201: // 详情界面返回按钮
		            	 if (detailsFragment !=null){
		                    	fv_fly.startAnim(0);
		                  }
		            	break;
		            case 202:// 详情界面测试用
		            	 fv_fly.setVisibility(View.VISIBLE);
	                     if (detailsFragment !=null){
	                    	fv_fly.startAnim(1);
	                     }
		            	break;
		            }
	        }
	    };
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.flycircle_main);
		gv_movies = (GridView) findViewById(R.id.gv_movies);
		fv_fly = (FlyCircleAnimView)findViewById(R.id.fv_fly);
	    rl_movie_details_content = (RelativeLayout)findViewById(R.id.rl_movie_details_content);
	    // 解析数据
		try {
			InputStream is = getAssets().open("movies_json");
			byte[] buffer = new byte[1024*1024];
			StringBuilder sb = new StringBuilder();
			while (is.read(buffer)!=-1) {
				sb.append(new String(buffer));
			}
			parseData(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	public void parseData(String response){
		LogUtil.e(this, response);
        try {
            movieList = new ArrayList<Movie>();//+"/moviesjson/"
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonMovieList = jsonObject.getJSONArray("movie_data");

            for (int i = 0; i < jsonMovieList.length(); i++) {
                JSONObject object = jsonMovieList.getJSONObject(i);

                Movie movie = new Movie();

                movie.movie_name = object.getString("movie_name");//	影片名称
                movie.movie_id = object.getString("movie_id");//影片id
                movie.movie_director = object.getString("movie_director");//导演
                movie.movie_cast = object.getString("movie_cast");//演员
                movie.movie_show_date = object.getString("movie_show_date");//上映日期
                movie.movie_want_see_num = object.getString("movie_want_see_num");//想看人数
                movie.movie_format = object.getString("movie_format");//影片制式
                if(TextUtils.isEmpty(movie.movie_format)){
                    movie.movie_format = "1";
                }
                movie.movie_img_url = object.getString("movie_img_url");//影片海报图
                if(object.has("movie_presale")){
                    movie.movie_desc = object.getString("movie_desc");//影片简短描述
                    movie.cinema_num = object.getString("cinema_num");//当天多少影院上映
                    movie.show_num = object.getString("show_num");//当天上映多少场
                    movie.movie_presale = object.getString("movie_presale");//是否是预售	1：是	0：不是
                    movie.movie_score = object.getString("movie_score");//影片评分
                    movie.promotion_type = object.getString("promotion_type");//活动类型
                    if(TextUtils.isEmpty(movie.promotion_type)){
                        movie.promotion_type = "0";
                    }
                    movie.is_new = object.getString("is_new");//是否新片	 1-是 0-不是
                }
                movieList.add(movie);
            }
            MoviesAdapter adapter = new MoviesAdapter(FlyCircleActivity.this,handler,movieList);
            gv_movies.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "解析数据错误", 0).show();
        }
	}

	
	
  public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            if (fv_fly.getVisibility() == View.VISIBLE){
                fv_fly.setVisibility(View.GONE);
                return true;
            }
            //getFragments 出错用最新的sv4包就可以
            List<Fragment>  fragments = getSupportFragmentManager().getFragments();
            int size = fragments.size();
            LogUtil.e("handleMessage","fragments  size = "+ size);
            for (int i = 0; i < size; i++) {
            	Fragment f = fragments.get(i);
            	if(f instanceof FlyCircleDetailsFragment){
            		fv_fly.startAnim(0);// 收缩圆，remove frgment 隐藏fv_fly
            		return true;
            	}
			}
            return super.onKeyDown(keyCode,event);
        }
        return super.onKeyDown(keyCode,event);
    }




}
