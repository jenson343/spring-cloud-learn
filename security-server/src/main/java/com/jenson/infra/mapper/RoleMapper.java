package com.jenson.infra.mapper;

import com.jenson.domain.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RoleMapper {
	List<Role> findByUserName(@Param("userName") String userName);
}
