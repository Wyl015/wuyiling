package com.test.utils.imageTest;

import org.apache.tomcat.util.buf.HexUtils;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class openCVTest {
    /*
     * @Author wuyiling
     * @Description 签名图替换背景颜色
     * @Date 13:45 2021/10/28
     * @Param []
     * @return void
     **/
    @Test
    public void test1() throws IOException {
        //指定的图片路径
        FileInputStream dir = new FileInputStream("C:/Users/Admin/Desktop/ItextpdfTest/签名图片.png");
        //新建一个长度为3的数组，负责保存rgb的值
        int[] rgb = new int[3];
        //通过ImageIO.read()方法来返回一个BufferedImage对象，可以对图片像素点进行修改
        BufferedImage bImage = ImageIO.read(dir);
        //获取图片的长宽高
        int width = bImage.getWidth();
        int height = bImage.getHeight();
        int minx = bImage.getMinTileX();
        int miny = bImage.getMinTileY();
        //遍历图片的所有像素点，并对各个像素点进行判断，是否修改
        for (int i = minx; i < width; i++) {
            for (int j = miny; j < height; j++) {
                int pixel = bImage.getRGB(i, j);
                //获取图片的rgb
                rgb[0] = (pixel & 0xff0000) >> 16;
                rgb[1] = (pixel & 0xff00) >> 8;
                rgb[2] = (pixel & 0xff);
                //进行判断，如果色素点在指定范围内，则进行下一步修改
                if (rgb[0] <= 255 && rgb[0] >= 100 && rgb[1] <= 255 && rgb[1] > 100 && rgb[2] <= 255 && rgb[2] > 100) {
//                    bImage.setRGB(i, j, 0xffffffff);
                    bImage.setRGB(i, j, (0 << 24) | (pixel & 0x00ffffff));//白->
                    System.out.print("像素：" + i +"," + j);
                    System.out.print("  RGB：" + pixel + ":" + Integer.toHexString(pixel));
                    System.out.println("  to  " + Integer.toHexString((0 << 24) | (pixel & 0xffffffff)));
                } else {
                    bImage.setRGB(i, j, (255 << 24) | (pixel & 0x00ffffff));//黑
                    System.out.print("像素：" + i +"," + j);
                    System.out.println("  RGB：" + pixel + ":" + Integer.toHexString(pixel));
                    System.out.println("  to  " + Integer.toHexString((255 << 24) | (pixel & 0x00ffffff)));
                }
            }
        }
        //输出照片保存在本地
        FileOutputStream ops;
        try {
            ops = new FileOutputStream(new File("C:/Users/Admin/Desktop/ItextpdfTest/签名图片2.png"));
            //这里写入的“jpg”是照片的格式，根据照片后缀有所不同
            ImageIO.write(bImage, "png", ops);
            ops.flush();
            ops.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /*
     * @Author wuyiling
     * @Description 签名图扣掉背景颜色
     * @Date 13:45 2021/10/28
     * @Param []
     * @return void
     **/
    @Test
    public void test2() {
        PictureUtils.transApla(new File("C:/Users/Admin/Desktop/ItextpdfTest/签名图片2.png"), 169);
    }



}