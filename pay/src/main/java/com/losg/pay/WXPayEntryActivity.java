package com.losg.pay;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;


public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String appID = findMetaData();
        if (TextUtils.isEmpty(appID)) {
            Log.e("losg_log", "metadata WEI_XIN_APPID can not null");
            finish();
            return;
        }
        api = WXAPIFactory.createWXAPI(this, appID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
        if (resp.errCode == 0) {
            //支付成功
            localBroadcastManager.sendBroadcast(new Intent(PayUtils.PAY_SUCCESS));
        } else if (resp.errCode == -1) {
            //可能的原因：签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常等。
            localBroadcastManager.sendBroadcast(new Intent(PayUtils.PAY_FAILURE));
        } else {
            //取消支付
            localBroadcastManager.sendBroadcast(new Intent(PayUtils.PAY_CANCEL));
        }
        finish();
    }


    private String findMetaData() {
        try {
            ApplicationInfo applicationInfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            String wei_xin_appid = applicationInfo.metaData.getString("WEI_XIN_APPID");
            return wei_xin_appid;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}