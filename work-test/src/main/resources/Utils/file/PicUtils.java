package com.wuyiling.worktest.Utils.file;

import com.yuuwei.faceview.enums.http.ResponseCodeEnum;
import com.yuuwei.faceview.exception.GlobalBaseException;
import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * @author thf
 * @version v1.0
 * @date 2018-10-12
 */
public class PicUtils {

    private static Logger logger = LoggerFactory.getLogger(PicUtils.class);

    @Value("${temp.path}")
    private String temp;

    /**
     * 根据指定大小压缩图片
     *
     * @param imageBytes  源图片字节数组
     * @param desFileSize 指定图片大小，单位kb
     * @return 压缩质量后的图片字节数组
     */
    public static byte[] compressPicForScale(byte[] imageBytes, long desFileSize) {
        if (imageBytes == null || imageBytes.length <= 0 || imageBytes.length < desFileSize * 1024) {
            return imageBytes;
        }
        long srcSize = imageBytes.length;
        double accuracy = getAccuracy(imageBytes.length / 1024, desFileSize);
        try {
            while (imageBytes.length > desFileSize * 1024) {
                ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream(imageBytes.length);
                Thumbnails.of(inputStream)
                        .scale(accuracy)
                        .outputQuality(accuracy)
                        .toOutputStream(outputStream);
                imageBytes = outputStream.toByteArray();
            }
            logger.info("【图片压缩】| 图片原大小={}kb | 压缩后大小={}kb", srcSize / 1024, imageBytes.length / 1024);
        } catch (Exception e) {
            logger.error("【图片压缩】msg=图片压缩失败!", e);
        }
        return imageBytes;
    }

    /**
     * 自动调节精度(经验数值)
     *
     * @param size 源图片大小
     * @return 图片压缩质量比
     */
    private static double getAccuracy(long size, long desFileSize) {
        double accuracy;
        if (size < desFileSize * 1.125) {
            accuracy = 0.85;
        } else if (size < desFileSize * 2.5) {
            accuracy = 0.6;
        } else if (size < desFileSize * 4) {
            accuracy = 0.44;
        } else {
            accuracy = 0.4;
        }
        return accuracy;
    }

    /**
     * 图片压缩像素
     *
     * @param imageBytes
     * @param maxPixel
     */
    public static byte[] compressPictureByQuality(byte[] imageBytes, int maxPixel) {
        try {
            //读取原图片像素，判断是否需要进行图片像素压缩
            ByteArrayInputStream buffer = new ByteArrayInputStream(imageBytes);
            BufferedImage bufferedImage = ImageIO.read(buffer);
            int width = bufferedImage.getWidth();
            int height = bufferedImage.getHeight();

            boolean isMaxPixel = width > maxPixel || height > maxPixel;
            if (!isMaxPixel) {
                return imageBytes;
            }
            logger.info("像素尺寸的长或宽超过{}像素，需要进行压缩", maxPixel);

            int compressWidth = Math.min(width, maxPixel);
            int compressHeight = Math.min(height, maxPixel);

            ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream(imageBytes.length);
            Thumbnails.of(inputStream)
                    .size(compressWidth, compressHeight)
                    .toOutputStream(outputStream);
            logger.info("【原图片像素压缩】原图片像素：【{}，{}】|压缩后图片像素：【{}，{}】", width, height, compressWidth, compressHeight);
            return outputStream.toByteArray();
        } catch (Exception e) {
            logger.error("文件压缩像素失败！: {}", e.getMessage(), e);
            throw new GlobalBaseException(ResponseCodeEnum.FILE_COMPRESS_ERROR);
        }
    }

    public static void main(String[] args) throws IOException {
        String image1 = "D:\\Document\\temp\\fv\\20201214153639.png";
        byte[] bytes = org.apache.commons.io.
                FileUtils.readFileToByteArray(new File(image1));
        //byte[] result = compressPictureByQuality(bytes, 4096);
        //long l = System.currentTimeMillis();
        byte[] result = PicUtils.compressPicForScale(bytes, 1024 * 2);// 图片小于300kb
        //System.out.println(System.currentTimeMillis() - l);
        org.apache.commons.io.FileUtils.writeByteArrayToFile(new File("D:\\Document\\temp\\fv\\dd1.png"), result);
    }


}
