package com.losg.library.utils;


import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Reader;
import java.lang.reflect.Type;


public class JsonUtils {
    private static final Gson gson = new Gson();

    public static JsonElement toJsonTree(Object src) {
        return gson.toJsonTree(src);
    }

    public static JsonElement toJsonTree(Object src, Type typeOfSrc) {
        return gson.toJsonTree(src, typeOfSrc);
    }

    public static String toJson(Object src) {
        return gson.toJson(src);
    }

    public static JSONObject toJsonObject(Object src) {
        try {
            return new JSONObject(gson.toJson(src));
        } catch (JSONException e) {
            e.printStackTrace();
            return new JSONObject();
        }
    }

    public static String toJson(Object src, Type typeOfSrc) {
        return gson.toJson(src, typeOfSrc);
    }

    public static void toJson(Object src, Appendable writer) throws JsonIOException {
        gson.toJson(src, writer);
    }

    public static void toJson(Object src, Type typeOfSrc, Appendable writer) throws JsonIOException {
        gson.toJson(src, typeOfSrc, writer);
    }

    public static void toJson(Object src, Type typeOfSrc, JsonWriter writer) throws JsonIOException {
        gson.toJson(src, typeOfSrc, writer);
    }

    public static String toJson(JsonElement jsonElement) {
        return gson.toJson(jsonElement);
    }

    public static void toJson(JsonElement jsonElement, Appendable writer) throws JsonIOException {
        gson.toJson(jsonElement, writer);
    }


    public static void toJson(JsonElement jsonElement, JsonWriter writer) throws JsonIOException {
        gson.toJson(jsonElement, writer);
    }

    public static <T> T fromJson(String json, Class<T> classOfT) throws JsonSyntaxException {
        try {
            return gson.fromJson(json, classOfT);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
        return null;
    }

    public static String dealJson(String json) {
        //屏蔽PHP警告信息
        if (!json.contains("{")) {
            json = "{\"code\":500,\"message\":\"数据解析失败\",\"data\":0}";
        } else
            json = json.substring(json.indexOf("{"), json.length());
        //过滤特殊字符
        json = JSONTokener(json);
        return json;
    }

    public static <T> T fromJson(String json, Type typeOfT) throws JsonSyntaxException {
        return gson.fromJson(json, typeOfT);
    }

    public static <T> T fromJson(Reader json, Class<T> classOfT) throws JsonSyntaxException, JsonIOException {
        return gson.fromJson(json, classOfT);
    }

    public static <T> T fromJson(Reader json, Type typeOfT) throws JsonIOException, JsonSyntaxException {
        return gson.fromJson(json, typeOfT);
    }

    public static <T> T fromJson(JsonReader reader, Type typeOfT) throws JsonIOException, JsonSyntaxException {
        return gson.fromJson(reader, typeOfT);
    }

    public static <T> T fromJson(JsonElement json, Class<T> classOfT) throws JsonSyntaxException {
        return gson.fromJson(json, classOfT);
    }

    public static <T> T fromJson(JsonElement json, Type typeOfT) throws JsonSyntaxException {
        return gson.fromJson(json, typeOfT);
    }


    public static String unescape(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        return str.replaceAll("&quot;", "\"").replaceAll("&amp;", "&").replaceAll("&lt;", "<").replace("&gt;", ">")
                .replaceAll("&nbsp;", " ").replaceAll("\\\\\\\\", "/");
    }


    public static String JSONTokener(String in) {
        if (in != null && in.startsWith("\ufeff")) {
            in = in.substring(1);
        }
        return in;
    }

}
