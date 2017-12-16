package com.losg.pay;

import android.content.Context;

/**
 * Created by come on 2016/1/21.
 */
public abstract class BasePay {

    protected Context          mContext;
    protected PayUtils.PayInfo mPayInfo;

    public BasePay(Context context, PayUtils.PayInfo payInfo) {
        mContext = context;
        mPayInfo = payInfo;
        toPay(payInfo);
    }

    protected abstract void toPay(PayUtils.PayInfo payInfo);
}
