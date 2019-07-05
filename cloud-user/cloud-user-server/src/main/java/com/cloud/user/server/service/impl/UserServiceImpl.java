package com.cloud.user.server.service.impl;

import com.cloud.common.config.GlobalException;
import com.cloud.common.enums.ResponseCodeEnum;
import com.cloud.common.base.BaseServiceImpl;
import com.cloud.common.utils.StringUtils;
import com.cloud.user.entity.User;
import com.cloud.user.server.mapper.UserMapper;
import com.cloud.user.server.service.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @program: cloud_example
 * @description: 用户接口实现
 * @author: yangchenglong
 * @create: 2019-05-31 16:03
 */
@Service
@Transactional
public class UserServiceImpl extends BaseServiceImpl<User> implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public int insert(User user) {
        return userMapper.insert(this.setEntity(user));
    }

    @Override
    public int insertSelective(User user) {
        return userMapper.insertSelective(this.setEntity(user));
    }

    //处理新增数据
    private User setEntity(User user){
        user.setUserCode(StringUtils.getSerialNumber());
        user.setCreateTime(new Date());
        user.setStatus(1);
        user.setPassword(StringUtils.md5(user.getPassword()));

        return user;
    }

    /**
     * @Author: yangchenglong on 2019/7/3
     * @Description: 新增并返回对象
     * update by: 
     * @Param: 
     * @return: 
     */
    @Override
    public User insertSelectiveGet(User user) {
        int result = this.insertSelective(user);
        if (result > 0)
            return user;
        
        return null;
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
        }else {
            throw new GlobalException(ResponseCodeEnum.RETURN_CODE_101005);
        }

        return user;
    }


}
