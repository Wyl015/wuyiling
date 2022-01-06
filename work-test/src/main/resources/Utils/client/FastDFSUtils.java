package com.wuyiling.worktest.Utils.client;

import com.yuuwei.faceview.util.common.json.FormatUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * FastDFS相关工具类
 *
 * @author mhw
 * @version v1.0
 * @date 2019-12-26
 */
@Slf4j
public class FastDFSUtils {

    /**
     * 拼接FastDFS拼接结果
     *
     * @param uploadResult 元素0：group_name 元素1：file_name
     * @return
     */
    public static String jointFilePath(String[] uploadResult) {
        if (uploadResult == null || uploadResult.length != 2) {
            log.error("上传结果不符合要求，{}", FormatUtils.obj2str(uploadResult));
            throw new RuntimeException("上传结果不符合要求");
        }
        return uploadResult[0] + "/" + uploadResult[1];
    }

    public static String[] splitFilePath(String fastDFSFilePath) {
        if (StringUtils.isBlank(fastDFSFilePath)) {
            throw new RuntimeException("FastDFS下载路径为空");
        }
        int splitIndex = fastDFSFilePath.indexOf("/");
        String groupName = fastDFSFilePath.substring(0, splitIndex);
        String fileNanem = fastDFSFilePath.substring(splitIndex + 1);
        return new String[]{groupName, fileNanem};
    }
}
