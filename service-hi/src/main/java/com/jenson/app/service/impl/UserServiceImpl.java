package com.jenson.app.service.impl;

import com.jenson.app.service.UserService;
import com.jenson.domain.entity.User;
import com.jenson.infra.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
	private static final BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();

	@Autowired
	private UserDao userRepository;

	@Override
	public User create(String username, String password) {
		User user=new User();
		user.setUsername(username);
		String hash = encoder.encode(password);
		user.setPassword(hash);
		User u=userRepository.save(user);
		return u;

	}
}
