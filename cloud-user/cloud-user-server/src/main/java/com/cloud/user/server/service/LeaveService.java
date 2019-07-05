package com.cloud.user.server.service;

import com.cloud.common.mybatis.BaseService;
import com.cloud.user.entity.Leave;

import java.util.List;

/**
 * @program: cloud_example
 * @description: 请假接口
 * @author: yangchenglong
 * @create: 2019-05-31 16:02
 */
public interface LeaveService extends BaseService<Leave> {

    Leave selectByLeaveCode(String leaveCode);

    List<Leave> selectByUserCode(String userCode);

}
