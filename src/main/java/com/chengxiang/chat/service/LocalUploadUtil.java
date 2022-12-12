package com.chengxiang.chat.service;

import com.chengxiang.chat.exception.BizException;
import org.springframework.beans.factory.annotation.Value;

import java.io.*;

/**
 * @author 程祥
 * @date 2022/11/30 12:54
 */
public class LocalUploadUtil {
    // 所有上传文件的存储路径
    private static String rootPath = "E:/images/";

    // 存储的服务器地址 方便访问
    private static String url = "http://127.0.0.1:3333/upload";

    /**
     * 上传文件
     * @param inputStream
     * @param fileName
     * @param filePath
     * @throws IOException
     */
    public static void upload(InputStream inputStream,String fileName, String filePath) throws IOException {
        File directory = new File(rootPath + filePath);
        // 如果目录不存在
        if(!directory.exists()) {
            if(!directory.mkdirs()) {
                throw new BizException("创建目录失败");
            }
        }
        File file = new File(rootPath + filePath + fileName);
        if(file.exists()) {
            return;
        }
        FileOutputStream fileOutputStream = null;
        BufferedInputStream bis = new BufferedInputStream(inputStream);
        try {
            fileOutputStream = new FileOutputStream(file);
            byte[] bytes = new byte[1024];
            int len;
            while((len = bis.read(bytes)) != -1) {
                fileOutputStream.write(bytes,0,len);
            }
            fileOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
            throw new BizException("文章写入失败！！！");
        } finally {
            inputStream.close();
            fileOutputStream.close();
        }
    }

    public static String getFileAccessUrl(String filePath) {
        return url + filePath;
    }

}
