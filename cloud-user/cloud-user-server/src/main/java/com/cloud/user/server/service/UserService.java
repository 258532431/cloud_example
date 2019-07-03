package com.cloud.user.server.service;

import com.cloud.user.entity.User;

/**
 * @program: cloud_example
 * @description: 用户接口
 * @author: yangchenglong
 * @create: 2019-05-31 16:02
 */
public interface UserService extends BaseService<User>{

    User insertSelectiveGet(User user);

    User selectByUsername(String username);

    User login(String username, String password);

}
