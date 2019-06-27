package com.cloud.gate.filter;

import com.cloud.common.config.GlobalException;
import com.cloud.common.enums.ResponseCodeEnum;
import com.cloud.common.utils.StringUtils;
import com.cloud.user.constant.UserConstants;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @program: cloud_example
 * @description: ZUUL请求过滤
 * @author: yangchenglong
 * @create: 2019-06-27 19:31
 */
@Component
public class RequestFilter extends ZuulFilter {

    /**
     * 指定该Filter的类型
     * ERROR_TYPE = "error";
     * POST_TYPE = "post";
     * PRE_TYPE = "pre";
     * ROUTE_TYPE = "route";
     */
    @Override
    public String filterType() {
        return "pre";
    }

    /**
     * 指定该Filter执行的顺序（Filter从小到大执行）
     * DEBUG_FILTER_ORDER = 1;
     * FORM_BODY_WRAPPER_FILTER_ORDER = -1;
     * PRE_DECORATION_FILTER_ORDER = 5;
     * RIBBON_ROUTING_FILTER_ORDER = 10;
     * SEND_ERROR_FILTER_ORDER = 0;
     * SEND_FORWARD_FILTER_ORDER = 500;
     * SEND_RESPONSE_FILTER_ORDER = 1000;
     * SIMPLE_HOST_ROUTING_FILTER_ORDER = 100;
     * SERVLET_30_WRAPPER_FILTER_ORDER = -2;
     * SERVLET_DETECTION_FILTER_ORDER = -3;
     */
    @Override
    public int filterOrder() {
        return 0;
    }

    /**
     * 指定需要执行该Filter的规则
     * 返回 true 则执行run()
     * 返回 false 则不执行run()
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run(){
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        HttpSession session = request.getSession();
        String refererUrl = request.getHeader("Referer");
        try {
            //如果是走swagger进来的请求，自动把token放入header
            if (StringUtils.isNotBlank(refererUrl) && refererUrl.endsWith("swagger-ui.html")) {
                ctx.addZuulRequestHeader(UserConstants.PC_ACCESS_TOKEN, (String) session.getAttribute("token"));
                ctx.addZuulRequestHeader(UserConstants.MOBILE_ACCESS_TOKEN, (String) session.getAttribute("token"));
            }
        } catch (Exception e) {
            throw new GlobalException(ResponseCodeEnum.RETURN_CODE_100500, e.getMessage());
        }
        return null;
    }

}
