package com.losg.library.widget.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.losg.library.R;


/**
 * Created by come on 2015/12/24.
 */
public class MessageInfoDialog extends BaAnimDialog implements View.OnClickListener {

    private TextView          mDialogMessage;
    private TextView          mActionOk;
    private TextView          mActionCancel;
    private DialogButtonClick mDialogButtonClick;
    private LinearLayout      mBtnLayer;
    private TextView          mTitle;
    private boolean           mAutoClose;

    private DialogCancelButtonClick mDialogCancelButtonClick;

    public MessageInfoDialog(Context context) {
        super(context, R.style.MessageDialog);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        mAutoClose = true;
        initView();
    }

    @Override
    protected int initLayout() {
        return R.layout.base_view_dialog;
    }

    private void initView() {
        mBtnLayer = (LinearLayout) findViewById(R.id.btn_layer);
        LinearLayout rootLayer = (LinearLayout) findViewById(R.id.root_layer);
        mDialogMessage = (TextView) findViewById(R.id.dialog_message);
        mActionOk = (TextView) findViewById(R.id.action_ok);
        mTitle = (TextView) findViewById(R.id.title);
        mActionCancel = (TextView) findViewById(R.id.action_cancel);
        mActionOk.setOnClickListener(this);
        mActionCancel.setOnClickListener(this);

        int width = getContext().getResources().getDisplayMetrics().widthPixels / 100 * 80;
        ViewGroup.LayoutParams layoutParams = rootLayer.getLayoutParams();
        layoutParams.width = width;
        rootLayer.setLayoutParams(layoutParams);
    }

    public void setButtonTitle(String okTitle, String cancelTitle) {
        mActionOk.setText(okTitle);
        mActionCancel.setText(cancelTitle);
        if (TextUtils.isEmpty(cancelTitle)) {
            mBtnLayer.getChildAt(0).setVisibility(View.GONE);
            mBtnLayer.getChildAt(1).setVisibility(View.GONE);
            mBtnLayer.getChildAt(2).setBackgroundResource(R.drawable.sr_dialog_message_click);
        }
    }

    public void setTitle(String mTitle) {
        if (!TextUtils.isEmpty(mTitle)) {
            this.mTitle.setVisibility(View.VISIBLE);
            this.mTitle.setText(mTitle);
        } else {
            this.mTitle.setVisibility(View.GONE);
        }
    }

    public void setDialogButtonClick(DialogButtonClick mDialogButtonClick) {
        this.mDialogButtonClick = mDialogButtonClick;
    }
    
    public void setDialogCancelButtonClick(DialogCancelButtonClick dialogCancelButtonClick) {
        mDialogCancelButtonClick = dialogCancelButtonClick;
    }

    public void setDialogCancelButtonClick(DialogCancelButtonClick dialogCancelButtonClick, boolean autoClose) {
        mDialogCancelButtonClick = dialogCancelButtonClick;
        mAutoClose = autoClose;
    }
    
    public void setMessage(String message) {
        mDialogMessage.setText(message);
    }

    @Override
    public void onClick(View v) {
        if (v == mActionOk) {
            ok();
            return;
        }
        if(mAutoClose)
            dismissMessageDialog();

        if (mDialogCancelButtonClick != null) {
            mDialogCancelButtonClick.click(this);
        }
    }

    public interface DialogButtonClick {
        void click(MessageInfoDialog dialog);
    }

    void dismissMessageDialog() {
        dismiss();
    }

    void ok() {
        if (mDialogButtonClick != null) {
            mDialogButtonClick.click(this);
        }
    }

    public interface DialogCancelButtonClick {

        void click(MessageInfoDialog dialog);
    }
}
