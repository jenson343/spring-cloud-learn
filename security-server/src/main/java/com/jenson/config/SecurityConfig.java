package com.jenson.config;

import com.jenson.app.service.impl.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.InMemoryUserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	/**
	 * 1、使用内存签名服务
	 */
	//身份验证管理生成器
//	@Override
//	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		// 密码编辑器
//		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//		// 使用内存存储
//		InMemoryUserDetailsManagerConfigurer<AuthenticationManagerBuilder> userConfig
//				= auth.inMemoryAuthentication()
//				// 设置密码编辑器
//				.passwordEncoder(passwordEncoder);
//		// 注册用户admin，密码为abc，并赋予USER和ADMIN角色权限
//		userConfig.withUser("admin")
//				.password(passwordEncoder.encode("abc"))
//				.authorities("ROLE_USER", "ROLE_ADMIN");
//		// 注册用户myuser，密码为123456，并赋予USER和ADMIN角色权限
//		userConfig.withUser("myuser")
//				.password(passwordEncoder.encode("123456"))
//				.authorities("ROLE_ADMIN");
//
//	}


	/**
	 * 2、从数据库中获取(报错了)
	 */

//	//注入数据源
//	@Autowired
//	private DataSource dataSource = null;
//
//	@Override
//	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		String pwdQuery = "select user_name,password,enabled from user where username = ?";
//		String roleQuery = "select user.user_name,role.role_name" +
//				"from user,role,user_role_c" +
//				"where user.id=user_role_c.user_id" +
//				"and role.id=user_role_c.role_id" +
//				"and user_name = ?";
//		// 密码编辑器
//		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//		auth.jdbcAuthentication()
//				// 密码编辑器
//				.passwordEncoder(passwordEncoder)
//				.dataSource(dataSource)
//				.usersByUsernameQuery(pwdQuery)
//				.authoritiesByUsernameQuery(roleQuery);
//
//	}
	/**
	 * 3、配置自定义的用户服务
	 */
//	MyUserDetailsService myUserDetailService;
//
//	@Override
//	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		auth.userDetailsService(myUserDetailService);
//	}

	// 加密方式
	@Bean
	public static PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//
//		http
////				.formLogin().loginPage("/signIn").loginProcessingUrl("/user/userLogin")
////				.and()
////				.logout().logoutUrl("/logout")
////				.and()
//				.authorizeRequests()
//				.anyRequest().authenticated()
//				.and()
//				.formLogin().and()
//				.httpBasic();
//	}
}
