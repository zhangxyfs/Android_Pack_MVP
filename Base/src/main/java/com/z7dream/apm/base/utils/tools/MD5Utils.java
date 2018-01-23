package com.z7dream.apm.base.utils.tools;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {

    /**
     * 功能： 获取MD5加密算法加密串(32位)
     *
     * @param str 待加密字符串
     * @return 加密后的字符串 如果失败返回""
     */
    public static String getMD5(String str) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            result = buf.toString();
        } catch (NoSuchAlgorithmException e) {
        }
        return result;
    }

    /**
     * 功能： 获取MD5加密算法加密串(16位)
     *
     * @param str 待加密字符串
     * @return 加密后的字符串 如果失败返回""
     */
    public static String getShortMD5(String str) {
        String result = getMD5(str);
        if (result != null && result.length() == 32) {
            result = result.substring(8, 24);
        } else {
            result = "";
        }
        return result;
    }


}
