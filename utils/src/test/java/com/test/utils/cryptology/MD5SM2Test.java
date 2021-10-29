package com.test.utils.cryptology;

import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.bouncycastle.util.encoders.Hex;
import org.junit.jupiter.api.Test;
import org.springframework.util.DigestUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class MD5SM2Test {

    @Test
    public void test() throws InvalidAlgorithmParameterException, NoSuchAlgorithmException {


        String str = "12";
        System.out.println("文字："  + new String(str.getBytes()));

        System.out.println("======MD5加密======");
        String digestPwd1 = DigestUtils.md5DigestAsHex(str.getBytes());
        System.out.println("密文：" + digestPwd1);

        System.out.println("======SM2加密======");
        SM2Util sm2 = new SM2Util();
        KeyPair keyPair = sm2.generateSm2KeyPair();
        BCECPrivateKey privateKey = (BCECPrivateKey) keyPair.getPrivate();
        BCECPublicKey publicKey = (BCECPublicKey) keyPair.getPublic();
        System.out.println("私钥：" + privateKey);
        System.out.println("公钥：" + publicKey);
        String pubKey = new String(Hex.encode(publicKey.getQ().getEncoded(true)));
        String prvKey = privateKey.getD().toString(16);
        System.out.println("私钥String：" + prvKey);
        System.out.println("公钥String：" + pubKey);

        try {
            System.out.println("加密前：" + str);
            String encode = sm2.encode(str, pubKey);
            System.out.println("加密后：" + encode);
            String decoder = new String(sm2.decoder(encode, prvKey));
            System.out.println("解密后：" + decoder);
        } catch (Exception e) {
            System.out.println("加解密测试错误");
        }
        // 签名和验签测试
        try {
            System.out.println("签名源数据：" + str);
            String signStr = sm2.sign(str, prvKey);
            System.out.println("签名后数据：" + signStr);
            boolean verify = sm2.verify(str, signStr, pubKey);
            System.out.println("签名验证结果：" + verify);
        } catch (Exception e) {
            System.out.println("签名和验签测试错误");
        }

    }


    @Test
    public void test1() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, InvalidKeySpecException, NoSuchPaddingException {

        //生成密钥
        SM2Util sm2 = new SM2Util();
        KeyPair keyPair = sm2.generateSm2KeyPair();
        BCECPrivateKey privateKey = (BCECPrivateKey) keyPair.getPrivate();
        BCECPublicKey publicKey = (BCECPublicKey) keyPair.getPublic();
        String pubKey = new String(Hex.encode(publicKey.getQ().getEncoded(true)));
        String prvKey = privateKey.getD().toString(16);
        System.out.println("Public Key: " + pubKey);
        System.out.println("Private Key: " + prvKey);

//        String pubKey = "024fcbd75ed00e5bc47efcf4739d76455a42cb8b1324acf837691e14fb13c84848";
//        String prvKey = "b751408ac1a80aff808180af7a644f578ab2322168581a0f1c51be01edddc0b2";
//        System.out.println("Public Key: " + pubKey);
//        System.out.println("Private Key: " + prvKey);

        String psw = "123" ;
        String psw1 = "" ;
        try {
            psw1 = sm2.encode(psw, pubKey);
        } catch (Exception e) {
            System.out.println("加密测试错误");
        }

        TestQuery testQuery = TestQuery.builder().name("name").password(psw1).build();
        Method getPassword = testQuery.getClass().getDeclaredMethod("getPassword");
        String password = getPassword.invoke(testQuery).toString();
        System.out.println("传入密码" + password);

        Method setPassword = testQuery.getClass().getDeclaredMethod("setPassword", String.class);

        setPassword.invoke(testQuery, "456");
        System.out.println("传入密码" + testQuery.getPassword());

        try {
            String decoder = new String(sm2.decoder(password, prvKey));
            System.out.println("解密后：" + decoder);
        } catch (Exception e) {
            System.out.println("解密测试错误");
        }


    }


    @Test
    public void test2() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, InvalidKeySpecException, NoSuchPaddingException {

        //生成密钥
        SM2Util sm2 = new SM2Util();
        KeyPair keyPair = sm2.generateSm2KeyPair();
        BCECPrivateKey privateKey = (BCECPrivateKey) keyPair.getPrivate();
        BCECPublicKey publicKey = (BCECPublicKey) keyPair.getPublic();
        String pubKey = new String(Hex.encode(publicKey.getQ().getEncoded(true)));
        String prvKey = privateKey.getD().toString(16);
        System.out.println("Public Key: " + pubKey);
        System.out.println("Private Key: " + prvKey);
        String psw = "123" ;
        String psw1 = "" ;
        try {
            psw1 = sm2.encode(psw, pubKey);
        } catch (Exception e) {
            System.out.println("加密测试错误");
        }
        TestQuery testQuery = TestQuery.builder().name("name").password(psw1).build();
        SM2Util sm2Util = new SM2Util();
        sm2Util.decodeSM2(testQuery, "password", prvKey);
        System.out.println("结果：" + testQuery.getPassword());


    }



}
