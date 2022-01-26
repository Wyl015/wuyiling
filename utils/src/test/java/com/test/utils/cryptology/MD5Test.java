package com.test.utils.cryptology;

import org.junit.jupiter.api.Test;
import org.springframework.util.DigestUtils;

public class MD5Test {

    @Test
    public void MD5Test() {
        String psd = "Yunxin@54321";
        System.out.println(new String(psd.getBytes()));
        String digestPwd1 = DigestUtils.md5DigestAsHex(psd.getBytes());
        System.out.println(digestPwd1);

    }

    @Test
    public void StringTest() {
        String psd = "\"专项业务远程视频辅助验证\"由中国工商银行贵州省分行合作公司-杭州宇为科技有限公司提供相关服务";
        System.out.println(psd);

    }
}
