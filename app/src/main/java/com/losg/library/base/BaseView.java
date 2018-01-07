package com.losg.library.base;

import android.support.annotation.Nullable;

import com.losg.library.widget.dialog.ProgressDialog;
import com.losg.library.widget.loading.BaLoadingView;


/**
 * Created by losg on 2016/6/14.
 */
public interface BaseView {

    //提示信息
    void toastMessage(String message);

    //错误提示
    void toastError(ErrorStatus errorStatus);

    //等待对话框
    void showWaitDialog(String message, ProgressDialog.DialogForceCloseListener dialogForceCloseListener);

    //等待是否有关闭按钮对话框
    void showWaitDialog(boolean showClose, String message, ProgressDialog.DialogForceCloseListener dialogForceCloseListener);

    //关闭信息对话框
    void dismissWaitDialog();

    //关闭信息对话框(无动画)
    void dismissWaitDialogWithoutAnim();
    
    //更改加载的状态
    void changeLoadingStatus(BaLoadingView.LoadingStatus status, int tag);

    //下拉刷新、上拉加载状态更改
    void refreshStatus(RefreshStatus refreshStatus, @Nullable ErrorStatus errorStatus);

    //refreshLayout 状态
    enum RefreshStatus {
        REFRESH_SUCCESS,        //刷新成功
        LOADING_SUCCESS,        //加载成功
        LOADING_ALL,            //全部记在完成
        Failure                 //刷新或加载失败
    }

    //加载时错误信息
    enum ErrorStatus {
        ERROR_DATA,              //数据解析异常
        ERROR_SERVICE,              //服务器繁忙
        ERROR_CONNECT_TIME_OUT,     //连接超时
        ERROT_NET_ERROR             //当前网络未连接
    }

}
