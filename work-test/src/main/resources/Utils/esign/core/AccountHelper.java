package com.wuyiling.worktest.Utils.esign.core;

import com.timevale.esign.sdk.tech.bean.*;
import com.timevale.esign.sdk.tech.bean.result.AddAccountResult;
import com.timevale.esign.sdk.tech.bean.result.GetAccountProfileResult;
import com.timevale.esign.sdk.tech.bean.result.Result;
import com.timevale.esign.sdk.tech.impl.constants.LicenseQueryType;
import com.timevale.esign.sdk.tech.service.AccountService;
import com.timevale.esign.sdk.tech.v3.client.ServiceClient;
import com.yuuwei.faceview.exception.EsignException;

import java.text.MessageFormat;

/***
 * @Description: 电子签名_账户辅助类
 * @Team: 公有云技术支持小组
 * @Author: 天云小生
 * @Date: 2018年06月19日
 */
public class AccountHelper {

    /***
     * <ul>
     * <li>方法名称：创建个人账户</li>
     * <li>方法用途：用于创建接口调用方(平台方)的个人客户账户</li>
     * <li>Demo封装方法：doAddAccount</li>
     * <li>SDK接口名称:addAccount</li>
     * </ul>
     *
     * @param serviceClient
     * @param personBean
     * @return
     * @throws EsignException
     */
    public static String doAddAccount(ServiceClient serviceClient, PersonBean personBean) throws EsignException {
        AccountService accountService = serviceClient.accountService();
        AddAccountResult addAccountResult = accountService.addAccount(personBean);
        String accountId = null;
        if (0 != addAccountResult.getErrCode()) {
            String exMsg = MessageFormat.format("创建个人客户账户失败：errCode = {0},msg = {1}",
                    String.valueOf(addAccountResult.getErrCode()), addAccountResult.getMsg());
            throw new EsignException(exMsg);
        } else {
            accountId = addAccountResult.getAccountId();
            System.out.println(
                    MessageFormat.format("创建个人客户账户成功：accountId = {0},可将该AccountId保存到贵司数据库以便日后直接使用.", accountId));
        }
        return accountId;
    }

    /***
     * <ul>
     * <li>方法名称：创建企业账户</li>
     * <li>方法用途：用于创建接口调用方(平台方)的企业客户账户</li>
     * <li>Demo封装方法：doAddAccount</li>
     * <li>SDK接口名称:addAccount</li>
     * </ul>
     *
     * @param serviceClient
     * @param organizeBean
     * @return
     * @throws EsignException
     */
    public static String doAddAccount(ServiceClient serviceClient, OrganizeBean organizeBean) throws EsignException {
        AccountService accountService = serviceClient.accountService();
        AddAccountResult addAccountResult = accountService.addAccount(organizeBean);
        String accountId = null;
        if (0 != addAccountResult.getErrCode()) {
            String exMsg = MessageFormat.format("创建企业客户账户失败：errCode = {0},msg = {1}",
                    String.valueOf(addAccountResult.getErrCode()), addAccountResult.getMsg());
            throw new EsignException(exMsg);
        } else {
            accountId = addAccountResult.getAccountId();
            System.out.println(
                    MessageFormat.format("创建企业客户账户成功：accountId = {0},可将该AccountId保存到贵司数据库以便日后直接使用.", accountId));
        }
        return accountId;
    }

    /***
     * <ul>
     * <li>方法名称：更新个人客户账户信息</li>
     * <li>方法用途：用于更新个人客户账户信息</li>
     * <li>Demo封装方法：doUpdateAccount</li>
     * <li>SDK接口名称:updateAccount</li>
     * </ul>
     *
     * @param serviceClient
     * @param personBean
     * @throws EsignException
     */
    public static void doUpdateAccount(ServiceClient serviceClient, UpdatePersonBean personBean, String accountId)
            throws EsignException {
        AccountService accountService = serviceClient.accountService();
        Result result = accountService.updateAccount(accountId, personBean, null);

        if (0 != result.getErrCode()) {
            String exMsg = MessageFormat.format("更新个人客户账户信息失败：errCode = {0},msg = {1}",
                    String.valueOf(result.getErrCode()), result.getMsg());
            throw new EsignException(exMsg);
        } else {
            System.out.println(MessageFormat.format("更新个人客户账户信息成功,accountId = {0}的账户信息已被更新.", accountId));
        }
    }

