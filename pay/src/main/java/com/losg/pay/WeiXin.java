package com.losg.pay;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.tencent.mm.sdk.constants.Build;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * Created by losg on 2016/10/8.
 */
public class WeiXin extends BasePay {

    private IWXAPI mApi;

    public WeiXin(Context context, PayUtils.PayInfo payInfo) {
        super(context, payInfo);
    }

    @Override
    protected void toPay(PayUtils.PayInfo payInfo) {
        if (!(payInfo instanceof PayUtils.WeiXinInfo)) {
            Log.e("losg_log", "payinfo must instanceof WeiXinInfo");
            return;
        }
        if (!(mContext instanceof Activity)) {
            Log.e("losg_log", "context must instanceof Activity");
            return;
        }
        mApi = WXAPIFactory.createWXAPI(mContext, ((PayUtils.WeiXinInfo) payInfo).appID);

        boolean isPaySupported = mApi.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
        if (!isPaySupported) {
            Toast.makeText(mContext, mContext.getResources().getString(R.string.weixin_version_not_support), Toast.LENGTH_SHORT).show();
            return;
        }

        PayReq req = new PayReq();
        req.appId = ((PayUtils.WeiXinInfo) payInfo).appID;
        req.partnerId = ((PayUtils.WeiXinInfo) payInfo).partnerId;
        req.prepayId = ((PayUtils.WeiXinInfo) payInfo).prepayId;
        req.nonceStr = ((PayUtils.WeiXinInfo) payInfo).nonceStr;
        req.timeStamp = ((PayUtils.WeiXinInfo) payInfo).timeStamp;
        req.packageValue = ((PayUtils.WeiXinInfo) payInfo).packageValue;
        req.sign = ((PayUtils.WeiXinInfo) payInfo).sign;
        req.extData = ((PayUtils.WeiXinInfo) payInfo).extData;
        req.options = new PayReq.Options();
        req.options.callbackClassName = "com.losg.pay.WXPayEntryActivity";
        mApi.sendReq(req);
    }
}
