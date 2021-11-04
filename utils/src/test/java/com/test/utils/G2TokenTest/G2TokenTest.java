package com.test.utils.G2TokenTest;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class G2TokenTest {

    public static void main(String[] args) throws Exception{
        DefaultHttpClient httpClient = new DefaultHttpClient();
        String url = "https://api.netease.im/nimserver/user/getToken.action";
        HttpPost httpPost = new HttpPost(url);

        String appKey = "4ea930ab3d6fb1f3613e9877332c9dd9";
        String appSecret = "0ebb47e3d455";
        String nonce =  "12345";
        String curTime = String.valueOf((new Date()).getTime() / 1000L);
        String checkSum = CheckSumBuilder.getCheckSum(appSecret, nonce ,curTime);///checkSum的计算方式请参考《服务端API-调用方式-请求结构》

        // 设置请求的header
        httpPost.addHeader("AppKey", appKey);
        httpPost.addHeader("Nonce", nonce);
        httpPost.addHeader("CurTime", curTime);
        httpPost.addHeader("CheckSum", checkSum);
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        // 设置请求的参数
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();

        //==用户 ID
        nvps.add(new BasicNameValuePair("uid", "1234567"));
        //==绑定的房间名称。未指定 channelName 时，获取的 Token 可以用于加入任何房间
        nvps.add(new BasicNameValuePair("channelName", "123456"));
        //==在 Token 有效期内该用户是否可以多次使用该 Token，默认为 true。true：有效期内可多次使用。false：有效期内只能使用一次。
        nvps.add(new BasicNameValuePair("repeatUse", "true"));
        //==NERTC Token 的过期时间，过期后，该用户将无法通过此 Token 加入房间。取值范围为 1~86400 秒，默认为 600 秒。
        //==当天24：00过期
        nvps.add(new BasicNameValuePair("expireAt"
                , String.valueOf(Duration.between(LocalDateTime.now(), LocalDateTime.of(LocalDate.now(), LocalTime.MAX)).toMillis()/1000L)));

        httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));

        // 执行请求
        HttpResponse response = httpClient.execute(httpPost);

        // 打印执行结果
        System.out.println(EntityUtils.toString(response.getEntity(), "utf-8"));
    }



    private static Long timeGap() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 16);
        cal.set(Calendar.SECOND, 50);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return  (cal.getTimeInMillis() - System.currentTimeMillis()) / 1000l;
    }


    @Test
    public void random() {
        Random random = new Random(14);
        System.out.println(random.nextInt(900000) + 99999);
    }


    @Test
    public void timeTest() {

        String curTime = String.valueOf((new Date()).getTime());
        System.out.println(curTime);
        Calendar cal = Calendar.getInstance(Locale.CHINA);
        System.out.println(System.currentTimeMillis());
        cal.set(Calendar.HOUR_OF_DAY, 24);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        System.out.println(cal.getTimeInMillis());
        System.out.println((cal.getTimeInMillis() - System.currentTimeMillis())/1000L/3600L);

    }

    @Test
    public void timeTest2() {
        long l1 = Duration.between(LocalDateTime.of(LocalDate.now(), LocalTime.MIN), LocalDateTime.of(LocalDate.now(), LocalTime.MAX)).toMillis();
        System.out.println(l1);
        long l2 = Duration.between(LocalDateTime.now(), LocalDateTime.of(LocalDate.now(), LocalTime.MAX)).toMillis();
        System.out.println(l2);
    }
}
