package com.jenson.domain.entity;


import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name="user")
public class User {
	/**
	 * 主键
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	/**
	 * 用户名
	 */
	@Column(nullable = false,unique = true)
	private String userName;
	/**
	 * 密码
	 */
	@Column(nullable = false)
	private String password;
	/**
	 * 启用
	 */
	@Column
	private Long enabled;
}
