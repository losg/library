package com.losg.library.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.losg.library.base.IMessageDialog;
import com.losg.library.base.interfaces.IMessageDialogButtonClick;
import com.losg.library.widget.DefaultPermissionDialog;

public class PermissionUtils {

    private static final int REQUEST_FOR_PERMISSION = 10012;
    private static final int REQUEST_FOR_SETTING    = 10013;

    private Context            mContext;
    private String[]           mPermissions;
    private boolean            mIsMust     = false;
    private boolean            mHasRequest = false;
    private PermissionListener mPermissionListener;

    private IMessageDialog mIMessageDialog;

    public PermissionUtils(Context context, Bundle savedInstanceState) {
        this.mContext = context;
        if (savedInstanceState == null) return;
        mIsMust = savedInstanceState.getBoolean("mIsMust");
        mPermissions = savedInstanceState.getStringArray("mPermission");
        if (mPermissions != null && mPermissions.length > 0) {
            mHasRequest = true;
        }
    }

    public void setMsgDialog(IMessageDialog iMessageDialog) {
        mIMessageDialog = iMessageDialog;
    }

    public void setMust(boolean must) {
        mIsMust = must;
    }

    //获取权限
    public void requestPermission(final String... permission) {
        //防止内存回收后,再次请求权限
        if (mHasRequest) return;

        //低版本不需要动态权限，直接返回成功后续做其它操作
        if (Build.VERSION.SDK_INT < 23) {
            if (mPermissionListener != null)
                mPermissionListener.permissionSuccess();
            return;
        }
        mPermissions = permission;

        if (!checkUserPermission()) {
            //开启权限说明
            showDialogForPermission();
            return;
        }

        if (mPermissionListener != null)
            mPermissionListener.permissionSuccess();

    }


    private void showDialogForPermission() {
        if (mIMessageDialog == null) {
            mIMessageDialog = new DefaultPermissionDialog(mContext);
        }
        //需要提示用户
        mIMessageDialog.setTitle("提醒");
        mIMessageDialog.setMessage("部分功能需要您的授权，否则影响使用");
        mIMessageDialog.setButtonTitle("知道了", "");
        mIMessageDialog.setOkButtonClick(new IMessageDialogButtonClick() {
            @Override
            public void messageDialogClick(IMessageDialog iMessageDialog) {
                iMessageDialog.dismissDialog();
                //请求获取权限
                ActivityCompat.requestPermissions((Activity) mContext, mPermissions, REQUEST_FOR_PERMISSION);
            }
        });
        mIMessageDialog.showDialog();
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("mIsMust", mIsMust);
        outState.putStringArray("mPermission", mPermissions);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (Build.VERSION.SDK_INT < 23 || requestCode != REQUEST_FOR_PERMISSION) return;
        if (grantResults.length == 0) return;

        //检查权限是否获取成功
        if (!checkUserPermission()) {
            //授权失败提示用户信息
            permissionDialog();
            return;
        }
        mPermissionListener.permissionSuccess();

    }

    //设置权限提示框
    private void permissionDialog() {
        if (mIMessageDialog == null) {
            mIMessageDialog = new DefaultPermissionDialog(mContext);
        }
        mIMessageDialog.setTitle("提醒");
        mIMessageDialog.setMessage("权限获取失败可能导致应用功能异常,请按照以下操作开启权限。\n请点击\"设置\" -\"权限\" - 打开所需的权限。 ");
        String negativeString = mIsMust ? "退出应用" : "暂不获取";
        mIMessageDialog.setButtonTitle("获取权限", negativeString);
        mIMessageDialog.setOkButtonClick(new IMessageDialogButtonClick() {
            @Override
            public void messageDialogClick(IMessageDialog iMessageDialog) {
                startSetting();
                iMessageDialog.dismissDialog();
            }
        });
        mIMessageDialog.setCancelButtonClick(new IMessageDialogButtonClick() {
            @Override
            public void messageDialogClick(IMessageDialog iMessageDialog) {
                iMessageDialog.dismissDialog();
                if (mPermissionListener != null) {
                    mPermissionListener.permissionFailure();
                }
            }
        });
        mIMessageDialog.showDialog();
    }

    //打开应用的设置
    private void startSetting() {
        try {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(Uri.parse("package:" + mContext.getPackageName()));
            if (mContext instanceof Activity)
                ((Activity) mContext).startActivityForResult(intent, REQUEST_FOR_SETTING);
        } catch (Exception e) {
            trySystemSetting();
        }
    }

    private void trySystemSetting() {
        try {
            Intent intent = new Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
            if (mContext instanceof Activity)
                ((Activity) mContext).startActivityForResult(intent, REQUEST_FOR_SETTING);
        } catch (Exception ex) {
        }
    }

    public void setPermissionListener(PermissionListener permissionListener) {
        this.mPermissionListener = permissionListener;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != REQUEST_FOR_SETTING || Build.VERSION.SDK_INT < 23 || mPermissions == null)
            return;
        //检查权限是否获取成功
        if (!checkUserPermission()) {
            mPermissionListener.permissionFailure();
            return;
        }
        mPermissionListener.permissionSuccess();
    }

    private boolean checkUserPermission() {
        if (mPermissionListener == null) return false;
        for (String permission : mPermissions) {
            if (ContextCompat.checkSelfPermission(mContext, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public interface PermissionListener {

        //授权成功
        void permissionSuccess();

        //授权失败
        void permissionFailure();
    }
}
