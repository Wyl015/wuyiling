package com.wuyiling.worktest.domain.ResponseParam;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OcrIdCardResponseParam {
    /**
     * @Description 返回请求时的交易流水号
     **/
    @JsonProperty(value = "Seqno")
    private String seqno;
    /**
     * @Description YYYYMMDD，当前时间
     **/
    @JsonProperty(value = "TransDate")
    private String transDate;
    /**
     * @Description HHMMSS，当前时间
     **/
    @JsonProperty(value = "TransTime")
    private String transTime;
    /**
     * @Description 0成功，其余失败
     **/
    @JsonProperty(value = "returen_code")
    private String returenCode;
    /**
     * @Description 交易结果说明
     **/
    @JsonProperty(value = "returen_msg")
    private String returenMsg;
    /**
     * @Description 场景编号，识别身份证正面为2，识别身份证背面为3
     **/
    @JsonProperty(value = "type")
    private String type;
    /**
     * @Description 备用字段，用于项目上特殊要求
     **/
    @JsonProperty(value = "Bak1")
    private String bak1;


    /**
     * @Description 备用字段，用于项目上特殊要求
     **/
    @JsonProperty(value = "result")
    private String result;
}
