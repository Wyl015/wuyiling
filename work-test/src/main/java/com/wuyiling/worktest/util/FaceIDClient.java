package com.wuyiling.worktest.util;

import com.wuyiling.common.ResponseCodeEnum;
import com.wuyiling.common.exception.GlobalBaseException;
import com.wuyiling.common.util.HttpClientPoolUtils;
import com.wuyiling.common.util.TimeUtils;
import com.wuyiling.common.util.json.FormatUtils;
import com.wuyiling.worktest.domain.ResponseParam.OcrIdCardResponseParam;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import sun.misc.BASE64Encoder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Objects;

/**
 * @author: lingjun.jlj
 * @date: 2019/5/22 16:07
 * @description:
 */
@Component
public class FaceIDClient {

    private static final Logger logger = LoggerFactory.getLogger(com.wuyiling.worktest.util.FaceIDClient.class);

    /**
     * 检测和识别中华人民共和国第二代身份证
     *
     * @param image 图片地址
     * @return 查询的客户身份证信息
     */
    public OcrIdCardResponseParam ocrIdCard(String image) {
        System.out.println("123");
        logger.info("faceID进行OCR识别身份证信息开始,源图片路径:{}", image);
        CloseableHttpClient httpClient = HttpClientPoolUtils.httpClient;
        HttpPost httpPost = new HttpPost("http://IP:PORT/IdRecognition");
        HttpEntity reqEntity = MultipartEntityBuilder.create()
                .addTextBody("TransDate", TimeUtils.parseDateToString(new Date(), "YYYYMMDD"))
                .addTextBody("TransTime", TimeUtils.parseDateToString(new Date(), "HHmmss"))
                .addTextBody("SeqNo", "123456789")
                .addTextBody("caseId", "2")
                .addTextBody("imageBase64", imagetoBase64(image))
                .addTextBody("suffix", "jpg")
                .addTextBody("Bak1", "")
                .build();
        httpPost.setEntity(reqEntity);

        try (CloseableHttpResponse httpResponse = httpClient.execute(httpPost)) {
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            String response = EntityUtils.toString(httpResponse.getEntity(),"UTF-8");
            if (statusCode != HttpStatus.SC_OK) {
                logger.error("FaceID进行OCR识别失败，响应码为：{}, 错误提示：{}", statusCode, response);
                throw new Exception("sd");
            }
            OcrIdCardResponseParam responseParam = FormatUtils.str2obj(response, OcrIdCardResponseParam.class);
            if (Objects.equals(responseParam.getReturenCode(), 0)) {
                logger.info("FaceID 识别出错，识别结果：{}", response);
            }
            return responseParam;
        } catch (Exception e) {
            logger.error("调用FaceID进行OCR识别IO异常:{}", e.getMessage(), e);
            throw new GlobalBaseException(ResponseCodeEnum.FACE_ID_ERROR);
        }
    }

//原来的模板
    public OcrIdCardResponseParam ocrIdCard2(String image) {
        logger.info("faceID进行OCR识别身份证信息开始,源图片路径:{}", image);
        CloseableHttpClient httpClient = HttpClientPoolUtils.httpClient;
        HttpPost httpPost = new HttpPost("https://api.megvii.com" + "/faceid/v3/ocridcard");
        HttpEntity reqEntity = MultipartEntityBuilder.create()
                .addTextBody("api_key", "TlsfzLo6Dkd9rb7sSnG_1G32lwgmhQZS")
                .addTextBody("api_secret", "1bTtvfCwvFbOKtiznhV4BPSnhp3M31iO")
                .addTextBody("return_portrait", "1")
                .addBinaryBody("image", new File(image))
                .build();
        httpPost.setEntity(reqEntity);
        try (CloseableHttpResponse httpResponse = httpClient.execute(httpPost)) {
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            String response = EntityUtils.toString(httpResponse.getEntity(),"UTF-8");
            if (statusCode != HttpStatus.SC_OK) {
                logger.error("FaceID进行OCR识别失败，响应码为：{}, 错误提示：{}", statusCode, response);
                throw new GlobalBaseException(ResponseCodeEnum.FACE_ID_ERROR);
            }
            OcrIdCardResponseParam responseParam = FormatUtils.str2obj(response, OcrIdCardResponseParam.class);
            if (Objects.equals(responseParam.getResult(), "2")) {
                logger.info("FaceID 识别出错，识别结果：{}", response);
            }
            return responseParam;
        } catch (IOException e) {
            logger.error("调用FaceID进行OCR识别IO异常:{}", e.getMessage(), e);
            throw new GlobalBaseException(ResponseCodeEnum.FACE_ID_ERROR);
        }
    }

    //将图片Base64编码
    public String imagetoBase64(String imgFile) {
        InputStream in = null;
        byte[] data = null;
        //读取图片字节数组
        try
        {
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        //对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        String result = encoder.encode(data);
        return result;
    }

}
