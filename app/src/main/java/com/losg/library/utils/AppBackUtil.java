package com.losg.library.utils;

import android.os.Handler;
import android.os.Process;

import com.losg.library.base.BaActivity;

/**
 * Created by Administrator on 2018/2/12.
 */

public class AppBackUtil {

    private boolean mExit = false;
    private BaActivity mActivity;
    private String     mAppName;

    public AppBackUtil(BaActivity activity, String appName) {
        mActivity = activity;
        this.mAppName = appName;
    }

    public void onBackPress() {
        if (mExit) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Process.killProcess(Process.myPid());
                }
            }, 300);
            mActivity.finish();
        } else {
            mExit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mExit = false;
                }
            }, 300);
            mActivity.toastMessage("再按一次退出" + mAppName);
        }
    }

}
