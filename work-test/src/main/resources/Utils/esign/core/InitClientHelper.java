package com.wuyiling.worktest.Utils.esign.core;


import com.timevale.esign.sdk.tech.bean.result.Result;
import com.timevale.esign.sdk.tech.v3.client.ServiceClient;
import com.timevale.esign.sdk.tech.v3.client.ServiceClientManager;
import com.timevale.tech.sdk.bean.HttpConnectionConfig;
import com.timevale.tech.sdk.bean.ProjectConfig;
import com.timevale.tech.sdk.bean.SignatureConfig;
import com.timevale.tech.sdk.constants.AlgorithmType;
import com.yuuwei.faceview.exception.EsignException;
import com.yuuwei.faceview.util.esign.constant.EsignConfig;
import lombok.extern.slf4j.Slf4j;

import java.text.MessageFormat;

/***
 * @Description: 电子签名_初始化客户端辅助类
 * @Team: 公有云技术支持小组
 * @Author: 天云小生
 * @Date: 2018年06月19日
 */
@Slf4j
public class InitClientHelper {

    /***
     * <ul>
     * <li>方法名称：注册客户端</li>
     * <li>方法用途：用于获取SDK可调用的相关接口</li>
     * <li>Demo封装方法：doRegistClient</li>
     * <li>SDK接口名称:registClient</li>
     * </ul>
     *
     * @throws EsignException
     */
    public static void doRegisterClient(String projectId, String projectSecret, String host) throws EsignException {
        log.info("e签宝注册信息：project_id:{},project_secret{},host:{}", projectId, projectSecret, host);
        String api_url = host + "/tgmonitor/rest/app!getAPIInfo2";
        ProjectConfig proCfg = new ProjectConfig();
        // 项目ID(应用ID)
        proCfg.setProjectId(projectId);
        // 项目Secret(应用Secret)
        proCfg.setProjectSecret(projectSecret);
        // 开放平台地址
        proCfg.setItsmApiUrl(api_url);

        HttpConnectionConfig httpConCfg = new HttpConnectionConfig();
        // 协议类型
        httpConCfg.setHttpType(EsignConfig.HTTP_TYPE);
        // 请求失败重试次数，默认5次
        httpConCfg.setRetry(5);
        // 代理服务IP地址
        // httpConCfg.setProxyIp(null);
        // 代理服务端口
        // httpConCfg.setProxyPort(0);
        // 代理服务器用户名
        // httpConCfg.setUsername(null);
        // 代理服务器用户密码
        // httpConCfg.setPassword(null);

        SignatureConfig signCfg = new SignatureConfig();
        // 算法类型
        signCfg.setAlgorithm(EsignConfig.ALGORITHM_TYPE);
        // 如果算法类型为RSA,则需要设置e签宝公钥和平台私钥
        if (AlgorithmType.RSA == EsignConfig.ALGORITHM_TYPE) {
            System.out.println("RSA");
            // e签宝公钥，可以从开放平台获取。若算法类型为RSA，此项必填
            signCfg.setEsignPublicKey(EsignConfig.ESIGN_PUBLIC_KEY);
            // 平台私钥，可以从开放平台下载密钥生成工具生成。若算法类型为RSA，此项必填
            signCfg.setPrivateKey(EsignConfig.PRIVATE_KEY);
        }
        // 注册客户端
        Result result = ServiceClientManager.registClient(proCfg, httpConCfg, signCfg);
        if (0 != result.getErrCode()) {
            String exMsg = MessageFormat.format("注册[{0}]的客户端失败: errCode = {1},msg = {2}", projectId,
                    String.valueOf(result.getErrCode()), result.getMsg());
            throw new EsignException(exMsg);
        } else {
            System.out.println(MessageFormat.format("注册[{0}]的客户端成功: errCode = {1},msg = {2}", projectId,
                    String.valueOf(result.getErrCode()), result.getMsg()));
        }
    }

    /***
     * <ul>
     * <li>方法名称：获取客户端</li>
     * <li>方法用途：获取已初始化客户端，通过客户端可获取SDK提供的各种电子签名服务</li>
     * <li>Demo封装方法：doGetServiceClient</li>
     * <li>SDK接口名称:ServiceClientManager.get(String projectId)</li>
     * </ul>
     *
     * @throws EsignException
     */
    public static ServiceClient doGetServiceClient(String projectId) throws EsignException {
        ServiceClient serviceClient = ServiceClientManager.get(projectId);
        if (null == serviceClient) {
            String exMsg = MessageFormat.format("ServiceClient为空,获取[{0}]的客户端失败,请重新注册客户端", projectId);
            throw new EsignException(exMsg);
        } else {
            System.out.println(MessageFormat.format("获取[{0}]的客户端成功", projectId));
        }
        return serviceClient;
    }

    /***
     *
     * <ul>
     * <li>方法名称：关闭客户端</li>
     * <li>方法用途：程序结束时,需关闭已初始化客户端</li>
     * <li>Demo封装方法：doShutdownServiceClient</li>
     * <li>SDK接口名称:ServiceClientManager.shutdown(String projectId)</li>
     * </ul>
     * @throws EsignException
     */
    public static void doShutdownServiceClient(String projectId) throws EsignException {
        try {
            ServiceClientManager.shutdown(projectId);
            ServiceClient serviceClient = ServiceClientManager.get(projectId);
            if (null != serviceClient) {
                String exMsg = MessageFormat.format("关闭[{0}]的客户端失败,请检查原因", projectId);
                throw new EsignException(exMsg);
            } else {
                System.out.println(MessageFormat.format("[{0}]的客户端关闭成功", projectId));
            }
        } catch (NullPointerException e) {
            String exMsg = MessageFormat.format("[{0}]的客户端关闭异常,请检查[{0}]的客户端是否注册成功或已关闭", projectId);
            throw new EsignException(exMsg);
        }

    }
}
