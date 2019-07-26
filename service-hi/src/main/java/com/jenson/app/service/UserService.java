package com.jenson.app.service;

import com.jenson.domain.entity.User;

public interface UserService {
	User create(String username, String password);
}
