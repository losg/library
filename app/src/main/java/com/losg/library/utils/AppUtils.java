package com.losg.library.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;
import android.support.v4.content.FileProvider;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.UUID;

public class AppUtils {

    public static boolean isSDCardAvailable(final Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            if (context.getExternalCacheDir() != null) {
                return true;
            }
        }
        return false;
    }

    public static void installApp(Context context, Uri downloadUri) {
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

    /**
     * Check how much usable space is available at a given path.
     *
     * @param path The path to check
     * @return The space available in bytes
     */
    @SuppressWarnings("deprecation")
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public static long getUsableSpace(File path) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            return path.getUsableSpace();
        }
        final StatFs stats = new StatFs(path.getPath());
        return (long) stats.getBlockSize() * (long) stats.getAvailableBlocks();
    }

    public static DisplayMetrics getDisplayMetrics(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics;
    }

    public static void makeCall(Context context, String phoneNumber) {
        if (context == null || TextUtils.isEmpty(phoneNumber)) {
            return;
        }

        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packInfo.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static int getVersionCode(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packInfo.versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return 1;
        }
    }

    public static CharSequence getAppName(Context context) {
        PackageManager packageManager = null;
        ApplicationInfo applicationInfo = null;
        try {
            packageManager = context.getApplicationContext().getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            applicationInfo = null;
        }
        return packageManager.getApplicationLabel(applicationInfo);
    }


    private static boolean isExecutable(String filePath) {
        Process p = null;
        try {
            p = Runtime.getRuntime().exec("ls -l " + filePath);
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String str = in.readLine();
            if (str != null && str.length() >= 4) {
                char flag = str.charAt(3);
                if (flag == 's' || flag == 'x') {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (p != null) {
                p.destroy();
            }
        }
        return false;
    }

    /**
     * 判断手机是否root
     */
    public static boolean isRooted() {
        String binPath = "/system/bin/su";
        String xBinPath = "/system/xbin/su";
        if (new File(binPath).exists() && isExecutable(binPath)) {
            return true;
        }
        if (new File(xBinPath).exists() && isExecutable(xBinPath)) {
            return true;
        }
        return false;
    }

    private static String DEVICE_ID = null;

    public static String getDeviceId(Context context) {
        if (TextUtils.isEmpty(DEVICE_ID) || "00".equals(DEVICE_ID)) {
            try {
                if (TextUtils.isEmpty(DEVICE_ID)) {
                    // 先获取androidid
                    DEVICE_ID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
                    // 在主流厂商生产的设备上，有一个很经常的bug，
                    // 就是每个设备都会产生相同的ANDROID_ID：9774d56d682e549c
                    if (TextUtils.isEmpty(DEVICE_ID) || "9774d56d682e549c".equals(DEVICE_ID)) {
                        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                        DEVICE_ID = telephonyManager.getDeviceId();
                    }
                    if (TextUtils.isEmpty(DEVICE_ID)) {
                        DEVICE_ID = UUID.randomUUID().toString();
                        DEVICE_ID = DEVICE_ID.replaceAll("-", "");
                    }
                }
            } catch (Exception e) {
                // deviceId = "00";
                DEVICE_ID = UUID.randomUUID().toString();
                DEVICE_ID = DEVICE_ID.replaceAll("-", "");
            } finally {
                return DEVICE_ID;
            }
        }
        if (TextUtils.isEmpty(DEVICE_ID)) {
            DEVICE_ID = "00";
        }
        return DEVICE_ID;
    }

    public static String getMacAddress(Context context) {
        if (context == null) {
            return "";
        }
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info.getMacAddress();
    }

    public static Bundle getAppMetaData(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            ApplicationInfo appInfo = pm.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            return appInfo.metaData;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return new Bundle();
    }

    public static Bundle getActivityMetaData(Activity activity) {
        try {
            PackageManager pm = activity.getPackageManager();
            ActivityInfo appInfo = pm.getActivityInfo(activity.getComponentName(), PackageManager.GET_META_DATA);
            return appInfo.metaData;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return new Bundle();
    }

    public static int getScreenMode(Context context) {
        try {
            return Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE);
        } catch (Exception localException) {
            return 0;
        }
    }

    public static int getScreenBrightness(Context context) {
        try {
            return Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        } catch (Exception localException) {
            return 255;
        }
    }

    public static void setScreenMode(Context context, int mode) {
        try {
            Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, mode);
        } catch (Exception localException) {
            localException.printStackTrace();
        }
    }

    public static void saveScreenBrightness(Context context, int brightness) {
        try {
            Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, brightness);
        } catch (Exception localException) {
            localException.printStackTrace();
        }
    }

    public static void setScreenBrightness(Activity activity, int brightness) {
        Window localWindow = activity.getWindow();
        WindowManager.LayoutParams localLayoutParams = localWindow.getAttributes();
        float f = brightness / 255.0F;
        localLayoutParams.screenBrightness = f;
        localWindow.setAttributes(localLayoutParams);
    }

}
