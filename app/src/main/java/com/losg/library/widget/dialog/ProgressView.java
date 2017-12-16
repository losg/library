package com.losg.library.widget.dialog;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.losg.library.R;


/**
 * Created by losg on 2016/8/30.
 */

public class ProgressView extends View implements ValueAnimator.AnimatorUpdateListener {

    private ValueAnimator mProgressAnimator;
    //当前进度
    private float          mCurrentPercent;
    private Paint          mProgressPaint;
    //动画时间
    private int            mAnimationTime;
    //进度条的颜色
    private int            mProgressColor;
    //外框宽度
    private float          mStrokeWidth;

    //增加还是减少标志
    private boolean mFlag = false;

    public ProgressView(Context context) {
        this(context, null);
    }

    public ProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ProgressView);
        mProgressColor = typedArray.getColor(R.styleable.ProgressView_progress_stroke_color, getColorAccent());
        mStrokeWidth = typedArray.getDimension(R.styleable.ProgressView_progress_stroke_size, 6);
        mAnimationTime = typedArray.getInteger(R.styleable.ProgressView_progress_time, 1100);
        typedArray.recycle();
        init();
    }

    private void init() {
        mProgressPaint = new Paint();
        mProgressPaint.setColor(mProgressColor);
        mProgressPaint.setAntiAlias(true);
        mProgressPaint.setStrokeWidth(mStrokeWidth);
        mProgressPaint.setStyle(Paint.Style.STROKE);

        mProgressAnimator = new ValueAnimator();
        mProgressAnimator.setInterpolator(new ProgressInterpolator());
        mProgressAnimator.setDuration(mAnimationTime);
        mProgressAnimator.setRepeatCount(-1);
        mProgressAnimator.setFloatValues(0f, 1f);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mProgressAnimator.removeAllUpdateListeners();
        mProgressAnimator.addUpdateListener(this);
        if(!mProgressAnimator.isRunning()) {
            mProgressAnimator.start();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (mProgressAnimator != null) {
            if (hasWindowFocus && !mProgressAnimator.isRunning()) {
                mProgressAnimator.start();
            } else if (!hasWindowFocus) {
                mProgressAnimator.cancel();
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if(mProgressAnimator != null){
            mProgressAnimator.removeAllUpdateListeners();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        RectF rectF = new RectF(mStrokeWidth, mStrokeWidth, getMeasuredWidth() - mStrokeWidth, getMeasuredHeight() - mStrokeWidth);
        if (mFlag) {
            canvas.drawArc(rectF, 720 * mCurrentPercent, 300 * (1 - mCurrentPercent), false, mProgressPaint);
        } else {
            canvas.drawArc(rectF, 360 * mCurrentPercent, 300 * mCurrentPercent, false, mProgressPaint);
        }
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        if ((float) animation.getAnimatedValue() < mCurrentPercent) {
            mFlag = !mFlag;
        }
        mCurrentPercent = (float) animation.getAnimatedValue();
        invalidate();
    }

    private int getColorAccent() {
        TypedArray typedArray = getContext().obtainStyledAttributes(new int[]{R.attr.colorAccent});
        return typedArray.getColor(0, Color.RED);
    }

}
