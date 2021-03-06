package com.cloud.user.server.mapper;

import com.cloud.common.base.BaseMapper;
import com.cloud.user.entity.User;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * @program: cloud_example
 * @description: 用户mapper接口
 * @author: yangchenglong
 * @create: 2019-05-31 16:02
 */
public interface UserMapper extends BaseMapper<User> {

    User selectByUsername(String username);
}