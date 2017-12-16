package com.losg.library.widget.loading;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by losg on 2016/8/27.
 */

public abstract class BaLoadingView extends LinearLayout {

    //加载状态
    public enum LoadingStatus {
        LOADING,         //加载中
        NET_ERROR,       //网络异常
        SERVER_ERROR,    //服务器繁忙
        RESULT_NULL,     //返回内容为空
        LADING_SUCCESS,  //加载成功
        CONNECT_TIMEOUT  //链接服务器超时
    }

    //当前加载状态
    private LoadingStatus mLoadingStatus;
    //当前View
    private View          mView;

    public BaLoadingView(Context context) {
        this(context, null);
    }

    public BaLoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mView = createView();
        if (mView == null) {
            mView = View.inflate(getContext(), initViewResource(), this);
        } else {
            addView(mView, new LayoutParams(-1, -1));
        }
        initView(mView);
    }

    protected abstract int initViewResource();

    protected View createView() {
        return null;
    }

    protected abstract void initView(View view);

    public void setLoadingStatus(LoadingStatus loadingStatus) {
        if (mLoadingStatus == loadingStatus) {
            return;
        }
        this.mLoadingStatus = loadingStatus;
        if (loadingStatus == LoadingStatus.LOADING) {
            loadding();
        } else if (loadingStatus == LoadingStatus.NET_ERROR) {
            networkError();
        } else if (loadingStatus == LoadingStatus.SERVER_ERROR) {
            serviceError();
        } else if (loadingStatus == LoadingStatus.RESULT_NULL) {
            resultNull();
        } else if (loadingStatus == LoadingStatus.CONNECT_TIMEOUT) {
            connectTimeout();
        }
    }

    //连接超时
    protected abstract void connectTimeout();

    //加载中
    protected abstract void loadding();

    //网络错误
    protected abstract void networkError();

    //返回数据空
    protected abstract void resultNull();

    //服务器错误
    protected abstract void serviceError();
}
