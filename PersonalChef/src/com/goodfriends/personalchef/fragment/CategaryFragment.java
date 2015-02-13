package com.goodfriends.personalchef.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.goodfriends.personalchef.ChefDetailActivity;
import com.goodfriends.personalchef.DishDetailActivity;
import com.goodfriends.personalchef.LoadingActivity;
import com.goodfriends.personalchef.R;
import com.goodfriends.personalchef.adapter.ChefAdapter;
import com.goodfriends.personalchef.adapter.DishAdapter;
import com.goodfriends.personalchef.bean.Caixi;
import com.goodfriends.personalchef.bean.ChefBean;
import com.goodfriends.personalchef.bean.Chefs;
import com.goodfriends.personalchef.bean.Dish;
import com.goodfriends.personalchef.bean.DishBean;
import com.goodfriends.personalchef.common.Common;
import com.goodfriends.personalchef.common.CommonFun1;
import com.goodfriends.personalchef.view.ExpandTabView;
import com.goodfriends.personalchef.view.ViewLeft;
import com.goodfriends.personalchef.view.ViewMiddle1;
import com.goodfriends.personalchef.view.ViewRight;
import com.goodfriends.personalchef.view.PullToRefreshListView.PullToRefreshBase;
import com.goodfriends.personalchef.view.PullToRefreshListView.PullToRefreshBase.OnRefreshListener;
import com.goodfriends.personalchef.view.PullToRefreshListView.PullToRefreshListView;

public class CategaryFragment extends Fragment implements OnClickListener {

	private PullToRefreshListView list_chushi;
	private ListView mListView, mListView2;
	private PullToRefreshListView list_caipin;
	private TextView btn_caipin, btn_chushi;
	private ExpandTabView expandTabView1;
	private ExpandTabView expandTabView2;
	private ArrayList<View> mViewArray = null;
	private ArrayList<View> mViewArray1 = null;
	private ViewLeft viewLeft, viewLeft1;
	private ViewMiddle1 viewMiddle;
	private ViewRight viewRight, viewRight1;
	/**
	 * 厨师信息
	 */
	private ChefBean chefBean;
	private List<Chefs> chefs;
	/**
	 * 菜品信息
	 */
	private DishBean dishBean;
	private List<Dish> dishs;
	private ChefAdapter adapter;
	private DishAdapter adapter2;

	private int distanceType = 0, dishcount = 4, page = 1, pagecount = 10,
			countRecord;
	// 菜系id，按距离排序类型，菜品数量，当前页，页数
	private String position;
	// 籍贯,当前位置
	private boolean hasMoreData, hasMoreData2;

