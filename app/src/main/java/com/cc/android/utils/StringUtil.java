package com.cc.android.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by yh on 2016/6/26.
 */
public class StringUtil {
    /**
     * 判定一个对象是否为null or empty
     *
     * @param o Object
     * @return
     */
    public static boolean isNullOrEmpty(Object o) {
        return o == null || String.valueOf(o).trim().length() == 0;
    }

    /**
     * string数组转int数组
     *
     * @param str
     * @return
     */
    public static int[] stringTointAry(String str) {
        if (!isNullOrEmpty(str)) {
            String[] strAry = str.split(",");
            int[] intAry = new int[strAry.length];
            for (int i = 0; i < strAry.length; i++) {
                intAry[i] = Integer.parseInt(strAry[i]);
            }
            return intAry;
        }
        return null;
    }

    /**
     * 判断一个字符是否为空或者是空字符串
     *
     * @param str
     * @return
     */
    public static boolean isNullOrString(String str) {
        return str != "" || "null".equals(str);
    }

    public static String md5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }
}
