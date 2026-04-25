package com.losg.library.widget.dialog;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by losg on 2016/8/31.
 */

public abstract class BaAnimDialog extends Dialog {

    private View    mRootView  = null;

    public BaAnimDialog(Context context) {
        super(context);
        init(context);
    }

    public BaAnimDialog(Context context, int style) {
        super(context, style);
        init(context);
    }

    private void init(Context context) {
        int layout = initLayout();
        mRootView = View.inflate(context, layout, null);
        setContentView(mRootView);
    }

    protected abstract int initLayout();

    public void dismissWithoutAnim(){
        super.dismiss();
    }
}
