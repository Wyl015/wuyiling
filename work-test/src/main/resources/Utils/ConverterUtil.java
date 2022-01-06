package com.wuyiling.worktest.Utils;

import com.yuuwei.faceview.async.JodConverterInstance;
import com.yuuwei.faceview.util.file.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * JodConverter工具类
 *
 * @author mhw
 * @date 2018/11/26 9:39
 */
@Component
public class ConverterUtil {

    private static final Logger logger = LoggerFactory.getLogger(com.yuuwei.faceview.util.ConverterUtil.class);

    private static String pdfConvertUrl;
    @Value("${files.pdf.convertUrl}")
    public void setPdfConvertUrl(String convertUrl) {
        pdfConvertUrl = convertUrl;
    }

    private static String pdfSystemName;
    @Value("${files.pdf.pdfSystemName}")
    public void setPdfSystemName(String name) {
        pdfSystemName = name;
    }

    /**
     * 格式转换方法。
     * 由JodConverter的单例获取documentConverter进行实现。
     *
     * @param inPath  转换文档的路径（包括文件名）
     * @param outPath 格式转换后的保存路径（包括文件名）
     * @author mhw
     * @date 2018/11/26 9:40
     */
    public static void word2pdf(String inPath, String outPath) {
        try {
            File in = new File(inPath);
            File out = new File(outPath);
            JodConverterInstance instance = JodConverterInstance.getInstance();
            instance.getDocumentConverter()
                    .convert(in)
                    .to(out)
                    .execute();
            logger.info("文档转换成功，生成地址：{}", outPath);
        } catch (Exception e) {
            logger.warn("文档转换异常，inPath:{}，outPath:{}", inPath, outPath, e);
            throw new RuntimeException();
        }
    }

    public static void word2PdfRemote(String inPath, String outPath) {
        try {
            byte[] bytes = getConvertResult(inPath);
            File out = new File(outPath);
            FileOutputStream fos = new FileOutputStream(out);
            fos.write(bytes, 0, bytes.length);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            logger.error("文档转换异常，inPath:{}，outPath:{}", inPath, outPath, e);
            throw new RuntimeException("文档转换异常");
        }
    }

    private static byte[] getConvertResult(String wordFile) throws IOException {
        CloseableHttpResponse httpResponse = null;
        try {
            CloseableHttpClient httpClient = com.yuuwei.faceview.util.HttpClientPoolUtils.httpClient;
            HttpPost httpPost = new HttpPost(pdfConvertUrl);
            HttpEntity reqEntity = MultipartEntityBuilder.create()
                    .addTextBody("from", pdfSystemName)
                    .addBinaryBody("wordFile", new File(wordFile))
                    .build();
            httpPost.setEntity(reqEntity);
            httpResponse = httpClient.execute(httpPost);
            Integer code = httpResponse.getStatusLine().getStatusCode();
            if (200 != code) {
                throw new RuntimeException("调用openoffice服务转换文件错误:code[{}]" + code);
            }
            byte[] responseString = EntityUtils.toByteArray(httpResponse.getEntity());
            return responseString;
        } finally {
            if (null != httpResponse) {
                logger.info("文档转换-关闭response 连接");
                httpResponse.close();
            }
        }
    }

    /**
     * 使用PdfBox将pdf转图片，并拼接成一张
     * 注意：不要用于大型文件的转换！！！
     *
     * @param pdfFilePath pdf文件地址
     * @param outPath     保存路径
     * @param dpi         dpi越大，清晰度越高
     * @return
     */
    public static void pdf2Image(String pdfFilePath, String outPath, int dpi) {
        File file = new File(pdfFilePath);
        if (!file.exists()) {
            logger.info("文件不存在，文件路径【{}】", pdfFilePath);
            return;
        }
        try (PDDocument pdDocument = PDDocument.load(file)) {
            // 将pdf转换成多张图片。
            List<BufferedImage> imageList = new ArrayList<>();
            PDFRenderer renderer = new PDFRenderer(pdDocument);
            int pageCount = pdDocument.getNumberOfPages();
            for (int i = 0; i < pageCount; i++) {
                BufferedImage image = renderer.renderImageWithDPI(i, dpi);
                imageList.add(image);
            }
            // 纵向拼接图片
            if (jointPics(imageList, outPath)) {
                logger.info("pdf转图片成功，保存路径【{}】", outPath);
            } else {
                logger.warn("pdf转图片失败，pdf路径【{}】，保存路径【{}】，dpi【{}】", pdfFilePath, outPath, dpi);
            }

        } catch (IOException e) {
            logger.error("pdf转图片时发生IO异常", e);
        }
    }

    private static boolean jointPics(List<BufferedImage> picList, String outPath) {// 纵向处理图片
        if (picList == null || picList.size() <= 0) {
            logger.info("没有图片，不进行图片拼接");
            return false;
        }
        ByteArrayOutputStream out = null;
        FileOutputStream output = null;
        try {
            int height = 0, // 总高度
                    width = 0, // 总宽度
                    tmpPicHeight = 0, // 单张图片的高度，临时参数
                    picNum = picList.size();// 图片的数量

            int[] heightArray = new int[picNum]; // 保存每个文件的高度
            BufferedImage buffer = null; // 保存图片流
            List<int[]> imgRGB = new ArrayList<>(); // 保存所有的图片的RGB
            int[] tmpPicRGB; // 保存一张图片中的RGB数据
            for (int i = 0; i < picNum; i++) {
                buffer = picList.get(i);
                heightArray[i] = tmpPicHeight = buffer.getHeight();// 图片高度
                if (i == 0) {
                    width = buffer.getWidth();// 图片宽度
                }
                height += tmpPicHeight; // 获取总高度
                tmpPicRGB = new int[width * tmpPicHeight];// 从图片中读取RGB
                tmpPicRGB = buffer
                        .getRGB(0, 0, width, tmpPicHeight, tmpPicRGB, 0, width);
                imgRGB.add(tmpPicRGB);
            }

            int offsetHeight = 0; // 设置偏移高度为0
            // 生成新图片
            BufferedImage imageResult = new BufferedImage(width, height,
                    BufferedImage.TYPE_INT_BGR);
            for (int i = 0; i < picNum; i++) {
                tmpPicHeight = heightArray[i];
                if (i != 0)
                    offsetHeight += heightArray[i - 1]; // 计算偏移高度
                imageResult.setRGB(0, offsetHeight, width, tmpPicHeight, imgRGB.get(i),
                        0, width); // 写入流中
            }
            File outFile = new File(outPath);
            out = new ByteArrayOutputStream();
            String format = FileUtils.getFileExt(outPath);
            ImageIO.write(imageResult, format, out);// 写图片
            byte[] b = out.toByteArray();
            output = new FileOutputStream(outFile);
            output.write(b);

            logger.info("图片拼接成功，保存地址【{}】", outPath);
            return true;
        } catch (Exception e) {
            logger.error("pdf转图片失败", e);
            return false;
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (output != null) {
                    output.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
