package com.xcr.orange.oa.util;

import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * @author zywan
 * @date 2022/4/26 11:26
 * Version : 1.0
 * Description : 代码目的，作用，如何工作
 * Notice : 本代码需要注意事项、备注事项
 */
public class GsonUtil {
    public static final Gson GSON = new GsonBuilder().setObjectToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE).create();

    public static final JsonParser JSON_PARSER = new JsonParser();

    public static String toJson(Object o) {
        return GSON.toJson(o);
    }

    public static <T> T fromJson(String json, Class<T> clz) throws JsonSyntaxException {
        return GSON.fromJson(json, clz);
    }

    public static <T> T parseObject(String line, Type typeOfT) throws JsonSyntaxException {
        return GSON.fromJson(line, typeOfT);
    }
}