    /***
     * <ul>
     * <li>方法名称：更新企业客户账户信息</li>
     * <li>方法用途：用于更新企业客户账户信息</li>
     * <li>Demo封装方法：doUpdateAccount</li>
     * <li>SDK接口名称:updateAccount</li>
     * </ul>
     *
     * @param serviceClient
     * @param organizeBean
     * @throws EsignException
     */
    public static void updateAccount(ServiceClient serviceClient, UpdateOrganizeBean organizeBean, String accountId)
            throws EsignException {
        AccountService accountService = serviceClient.accountService();
        Result result = accountService.updateAccount(accountId, organizeBean, null);

        if (0 != result.getErrCode()) {
            String exMsg = MessageFormat.format("更新企业客户账户信息失败：errCode = {0},msg = {1}",
                    String.valueOf(result.getErrCode()), result.getMsg());
            throw new EsignException(exMsg);
        } else {
            System.out.println(MessageFormat.format("更新企业客户账户信息成功,accountId = {0}的账户信息已被更新.", accountId));
        }
    }

    /***
     * <ul>
     * <li>方法名称：注销账户</li>
     * <li>方法用途：用于注销个人客户账户或企业客户账户</li>
     * <li>Demo封装方法：doDeleteAccount</li>
     * <li>SDK接口名称:deleteAccount</li>
     * </ul>
     *
     * @param serviceClient
     * @param accountId
     * @throws EsignException
     */
    public static void doDeleteAccount(ServiceClient serviceClient, String accountId) throws EsignException {
        AccountService accountService = serviceClient.accountService();
        Result result = accountService.deleteAccount(accountId);

        if (0 != result.getErrCode()) {
            String exMsg = MessageFormat.format("注销账户失败：errCode = {0},msg = {1}", String.valueOf(result.getErrCode()),
                    result.getMsg());
            throw new EsignException(exMsg);
        } else {
            System.out.println(MessageFormat.format("注销账户成功,accountId = {0}的账户已被注销.", accountId));
        }
    }

    /***
     * <li>方法名称：根据证件号获取账户信息</li>
     * <li>方法用途：用于查看AccountId创建时所用的姓名和证件号</li>
     * <li>Demo封装方法：doGetAccountInfoByIdNo</li>
     * <li>SDK接口名称:getAccountInfoByIdNo</li>
     * </ul>
     *
     * @param serviceClient
     * @param idNo
     * @param idNoType
     * @throws EsignException
     */
    public static void doGetAccountInfoByIdNo(ServiceClient serviceClient, String idNo, LicenseQueryType idNoType)
            throws EsignException {
        AccountService accountService = serviceClient.accountService();
        GetAccountProfileResult accountInfoResult = accountService.getAccountInfoByIdNo(idNo, idNoType);

        if (0 != accountInfoResult.getErrCode()) {
            String exMsg = MessageFormat.format("查询账户信息失败：errCode = {0},msg = {1}",
                    String.valueOf(accountInfoResult.getErrCode()), accountInfoResult.getMsg());
            throw new EsignException(exMsg);
        } else {
            System.out.println("查询账号信息成功");
            // 账户详情
            AccountProfile accountProfile = accountInfoResult.getAccountInfo();
            System.out.println(MessageFormat.format("账户标识AccountId = {0},名称 = {1},证件号 = {2},绑定手机号 = {3}",
                    accountProfile.getAccountUid(), accountProfile.getName(), accountProfile.getIdNo(),
                    accountProfile.getMobile()));
        }
    }
}
