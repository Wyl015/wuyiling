package com.test.utils.cryptology;

import org.junit.jupiter.api.Test;
import org.springframework.util.DigestUtils;

public class MD5Test {

    @Test
    public void MD5Test() {
        String psd = "Aa@123456";
        System.out.println(new String(psd.getBytes()));
        String digestPwd1 = DigestUtils.md5DigestAsHex(psd.getBytes());
        System.out.println(digestPwd1);

    }
}
