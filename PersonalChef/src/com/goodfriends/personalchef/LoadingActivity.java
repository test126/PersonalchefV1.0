package com.goodfriends.personalchef;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.goodfriends.personalchef.application.SysApplication;
import com.goodfriends.personalchef.bean.Caixi;
import com.goodfriends.personalchef.bean.Loca;
import com.goodfriends.personalchef.common.Common;
import com.goodfriends.personalchef.common.CommonFun;

public class LoadingActivity extends Activity {

	public static List<Caixi> caixis = null;
	public static List<String> jiguans;
	public static Loca loca;
	private boolean b;
	private AlertDialog.Builder builder_call;

	public LocationClient mLocationClient = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		SysApplication.getInstance().addActivity(this);
		setContentView(R.layout.activity_loading);

		mLocationClient = new LocationClient(getApplicationContext());
		mLocationClient.registerLocationListener(new BDLocationListener() {

			@Override
			public void onReceiveLocation(BDLocation location) {
				// TODO Auto-generated method stub
				if (location == null)
					return;
				String lat = location.getAddrStr();
				String pro = location.getCity();
				String city = location.getDistrict();
				String dis = location.getStreet() + location.getStreetNumber();
				loca = new Loca(pro, city, dis, lat);
				new Thread(jiguanRunnable).start();
			}
		});

		SharedPreferences preferences = getSharedPreferences("ISFIRST",
				MODE_PRIVATE);
		b = preferences.getBoolean("isfirst", true);
		try {
			if (Common.checkNetWork(getApplicationContext())) {
				InitLocation();
				mLocationClient.start();
			} else {
				myHandler.sendEmptyMessage(440);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void onDestroy() {
		super.onDestroy();
		mLocationClient.stop();
	};

	Runnable caixiRunnable = new Runnable() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			caixis = CommonFun.getCaixi(LoadingActivity.this);
			if (caixis != null) {
				myHandler.sendEmptyMessage(0);
			} else {
				myHandler.sendEmptyMessage(444);
			}
		}
	};

	Runnable jiguanRunnable = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			jiguans = CommonFun.getjiguan(LoadingActivity.this);
			if (jiguans != null) {
				myHandler.sendEmptyMessage(1);
			} else {
				myHandler.sendEmptyMessage(444);
			}
		}
	};

	@SuppressLint("HandlerLeak")
	private Handler myHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				new Handler().postDelayed(new Runnable() {
					public void run() {
						// TODO Auto-generated method stub
						if (b) {
							SharedPreferences preferences = getSharedPreferences(
									"ISFIRST", MODE_PRIVATE);
							Editor editor = preferences.edit();
							editor.putBoolean("isfirst", false);
							editor.commit();
							Intent intent = new Intent(LoadingActivity.this,
									GuideViewActivity.class);
							startActivity(intent);
							LoadingActivity.this.finish();
						} else {
							startActivity(new Intent(LoadingActivity.this,
									MainActivity.class));
							LoadingActivity.this.finish();
						}
					}
				}, 100);
				break;

			case 1:
				new Thread(caixiRunnable).start();
				break;
			case 444:
				builder_call = new AlertDialog.Builder(LoadingActivity.this);
				builder_call.setMessage("服务器连接异常");
				builder_call.setCancelable(false);
				builder_call.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								LoadingActivity.this.finish();
								dialog.dismiss();
							}
						});
				builder_call.create();
				builder_call.show();
				break;

			case 440:
				builder_call = new AlertDialog.Builder(LoadingActivity.this);
				builder_call.setMessage("网络连接异常");
				builder_call.setCancelable(false);
				builder_call.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								LoadingActivity.this.finish();
								dialog.dismiss();
							}
						});
				builder_call.create();
				builder_call.show();
				break;
			default:
				break;
			}
		};
	};

	private void InitLocation() {
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// 设置定位模式
		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
		option.setScanSpan(5000);// 设置发起定位请求的间隔时间为5000ms
		option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
		option.setNeedDeviceDirect(true);// 返回的定位结果包含手机机头的方向
		mLocationClient.setLocOption(option);
	}
}
