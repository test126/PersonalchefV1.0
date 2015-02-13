package com.goodfriends.personalchef.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.goodfriends.personalchef.ChefDetailActivity;
import com.goodfriends.personalchef.DishDetailActivity;
import com.goodfriends.personalchef.LoadingActivity;
import com.goodfriends.personalchef.MainActivity;
import com.goodfriends.personalchef.R;
import com.goodfriends.personalchef.WebUrlActivity;
import com.goodfriends.personalchef.adapter.HomeAdAdapter;
import com.goodfriends.personalchef.bean.Advs;
import com.goodfriends.personalchef.bean.Chefs;
import com.goodfriends.personalchef.bean.Dish;
import com.goodfriends.personalchef.bean.IndexFun;
import com.goodfriends.personalchef.common.Common;
import com.goodfriends.personalchef.common.CommonFun;
import com.goodfriends.personalchef.common.Msg;
import com.goodfriends.personalchef.util.AsynImageLoader;
import com.goodfriends.personalchef.util.FileCache;
import com.goodfriends.personalchef.util.MyImageLoader;
import com.goodfriends.personalchef.util.ShareImageCache;
import com.goodfriends.personalchef.util.VolleyHttp;

@SuppressLint("HandlerLeak")
public class HomeFragment extends Fragment implements OnClickListener {

