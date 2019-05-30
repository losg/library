package com.losg.library.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Process;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;

import com.losg.library.base.IMessageDialog;
import com.losg.library.base.interfaces.IMessageDialogButtonClick;
import com.losg.library.widget.DefaultPermissionDialog;

import java.io.File;

/**
 * @author losg
 * create time 2019/05/07
 */
public class AndroidInstaller {

    //跳转到应用安装权限设置界面的requestCode
    private static final int REQUEST_FOR_SETTING = 10011;
    //获取安装权限的requestCode
    private static final int PERMISSION_FOR_INSTALL = 10010;
    //获取安装权限
    private static String PERMISSION_INSTALL = "android.permission.REQUEST_INSTALL_PACKAGES";

    private Context mContext;
    //apk所在目录
    private String mApkPath;

    //提示用户信息
    private IMessageDialog mIMessageDialog;

    //在请求权限前提示用户信息
    private boolean mShowMsgBeforePermission = false;

    //要求必须获取到权限，否则退出APP(强制)
    private boolean mMustPermission = false;


    /**
     * 提示用户信息
     *
     * @param messageDialog
     */
    public void setMessageDialog(IMessageDialog messageDialog) {
        mIMessageDialog = messageDialog;
    }

    /**
     * 在请求权限前提示用户信息
     */
    public void showMsgBeforePermission() {
        mShowMsgBeforePermission = true;
    }

    /**
     * 必须要获取所有权限。否则退出APP
     */
    public void mustAllPermission() {
        mMustPermission = true;
    }


    /**
     * Android 8.0应用安装权限
     *
     * @param context
     * @param apkPath
     */
    public void installApk(Context context, String apkPath) {
        installApk(context, apkPath, false);
    }

    private void installApk(Context context, String apk, boolean isCallBack) {
        this.mContext = context;
        this.mApkPath = apk;
        if (Build.VERSION.SDK_INT >= 26) {
            boolean canInstall = context.getPackageManager().canRequestPackageInstalls();
            //已经获取到权限,直接进行安装
            if (canInstall) {
                installApp(context, apk);
                return;
            }
            //获取权限失败
            if (isCallBack) {
                permissionFailure(false);
                return;
            }

            //获取权限请求
            if (mShowMsgBeforePermission) {
                if(mIMessageDialog == null){
                    mIMessageDialog = new DefaultPermissionDialog(mContext);
                }
                mIMessageDialog.setButtonTitle("知道了", "");
                mIMessageDialog.setTitle("提醒");
                mIMessageDialog.setMessage("APP更新需要获取安装权限");
                mIMessageDialog.setOkButtonClick(new IMessageDialogButtonClick() {
                    @Override
                    public void messageDialogClick(IMessageDialog iMessageDialog) {
                        iMessageDialog.dismissDialog();
                        ActivityCompat.requestPermissions((Activity) mContext, new String[]{PERMISSION_INSTALL}, PERMISSION_FOR_INSTALL);
                    }
                });
                mIMessageDialog.showDialog();
                return;
            }
            ActivityCompat.requestPermissions((Activity) context, new String[]{PERMISSION_INSTALL}, PERMISSION_FOR_INSTALL);
            return;
        }
        installApp(context, apk);
    }

    /**
     * 获取应用安装权限返回的回调方法
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     * @return
     */
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode != PERMISSION_FOR_INSTALL) {
            return;
        }
        if (permissions.length != 0 && permissions[0].equals(PERMISSION_INSTALL) && Build.VERSION.SDK_INT >= 26) {
            //获取到了安装权限,直接进行安装
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                installApp(this.mContext, this.mApkPath);
            } else {
                permissionFailure(true);
            }
        }
    }

    /**
     * 调起系统进行安装
     */
    private void permissionSuccess() {
        installApp(this.mContext, this.mApkPath);
    }

    private void permissionFailure(boolean firstPermission) {
        //获取权限失败,提醒用户设置安装权限
        if (firstPermission && Build.VERSION.SDK_INT >= 26) {
            //第一次用户授权取消,提醒用户手动添加安装权限
            Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
            if (mContext instanceof Activity)
                ((Activity) this.mContext).startActivityForResult(intent, REQUEST_FOR_SETTING);
            return;
        }

        if (mIMessageDialog == null) return;
        //权限获取失败,进行其他的操作,关闭APP OR 直接提示失败不进行安装
        mIMessageDialog.setButtonTitle("获取权限", mMustPermission ? "退出应用" : "暂不获取");
        mIMessageDialog.setTitle("提醒");
        mIMessageDialog.setMessage("应用获取安装权限失败,为了更好的用户体验,请开启安装权限");
        mIMessageDialog.setOkButtonClick(new IMessageDialogButtonClick() {
            @Override
            public void messageDialogClick(IMessageDialog iMessageDialog) {
                iMessageDialog.dismissDialog();
            }
        });

        mIMessageDialog.setCancelButtonClick(new IMessageDialogButtonClick() {
            @Override
            public void messageDialogClick(IMessageDialog iMessageDialog) {
                iMessageDialog.dismissDialog();
                if (mMustPermission && mContext instanceof AppCompatActivity) {
                    ((AppCompatActivity) mContext).finish();
                    //延迟退出APP,直接强杀会给用户感觉应用崩溃了
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Process.killProcess(Process.myPid());
                        }
                    }, 200);
                }
            }
        });
        mIMessageDialog.showDialog();
    }


    /**
     * 跳转到应用安装权限设置界面返回,返回的结果,继续检测是否有安装权限
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_FOR_SETTING) {
            this.installApk(this.mContext, this.mApkPath, true);
        }
    }


    /**
     * 安装应用
     *
     * @param context
     * @param apk
     */
    public void installApp(Context context, String apk) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        File file = new File(apk);

        if (!file.exists()) {
            return;
        }

        Uri apkUri = null;
        //7.0以上申请临时访问权限
        if (Build.VERSION.SDK_INT >= 24) {
            apkUri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileProvider", file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            apkUri = Uri.fromFile(file);
        }
        if (apkUri != null) {
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
            context.startActivity(intent);
        }
    }

}
