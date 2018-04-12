
package com.losg.library.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils.TruncateAt;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.losg.library.R;
import com.losg.library.R.color;
import com.losg.library.R.dimen;
import com.losg.library.R.id;
import com.losg.library.R.layout;
import com.losg.library.R.mipmap;
import com.losg.library.R.string;
import com.losg.library.utils.CommonUtils;
import com.losg.library.utils.DisplayUtil;
import com.losg.library.widget.TransStatusBar;
import com.losg.library.widget.dialog.ProgressDialog;
import com.losg.library.widget.dialog.ProgressDialog.DialogForceCloseListener;
import com.losg.library.widget.loading.BaLoadingView.LoadingStatus;
import com.losg.library.widget.loading.BaLoadingViewHelper;

import java.util.HashMap;

public abstract class BaActivity extends AppCompatActivity implements BaseView {

    protected Context                               mContext;
    protected Toolbar                               mToolbar;
    protected LinearLayout                          mToolLayer;
    protected TransStatusBar                        mTransStatusBar;
    protected TextView                              mTitleView;
    private   ProgressDialog                        mWaitDialog;
    private   HashMap<Integer, BaLoadingViewHelper> mLoadingViews;
    private   IRefreshView                          mIRefreshView;
    private   IWaitDialog                           mIWaitDialog;
    private   IToast                                mIToast;

    public BaActivity() {
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(layout.base_activity_base);
        LinearLayout linearLayout = (LinearLayout) this.findViewById(id.root_view);
        mToolLayer = (LinearLayout) this.findViewById(id.tool_layer);
        mTransStatusBar = (TransStatusBar) this.findViewById(id.status_bar);
        View view = View.inflate(this, this.initLayout(), null);
        if (view != null) {
            linearLayout.addView(view, new LayoutParams(-1, -1));
        }

        if (savedInstanceState != null) {
            this.restoreInstance(savedInstanceState);
        }

        this.initParams();
        this.bindView();
        this.initToolbar();
        this.initView();
        this.initOthers();
    }

    public void setStatusTrans() {
        mTransStatusBar.setVisibility(View.VISIBLE);
    }

    protected void initOthers() {
    }

    /**
     * 绑定布局文件
     *
     * @return
     */
    protected abstract int initLayout();

    /**
     * 绑定view 留给butterknife 或 findview
     */
    protected void bindView() {

    }


    /***
     * 初始化view信息
     */
    protected abstract void initView();

    /**
     * 恢复数据
     *
     * @param savedInstanceState
     */
    protected void restoreInstance(Bundle savedInstanceState) {
    }

