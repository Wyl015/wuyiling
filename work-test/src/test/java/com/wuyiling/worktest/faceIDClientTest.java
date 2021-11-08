package com.wuyiling.worktest;

import com.wuyiling.common.util.json.FormatUtils;
import com.wuyiling.worktest.domain.ResponseParam.OcrIdCardResponseParam;
import com.wuyiling.worktest.util.FaceIDClient;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class faceIDClientTest extends BaseSpringTest{
    @Autowired
    private FaceIDClient faceIDClient;

    @Test
    public void ocrIdCard() {
        System.out.println("12");
        OcrIdCardResponseParam result = faceIDClient.ocrIdCard("C:\\Users\\Admin\\Desktop\\ItextpdfTest\\测试身份证反面.jpg");
        log.info(FormatUtils.obj2str(result));
    }
    @Test
    public void ocrIdCard2() {
        System.out.println("12");
        OcrIdCardResponseParam result = faceIDClient.ocrIdCard2("C:\\Users\\Admin\\Desktop\\ItextpdfTest\\测试身份证反面.jpg");
        log.info(FormatUtils.obj2str(result));
    }
}
