package com.wuyiling.worktest;

import com.wuyiling.worktest.util.SensitiveInformationUtils.SensitiveInformationEnum;
import com.wuyiling.worktest.util.SensitiveInformationUtils.SensitiveInformationUtils;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class WorkTestApplicationTests {

    @Test
    void contextLoads() {
        String BANK_REMINDER = "\"专项业务远程视频辅助验证\"由中国工商银行贵州省分行合作公司-杭州宇为科技有限公司提供相关服务";
        BANK_REMINDER.replaceFirst("province","贵州省" );
        System.out.println(BANK_REMINDER.replaceFirst("province","贵州省" ));
        System.out.println(BANK_REMINDER.replaceFirst("贵州省","province" ));
    }
    @Test
    void SensitiveInformationUtilsTest(){
        String name = "测试数";
        testSample testSample = new testSample();
        testSample.setNAME(name.toString());
        System.out.println("=============");
        System.out.println(testSample);
        String result = SensitiveInformationUtils.hideToStar(testSample.getPHONE(), SensitiveInformationEnum.PHONE.getCode(), true);
        System.out.println(result);
         result = SensitiveInformationUtils.hideToStar(testSample.getNAME(), SensitiveInformationEnum.NAME.getCode(), true);
        System.out.println(result);
         result = SensitiveInformationUtils.hideToStar(testSample.getIDCARD(), SensitiveInformationEnum.IDCARD.getCode(), true);
        System.out.println(result);
         result = SensitiveInformationUtils.hideToStar(testSample.getBANKCARD(), SensitiveInformationEnum.BANKCARD.getCode(), true);
        System.out.println(result);

    }

    @Data
    private class testSample {
        String PHONE = "12345678912";
        String NAME = "测试员";
        String IDCARD = "223541026984552266448";
        String BANKCARD = "123456789123456789";
    }


    @Test
    public void testleetcode() {
        int[] a = new int[16];
    }

}
