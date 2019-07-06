package com.cloud.common.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: yangchenglong on 2019/6/27
 * @Description: RSA加密工具类
 */
public class RSAUtils {

    /**
     * @Description: 随机生成密钥对 - 密钥KEY对应的map key值
     */
    public static final String PRIVATEKEY = "privateKey";
    /**
     * @Description: 随机生成密钥对 - 公钥KEY对应的map key值
     */
    public static final String PUBLICKEY = "publicKey";

    /**
     * 随机生成密钥对
     */
    public static Map<String, String> genKeyPair() throws NoSuchAlgorithmException {
        // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        // 初始化密钥对生成器，密钥大小为96-1024位
        keyPairGen.initialize(1024,new SecureRandom());
        // 生成一个密钥对，保存在keyPair中
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPrivateKey $privateKey = (RSAPrivateKey) keyPair.getPrivate();   // 得到私钥
        RSAPublicKey $publicKey = (RSAPublicKey) keyPair.getPublic();  // 得到公钥
        String publicKey = new String(Base64.encodeBase64($privateKey.getEncoded()));
        // 得到私钥字符串
        String privateKey = new String(Base64.encodeBase64(($publicKey.getEncoded())));

        Map<String, String> map = new HashMap<>();
        map.put(PRIVATEKEY, privateKey);
        map.put(PUBLICKEY, publicKey);
        return map;
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        System.out.println(JSONObject.toJSONString(genKeyPair()));
    }

    /**
     * RSA公钥加密
     * @param str 待加密字符串
     * @param publicKey 公钥
     * @return 密文
     * @throws Exception 加密过程中的异常信息
     */
    public static String encrypt( String str, String publicKey ) throws Exception{
        //base64编码的公钥
        byte[] decoded = Base64.decodeBase64(publicKey);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
        //RSA加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        String outStr = Base64.encodeBase64String(cipher.doFinal(str.getBytes("UTF-8")));
        return outStr;
    }

    /**
     * RSA私钥解密
     * @param str 已加密字符串
     * @param privateKey 私钥
     * @return 铭文
     * @throws Exception 解密过程中的异常信息
     */
    public static String decrypt(String str, String privateKey) throws Exception{
        //64位解码加密后的字符串
        byte[] inputByte = Base64.decodeBase64(str.getBytes("UTF-8"));
        //base64编码的私钥
        byte[] decoded = Base64.decodeBase64(privateKey);
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
        //RSA解密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        String outStr = new String(cipher.doFinal(inputByte));
        return outStr;
    }

}
