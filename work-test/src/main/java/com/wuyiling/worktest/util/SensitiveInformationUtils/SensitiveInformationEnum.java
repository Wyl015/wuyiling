package com.wuyiling.worktest.util.SensitiveInformationUtils;

public enum SensitiveInformationEnum {
    /**
     * @Author
     * @Description 用于信息脱敏
     **/
    PHONE(1, "手机号"),
    NAME(2, "姓名"),
    IDCARD(3, "身份证号"),
    BANKCARD(4, "银行卡号");

    private int code;
    private String des;

    SensitiveInformationEnum(int code, String des) {
        this.code = code;
        this.des = des;
    }

    public int getCode() {
        return code;
    }

    public String getDes() {
        return des;
    }
}
