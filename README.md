#spring-boot-learn  
###spring boot 学习项目

####1、快速新建一个Spring-Boot Demo：hello-world
1、通过idea新建一个spring boot，观察pom.xml文件及基本文件结构  
1.1、 pom.xml  
1.1.1、 <parent>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>1.5.3.RELEASE</version>
        <relativePath/> 
    </parent>
    
1.1.2、 起步依赖  

     <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
     </dependencies>

    
1.2、 启动类 xxxApplication  

    @SpringBootApplication
    public class DemoApplication {
    
    	public static void main(String[] args) {
    		SpringApplication.run(DemoApplication.class, args);
    	}
    
    }
@SpringBootApplication 开启组件扫描及自动配置  
SpringApplication.run 负责启用引导应用程序  

1.3、配置文件 application 
有.yml 和 .properties 两种形式的文件，区别在于层级结构的写法不一样，通常用.yml，
因为层级结构明显  
端口口号配置  
    
    server.port: 端口

应用名称:  

    spring:
      application:
        name: hello-world
        
1.4、通过上述手动在该项目中建立第一个module，服务 hello-world  
启动  
![](/picture/0001.png "启动成功")

1.5 创建接口  
根据ddd模型，创建controller文件于 api.controller.HelloWorldController  
controller 文件必要注解 @RestController  
@RequestMapping 为请求方式，参数为路径，类似的有 GetMapping,PostMapping 等
![](/picture/0002.png)  

####2、spring-boot + mybatis（在上一步的hello-world模块下进行实验）  
2.1 添加依赖 mabatis 和 mysql

    <dependency>
        <groupId>org.mybatis.spring.boot</groupId>
        <artifactId>mybatis-spring-boot-starter</artifactId>
        <version>1.3.0</version>
    </dependency>
    
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>5.1.46</version>
    </dependency>
    
