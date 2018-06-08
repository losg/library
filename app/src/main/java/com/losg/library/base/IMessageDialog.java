package com.losg.library.base;

import com.losg.library.base.interfaces.IMessageDialogButtonClick;

/**
 * Created by losg on 2016/11/23.
 * 自定义等待对话框接口
 */
public interface IMessageDialog {

    /**
     * 设置标题
     * @param title
     */
    void setTitle(String title);

    /**
     *  设置显示的文字
     * @param message
     */
    void setMessage(String message);

    /**
     * 设置 按钮文字
     * @param okRight
     * @param cancelLeft
     */
    void setButtonTitle(String okRight, String cancelLeft);

    /**
     * 确定 按钮点击
     */
    void setOkButtonClick(IMessageDialogButtonClick iMessageDialogButtonClick);

    /**
     * 取消 按钮点击
     */
    void setCancelButtonClick(IMessageDialogButtonClick iMessageDialogButtonClick);

    /**
     * 显示
     */
    void showDialog();

    /**
     * 销毁
     */
    void dismissDialog();

}
