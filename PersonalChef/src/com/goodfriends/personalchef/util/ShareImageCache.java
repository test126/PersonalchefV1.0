package com.goodfriends.personalchef.util;

import java.util.ArrayList;
import java.util.List;

import com.goodfriends.personalchef.bean.Advs;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class ShareImageCache {

	private Context context = null;
	private SharedPreferences sharedPreferences;
	public ShareImageCache(Context context) {
		this.context = context;
	}
	
	public void putAdvs(List<Advs> list){
		sharedPreferences = context.getSharedPreferences("advs", Context.MODE_PRIVATE); //私有数据
		Editor editor = sharedPreferences.edit();//获取编辑器
		editor.clear();
		StringBuilder strUrl = new StringBuilder();
		StringBuilder strImgUrl = new StringBuilder();
		Advs advs = null;
		for(int i=0;i<list.size();i++){
			advs = list.get(i);
			strUrl.append(advs.getUrl().trim() +" ");
			strImgUrl.append(advs.getImgurl().trim() +" ");
		}
		
		editor.putString("url", strUrl.toString().trim());
		Log.i("putUrl",  strUrl.toString().trim());
		editor.putString("imgurl", strImgUrl.toString().trim());
		Log.i("putImgUrl",  strImgUrl.toString().trim());
		editor.commit();//提交修改
		
	}
	public List<Advs> getAdvs(){
		List<Advs> list = new ArrayList<Advs>();
		sharedPreferences = context.getSharedPreferences("advs", Context.MODE_PRIVATE); //私有数据
		String strUrl = sharedPreferences.getString("url", "");
		String strImgurl = sharedPreferences.getString("imgurl", "");
		String[] url = strUrl.split(" ");
		String[] imgurl = strImgurl.split(" ");
		Log.i("getUrl", strUrl);
		Log.i("getImgUrl", strImgurl);
		for(int i=0;i<url.length;i++){
			Advs advs = new Advs();
			advs.setUrl(url[i]);
			advs.setImgurl(imgurl[i]);
			list.add(advs);
			Log.i("test","从share中读取一个广告信息");
		}
		
		return list;
		
	}
	

}
