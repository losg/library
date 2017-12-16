package com.losg.library.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by come on 2015/12/3.
 */
public class SharedPreferencesUtil {

    private final String SHARED_NAME = "losg";

    private static SharedPreferencesUtil mSharedPreferences;
    private        SharedPreferences     mSp;

    private SharedPreferencesUtil(Context context) {
        mSp = context.getSharedPreferences(SHARED_NAME, Context.MODE_PRIVATE);
    }

    public static SharedPreferencesUtil getInstance(Context context) {
        if (mSharedPreferences == null) {
            synchronized (SharedPreferencesUtil.class) {
                if (mSharedPreferences == null) {
                    mSharedPreferences = new SharedPreferencesUtil(context);
                }
            }
        }
        return mSharedPreferences;
    }

    //String
    public void putString(String key, String value) {
        SharedPreferences.Editor edit = mSp.edit();
        edit.putString(key, value);
        edit.commit();
    }

    public String getString(String key) {
        return mSp.getString(key, "");
    }

    public String getString(String key, String defaultValue) {
        return mSp.getString(key, defaultValue);
    }

    //boolean
    public void putBoolean(String key, boolean value) {
        SharedPreferences.Editor edit = mSp.edit();
        edit.putBoolean(key, value);
        edit.commit();
    }

    public boolean getBoolean(String key) {
        return mSp.getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return mSp.getBoolean(key, defaultValue);
    }

    //integer
    public void putInteger(String key, int value) {
        SharedPreferences.Editor edit = mSp.edit();
        edit.putInt(key, value);
        edit.commit();
    }

    public int getInteger(String key) {
        return mSp.getInt(key, 0);
    }

    public int getInteger(String key, int defaultVaule) {
        return mSp.getInt(key, defaultVaule);
    }

}
