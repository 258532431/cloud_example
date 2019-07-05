package com.cloud.user.server.service.impl;

import com.cloud.common.config.GlobalException;
import com.cloud.common.enums.ResponseCodeEnum;
import com.cloud.common.mybatis.BaseServiceImpl;
import com.cloud.common.utils.StringUtils;
import com.cloud.user.entity.Leave;
import com.cloud.user.entity.User;
import com.cloud.user.server.mapper.LeaveMapper;
import com.cloud.user.server.mapper.UserMapper;
import com.cloud.user.server.service.LeaveService;
import com.cloud.user.server.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @program: cloud_example
 * @description: 请假接口实现
 * @author: yangchenglong
 * @create: 2019-05-31 16:03
 */
@Service
public class LeaveServiceImpl extends BaseServiceImpl<Leave> implements LeaveService {

    @Resource
    private LeaveMapper leaveMapper;

    @Override
    public Leave selectByLeaveCode(String leaveCode) {
        return leaveMapper.selectByLeaveCode(leaveCode);
    }

    @Override
    public List<Leave> selectByUserCode(String userCode) {
        return leaveMapper.selectByUserCode(userCode);
    }
}
