/**
 * 
 */
package com.heaven.circleanimview.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.heaven.circleanimview.R;
import com.heaven.circleanimview.bean.FlyBean;
import com.heaven.circleanimview.bean.Movie;
import com.heaven.circleanimview.utils.GlideUtil;
import com.heaven.circleanimview.utils.LogUtil;

/**
 * 影片列表适配器
 */
public class MoviesAdapter extends BaseAdapter {
	
	private Context context;
	private Handler handler;
	private ArrayList<Movie> movies;
	
	public MoviesAdapter(Context context, Handler handler,ArrayList<Movie> movies) {
		this.context = context;
		this.handler = handler;
		this.movies = movies;
	}

	@Override
	public int getCount() {
		return movies.size();
	}

	@Override
	public Object getItem(int position) {
		return movies.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		MovieViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.movies_list_hot_item,null);
			holder = new MovieViewHolder(convertView);
			convertView.setTag(holder);
		}else{
			holder = (MovieViewHolder) convertView.getTag();
		}

       
        holder.img_movie_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.e("setOnClickListener", "  Width  = " + v.getWidth() + " height = " + v.getHeight());

                Rect rect = new Rect();
                v.getWindowVisibleDisplayFrame(rect);//view在整个屏幕的坐标，也含有通知栏高度
                LogUtil.e("setOnClickListener", "  getWindowVisibleDisplayFrame rect = " + rect);

                int statusBarHeight = rect.top;

                v.getGlobalVisibleRect(rect);// view在整个屏幕的坐标，包括状态栏高度。注意如果view未显示全，获取坐标的bottom是错误的，最大是屏幕的高度，其实view的bottom要高于屏幕的高度。
                LogUtil.e("setOnClickListener", "  getGlobalVisibleRect rect = " + rect);

                Movie movie = (Movie) v.getTag(R.id.img_tag);
                Message msg = handler.obtainMessage();
                msg.obj = new FlyBean(v.getWidth(),v.getHeight(),rect,statusBarHeight,movie);
                msg.what = 1;
                handler.sendMessage(msg);
            }
        });


        LogUtil.e(this, " position = " + position);
        Movie movie=movies.get(position);
        GlideUtil.displayImage(context,movie.movie_img_url,holder.img_movie_photo);

        holder.img_movie_photo.setTag(R.id.img_tag,movie);
        holder.text_movie_name.setText(movie.movie_name);

        if (TextUtils.isEmpty(movie.movie_presale)) {// 即将上映
            setWantSee(holder, movie);
            holder.text_movie_right_info2.setText("人想看");
            holder.ll_hot.setVisibility(View.GONE);
            holder.ll_presal.setVisibility(View.VISIBLE);
        } else {
            holder.ll_hot.setVisibility(View.VISIBLE);
            holder.ll_presal.setVisibility(View.GONE);

            if (movie.movie_presale.equals("1")) {
            // 预售
                holder.text_movie_right_info2.setText("人想看");
                holder.tv_see_num.setText(movie.movie_want_see_num);
                setWantSee(holder, movie);

                holder.ll_hot.setVisibility(View.GONE);
                holder.ll_presal.setVisibility(View.VISIBLE);

            } else {
                holder.ll_hot.setVisibility(View.VISIBLE);
                holder.ll_presal.setVisibility(View.GONE);
                holder.text_movie_right_info2.setText(Float.valueOf(movie.movie_score)+"评分");
            }
        }
		return convertView;
	}
	
	
	 /**
     * 多少人想看   想看人数超过4位数显示9999+
     * @param holder
     * @param movie
     */
    private void setWantSee(MovieViewHolder holder, Movie movie) {
        holder.tv_see_num.setText(movie.movie_want_see_num);
        try {
            int wantSee = Integer.valueOf(movie.movie_want_see_num);
            if(wantSee>9999){
                holder.tv_see_num.setText(9999+"+");
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            holder.tv_see_num.setText("0");
        }
    }
	
	
  public static class MovieViewHolder {
        public LinearLayout ll_hot;
        public LinearLayout ll_presal;
        public TextView tv_see_num;
        public ImageView movie_format;
        public RelativeLayout lyt_movie_photo;
        public TextView  text_movie_name;
        public TextView  text_movie_right_info2;
        public ImageView  img_movie_photo;
        public LinearLayout  lyt_movie_title;

        public MovieViewHolder(View view) {
            movie_format=(ImageView)view.findViewById(R.id.movie_format);
            lyt_movie_photo=(RelativeLayout)view.findViewById(R.id.lyt_movie_photo);
            text_movie_name=(TextView)view.findViewById(R.id.text_movie_name);
            text_movie_right_info2=(TextView)view.findViewById(R.id.text_movie_right_info2);
            tv_see_num=(TextView)view.findViewById(R.id.tv_see_num);
            img_movie_photo=(ImageView)view.findViewById(R.id.img_movie_photo);
            lyt_movie_title=(LinearLayout)view.findViewById(R.id.lyt_movie_title);
            ll_hot=(LinearLayout)view.findViewById(R.id.ll_hot);
            ll_presal=(LinearLayout)view.findViewById(R.id.ll_presal);
        }
    }
}
