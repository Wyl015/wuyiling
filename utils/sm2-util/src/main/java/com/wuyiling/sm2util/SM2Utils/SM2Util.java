package com.wuyiling.sm2util.SM2Utils;

import org.bouncycastle.asn1.gm.GMNamedCurves;
import org.bouncycastle.asn1.gm.GMObjectIdentifiers;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.bouncycastle.jcajce.provider.asymmetric.x509.CertificateFactory;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.jce.spec.ECPrivateKeySpec;
import org.bouncycastle.jce.spec.ECPublicKeySpec;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.ByteArrayInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

@Component
public class SM2Util {
    private BouncyCastleProvider provider;
    // 获取SM2相关参数
    private X9ECParameters parameters;
    // 椭圆曲线参数规格
    private ECParameterSpec ecParameterSpec;
    // 获取椭圆曲线KEY生成器
    private KeyFactory keyFactory;

    public SM2Util(){
        try {
            provider = new BouncyCastleProvider();
            parameters = GMNamedCurves.getByName("sm2p256v1");
            ecParameterSpec = new ECParameterSpec(parameters.getCurve(),
                    parameters.getG(), parameters.getN(), parameters.getH());
            keyFactory = KeyFactory.getInstance("EC", provider);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * SM2算法生成密钥对
     *
     * @return 密钥对信息
     */
    public KeyPair generateSm2KeyPair() throws InvalidAlgorithmParameterException, NoSuchAlgorithmException {
        final ECGenParameterSpec sm2Spec = new ECGenParameterSpec("sm2p256v1");
        // 获取一个椭圆曲线类型的密钥对生成器
        final KeyPairGenerator kpg = KeyPairGenerator.getInstance("EC", provider);
        SecureRandom random = new SecureRandom();
        // 使用SM2的算法区域初始化密钥生成器
        kpg.initialize(sm2Spec, random);
        // 获取密钥对
        KeyPair keyPair = kpg.generateKeyPair();
        return keyPair;
    }



    /**
     * 加密
     *
     * @param input  待加密文本
     * @param pubKey 公钥
     * @return
     */
    public String encode(String input, String pubKey)
            throws NoSuchPaddingException, NoSuchAlgorithmException,
            BadPaddingException, IllegalBlockSizeException,
            InvalidKeySpecException, InvalidKeyException {
//        // 获取SM2相关参数
//        X9ECParameters parameters = GMNamedCurves.getByName("sm2p256v1");
//        // 椭圆曲线参数规格
//        ECParameterSpec ecParameterSpec = new ECParameterSpec(parameters.getCurve(), parameters.getG(), parameters.getN(), parameters.getH());
        // 将公钥HEX字符串转换为椭圆曲线对应的点
        ECPoint ecPoint = parameters.getCurve().decodePoint(Hex.decode(pubKey));
        // 获取椭圆曲线KEY生成器
        KeyFactory keyFactory = KeyFactory.getInstance("EC", provider);
        BCECPublicKey key = (BCECPublicKey) keyFactory.generatePublic(new ECPublicKeySpec(ecPoint, ecParameterSpec));
        // 获取SM2加密器
        Cipher cipher = Cipher.getInstance("SM2", provider);
        // 初始化为加密模式
        cipher.init(Cipher.ENCRYPT_MODE, key);
        // 加密并编码为base64格式
        return Base64.getEncoder().encodeToString(cipher.doFinal(input.getBytes()));
    }

    /**
     * 解密
     *
     * @param input  待解密文本
     * @param prvKey 私钥
     * @return
     */
    public byte[] decode(String input, String prvKey) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidKeySpecException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        // 获取SM2加密器
        Cipher cipher = Cipher.getInstance("SM2", provider);
        // 将私钥HEX字符串转换为X值
        BigInteger bigInteger = new BigInteger(prvKey, 16);
        BCECPrivateKey privateKey = (BCECPrivateKey) keyFactory.generatePrivate(new ECPrivateKeySpec(bigInteger,
                ecParameterSpec));
        // 初始化为解密模式
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        // 解密
        return cipher.doFinal(Base64.getDecoder().decode(input));
    }




    public byte[] decode2(byte[] input, String prvKey) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidKeySpecException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

        // 获取SM2加密器
        Cipher cipher = Cipher.getInstance("SM2", provider);
        // 将私钥HEX字符串转换为X值
        BigInteger bigInteger = new BigInteger(prvKey, 16);
        BCECPrivateKey privateKey = (BCECPrivateKey) keyFactory.generatePrivate(new ECPrivateKeySpec(bigInteger, ecParameterSpec));
        // 初始化为解密模式
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        // 解密
        return cipher.doFinal(input);
    }




    /**
     * 签名
     *
     * @param plainText 待签名文本
     * @param prvKey    私钥
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws InvalidKeyException
     * @throws SignatureException
     */
    public String sign(String plainText, String prvKey) throws NoSuchAlgorithmException, InvalidKeySpecException,
            InvalidKeyException, SignatureException {
        // 创建签名对象
        Signature signature = Signature.getInstance(GMObjectIdentifiers.sm2sign_with_sm3.toString(), provider);
        // 将私钥HEX字符串转换为X值
        BigInteger bigInteger = new BigInteger(prvKey, 16);
        BCECPrivateKey privateKey = (BCECPrivateKey) keyFactory.generatePrivate(new ECPrivateKeySpec(bigInteger,
                ecParameterSpec));
        // 初始化为签名状态
        signature.initSign(privateKey);
        // 传入签名字节
        signature.update(plainText.getBytes());
        // 签名
        return Base64.getEncoder().encodeToString(signature.sign());
    }

