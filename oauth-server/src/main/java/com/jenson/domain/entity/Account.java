package com.jenson.domain.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * 账户实体类
 */
@Data
@Entity
@Table(name="account")
public class Account {
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
	private String passWord;
	/**
	 * 角色
	 */
	@Column
	private String roles;

}