2.2 配置文件     
    
    #配置mapper xml 路径,下划线转驼峰
    mybatis:
      mapperLocations:
        - actuatorclasspath*:/mapper/*Mapper.xml
      configuration:
        mapUnderscoreToCamelCase: true
         
    #数据库配置
    spring:
      datasource:
        driver-class-name: com.mysql.jdbc.Driver
        url: jdbc:mysql://localhost:3306/zuul_route?useUnicode=true&characterEncoding=utf-8&useSSL=false
        username: root
        password: root

2.3 创建一个Dto类来作为接收查询到的数据的返回类型  
创建 com.jenson.api.dto.ApiGatewayDTO  

2.4 创建一个mapper接口类  com.jenson.infra.mapper.ApiGatewayMapper
使用@Mapper注解 或 在启动类中使用@MapperScan("mapper类所在的包")   
声明一个接口用于查询数据
![](/picture/0003.png) 

2.5 在配置文件mabatis配置的路径下，创建一个xxxMapper.xml文件，用来写sql  
![](/picture/0004.png)  

2.6 在controller中调用，测试能否实现数据库访问  
成功！返回数据被自动转换成json类型  
![](/picture/0005.png)  

####3、学习Actuator（在hello-world服务上进行） 
actuator（监控）功能提供了很多监控所需的接口  
 
1、起步依赖  

    <!--actuator-->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
        <version>1.5.14.RELEASE</version>
    </dependency>

起步依赖配置后即生效，
`注意：spring-boot-starter-parent 需要用1.5.3，版本过高会导致这一步出问题，访问不了对应的接口`

2、配置 application.yml  
    
    #如果不配置，则使用和Server相同的端口
    management:
      port: 8761
      security:
        enabled: false

3、访问 http://localhost:8761/health  
![](/picture/0006.png)  

####4、打包执行  
spring-boot 自带tomcat，打成jar包来执行更能体现微服务"微"的优势  

1、 spring-boot-maven-plugin 将Spring Boot应用打包为可执行的jar,文件名为<finalName>标签中的内容

    <build>
        <finalName>hello</finalName>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

2、打包  

     mvn clean && mvn package
     
![](/picture/0007.png) 

3、执行

    cd target/
    nohup java -jar hello.jar >temp.txt &

这种方法会把日志文件输入到你指定的文件中，没有则会自动创建。进程会在后台运行。  

4、关闭  
通过端口找出打开的jar包  
lsof(list open files)是一个列出当前系统打开文件的工具。  
参数：  
-i : port 列出对应端口文件的信息  
-n : 不将IP转换为hostname  
-p : 列出某个 PID 进程打开的文件  
-t  /path/file：显示/path/file的进程id号  

    kill -9 $(lsof -n -P -t -i:8760)

####5、EUREKA 
1、创建一个新的服务 eureka-server 
 
2、起步依赖  
    
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-eureka-server</artifactId>
        <version>1.3.5.RELEASE</version>
    </dependency>
    
3、启动类中添加注解
    
    @EnableEurekaServer

4、配置文件 application.yml 中
    
    eureka:
     instance:
       hostname: localhost
     client:
      #表示是否将自己注册到Eureka Server
      registerWithEureka: false
      #表示是否从Eureka Server获取注册信息
      fetchRegistry: false
      #设置与Eureka Server交互的地址，查询服务和注册服务都需要依赖这个地址。多个地址可使用 , 分隔。
      serviceUrl:
       defaultZone: http://${eureka.instance.hostname}:${server.port}/eureka/

5、启动  
![](/picture/0008.png) 

6、将hello-world服务注册到该eureka  
6.1、起步依赖  

    <!--eureka-client-->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-eureka</artifactId>
        <version>1.3.5.RELEASE</version>
    </dependency>

6.2、启动类注解  

    @EnableEurekaClient
    
6.3、application.yml 配置,eureka.serviceUrl 为eureka服务地址  

    eureka:
        serviceUrl:
          defaultZone: http://localhost:8000/eureka/
          
6.4、启动  
 ![](/picture/0009.png) 
 
####6、搭建高可用模式的服务注册中心的方法  
使用集群的方式，Eureka通过互相注册的方式来实现高可用的部署,多节点注册中心，此处使用双节点 注册中心

1、配置一下ip映射，模拟两台服务器上的分布式部署
    
    127.0.0.1 peer1  
    127.0.0.1 peer2
  
2、创建两个eureka服务分别为peer1:8000和peer2:8001,其defaultZone分别指向对方，即分别注册到对方到服务上  
修改上一步eureka-server 的配置文件 eureka.instance.hostname=peer1，defaultZone: http://peer2:8001/eureka/，作为peer1的eureka服务  
注意：两个eureka服务的服务名一致

3、启动  
访问 http://peer1:8000/  

 ![](/picture/0010.png) 
 
访问 http://peer2:8001/

 ![](/picture/0011.png) 
 
 此时在peer1中存在副本peer2，在peer2中村在副本peer1，但为available-replicas，不可用  
 将eureka配置中的registerWithEureka，fetchRegistry注释，重新启动
 
 ![](/picture/0012.png)
 ![](/picture/0013.png)
 此时副本由不可用变为可用，在注册的应用中也能看到 EUREKA-SERVER
 
 4、将服务注册到peer1和peer2中  
 
    eureka:
      client:
        serviceUrl:
          defaultZone: http://peer1:8000/eureka/,http://peer2:8001/eureka/
          
 在peer1和peer2中均能看到注册的服务。  
 将peer1停止，在peer2中仍然能看到注册在peer1中的服务
  ![](/picture/0014.png)
  

####7、Ribbon 负载均衡
1、此处采用RestTemplate+Ribbon的方式来调用接口，测试均衡负载   
创建一个服务ribbon-consumer作为作为消费者来调用接口  

2、引入依赖

    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-ribbon</artifactId>
        <version>1.3.5.RELEASE</version>
    </dependency>

3、使用RestTemplate来调用之前创建的服务hello-world的接口  
RestTemplate 是 org.springframework.web 中的类，不需要引入其他依赖,查看源码可知其对应不同类型的请求的用法  
接口的地址使用服务名称   
    
    @Autowired
    RestTemplate restTemplate;
    @GetMapping
    public String getHelloWorldApi(){
        return restTemplate.getForObject("http://localhost:8760/",String.class);
    }

4、 启动  
   直接使用会报错，需要在配置文件里定义一个bean
   ![](/picture/0015.png)
   
       @Configuration
       public class RibbonConfig {
        @Bean
        RestTemplate restTemplate(){
            return new RestTemplate();
        }
       }

5、再次启动，启动成功
![](/picture/0016.png)

6、在实际情况下是不可能直接在代码中写地址的，因为注册到了同一个eureka，所以可以利用服务名称来访问  
此时使用到之前引入到ribbon依赖  
在配置文件中加入 @LoadBalanced

将访问服务处代码 

    restTemplate.getForObject("http://localhost:8760/",String.class);
    
    改为
    
    restTemplate.getForObject("http://hello-world/",String.class);

7、重启，访问成功

8、为体现均衡负载，在hello-wold中将端口号输出，启动多个服务，在不同的端口号，重新访问

8.1 在idea中默认的单实例启动去掉，方便测试
![](/picture/0017.png)
mac版中为"允许平行运行"

8.2 将端口号从接口中返回  
获取配置文件中的信息采用@Value注解

    @Value("${server.port}")
	String port;//获取端口号
	
	@GetMapping("/")
    public String hello() {
        return "hello world：" + port;
    }

8.3 重新启动hello-world，修改端口号后再次启动
此时可以看到，同一个服务在两个地址启动来
![](/picture/0018.png)
![](/picture/0019.png)

8.4 访问 http://localhost:8764/getHello 
可以发现restTemplate在两个接口间交替调用，即，实现了均衡负载
![](/picture/0020.png)
![](/picture/0021.png)


####8、Hystrix  
在 上一步创建的服务 ribbon-cusnsumer 的基础上进行  

1、引入起步依赖  

       <dependency>
           <groupId>org.springframework.cloud</groupId>
           <artifactId>spring-cloud-starter-hystrix</artifactId>
           <version>1.3.5.RELEASE</version>
       </dependency>

2、在启动类中使用注解开启断路器功能 
    
    @EnableCircuitBreaker

3、为了使用注解@HystrixCommand 来指定回调方法，需要将上一步的中RestTemplate调用的代码放到一个Service的方法中

    @Service
    public class RibbonConsumerService {
    	@Autowired
    	RestTemplate restTemplate;
    
    	//调用接口，在该方法上增加回调注解
    	@HystrixCommand(fallbackMethod = "helloWorldApiFallBack")
    	public String getHelloWorldApi(){
    		return restTemplate.getForObject("http://hello-world/",String.class);
    	}
    
    	private String helloWorldApiFallBack(){
    		return "error";
    	}
    
    }

查看@HystrixCommand源码可知其参数及用法示例，参考示例代码写下回调函数。

4、在controller中写接口调用该函数,启动
    
    @RestController
    public class RibbonConsumerController {
        
        @Autowired
    	RibbonConsumerService ribbonConsumerService;
    
    	@GetMapping("/getHello")
    	public String getHelloWorldApi(){
    		return ribbonConsumerService.getHelloWorldApi();
    	}
    
    }

注意：此时在controller中调用自己定义的Service时需要@Autowired自动装载的注解，否则调用报错。

5、停掉hello-world服务，接口走回调函数，返回error
![](/picture/0022.png)  

####9、声明式调用Feign  

1、创建一个新的服务feign-consumer进行测试  
引入依赖：
    
    <!--feign-->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-feign</artifactId>
        <version>1.3.5.RELEASE</version>
    </dependency>
    
查看feign的pom文件可以看出feign默认引入来hystrix和ribbon，即feign具有熔断和均衡负载的功能。  
所以通常消费服务使用的是 feign
![](/picture/0023.png) 

2、利用feign来消费之前的hello-world服务 

2.1、开启Feign 支持，在启动类中增加 @EnableFeignClients 注解

2.2、创建一个service 来绑定服务和接口  
创建 com.jenson.app.service.helloService
    
    @Service
    @FeignClient("hello-world")
    public interface HelloService {
    	@GetMapping("/query")
    	List<Object> queryApiGateway();
    
    	@GetMapping("/")
    	String hello();
    }

利用  @FeignClient 来绑定服务
绑定接口 @xxMapping同定义接口处

2.3 在controller中创建一个接口来调用该接口来访问。
    
    @RestController
    public class FeignEurekaController {
    		@Autowired
    		private HelloService helloService;
    
    		@GetMapping
    		public Object queryApi(){
    			return helloService.queryApiGateway();
    		}
    
    		@GetMapping("/hello")
    		public String hello(){
    			return helloService.hello();
    		}
    }

启动。。。  
访问:http://localhost:8765/，http://localhost:8765/hello
![](/picture/0024.png) 
![](/picture/0025.png)   
两个服务都访问成功

3、测试均衡负载  
利用测试ribbon同样的方法测试  
频繁访问 http://localhost:8765/hello ,发现端口会产生变化，说明接口在交替调用

4、测试hystrix
通过观察@FeignClient注解的源码可知，有个参数叫 fallback 参数为类
  
    @FeignClient(name = "hello-world",fallback = HelloFallBack.class)

创建HelloFallBack，使用@Component注解定义为一个Spring组件，实现Feign调用服务的接口类
    
    @Component
    public class HelloFallBack implements HelloService {
        @Override
        public List<Object> queryApiGateway() {
            List<Object> objectList = (new ArrayList<Object>());
            objectList.add((Object) "error api");
            return (objectList);
        }
    
        @Override
        public String hello() {
            return "error hello";
        }
    }

启动，关闭hello-world服务。。。
实验失败，并没有调用fallback实现类  
原因：没有在配置文件application.yml中配置启用 hystrix 参数

    feign:
      hystrix:
        enabled: true

重启，成功进入到fallback类中
![](/picture/0026.png) 
![](/picture/0027.png)  

5、 fallbackFactory 通过源码看到一个参数名叫fallbackFactory  
fallbackFactory参数依然是个类，实现FallbackFactory，泛型为FeignClient接口类

5.1、改用 fallbackFactory 参数
    
    @FeignClient(name = "hello-world",fallbackFactory = HelloFallBackFactory.class)

5.2 创建一个继承FeignClient接口HelloService的接口HelloClientWithFallBack

5.3 创建 HelloFallBackFactory 
    
    @Component
    public class HelloFallBackFactory implements FallbackFactory<HelloService> {
    
    	private static final Logger LOGGER = LoggerFactory.getLogger(HelloFallBackFactory.class);
    
    	@Override
    	public HelloService create(Throwable cause) {
    
    		HelloFallBackFactory.LOGGER.info("fallback; reason was: {}", cause.getMessage());
    
    		return new HelloClientWithFallBack(){
    
    			@Override
    			public List<Object> queryApiGateway() {
    				List<Object> objectList = (new ArrayList<>());
    				objectList.add("error api from factory");
    				return (objectList);
    			}
    
    			@Override
    			public String hello() {
    				return "error hello from factory";
    			}
    		};
    	}
    }

 重启，如图，放回新的错误消息，走的facroty
 ![](/picture/0028.png) 
 ![](/picture/0029.png) 
  
  控制台中也输出了错误日志
  ![](/picture/0030.png) 
  
6、 @FeignClient参数configuration  

    @FeignClient(name = "hello-world",configuration = FeignClientConfig.class,fallbackFactory = HelloFallBackFactory.class)
    
创建 com.jenson.config.FeignClientConfig ,配置超时重试

    @Configuration
    public class FeignClientConfig {
    	@Bean
    	public Retryer feignRetryer(){
    		return new Retryer.Default(100,SECONDS.toMillis(1),5);
    	}
    
    }

更多配置参考 org.springframework.cloud.netflix.feign.FeignClientsConfiguration  
不晓得怎么测。。。。

####10、Api网关 Spring Cloud Zuul 
1、快速创建
1.1、创建一个新的服务zuul-service 作为网关服务

2、起步依赖
    
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-zuul</artifactId>
        <version>1.3.5.RELEASE</version>
    </dependency>
    
1.3、启动类增加注解

    @EnableZuulProxy
    
1.4、配置文件中配置网关
    
    zuul:
      prefix: /v1 #前缀，可用作版本号，可以没有
      routes:
        hiapi:
          path: /hello/**
          serviceId: hello-world   #利用serviceId的话会进行负载均衡，也可用url
    #      url: http://localhost:8762

1.5、启动，访问 localhost:5000/v1/hello/
![](/picture/0031.png)  
路由成功！

2、zuul 中的过滤器  

zull 网关有过滤器的功能，客户化过滤器

2.1、创建com.jenson.infra.filter.MyFilter 继承ZuulFilter

2.2、查看com.netflix.zuul.ZuulFilter源码注释，函数的含义及使用方法
    
    filterType:
    过滤器类型，有4种，pre，post,route,error
    filterOrder:
    过滤顺序，值越小越早执行该过滤器
    shouldFilter:
    表示该过滤器是否过滤逻辑，如果为true，则执行run()方法，如果为false，则不执行run()
    run:
    run()方法写具体的过滤逻辑
2.3 尝试在路由调用前进行一个校验逻辑
    
    @Component
    public class MyFilter extends ZuulFilter {
    	private static Logger log = LoggerFactory.getLogger(MyFilter.class);
    
    	@Override
    	public String filterType() {
    		return PRE_TYPE;
    	}
    
    	@Override
    	public int filterOrder() {
    		return 0;
    	}
    
    	@Override
    	public boolean shouldFilter() {
    		return true;
    	}
    
    	@Override
    	public Object run() {
    		RequestContext ctx = RequestContext.getCurrentContext();
    		HttpServletRequest request = ctx.getRequest();
    		log.info(String.format("%s >>> %s", request.getMethod(), request.getRequestURL().toString()));
    		//判断是否存在tocken参数，存在才允许路由，实际项目上要对值进行校验
    		Object accessToken = request.getParameter("token");
    		if (accessToken == null) {
    			log.warn("token is empty");
    			ctx.setSendZuulResponse(false);//不对其进行路由
    			ctx.setResponseStatusCode(401);
    			try {
    				ctx.getResponse().getWriter().write("token is empty");
    			} catch (Exception e) {
    				return null;
    			}
    		}
    		log.info("ok");
    		return null;
    	}
    }

重启，访问 localhost:5000/v1/hello/
产生预期的校验失败的效果  
![](/picture/0032.png)  
![](/picture/0033.png)  

增加一个tocken参数再尝试访问,访问:localhost:5000/v1/hello?token=xxx  
产生预期的效果，访问成功
![](/picture/0034.png) 
![](/picture/0035.png) 

3、zuul 中的hystrix   
断路器实现的是ZuulFallbackProvider接口  
继承了两个函数  
    
    getRoute //似乎是回退的路由，具体有是吗用看不太懂，"*"代表所有路由吧
    fallbackResponse //放回的是一个ClientHttpResponse，可以创建一个匿名类再实现里面的函数

ClientHttpResponse中的函数
    
    HttpHeaders
    InputStream
    HttpStatus
    getRawStatusCode
    getStatusText
    close
    
实现匿名接口类中函数如下(具体不太懂，但这样可以放回消息):  

    @Component
    public class MyFallbackProvider implements ZuulFallbackProvider {
    	@Override
    	public String getRoute() {
    		return "*";
    	}
    
    	@Override
    	public ClientHttpResponse fallbackResponse() {
    		return new ClientHttpResponse(){
    
    			@Override
    			public HttpHeaders getHeaders() {
    				HttpHeaders headers = new HttpHeaders();
    				headers.setContentType(MediaType.APPLICATION_JSON);
    				return headers;
    
    			}
    
    			@Override
    			public InputStream getBody() throws IOException {
                    JSONObject bodyJson = new JSONObject();
                    bodyJson.put("msg","error, i'm the zull fallback");
                    return new ByteArrayInputStream(bodyJson.toString().getBytes());

    
    			}
    
    			@Override
    			public HttpStatus getStatusCode() throws IOException {
    				return HttpStatus.OK;
    			}
    
    			@Override
    			public int getRawStatusCode() throws IOException {
    				return 200;
    			}
    
    			@Override
    			public String getStatusText() throws IOException {
    				return "OK";
    			}
    
    			@Override
    			public void close() {
    
    			}
    		};
    	}
    }

为了让它返回json，上一步用到了 JSONObject，需要引入一个json的依赖  

            <dependency>
                <groupId>net.minidev</groupId>
                <artifactId>json-smart</artifactId>
                <version>2.3</version>
                <scope>compile</scope>
            </dependency>


重启，停调hello-world服务，测试，访问：http://localhost:5000/v1/hello?token=xxx

达到预期效果:
![](/picture/0036.png)  

修改getRoute分别为 null 和 hello-world再尝试,发现改成null后并不影响回调，这部分需要在研究

4、尝试从数据库中读取配置信息

4.1、首先，先在配置文件中将路由信息注释掉

4.2 编写客户化网关定位代码
    
    public class CustomRouteLocator extends SimpleRouteLocator implements RefreshableRouteLocator {
    
    	public final static Logger logger = LoggerFactory.getLogger(CustomRouteLocator.class);
    	private ZuulProperties properties;
    
    	public CustomRouteLocator(String servletPath, ZuulProperties properties) {
    		super(servletPath, properties);
    		this.properties = properties;
    	}
    
    	@Override
    	public void refresh() {
    		doRefresh();
    	}
    
    	@Override
    	protected Map<String, ZuulProperties.ZuulRoute> locateRoutes() {
    		LinkedHashMap<String, ZuulProperties.ZuulRoute> routesMap = new LinkedHashMap<>();
    		//从application.properties中加载路由信息
    		routesMap.putAll(super.locateRoutes());
    		//从db中加载路由信息
    //		routesMap.putAll(locateRoutesFromDB());
    		//自定义的路由信息
    		ZuulProperties.ZuulRoute customZuulRoute = new ZuulProperties.ZuulRoute();
    		customZuulRoute.setId("abcd1234");
    		customZuulRoute.setPath("/hello/**");
    		customZuulRoute.setServiceId("hello-world");
    //		customZuulRoute.setUrl("http://localhost:8762");
    		routesMap.put("/hello/**",customZuulRoute);
    
    		//优化一下配置
    		LinkedHashMap<String, ZuulProperties.ZuulRoute> values = new LinkedHashMap<>();
    		for (Map.Entry<String, ZuulProperties.ZuulRoute> entry : routesMap.entrySet()) {
    			String path = entry.getKey();
    			// Prepend with slash if not already present.
    			if (!path.startsWith("/")) {
    				path = "/" + path;
    			}
    			if (StringUtils.hasText(this.properties.getPrefix())) {
    				path = this.properties.getPrefix() + path;
    				if (!path.startsWith("/")) {
    					path = "/" + path;
    				}
    			}
    			values.put(path, entry.getValue());
    		}
    		return values;
    	}
    }

重点在重写locateRoutes函数的时候自定义路由信息（信息可以从数据库获取）

4.3 编写配置类

    @Configuration
    public class CustomZuulConfig {
    	@Autowired
    	ZuulProperties zuulProperties;
    	@Autowired
    	ServerProperties server;
    
    	@Bean
    	public CustomRouteLocator routeLocator() {
    		CustomRouteLocator routeLocator = new CustomRouteLocator(this.server.getServletPrefix(), this.zuulProperties);
    
    		return routeLocator;
    	}
    }

4.4 、代码分析

####11、配置中心

1、创建一个配置中心的服务 config-server
    
1.1、引入依赖
    
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-config-server</artifactId>
        <version>1.3.3.RELEASE</version>
    </dependency>

1.2、 启动类增加注解 @EnableConfigServer

1.3、从git仓库读取配置文件，配置application.yml

    spring:
      cloud:
        config:
          server:
            git:
              uri:  xxxx
              searchPaths: respo
              username: 11282
              password: ****
            lable: master


    
####12、JPA的使用  

1、依赖

            <dependency>
                <groupId>mysql</groupId>
                <artifactId>mysql-connector-java</artifactId>
                <scope>Compile</scope>
            </dependency>
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-data-jpa</artifactId>
            </dependency>

2、配置文件
    
    spring:
      jpa:
        hibernate:
          ddl-auto: update
        show-sql: true

3、实体类

    @Data
    @Entity
    @Table(name="account")
    public class Account {
    	/**
    	 * 主键
    	 */
    	@Id
    	@GeneratedValue(strategy = GenerationType.IDENTITY)
    	private String id;
    	/**
    	 * 用户名
    	 */
    	@Column(nullable = false,unique = true)
    	private String userName;
    	/**
    	 * 密码
    	 */
    	@Column(nullable = false)
    	private String passWord;
    	/**
    	 * 角色
    	 */
    	@Column
    	private String[] roles;
    	
    }
    
