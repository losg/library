
package com.losg.library.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.losg.library.widget.dialog.ProgressDialog;
import com.losg.library.widget.dialog.ProgressDialog.DialogForceCloseListener;
import com.losg.library.widget.loading.BaLoadingView.LoadingStatus;
import com.losg.library.widget.loading.BaLoadingViewHelper;

import java.util.HashMap;

public abstract class BaFragment extends Fragment implements BaseView {

    protected Context                               mContext;
    private   HashMap<Integer, BaLoadingViewHelper> mLoadingViews;
    private   String                                mTitle;
    private   IRefreshView                          mIRefreshView;

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(this.initLayout(), container, false);
        this.initParams();
        if (savedInstanceState != null) {
            this.restoreInstance(savedInstanceState);
        }

        this.bindView(view);
        this.initView(view);
        this.initOthers(view);
        return view;
    }

    /**
     * 绑定view 留给butterknife 或 findview
     */
    protected void bindView(View view) {
    }

    protected void initOthers(View view) {
    }

    /**
     * 绑定布局文件
     *
     * @return
     */
    protected abstract int initLayout();

    /**
     * 初始化view
     */
    protected abstract void initView(View view);

    /**
     * fragment 修改上次activity的标题
     *
     * @param title
     */
    public void setTitle(String title) {
        this.mTitle = title;
        if (this.mContext != null && !TextUtils.isEmpty(this.mTitle)) {
            ((BaActivity) this.mContext).setTitle(this.mTitle);
        }

    }

    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden && this.mContext != null && !TextUtils.isEmpty(this.mTitle)) {
            ((BaActivity) this.mContext).setTitle(this.mTitle);
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (!TextUtils.isEmpty(this.mTitle)) {
            outState.putString("fragment_title", this.mTitle);
        }
    }

    protected void restoreInstance(Bundle savedInstanceState) {
        this.mTitle = savedInstanceState.getString("fragment_title");
    }

    private void initParams() {
        this.mContext = this.getActivity();
        this.mLoadingViews = new HashMap();
    }

    /***
     * 绑定刷新控件
     *
     * @param refreshView
     */
    protected void bindRefreshView(IRefreshView refreshView) {
        mIRefreshView = refreshView;
    }

    protected <T extends BaLoadingViewHelper> void bindLoadingView(T loadingHelper, View view, int tag) {
        loadingHelper.bindView(view);
        this.mLoadingViews.put(tag, loadingHelper);
    }

    public void changeLoadingStatus(LoadingStatus status, int tag) {
        BaLoadingViewHelper loadingViewHelper = this.mLoadingViews.get(tag);
        if (loadingViewHelper != null) {
            loadingViewHelper.setStatus(status);
        }
    }

    public void refreshStatus(RefreshStatus refreshStatus, @Nullable ErrorStatus errorStatus) {
        if (mIRefreshView != null) {
            mIRefreshView.setRefreshStatus(refreshStatus);
        }
        if (refreshStatus == RefreshStatus.Failure) {
            if (errorStatus != null)
                this.toastError(errorStatus);
        }
    }

    public void showWaitDialog(String message, DialogForceCloseListener dialogForceCloseListener) {
        ((BaActivity) this.mContext).showWaitDialog(message, dialogForceCloseListener);
    }

    public void showWaitDialog(boolean showClose, String message, ProgressDialog.DialogForceCloseListener dialogForceCloseListener){
        ((BaActivity) this.mContext).showWaitDialog(showClose, message, dialogForceCloseListener);
    }

    public void dismissWaitDialog() {
        ((BaActivity) this.mContext).dismissWaitDialog();
    }

    public void toastError(ErrorStatus errorStatus) {
        ((BaActivity) this.mContext).toastError(errorStatus);
    }

    public void toastMessage(String message) {
        ((BaActivity) this.mContext).toastMessage(message);
    }

}
