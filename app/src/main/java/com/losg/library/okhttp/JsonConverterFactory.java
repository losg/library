package com.losg.library.okhttp;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;


/**
 * Created by losg on 2016/6/7.
 */
public class JsonConverterFactory extends Converter.Factory {

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        Class clazz = null;
        try {
            clazz = Class.forName(type.toString().substring(type.toString().indexOf(" ") + 1, type.toString().length()));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return new JsonResponseBodyConverter<>(clazz);
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        return new JsonRequestBodyConverter<>();
    }
}
