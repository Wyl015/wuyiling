package com.test.utils.cryptology;

import org.bouncycastle.asn1.gm.GMNamedCurves;
import org.bouncycastle.asn1.gm.GMObjectIdentifiers;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.jce.spec.ECPrivateKeySpec;
import org.bouncycastle.jce.spec.ECPublicKeySpec;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.util.encoders.Hex;
import org.junit.jupiter.api.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

public class SM2Test {

    //生成密钥对
    @Test
    public void SM2Test1() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeySpecException, SignatureException, InvalidKeyException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException {
        BouncyCastleProvider provider = new BouncyCastleProvider();
        // 获取椭圆曲线相关生成参数规格
        ECGenParameterSpec genParameterSpec = new ECGenParameterSpec("sm2p256v1");
        // 获取一个椭圆曲线类型的密钥对生成器
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC", provider);
        // 使用SM2的算法区域初始化密钥生成器
        keyPairGenerator.initialize(genParameterSpec, new SecureRandom());

        // 生成密钥对
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        BCECPrivateKey privateKey = (BCECPrivateKey) keyPair.getPrivate();
        BCECPublicKey publicKey = (BCECPublicKey) keyPair.getPublic();

        // 拿到32字节的私钥HEX
        String prvKey = privateKey.getD().toString(16);
        // true  代表压缩密钥，以02、03开头，长度为33字节
        // false 代表未压缩，以04开头，长度为65字节
        String pubKey = new String(Hex.encode(publicKey.getQ().getEncoded(false)));


        System.out.println("prvKey: " + prvKey );
        System.out.println("pubKey: " + pubKey );
        //toString 和 String() 的区别
        System.out.println("pubKey.toString: " + Hex.encode(publicKey.getQ().getEncoded(true)).toString() );

       //加载HEX密钥对字符串
        SM2Test2( provider, prvKey, pubKey);
        //签名和验签
        SM2Test3( provider, privateKey, publicKey);
        //加解密
        SM2Test4( provider, privateKey, publicKey);

    }

    //加载HEX密钥对字符串
    @Test
    public void SM2Test2(Provider provider, String prvKey, String pubKey) throws InvalidKeySpecException, NoSuchAlgorithmException {
//        String prvKey = "私钥HEX字符串", pubKey = "公钥HEX字符串";
        // 获取SM2相关参数
        X9ECParameters parameters = GMNamedCurves.getByName("sm2p256v1");
        // 椭圆曲线参数规格
        ECParameterSpec ecParameterSpec = new ECParameterSpec(parameters.getCurve(), parameters.getG(), parameters.getN(), parameters.getH());
        // 将公钥HEX字符串转换为椭圆曲线对应的点
        ECPoint ecPoint = parameters.getCurve().decodePoint(Hex.decode(pubKey));
        // 将私钥HEX字符串转换为X值
        BigInteger bigInteger = new BigInteger(prvKey, 16);
        // 获取椭圆曲线KEY生成器
        KeyFactory keyFactory = KeyFactory.getInstance("EC", provider);
        // 将X值转为私钥KEY对象
        BCECPrivateKey privateKey = (BCECPrivateKey) keyFactory.generatePrivate(new ECPrivateKeySpec(bigInteger, ecParameterSpec));
        System.out.println("Private Key: " + privateKey.getD().toString(16));
        // 将椭圆曲线点转为公钥KEY对象
        BCECPublicKey publicKey = (BCECPublicKey) keyFactory.generatePublic(new ECPublicKeySpec(ecPoint, ecParameterSpec));
        String prvKey1 = privateKey.getD().toString(16);
        // true  代表压缩密钥，以02、03开头，长度为33字节
        // false 代表未压缩，以04开头，长度为65字节
        String pubKey1 = new String(Hex.encode(publicKey.getQ().getEncoded(true)));

        System.out.println("prvKey1: " + prvKey1 );
        System.out.println("pubKey1: " + pubKey1 );
    }

    //签名和验签
    @Test
    public void SM2Test3(Provider provider, BCECPrivateKey privateKey, BCECPublicKey publicKey) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        byte[] bytes = "Hello World".getBytes(), signBytes;
        // 创建签名对象
        Signature signature = Signature.getInstance(GMObjectIdentifiers.sm2sign_with_sm3.toString(), provider);
        // 初始化为签名状态
        signature.initSign(privateKey);
        // 传入签名字节
        signature.update(bytes);
        // 返回签名字节
        System.out.println(Hex.encode(signBytes = signature.sign()));
        // 初始化为验签状态
        signature.initVerify(publicKey);
        // 传入签名字节
        signature.update(bytes);
        // 返回验签结果
        System.out.println(signature.verify(signBytes));

    }

    //加解密
    @Test
    public void SM2Test4(Provider provider, BCECPrivateKey privateKey, BCECPublicKey publicKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        String word = "你好世界";
        // 获取SM2加密器
        Cipher cipher = Cipher.getInstance("SM2", provider);
        // 初始化为加密模式
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        // 加密并编码为base64格式
        word = Base64.getEncoder().encodeToString(cipher.doFinal(word.getBytes()));
        System.out.println("密文：" + word);
        // 初始化为解密模式
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        // 解密
        word = new String(cipher.doFinal(Base64.getDecoder().decode(word)));
        System.out.println("解密：" + word);


    }




}
