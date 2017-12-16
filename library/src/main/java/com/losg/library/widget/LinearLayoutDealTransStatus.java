package com.losg.library.widget;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.WindowInsets;
import android.widget.LinearLayout;

/**
 * Created by losg on 2016/12/5.
 */

public class LinearLayoutDealTransStatus extends LinearLayout {

    public LinearLayoutDealTransStatus(Context context) {
        super(context);
    }

    public LinearLayoutDealTransStatus(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LinearLayoutDealTransStatus(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public WindowInsets onApplyWindowInsets(WindowInsets insets) {
        if(Build.VERSION.SDK_INT >= 20){
            return super.onApplyWindowInsets(insets.replaceSystemWindowInsets(0, 0, 0, insets.getSystemWindowInsetBottom()));
        }else{
            return insets;
        }
    }
}
