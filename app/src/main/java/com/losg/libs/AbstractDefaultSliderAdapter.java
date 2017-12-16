package com.losg.libs;

import android.content.Context;
import android.view.View;

/**
 * Created by losg on 2017/2/23.
 */

public abstract class AbstractDefaultSliderAdapter {

    protected Context mContext;

    public AbstractDefaultSliderAdapter(Context context){
        mContext = context;
    }

    public abstract int getCount();

    public abstract int viewLayoutReource();

    public abstract void bindView(View view, int position);
}
