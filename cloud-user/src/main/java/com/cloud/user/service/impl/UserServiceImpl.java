package com.cloud.user.service.impl;

import com.cloud.user.config.GlobalException;
import com.cloud.user.entity.User;
import com.cloud.user.enums.ResponseCodeEnum;
import com.cloud.user.mapper.UserMapper;
import com.cloud.user.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.UUID;

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
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    public int insertSelective(User user) {
        user.setUserCode(UUID.randomUUID().toString());
        user.setCreateTime(new Date());
        user.setStatus(0);
        return userMapper.insertSelective(user);
    }

    @Override
    public User selectByUsername(String username) {
        return userMapper.selectByUsername(username);
    }

    @Override
    public User login(String username, String password) {
        User user = this.selectByUsername(username);
        if(user != null){
            if(!user.getPassword().equals(password)){
                throw new GlobalException(ResponseCodeEnum.RETURN_CODE_101001);
            }
            if(user.getStatus() != 1) {
                throw new GlobalException(ResponseCodeEnum.RETURN_CODE_101004);
            }
        }

        return user;
    }


}
