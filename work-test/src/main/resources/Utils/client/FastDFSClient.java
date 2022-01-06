package com.wuyiling.worktest.Utils.client;

import com.yuuwei.faceview.util.PropertyUtils;
import com.yuuwei.faceview.util.common.json.FormatUtils;
import lombok.extern.slf4j.Slf4j;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * FastDFS工具类
 * todo:连接池
 *
 * @author mhw
 * @version v1.0
 * @date 2019-12-24
 */
@Slf4j
public class FastDFSClient {

    /**
     * 加载配置文件
     */
    static {
        try {
            //1.加载FastDFS客户端的配置文件
            Properties prop = new Properties();
            prop.setProperty("fastdfs.tracker_servers", PropertyUtils.getAppProperty("fastdfs.tracker_server"));
            prop.setProperty("fastdfs.charset", PropertyUtils.getAppProperty("fastdfs.charset"));
            prop.setProperty("fastdfs.connect_timeout_in_seconds", PropertyUtils.getAppProperty("fastdfs.connect_timeout"));
            prop.setProperty("fastdfs.network_timeout_in_seconds", PropertyUtils.getAppProperty("fastdfs.network_timeout"));
            prop.setProperty("fastdfs.http_tracker_http_port", PropertyUtils.getAppProperty("fastdfs.http.tracker_http_port"));
            ClientGlobal.initByProperties(prop);
        } catch (Exception e) {
            log.error("FastDFS 初始化异常！", e);
        }
    }

    public static String[] upload(String localPath, String ext) {
        String[] uploadResults = null;
        TrackerServer trackerServer = null;
        StorageClient storageClient = null;
        try {
            trackerServer = getTrackerServer();
            storageClient = new StorageClient(trackerServer, null);
            uploadResults = storageClient.upload_file(localPath, ext, null);
        } catch (IOException e) {
            log.error("IO异常", e);
            throw new RuntimeException();
        } catch (Exception e) {
            log.error("上传文件到FastDFS异常", e);
            throw new RuntimeException();
        } finally {
            if (trackerServer != null) {
                try {
                    trackerServer.close();
                } catch (Exception e) {
                    log.error("关闭trackerServer异常", e);
                }
            }
        }
        if (uploadResults == null) {
            log.error("上传失败，异常代码：" + storageClient.getErrorCode());
            throw new RuntimeException();
        }
        log.info("上传成功：" + FormatUtils.obj2str(uploadResults));
        return uploadResults;
    }

    /**
     * 上传文件
     *
     * @param content 文件流
     * @param ext     扩展名
     * @return 第一个元素：groupName，第二个元素：remoteFileName
     */
    public static String[] upload(byte[] content, String ext) {
        return upload(content, ext, null);
    }


    /**
     * 上传文件（带元数据）
     * todo:抛出自定义异常
     *
     * @param content 文件流
     * @param ext     扩展名
     * @param metaMap 元数据
     * @return 第一个元素：groupName，第二个元素：remoteFileName
     */
    public static String[] upload(byte[] content, String ext, Map<String, String> metaMap) {
        String[] uploadResults = null;
        TrackerServer trackerServer = null;
        StorageClient storageClient = null;
        NameValuePair[] nameValuePairs = null;
        try {
            trackerServer = getTrackerServer();
            storageClient = new StorageClient(trackerServer, null);
            if (!CollectionUtils.isEmpty(metaMap)) {
                List<NameValuePair> metaList = new ArrayList<>();
                metaMap.forEach((k, v) -> metaList.add(new NameValuePair(k, v)));
                nameValuePairs = metaList.toArray(new NameValuePair[]{});
            }
            uploadResults = storageClient.upload_file(content, ext, nameValuePairs);
        } catch (IOException e) {
            log.error("IO异常", e);
            throw new RuntimeException();
        } catch (Exception e) {
            log.error("上传文件到FastDFS异常", e);
            throw new RuntimeException();
        } finally {
            if (trackerServer != null) {
                try {
                    trackerServer.close();
                } catch (Exception e) {
                    log.error("关闭trackerServer异常", e);
                }

            }
        }
        if (uploadResults == null) {
            log.error("上传失败，异常代码：" + storageClient.getErrorCode());
            throw new RuntimeException();
        }
        log.info("上传成功：" + FormatUtils.obj2str(uploadResults));
        return uploadResults;
    }

    public static FileInfo getFile(String groupName, String remoteFileName) {
        TrackerServer trackerServer = null;
        StorageClient storageClient = null;
        try {
            trackerServer = getTrackerServer();
            storageClient = new StorageClient(trackerServer, null);
            return storageClient.get_file_info(groupName, remoteFileName);
        } catch (Exception e) {
            log.error("Fast DFS获取文件信息异常", e);
            throw new RuntimeException();
        } finally {
            if (trackerServer != null) {
                try {
                    trackerServer.close();
                } catch (Exception e) {
                    log.error("关闭trackerServer异常", e);
                }

            }
        }
    }

    public static void downFile(String groupName, String remoteFileName, String savePath) {
        TrackerServer trackerServer = null;
        StorageClient storageClient = null;
        try {
            trackerServer = getTrackerServer();
            storageClient = new StorageClient(trackerServer, null);
            log.info("groupName为："+groupName);
            log.info("remoteFileName为："+remoteFileName);
            storageClient.download_file(groupName, remoteFileName, savePath);
        } catch (Exception e) {
            log.error("Fast DFS获取文件异常【{}】", e);
            throw new RuntimeException();
        } finally {
            if (trackerServer != null) {
                try {
                    trackerServer.close();
                } catch (Exception e) {
                    log.error("关闭trackerServer异常", e);
                }

            }
        }
    }

    private static TrackerServer getTrackerServer() throws IOException {
        TrackerClient trackerClient = new TrackerClient();
        return trackerClient.getConnection();
    }
}
