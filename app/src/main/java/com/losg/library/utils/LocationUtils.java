package com.losg.library.utils;


public class LocationUtils {
    public static int getDistance(double lonA, double latA, double lonB, double latB) {
        // 东西经，南北纬处理，只在国内可以不处理(假设都是北半球，南半球只有澳洲具有应用意义)
        double MLonA = lonA;
        double MLatA = latA;
        double MLonB = lonB;
        double MLatB = latB;
        // 地球半径（千米）
        double R = 6371.004;
        double C = Math.sin(rad(latA)) * Math.sin(rad(latB)) + Math.cos(rad(latA)) * Math.cos(rad(latB)) * Math.cos(rad(MLonA - MLonB));
        return (int) (R * Math.acos(C));
    }

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }
}
