package com.losg.library.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.losg.library.R;

/**
 * Created by losg
 */

public class TransStatusBar extends View {

    private int mStartsBarHeight;
    private int mMinSdk;
    public TransStatusBar(Context context) {
        this(context, null);
    }
    public TransStatusBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public TransStatusBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        int resourceId = context.getResources()
                .getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            mStartsBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        }
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TransStatusBar);
        mMinSdk = (int) typedArray.getDimension(R.styleable.TransStatusBar_min_sdk, 21);
        typedArray.recycle();
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (Build.VERSION.SDK_INT >= mMinSdk) {
            setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), mStartsBarHeight);
        } else {
            setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), 0);
        }
    }
}