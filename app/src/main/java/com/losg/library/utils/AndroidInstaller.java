package com.losg.library.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;

/**
 * Created by losg on 2018/4/13.
 */

public class AndroidInstaller {

    private static final int REQUET_FOR_INSTALL = 10010;
    private static final int REQUET_FOR_SETTING = 10012;

    private Context mContext;
    private String  mApkPath;
    private boolean mReuestForInstall;

    public void intallApk(Context context, String apk) {
        mContext = context;
        mApkPath = apk;
        if (Build.VERSION.SDK_INT >= 26) {
            boolean canInstall = context.getPackageManager().canRequestPackageInstalls();
            if (canInstall) {
                AppUtils.installApp(context, apk);
            } else {
                mReuestForInstall = true;
                //请求安装未知应用来源的权限
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.REQUEST_INSTALL_PACKAGES}, REQUET_FOR_INSTALL);
            }
        } else {
            AppUtils.installApp(context, apk);
        }

    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        boolean requestForInstall = mReuestForInstall;
        mReuestForInstall = false;
        if (!requestForInstall || grantResults == null || grantResults.length == 0) return;
        switch (requestCode) {
            case 10010:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    AppUtils.installApp(mContext, mApkPath);
                } else {
                    //跳转允许安装APP
                    Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
                    ((Activity) mContext).startActivityForResult(intent, REQUET_FOR_SETTING);
                }
                break;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUET_FOR_SETTING:
                intallApk(mContext, mApkPath);
                break;
        }
    }
}
