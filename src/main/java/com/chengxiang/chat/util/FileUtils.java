package com.chengxiang.chat.util;

import cn.hutool.core.util.HexUtil;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author 程祥
 * @date 2022/12/3 21:45
 */
public class FileUtils {
    /**
     * 获取文件MD5值
     * @param inputStream
     * @return
     */
    public static String getMd5(InputStream inputStream) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[8 * 1024];
            int length;
            while((length = inputStream.read(buffer)) != -1) {
                md5.update(buffer,0, length);
            }
            return new String(HexUtil.encodeHex(md5.digest()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取文件的扩展名
     * @param fileName
     * @return
     */
    public static String getExtName(String fileName) {
        int idx;
        if(StringUtils.isEmpty(fileName) || (idx = fileName.lastIndexOf(".")) == -1) {
            return "";
        }
        return fileName.substring(idx);
    }
}
