package com.goodfriends.personalchef.util;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.goodfriends.personalchef.bean.GsonAdvs;
import com.goodfriends.personalchef.common.CommonFun;
import com.goodfriends.personalchef.common.Msg;
import com.google.gson.Gson;

public class VolleyHttp {
	private RequestQueue mQueue;
	private Handler handler;

	public VolleyHttp(Context context, Handler handler) {
		super();
		this.handler = handler;
		mQueue = Volley.newRequestQueue(context);
	}

	/**
	 * 根据url发送get请求 返回json字符串,并解析
	 * 
	 * @param url
	 */
	public void requestAdvs() {
		Listener listener = new Response.Listener<String>() {
			public void onResponse(String response) {
				Message msg = new Message();
				msg.what = Msg.get_advs_content;
				List advs = parseAdvs(response);
				msg.obj = advs;
				handler.sendMessage(msg);

			}
		};

		ErrorListener errorListener = new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
			}
		};
		Request request = new StringRequest(CommonFun.advurl, listener, errorListener);
		mQueue.add(request);
		mQueue.start();
	}

	/**
	 * 
	 * @param strJson
	 * @return 根据返回的字符串解析成朋友信息列表，也可能是一个的信息
	 */
	public List parseAdvs(String strJson) {
		Gson gson = new Gson();
		GsonAdvs advs = gson.fromJson(strJson, GsonAdvs.class);
		
		return advs.getContent();
	}

}
