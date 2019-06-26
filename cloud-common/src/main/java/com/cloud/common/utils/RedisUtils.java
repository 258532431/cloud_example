package com.cloud.common.utils;

import com.cloud.common.config.GlobalException;
import com.cloud.common.enums.ResponseCodeEnum;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.io.*;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @Author: yangchenglong on 2019/6/26
 * @Description: redis工具类
 */
@Component
public class RedisUtils {

    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 批量删除对应的value
     * @param keys
     */
    public void remove(final String... keys) {
        for (String key : keys) {
            remove(key);
        }
    }

    /**
     * 删除对应的value
     * @param key
     */
    public void remove(final String key) {
        if (exists(key)) {
            redisTemplate.delete(key);
        }
    }

    /**
     * 判断缓存中是否有对应的value
     * @param key
     * @return
     */
    public boolean exists(final String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 读取缓存
     * @param key
     * @return
     */
    public Object get(final String key) {
        Object result = null;
        ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
        result = operations.get(key);
        return objectDeserialization((String)result);
    }

    /**
     * 写入缓存
     * @param key
     * @param value
     * @return
     */
    public boolean set(final String key, Object value) {
        boolean result = false;
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            operations.set(key, objectSerialiable(value));
            result = true;
        } catch (Exception e) {
            throw new GlobalException(ResponseCodeEnum.RETURN_CODE_100500, e.getMessage());
        }
        return result;
    }

    /**
     * 写入缓存
     * @param key
     * @param value
     * @return
     */
    public boolean set(final String key, Object value, Long expireTime) {
        boolean result = false;
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            operations.set(key, objectSerialiable(value));
            result = redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new GlobalException(ResponseCodeEnum.RETURN_CODE_100500, e.getMessage());
        }
        return result;
    }


    /**
     * 写入缓存
     * @param key
     * @param value
     * @return
     */
    /*public boolean setSessionCache(final String key, Object value) {
        boolean result = false;
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            operations.set(key, objectSerialiable(value));
            if (Utils.isMobileDevice()) {
                result = redisTemplate.expire(key, Constant.SESSION_MOBILE_EXPIRETIME_SECONDS, TimeUnit.SECONDS);
            } else {
                result = redisTemplate.expire(key, Constant.SESSION_PC_EXPIRETIME_SECONDS, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            throw new GlobalException(ResponseCodeEnum.RETURN_CODE_100500, e.getMessage());
        }
        return result;
    }*/

    /**
     * 刷新key
     * */
    /*public boolean refreshSessionCache(final String key) {
        boolean result = false;
        try {
            if (Utils.isMobileDevice()) {
                result = redisTemplate.expire(key, Constant.SESSION_MOBILE_EXPIRETIME_SECONDS, TimeUnit.SECONDS);
            } else {
                result = redisTemplate.expire(key, Constant.SESSION_PC_EXPIRETIME_SECONDS, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            throw new GlobalException(ResponseCodeEnum.RETURN_CODE_100500, e.getMessage());
        }
        return result;
    }*/

    /**
     * 对象序列化为字符串
     **/
    public static String objectSerialiable(Object obj){
        String serStr = null;
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(obj);
            serStr = byteArrayOutputStream.toString("ISO-8859-1");
            serStr = java.net.URLEncoder.encode(serStr, "UTF-8");

            objectOutputStream.close();
            byteArrayOutputStream.close();
        } catch (UnsupportedEncodingException e) {
            throw new GlobalException(ResponseCodeEnum.RETURN_CODE_100500, e.getMessage());
        } catch (IOException e) {
            throw new GlobalException(ResponseCodeEnum.RETURN_CODE_100500, e.getMessage());
        }
        return serStr;
    }


    /**
     * 字符串反序列化为对象
     * */
    public static Object objectDeserialization(String serStr){
        Object newObj = null;
        try {
            String redStr = java.net.URLDecoder.decode(serStr, "UTF-8");
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(redStr.getBytes("ISO-8859-1"));
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            newObj = objectInputStream.readObject();
            objectInputStream.close();
            byteArrayInputStream.close();
        } catch (UnsupportedEncodingException e) {
            throw new GlobalException(ResponseCodeEnum.RETURN_CODE_100500, e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new GlobalException(ResponseCodeEnum.RETURN_CODE_100500, e.getMessage());
        } catch (IOException e) {
            throw new GlobalException(ResponseCodeEnum.RETURN_CODE_100500, e.getMessage());
        }
        return newObj;
    }

}
