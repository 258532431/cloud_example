package com.cloud.user.service.impl;

import com.cloud.user.entity.User;
import com.cloud.user.mapper.UserMapper;
import com.cloud.user.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @program: cloud_example
 * @description: 用户接口实现
 * @author: yangchenglong
 * @create: 2019-05-31 16:03
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public User selectByPrimaryKey(Long id) {
        return this.userMapper.selectByPrimaryKey(id);
    }
}
