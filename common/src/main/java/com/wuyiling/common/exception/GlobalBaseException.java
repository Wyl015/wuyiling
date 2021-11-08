package com.wuyiling.common.exception;

import com.wuyiling.common.ResponseCodeEnum;

public class GlobalBaseException extends RuntimeException {
    private Integer code;

    public GlobalBaseException(ResponseCodeEnum response) {
        super(response.getMsg());
        this.code = response.getCode();
    }

    public GlobalBaseException(ResponseCodeEnum response, String message) {
        super(message);
        this.code = response.getCode();
    }

    public GlobalBaseException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