	private TextView chushi_more, caipin_more, phone;
	private ImageView chushi1, chushi2, chushi3, caipin1, caipin2, caipin3,
			caipin4;
	private ScrollView scrollView;
	private List<Advs> advs = null;
	private List<View> adImages = new ArrayList();
	private HomeAdAdapter adAdapter = null;
	public static List<Dish> dishs = null;
	private List<Chefs> chefs = null;
	private ViewPager adv_viewPager;
	private ProgressDialog progressDialog;
	private ImageView huodong1, huodong2;
	private MyImageLoader imgLoader = null;
	private VolleyHttp http = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.activity_home, null);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		adImages = new ArrayList<View>();
		http = new VolleyHttp(this.getActivity(), myHandler);
		http.requestAdvs();
		imgLoader = new MyImageLoader(this.getActivity());
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initView();
		if (dishs != null) {
			myHandler.sendEmptyMessage(1);
		} else {
			new Thread(dishRunnable).start();
		}
		if (chefs != null) {
			myHandler.sendEmptyMessage(2);
		} else {
			new Thread(chefRunnable).start();
		}
	};


	Runnable dishRunnable = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			dishs = CommonFun.getRecommendDishs(getActivity(), 1, 4);
			if (dishs != null) {
				myHandler.sendEmptyMessage(1);
			}
		}
	};

	Runnable chefRunnable = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			chefs = CommonFun.getRecommendMasters(getActivity(), 1, 3);
			if (chefs != null) {
				myHandler.sendEmptyMessage(2);
			}
		}
	};

	
	public void notifyAdvsDataSetChanged(List<Advs> advs){
		adImages.clear();
		int size = advs.size();
		for (int i = 0; i < size; i++) {
			System.out.println("notify");
			ImageView adv_image = new ImageView(getActivity());
			adv_image.setScaleType(ScaleType.CENTER_CROP);
			imgLoader.loadBitmap(adv_image, advs.get(i).getImgurl());
			adImages.add(adv_image);
		}
		adAdapter.notifyDataSetChanged();
		adAdapter.setRadioCircles();
	}
	private Handler myHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case Msg.get_advs_content:
				advs = (List<Advs>) msg.obj;
				notifyAdvsDataSetChanged(advs);
				//保存标题广告信息
				new ShareImageCache(HomeFragment.this.getActivity()).putAdvs(advs);
				break;
			case 0:
				break;
			case 1:
				if (dishs.size() != 0) {
					setDishImage();
				}
				break;
			case 2:
				switch (chefs.size()) {
				case 1:
					imgLoader.loadBitmap(chushi1, chefs.get(0).getHeadpicurl());
					break;
				case 2:
					imgLoader.loadBitmap(chushi1, chefs.get(0).getHeadpicurl());
					imgLoader.loadBitmap(chushi2, chefs.get(1).getHeadpicurl());
					break;
				case 3:
					imgLoader.loadBitmap(chushi1, chefs.get(0).getHeadpicurl());
					imgLoader.loadBitmap(chushi2, chefs.get(1).getHeadpicurl());
					imgLoader.loadBitmap(chushi3, chefs.get(2).getHeadpicurl());
					break;
				default:
					break;
				}
				break;
			case 3:
				Intent intent = new Intent(getActivity(), WebUrlActivity.class);
				intent.putExtra("url", indexFuns.get(0).getUrl());
				startActivity(intent);
				break;
			case 4:
				Intent intent1 = new Intent(getActivity(), WebUrlActivity.class);
				intent1.putExtra("url", indexFuns.get(1).getUrl());
				startActivity(intent1);
				break;
			case 100:
				adv_viewPager.setCurrentItem(msg.arg1);
				break;
			case 101:
				Intent adIntent = new Intent(getActivity(),
						WebUrlActivity.class);
				String url = advs.get(msg.arg1).getUrl();
				adIntent.putExtra("url", url);
				startActivity(adIntent);
				break;
			default:
				break;
			}
		}
	};

	private void setDishImage() {
		// TODO Auto-generated method stub
		switch (dishs.size()) {
		case 1:
			if (dishs.get(0).getBigimgurl() != null) {

				imgLoader.loadBitmap(caipin1, dishs.get(0).getBigimgurl());
			} else {
				caipin1.setImageResource(R.drawable.nopic);
			}
			break;
		case 2:
			if (dishs.get(0).getMiddleimgurl() != null) {

				imgLoader.loadBitmap(caipin1, dishs.get(0).getMiddleimgurl());
			} else {
				caipin1.setImageResource(R.drawable.nopic);
			}
			if (dishs.get(1).getSmallimgurl() != null) {

				imgLoader.loadBitmap(caipin2, dishs.get(1).getSmallimgurl());
			} else {
				caipin2.setImageResource(R.drawable.ic_launcher);
			}
			break;
		case 3:
			if (dishs.get(0).getSmallimgurl() != null) {

				imgLoader.loadBitmap(caipin1, dishs.get(0).getSmallimgurl());
			} else {
				caipin1.setImageResource(R.drawable.nopic);
			}
			if (dishs.get(1).getSmallimgurl() != null) {

				imgLoader.loadBitmap(caipin2, dishs.get(1).getSmallimgurl());
			} else {
				caipin2.setImageResource(R.drawable.ic_launcher);
			}
			if (dishs.get(2).getSmallimgurl() != null) {

				imgLoader.loadBitmap(caipin3, dishs.get(2).getSmallimgurl());
			} else {
				caipin3.setImageResource(R.drawable.ic_launcher);
			}
			break;
		case 4:
			if (dishs.get(0).getSmallimgurl() != null) {

				imgLoader.loadBitmap(caipin1, dishs.get(0).getSmallimgurl());
			} else {
				caipin1.setImageResource(R.drawable.nopic);
			}
			if (dishs.get(1).getSmallimgurl() != null) {

				imgLoader.loadBitmap(caipin2, dishs.get(1).getSmallimgurl());
			} else {
				caipin2.setImageResource(R.drawable.ic_launcher);
			}
			if (dishs.get(2).getSmallimgurl() != null) {

				imgLoader.loadBitmap(caipin3, dishs.get(2).getSmallimgurl());
			} else {
				caipin3.setImageResource(R.drawable.ic_launcher);
			}
			if (dishs.get(3).getSmallimgurl() != null) {

				imgLoader.loadBitmap(caipin4, dishs.get(3).getSmallimgurl());
			} else {
				caipin4.setImageResource(R.drawable.ic_launcher);
			}
			break;
		default:
			break;
		}
	};

	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.home_chushi_more:
			CategaryFragment.SWITCH = Common.CHUSHI;
			CategaryFragment.cookstyleid = 0;
			MainActivity.mTab2.performClick();
			//newCategory();
			break;
		case R.id.home_caipin_more:
			CategaryFragment.SWITCH = Common.CAIPIN;
			CategaryFragment.cookstyleid = 0;
			MainActivity.mTab2.performClick();
			//newCategory();
			break;
		case R.id.home_chushi_iv1:
			if (chefs != null) {
				if (chefs.size() > 0) {
					toChefDetail(chefs.get(0).getId());
				}
			}
			break;
		case R.id.home_chushi_iv2:
			if (chefs != null) {
				if (chefs.size() > 1) {
					toChefDetail(chefs.get(1).getId());
				}
			}
			break;
		case R.id.home_chushi_iv3:
			if (chefs != null) {
				if (chefs.size() > 2) {
					toChefDetail(chefs.get(2).getId());
				}
			}
			break;
		case R.id.home_caipin_iv1:
			if (dishs != null) {
				if (dishs.size() > 0) {
					toDishDetail(dishs.get(0).getDishid());
				}
			}
			break;
		case R.id.home_caipin_iv2:
			if (dishs != null) {
				if (dishs.size() > 1) {
					toDishDetail(dishs.get(1).getDishid());
				}
			}
			break;
		case R.id.home_caipin_iv3:
			if (dishs != null) {
				if (dishs.size() > 2) {
					toDishDetail(dishs.get(2).getDishid());
				}
			}
			break;
		case R.id.home_caipin_iv4:
			if (dishs != null) {
				if (dishs.size() > 3) {
					toDishDetail(dishs.get(3).getDishid());
				}
			}
			break;
		case R.id.ll1:
			CategaryFragment.SWITCH = Common.CHUSHI;
			CategaryFragment.cookstyleid = LoadingActivity.caixis.get(0)
					.getId();
			CategaryFragment.cookstylename = LoadingActivity.caixis.get(0)
					.getName();
			MainActivity.mTab2.performClick();
			break;
		case R.id.ll2:
			CategaryFragment.SWITCH = Common.CHUSHI;
			CategaryFragment.cookstyleid = LoadingActivity.caixis.get(1)
					.getId();
			CategaryFragment.cookstylename = LoadingActivity.caixis.get(1)
					.getName();
			MainActivity.mTab2.performClick();
			break;
		case R.id.ll3:
			CategaryFragment.SWITCH = Common.CHUSHI;
			CategaryFragment.cookstyleid = LoadingActivity.caixis.get(2)
					.getId();
			CategaryFragment.cookstylename = LoadingActivity.caixis.get(2)
					.getName();
			MainActivity.mTab2.performClick();
			break;
		case R.id.ll4:
			CategaryFragment.SWITCH = Common.CHUSHI;
			CategaryFragment.cookstyleid = LoadingActivity.caixis.get(3)
					.getId();
			CategaryFragment.cookstylename = LoadingActivity.caixis.get(3)
					.getName();
			MainActivity.mTab2.performClick();
			break;
		case R.id.ll5:
			CategaryFragment.SWITCH = Common.CHUSHI;
			CategaryFragment.cookstyleid = LoadingActivity.caixis.get(4)
					.getId();
			CategaryFragment.cookstylename = LoadingActivity.caixis.get(4)
					.getName();
			MainActivity.mTab2.performClick();
			break;
		case R.id.ll6:
			CategaryFragment.SWITCH = Common.CHUSHI;
			CategaryFragment.cookstyleid = LoadingActivity.caixis.get(5)
					.getId();
			CategaryFragment.cookstylename = LoadingActivity.caixis.get(5)
					.getName();
			MainActivity.mTab2.performClick();
			break;
		case R.id.ll7:
			CategaryFragment.SWITCH = Common.CHUSHI;
			CategaryFragment.cookstyleid = LoadingActivity.caixis.get(6)
					.getId();
			CategaryFragment.cookstylename = LoadingActivity.caixis.get(6)
					.getName();
			MainActivity.mTab2.performClick();
			break;
		case R.id.ll8:
			CategaryFragment.SWITCH = Common.CHUSHI;
			CategaryFragment.cookstyleid = LoadingActivity.caixis.get(7)
					.getId();
			CategaryFragment.cookstylename = LoadingActivity.caixis.get(7)
					.getName();
			MainActivity.mTab2.performClick();
			break;
		case R.id.home_huodong1:
			getIndexFun1();
			break;
		case R.id.home_huodong2:
			getIndexFun2();
			break;
		case R.id.phone:
			AlertDialog.Builder builder_call = new AlertDialog.Builder(
					getActivity());
			builder_call.setMessage("拨打客服电话" + "4009916999" + "？");
			builder_call.setCancelable(false);
			builder_call.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							Intent intent = new Intent(Intent.ACTION_CALL, Uri
									.parse("tel:" + "4009916999"));
							startActivity(intent);
							dialog.dismiss();
						}
					}).setNegativeButton("取消",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							dialog.dismiss();
						}
					});
			builder_call.create();
			builder_call.show();
			break;
		default:
			break;
		}
	}

	private List<IndexFun> indexFuns;

	private void getIndexFun1() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				indexFuns = CommonFun.getIndexFun(1);
				if (indexFuns != null) {
					myHandler.sendEmptyMessage(3);
				}
			}
		}).start();
	}

	private void getIndexFun2() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				indexFuns = CommonFun.getIndexFun(2);
				if (indexFuns != null) {
					myHandler.sendEmptyMessage(4);
				}
			}
		}).start();
	}