    public boolean verify(String plainText, String signatureValue, String pubKey) throws NoSuchAlgorithmException, InvalidKeySpecException,
            InvalidKeyException, SignatureException {
        // 创建签名对象
        Signature signature = Signature.getInstance(GMObjectIdentifiers.sm2sign_with_sm3.toString(), provider);
        // 将公钥HEX字符串转换为椭圆曲线对应的点
        ECPoint ecPoint = parameters.getCurve().decodePoint(Hex.decode(pubKey));
        BCECPublicKey key = (BCECPublicKey) keyFactory.generatePublic(new ECPublicKeySpec(ecPoint, ecParameterSpec));
        // 初始化为验签状态
        signature.initVerify(key);
        signature.update(plainText.getBytes());
        return signature.verify(Base64.getDecoder().decode(signatureValue));
    }

    /**
     * 证书验签
     *
     * @param certStr      证书串
     * @param plaintext    签名原文
     * @param signValueStr 签名产生签名值 此处的签名值实际上就是 R和S的sequence
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws SignatureException
     */
    public boolean certVerify(String certStr, String plaintext, String signValueStr)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, CertificateException {

        byte[] signValue = Base64.getDecoder().decode(signValueStr);
        /*
         * 解析证书
         */
        CertificateFactory factory = new CertificateFactory();
        X509Certificate certificate = (X509Certificate) factory
                .engineGenerateCertificate(new ByteArrayInputStream(Base64.getDecoder().decode(certStr)));
        // 验证签名
        Signature signature = Signature.getInstance(certificate.getSigAlgName(), provider);
        signature.initVerify(certificate);
        signature.update(plaintext.getBytes());
        return signature.verify(signValue);
    }

//    public static void main(String[] args) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException {
//        String str = "看看能不能一次通过";
//        SM2Util sm2 = new SM2Util();
//        KeyPair keyPair = sm2.generateSm2KeyPair();
//        BCECPrivateKey privateKey = (BCECPrivateKey) keyPair.getPrivate();
//        BCECPublicKey publicKey = (BCECPublicKey) keyPair.getPublic();
//
//        // 拿到密钥
//        String pubKey = new String(Hex.encode(publicKey.getQ().getEncoded(false)));
//        String prvKey = privateKey.getD().toString(16);
//        System.out.println("Private Key: " + prvKey);
//        System.out.println("Public Key: " + pubKey);
//        // 加解密测试
//        try {
//            System.out.println("加密前：" + str);
//            String encode = sm2.encode(str, pubKey);
//            System.out.println("加密后：" + encode);
//            String decoder = new String(sm2.decode(encode, prvKey));
//            System.out.println("解密后：" + decoder);
//        } catch (Exception e) {
//            System.out.println("加解密测试错误");
//        }
//        // 签名和验签测试
//        try {
//            System.out.println("签名源数据：" + str);
//            String signStr = sm2.sign(str, prvKey);
//            System.out.println("签名后数据：" + signStr);
//            boolean verify = sm2.verify(str, signStr, pubKey);
//            System.out.println("签名验证结果：" + verify);
//        } catch (Exception e) {
//            System.out.println("签名和验签测试错误");
//        }
//    }



    //通过反射修改传入类中password成员的值
    public void decodeSM2 (Object signQuery, String param, String prvKey) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        System.out.println("传入参数" + signQuery.toString());
        Method getPassword = signQuery.getClass().getDeclaredMethod("getPasswor");


        String password = getPassword.invoke(signQuery).toString();
        System.out.println("传入密码" + password);
        String decodepsd = "";
        SM2Util sm2Util = new SM2Util();
        try {
           decodepsd = new String(sm2Util.decode(password, prvKey));
            System.out.println("解密后：" + decodepsd);
        } catch (Exception e) {
            System.out.println("解密测试错误");
        }
        Method setPassword = signQuery.getClass().getDeclaredMethod("setPassword", String.class);
        setPassword.invoke(signQuery, decodepsd);
        System.out.println("参数修改" + signQuery.toString());

    }


    /**
     * 解密
     *
     * @param input  待解密文本
     * @param prvKey 私钥
     * @return
     */
    public byte[] decode2(String input, String prvKey) {
        System.out.println("解密参数input："+ input +","+ prvKey);
        try {
            // 获取SM2加密器
            Cipher cipher = Cipher.getInstance("SM2", provider);
            // 将私钥HEX字符串转换为X值
            BigInteger bigInteger = new BigInteger(prvKey, 16);
            BCECPrivateKey privateKey = (BCECPrivateKey) keyFactory.generatePrivate(new ECPrivateKeySpec(bigInteger,
                    ecParameterSpec));
            // 初始化为解密模式
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            // 解密
            return cipher.doFinal(Base64.getDecoder().decode(input));
        } catch ( Exception e) {
            System.out.println(e);
        }
        return null;
    }
}
