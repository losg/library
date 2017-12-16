package com.losg.pay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.alipay.sdk.app.PayTask;

import java.util.Map;

/**
 * Created by losg on 2016/10/8.
 */
public class Alipy extends BasePay {

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            PayResult payResult = new PayResult((Map<String, String>) msg.obj);
            String resultStatus = payResult.resultStatus;
            LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(mContext);
            // 判断resultStatus 为9000则代表支付成功
            if (TextUtils.equals(resultStatus, "9000")) {
                localBroadcastManager.sendBroadcast(new Intent(PayUtils.PAY_SUCCESS));
            } else if (TextUtils.equals(resultStatus, "6001")) {
                localBroadcastManager.sendBroadcast(new Intent(PayUtils.PAY_CANCEL));
            } else
                localBroadcastManager.sendBroadcast(new Intent(PayUtils.PAY_FAILURE));
        }
    };

    public Alipy(Context context, PayUtils.PayInfo payInfo) {
        super(context, payInfo);
    }

    @Override
    protected void toPay(final PayUtils.PayInfo payInfo) {
        if (!(payInfo instanceof PayUtils.AlipyInfo)) {
            Log.e("losg_log", "payinfo must instanceof AlipayInfo");
            return;
        }
        if (!(mContext instanceof Activity)) {
            Log.e("losg_log", "context must instanceof Activity");
            return;
        }
        new Thread(new PayThread()).start();
    }

    private class PayThread implements Runnable {

        @Override
        public void run() {
            PayTask alipay = new PayTask((Activity) mContext);
            Map<String, String> result = alipay.payV2(((PayUtils.AlipyInfo) mPayInfo).payInfo, true);
            Message msg = new Message();
            msg.obj = result;
            mHandler.sendMessage(msg);
        }
    }
}
