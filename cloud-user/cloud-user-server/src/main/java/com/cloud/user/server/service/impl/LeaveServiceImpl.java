package com.cloud.user.server.service.impl;

import com.cloud.common.base.BaseServiceImpl;
import com.cloud.user.entity.Leave;
import com.cloud.user.server.mapper.LeaveMapper;
import com.cloud.user.server.service.LeaveService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @program: cloud_example
 * @description: 请假接口实现
 * @author: yangchenglong
 * @create: 2019-05-31 16:03
 */
@Service("leaveService")
public class LeaveServiceImpl extends BaseServiceImpl<Leave> implements LeaveService {

    @Resource
    @Qualifier("leaveMapper")
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