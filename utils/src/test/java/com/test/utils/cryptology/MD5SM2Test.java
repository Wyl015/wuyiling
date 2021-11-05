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
import java.util.Base64;

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
            String decoder = new String(sm2.decode(encode, prvKey));
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
        String pubKey = new String(Hex.encode(publicKey.getQ().getEncoded(false)));
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
            String decoder = new String(sm2.decode(password, prvKey));
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
        String psw = "Ab@123456" ;
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


    @Test
    public void test3() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        //生成密钥
        SM2Util sm2 = new SM2Util();
        String pubKey = "0403fb7f37a484e6efea50b86dd499901a7fcf54e3ab440e30ee79864729029fa33dbe794863e50a1af1b4dfdbe156cb71aff4bd863d0193d9ef4f7bddb50d1487";
        String prvKey = "2f08402314ffaef96a124f0eacd384f2f25c629c91d724130e64826541f8ec6";
        System.out.println("Public Key: " + pubKey);
        System.out.println("Private Key: " + prvKey);
        String psw = "Aa@123456" ;
        // 加解密测试
        try {
            System.out.println("加密前：" + psw);
            String encode = sm2.encode(psw, pubKey);
            System.out.println("加密后：" + encode);
            String decoder = new String(sm2.decode2(encode, prvKey));
            System.out.println("解密后：" + decoder);
        } catch (Exception e) {
            System.out.println("加解密测试错误");
        }
    }

    @Test
    public void test4()  {
        SM2Util sm2 = new SM2Util();
        //生成密钥
        String input = "cfe1dabd772072742cd07659d772d1c9e77b60adbee9599ba06b570781a3bdd6";
        String prvKey = "cfe1dabd772072742cd07659d772d1c9e77b60adbee9599ba06b570781a3bdd6";
        // 加解密测试
        try {
            String decoder = new String(sm2.decode2(input, prvKey));
            System.out.println("解密后：" + decoder);

        } catch (Exception e) {
            System.out.println("加解密测试错误");
        }

    }




    @Test
    public void test5() throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidKeySpecException {
        String IOSinput ="ECADD5FF9EA9BB801D00ADCC716C9C2400C62AD85D9300B9F8D152EAFB44FBB024B70A221DFD5B3A6177A073C0EAABE65EDF820CCD0403CE33B9C97CF3D02376FD9E0F79ABAD49160F8DD03F66C277065897C0F20FA55230D089A3B89193449CB96630";
        String prvkey = "f6a13c5c9485097050df9d30eda54eda6d3ba9f735755b3562e990aed0bc99a0";

        byte[] bytes = IOSinput.getBytes();


        SM2Util sm2Util = new SM2Util();


        String decoder = new String(sm2Util.decode2(bytes,prvkey));

        System.out.println(decoder);
    }

}