重点：@Entity，@Table，@Id，@GeneratedValue，@Column 均在javax.persistence.* 中

4、资源实现接口

    public interface AccountRepository extends JpaRepository<Account,Long> {
    }
    
  
####13、Spring Security  

1、依赖
    
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
    
仅配置此依赖即可自动启动Spring Security

启动后会在控制台看到自动生成的密码 

    Using default security password: 7d579547-95f8-412d-8b17-7d84079934c1

账号为 user
    
2、自定义用户服务信息

2.1 使用内存签名服务

继承WebSecurityConfigurerAdapter，重写configure
    
    import org.springframework.context.annotation.Configuration;
    import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
    import org.springframework.security.config.annotation.authentication.configurers.provisioning.InMemoryUserDetailsManagerConfigurer;
    import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
    import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
    import org.springframework.security.crypto.password.PasswordEncoder;
    
    @Configuration
    public class SecurityConfig extends WebSecurityConfigurerAdapter {
    
    	@Override
    	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    		// 密码编辑器
    		PasswordEncoder passwordEncoder =new BCryptPasswordEncoder();
    		// 使用内存存储
    		InMemoryUserDetailsManagerConfigurer<AuthenticationManagerBuilder> userConfig
    				= auth.inMemoryAuthentication()
    				// 设置密码编辑器
    				.passwordEncoder(passwordEncoder);
    		// 注册用户admin，密码为abc，并赋予USER和ADMIN角色权限
    		userConfig.withUser("admin")
    				.password(passwordEncoder.encode("abc"))
    				.authorities("ROLE_USER","ROLE_ADMIN");
    		// 注册用户myuser，密码为123456，并赋予USER和ADMIN角色权限
    		userConfig.withUser("myuser")
    				.password(passwordEncoder.encode("123456"))
    				.authorities("ROLE_ADMIN");
    
    	}
    }

