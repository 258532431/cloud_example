package com.cloud.common.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @program: cloud_example
 * @description: http请求工具类
 * @author: yangchenglong
 * @create: 2019-06-27 14:55
 */
public class HttpUtils {

    private static CloseableHttpClient httpclient;
    //编码格式
    private final static String ENCODING = "UTF-8";
    static {
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(200);//设置连接池的最大连接数
        cm.setDefaultMaxPerRoute(100);//设置每个路由上的默认连接个数
        httpclient = HttpClients.custom().setConnectionManager(cm).build();
    }

    /**
     * 发送post请求
     * @param url
     * @param paramsMap
     * @return
     */
    public static String post(String url, Map<String, String> paramsMap) {
        String responseText = "";
        CloseableHttpResponse response = null;
        try {
            HttpPost method = new HttpPost(url);
            if (paramsMap != null) {
                List<NameValuePair> paramList = new ArrayList<NameValuePair>();
                for (Map.Entry<String, String> param : paramsMap.entrySet()) {
                    NameValuePair pair = new BasicNameValuePair(param.getKey(), param.getValue());
                    paramList.add(pair);
                }
                method.setEntity(new UrlEncodedFormEntity(paramList, ENCODING));
            }
            response = httpclient.execute(method);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                responseText = EntityUtils.toString(entity, ENCODING);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return responseText;
    }

    /**
     * 发送get请求
     * @param url
     * @return
     */
    public static String get(String url) throws Exception {
        HttpGet get = new HttpGet(url);
        CloseableHttpResponse response = httpclient.execute(get);
        HttpEntity entity = response.getEntity();
        String body = EntityUtils.toString(entity);
        response.close();
        return body;
    }

    /**
     * 发送XML参数请求
     * */
    public static String postXML(String url, String xml) throws Exception {
        HttpPost post = new HttpPost(url);
        StringEntity se = new StringEntity(xml, ENCODING);
        post.setEntity(se);
        CloseableHttpResponse response = httpclient.execute(post);
        HttpEntity entity = response.getEntity();
        String body = EntityUtils.toString(entity, ENCODING);
        response.close();
        return body;
    }

    /**
     * 发送json请求
     * */
    public static String postJson(String url, Map map) throws Exception {
        HttpPost post = new HttpPost(url);
        JSONObject jsonParam = new JSONObject();
        Set<String> keySet = map.keySet();
        for(String key : keySet) {
            jsonParam.put(key, map.get(key));
        }
        StringEntity entity = new StringEntity(jsonParam.toString(),ENCODING);//解决中文乱码问题
        entity.setContentEncoding(ENCODING);
        entity.setContentType("application/json");
        post.setEntity(entity);
        CloseableHttpResponse response = httpclient.execute(post);
        HttpEntity res = response.getEntity();
        String body = EntityUtils.toString(res, ENCODING);
        response.close();
        return body;
    }

    /**
     * 上传文件
     * */
    public static String postUpload(String url, CommonsMultipartFile file, String filePath, String fileName) throws Exception {
        HttpPost post = new HttpPost(url);
        StringBody filepath = new StringBody(filePath, ContentType.create("text/plain", ENCODING));
        StringBody filename = new StringBody(fileName, ContentType.create("text/plain", ENCODING));
        CommonsMultipartFile cf= (CommonsMultipartFile)file;
        DiskFileItem fi = (DiskFileItem)cf.getFileItem();
        // 把文件转换成流对象FileBody
        FileBody file1= new FileBody(fi.getStoreLocation());
        //相当于<input type="file" name="file"/>
        HttpEntity reqEntity = MultipartEntityBuilder.create().addPart("file", file1).addPart("filePath", filepath).addPart("fileName", filename).build();
        post.setEntity(reqEntity);
        CloseableHttpResponse response = httpclient.execute(post);
        HttpEntity entity = response.getEntity();
        String body = EntityUtils.toString(entity, ENCODING);
        response.close();
        return body;
    }

}
