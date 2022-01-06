package com.wuyiling.worktest.Utils.file;


import com.yuuwei.faceview.enums.http.ResponseCodeEnum;
import com.yuuwei.faceview.exception.GlobalBaseException;
import com.yuuwei.faceview.util.IDUtils;
import com.yuuwei.faceview.util.PropertyUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author: fw
 * @date: 2021/8/13 16:24
 * @description:
 */

@Slf4j
public class FileDownLoadUtils {

    private static final String tempPath = PropertyUtils.getAppProperty("temp.path");

    public static String downLoadFileFromHttp(String path){
        try {
            String pre = path.substring(0,path.indexOf(":"));
            if(StringUtils.equals(pre,"http")){
                return downLoadHttpFile(path);
            }else if(StringUtils.equals(pre,"https")){
                return downLoadHttpsFile(path);
            }else {
                log.error("文件地址,不属于http/https前缀的合法路径:{}",path);
                throw new GlobalBaseException(ResponseCodeEnum.SERVER_ERROR);
            }
        }catch (Exception e){
            log.error("文件下载失败:{}",path);
            throw new GlobalBaseException(ResponseCodeEnum.SERVER_ERROR);
        }

    }

    private static String downLoadHttpsFile(String path) throws Exception {
        FileOutputStream fos = null;
        InputStream inputStream = null;
        try {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //设置超时间为10秒
            conn.setConnectTimeout(10 * 1000);
            //防止屏蔽程序抓取而返回403错误
//        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
//        conn.setRequestProperty("lfwywxqyh_token",toekn);

            //得到输入流
            inputStream = conn.getInputStream();
            //获取自己数组
            byte[] getData = readInputStream(inputStream);

            //文件保存位置
            String suffix = path.substring(path.lastIndexOf("."));
            File file = new File(tempPath + File.separator + IDUtils.generateFileName() + suffix);
            fos = new FileOutputStream(file);
            fos.write(getData);

            return file.getAbsolutePath();
        }finally {
            if (fos != null) {
                fos.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    private static String downLoadHttpFile(String path) throws Exception {
        FileOutputStream fos = null;
        InputStream inputStream = null;
        try {
            String suffix=path.substring(path.lastIndexOf("."));
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //设置超时间为10秒
            conn.setConnectTimeout(10 * 1000);
            //防止屏蔽程序抓取而返回403错误
//        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
//        conn.setRequestProperty("lfwywxqyh_token",toekn);

            //得到输入流
            inputStream = conn.getInputStream();
            //获取自己数组
            byte[] getData = readInputStream(inputStream);

            //文件保存位置

            File file = new File(tempPath + File.separator + IDUtils.generateFileName()+suffix);
            fos = new FileOutputStream(file);
            fos.write(getData);

            return file.getAbsolutePath();
        }finally {
            if (fos != null) {
                fos.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    private static byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }
}
