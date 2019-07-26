package com.jenson.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

//	@Autowired
//	UserDetailsService userDetailsService;
//
//	@Autowired
//	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
////		auth.inMemoryAuthentication()
////				.withUser("jenson")
////				.password("123").authorities("ROLE_USER");
////		auth.inMemoryAuthentication()
////				.withUser("admin")
////				.password("admin").authorities("ROLE_ADMIN");
//
////		auth.userDetailsService(userDetailsService());
//		auth.userDetailsService(userDetailsService);
//	}

	//	@Bean
//	@Override
//	public UserDetailsService userDetailsService(){
//		InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
//		manager.createUser(User.withUsername("jenson").password("1234").roles("USER").build());
// 		manager.createUser(User.withUsername("admin").password("admin").roles("USER","ADMIN").build());
//		return manager;
//	}
// 加密方式
	@Bean
	public static PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.authorizeRequests()
				// 不需要验证可直接访问的资源
				.antMatchers("/css/**", "/index").permitAll()
				// 以下资源需要验证，仅能用USER角色来验证
				.antMatchers("/user/**").hasRole("USER")
				.antMatchers("/blogs/**").hasRole("USER")
//				.antMatchers("/*").hasAnyRole()
				.and()
				// 表单登陆地址及登陆失败地址
				.formLogin().loginPage("/login").failureUrl("/login-error")
				.and()
				// 异常处理定位界面
				.exceptionHandling().accessDeniedPage("/401");

		// 注销成功后重定向到首页
		http.logout().logoutSuccessUrl("/");
	}
}
