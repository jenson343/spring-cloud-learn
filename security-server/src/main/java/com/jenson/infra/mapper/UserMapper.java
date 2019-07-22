package com.jenson.infra.mapper;

import com.jenson.domain.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
	User findByUserName(@Param("userName") String userName);
}
