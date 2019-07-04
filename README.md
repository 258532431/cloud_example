# cloud_example 项目介绍        
1、此项目为cloud微服务框架项目      
2、数据库使用mysql，数据库名在yml配置文件查看, 数据库表在sql文件夹       
3、运行项目需要安装redis、rabbitmq    
4、maven的settings.xml文件在项目根目录   
5、运行内存配置VM Options： -Xms10m -Xmx200m       
6、运行项目设置Active profiles：dev         

# 项目技术点        
1、用户鉴权信息使用redis存储        
2、rabbitmq消息中间件(rabbitmq集群暂未配置)       
3、redis缓存（redis集群暂未配置）      
4、zuul路由转发      
5、feign服务接口调用，hystrix熔断机制，ribbon负载均衡   
6、swagger api接口展示（地址：http://127.0.0.1:8410/swagger-ui.html）       
7、LCN解决分布式事务（暂未配置）      

# 前端vue框架介绍         
1、除登录接口外，调用其他接口需在header中传入token值，token的参数名由前后端双方约定         

# 相关知识-如需使用spring redis session 问题                
前端使用vue axios 进行ajax请求需配置：        
axios.defaults.withCredentials = true   //解决跨域造成的每次sessionId不一样          

# 相关知识-跨域请求       
注解：@CrossOrigin

# 相关知识-git忽略提交target文件夹
1、git rm -r --cached target   
2、git push origin master

# 相关知识-lombock说明    
@Data 注解在类上；提供类所有属性的 getting 和 setting 方法，此外还提供了equals、canEqual、hashCode、toString 方法    
@Setter ：注解在属性上；为属性提供 setting 方法    
@Getter ：注解在属性上；为属性提供 getting 方法    
@Log4j ：注解在类上；为类提供一个 属性名为log 的 log4j 日志对象   
@NoArgsConstructor ：注解在类上；为类提供一个无参的构造方法     
@AllArgsConstructor ：注解在类上；为类提供一个全参的构造方法    
@Cleanup : 可以关闭流    
@Builder ： 被注解的类加个构造者模式     
@Synchronized ： 加个同步锁   
@SneakyThrows : 等同于try/catch 捕获异常   
@NonNull : 如果给参数加个这个注解 参数为null会抛出空指针异常      
@Value : 注解和@Data类似，区别在于它会把所有成员变量默认定义为private final修饰，并且不会生成set方法。       
@toString：注解在类上；为类提供toString方法（可以添加排除和依赖）；      
官方文档https://projectlombok.org/features/index.html

    