调用接口后出现如下提示，输入账号密码接口调通

![](/picture/0037.png)  

2.2 基于数据库表进行验证
    
    各种失败报错，放弃了～～～～～～～

2.3 配置自定义的用户服务

2.3.1、
    
继承WebSecurityConfigurerAdapter,增加一个加密方式，暂时不清楚是否可以把这个bean拿到其他地方

    // 加密方式
    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
实现官方的 UserDetailsService，如下

    @Component
    @Slf4j
    public class MyUserDetailsService implements UserDetailsService {
    
    	@Autowired
    	private PasswordEncoder passwordEncoder;
    	@Autowired
    	private RoleMapper roleMapper;
    	@Autowired
    	private UserMapper userMapper;
    
    	@Override
    	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
    		User user = userMapper.findByUserName(userName);
    		if (user == null) {
    			throw new AuthenticationCredentialsNotFoundException("authError");
    		}
    		log.info("{}", user);
    		List<Role> role = roleMapper.findByUserName(userName);
    		log.info("{}", role);
    		List<GrantedAuthority> authorities = new ArrayList<>();
    		role.forEach(role1 -> authorities.addAll(AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_" + role1.getRoleName().toUpperCase())));
    		log.info("{}", authorities);
    		return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(), authorities);
    	}
    }
    
