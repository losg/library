package com.losg.pay;

import android.content.Context;

/**
 * Created by come on 2016/1/21.
 */
public class PayUtils {

    //支付成功广播
    public static final String PAY_SUCCESS = "com.losg.pay.success";
    //支付失败广播
    public static final String PAY_FAILURE = "com.losg.pay.failure";
    //用户取消支付
    public static final String PAY_CANCEL  = "com.losg.pay.cancel";

    public enum PayType {
        ALIPY,          //支付宝支付
        WEI_XIN         //微信支付
    }

    public static void toPay(Context context, PayType payType, PayInfo payInfo) {
        if (payType == PayType.ALIPY) {
            new Alipy(context, payInfo);
        } else if (payType == PayType.WEI_XIN) {
            new WeiXin(context, payInfo);
        }
    }

    public interface PayInfo {

    }

    public static class AlipyInfo implements PayInfo {

        public String payInfo;
    }

    public static class WeiXinInfo implements PayInfo {

        public String appID;        //appid
        public String partnerId;    //商户ID
        public String prepayId;     //微信返回的订单id
        public String nonceStr;     //随机字符串
        public String timeStamp;    //时间戳
        public String packageValue; //包名
        public String sign;         //校验数据
        public String extData;      //其它信息
    }

}
