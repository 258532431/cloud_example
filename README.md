# cloud_example 项目介绍        
1、此项目为cloud微服务框架项目      
2、数据库使用mysql，数据库名在yml配置文件查看, 数据库表在sql文件夹       
3、运行项目需要安装redis、rabbitmq    
4、maven的settings.xml文件在项目根目录   
5、运行内存配置VM Options： -Xms10m -Xmx200m       
6、运行项目设置Active profiles：dev      
7、项目依次启动顺序：center、其他         

# 项目技术点        
1、用户鉴权信息使用redis存储（做单点登录）        
2、rabbitmq消息中间件、消费确认机制(rabbitmq集群暂未配置)       
3、redis缓存（redis单服务器、redis集群）      
4、zuul路由转发（zuul默认使用ribbon做负载均衡）        
5、feign服务接口调用，hystrix熔断机制，ribbon负载均衡   
6、swagger api接口展示（dev环境地址：http://127.0.0.1:8410/swagger-ui.html）       
7、tx-lcn分布式事务(会导致系统性能下降，建议通过中间件和补偿机制实现事务)https://www.txlcn.org/zh-cn/docs/start.html             
8、activiti工作流(调用swagger接口创建新模型会自动建表)        

# redis单机配置     
1、redis服务用单机模式启动（只启动一个redis服务）          
2、将项目中 RedisClusterConfig 类中的注解注释掉，注释掉yml配置文件中的redis集群配置项，注释掉pom文件中jedis引入和排除lettuce的exclusions配置       

# windows的redis集群配置（redis版本3.0）                
1、配置三主三从，redis配置文件例子见项目根目录的redis集群目录，不设置redis密码               
2、redis5.0以下版本需安装ruby（这里版本Ruby+Devkit2.6.3-1 (x64)），下载地址：https://rubyinstaller.org/downloads/           
3、安装Redis的Ruby驱动redis-xxxx.gem，安装文件放在了项目根目录的redis集群目录rubygems-3.0.4.zip(https://rubygems.org/pages/download)，解压后执行ruby setup.rb               
4、用 GEM 安装 Redis ：切换到redis安装目录，需要在命令行中，执行 gem install redis         
5、项目根目录的redis集群目录下的redis-trib保存到一个Redis的目录下（如果你是一个redis服务一个文件夹，不用每个都放，我这里是用一个文件夹多个配置文件）       
6、依次启动6个redis服务     
7、使用命令创建集群：redis-trib.rb create --replicas 1 127.0.0.1:6380 127.0.0.1:6381 127.0.0.1:6382 127.0.0.1:6383 127.0.0.1:6384 127.0.0.1:6385        
8、输入yes确认（--replicas 1 表示每个主数据库拥有从数据库个数为1。master节点不能少于3个，所以我们用了6个redis）         
9、如果出现 redis-trib.rb is not longer available! 如果redis版本是5.0以上，则使用如下命令:  redis-cli --cluster create 节点... --cluster-replicas 1       
10、将项目中 RedisConfiguration 类中的注解注释掉，注释掉yml配置文件中的redis单机配置项              

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

    
