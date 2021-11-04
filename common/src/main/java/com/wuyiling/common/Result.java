package com.wuyiling.common;


import io.swagger.annotations.ApiModelProperty;

public class Result<T> {

    @ApiModelProperty(value = "响应状态", position = 1)
    private Integer code;

    @ApiModelProperty(value = "请求响应消息", position = 2)
    private String errorMsg;

    @ApiModelProperty(value = "响应数据", position = 3)
    private T data;

    public Result() {
    }

    public Result(Integer code, String errorMsg, T data) {
        this.code = code;
        this.errorMsg = errorMsg;
        this.data = data;
    }


    /**
     * 响应成功
     *
     * @return
     */
    public static Result buildSuccess() {
        return buildSuccess(null);
    }

    /**
     * 响应成功
     *
     * @param data 响应数据
     * @return
     */
    public static <T> Result<T> buildSuccess(T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setErrorMsg("");
        result.setData(data);
        return result;
    }

    /**
     * 响应失败
     *
     * @param status   响应码
     * @param errorMsg 响应的错误消息
     * @return
     */
    public static <T> Result<T> buildFail(Integer status, String errorMsg) {
        Result<T> result = new Result<>();
        result.setCode(status);
        result.setErrorMsg(errorMsg);
        result.setData(null);
        return result;
    }

    /**
     * 响应失败
     *
     * @param responseCodeEnum 响应枚举
     * @return
     */
    public static <T> Result<T> buildFail(ResponseCodeEnum responseCodeEnum) {
        Result<T> result = new Result<>();
        result.setCode(responseCodeEnum.getCode());
        result.setErrorMsg(responseCodeEnum.getMsg());
        result.setData(null);
        return result;
    }


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
