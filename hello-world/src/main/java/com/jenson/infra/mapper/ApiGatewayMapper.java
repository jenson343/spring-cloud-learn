package com.jenson.infra.mapper;

import com.jenson.api.controller.dto.ApiGatewayDTO;

import java.util.List;

public interface ApiGatewayMapper {
	List<ApiGatewayDTO> queryApiGateway();
}