//	private void newCategory() {
//		FragmentManager fm = getActivity().getSupportFragmentManager();
//		FragmentTransaction tx = fm.beginTransaction();
//		if (MainActivity.categoryFragment == null) {
//			MainActivity.categoryFragment = new CategaryFragment();
//		} else {
//			tx.remove(MainActivity.categoryFragment);
//			MainActivity.categoryFragment = new CategaryFragment();
//		}
//		tx.replace(R.id.container, MainActivity.categoryFragment);
//		tx.addToBackStack(null);
//		tx.commit();
//		MainActivity.mTab2.performClick();
//	}

	private void toChefDetail(int id) {
		Intent intent = new Intent(getActivity(), ChefDetailActivity.class);
		intent.putExtra("chefId", id);
		startActivity(intent);
	}

	private void toDishDetail(int id) {
		Intent intent = new Intent(getActivity(), DishDetailActivity.class);
		intent.putExtra("dishId", id);
		startActivity(intent);
	}

	private void initView() {
		//showDialog("loading");
		scrollView = (ScrollView) getActivity().findViewById(R.id.scrollView);
		scrollView.setHorizontalScrollBarEnabled(false);
		chushi_more = (TextView) getActivity().findViewById(
				R.id.home_chushi_more);
		chushi_more.setOnClickListener(this);
		caipin_more = (TextView) getActivity().findViewById(
				R.id.home_caipin_more);
		caipin_more.setOnClickListener(this);
		chushi1 = (ImageView) getActivity().findViewById(R.id.home_chushi_iv1);
		chushi1.setOnClickListener(this);
		chushi2 = (ImageView) getActivity().findViewById(R.id.home_chushi_iv2);
		chushi2.setOnClickListener(this);
		chushi3 = (ImageView) getActivity().findViewById(R.id.home_chushi_iv3);
		chushi3.setOnClickListener(this);
		caipin1 = (ImageView) getActivity().findViewById(R.id.home_caipin_iv1);
		caipin1.setOnClickListener(this);
		caipin2 = (ImageView) getActivity().findViewById(R.id.home_caipin_iv2);
		caipin2.setOnClickListener(this);
		caipin3 = (ImageView) getActivity().findViewById(R.id.home_caipin_iv3);
		caipin3.setOnClickListener(this);
		caipin4 = (ImageView) getActivity().findViewById(R.id.home_caipin_iv4);
		caipin4.setOnClickListener(this);
		huodong1 = (ImageView) getActivity().findViewById(R.id.home_huodong1);
		huodong1.setOnClickListener(this);
		huodong2 = (ImageView) getActivity().findViewById(R.id.home_huodong2);
		huodong2.setOnClickListener(this);
		caixi_ll1 = (LinearLayout) getActivity().findViewById(R.id.ll1);
		caixi_ll1.setOnClickListener(this);
		caixi_ll2 = (LinearLayout) getActivity().findViewById(R.id.ll2);
		caixi_ll2.setOnClickListener(this);
		caixi_ll3 = (LinearLayout) getActivity().findViewById(R.id.ll3);
		caixi_ll3.setOnClickListener(this);
		caixi_ll4 = (LinearLayout) getActivity().findViewById(R.id.ll4);
		caixi_ll4.setOnClickListener(this);
		caixi_ll5 = (LinearLayout) getActivity().findViewById(R.id.ll5);
		caixi_ll5.setOnClickListener(this);
		caixi_ll6 = (LinearLayout) getActivity().findViewById(R.id.ll6);
		caixi_ll6.setOnClickListener(this);
		caixi_ll7 = (LinearLayout) getActivity().findViewById(R.id.ll7);
		caixi_ll7.setOnClickListener(this);
		caixi_ll8 = (LinearLayout) getActivity().findViewById(R.id.ll8);
		caixi_ll8.setOnClickListener(this);
		phone = (TextView) getActivity().findViewById(R.id.phone);
		phone.setOnClickListener(this);
		adv_viewPager = (ViewPager) getActivity().findViewById(
				R.id.news_body_veiw);
		getCacheAdvs();
		adAdapter = new HomeAdAdapter(adImages, getActivity(), myHandler);
		adv_viewPager.setAdapter(adAdapter);
		adv_viewPager.setOnPageChangeListener(adAdapter);
		// adv_viewPager.setOnClickListener(this);
	}
	
	/**
	 * 从本地取出上一次保存的标题广告信息
	 */
	public void getCacheAdvs(){
		List<Advs> list = new ShareImageCache(this.getActivity()).getAdvs();
		FileCache fileCache = new FileCache(imgLoader.getSDCacheDir());
		int size = list.size();
		for (int i = 0; i < size; i++) {
			ImageView adv_image = new ImageView(getActivity());
			adv_image.setScaleType(ScaleType.CENTER_CROP);
			String imgUrl = list.get(i).getImgurl();
			Log.i("share", "imgUrl "+imgUrl);
			Bitmap bm = fileCache.getBitmapFromSD(imgUrl);
			if( bm != null){
				Log.i("share", "new le yi ge ");
				adv_image.setImageBitmap(bm);
			}else{
				Log.i("share", "new le yi ge no pic");
				adv_image.setImageResource(R.drawable.nopic);
			}
			adImages.add(adv_image);
			
		}
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		adAdapter.free();
	}

	private void showDialog(String s) {
		progressDialog = new ProgressDialog(getActivity());
		progressDialog.setMessage(s);
		progressDialog.setCancelable(false);
		progressDialog.show();
	}

	private void closeDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
			progressDialog = null;
		}
	}

	private LinearLayout caixi_ll1, caixi_ll2, caixi_ll3, caixi_ll4, caixi_ll5,
			caixi_ll6, caixi_ll7, caixi_ll8;

	@Override
	public void setMenuVisibility(boolean menuVisible) {
		super.setMenuVisibility(menuVisible);
		if (this.getView() != null)
			this.getView()
					.setVisibility(menuVisible ? View.VISIBLE : View.GONE);
	}

}