	public static int SWITCH = Common.CHUSHI;
	public static int cookstyleid = 0;
	public static String nativeplace = "0", cookstylename = "菜系";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.activity_category, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		initView();
		initValues();
	}
	

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		if(hidden == false){
			initValues();
			Log.i("cateFragment", "显示");
		}
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

		list_chushi.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				new Thread(chefRunnable).start();
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				loadMore();
			}
		});

		list_caipin.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				new Thread(dishRunnable).start();
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				loadMore2();
			}
		});

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),
						ChefDetailActivity.class);
				intent.putExtra("chefId", chefs.get(arg2).getId());
				startActivity(intent);
			}
		});

		mListView2.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),
						DishDetailActivity.class);
				intent.putExtra("dishId", dishs.get(arg2).getDishid());
				startActivity(intent);
			}
		});
	}

	private void initCaipinListener() {
		// TODO Auto-generated method stub
		ViewLeft.all.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ViewLeft.position = -1;
				onRefreshLeft2(viewLeft1, "菜系", LoadingActivity.caixis);
			}
		});
		ViewRight.all.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				onRefresh2(viewRight1, "食材");
			}
		});
		viewLeft1.setOnSelectListener(new ViewLeft.OnSelectListener() {
			public void getValue(String distance, String showText) {
				onRefreshLeft2(viewLeft1, showText, LoadingActivity.caixis);
			}
		});

		viewRight1.setOnSelectListener(new ViewRight.OnSelectListener() {
			public void getValue(String distance, String showText) {
				onRefresh2(viewRight1, showText);
			}
		});
	}

	private void initChushiListener() {
		// TODO Auto-generated method stub
		ViewLeft.all.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ViewLeft.position = -1;
				cookstylename = "菜系";
				onRefreshLeft(viewLeft, cookstylename, LoadingActivity.caixis);
			}
		});
		ViewMiddle1.all.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ViewMiddle1.position = -1;
				onRefreshMiddle(viewMiddle, "籍贯", LoadingActivity.jiguans);
			}
		});
		ViewRight.all.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				onRefresh(viewRight, "距离");
			}
		});
		viewMiddle.setOnSelectListener(new ViewMiddle1.OnSelectListener() {
			@Override
			public void getValue(String distance, String showText) {
				// TODO Auto-generated method stub
				onRefreshMiddle(viewMiddle, showText, LoadingActivity.jiguans);
			}
		});
		viewLeft.setOnSelectListener(new ViewLeft.OnSelectListener() {
			public void getValue(String distance, String showText) {
				onRefreshLeft(viewLeft, showText, LoadingActivity.caixis);
			}
		});

		viewRight.setOnSelectListener(new ViewRight.OnSelectListener() {
			public void getValue(String distance, String showText) {
				onRefresh(viewRight, showText);
			}
		});
	}

	private void initChushiValue() {
		// TODO Auto-generated method stub

		mViewArray = new ArrayList<View>();
		viewLeft = new ViewLeft(getActivity(), LoadingActivity.caixis);
		viewMiddle = new ViewMiddle1(getActivity(), LoadingActivity.jiguans);
		viewRight = new ViewRight(getActivity());

		mViewArray.add(viewLeft);
		mViewArray.add(viewMiddle);
		mViewArray.add(viewRight);

		ArrayList<String> mTextArray1 = new ArrayList<String>();
		mTextArray1.add(cookstylename);
		mTextArray1.add("籍贯");
		mTextArray1.add("距离");
		expandTabView1.setValue(mTextArray1, mViewArray);
		expandTabView1.setTitle(viewLeft.getShowText(), 0);
		expandTabView1.setTitle(viewMiddle.getShowText(), 1);
		expandTabView1.setTitle(viewRight.getShowText(), 2);
		initChushiListener();
	}

	private void initCaipinValue() {
		// TODO Auto-generated method stub
		mViewArray1 = new ArrayList<View>();
		viewLeft1 = new ViewLeft(getActivity(), LoadingActivity.caixis);
		viewRight1 = new ViewRight(getActivity());

		mViewArray1.add(viewLeft1);
		mViewArray1.add(viewRight1);

		ArrayList<String> mTextArray2 = new ArrayList<String>();
		mTextArray2.add(cookstylename);
		mTextArray2.add("食材");
		expandTabView2.setValue(mTextArray2, mViewArray1);
		expandTabView2.setTitle(viewLeft1.getShowText(), 0);
		expandTabView2.setTitle(viewRight1.getShowText(), 1);
		initCaipinListener();
	}

	private void initView() {
		// TODO Auto-generated method stub
		btn_chushi = (TextView) getActivity()
				.findViewById(R.id.category_chushi);
		btn_chushi.setOnClickListener(this);
		btn_caipin = (TextView) getActivity()
				.findViewById(R.id.category_caipin);
		btn_caipin.setOnClickListener(this);
		list_caipin = (PullToRefreshListView) getActivity().findViewById(
				R.id.list_caipin);
		list_caipin.setPullLoadEnabled(false);
		list_caipin.setScrollLoadEnabled(true);
		mListView2 = list_caipin.getRefreshableView();
		mListView2.setDivider(getResources().getDrawable(R.color.line_divide));
		mListView2.setDividerHeight(16);
		list_chushi = (PullToRefreshListView) getActivity().findViewById(
				R.id.list_chushi);
		list_chushi.setPullLoadEnabled(false);
		list_chushi.setScrollLoadEnabled(true);
		mListView = list_chushi.getRefreshableView();
		mListView.setDivider(getResources().getDrawable(R.color.line_divide));
		mListView.setDividerHeight(16);

		expandTabView1 = (ExpandTabView) getActivity().findViewById(
				R.id.expandtab_view1);
		expandTabView2 = (ExpandTabView) getActivity().findViewById(
				R.id.expandtab_view2);
	}

	public void initValues() {
		if (SWITCH == Common.CHUSHI) {
			setChuShiVisiable();
			if (expandTabView1.getChildCount() == 0) {
				initChushiValue();
			}

			if (chefs != null) {
				myHandler.sendEmptyMessage(0);
			} else {
				setLastUpdateTime();
				list_chushi.doPullRefreshing(true, 0);
			}
		} else if (SWITCH == Common.CAIPIN) {
			setCaiPinVisiable();
			if (expandTabView2.getChildCount() == 0) {
				initCaipinValue();
			}

			if (dishs != null) {
				myHandler.sendEmptyMessage(1);
			} else {
				setLastUpdateTime2();
				list_caipin.doPullRefreshing(true, 0);
			}
		}
	}

	private void loadMore() {
		// TODO Auto-generated method stub
		countRecord = chefBean.getChefInfo().getCountRecord();
		if (pagecount < countRecord) {
			hasMoreData = true;
			pagecount = pagecount + 10;
			new Thread(chefRunnable).start();
		} else {
			hasMoreData = false;
			myHandler.sendEmptyMessage(444);
		}
	}

	private void loadMore2() {
		// TODO Auto-generated method stub
		countRecord = dishBean.getDishInfo().getCountRecord();
		if (pagecount < countRecord) {
			hasMoreData2 = true;
			pagecount = pagecount + 10;
			new Thread(dishRunnable).start();
		} else {
			hasMoreData2 = false;
			myHandler.sendEmptyMessage(445);
		}
	}

	private void onRefresh(View view, String showText) {

		expandTabView1.onPressBack();
		int position = getPositon(view);
		if (position >= 0
				&& !expandTabView1.getTitle(position).equals(showText)) {
			expandTabView1.setTitle(showText, position);
		}
	}

	private void onRefresh2(View view, String showText) {

		expandTabView2.onPressBack();
		int position = getPositon(view);
		if (position >= 0
				&& !expandTabView2.getTitle(position).equals(showText)) {
			expandTabView2.setTitle(showText, position);
		}
	}

	private void onRefreshLeft(View view, String showText, List<Caixi> list) {

		expandTabView1.onPressBack();
		int position = getPositon(view);
		if (position >= 0
				&& !expandTabView1.getTitle(position).equals(showText)) {
			expandTabView1.setTitle(showText, position);
		}
		if (ViewLeft.position == -1) {
			cookstyleid = 0;
		} else {
			cookstyleid = LoadingActivity.caixis.get(ViewLeft.position).getId();
		}
		if (SWITCH == Common.CHUSHI) {
			setLastUpdateTime();
			list_chushi.doPullRefreshing(true, 0);
		} else {
			setLastUpdateTime2();
			list_caipin.doPullRefreshing(true, 0);
		}
	}

	private void onRefreshLeft2(View view, String showText, List<Caixi> list) {

		expandTabView2.onPressBack();
		int position = getPositon(view);
		if (position >= 0
				&& !expandTabView2.getTitle(position).equals(showText)) {
			expandTabView2.setTitle(showText, position);
		}
		if (ViewLeft.position == -1) {
			cookstyleid = 0;
		} else {
			cookstyleid = LoadingActivity.caixis.get(ViewLeft.position).getId();
		}
		if (SWITCH == Common.CHUSHI) {
			setLastUpdateTime();
			list_chushi.doPullRefreshing(true, 0);
		} else {
			setLastUpdateTime2();
			list_caipin.doPullRefreshing(true, 0);
		}
	}

	private void onRefreshMiddle(View view, String showText, List<String> list) {

		expandTabView1.onPressBack();
		int position = getPositon(view);
		if (position >= 0
				&& !expandTabView1.getTitle(position).equals(showText)) {
			expandTabView1.setTitle(showText, position);
		}
		if (ViewMiddle1.position == -1) {
			nativeplace = "0";
		} else {
			nativeplace = LoadingActivity.jiguans.get(ViewMiddle1.position);
		}
		setLastUpdateTime();
		list_chushi.doPullRefreshing(true, 0);
	}

	private int getPositon(View tView) {
		for (int i = 0; i < mViewArray.size(); i++) {
			if (mViewArray.get(i) == tView) {
				return i;
			}
		}
		return -1;
	}

	Runnable chefRunnable = new Runnable() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			position = LoadingActivity.loca.getAddr();
			chefBean = CommonFun1.getChefList(cookstyleid, nativeplace,
					distanceType, dishcount, page, pagecount, position);
			if (chefBean != null) {
				myHandler.sendEmptyMessage(chefBean.getErrcode());
			} else {
				myHandler.sendEmptyMessage(444);
			}
		}
	};

	Runnable dishRunnable = new Runnable() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			dishBean = CommonFun1.getDishList(cookstyleid, null, page,
					pagecount);
			if (dishBean != null) {
				myHandler.sendEmptyMessage(1);
			}
		}
	};

	@SuppressLint("HandlerLeak")
	private Handler myHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				chefs = chefBean.getChefInfo().getList();
				adapter = new ChefAdapter(getActivity(), chefBean.getChefInfo()
						.getList());
				mListView.setAdapter(adapter);
				if (chefs.size() > 0) {
					list_chushi.onPullDownRefreshComplete();
					list_chushi.onPullUpRefreshComplete();
					setLastUpdateTime();
				} else {
					myHandler.sendEmptyMessage(444);
				}
				break;
			case 1:
				dishs = dishBean.getDishInfo().getList();
				adapter2 = new DishAdapter(getActivity(), dishs);
				mListView2.setAdapter(adapter2);
				if (dishs.size() > 0) {
					list_caipin.onPullDownRefreshComplete();
					list_caipin.onPullUpRefreshComplete();
					setLastUpdateTime2();
				} else {
					myHandler.sendEmptyMessage(445);
				}
				break;
			case 444:
				list_chushi.onPullDownRefreshComplete();
				list_chushi.onPullUpRefreshComplete();
				list_chushi.setHasMoreData(hasMoreData);
				setLastUpdateTime();
				break;
			case 445:
				list_caipin.onPullDownRefreshComplete();
				list_caipin.onPullUpRefreshComplete();
				list_caipin.setHasMoreData(hasMoreData2);
				setLastUpdateTime2();
				break;
			case 911:
				break;
			default:
				break;
			}
		};
	};

	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.category_caipin:
			expandTabView1.onPressBack();
			expandTabView2.onPressBack();
			setCaiPinVisiable();
			if (SWITCH == Common.CHUSHI) {
				if (expandTabView2.getChildCount() == 0) {
					initCaipinValue();
				}
				if (dishs != null) {
					myHandler.sendEmptyMessage(1);
				} else {
					setLastUpdateTime2();
					list_caipin.doPullRefreshing(true, 0);
				}
			}
			SWITCH = Common.CAIPIN;
			break;
		case R.id.category_chushi:
			expandTabView1.onPressBack();
			expandTabView2.onPressBack();
			setChuShiVisiable();
			if (SWITCH == Common.CAIPIN) {
				// removeData();
				if (expandTabView1.getChildCount() == 0) {
					initChushiValue();
				}
				if (chefs != null) {
					myHandler.sendEmptyMessage(0);
				} else {
					setLastUpdateTime();
					list_chushi.doPullRefreshing(true, 0);
				}
			}
			SWITCH = Common.CHUSHI;
			break;
		default:
			break;
		}
	}

	private void setCaiPinVisiable() {
		btn_caipin.setPressed(true);
		btn_caipin.setBackgroundResource(R.drawable.category_caipin_press);
		btn_chushi.setBackgroundResource(R.drawable.category_chushi_normal);
		btn_caipin.setTextColor(getResources().getColor(R.color.white));
		btn_chushi.setTextColor(getResources().getColor(R.color.orange));
		list_caipin.setVisibility(View.VISIBLE);
		list_chushi.setVisibility(View.GONE);
		expandTabView1.setVisibility(View.GONE);
		expandTabView2.setVisibility(View.VISIBLE);
	}

	private void setChuShiVisiable() {
		btn_chushi.setBackgroundResource(R.drawable.category_chushi_press);
		btn_caipin.setBackgroundResource(R.drawable.category_caipin_normal);
		btn_chushi.setTextColor(getResources().getColor(R.color.white));
		btn_caipin.setTextColor(getResources().getColor(R.color.orange));
		list_caipin.setVisibility(View.GONE);
		list_chushi.setVisibility(View.VISIBLE);
		expandTabView1.setVisibility(View.VISIBLE);
		expandTabView2.setVisibility(View.GONE);
	}

	/**
	 * 清空数据
	 */
	// private void removeData() {
	// System.out.println("removeData");
	// if (mViewArray != null) {
	// mViewArray.clear();
	// mViewArray = null;
	// }
	// if (mViewArray1 != null) {
	// mViewArray1.clear();
	// mViewArray1 = null;
	// }
	// if (expandTabView2.getChildCount() != 0) {
	// viewLeft1 = null;
	// viewRight1 = null;
	// expandTabView2.removeAllViews();
	// }
	// if (expandTabView1.getChildCount() != 0) {
	// viewLeft = null;
	// viewMiddle = null;
	// viewRight = null;
	// expandTabView1.removeAllViews();
	// }
	// }

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		// removeData();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void setMenuVisibility(boolean menuVisible) {
		super.setMenuVisibility(menuVisible);
		if (this.getView() != null)
			this.getView()
					.setVisibility(menuVisible ? View.VISIBLE : View.GONE);
	}

	private void setLastUpdateTime() {
		String text = formatDateTime(System.currentTimeMillis());
		list_chushi.setLastUpdatedLabel(text);
	}

	private void setLastUpdateTime2() {
		String text = formatDateTime(System.currentTimeMillis());
		list_caipin.setLastUpdatedLabel(text);
	}

	@SuppressLint("SimpleDateFormat")
	private SimpleDateFormat mDateFormat = new SimpleDateFormat("MM-dd HH:mm");

	private String formatDateTime(long time) {
		if (0 == time) {
			return "";
		}
		return mDateFormat.format(new Date(time));
	}

}
