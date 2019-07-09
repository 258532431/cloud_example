package com.cloud.user.server.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.cloud.common.base.BaseServiceImpl;
import com.cloud.common.utils.StringUtils;
import com.cloud.user.entity.Leave;
import com.cloud.user.server.feign.ActivitiFeign;
import com.cloud.user.server.mapper.LeaveMapper;
import com.cloud.user.server.service.LeaveService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: cloud_example
 * @description: 请假接口实现
 * @author: yangchenglong
 * @create: 2019-05-31 16:03
 */
@Service
@Transactional
public class LeaveServiceImpl extends BaseServiceImpl<Leave> implements LeaveService {

    @Resource
    private LeaveMapper leaveMapper;

    @Resource
    private ActivitiFeign activitiFeign;

    @Override
    public int insertSelective(Leave leave) {
        int result = leaveMapper.insertSelective(this.setEntity(leave));
        //启动工作流
        Map<String, Object> map = new HashMap<>();
        map.put("usercode", leave.getUserCode());//申请人
        map.put("auditorCode", "704528e0c93449aa9abba8906c4d2a77");//审核人
        map.put("leaveCode", leave.getLeaveCode());
        map.put("reason", leave.getContent());
        map.put("startDate", "2019-07-09 09:00");
        map.put("endDate", "2019-07-09 18:00");
        activitiFeign.startTask("leave", leave.getLeaveCode(), JSONObject.toJSONString(map));

        return result;
    }

    //处理新增数据
    private Leave setEntity(Leave leave){
        leave.setLeaveCode(StringUtils.getSerialNumber());
        leave.setAuditStatus(0);

        return leave;
    }

    @Override
    public Leave selectByLeaveCode(String leaveCode) {
        return leaveMapper.selectByLeaveCode(leaveCode);
    }

    @Override
    public List<Leave> selectByUserCode(String userCode) {
        return leaveMapper.selectByUserCode(userCode);
    }
}
