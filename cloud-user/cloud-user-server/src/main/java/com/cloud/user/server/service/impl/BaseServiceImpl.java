package com.cloud.user.server.service.impl;

import com.cloud.common.config.GlobalException;
import com.cloud.common.enums.ResponseCodeEnum;
import com.cloud.common.utils.StringUtils;
import com.cloud.user.entity.User;
import com.cloud.user.server.mapper.BaseMapper;
import com.cloud.user.server.mapper.UserMapper;
import com.cloud.user.server.service.BaseService;
import com.cloud.user.server.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @program: cloud_example
 * @description: 基础接口实现
 * @author: yangchenglong
 * @create: 2019-05-31 16:03
 */
@Service
public abstract class BaseServiceImpl<T> implements BaseService<T> {

    @Resource
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
