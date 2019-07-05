package com.cloud.user.server.mapper;

import com.cloud.common.mybatis.BaseMapper;
import com.cloud.user.entity.Leave;

import java.util.List;

/**
 * @Author: yangchenglong on 2019/7/5
 * @Description: 请假mapper接口
 * update by:
 */
public interface LeaveMapper extends BaseMapper<Leave> {

    Leave selectByLeaveCode(String leaveCode);

    List<Leave> selectByUserCode(String userCode);

}