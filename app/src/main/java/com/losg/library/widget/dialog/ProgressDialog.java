package com.losg.library.widget.dialog;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.losg.library.R;


/**
 * Created by losg on 2016/8/31.
 */

public class ProgressDialog extends BaAnimDialog implements View.OnClickListener {

    private TextView                 mMessage;
    private DialogForceCloseListener mDialogForceCloseListener;
    private View                     mCloseLine;
    private View                     mCloseView;

    public ProgressDialog(Context context) {
        super(context, R.style.ProgressDialog);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView() {
        mMessage = (TextView) findViewById(R.id.message);
        findViewById(R.id.close).setOnClickListener(this);
        mCloseLine = findViewById(R.id.close_line);
        mCloseView = findViewById(R.id.close);
    }

    @Override
    protected int initLayout() {
        return R.layout.base_progress_dialog;
    }

    public void setMessage(String message) {
        mMessage.setText(message);
    }

    public void showClose(boolean show) {
        mCloseLine.setVisibility(show ? View.VISIBLE : View.GONE);
        mCloseView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onClick(View v) {
        if (mDialogForceCloseListener != null) {
            mDialogForceCloseListener.forceClose();
        }
        dismiss();
    }

    public void setDialogForceCloseListener(DialogForceCloseListener dialogForceCloseListener) {
        mDialogForceCloseListener = dialogForceCloseListener;
    }

    //用户强制关闭事件
    public interface DialogForceCloseListener {

        void forceClose();
    }
}
