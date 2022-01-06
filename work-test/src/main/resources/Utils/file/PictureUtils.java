package com.wuyiling.worktest.Utils.file;

import com.yuuwei.faceview.constant.FileTypeConstants;
import com.yuuwei.faceview.domain.bean.ImageInfoBean;
import com.yuuwei.faceview.domain.bean.callback.WatermarkExtraBean;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.util.Base64;

/**
 * 生成所给定条件的图片
 *
 * @author 陶海峰
 * @date 2017-10-13
 */
public class PictureUtils {

    private static final Logger logger = LoggerFactory.getLogger(PictureUtils.class);

    private static final String USER_ICON = "pic/user.png";
    private static final String TIME_ICON = "pic/time.png";
    private static final String ADDRESS_ICON = "pic/address.png";
    private static final String SIGNER_ICON = "pic/signer.png";

    /**
     * 生成水印
     *
     * @param root    根目录
     * @param name    客户姓名
     * @param address 面签地址
     * @param path    水印存储地址
     * @param extras  水印附加信息
     */
    public static void getWatermark(String root, String name,
                                    String address, String path,
                                    WatermarkExtraBean extras) {
        logger.info("根据给定的字符串{}-{}-{},生成透明文字水印图片开始", name, address, extras);
        int length = 20;
        if (!StringUtils.isBlank(address) && address.length() > length) {
            length = address.length();
        }
        // 图片的宽度
        int width = 26 * length;
        // 图片的高度
        int height = 120;
        // 图片的类型
        int imageType = BufferedImage.TYPE_INT_RGB;
        // 字体的样式
        String fontName = "微软雅黑";
        // 字体的形状：普通
        int style = Font.PLAIN;
        // 字体的大小
        int size = 20;
        try {
            BufferedImage bufImage = new BufferedImage(width, height, imageType);
            Graphics2D graphics = bufImage.createGraphics();
            // 将图片设置为透明
            bufImage = graphics.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);

            // 画阴影部分
            graphics = bufImage.createGraphics();
            graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.XOR, 0.4f));
            graphics.setColor(Color.GRAY);
            graphics.setBackground(Color.GRAY);
            graphics.fillRect(0, 0, bufImage.getWidth(), bufImage.getHeight());
            graphics.dispose();

            graphics = bufImage.createGraphics();
            graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
            int index = 0;
            //添加银行名称icon
            if (StringUtils.isNotBlank(extras.getBankIcon())) {
                ImageIcon imageIcon = new ImageIcon(extras.getBankIcon());
                Image image = imageIcon.getImage();
                graphics.drawImage(image, 2, 32 + index * 30, null);
                index++;
            }

            if (StringUtils.isBlank(extras.getSigner())) {
                // 加用户头像水印
                ImageIcon imageIcon = new ImageIcon(root + USER_ICON);
                Image image = imageIcon.getImage();
                graphics.drawImage(image, 2, 32 + index * 30, null);
                index++;

                // 加地址水印
                imageIcon = new ImageIcon(root + ADDRESS_ICON);
                image = imageIcon.getImage();
                graphics.drawImage(image, 2, 32 + index * 30, null);
            }

            // 设置字体
            graphics.setFont(new Font(fontName, style, size));
            // 消除字体的锯齿
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            // 设置颜色：白色
            graphics.setColor(Color.WHITE);
            int y = 0;
            // 绘制字符串(横向居中)

            if (StringUtils.isNotBlank(extras.getBankName())) {
                graphics.drawString(extras.getBankName(), 30, 50 + 30 * y);
                y++;
            }
            //添加面签员
            if (StringUtils.isNotBlank(extras.getSigner())) {
                String signer = "面签员：" + extras.getSigner();
                graphics.drawString(signer, 240, 50 + 30 * y);

                name = "客户：" + name;
            }
            graphics.drawString(name, 30, 50 + 30 * y);
            y++;
            graphics.drawString(address, 30, 50 + 30 * y);
            graphics.dispose();
            File file = new File(path);
            ImageIO.write(bufImage, FileTypeConstants.FILE_TYPE_PNG, file);
        } catch (Exception e) {
            logger.error("生成水印失败，{}", e.getMessage(), e);
        } finally {
            logger.info("根据字符串{}生成水印图片完成", address);
        }
    }

    /**
     * 按固定大小缩放图片
     *
     * @param source     源图片
     * @param dir        缓存目录
     * @param videoWidth 视频的宽
     * @return
     */
    public static String zoomWithSize(String source, String dir, int videoWidth) throws IOException {
        ImageInfoBean imageInfo = PictureUtils.getImageInfo(source);
        Integer width = imageInfo.getWidth();
        Integer height = imageInfo.getHeight();
        if (width == videoWidth) {
            // 宽度一致，不用修改
            return source;
        }
        String newFile = dir + System.currentTimeMillis() + "_zoom." + FileUtils.getFileExt(source);
        // 缩小图片分辨率
        BigDecimal w = new BigDecimal(width);
        BigDecimal v = new BigDecimal(videoWidth);
        BigDecimal h = new BigDecimal(height);
        BigDecimal scale = v.divide(w, 2, BigDecimal.ROUND_DOWN);
        int targetWidth = w.multiply(scale).intValue() / 2 * 2;
        int targetHeight = h.multiply(scale).intValue() / 2 * 2;
        Thumbnails.of(source).size(targetWidth, targetHeight).outputQuality(1).toFile(newFile);
        return newFile;
    }

    /**
     * 获取图片信息
     *
     * @param imagePath 图片地址
     * @return 图片信息
     */
    public static ImageInfoBean getImageInfo(String imagePath) {
        InputStream inputStream = null;
        ImageInfoBean imageInfoBean = new ImageInfoBean();
        try {
            File file = new File(imagePath);
            inputStream = new FileInputStream(file);
            BufferedImage image = ImageIO.read(inputStream);
            imageInfoBean.setHeight(image.getHeight());
            imageInfoBean.setWidth(image.getWidth());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != inputStream) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return imageInfoBean;
    }

    /**
     * 将二进制数组转化为图片
     *
     * @param url        图片输出的url
     * @param imgStr     输入的二进制数组
     * @param formatName 输出图片的格式(要和url后面的格式相同)
     */
    public static void byteToImage(String url, String imgStr, String formatName) {
        try {
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] b = decoder.decode(imgStr);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {// 调整异常数据
                    b[i] += 256;
                }
            }
            OutputStream out = new FileOutputStream(url);
            out.write(b);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将图片转换为二进制
     *
     * @param url 图片的url
     * @return 返回图片的二进制数组
     */
    public static byte[] imageToByte(String url) {
        int index = StringUtils.lastIndexOf(url, '.');
        String formatName;
        if (index > -1) {
            formatName = StringUtils.substring(url, index + 1).toUpperCase();
        } else {
            throw new RuntimeException("请输入完整的url路径");
        }
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            File file = new File(url);
            BufferedImage bufImage = ImageIO.read(file);
            ImageIO.write(bufImage, formatName, out);
            byte[] bytes = out.toByteArray();
            return bytes;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String imgByBase64(String path) {
        if (StringUtils.isBlank(path)) {
            return null;
        }
        try (FileInputStream in = new FileInputStream(new File(path))) {
            byte[] bytes = new byte[in.available()];
            in.read(bytes);
            return new String(Base64.getEncoder().encode(bytes));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static void main(String[] args) throws IOException {
        String root = "D:\\Document\\temp\\video\\";
        String name = "钱占东";
        String address = "河南省郑州市金水区凤凰台街道凤凰城小区B区";
        String path = "D:\\Document\\temp\\video\\qian.png";
        WatermarkExtraBean bean = new WatermarkExtraBean();
        //bean.setBankName("XXX");
        //bean.setBankIcon(zoomWithSize("D:\\Document\\saas-bank-123124141.png", "D:\\Document\\temp\\fv", 24));
        getWatermark(root, name, address, path, bean);
    }

}