利用mybatis获取验证信息，这里用了@Component的话mapper接口就需要用@Mapper注解，具体为什么不清楚

以上就实现了自定义账号密码

####14、Spring Security 案例学习 2 

1、构建工程 springboot-security

引入依赖，同14节，多引入页面渲染多依赖
    
    <!--security-->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
    <!--html页面渲染-->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-thymeleaf</artifactId>
    </dependency>
    <dependency>
        <groupId>org.thymeleaf.extras</groupId>
        <artifactId>thymeleaf-extras-springsecurity4</artifactId>
    </dependency>
    
同14节配置类SecurityConfig继承自WebSecurityConfigurerAdapter，多加了注解@EnableWebSecurity  
 
定义configureGloba来实现用户配置在内存中,使用@Autowired注解，产生效果和14节2.1相同

    @EnableWebSecurity
    @Configuration
    public class SecurityConfig extends WebSecurityConfigurerAdapter {
    
    	@Autowired
    	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
    		auth.inMemoryAuthentication()
    				.withUser("jenson")
    				.password("123").authorities("ROLE_USER");
    	}
    }
    
在 SecurityConfig 类名上增加注解 @EnableGlobalMethodSecurity(prePostEnabled = true)，可设定
用户对某个控制层的方法是否具有访问权限

