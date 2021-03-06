package com.cloud.gate.config;

import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Component
@Primary  //多个bean时 此类优先使用
public class SwaggerDocumentConfig implements SwaggerResourcesProvider {

    @Resource
    private RouteLocator routeLocator;

    @Override
    public List<SwaggerResource> get() {
        List resources = new ArrayList<>();
        resources.add(swaggerResource("网关授权API", "/gate/v2/api-docs", "1.0"));
        resources.add(swaggerResource("用户服务API", "/user/v2/api-docs", "1.0"));
        resources.add(swaggerResource("工作流服务API", "/activiti/v2/api-docs", "1.0"));
        resources.add(swaggerResource("消息队列服务API", "/mq/v2/api-docs", "1.0"));
        resources.add(swaggerResource("团队合作服务API", "/teambition/v2/api-docs", "1.0"));
        /* 自动映射API
        List<SwaggerResource> resources = new ArrayList<>();
        List<Route> routes = routeLocator.getRoutes();
        routes.forEach(route -> {
            resources.add(swaggerResource(route.getId(), route.getFullPath().replace("**", "v2/api-docs"), "1.0"));
        });*/
        return resources;
    }

    private SwaggerResource swaggerResource(String name, String location, String version) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion(version);
        return swaggerResource;
    }
}
