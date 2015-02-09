package com.goodfriends.personalchef.util;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.goodfriends.personalchef.R;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.ImageView;

public class VolleyImage {

	private Context context;
	private BitmapCache imageCache;
	private RequestQueue mRequestQueue;
	private ImageLoader mImageLoader;
	private int width, height;
	int defaultPic = R.drawable.nopic;
	public VolleyImage(Context context) {
		super();
		this.context = context;
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		manager.getDefaultDisplay().getMetrics(dm);
		width = dm.widthPixels;
		height = dm.heightPixels;
		mRequestQueue = Volley.newRequestQueue(context); 
		imageCache = new BitmapCache();
		mImageLoader = new ImageLoader(mRequestQueue, imageCache);
	}
	
	public void loadImg(ImageView imgView,String url){
		ImageListener listener = ImageLoader
				.getImageListener(imgView, defaultPic,defaultPic);
		mImageLoader.get(url, listener,width,height);
	}

}
