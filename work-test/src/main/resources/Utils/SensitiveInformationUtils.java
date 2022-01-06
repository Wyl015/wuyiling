package com.wuyiling.worktest.Utils;

import com.yuuwei.faceview.enums.order.SensitiveInformationEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

@Slf4j
public class SensitiveInformationUtils {
    public static String hideToStar(String content, int type) {
        if (StringUtils.isBlank(content)) {
            return content;
        }
        try {
            String mContent = content.trim();
            String data = "";
            if (type == SensitiveInformationEnum.NAME.getCode()) {
                data = mContent;
                if (mContent.length() == 2) {
                    data = data.substring(0, 1) + "*";
                } else if (mContent.length() > 2) {
                    StringBuilder mark = new StringBuilder();
                    for (int i = 0; i < mContent.length() - 2; i++) {
                        mark.append("*");
                    }
                    data = data.substring(0, 1) + mark.toString() + data.substring(data.length() - 1);
                }
            } else if (type == SensitiveInformationEnum.PHONE.getCode()) {
                data = mContent.replace(mContent.subSequence(3, mContent.length() - 4), "****");
            } else if (type == SensitiveInformationEnum.IDCARD.getCode()) {
                data = mContent.replace(mContent.subSequence(5, mContent.length() - 4), "******");
            } else if (type == SensitiveInformationEnum.BANKCARD.getCode()) {
                data = mContent.replace(mContent.subSequence(5, mContent.length() - 4), "******");
            }
        return data;
        } catch (Exception e) {
            log.error("信息脱敏异常：{}", e);
            return content;
        }
    }
}
