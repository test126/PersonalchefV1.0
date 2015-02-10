package com.goodfriends.personalchef;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.goodfriends.personalchef.application.SysApplication;
import com.goodfriends.personalchef.fragment.CategaryFragment;
import com.goodfriends.personalchef.fragment.HomeFragment;
import com.goodfriends.personalchef.fragment.MeFragment;
import com.goodfriends.personalchef.fragment.OrderFragment;

public class MainActivity extends FragmentActivity implements OnClickListener {

	public static RadioButton mTab1;
	public static RadioButton mTab2;
	public static RadioButton mTab3;
	public static RadioButton mTab4;

	public static HomeFragment homeFragment;
	public static CategaryFragment categoryFragment;
	public static OrderFragment orderFragment;
	public static MeFragment meFragment;
	
	public RadioGroup radioGroup;
	
	private FragmentManager manager;
	private FragmentTransaction trans;

	public Fragment currentFragment;
	public Fragment fromFragment;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		SysApplication.getInstance().addActivity(this);
		setContentView(R.layout.activity_main1);

		manager = getSupportFragmentManager();
		trans = manager.beginTransaction();
		
		mTab1 = (RadioButton) findViewById(R.id.radio_button1);
		mTab2 = (RadioButton) findViewById(R.id.radio_button2);
		mTab3 = (RadioButton) findViewById(R.id.radio_button3);
		mTab4 = (RadioButton) findViewById(R.id.radio_button4);
		radioGroup = (RadioGroup) findViewById(R.id.main_radio);

		mTab1.setOnClickListener(this);
		mTab2.setOnClickListener(this);
		mTab3.setOnClickListener(this);
		mTab4.setOnClickListener(this);

		mTab1.performClick();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	private long mExitTime;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			if ((System.currentTimeMillis() - mExitTime) > 2000) {// 如果两次按键时间间隔大于2000毫秒，则不退出
				Toast.makeText(getApplicationContext(),
						getString(R.string.once_again_exit), Toast.LENGTH_SHORT)
						.show();
				mExitTime = System.currentTimeMillis();// 更新mExitTime
			} else {
				finish();
				System.exit(0);// 否则退出程序
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void switchFragment(Fragment from,Fragment to){
		if(currentFragment==null){
			
		}
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction tx = fm.beginTransaction();
		switch (arg0.getId()) {
		case R.id.radio_button1:
			if (homeFragment == null) {
				homeFragment = new HomeFragment();
				trans.add(R.id.container, homeFragment)
				.commit(); 
				currentFragment = homeFragment;
			}
			if (!homeFragment.isAdded()) { // 先判断是否被add过
				tx.hide(currentFragment).add(R.id.container, homeFragment)
						.commit(); // 隐藏当前的fragment，add下一个到Activity中
			} else {
				tx.hide(currentFragment).show(homeFragment).commit(); // 隐藏当前的fragment，显示下一个
			}
			// tx.replace(R.id.container, homeFragment);
			// tx.addToBackStack(null);
			// tx.commit();
			break;
		case R.id.radio_button2:
			if (categoryFragment == null) {
				categoryFragment = new CategaryFragment();
				tx.add(R.id.container, categoryFragment);
			}

			tx.hide(homeFragment).hide(orderFragment).hide(meFragment)
					.show(categoryFragment).commit();
			// tx.replace(R.id.container, categoryFragment);
			// tx.addToBackStack(null);
			// tx.commit();
			break;
		case R.id.radio_button3:
			if (orderFragment == null) {
				orderFragment = new OrderFragment();
				tx.add(R.id.container, orderFragment);
			}
			tx.hide(homeFragment).hide(categoryFragment).hide(meFragment)
					.show(orderFragment).commit();

			// tx.replace(R.id.container, orderFragment);
			// tx.addToBackStack(null);
			// tx.commit();
			break;
		case R.id.radio_button4:
			if (meFragment == null) {
				meFragment = new MeFragment();
				tx.add(R.id.container, meFragment);
			}
			tx.hide(homeFragment).hide(categoryFragment).hide(orderFragment)
					.show(meFragment).commit();
			// tx.replace(R.id.container, meFragment);
			// tx.addToBackStack(null);
			// tx.commit();
			break;
		default:
			break;
		}
	}
}
