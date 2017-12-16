package com.losg.library.widget.loading;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;

/**
 * Created by losg on 2016/6/9.
 */
public abstract class BaLoadingViewHelper<T extends BaLoadingView> {

    private final static String TAG = "BaLoadingViewHelper";

    private View    mBindView;
    private T       mLoadingView;
    private Context mContext;

    public BaLoadingViewHelper(Context context) {
        mContext = context;
        mLoadingView = createLoadingView(context);
    }

    //绑定View
    public void bindView(View view) {
        this.mBindView = view;
        ViewParent parent = view.getParent();
        if (parent == null) {
            Log.e(TAG, "所绑定的父布局不能为空");
            return;
        }
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        int position = ((ViewGroup) parent).indexOfChild(view);
        ((ViewGroup) parent).removeView(view);
        FrameLayout frameLayout = new FrameLayout(mContext);
        ((ViewGroup) parent).addView(frameLayout, position, layoutParams);
        frameLayout.addView(view, new ViewGroup.LayoutParams(-1, -1));
        frameLayout.addView(mLoadingView, new ViewGroup.LayoutParams(-1, -1));
        view.setVisibility(View.INVISIBLE);
        view.setEnabled(false);
    }

    protected abstract T createLoadingView(Context context);

    public void setStatus(BaLoadingView.LoadingStatus loadingStatus) {
        if (mBindView == null) return;
        mLoadingView.setLoadingStatus(loadingStatus);
        if (loadingStatus != BaLoadingView.LoadingStatus.LADING_SUCCESS) {
            mLoadingView.setVisibility(View.VISIBLE);
            mBindView.setVisibility(View.INVISIBLE);
            mBindView.setEnabled(false);
            return;
        }
        mLoadingView.setVisibility(View.GONE);
        mBindView.setVisibility(View.VISIBLE);
        mBindView.setEnabled(true);
    }
}