增加此注解后控制层对应接口函数上使用注解 @PreAuthorize("hasRole('ROLE_ADMIN')") 设定可访问角色

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    	@GetMapping
    	String test(){
    		return "test~~";
    	} 
    
之后使用user用户访问后报403错误，用admin角色访问后可成功调用。
    
2、配置HttpSecurity，重写WebSecurityConfigurerAdapter的 参数为HttpSecurity的configure方法

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                // 不需要验证可直接访问的资源
                .antMatchers("/css/**", "/index").permitAll()
                // 以下资源需要验证，仅能用USER角色来验证
                .antMatchers("/user/**").hasRole("USER")
                .antMatchers("/blogs/**").hasRole("USER")
                .and()
                // 表单登陆地址及登陆失败地址
                .formLogin().loginPage("/login").failureUrl("/login-error")
                .and()
                // 异常处理定位界面
                .exceptionHandling().accessDeniedPage("/401");

        // 注销成功后重定向到首页
        http.logout().logoutSuccessUrl("/");
    	}

对应controller层定义如下

    @Controller
    public class MainController {
    	@RequestMapping("/")
    	public String root() {
    		return "redirect:/index";
    	}
    
    	@RequestMapping("/index")
    	public String index() {
    		return "index";
    	}
    
    	@RequestMapping("/user/index")
    	public String userIndex() {
    		return "user/index";
    	}
    
    	@RequestMapping("login")
    	public String login() {
    		return "login";
    	}
    
    	@RequestMapping("/login-error")
    	public String loginError(Model model) {
    		model.addAttribute("loginError", true);
    		return "login";
    	}
    
    	@GetMapping("/401")
    	public String accessDenied() {
    		return "401";
    	}
    }
    
