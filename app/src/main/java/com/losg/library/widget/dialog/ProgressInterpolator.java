package com.losg.library.widget.dialog;

import android.view.animation.Interpolator;

/**
 * Created by losg on 2016/8/31.
 *  加速动画，tan 不会出现 sin cos 到末尾和初期 卡一会的现象
 */

public class ProgressInterpolator implements Interpolator {

    @Override
    public float getInterpolation(float input) {
       return  (float) Math.tan(input * Math.PI / 4);
    }
}
