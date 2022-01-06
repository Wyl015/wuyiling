package com.wuyiling.worktest.Utils;

/**
 * 面签单状态工具类
 * @author thf
 * @date 2017-12-27
 * @version v1.2
 */
public class FaceViewStatusUtils {

    /**
     * 根据状态返回对应的中文释义
     * @param status
     * @param orderStatus 订单状态
     * @return
     */
    public static String getStatusName(int status, int orderStatus) {
        if (orderStatus == 3) {
            return "待审核";
        } else if (orderStatus == 4) {
            return "已审核";
        } else if (orderStatus == 5) {
            return "待批准";
        }
        switch (status) {
            case 0 : return "取消面签";
            case 1 : return "待面签";
            case 3 : return "面签成功";
            case 4 : return "面签失败";
            case 5 : return "排队中";
            case 6 : return "待排队";
            case 8 : return "待接单";
            case 9 : return "预约成功";
            case 10 : return "预约失败";
            case 11 : return "取消预约";
            case 13 : return "面签过期";
            case 14 : return "待上传";
            case 15 : return "待审核";
            default : return "";
        }
    }

    /**
     * 根据状态返回对应的中文释义
     * @param orderStatus 订单状态
     * @return
     */
    public static String getOrderName(int orderStatus) {
        switch (orderStatus) {
            case 1 : return "录单中";
            case 2 : return "待面签";
            case 3 : return "审核中";
            case 4 : return "审核完成";
            case 5 : return "待批准";
            default : return "";
        }
    }

    public static String getStatusMean(int status) {
        if (status == 3) {
            return "已面签";
        } else if (status == 14) {
            return "待上传";
        } else if (status == 15){
            return "待审核";
        } else {
            return "未面签";
        }
    }



}
