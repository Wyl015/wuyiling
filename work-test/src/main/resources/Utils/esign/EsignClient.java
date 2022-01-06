package com.wuyiling.worktest.Utils.esign;

import com.timevale.esign.sdk.tech.bean.PersonBean;
import com.timevale.esign.sdk.tech.bean.PosBean;
import com.timevale.esign.sdk.tech.bean.SignPDFStreamBean;
import com.timevale.esign.sdk.tech.bean.result.FileDigestSignResult;
import com.timevale.esign.sdk.tech.bean.seal.PersonTemplateType;
import com.timevale.esign.sdk.tech.bean.seal.SealColor;
import com.timevale.esign.sdk.tech.impl.constants.LegalAreaType;
import com.timevale.esign.sdk.tech.impl.constants.SignType;
import com.timevale.esign.sdk.tech.v3.client.ServiceClient;
import com.yuuwei.faceview.exception.EsignException;
import com.yuuwei.faceview.util.esign.constant.EsignConfig;
import com.yuuwei.faceview.util.esign.core.*;
import com.yuuwei.faceview.util.esign.param.PostParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: lingjun.jlj
 * @date: 2019-03-28 20:43
 * @description: e签宝
 */
@Slf4j
public class EsignClient {

    /***
     * 创建个人客户账户
     * @param serviceClient e签宝客户端服务
     * @param username 个人姓名
     * @param idNo 身份证号码
     *
     * @throws EsignException
     */
    public String addPersonAccount(ServiceClient serviceClient, String username, String idNo) throws EsignException {

        PersonBean personBean = new PersonBean();
        // 姓名
        personBean.setName(username);
        // 证件号码
        personBean.setIdNo(idNo);
        // 用于接收签署验证码的手机号码,可空
        // personBean.setMobile("");

        // 个人归属地：
        // MAINLAND-大陆身份证|HONGKONG-香港居民来往内地通行证|MACAO-澳门居民来往内地通行证|TAIWAN-台湾居民来往大陆通行证
        // PASSPORT-中国护照|FOREIGN-外籍证件|OTHER-其他
        personBean.setPersonArea(LegalAreaType.MAINLAND);

        //个人客户账户AccountId
        String personAccountId = AccountHelper.doAddAccount(serviceClient, personBean);
        return personAccountId;
    }

    /***
     * 创建个人客户模板印章
     * @param serviceClient 客户端服务
     * @param accountId e签宝个人账户id
     *
     * @throws EsignException
     */
    public String addPersonTemplateSeal(ServiceClient serviceClient, String accountId) throws EsignException {

        // 印章模板类型,可选SQUARE-正方形印章 | RECTANGLE-矩形印章 | BORDERLESS-无框矩形印章
        PersonTemplateType personTemplateType = PersonTemplateType.BORDERLESS;

        // 印章颜色：RED-红色 | BLUE-蓝色 | BLACK-黑色
        SealColor sealColor = SealColor.RED;

        // 个人模板印章SealData
        String personSealData = SealHelper.doAddTemplateSeal(serviceClient, accountId, personTemplateType, sealColor);
        return personSealData;
    }

