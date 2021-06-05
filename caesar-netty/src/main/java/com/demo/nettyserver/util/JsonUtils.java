/**
 *
 */
package com.demo.nettyserver.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

public abstract class JsonUtils {

    private final static Gson gson = new GsonBuilder().setDateFormat("yyyyMMddHHmmss")
            .serializeSpecialFloatingPointValues().create();

    public static String toJson(Object src) {
        return gson.toJson(src);
    }

    public static String toJsonForDebug(Object src) {
        return new GsonBuilder().setDateFormat("yyyyMMddHHmmss").serializeNulls().setPrettyPrinting().create()
                .toJson(src);
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }

    public static <T> T fromJson(String json, Type type) {
        return gson.fromJson(json, type);
    }
}
