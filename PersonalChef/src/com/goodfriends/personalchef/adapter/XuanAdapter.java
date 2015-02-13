package com.goodfriends.personalchef.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.goodfriends.personalchef.R;
import com.goodfriends.personalchef.bean.Dish;
import com.goodfriends.personalchef.util.ImageCallback;
import com.goodfriends.personalchef.util.LoadImage;
import com.goodfriends.personalchef.util.MyImageLoader;

public class XuanAdapter extends BaseAdapter {

	private List<Dish> lists;
	private Context context;
	private LayoutInflater layoutInflater;
	private MyImageLoader imgLoader;
	public XuanAdapter(Context context, List<Dish> lists) {
		// TODO Auto-generated constructor stub
		this.context = context;
		imgLoader = new MyImageLoader(context);
		this.lists = lists;
		layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return lists.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return lists.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		final viewHolder holder;
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.xancai_grid_item,
					null);
			holder = new viewHolder();
			holder.text_name = (TextView) convertView
					.findViewById(R.id.xuancai_name);
			holder.text_times = (TextView) convertView
					.findViewById(R.id.xuancai_times);
			holder.text_intro = (TextView) convertView
					.findViewById(R.id.xuancai_intro);
			holder.iv_head = (ImageView) convertView
					.findViewById(R.id.xuancai_iv);
			viewHolder.choice = (TextView) convertView
					.findViewById(R.id.xuancai_choice);
			convertView.setTag(holder);
		} else {
			holder = (viewHolder) convertView.getTag();
		}

		holder.text_name.setText(lists.get(arg0).getName());
		holder.text_times.setText(lists.get(arg0).getordercount() + "");
		holder.text_intro.setText(lists.get(arg0).getDesc());
		imgLoader.loadBitmap(holder.iv_head, lists.get(arg0).getBigimgurl());
//		
//		LoadImage loadImage = new LoadImage(context.getResources());
//		loadImage.loadImage(lists.get(arg0).getBigimgurl(),
//				new ImageCallback() {
//					public void imageLoaded(Bitmap bitmap) {
//						// TODO Auto-generated method stub
//						holder.iv_head.setImageBitmap(bitmap);
//					}
//				});
		return convertView;
	}

	public static class viewHolder {
		private TextView text_name;
		private TextView text_times;
		private TextView text_intro;
		private ImageView iv_head;
		public static TextView choice;
	}

}