    /***
     * <ul>
     * <li>方法名称：个人客户进行合同签署</li>
     * <li>文件方式：本地文件路径</li>
     * <li>Demo封装方法：doSign_Person</li>
     * </ul>
     *
     * @param serviceClient  e签宝客户端服务
     * @param personAccountId e签宝个人账户id
     * @param personSealData e签宝个人签章
     * @param srcPdfPath 待签署PDF文件路径
     * @param outSignedPdfPath 签署后的合同文件路径
     * @param post 签章签署位置
     */
    public FileDigestSignResult doSign_Person(ServiceClient serviceClient,
                                              String personAccountId,
                                              String personSealData,
                                              String srcPdfPath,
                                              String outSignedPdfPath,
                                              PostParam post) {
        try {

            // 文档名称,此文档名称用于在e签宝服务端记录签署日志时用,非签署后PDF文件中的文件名.若为空则取待签署PDF文件中的文件名称
            String signLogFileName = "深圳工行车牌分期合同签署";
            // 文档编辑密码,如果待签署PDF文件设置了编辑密码时需要填写编辑密码,否则请传入null
            String ownerPWD = null;

            // 获取个人客户签署时待签署PDF文件的字节流
            byte[] srcPdfBytes = FileHelper.doGetFileBytes(srcPdfPath);

            // 个人客户签署盖章
            FileDigestSignResult personSignResult = doSign_PersonByPDFBytes(serviceClient, personAccountId, personSealData, srcPdfBytes,
                    null, signLogFileName, ownerPWD, post);

            // 个人签署完成后将PDF文件字节流保存为本地PDF文件
            byte[] AllSignedPdfBytes = personSignResult.getStream();
            FileHelper.doSaveFileByStream(AllSignedPdfBytes, outSignedPdfPath);
            return personSignResult;

        } catch (EsignException e) {
            System.out.println("签署时发生异常:" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /***
     * <ul>
     * <li>方法名称：个人客户使用签章图片进行合同签署</li>
     * <li>文件方式：本地文件路径</li>
     * <li>Demo封装方法：doSign_Person</li>
     * </ul>
     *
     * @param serviceClient  e签宝客户端服务
     * @param personSealData e签宝个人签章
     * @param srcPdfPath 待签署PDF文件路径
     * @param outSignedPdfPath 签署后的合同文件路径
     * @param post 签章签署位置
     */
    public void doSign_Person(ServiceClient serviceClient,
                              String personSealData,
                              String srcPdfPath,
                              String outSignedPdfPath,
                              PostParam post) {
        try {
            // 文档名称,此文档名称用于在e签宝服务端记录签署日志时用,非签署后PDF文件中的文件名.若为空则取待签署PDF文件中的文件名称
            String signLogFileName = "深圳工行车牌分期合同签署";
            // 文档编辑密码,如果待签署PDF文件设置了编辑密码时需要填写编辑密码,否则请传入null
            String ownerPWD = null;

            // 获取个人客户签署时待签署PDF文件的字节流
            byte[] srcPdfBytes = FileHelper.doGetFileBytes(srcPdfPath);

            // 个人客户签署盖章
            FileDigestSignResult personSignResult = doSign_PersonByImage(serviceClient, personSealData, srcPdfBytes,
                    null, signLogFileName, ownerPWD, post);

            // 个人签署完成后将PDF文件字节流保存为本地PDF文件
            byte[] AllSignedPdfBytes = personSignResult.getStream();
            FileHelper.doSaveFileByStream(AllSignedPdfBytes, outSignedPdfPath);

        } catch (EsignException e) {
            System.out.println("签署时发生异常:" + e.getMessage());
            e.printStackTrace();
        }
    }

    /***
     * <ul>
     * <li>方法名称：个人客户签署盖章</li>
     * <li>文件方式：PDF文件字节流</li>
     * <li>方法用途：演示个人客户加盖公章</li>
     * <li>Demo封装方法：doSign_PersonByPdfBytes</li>
     * </ul>
     *
     * @param serviceClient
     * @param accountId  个人账户id
     * @param sealData  签章
     * @param pdfBytes pdf数据流
     * @param outPdfPath  输出合同路径
     * @param signLogFileName
     * @param ownerPWD
     * @param post 关键字，合同签署的位置
     *
     * @return fileDigestSignResult
     * @throws EsignException
     */
    public FileDigestSignResult doSign_PersonByPDFBytes(ServiceClient serviceClient,
                                                        String accountId,
                                                        String sealData,
                                                        byte[] pdfBytes,
                                                        String outPdfPath,
                                                        String signLogFileName,
                                                        String ownerPWD,
                                                        PostParam post) throws EsignException {

        SignPDFStreamBean signPDFStreamBean = null;

        // 签署后PDF文件本地保存路径,如果希望签署后依然返回PDF文件字节流时请设置该属性为空
        if (StringUtils.isNotBlank(outPdfPath)) {
            // 设置个人客户签署PDF文档信息,此时签署后不返回PDF文件字节流而是直接按照outPdfPath参数的地址保存签署后的PDF文件
            signPDFStreamBean = SignHelper.doSetSignPDFStreamBean(pdfBytes, outPdfPath, signLogFileName, ownerPWD);
        } else {
            // 设置个人客户签署PDF文档信息,此时签署后直接返回PDF文件字节流,需要接口调用方获取文件字节流后按照实际业务处理(保存为本地文件或传递到其他位置)
            signPDFStreamBean = SignHelper.doSetSignPDFStreamBean(pdfBytes, null, signLogFileName, ownerPWD);
        }

        // 签章类型,Single-单页签章、Multi-多页签章、Edges-骑缝章、Key-关键字签章
        SignType signType = SignType.Key;
        // 关键字
        String key = post.getKey();
        // 签署位置X坐标,默认值为0,以pdf页面的左下角作为原点,控制距离页面左端的横向移动距离,单位为px
        float posX = post.getPosX();
        // 签署位置Y坐标,默认值为0,以pdf页面的左下角作为原点,控制距离页面底端的纵向移动距离,单位为px
        float posY = post.getPosY();
        // 印章图片在PDF文件中的等比缩放大小,公章标准大小为4.2厘米即159px
        float widthScaling = post.getWidthScaling();
        // 印章SealData
        String personSealData = sealData;

        // 设置个人客户签章位置信息
        PosBean posBean = SignHelper.doSetKeyPosBean(signType, key, posX, posY, widthScaling);
        // 个人客户签署盖章
        FileDigestSignResult fileDigestSignResult = SignHelper.doUserLocalSignPDF((com.timevale.esign.sdk.tech.v3.client.ServiceClient) serviceClient, accountId, personSealData, signPDFStreamBean, posBean, signType);
        return fileDigestSignResult;
    }

    /***
     * <ul>
     * <li>方法名称：个人使用手写签章签署盖章</li>
     * <li>文件方式：PDF文件字节流</li>
     * <li>Demo封装方法：doSign_OrganizeByPDFBytes</li>
     * </ul>
     * @param serviceClient
     * @param sealData 印章图片Base64
     * @return fileDigestSignResult
     * @throws EsignException
     */
    public FileDigestSignResult doSign_PersonByImage(ServiceClient serviceClient,
                                                     String sealData,
                                                     byte[] pdfBytes,
                                                     String outPdfPath,
                                                     String signLogFileName,
                                                     String ownerPWD,
                                                     PostParam post) throws EsignException {

        SignPDFStreamBean signPDFStreamBean = null;

        // 签署后PDF文件本地保存路径,如果希望签署后依然返回PDF文件字节流时请设置该属性为空
        if (StringUtils.isNotBlank(outPdfPath)) {
            // 设置个人签署PDF文档信息,此时签署后不返回PDF文件字节流而是直接按照outPdfPath参数的地址保存签署后的PDF文件
            signPDFStreamBean = SignHelper.doSetSignPDFStreamBean(pdfBytes, outPdfPath, signLogFileName, ownerPWD);
        } else {
            // 设置个人签署PDF文档信息,此时签署后直接返回PDF文件字节流,需要接口调用方获取文件字节流后按照实际业务处理(保存为本地文件或传递到其他位置)
            signPDFStreamBean = SignHelper.doSetSignPDFStreamBean(pdfBytes, null, signLogFileName, ownerPWD);
        }
        // 签章类型,Single-单页签章、Multi-多页签章、Edges-骑缝章、Key-关键字签章
        SignType signType = SignType.Key;

        // 设置个人签章位置信息
        PosBean posBean = SignHelper.doSetKeyPosBean(signType, post.getKey(), post.getPosX(), post.getPosY(), post.getWidthScaling());

        // 个人签署签署盖章
        FileDigestSignResult fileDigestSignResult = SignHelper.doSelfLocalSignPDF((com.timevale.esign.sdk.tech.v3.client.ServiceClient) serviceClient, signPDFStreamBean, posBean, sealData, signType);
        return fileDigestSignResult;
    }

    /***
     * <ul>
     * <li>方法名称：PDF模板生成</li>
     * </ul>
     * @param serviceClient
     * @param srcPdfPath 源文档
     * @param outPdfPath 输出文档
     * @param pdfEditPWD PDF密码
     * @param isFlat 填充后是否经用现有文档中的对象域
     * @param txtFields 待填充文本域
     * @throws EsignException
     */
    public void doCreateFileFromTemplate(ServiceClient serviceClient,
                                         String srcPdfPath,
                                         String outPdfPath,
                                         String pdfEditPWD,
                                         Boolean isFlat,
                                         Map<String, Object> txtFields) throws EsignException {
        //PDF模板生成
        PDFTemplateHelper.doCreateFileFromTemplate(serviceClient, srcPdfPath, outPdfPath, pdfEditPWD, isFlat, txtFields);
    }

    /***
     * <ul>
     * <li>方法名称：签字图片转化成BASE64</li>
     * </ul>
     *
     * @throws EsignException
     */
    public String doGetImageStrFromPath(String imagePath) throws EsignException {
        //PDF模板生成
        return SealHelper.doGetImageStrFromPath(imagePath);
    }


    public static void main(String[] args) {
        try {
            //客户端初始化，全局使用，只需注册一次
            //InitClientHelper.doRegisterClient();
            //获取以及初始化的客户端，调用SDK
            ServiceClient serviceClient = InitClientHelper.doGetServiceClient(EsignConfig.PROJECT_ID);
            //可将该AccountId保存到贵司数据库以便日后直接使用,只创建一次即可
            EsignClient client = new EsignClient();
//            String personAccountId = client.addPersonAccount(serviceClient, "蒋灵俊", "330781199509082330");
//
//            // 个人客户印章SealData,可将该SealData保存到贵司数据库以便日后直接使用,只创建一次即可
//            String personSealData = client.addPersonTemplateSeal(serviceClient, personAccountId);
//            String pdfPath = "E:\\工行信用卡申请.pdf";
//            PostParam post = new PostParam();
//            post.setKey("签署人：");
//            //个人签署合同
//            client.doSign_Person(serviceClient, personAccountId, personSealData, pdfPath, pdfPath, post);
            String srcPdfPath = "C:\\Users\\Administrator\\Desktop\\车牌分期信用卡申请表-表单.pdf";
            String outPdfPath = "E:\\车牌分期信用卡申请表.pdf";
            Map<String, Object> params = new HashMap<>();
            params.put("username", "蒋灵俊");
            params.put("male", "√");
            params.put("female", " ");
            params.put("pinyin", "JIANGLINGJUN");
            client.doCreateFileFromTemplate(serviceClient, srcPdfPath, outPdfPath, null, true, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
