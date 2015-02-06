package com.goodfriends.personalchef.wxapi;

import android.widget.Toast;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.umeng.socialize.weixin.view.WXCallbackActivity;

public class WXEntryActivity extends WXCallbackActivity {

	@Override
	public void onReq(BaseReq req) {
		// TODO Auto-generated method stub
		super.onReq(req);
	}

	@Override
	public void onResp(BaseResp resp) {
		// TODO Auto-generated method stub
		super.onResp(resp);
		if (resp.errCode == BaseResp.ErrCode.ERR_OK) {
			
			// new Thread(new Runnable() {
			// @Override
			// public void run() {
			// // TODO Auto-generated method stub
			// CommonFun1.notifyShare(
			// getSharedPreferences("USERINFO",
			// Context.MODE_PRIVATE).getInt("userId", 0),
			// channel, "");
			// }
			// }).start();
			Toast.makeText(this, "分享成功", Toast.LENGTH_LONG).show();
		}
	}

}