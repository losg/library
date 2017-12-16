package com.losg.library.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.losg.library.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by come on 2015/11/23.
 */
public class CommonUtils {

    private static Toast sToast;

    public static int getActionBarSize(Context context) {
        int attr[] = {R.attr.actionBarSize};
        TypedArray typedArray = context.obtainStyledAttributes(attr);
        int actionBarSize = (int) typedArray.getDimension(0, 0);
        typedArray.recycle();
        return actionBarSize;
    }

    public static void closeInputStream(InputStream inputStream) {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                inputStream = null;
            }
        }
    }

    public static void closeOutputStream(OutputStream OnputStream) {
        if (OnputStream != null) {
            try {
                OnputStream.close();
            } catch (IOException e) {
                OnputStream = null;
            }
        }
    }

    public static String convertStreamToString(InputStream is) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int i = 0;
        try {
            while ((i = is.read()) != -1) {
                byteArrayOutputStream.write(i);
            }
        } catch (Exception e) {
        } finally {
            CommonUtils.closeInputStream(is);
        }
        return byteArrayOutputStream.toString();
    }

    public static void toastMessage(Context context, String message) {
        if (sToast == null) {
            sToast = new Toast(context);
        }
        TextView textView = new TextView(context);
        textView.setText(message);
        textView.setTextSize(14);
        textView.setTextColor(Color.WHITE);
        textView.setBackgroundResource(R.drawable.bg_toast);
        sToast.setView(textView);
        sToast.setDuration(Toast.LENGTH_LONG);
        sToast.setGravity(Gravity.CENTER, 0, 0);
        sToast.show();
    }

    public static String convertPrice(String price) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        if (TextUtils.isEmpty(price)) {
            return "";
        }
        return "¥" + decimalFormat.format(CommonUtils.stringToDouble(price));
    }

    public static boolean makePathExist(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return file.mkdirs();
        }
        return true;
    }

    public static boolean makeFileExist(String path) {
        String DirPath = path.substring(0, path.lastIndexOf("/"));
        System.out.println(DirPath);
        File file = new File(DirPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        file = new File(path);
        if (!file.exists()) {
            try {
                return file.createNewFile();
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    public static boolean tabbleIsExist(SQLiteDatabase db, String tableName) {
        boolean result = false;
        if (tableName == null) {
            return false;
        }
        Cursor cursor = null;
        try {
            String sql = "SELECT COUNT(*) FROM sqlite_master where type='table' and name='" + tableName + "'";
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToNext()) {
                int count = cursor.getInt(0);
                if (count > 0) {
                    result = true;
                }
            }
        } catch (Exception e) {
        }
        return result;
    }

    public static String dotLeftOne(String price) {
        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        String format = decimalFormat.format(CommonUtils.stringToDouble(price));
        return format;
    }

    public static String dotLeftTwo(String price) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        String format = decimalFormat.format(CommonUtils.stringToDouble(price));
        return format;
    }

    public static float stringToFloat(String number) {
        try {
            return Float.parseFloat(number);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static double stringToDouble(String number) {
        try {
            return Double.parseDouble(number);
        } catch (Exception e) {
        }
        return 0;
    }

    public static int stringToInteger(String number) {
        try {
            return Integer.parseInt(number);
        } catch (Exception e) {
        }
        return 0;
    }

    public static long getTime(String time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date parse = simpleDateFormat.parse(time);
            return parse.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return System.currentTimeMillis();
    }


    public static String getAppVersionName(Context context) {
        String appVersionName = "";
        PackageManager manager = context.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            appVersionName = info.versionName; // 版本名
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appVersionName;
    }

    public static String FormetFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }

}
