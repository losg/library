package com.losg.library.utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by losg on 2016/5/16.
 */
public class JsonParse {

    public static <T> T parseJson(String json, Class<T> clazz) throws Exception {
        if (clazz == null) return null;
        JSONObject jsonObject = new JSONObject(json);
        T t = clazz.newInstance();
        jsonParse(t, jsonObject);
        return t;
    }

    //回调遍历所有属性及元素为对象的属性，赋值并初始化(防止空指针)
    private static void jsonParse(Object t, JSONObject jsonObject) throws Exception {
        List<Field> declaredFields = new ArrayList<>();
        getAllFields(declaredFields, t.getClass());
        for (Field field : declaredFields) {
            field.setAccessible(true);
            String name = field.getName();
            //字符串
            if (field.getType().getName().equals(String.class.getName())) {
                String stringValue = "";
                try {
                    stringValue = jsonObject.getString(name);
                    if(stringValue.equals("null") || stringValue.equals("NULL")){
                        stringValue = "";
                    }
                } catch (Exception e) {
                }
                field.set(t, stringValue);
                continue;
            }
            //整型
            if (field.getType().getName().equals(int.class.getName())) {
                int stringValue = 0;
                try {
                    stringValue = jsonObject.getInt(name);
                } catch (Exception e) {
                }
                field.set(t, stringValue);
                continue;
            }
            //boolean
            if (field.getType().getName().equals(boolean.class.getName())) {
                boolean stringValue = false;
                try {
                    stringValue = jsonObject.getBoolean(name);
                } catch (Exception e) {
                }
                field.set(t, stringValue);
                continue;
            }
            //LIST
            if (field.getType().getName().equals(List.class.getName())) {
                field.set(t, new ArrayList<>());
                String typeName = field.getGenericType().toString();
                Class<?> typeClazz = Class.forName(typeName.substring(typeName.indexOf("<") + 1, typeName.indexOf(">")));
                List o = (List) field.get(t);
                //字符串或者数字直接添加
                if(typeClazz.getName().equals(String.class.getName()) || typeClazz.getName().equals(int.class.getName()) || typeClazz.getName().equals(boolean.class.getName())){
                    try{
                        List o1 = (List) field.get(t);
                        JSONArray jsonArray = jsonObject.getJSONArray(name);
                        for(int i = 0 ; i < jsonArray.length(); i++){
                            o1.add(jsonArray.get(i));
                        }
                    }catch (Exception e){}
                    //对象
                }else{
                    try{
                        for(int i = 0; i < jsonObject.getJSONArray(name).length(); i++){
                            Object o1 = typeClazz.newInstance();
                            jsonParse(o1, jsonObject.getJSONArray(name).getJSONObject(i));
                            o.add(o1);
                        }
                    }catch(Exception e){
                    }
                }
                continue;
            }

            if(field.get(t) == null){
                field.set(t, Class.forName(field.getGenericType().toString().replace("class ","")).newInstance());
            }
            //对象
            try{
                jsonParse(field.get(t), jsonObject.getJSONObject(name));
            }catch(Exception e){
                jsonParse(field.get(t), null);
            }
        }
    }

    //获取所有属性(包括父类)
    private static void getAllFields(List<Field> field, Class clazz){
        if(field == null){
            field = new ArrayList<Field>();
        }

        for(Field clazzField : clazz.getDeclaredFields()){
            field.add(clazzField);
        }

        if(!clazz.getSuperclass().getName().equals(Object.class.getName())){
            getAllFields(field, clazz.getSuperclass());
        }
    }
}
