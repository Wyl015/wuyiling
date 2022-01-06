package com.wuyiling.worktest.Utils;

import org.apache.commons.codec.binary.Base64;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * RSA加签，验签工具类
 * @author thf
 * @date 2018-06-07
 * @version v1.0
 */
public class RsaUtils {

    /** 签名算法 */
    private static final String SIGNATURE_ALGORITHMS = "SHA1WithRSA";
    /** 加密算法 */
    private static final String KEY_ALGORITHMS = "RSA";

    public static final String CHARSET_UTF_8 = "UTF-8";
    public static final String DELIMITER = "$";
    public static final String SIGN_EQUAL = "=";

    /** 请求参数的key */
    public static final String KEY_SIGN = "sign";
    public static final String KEY_DATA = "data";
    public static final String KEY_ORIGIN = "origin";

    /** 私钥 */
    public static final String PRIVATE_KEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALy9HtSLeISoBVB9\n" +
                                            "bo8r/HHloC/3GrwuNsp9/r8qgRECjNIifDfh4Vb2un3pbj2KQshByiYQV5EyqPEH\n" +
                                            "wq9uh+3X60/keVwyW+fg4WphgWh07GWReC1snK8biFEFTCZX7crCzjIB5r5Zw6Qs\n" +
                                            "3IerXSljYEO4YRQh8NukoWKxEf6BAgMBAAECgYEAgjMMw577IrguAlqlHEtBuOip\n" +
                                            "Wq1iWhKfZY0JYaLWqn9R2dxESOCf7LgD6rxPelCYxUDtTNjVL6r89FlgKllblr1C\n" +
                                            "9A6I4VubeZ3QQsWvqPJzL5qwlD6wUJLJV7zqke5kW067EMO8Boid0RAPY4p978nQ\n" +
                                            "2ebg49+mO/9E339sQOECQQDx9Bkp9cQdaexeVm5eoNb40OBREFaX3VyRARaurBYG\n" +
                                            "CG0m81p5SSDVbInbF+Qr0vj73Unk1zP/AD7JmEUdZtClAkEAx7Ilm7GQBAVl1RdM\n" +
                                            "BawKKaCWjokG+jrNY0D82pfHyiR1Yo9Xa+nxnLtuolqPcUMUCWul6+Td+GoHfnML\n" +
                                            "aPPTrQJBAMmIBJ3Ks/u8CsHZRD1Vwzmk4fMjpL0CCORO/9GmTQHhaumSsb1siAi9\n" +
                                            "S2ZO1CMcq38+pxoRlqHEVogcaRVAWnECQB1ugSJ4QgyFqyOD4n13hKvr8iShx3h7\n" +
                                            "0CePvXSOKDD/vJlBHRZXjXeXHFArXrbHtx1IU1T4D0r8fYxCcm0OeqUCQFTysTHv\n" +
                                            "emrALsGTQYABE5MmZcIQ5BB1vZikDE/R9/v67L7RpMuL/d9LxwzKwWbH69T4Alnr\n" +
                                            "oPwAeuIiz+Opy6E=";

    public static String getSHA1WithRSASign(String content, String privateKey) throws Exception {
        return getSHA1WithRSASign(content, privateKey, CHARSET_UTF_8);
    }

    /**
     * SHA1WithRSA签名,并对签名进行base64编码
     * @param content 待签名数据
     * @param privateKey 私钥
     * @param charset 编码格式
     * @return
     */
    public static String getSHA1WithRSASign(String content, String privateKey, String charset) throws Exception {
        byte[] contentBytes = content.getBytes(charset);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHMS);
        signature.initSign(getPrivateKey(privateKey));
        signature.update(contentBytes);
        byte[] bytes = Base64.encodeBase64(signature.sign());
        return new String(bytes);
    }

    /**
     * 用对方的公钥对数据进行验签
     * @param data 加签数据
     * @param publicKey 对方的公钥
     * @param sign 签名
     * @return
     * @throws Exception
     */
    public static boolean verify(String data, String publicKey, String sign, String charset) {
        boolean result = false;
        try {
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHMS);
            signature.initVerify(getPublicKey(publicKey));
            signature.update(data.getBytes(charset));
            result = signature.verify(Base64.decodeBase64(sign));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取私钥
     * @param key 私钥（经过base64编码）
     * @return
     * @throws Exception
     */
    private static PrivateKey getPrivateKey(String key) throws Exception {
        byte[] keyBytes = Base64.decodeBase64(key);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHMS);
        return keyFactory.generatePrivate(keySpec);
    }

    /**
     * 获取公钥
     * @param key 公钥（经过base64编码）
     * @return
     * @throws Exception
     */
    private static PublicKey getPublicKey(String key) throws Exception {
        byte[] keyBytes = Base64.decodeBase64(key);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHMS);
        return keyFactory.generatePublic(keySpec);
    }

}
