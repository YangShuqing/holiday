/*
 * Copyright (c) 2020 , Inc. All Rights Reserved.
 */

package xyz.xiamang.holiday.config;

import java.lang.reflect.Field;

import com.alibaba.fastjson.JSON;

/**
 *
 * @author yangshuqing
 */
public class JsonConfig {

    /**
     *  通过key 获取字段值
     * @param key 字段
     * @return Object
     */
    public Object get(String key) {
        Field[] fields = getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(true);
            if (fields[i].getName().equals(key)) {
                try {
                    return fields[i].get(this);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

}
