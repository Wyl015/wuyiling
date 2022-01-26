package com.wuyiling.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 面签系统自定义响应结构
 *
 * @author 陶海峰
 * @date 2017-11-02
 */
@Data
@ApiModel(value = "响应信息")
public class FaceViewResult<T> {

    @ApiModelProperty(value = "响应状态", position = 1)
    private Integer code;

    @ApiModelProperty(value = "请求响应消息", position = 2)
    private String errorMsg;

    @ApiModelProperty(value = "响应数据", position = 3)
    private T data;

    public FaceViewResult() {
    }

    public FaceViewResult(Integer code, String errorMsg, T data) {
        this.code = code;
        this.errorMsg = errorMsg;
        this.data = data;
    }

    /**
     * 响应成功
     *
     * @return
     */
    public static FaceViewResult buildSuccess() {
        return buildSuccess(null);
    }

    /**
     * 响应成功
     *
     * @param data 响应数据
     * @return
     */
    public static <T> FaceViewResult<T> buildSuccess(T data) {
        FaceViewResult<T> result = new FaceViewResult<>();
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
    public static <T> FaceViewResult<T> buildFail(Integer status, String errorMsg) {
        FaceViewResult<T> result = new FaceViewResult<>();
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
    public static <T> FaceViewResult<T> buildFail(ResponseCodeEnum responseCodeEnum) {
        FaceViewResult<T> result = new FaceViewResult<>();
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
