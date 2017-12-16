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

    private boolean mIsClosing = false;
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

    @Override
    public void show() {
        super.show();
        mIsClosing = false;
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(mRootView, "scaleX", 0.5f, 1f, 0.98f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(mRootView, "scaleY", 0.5f, 1f, 0.98f, 1f);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(mRootView, "alpha", 0.5f, 1f, 0.98f, 1f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleX, scaleY, alpha);
        animatorSet.setDuration(500);
        animatorSet.setInterpolator(new LinearInterpolator());
        animatorSet.start();
    }

    @Override
    public void dismiss() {
        if (mIsClosing) return;
        mIsClosing = true;
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(mRootView, "scaleX", 1f, 0.5f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(mRootView, "scaleY", 1f, 0.5f);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(mRootView, "alpha", 0.8f, 0f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleX, scaleY, alpha);
        animatorSet.setDuration(200);
        animatorSet.start();
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                BaAnimDialog.super.dismiss();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

}
