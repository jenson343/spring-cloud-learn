package com.jenson.app.service.impl;

import com.jenson.domain.entity.Role;
import com.jenson.domain.entity.User;
import com.jenson.infra.mapper.RoleMapper;
import com.jenson.infra.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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
