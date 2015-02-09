package com.goodfriends.personalchef.adapter;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.goodfriends.personalchef.R;
import com.goodfriends.personalchef.view.AdvsViewPager;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class HomeAdAdapter extends PagerAdapter implements OnPageChangeListener, OnClickListener {

	private List<View> adViewList = null;// 广告图片控件列表
	private RadioGroup radioGroup = null;
	private Activity pActivity = null;
	private AdvsViewPager advsViewPager = null;
	private Timer timer = null;
	private int CHECK = 0;
	private int PageScrollState=0;//1正在滑动,2滑动完毕,0什么都没做。
	private Handler handler = null;
	public HomeAdAdapter(List<View> adViewList, Activity pActivity,Handler handler) {
		super();
		this.adViewList = adViewList;
		this.pActivity = pActivity;
		advsViewPager = (AdvsViewPager)pActivity.findViewById(R.id.news_body_veiw);
		setRadioCircles();
		this.handler = handler;
		timer();

	}

	 public void timer() {
		timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				loopAdvUI();
			}

		},10,3000);
		
	}
	 
	 private void loopAdvUI() {
			int size = adViewList.size();
			if(size==0){
				return;
			}
			if(PageScrollState == 1){
				return;
			}
			CHECK++;
			if(CHECK > size){
				CHECK = 0;
			}
			Message msg = new Message();
			msg.what = 100;
			msg.arg1 = CHECK;
			handler.sendMessage(msg);
			
			
		}

	@Override
	public void destroyItem(View container, int position, Object object) {
		// TODO Auto-generated method stub
		((android.support.v4.view.ViewPager) container).removeView(adViewList
				.get(position));
	}

	@Override
	public Object instantiateItem(View container, int position) {
		// TODO Auto-generated method stub
		View view = adViewList.get(position);
		view.setTag(position);
		view.setOnClickListener(this);
		((android.support.v4.view.ViewPager) container).addView(view);

		return adViewList.get(position);
	}

	@Override
	public void onPageScrollStateChanged(int state) {
		
	}

	@Override
	public void onPageScrolled(int index, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int index) {
		// TODO Auto-generated method stub
		System.out.println("select" + index);
		radioGroup.check(radioGroup.getChildAt(index).getId());
		CHECK = index;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return adViewList.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0 == arg1;
	}

	// 设置小圆点
	public void setRadioCircles() {
		radioGroup = (RadioGroup) pActivity
				.findViewById(R.id.advs_gallery_mark);
		for (int i = 0; i < adViewList.size(); i++) {
			RadioButton rb = new RadioButton(pActivity);
			rb.setId(i);
			rb.setClickable(false);
			rb.setPadding(45, 0, 0, 0);
			rb.setButtonDrawable(R.drawable.adv_gallery_mark_selector);
			rb.setBackgroundResource(android.R.color.transparent);
			radioGroup.addView(rb);

		}
		if (adViewList.size() > 0) {
			radioGroup.check(0);
		}

	}
	
	public void free(){
		if(timer!=null){
			timer.cancel();
		}
	}

	@Override
	public void onClick(View view) {
		Message msg = new Message();
		msg.what = 101;
		msg.arg1 = (Integer) view.getTag();
		handler.sendMessage(msg);
	}

}
