package com.cloud.user.server.mapper;

import com.cloud.common.base.BaseMapper;
import com.cloud.user.entity.Leave;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

/**
 * @Author: yangchenglong on 2019/7/5
 * @Description: 请假mapper接口
 * update by:
 */
@Qualifier("leaveMapper")
public interface LeaveMapper extends BaseMapper<Leave> {

    Leave selectByLeaveCode(String leaveCode);

    List<Leave> selectByUserCode(String userCode);

}