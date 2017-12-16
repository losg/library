package com.losg.library.okhttp;


import com.losg.library.utils.JsonUtils;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by losg on 2016/6/7.
 */
public class JsonResponseBodyConverter <T> implements Converter<ResponseBody, T> {

    private Class clazz;

    public JsonResponseBodyConverter(Class clazz) {
        this.clazz = clazz;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        return (T) JsonUtils.fromJson(value.string(), clazz);
    }
}
