package com.wuyiling.worktest.Utils;

import com.yuuwei.faceview.constant.SystemConsts;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;

/**
 * @author mhw
 * @version v1.0
 * @date 2019-07-12
 */
@Slf4j
public class IPUtils {

    public static String getRealIp(HttpServletRequest request) {
        String realIp = request.getHeader(SystemConsts.X_FORWARDED_FOR);
        if (StringUtils.isBlank(realIp) || StringUtils.equalsIgnoreCase(SystemConsts.IP_UNKNOWN, realIp)) {
            realIp = request.getHeader(SystemConsts.X_REAL_IP);
        }
        if (StringUtils.isBlank(realIp) || StringUtils.equalsIgnoreCase(SystemConsts.IP_UNKNOWN, realIp)) {
            realIp = request.getRemoteAddr();
        }
        if (realIp.contains(",")) {
            return realIp.split(",")[0];
        } else {
            return realIp;
        }
    }

    public static String getLocalAddress() {
        String ipSwitch = com.yuuwei.faceview.util.PropertyUtils.getAppProperty("ip.switch");
        // 返回手动配置的ip
        if (StringUtils.equals(ipSwitch, "0")) {
            String ip = System.getenv("LOCAL_IP");
            log.info("获取到手动配置的ip：{}", ip);
            return ip;
        }
        // 自动获取ip
        if (StringUtils.equals(ipSwitch, "1")) {
            return getLocalAddressByQuery();
        }

        log.error("异常配置");
        return "127.0.0.1";
    }

    private static String getLocalAddressByQuery() {
        try {
            ArrayList<String> localIpAddrList = getLocalIpAddrList();
            if (CollectionUtils.isEmpty(localIpAddrList)) {
                log.error("未能获取到ip");
                return "127.0.0.1";
            }
            String ip = localIpAddrList.get(0);
            log.info("获取到ip：{}", ip);
            return ip;
        } catch (Exception e) {
            log.error("获取本地ip失败，返回默认数据127.0.0.1");
            return "127.0.0.1";
        }
    }

    private static ArrayList<String> getLocalIpAddrList() {
        ArrayList<String> ipList = new ArrayList<>();
        try {
            Enumeration interfaces= NetworkInterface.getNetworkInterfaces();
            while(interfaces.hasMoreElements()) {
                NetworkInterface ni=(NetworkInterface)interfaces.nextElement();
                Enumeration ipAddrEnum = ni.getInetAddresses();
                while(ipAddrEnum.hasMoreElements()) {
                    InetAddress addr = (InetAddress)ipAddrEnum.nextElement();
                    if (addr.isLoopbackAddress()) {
                        continue;
                    }

                    String ip = addr.getHostAddress();
                    if (ip.contains(":")) {
                        //skip the IPv6 addr
                        continue;
                    }

                    log.debug("Interface: " + ni.getName()
                            + ", IP: " + ip);
                    ipList.add(ip);
                }
            }

            Collections.sort(ipList);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Failed to get local ip list. " + e.getMessage());
            throw new RuntimeException("Failed to get local ip list");
        }

        return ipList;
    }

}
