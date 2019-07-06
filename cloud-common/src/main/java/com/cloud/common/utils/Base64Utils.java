package com.cloud.common.utils;

import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @Author: yangchenglong on 2019/7/6
 * @Description: Base64工具类
 * update by:
 */
public class Base64Utils {

	//base64 解码
    public static String decode(byte[] bytes) {  
        return new String(Base64.decodeBase64(bytes));  
    }  
  
    //base64 编码
    public static String encode(byte[] bytes) {  
        return new String(Base64.encodeBase64(bytes));  
    }

    /**
     * 本地图片转换成base64字符串
     * @param imgFile 图片本地路径
     */
    public static String localImageToBase64(String imgFile) { // 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        InputStream in = null;
        byte[] data = null;
        // 读取图片字节数组
        try {
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return encode(data); // 返回Base64编码过的字节数组字符串
    }


    /**
     * 在线图片转换成base64字符串
     * @param imgURL 图片线上路径
     */
    public static String onlineImageToBase64(String imgURL) {
        ByteArrayOutputStream data = new ByteArrayOutputStream();
        try {
            // 创建URL
            URL url = new URL(imgURL);
            byte[] by = new byte[1024];
            // 创建链接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            InputStream is = conn.getInputStream();
            // 将内容读取内存中
            int len = -1;
            while ((len = is.read(by)) != -1) {
                data.write(by, 0, len);
            }
            // 关闭流
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return encode(data.toByteArray());
    }

}