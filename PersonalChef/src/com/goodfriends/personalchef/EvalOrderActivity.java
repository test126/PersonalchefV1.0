package com.goodfriends.personalchef;

import com.goodfriends.personalchef.application.SysApplication;
import com.goodfriends.personalchef.bean.Chefs;
import com.goodfriends.personalchef.bean.OrderBean;
import com.goodfriends.personalchef.common.CommonFun1;
import com.goodfriends.personalchef.util.AsynImageLoader;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.Toast;

public class EvalOrderActivity extends Activity implements OnClickListener,
		OnRatingBarChangeListener {

	private OrderBean orderBean;
	private String orderId, content;
	private ImageView back, chefHead, certification, gold, star, grade1,
			grade2, grade3, grade4, grade5;
	private TextView chefName, jiguan, caixi, send;
	private Chefs chefs;
	private RatingBar rating1, rating2, rating3;
	private int userid, masterid, i1 = 0, i2 = 0, i3 = 0;
	private EditText editText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		SysApplication.getInstance().addActivity(this);
		orderId = getIntent().getExtras().getString("orderId");
		setContentView(R.layout.activity_eval);
		userid = getSharedPreferences("USERINFO", MODE_PRIVATE).getInt(
				"userId", 0);
		initView();
		new Thread(runnable).start();
	}

	private void initView() {
		// TODO Auto-generated method stub
		back = (ImageView) findViewById(R.id.title_back);
		back.setOnClickListener(this);
		chefHead = (ImageView) findViewById(R.id.orderdetail_head);
		chefName = (TextView) findViewById(R.id.orderdetail_name);
		jiguan = (TextView) findViewById(R.id.orderdetail_jiguan);
		caixi = (TextView) findViewById(R.id.orderdetail_caixi);
		certification = (ImageView) findViewById(R.id.orderdetail_certification);
		gold = (ImageView) findViewById(R.id.orderdetail_gold);
		star = (ImageView) findViewById(R.id.orderdetail_star);
		grade1 = (ImageView) findViewById(R.id.orderdeatail_grade1);
		grade2 = (ImageView) findViewById(R.id.orderdeatail_grade2);
		grade3 = (ImageView) findViewById(R.id.orderdeatail_grade3);
		grade4 = (ImageView) findViewById(R.id.orderdeatail_grade4);
		grade5 = (ImageView) findViewById(R.id.orderdeatail_grade5);
		rating1 = (RatingBar) findViewById(R.id.ratingbar1);
		rating1.setOnRatingBarChangeListener(this);
		rating2 = (RatingBar) findViewById(R.id.ratingbar2);
		rating2.setOnRatingBarChangeListener(this);
		rating3 = (RatingBar) findViewById(R.id.ratingbar3);
		rating3.setOnRatingBarChangeListener(this);
		send = (TextView) findViewById(R.id.tijiao);
		send.setOnClickListener(this);
		editText = (EditText) findViewById(R.id.eval_remark);
	}

	Runnable runnable2 = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			boolean b = CommonFun1.evalOrder(orderId, userid, masterid, i1, i2,
					i3, content);
			if (b) {
				myHandler.sendEmptyMessage(1);
			} else {
				myHandler.sendEmptyMessage(404);
			}
		}
	};

	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			orderBean = CommonFun1.getOrderDeatail(orderId);
			if (orderBean != null) {
				myHandler.sendEmptyMessage(orderBean.getErrcode());
			}
		}
	};

	@SuppressLint("HandlerLeak")
	private Handler myHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				setChefInfo();
				break;
			case 1:
				closeDialog();
				Toast.makeText(getApplicationContext(), "已成功评价",
						Toast.LENGTH_SHORT).show();
				EvalOrderActivity.this.finish();
				break;
			case 403:
				Toast.makeText(getApplicationContext(), "请输入评价内容",
						Toast.LENGTH_SHORT).show();
				break;
			case 404:
				closeDialog();
				Toast.makeText(getApplicationContext(), "评价失败，请稍候重试",
						Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		}
	};

	private void setChefInfo() {
		// TODO Auto-generated method stub
		chefs = orderBean.getChefs();
		masterid = chefs.getId();
		AsynImageLoader asynImageLoader = new AsynImageLoader();
		asynImageLoader.showImageAsyn(chefHead, chefs.getHeadpicurl(),
				R.drawable.nopic);
		chefName.setText(chefs.getName());
		jiguan.setText("籍贯：" + chefs.getNativeplace());
		caixi.setText("擅长：" + chefs.getCookstyles());
		if (chefs.getIsauth() == 1) {
			certification.setVisibility(View.VISIBLE);
		}
		switch (chefs.getGrade()) {
		case 1:
			break;
		case 2:
			gold.setVisibility(View.VISIBLE);
			break;
		case 3:
			gold.setVisibility(View.VISIBLE);
			break;
		case 4:
			star.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}
		switch (chefs.getScore()) {
		case 1:
			grade1.setBackgroundResource(R.drawable.cookgrade_icon_orange);
			break;
		case 2:
			grade1.setBackgroundResource(R.drawable.cookgrade_icon_orange);
			grade2.setBackgroundResource(R.drawable.cookgrade_icon_orange);
			break;
		case 3:
			grade1.setBackgroundResource(R.drawable.cookgrade_icon_orange);
			grade2.setBackgroundResource(R.drawable.cookgrade_icon_orange);
			grade3.setBackgroundResource(R.drawable.cookgrade_icon_orange);
			break;
		case 4:
			grade1.setBackgroundResource(R.drawable.cookgrade_icon_orange);
			grade2.setBackgroundResource(R.drawable.cookgrade_icon_orange);
			grade3.setBackgroundResource(R.drawable.cookgrade_icon_orange);
			grade4.setBackgroundResource(R.drawable.cookgrade_icon_orange);
			break;
		case 5:
			grade1.setBackgroundResource(R.drawable.cookgrade_icon_orange);
			grade2.setBackgroundResource(R.drawable.cookgrade_icon_orange);
			grade3.setBackgroundResource(R.drawable.cookgrade_icon_orange);
			grade4.setBackgroundResource(R.drawable.cookgrade_icon_orange);
			grade5.setBackgroundResource(R.drawable.cookgrade_icon_orange);
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.title_back:
			this.finish();
			break;
		case R.id.tijiao:
			content = editText.getText().toString().trim();
			if (content.equals("")) {
				myHandler.sendEmptyMessage(403);
			} else {
				showDialog("提交中...");
				new Thread(runnable2).start();
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onRatingChanged(RatingBar arg0, float arg1, boolean arg2) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.ratingbar1:
			i1 = (int) arg1;
			break;
		case R.id.ratingbar2:
			i2 = (int) arg1;
			break;
		case R.id.ratingbar3:
			i3 = (int) arg1;
			break;
		default:
			break;
		}
	};

	private ProgressDialog progressDialog;

	private void showDialog(String s) {
		progressDialog = new ProgressDialog(this);
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
}
