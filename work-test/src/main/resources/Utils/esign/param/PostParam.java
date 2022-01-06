package com.wuyiling.worktest.Utils.esign.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: lingjun.jlj
 * @date: 2019/4/1 0001 15:21
 * @description: 合同签署关键字以及签章位置坐标
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostParam {

    /**
     * 关键字
     */
    String key;
    /**
     * 签署位置X坐标,默认值为0,以pdf页面的左下角作为原点,控制距离页面左端的横向移动距离,单位为px
     */
    float posX;
    /**
     * 签署位置Y坐标,默认值为0,以pdf页面的左下角作为原点,控制距离页面底端的纵向移动距离,单位为px
     */
    float posY;
    /**
     * 印章图片在PDF文件中的等比缩放大小,公章标准大小为4.2厘米即159px
     */
    float widthScaling;
    /**
     * 页面页数
     */
    private String page;

    public PostParam(String key, float posX, float posY, float widthScaling) {
        this.key = key;
        this.posX = posX;
        this.posY = posY;
        this.widthScaling = widthScaling;
    }
}
