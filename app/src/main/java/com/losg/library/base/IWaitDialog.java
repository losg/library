package com.losg.library.base;

import com.losg.library.widget.dialog.ProgressDialog;

/**
 * Created by losg on 2016/11/23.
 * 自定义等待对话框接口
 */
public interface IWaitDialog {

    /**
     *  设置显示的文字
     * @param message
     */
    void setMessage(String message);

    /***
     *设置是否显示关闭
     */
    void showClose(boolean showClose);

    /**
     * 设置关闭事件
     * @param dialogForceCloseListener
     */
    void setCloseListener(ProgressDialog.DialogForceCloseListener dialogForceCloseListener);

    /**
     * 显示对话框
     */
    void showWait();

    /**
     * 关闭对话框
     */
    void dismissWait();
    
    /**
     * 无动画关闭dialog
     */
    void dismissWithoutAnim();
}
