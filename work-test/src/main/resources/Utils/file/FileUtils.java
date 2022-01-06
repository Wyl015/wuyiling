package com.wuyiling.worktest.Utils.file;

import com.yuuwei.faceview.constant.FileTypeConstants;
import com.yuuwei.faceview.util.Base64DecodedMultipartFile;
import com.yuuwei.faceview.util.HttpClientPoolUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;

/**
 * 读取文件工具类
 *
 * @author 陶海峰
 * @date 2017-11-27
 */
public class FileUtils {

    private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);

    /**
     * 获取路径的文件名称
     *
     * @param path
     * @return
     */
    public static String getAllFileName(String path) {
        return path.substring(path.lastIndexOf("/") + 1, path.length());
    }

    /**
     * 获取文件名称
     *
     * @param fileName
     */
    public static String getFileName(String fileName) {

        return fileName.substring(0, fileName.lastIndexOf("."));
    }

    /**
     * 获取文件的后缀类型
     *
     * @param fileName
     * @return
     */
    public static String getFileExt(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
    }

    /**
     * 检查文件的url路径
     *
     * @param url 目前只解析aac和mp4
     * @return
     */
    public static int checkContentType(String url) {
        if (StringUtils.isBlank(url)) {
            return 0;
        }
        String type = url.substring(url.lastIndexOf(".") + 1).toLowerCase();
        if (StringUtils.equals(type, FileTypeConstants.FILE_TYPE_MP4)) {
            // 视频
            return 1;
        } else if (StringUtils.equals(type, FileTypeConstants.FILE_TYPE_AAC)) {
            // 音频
            return 2;
        }
        return 0;
    }

    /**
     * @param byteArrayOutputStream 将文件内容写入ByteArrayOutputStream
     * @param response              HttpServletResponse	写入response
     * @param returnName            返回的文件名
     */
    public static void download(ByteArrayOutputStream byteArrayOutputStream, HttpServletResponse response, String returnName) throws IOException {
        response.setContentType("application/octet-stream;charset=utf-8");
        //保存的文件名,必须和页面编码一致,否则乱码
        returnName = response.encodeURL(new String(returnName.getBytes(), "iso8859-1"));
        response.addHeader("Content-Disposition", "attachment;filename=" + returnName);
        response.setContentLength(byteArrayOutputStream.size());
        //取得输出流
        ServletOutputStream outputstream = response.getOutputStream();
        //写到输出流
        byteArrayOutputStream.writeTo(outputstream);
        //关闭
        byteArrayOutputStream.close();
        //刷数据
        outputstream.flush();
    }

    /**
     * 设置文字样式
     *
     * @param nStyle
     * @param nFont
     * @return
     */
    public static CellStyle excelText(CellStyle nStyle, Font nFont) {
        nFont.setFontName("Times New Roman");
        nFont.setFontHeightInPoints((short) 13);
        //横向居中
        nStyle.setAlignment(HorizontalAlignment.CENTER);
        //纵向居中
        nStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        //表格线(上下左右的细线)
        nStyle.setBorderTop(BorderStyle.THIN);
        nStyle.setBorderLeft(BorderStyle.THIN);
        nStyle.setBorderRight(BorderStyle.THIN);
        nStyle.setBorderBottom(BorderStyle.THIN);
        nStyle.setFont(nFont);
        return nStyle;
    }

    /**
     * 获取文件大小
     *
     * @param file
     * @return
     */
    public static String getFileSize(File file) {
        String size = "";
        if (file.exists() && file.isFile()) {
            long fileS = file.length();
            DecimalFormat df = new DecimalFormat("#.00");
            if (fileS < 1024) {
                size = df.format((double) fileS) + "BT";
            } else if (fileS < 1048576) {
                size = df.format((double) fileS / 1024) + "KB";
            } else if (fileS < 1073741824) {
                size = df.format((double) fileS / 1048576) + "MB";
            } else {
                size = df.format((double) fileS / 1073741824) + "GB";
            }
        } else if (file.exists() && file.isDirectory()) {
            size = "";
        } else {
            size = "0BT";
        }
        return size;
    }

    /**
     * 获取文件大小
     *
     * @param fileSize
     * @return
     */
    public static String getFileSize(long fileSize) {
        String size = "";
        if (fileSize > 0) {
            DecimalFormat df = new DecimalFormat("#.00");
            if (fileSize < 1024) {
                size = df.format((double) fileSize) + "BT";
            } else if (fileSize < 1048576) {
                size = df.format((double) fileSize / 1024) + "KB";
            } else if (fileSize < 1073741824) {
                size = df.format((double) fileSize / 1048576) + "MB";
            } else {
                size = df.format((double) fileSize / 1073741824) + "GB";
            }
        } else {
            size = "0BT";
        }
        return size;
    }

    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        if (file.isFile() && file.exists()) {
            boolean succeedDelete = file.delete();
            if (succeedDelete) {
                logger.info("删除单个文件" + fileName + "成功！");
                return true;
            } else {
                logger.info("删除单个文件" + fileName + "失败！");
                return true;
            }
        } else {
            logger.info("删除单个文件" + fileName + "失败！");
            return false;
        }
    }

    /**
     * 删除文件
     *
     * @param path
     * @param isDelete 是否删除当前文件夹
     */
    public static void deleteFile(String path, boolean isDelete) {
        //输入要删除文件目录的绝对路径
        File file = new File(path);
        if (isDelete) {
            deleteFile(file);
        } else {
            File[] files = file.listFiles();
            for (File f : files) {
                deleteFile(f);
            }
        }
    }

    public static void deleteFile(File file) {
        //判断文件不为null或文件目录存在
        if (file == null || !file.exists()) {
            logger.info("文件路径不存在，请检查文件路径是否正确");
            return;
        }
        if (file.isDirectory()) {
            //取得这个目录下的所有子文件对象
            File[] files = file.listFiles();
            //遍历该目录下的文件对象
            for (File f : files) {
                deleteFile(f);
            }
        } else {
            file.delete();
        }
        //删除空文件夹  for循环已经把上一层节点的目录清空。
        file.delete();
    }

    /**
     * 删除文件夹
     *
     * @param dir
     * @return
     */
    public static boolean deleteDirectory(String dir) {
        //如果dir不以文件分隔符结尾，自动添加文件分隔符
        if (!dir.endsWith(File.separator)) {
            dir = dir + File.separator;
        }
        File dirFile = new File(dir);
        //如果dir对应的文件不存在，或者不是一个目录，则退出
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            logger.info("删除目录失败" + dir + "目录不存在！");
            return false;
        }
        boolean flag = true;
        //删除文件夹下的所有文件(包括子目录)
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            //删除子文件
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag) {
                    break;
                }
            }
            //删除子目录
            else {
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag) {
                    break;
                }
            }
        }

        if (!flag) {
            logger.info("删除目录失败");
            return false;
        }

        //删除当前目录
        if (dirFile.delete()) {
            logger.info("删除目录" + dir + "成功！");
            return true;
        } else {
            logger.info("删除目录" + dir + "失败！");
            return false;
        }
    }

    public static MultipartFile base64ToMultipart(String base64) {
        try {
            String[] baseStrs = base64.split(",");
            BASE64Decoder decoder = new BASE64Decoder();
            byte[] b = decoder.decodeBuffer(baseStrs[1]);

            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }

            return new Base64DecodedMultipartFile(b, baseStrs[0]);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String multipartToBase64HaiNan(String filePath) {
        String fileType = filePath.substring(filePath.lastIndexOf(".") + 1);
//        String pattern = "data:image/" + fileType + ";base64,";
        byte[] bytes = null;
        try (InputStream inputStream = new FileInputStream(filePath)) {
            bytes = new byte[inputStream.available()];
            inputStream.read(bytes);

        } catch (IOException e) {
            e.printStackTrace();
        }
        BASE64Encoder encoder = new BASE64Encoder();
        String base64Str = encoder.encode(bytes);
        return base64Str.replaceAll("(\r\n|\r|\n|\n\r)", "");
    }

    public static String multipartToBase64(String filePath) {
        String fileType = filePath.substring(filePath.lastIndexOf(".") + 1);
        String pattern = "data:image/" + fileType + ";base64,";
        byte[] bytes = null;
        try (InputStream inputStream = new FileInputStream(filePath)) {
            bytes = new byte[inputStream.available()];
            inputStream.read(bytes);

        } catch (IOException e) {
            e.printStackTrace();
        }
        BASE64Encoder encoder = new BASE64Encoder();
        String base64Str = encoder.encode(bytes);
        return pattern+ base64Str.replaceAll("(\r\n|\r|\n|\n\r)", "");
    }

    /**
     * 根据地址获得数据的字节流
     *
     * @param strUrl 网络连接地址
     * @return
     */
    public static byte[] getFileFromNetByUrl(String strUrl) {
        try {
            URL url = new URL(strUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5 * 1000);
            InputStream inStream = conn.getInputStream();//通过输入流获取图片数据
            byte[] btImg = readInputStream(inStream);//得到图片的二进制数据
            return btImg;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 从输入流中获取数据
     *
     * @param inStream 输入流
     * @return
     * @throws Exception
     */
    public static byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        inStream.close();
        return outStream.toByteArray();
    }

    /**
     * 文件下载
     *
     * @param url         待下载文件的url
     * @param storagePath 文件下载到本地的路径
     */
    public static void downloadFile(String url, String storagePath) throws Exception {
        OutputStream out = null;
        InputStream in = null;
        CloseableHttpClient httpClient = HttpClientPoolUtils.httpClient;
        try {
            HttpGet httpGet = new HttpGet(url);
            CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = httpResponse.getEntity();
                in = entity.getContent();
                File file = new File(storagePath);
                out = new FileOutputStream(file);
                byte[] buffer = new byte[4096];
                int readLength = 0;
                while ((readLength = in.read(buffer)) > 0) {
                    byte[] bytes = new byte[readLength];
                    System.arraycopy(buffer, 0, bytes, 0, readLength);
                    out.write(bytes);
                }
                out.flush();
            }
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        String tempPath = "D:\\Document\\temp\\biz\\";
        FileUtils.deleteFile(tempPath, false);
    }

}
