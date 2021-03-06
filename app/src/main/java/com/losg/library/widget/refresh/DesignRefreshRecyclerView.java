package com.losg.library.widget.refresh;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import com.losg.library.R;
import com.losg.library.base.BaseView;
import com.losg.library.base.IRefreshView;


/**
 * Created by losg on 2017/5/27.
 */

public class DesignRefreshRecyclerView extends RefreshRecyclerView implements IRefreshView {

    private boolean mCanRefresh = true;
    private RecyclerView.OnScrollListener mOnScrollListener;
    private float                         mDownY;
    private float                         mMinMove;

    public DesignRefreshRecyclerView(Context context) {
        this(context, null);
    }

    public DesignRefreshRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        int padding = 0;
        TypedArray ty = context.obtainStyledAttributes(attrs, R.styleable.DesignRefreshRecyclerView);
        padding = ty.getDimensionPixelSize(R.styleable.DesignRefreshRecyclerView_padding_top, 0);
        ty.recycle();
        mRecycler.setPadding(0, padding, 0, 0);
        mRecycler.setClipToPadding(false);
        mRecycler.setClipChildren(false);
        init();
    }

    @Override
    public void setAdapter(RecyclerView.Adapter adapter) {
        super.setAdapter(adapter);
    }

    private void init() {
        setFooterView(new DesignBookRefreshFooter(getContext()));
        mMinMove = new ViewConfiguration().getScaledTouchSlop();
    }

    @Override
    public void setRefreshStatus(BaseView.RefreshStatus refreshStatus) {
        if (refreshStatus == BaseView.RefreshStatus.REFRESH_SUCCESS || refreshStatus == BaseView.RefreshStatus.LOADING_SUCCESS) {
            setRefreshSuccess();
        } else if (refreshStatus == BaseView.RefreshStatus.Failure) {
            setRefreshSuccess();
            setLoadingError("加载失败,点击重新加载");
        } else if (refreshStatus == BaseView.RefreshStatus.LOADING_ALL) {
            setLoadingAll(true);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                RecyclerView.LayoutManager layoutManager = mRecycler.getLayoutManager();
                int lastCompletelyVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition();
                float dy = ev.getY() - mDownY;
                //已经是末尾
                if(dy < 0 && Math.abs(dy) > mMinMove * 2 && lastCompletelyVisibleItemPosition == mRecycler.getAdapter().getItemCount() - 1){
                    if (mCanLoading && mLoadingStatus == LoadingStatus.NORMAL) {
                        mFooterView.loadding();
                        mLoadingStatus = LoadingStatus.LOADING;
                        if (mRefreshListener != null) {
                            mRefreshListener.onLoading();
                        }
                    }
                }
                break;
        }

        if (mCanRefresh)
            return super.dispatchTouchEvent(ev);
        else {
            setEnabled(false);
            getChildAt(0).dispatchTouchEvent(ev);
        }
        return true;
    }

    public void setCanRefresh(boolean canRefresh) {
        mCanRefresh = canRefresh;
    }

    public void setOnScrollChangeListener(RecyclerView.OnScrollListener onScrollChangeListener) {
        mRecycler.addOnScrollListener(onScrollChangeListener);
        mOnScrollListener = onScrollChangeListener;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mRecycler.removeOnScrollListener(mOnScrollListener);
    }
}
