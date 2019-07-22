package com.cloud.teambition.server.utils;

import com.cloud.common.config.GlobalException;
import com.cloud.common.enums.ResponseCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.concurrent.TimeUnit;

/**
 * @Author: yangchenglong on 2019/6/27
 * @Description: redis工具类
 */
@Component
@Slf4j
public class RedisUtils {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * @Author: yangchenglong on 2019/6/27
     * @Description: 写入key、String
     * update by:
     * @Param:
     * @return:
     */
    public boolean set(String key, String value) {
        boolean result = false;
        try {
            ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
            ops.set(key, value);
            result = true;
        } catch (Exception e) {
            throw new GlobalException(ResponseCodeEnum.RETURN_CODE_100500, e.getMessage());
        }
        return result;
    }

    /**
     * @Author: yangchenglong on 2019/6/27
     * @Description: 写入key、Object
     * update by:
     * @Param:
     * @return:
     */
    public boolean setObject(String key, Object value) {
        boolean result = false;
        try {
            ValueOperations<Serializable, Object> ops = redisTemplate.opsForValue();
            ops.set(key, objectSerialiable(value));
            result = true;
        } catch (Exception e) {
            throw new GlobalException(ResponseCodeEnum.RETURN_CODE_100500, e.getMessage());
        }
        return result;
    }

    /**
     * @return void
     * @Author YangChengLong
     * @Description //TODO 新增KEY，有过期时间
     * @Date 2019/3/28 11:53
     * @Param [key, value, time 时间, timeUnit 单位]
     **/
    public void setExpire(String key, String value, long time, TimeUnit timeUnit) {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        ops.set(key, value, time, timeUnit);
    }

    /**
     * @Author: yangchenglong on 2019/6/27
     * @Description: 写入key、value、失效时间（秒）
     * update by:
     * @Param:
     * @return:
     */
    public boolean setExpireObject(String key, Object value, Long expireTime) {
        boolean result = false;
        try {
            ValueOperations<Serializable, Object> ops = redisTemplate.opsForValue();
            ops.set(key, objectSerialiable(value));
            result = redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new GlobalException(ResponseCodeEnum.RETURN_CODE_100500, e.getMessage());
        }
        return result;
    }

    //获取KEY
    public String get(String key) {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        return ops.get(key);
    }

    /**
     * @Author: yangchenglong on 2019/6/27
     * @Description: 获取key对应的值
     * update by:
     * @Param:
     * @return:
     */
    public Object getObject(String key) {
        Object result = null;
        ValueOperations<Serializable, Object> ops = redisTemplate.opsForValue();
        result = ops.get(key);
        return objectDeserialization((String)result);
    }

    //判断缓存中是否有对应的key
    public boolean exists(String key) {
        return redisTemplate.hasKey(key);
    }

    //删除KEY
    public void remove(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 加锁
     * @param key
     * @param value 当前时间+超时时间
     * @return
     */
    public boolean lock(String key, String value){
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        if(ops.setIfAbsent(key, value)){//分布式锁 如果不存在KEY就保存value并返回1，如果存在就返回0
            return true;
        }
        //如果锁超时
        String currentValue = stringRedisTemplate.opsForValue().get(key);
        if(!StringUtils.isBlank(currentValue) && Long.parseLong(currentValue) < System.currentTimeMillis()){
            //获取上一个锁的时间
            String oldValue  = stringRedisTemplate.opsForValue().getAndSet(key, value);
            if(!StringUtils.isEmpty(oldValue) && oldValue.equals(currentValue)){
                return true;
            }
        }
        return false;
    }

    /**
     * 解锁
     * @param key
     * @param value 当前时间+超时时间
     */
    public void unlock(String key, String value){
        try {
            String currentValue = stringRedisTemplate.opsForValue().get(key);
            if(!StringUtils.isEmpty(currentValue) && currentValue.equals(value)){
                redisTemplate.opsForValue().getOperations().delete(key);
            }
        } catch (Exception e) {
            log.error("redis 解锁异常", e);
            e.printStackTrace();
        }
    }

    /**
     * @Author: yangchenglong on 2019/6/27
     * @Description: 设置key失效时间
     * update by:
     * @Param:
     * @return:
     */
    public boolean expire(String key, Long expireTime) {
        boolean result = false;
        try {
            result = redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new GlobalException(ResponseCodeEnum.RETURN_CODE_100500, e.getMessage());
        }
        return result;
    }

    /**
     * @Author: yangchenglong on 2019/6/27
     * @Description: 对象序列化为字符串
     * update by:
     * @Param:
     * @return:
     */
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
     * @Author: yangchenglong on 2019/6/27
     * @Description: 字符串反序列化为对象
     * update by:
     * @Param:
     * @return:
     */
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

    /**
     * @Author: yangchenglong on 2019/6/27
     * @Description: 设置缓存
     * update by:
     * @Param:
     * @return:
     */
    public void setSessionCache(String key, Object value, Long time) {
        this.setExpireObject(key, value, time);
    }

    /**
     * @Author: yangchenglong on 2019/6/27
     * @Description: 刷新缓存
     * update by:
     * @Param:
     * @return:
     */
    public void refreshSessionCache(String key, Long time) {
        this.expire(key, time);
    }

}
