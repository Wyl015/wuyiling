package com.wuyiling.worktest.Utils.sm2;

import com.yuuwei.faceview.enums.http.ResponseCodeEnum;
import com.yuuwei.faceview.exception.GlobalFaceSignException;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

@Slf4j
@Component
public class SM2UtilBC {
    private BouncyCastleProvider provider;
    // 获取SM2相关参数
    private X9ECParameters parameters;
    // 椭圆曲线参数规格
    private ECParameterSpec ecParameterSpec;
    // 获取椭圆曲线KEY生成器
    private KeyFactory keyFactory;

    public SM2UtilBC(){
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
    public byte[] decode(String input, String prvKey) {
        log.info("SM2解密入参：【{}】，【{}】", input, prvKey);
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
            log.info("SM2解密失败" + e);
            throw new GlobalFaceSignException(ResponseCodeEnum.SM2_ERROR);
        }
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

    /**
     * 签名验证
     *
     * @param plainText 待签名文本
     * @param signatureValue 签名后文本
     * @param pubKey    私钥
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws InvalidKeyException
     * @throws SignatureException
     */
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


    public static void main(String[] args) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException {
        String str = "123456";
        SM2UtilBC sm2 = new SM2UtilBC();
        KeyPair keyPair = sm2.generateSm2KeyPair();
        BCECPrivateKey privateKey = (BCECPrivateKey) keyPair.getPrivate();
        BCECPublicKey publicKey = (BCECPublicKey) keyPair.getPublic();

        // 拿到密钥
//        String pubKey = new String(Hex.encode(publicKey.getQ().getEncoded(false)));
//        String prvKey = privateKey.getD().toString(16);
        String pubKey = "04624ddf1463cc31d6a81913dd60916b910d38bfde12607dc0efec2a752dfa2341df01b1f8d1c66f320f49a1f3a63ab66caf8fa8e356dd4dd0ac3861d1e8e89480";
        String prvKey = "3890e778ef95f6cb3e1ffa07d8bff56a36c0903d4e5f80f347abfddc356abcaa";
//        System.out.println("Private Key: " + prvKey);
//        System.out.println("Public Key: " + pubKey);
        // 加解密测试
        try {
            System.out.println("加密前：" + str);
            String encode = sm2.encode(str, pubKey);
            System.out.println("加密后：" + encode);
            String hexEncoded = Hex.toHexString(Base64.getDecoder().decode(encode));
            System.out.println("加密后：" + hexEncoded);
            System.out.println("加密后：" + Base64.getEncoder().encodeToString(Hex.decode(hexEncoded)));
            String decoder = new String(sm2.decode(encode, prvKey));
            System.out.println("解密后：" + decoder);
        } catch (Exception e) {
            System.out.println("加解密测试错误");
            e.printStackTrace();
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
}
