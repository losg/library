package com.losg.library.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by losg on 2018/2/26.
 */

public class WrapContentView extends LinearLayout {

    public WrapContentView(Context context) {
        super(context);
        setOrientation(VERTICAL);
    }

    public WrapContentView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
    }

    public WrapContentView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            childAt.measure(widthMeasureSpec, heightMeasureSpec);
            height += childAt.getMeasuredHeight();
        }
        int size = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(size, height);
    }
}
