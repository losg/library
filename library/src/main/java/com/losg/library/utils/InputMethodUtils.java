package com.losg.library.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;


public class InputMethodUtils {
    public static void hideInputMethod(Activity activity) {
        InputMethodManager inputMethod = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethod.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static void hideInputMethod(Context context, View view) {
        InputMethodManager inputMethod = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethod.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void showInputMethod(Context context, View view) {
        InputMethodManager inputMethod = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethod.showSoftInput(view, 0);
        inputMethod.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }
}
