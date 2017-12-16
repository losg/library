package com.losg.library.okhttp;


import com.losg.library.utils.JsonUtils;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import retrofit2.Converter;

/**
 * Created by losg on 2016/6/7.
 */
public class JsonRequestBodyConverter<T> implements Converter<T, RequestBody> {
    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");

    @Override
    public RequestBody convert(T value) throws IOException {
        Buffer buffer = new Buffer();
        String jsonString = JsonUtils.toJson(value);
        return RequestBody.create(MEDIA_TYPE, jsonString.getBytes());
    }
}