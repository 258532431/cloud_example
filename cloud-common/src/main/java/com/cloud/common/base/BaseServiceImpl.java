package com.cloud.common.base;

import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

/**
 * @program: cloud_example
 * @description: 基础接口实现
 * @author: yangchenglong
 * @create: 2019-05-31 16:03
 */
public abstract class BaseServiceImpl<T> implements BaseService<T> {

    @Autowired
    private BaseMapper<T> baseMapper;

    @Override
    public int insert(T t) {
        return baseMapper.insert(t);
    }

    @Override
    public int insertSelective(T t) {
        return baseMapper.insertSelective(t);
    }

    @Override
    public int deleteByPrimaryKey(Object key) {
        return baseMapper.deleteByPrimaryKey(key);
    }

    @Override
    public int updateByPrimaryKeySelective(T t) {
        return baseMapper.updateByPrimaryKeySelective(t);
    }

    @Override
    public int updateByPrimaryKey(T t) {
        return baseMapper.updateByPrimaryKey(t);
    }

    @Override
    public T selectByPrimaryKey(Object key) {
        return baseMapper.selectByPrimaryKey(key);
    }
}
