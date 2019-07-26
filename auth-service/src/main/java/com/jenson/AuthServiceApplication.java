package com.jenson;

import com.jenson.app.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.sql.DataSource;

@EnableDiscoveryClient
@SpringBootApplication
@EnableResourceServer
public class AuthServiceApplication {
	@Autowired
	@Qualifier("dataSource")
	private DataSource dataSource;

	public static void main(String[] args) {
		SpringApplication.run(AuthServiceApplication.class, args);
	}

	@Configuration
	@EnableAuthorizationServer  //开启授权服务的功能
	protected  class OAuth2AuthorizationConfig extends AuthorizationServerConfigurerAdapter {

		//private TokenStore tokenStore = new InMemoryTokenStore();

		JdbcTokenStore tokenStore=new JdbcTokenStore(dataSource);

		@Autowired
		@Qualifier("authenticationManagerBean")
		private AuthenticationManager authenticationManager;

		@Autowired
		private UserService userServiceDetail;


		@Override
		public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

			//客户端信息存在内存中
			clients.inMemory()
					//创建一个clientId为browser的客户端
					.withClient("browser")
					//配置验证类型
					.authorizedGrantTypes("refresh_token", "password")
					//客户端域为"ui"
					.scopes("ui")
					.and()
					.withClient("service-hi")
					.secret("123456")
					.authorizedGrantTypes("client_credentials", "refresh_token","password")
					.scopes("server");

		}

		@Override
		public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
			endpoints
					//tocken 的存储方式
					.tokenStore(tokenStore)
					//开启密码类型的验证，需要在WebSecurityConfigurerAdapter中配置 AuthenticationManager 这个bean
					.authenticationManager(authenticationManager)
					//用来读取用户验证信息
					.userDetailsService(userServiceDetail);
		}

		@Override
		public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
			oauthServer
					.tokenKeyAccess("permitAll()")
					.checkTokenAccess("isAuthenticated()");

		}
	}
}
