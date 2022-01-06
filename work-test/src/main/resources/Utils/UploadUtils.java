package com.wuyiling.worktest.Utils;

import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 上传工具类
 * @author 陶海峰
 * @date 2017-11-23
 */
public class UploadUtils {

    private static final Logger logger = LoggerFactory.getLogger(com.yuuwei.faceview.util.UploadUtils.class);

    private static final String LINUX_OS_NAME = "linux";

    /**
     * 上传文件
     * @param mt
     * @param fileName 文件名称
     * @param filePath 文件路径
     * @param basePath 文件根路径
     * @return 返回文件的完整url
     */
    public static String uploadFile(MultipartFile mt, String fileName, String filePath, String basePath) throws Exception {
        String path = basePath + filePath;
        new File(path).mkdirs();
        String url = path + fileName;
        mt.transferTo(new File(url));
        readPermission(basePath);
        logger.info("文件：{},上传成功", url);
        return url;
    }

    /**
     * 上传文件
     * @param mt
     * @param fileName 文件名称
     * @param filePath 文件路径
     * @param basePath 文件根路径
     * @return 返回文件的完整url
     */
    public static String uploadImage(MultipartFile mt, String fileName, String filePath, String basePath) throws Exception {
        String path = basePath + filePath;
        new File(path).mkdirs();
        String url = path + fileName;
        // 其中的scale是可以指定图片的大小，值在0到1之间，1f就是原图大小，0.5就是原图的一半大小，这里的大小是指图片的长宽。
        // 而outputQuality是图片的质量，值也是在0到1，越接近于1质量越好，越接近于0质量越差。
        Thumbnails.of(mt.getInputStream()).scale(1f).outputQuality(0.9f).toFile(url);
//        Runtime.getRuntime().exec("chmod 777 -R " + basePath);
        logger.info("文件：{},上传成功", url);
        return url;
    }

    public static String uploadFile(byte[] imageBytes, String fileName, String filePath, String basePath) throws Exception {
        new File(basePath + filePath).mkdirs();
        String url = basePath + filePath + fileName;
        org.apache.commons.io.FileUtils.writeByteArrayToFile(new File(url), imageBytes);
        readPermission(basePath);
        logger.info("文件：{},上传成功", url);
        return url;
    }

    /**
     * 在linux环境下，需要对文件添加读权限
     * @param path 文件路径
     */
    public static void readPermission(String path) {
        String os = System.getProperty("os.name").toLowerCase();
        if (StringUtils.indexOf(os, LINUX_OS_NAME) != -1) {
            try {
                Runtime.getRuntime().exec("chmod 777 -R " + path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 通过文件url下载文件到本地
     *
     * @param fileUrl 文件下载路径
     * @param fileName 文件名称
     * @param filePath 文件路径
     * @param basePath 文件根路径
     * @return 返回文件的完整url
     */
    public static String saveFileByUrl(String fileUrl, String fileName, String filePath, String basePath) throws Exception {
        //GET请求获取输入流，得到图片的二进制数据，以二进制封装得到数据，具有通用性
        URL url = new URL(fileUrl);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(3 * 1000);
        InputStream inStream = conn.getInputStream();
        byte[] data = readInputStream(inStream);

        //保存文件到本地
        String path = basePath + filePath;
        new File(path).mkdirs();
        String savePath = path + fileName;
        File imageFile = new File(savePath);
        FileOutputStream outStream = new FileOutputStream(imageFile);
        outStream.write(data);
        outStream.close();

        //对文件添加读权限
        readPermission(basePath);
        logger.info("文件：{},上传成功", savePath);
        return savePath;
    }

    private static byte[] readInputStream(InputStream inStream) throws Exception{
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        //创建一个Buffer字符串
        byte[] buffer = new byte[1024];
        //每次读取的字符串长度，如果为-1，代表全部读取完毕
        int len = 0;
        //使用一个输入流从buffer里把数据读取出来
        while( (len=inStream.read(buffer)) != -1 ){
            //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
            outStream.write(buffer, 0, len);
        }
        //关闭输入流
        inStream.close();
        //把outStream里的数据写入内存
        return outStream.toByteArray();
    }

}
