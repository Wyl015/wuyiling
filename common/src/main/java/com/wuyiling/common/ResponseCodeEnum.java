package com.wuyiling.common;

public enum ResponseCodeEnum {

    /**
     * 系统状态码
     */
    SUCCESS(200, "成功"),
    FAIL_PWD_OVER_LIMIT(401, "错误次数超限,请联系管理员"),
    TOKEN_TIMEOUT(403, "用户信息过期，请重新登录"),
    OPERATE_ERROR(500, "操作异常，请重新再试，或联系管理员!"),
    THIRD_PARTY_ERROR(1001, "第三方调用错误"),
    SERVER_ERROR(1001, "服务器开小差了，请联系管理员"),
    REPEAT_SUBMIT_ERROR(1002, "请勿重复提交"),
    LOGIN_NUMBER_ERROR(1003, "登录错误次数超限,请联系管理员"),
    LOGIN_IP_LIMIT_ERROR(1004, "ip限制，不允许登录"),
    LOGIN_NOT_YW_ERROR(1005, "非业务员登录，无法登录"),
    ILLEGALITY_ERROR(1006, "非法请求"),
    VERIFY_CODE_INVALID(1007,"验证码已过期,请重新获取验证码"),
    SM2_ERROR(1008,"国密工具异常"),
    FACE_ID_ERROR(1009,"调用FaceID进行OCR识别IO异常"),
    ;
    /**
     * 响应码
     */
    private int code;
    /**
     * 响应信息
     */
    private String msg;

    ResponseCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }


}
