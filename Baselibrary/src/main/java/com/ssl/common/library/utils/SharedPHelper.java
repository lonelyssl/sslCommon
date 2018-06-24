/**
 * Copyright (C), 2014-2017, Zhengzhou IMAN Technology Development. Co., Ltd
 * Company: ImanSoft( http://www.imansoft.cn/ )
 */
package com.ssl.common.library.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import android.util.Base64;

import java.util.Map;
import java.util.Set;

/***
 * SharedPreferences 工具类
 */
public final class SharedPHelper {
    /**
     *
     */
    private SharedPreferences sharedPreferences;

    //
    public static final String name_file = "wsGameSDK";

    /**
     * 构造函数
     *
     * @param context
     */
    private SharedPHelper(Context context, String name, int mode) {
        super();
        this.sharedPreferences = context.getSharedPreferences(name, mode);
    }

    public static SharedPHelper getInstance(Context context) {
        return new SharedPHelper(context, name_file, Context.MODE_PRIVATE);
    }

    /**
     * 获取共享引用实例
     *
     * @param context activity的context
     * @param name    文件名称
     * @param mode    权限标识
     * @return
     */
    public static SharedPHelper getInstance(Context context, String name, int mode) {
        return new SharedPHelper(context, name, mode);
    }

    /**
     * 添加字段
     *
     * @param key   字段名称
     * @param value 字段值
     */
    public void put(String key, Object value) {
        try {
            if (key == null)
                return;
            Editor editor = this.sharedPreferences.edit();// 获取编辑器
            if (value == null) {
                value = "";
            }
            if (value != null) {
                String type = value.getClass().getSimpleName();
                if ("String".equals(type)) {
                    String str = (String) value;
                    // 加密
                    str = Base64.encodeToString(str.getBytes(), Base64.URL_SAFE | Base64.NO_WRAP);
                    editor.putString(key, str);
                } else if ("Integer".equals(type)) {
                    editor.putInt(key, (Integer) value);
                } else if ("Boolean".equals(type)) {
                    editor.putBoolean(key, (Boolean) value);
                } else if ("Float".equals(type)) {
                    editor.putFloat(key, (Float) value);
                } else if ("Long".equals(type)) {
                    editor.putLong(key, (Long) value);
                } else {
                    String str = JSONParser.bean2Json(value);
                    str = Base64.encodeToString(str.getBytes(), Base64.URL_SAFE | Base64.NO_WRAP);
                    editor.putString(key, str);
                }
            }
            editor.commit();// 提交修改
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存数据的方法，
     *
     * @param headers
     */
    public void put(Map<String, String> headers) {
        try {
            Editor editor = this.sharedPreferences.edit();// 获取编辑器
            if (headers != null) {
                Set<String> keys = headers.keySet();
                for (String key : keys) {
                    Object value = headers.get(key);
                    put(key, value);
                }
            }
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据字段名称获取字段值
     *
     * @param <T>
     * @param key 字段名称
     *            默认值
     * @return
     */
    public <T> T get(String key, T defaultValue) {
        if (defaultValue == null) {
            return (T) get(key, String.class, defaultValue);
        } else {
            return (T) get(key, defaultValue.getClass(), defaultValue);
        }
    }

    /**
     * 根据字段名称获取字段值
     *
     * @param key          字段名称
     * @param valueType    字段值类型
     * @param defaultValue 默认字段值
     * @return 字段值 如果返回值类型不在{String,Integer,Boolean,Float,Long}中，默认返回null.
     */
    private <T> Object get(String key, Class<?> valueType, T defaultValue) {
        String type = valueType.getSimpleName();
        if ("String".equals(type)) {
            String value = this.sharedPreferences.getString(key, "");
            if (TextUtils.isEmpty(value)) {
                return defaultValue;
            } else {
                // 解密
                byte[] bt = Base64.decode(value, Base64.URL_SAFE | Base64.NO_WRAP);
                String strvalue = new String(bt);
                return strvalue;
            }

        } else if ("Integer".equals(type)) {
            return this.sharedPreferences.getInt(key, (Integer) defaultValue);
        } else if ("Boolean".equals(type)) {
            return sharedPreferences.getBoolean(key, (Boolean) defaultValue);
        } else if ("Float".equals(type)) {
            return sharedPreferences.getFloat(key, (Float) defaultValue);
        } else if ("Long".equals(type)) {
            return sharedPreferences.getLong(key, (Long) defaultValue);
        }
        return null;
    }

    /**
     * 根据字段名称删除字段
     *
     * @param key 字段名称
     */
    public void delete(String key) {
        Editor editor = this.sharedPreferences.edit();
        editor.remove(key);
        editor.commit();
    }

    /**
     * 清空所有字段名称
     *
     * @throws Exception
     */
    public void deleteAll() throws Exception {
        Editor editor = this.sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

}
