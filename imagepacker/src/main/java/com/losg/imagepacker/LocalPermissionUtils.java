package com.losg.imagepacker;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

/**
 * Created by losg on 2016/5/30.
 */
public class LocalPermissionUtils {

    private Context mContext;
    private String  mPermission;
    private boolean isMSDK                = false;
    private boolean isToRequestPermission = false;
    private PermissionListener permissionListener;
    private boolean isMust = false;

    public LocalPermissionUtils(Context mContext) {
        this.mContext = mContext;
        if (Build.VERSION.SDK_INT >= 23) {
            isMSDK = true;
        }
    }

    public LocalPermissionUtils setMust(boolean must) {
        isMust = must;
        return this;
    }

    public String getmPermission() {
        return mPermission;
    }

    //获取权限
    public boolean permissionCheck(final String permission) {
        if (!isMSDK) {
            if (permissionListener != null) {
                permissionListener.permissionSuccess();
            }
            return true;
        }
        mPermission = permission;
        if (ContextCompat.checkSelfPermission(mContext, permission) != PackageManager.PERMISSION_GRANTED) {
            //没有获取权限
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, Manifest.permission.ACCESS_FINE_LOCATION)) {
                //需要提示用户
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("提醒");
                builder.setMessage("部分功能需要您的授权，否则影响使用");
                builder.setCancelable(false);
                builder.setPositiveButton("知道了", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //请求获取权限
                        ActivityCompat.requestPermissions((Activity) mContext, new String[]{permission}, 1000);
                        dialog.dismiss();
                    }
                });
                builder.show();
                return false;
            }
            //请求获取权限
            ActivityCompat.requestPermissions((Activity) mContext, new String[]{permission}, 1000);
            return false;
        }

        if (permissionListener != null) {
            permissionListener.permissionSuccess();
        }
        return true;
    }

    public void onResume() {
        if (!isMSDK) return;
        //去请求权限
        if (isToRequestPermission) {
            checkUserPermission();
        }
        isToRequestPermission = false;
    }

    private void checkUserPermission() {
        if (permissionListener == null) return;
        if (ContextCompat.checkSelfPermission(mContext, mPermission) != PackageManager.PERMISSION_GRANTED) {
            permissionListener.permissionFailure();
        } else {
            permissionListener.permissionSuccess();
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("isMust", isMust);
        outState.putString("mPermission", mPermission);
        outState.putBoolean("isToRequestPermission", isToRequestPermission);
    }

    public void onReBackState(Bundle saveState) {
        if (saveState == null) return;
        isMust = saveState.getBoolean("isMust");
        isToRequestPermission = saveState.getBoolean("isToRequestPermission");
        mPermission = saveState.getString("mPermission");
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (!isMSDK) return;
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (permissionListener != null) {
                permissionListener.permissionSuccess();
            }
            return;
        }
        //授权失败
        permissionDialog();
    }

    //设置权限提示框
    private void permissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("提醒");
        builder.setMessage("您拒绝权限申请。\n请点击\"设置\" -\"权限\" - 打开所需的权限。 ");
        builder.setCancelable(false);
        builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                startSetting();
            }
        });

        String negativeString = isMust ? "退出应用" : "关闭";

        builder.setNegativeButton(negativeString, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (permissionListener != null) {
                    permissionListener.permissionFailure();
                }
            }
        });

        builder.show();
    }

    //打开应用的设置
    private void startSetting() {
        isToRequestPermission = true;
        try {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    .setData(Uri.parse("package:" + mContext.getPackageName()));
            mContext.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            Intent intent = new Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
            mContext.startActivity(intent);
        }
    }

    public void setPermissionListener(PermissionListener permissionListener) {
        this.permissionListener = permissionListener;
    }

    public interface PermissionListener {

        //授权成功
        void permissionSuccess();

        //授权失败
        void permissionFailure();
    }
}
