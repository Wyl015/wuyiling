package com.wuyiling.worktest.Utils.sms;

import com.yuuwei.faceview.constant.SystemConsts;
import com.yuuwei.faceview.enums.http.ResponseCodeEnum;
import com.yuuwei.faceview.exception.GlobalFaceSignException;
import com.yuuwei.faceview.tp.constant.SystemConstants;
import com.yuuwei.faceview.util.ProxyHttpClientPoolUtils;
import com.yuuwei.faceview.util.common.json.FormatUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 手机短信服务
 *
 * @author gml
 * @version v1.0.0
 * @date 2019-12-25
 */
@Slf4j
public class SmsClient {

    /**
     * 发送短信
     *
     * @param mobile  手机号
     * @param message 短信消息
     * @return
     */
    public static void sendSms(String mobile, String message, String templateId) {
        try {
            //HTTP代理
            CloseableHttpClient httpClient = ProxyHttpClientPoolUtils.httpClient;
            HttpPost httpPost = new HttpPost(SystemConsts.MOBILE_PUSH_URL);
            httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");
            List<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("mobile", mobile));
            nameValuePairs.add(new BasicNameValuePair("tpl_id", templateId));
            if (!StringUtils.isBlank(message)) {
                //根据模板id增加判断
                if(Integer.parseInt(templateId)>235931){
                    nameValuePairs.add(new BasicNameValuePair("tpl_value", message));
                }else {
                    nameValuePairs.add(new BasicNameValuePair("tpl_value",URLEncoder.encode(message, SystemConstants.DEFAULT_CHARSET)));
                }
            }
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, SystemConstants.DEFAULT_CHARSET));
            CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
            if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                throw new GlobalFaceSignException(ResponseCodeEnum.NETWORK_ERROR);
            }
            String response = EntityUtils.toString(httpResponse.getEntity(), SystemConstants.DEFAULT_CHARSET);
            log.info("短信推送的返回消息：{}", response);
            // 短信返回的内容中，包含非法字符，需要替换掉
            if (response.contains("\uFEFF")){
                response = response.replace("\uFEFF","");
            }
            int returnCode = FormatUtils.childInt(response, "returnCode");
            if (!Objects.equals(returnCode, SystemConsts.SUCCESS_CODE)) {
                throw new GlobalFaceSignException(ResponseCodeEnum.MESSAGE_SENDING_ERROR);
            }
        } catch (IOException e) {
            log.error("向手机号：{}，发送消息IO异常：{}", mobile, message, e);
            throw new GlobalFaceSignException(ResponseCodeEnum.OPERATE_ERROR);
        }
    }

    public static void main(String[] args) {
        String verifyCode = String.valueOf(ThreadLocalRandom.current().nextInt(899999) + 100000);
        String message = "#name#=" + verifyCode;
//        sendSms("17826852173", message, SystemConsts.WEB_SEND_CODE_TEMPLATE);
        String response = "\uFEFF{\"returnCode\":0,\"returnInfo\":\"\\u53d1\\u9001\\u6210\\u529f\"}";
        if (response.contains("\uFEFF")){
            response = response.replace("\uFEFF","");
        }
        int returnCode = FormatUtils.childInt(response, "returnCode");
        System.out.println(returnCode);
    }
}

