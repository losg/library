package com.losg.library.widget;

import android.content.Context;

import com.losg.library.base.IMessageDialog;
import com.losg.library.base.interfaces.IMessageDialogButtonClick;
import com.losg.library.widget.dialog.MessageInfoDialog;

/**
 * @author losg
 * create time 2019/05/30
 */
public class DefaultPermissionDialog implements IMessageDialog {

    private MessageInfoDialog         mMessageInfoDialog;
    private IMessageDialogButtonClick mOkButtonClick;
    private IMessageDialogButtonClick mCancelButtonClick;

    public DefaultPermissionDialog(Context context) {
        mMessageInfoDialog = new MessageInfoDialog(context);
        mMessageInfoDialog.setDialogButtonClick(new MessageInfoDialog.DialogButtonClick() {
            @Override
            public void click(MessageInfoDialog dialog) {
                mOkButtonClick.messageDialogClick(DefaultPermissionDialog.this);
            }
        });
        mMessageInfoDialog.setDialogCancelButtonClick(new MessageInfoDialog.DialogCancelButtonClick() {
            @Override
            public void click(MessageInfoDialog dialog) {
                mCancelButtonClick.messageDialogClick(DefaultPermissionDialog.this);
            }
        });
    }


    @Override
    public void setTitle(String title) {
        mMessageInfoDialog.setTitle(title);
    }

    @Override
    public void setMessage(String message) {
        mMessageInfoDialog.setMessage(message);
    }

    @Override
    public void setButtonTitle(String okRight, String cancelLeft) {
        mMessageInfoDialog.setButtonTitle(okRight, cancelLeft);
    }

    @Override
    public void setOkButtonClick(IMessageDialogButtonClick iMessageDialogButtonClick) {
        mOkButtonClick = iMessageDialogButtonClick;
    }

    @Override
    public void setCancelButtonClick(IMessageDialogButtonClick iMessageDialogButtonClick) {
        mCancelButtonClick = iMessageDialogButtonClick;
    }

    @Override
    public void showDialog() {
        mMessageInfoDialog.show();
    }

    @Override
    public void dismissDialog() {
        mMessageInfoDialog.dismiss();
    }
}
