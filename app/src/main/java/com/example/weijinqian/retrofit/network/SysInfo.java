package com.example.weijinqian.retrofit.network;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;

/**
 * Created by weijinqian on 2017/8/23.
 */

class SysInfo {

    /**
     * 根据输入流产生md5
     * @param is 输入流
     * @return md5值的字符数组
     */
    public final static byte[] md5Data(InputStream is) {
        if (is == null) {
            return null;
        }
        byte[] data = null;
        try {
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            byte[] buf = new byte[4096];
            int size;
            while ((size = is.read(buf)) > 0) {
                digest.update(buf, 0, size);
            }
            data = digest.digest();
            is.close();
        } catch (Exception e) {
//            if (Macro.IS_CATCH_LOG_ENABLE) {
//                BDLog.printStackTrace(e);
//            }
        }
        return data;
    }

    /**
     * 文件进行 md5 值的计算
     * @param f 输入的文件
     * @return md5值的字符数组
     */
    public final static byte[] md5Data(File f) {
        if (f == null || !f.exists()) {
            return null;
        }
        byte[] data = null;
        try {
            data = md5Data(new FileInputStream(f));
        } catch (Exception e) {
//            if (Macro.IS_CATCH_LOG_ENABLE) {
//                BDLog.printStackTrace(e);
//            }
        }
        return data;
    }
    /**
     * md5值转换为字符串
     * @param md5Data md5值的字符数组
     * @return 转为字符串的数组
     */
    public final static String md5String(byte[] md5Data) {
        if (md5Data == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < md5Data.length; i++) {
            sb.append(getHexchar(md5Data[i] >> 4));
            sb.append(getHexchar(md5Data[i]));
        }
        return sb.toString();
    }
    /**
     * 转换数据为16进制格式
     *
     * @param value 整型数据
     * @return 16进制 char 数据
     */
    private final static char getHexchar(int value) {
        value = value & 0xF;
        if (value < 10) {
            return (char) ('0' + value);
        } else {
            return (char) ('A' + value - 10);
        }
    }
}
