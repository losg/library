package com.losg.library.utils;

/**
 * Created by losg on 2016/11/23.
 */

public class MathUtils {

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
}
