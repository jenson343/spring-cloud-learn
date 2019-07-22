package com.jenson.infra.mapper;

import com.jenson.domain.entity.Account;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AccoutMapper {

	Account selectAccount(@Param("id") Long id);

	Account selectAccountByUserName(@Param("userName") String userName);
}