相关页面编写  
    
配置文件：
    
    spring:
      thymeleaf:
        mode: HTML5
        encoding: UTF-8
        cache: false

html页面编写在  /resource/templates/* 下

3、利用 auth.userDetailsService() 来创建用户

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService());
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService(){
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername("jenson").password("1234").roles("USER").build());
        manager.createUser(User.withUsername("admin").password("admin").roles("USER","ADMIN").build());
        return manager;
    }
    
4、从数据库中读取用户认证信息，使用jpa

4.1、引入jpa和jpa配置
    
4.2、同14节2.3.1 依靠 实现 UserDetailsService接口，重写loadUserByUsername，返回UserDetails来实现

    @Service
    public class UserService implements UserDetailsService {
    
    	@Autowired
    	private UserDao userRepository;
    	@Override
    	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    		return userRepository.findByUsername(username);
    	}
    }
    
####15、使用Spring Cloud OAuth2 

一、编写授权服务

1、oath2的表结构是固定的

2、引入依赖，oauth2依赖,使用的是jpa连接数据库
    
    <dependencies>
            <!--oauth2-->
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-starter-oauth2</artifactId>
            </dependency>
            <!--mysql-->
            <dependency>
                <groupId>mysql</groupId>
                <artifactId>mysql-connector-java</artifactId>
            </dependency>
            <!--jpa-->
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-data-jpa</artifactId>
            </dependency>
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-web</artifactId>
            </dependency>
            <!--eureka-client-->
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-starter-eureka</artifactId>
                <version>1.3.5.RELEASE</version>
            </dependency>
        </dependencies>

3、配置文件，除数据库相关配置外主要配置如下

    server:
      context-path: /uaa
    security:
      oauth2:
        resource:
          filter-order: 3

4、配置spring security ，继承WebSecurityConfigurerAdapter，同上两节学习的继承自同一类。

5、从数据库中获取用户密码同14节

6、配置AuthorizationServer，在启动类中

7、暴露Remote Token Services接口

8、获取token

curl获取:  curl service-hi:123456@localhost:8050/uaa/oauth/token -d grant_type=password -d username=jensondb2 -d password=1234
![](/picture/0038.png)  

postman 获取

[{"key":"Authorization","value":"Basic c2VydmljZS1oaToxMjM0NTY=","description":""}]

c2VydmljZS1oaToxMjM0NTY 为 service-hi:123456 base64编码

![](/picture/0040.png)  
![](/picture/0039.png)  

二、编写service-hi受保护资源服务

1、依赖
    
    <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-starter-eureka</artifactId>
                <version>1.3.5.RELEASE</version>
            </dependency>
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-web</artifactId>
            </dependency>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-starter-feign</artifactId>
                <version>1.4.5.RELEASE</version>
            </dependency>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-starter-oauth2</artifactId>
                <version>1.2.1.RELEASE</version>
            </dependency>
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-data-jpa</artifactId>
            </dependency>
            <dependency>
                <groupId>mysql</groupId>
                <artifactId>mysql-connector-java</artifactId>
            </dependency>
        </dependencies>
        
2、配置文件

security配置如下：
    
    security:
      oauth2:
        resource:
          # 用于获取当前Token的用户信息
          user-info-uri: http://localhost:8050/uaa/users/current
        client:
          # 以下配置需与uaa服务中配置一致
          client-id: service-hi
          client-secret: 123456
          access-token-uri: http://localhost:8050/uaa/oauth/token
          grant-type: client_credentials,password
          scope: server               

3、Resource Server配置类,继承自ResourceServerConfigurerAdapter，配置类似于Spring Security的HttpSecurity配置  

    @Configuration
    @EnableResourceServer
    @EnableGlobalMethodSecurity(prePostEnabled = true)
    public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    
    	@Override
    	public void configure(HttpSecurity http) throws Exception{
    		http.authorizeRequests()
    				.antMatchers("/user/registry").permitAll()
    				.anyRequest().authenticated();
    	}
    }
    
4、配置OAuth2 Client来访问被保护资源

    @EnableOAuth2Client
    @EnableConfigurationProperties
    @Configuration
    public class OAth2ClientConfig {
    
    	// 配置受保护的资源信息，通过读取配置文件中security.oauth2.client中的配置来获取bean的属性
    	@Bean
    	@ConfigurationProperties(prefix = "security.oauth2.client")
    	public ClientCredentialsResourceDetails clientCredentialsResourceDetails(){
    		return new ClientCredentialsResourceDetails();
    	}
    
    	/**
    	 *  @EnableOAuth2Client.
    	 * 1.oauth2ClientContextFilter
    	 * 2.AccessTokenRequest
    	 */
    	// 配置过滤器，存储当前请求和上下文
    	@Bean
    	public RequestInterceptor oauth2FeignRequestInterceptor(){
    		return new OAuth2FeignRequestInterceptor(new DefaultOAuth2ClientContext(), clientCredentialsResourceDetails());
    	}
    
    	// 用于向Uaa服务请求
    	@Bean
    	public OAuth2RestTemplate clientCredentialsRestTemplate() {
    		return new OAuth2RestTemplate(clientCredentialsResourceDetails());
    	}
    }

    
5、编写测试接口
    
注意：在配置文件中 security.oauth2.resource.user-info-uri 的配置出错会导致校验不成功




