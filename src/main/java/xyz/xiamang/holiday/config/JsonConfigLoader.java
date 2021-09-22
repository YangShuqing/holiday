/*
 * Copyright (c) 2020 , Inc. All Rights Reserved.
 */

package xyz.xiamang.holiday.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 *
 * @author yangshuqing
 */
public class JsonConfigLoader {

    private static final Logger log = LoggerFactory.getLogger(JsonConfigLoader.class);

    private static String loadAsString(File file) throws FileNotFoundException {
        return loadAsString(new FileInputStream(file));
    }

    private static String loadAsString(String configFileName) {
        InputStream in = JsonConfigLoader.class.getResourceAsStream(configFileName);//tablesource.json
        return loadAsString(in);
    }

    private static String loadAsString(InputStream in) {
        byte[] bytes = new byte[0];
        try {
            bytes = new byte[in.available()];
            in.read(bytes);
        } catch (IOException e) {
            log.error("加载 配置失败 {} {}", e);
            e.printStackTrace();
        }
        return new String(bytes, Charset.forName("UTF-8"));
    }

    public static <T extends JsonConfig> Map<String, T> loadAndToMap(String configFileName, Class<T> clazz, String keyField) {
        List<T> list = loadList(configFileName, clazz);
        Map<String, T> map = new HashMap<>();
        list.stream().forEach(l -> {
            Object k = l.get(keyField);
            map.put((String) k, l);
            log.info("配置 {} {}", k, l);
        });
        log.info("加载配置 {} 成功 共 {}项", configFileName, map.size());
        return map;
    }

    public static <T> T load(String configFileName, Class<T> clazz) {
        String s = loadAsString(configFileName);
        return JSONObject.parseObject(s, clazz);
    }

    public static <T> T load(File file, Class<T> clazz) {
        String s;
        try {
            s = loadAsString(file);
            System.out.println(s);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return JSONObject.parseObject(s, clazz);
    }

    public static <T> List<T> loadList(String configFileName, Class<T> clazz) {
        String s = loadAsString(configFileName);
        log.debug("json config {} -{}", configFileName, s);
        return JSONArray.parseArray(s, clazz);
    }
}