    /***
     * 初始化全局变量
     */
    private void initParams() {
        this.mContext = this;
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

    /**
     * 绑定等待对话框 若为空，则采用默认
     */
    protected void bindWaitDialog(IWaitDialog iWaitDialog) {
        mIWaitDialog = iWaitDialog;
    }

    /**
     * 绑定自定toast
     *
     * @param iToast
     */
    protected void bindToast(IToast iToast) {
        mIToast = iToast;
    }

    /***
     * 添加标题栏 居中显示标题
     */
    private void initToolbar() {
        this.mToolbar = (Toolbar) this.findViewById(id.toolbar);
        this.mToolbar.setNavigationIcon(mipmap.ic_toolbar_back);
        this.setSupportActionBar(this.mToolbar);
        this.mToolbar.setTitleTextColor(-1);
        this.mTitleView = new TextView(this);
        mTitleView.setLines(1);
        this.mTitleView.setTextSize((float) DisplayUtil.px2sp(this, this.getResources().getDimension(dimen.base_title_size)));
        this.mTitleView.setEllipsize(TruncateAt.valueOf("END"));
        this.mTitleView.setTextColor(this.getResources().getColor(color.base_white));
        android.support.v7.app.ActionBar.LayoutParams params = new android.support.v7.app.ActionBar.LayoutParams(-2, -2);
        params.gravity = params.gravity & ~Gravity.HORIZONTAL_GRAVITY_MASK | Gravity.CENTER_HORIZONTAL;
        this.getSupportActionBar().setCustomView(this.mTitleView, params);
        this.getSupportActionBar().setDisplayShowCustomEnabled(true);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setTitle("");
    }

    /**
     * 设置标题名称
     *
     * @param title
     */
    public void setTitle(String title) {
        this.mTitleView.setText(title);
    }

    /**
     * 获取标题栏
     *
     * @return
     */
    public Toolbar getToolbar() {
        return mToolbar;
    }

    /***
     * 显示or隐藏返回键
     *
     * @param visible
     */
    public void setDisplayHomeAsUpEnabled(boolean visible) {
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(visible);
    }

    /**
     * 提示错误信息
     *
     * @param errorStatus
     */
    public void toastError(ErrorStatus errorStatus) {
        String errorMessage = "";
        if (errorStatus == ErrorStatus.ERROR_CONNECT_TIME_OUT) {
            errorMessage = this.getResources().getString(string.net_connect_timeout);
        } else if (errorStatus == ErrorStatus.ERROR_SERVICE) {
            errorMessage = this.getResources().getString(string.service_error);
        } else if (errorStatus == ErrorStatus.ERROT_NET_ERROR) {
            errorMessage = this.getResources().getString(string.network_error);
        } else if (errorStatus == ErrorStatus.ERROR_DATA) {
            errorMessage = this.getResources().getString(string.error_data);
        }
        this.toastMessage(errorMessage);
    }

    /**
     * 提示消息
     *
     * @param message
     */
    public void toastMessage(String message) {
        //若自定义toast不为空，则采用自定义显示内容
        if (mIToast != null) {
            mIToast.toastMessage(message);
            return;
        }
        CommonUtils.toastMessage(this.mContext, message);
    }

    /**
     * 绑定加载视图
     *
     * @param loadingHelper 加载布局
     * @param view          需要绑定的view
     * @param tag           绑定对应的tag，可以通过tag找到该view
     * @param <T>
     */
    protected <T extends BaLoadingViewHelper> void bindLoadingView(T loadingHelper, View view, int tag) {
        loadingHelper.bindView(view);
        this.mLoadingViews.put(tag, loadingHelper);
    }

    /**
     * 更改绑定view的加载状态
     *
     * @param status 当前的状态
     * @param tag    对应绑定的tag
     */
    public void changeLoadingStatus(LoadingStatus status, int tag) {
        BaLoadingViewHelper loadingViewHelper = this.mLoadingViews.get(tag);
        if (loadingViewHelper != null) {
            loadingViewHelper.setStatus(status);
        }
    }

    /***
     * 修改刷新状态
     *
     * @param refreshStatus 刷新的状态
     * @param errorStatus   错误信息
     */
    public void refreshStatus(RefreshStatus refreshStatus, @Nullable ErrorStatus errorStatus) {
        if (mIRefreshView != null) {
            mIRefreshView.setRefreshStatus(refreshStatus);
        }
        if (refreshStatus == RefreshStatus.Failure) {
            if (errorStatus != null)
                this.toastError(errorStatus);
        }
    }


    /**
     * 显示等待对话框
     *
     * @param message                  显示等待对话框对应的标题
     * @param dialogForceCloseListener 对话框强行关闭监听
     */
    public void showWaitDialog(String message, DialogForceCloseListener dialogForceCloseListener) {
        showWaitDialog(false, message, dialogForceCloseListener, true);
    }


    public void showWaitDialog(boolean showClose, String message, ProgressDialog.DialogForceCloseListener dialogForceCloseListener) {

        showWaitDialog(showClose, message, dialogForceCloseListener, true);
    }

    public void showWaitDialog(boolean showClose, String message, ProgressDialog.DialogForceCloseListener dialogForceCloseListener, boolean showIfNotShow) {

        //若绑定自定义加载对话框，则采用绑定的
        if (mIWaitDialog != null) {
            mIWaitDialog.setMessage(message);
            mIWaitDialog.setCloseListener(dialogForceCloseListener);
            mIWaitDialog.showClose(showClose);
            mIWaitDialog.showWait();
            return;
        }

        if (this.mWaitDialog == null) {
            this.mWaitDialog = new ProgressDialog(this);
        }

        if (!this.mWaitDialog.isShowing() && showIfNotShow) {
            this.mWaitDialog.setMessage(message);
            this.mWaitDialog.showClose(showClose);
            this.mWaitDialog.setDialogForceCloseListener(dialogForceCloseListener);
            this.mWaitDialog.show();
        } else {
            this.mWaitDialog.setMessage(message);
        }
    }


    /**
     * 关闭等待对话框
     */
    public void dismissWaitDialog() {
        //若绑定自定义加载对话框，则采用绑定的
        if (mIWaitDialog != null) {
            mIWaitDialog.dismissWait();
            return;
        }
        if (mWaitDialog != null)
            this.mWaitDialog.dismiss();
    }

    /**
     * 关闭等待对话框
     */
    public void dismissWaitDialogWithoutAnim() {
        //若绑定自定义加载对话框，则采用绑定的
        if (mIWaitDialog != null) {
            mIWaitDialog.dismissWithoutAnim();
            return;
        }
        if (mWaitDialog != null)
            this.mWaitDialog.dismissWithoutAnim();
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
        }
        return true;
    }

}
