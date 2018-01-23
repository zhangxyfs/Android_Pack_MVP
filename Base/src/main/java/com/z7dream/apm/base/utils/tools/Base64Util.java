package com.z7dream.apm.base.utils.tools;


import android.support.annotation.NonNull;

import com.google.gson.Gson;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * Created by xiaoyu.zhang on 2016/11/11 13:50
 * Email:zhangxyfs@126.com
 *  
 */
public class Base64Util {

    /**
     * 将base64的字符串转化成object
     *
     * @param value
     * @return
     */
    public static <T extends Object> T getEntityByOIS(String value) {
        T obj = null;
        byte[] base64Bytes = Base64.decode(value);
        ByteArrayInputStream bais = new ByteArrayInputStream(base64Bytes);
        ObjectInputStream ois = null;

        try {
            ois = new ObjectInputStream(bais);
            obj = (T) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                assert ois != null;
                ois.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return obj;
    }

    /**
     * 将base64字符串转换成object
     *
     * @param value
     * @param flag
     * @param object
     * @return
     */
    public static Object getEntityByJson(@NonNull String value, int flag, @NonNull Object object) {
        byte[] base64Bytes = android.util.Base64.decode(value, flag);
        String json = Arrays.toString(base64Bytes);
        return new Gson().fromJson(json, object.getClass());
    }

    /**
     * 将base64字符串转换成string
     *
     * @param value
     * @param flag
     * @return
     */
    public static String fromBase64(@NonNull String value, int flag) {
        String result = "";
        try {
            result = new String(android.util.Base64.decode(value, flag), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 将obj转换成base64字符串
     *
     * @param json
     * @param flag
     * @return
     */
    public static String toBase64(String json, int flag) {
        String base64Str = null;
        byte[] bytes = null;
        try {
            bytes = json.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (bytes != null) {
            base64Str = android.util.Base64.encodeToString(bytes, flag);
        }
        return base64Str;
    }
}
