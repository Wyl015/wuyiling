package com.wuyiling.worktest.service;

import com.wuyiling.common.FaceViewResult;
import com.wuyiling.worktest.domain.ResponseParam.OcrIdCardResponseParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ApiModel(value = "排队信息")
public class QueueInfoVO {

    @ApiModelProperty(value = "预计等待时间")
    private String expectedWaitTime;

    @ApiModelProperty(value = "排队等待时间")
    private String queueWaitTime;

    @ApiModelProperty(value = "当前排位人数")
    private Long lineNumber;

    @ApiModelProperty(value = "银行工作时间")
    private OcrIdCardResponseParam workTime;

    @ApiModelProperty(value = "坐席人数")
    private Long queueSignerSize;

    @ApiModelProperty(value = "显示信息")
    private String showMsg;

    @ApiModelProperty(value = "用户状态 0正常 1掉线")
    private String status;


    public QueueInfoVO(String expectedWaitTime) {
        this.expectedWaitTime = expectedWaitTime;
    }

    public static void main(String[] args) {
        QueueInfoVO queueInfoVO = new QueueInfoVO("123");
        System.out.println(FaceViewResult.buildSuccess(queueInfoVO));

    }
}
