package com.jenson.api.controller.dto;

public class ApiGatewayDTO {
	private String id;
	private String path;
	private String serviceId;
	private String url;
	private int retryable;
	private int enabled;
	private int strip_prefix;
	private String api_name;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getRetryable() {
		return retryable;
	}

	public void setRetryable(int retryable) {
		this.retryable = retryable;
	}

	public int getEnabled() {
		return enabled;
	}

	public void setEnabled(int enabled) {
		this.enabled = enabled;
	}

	public int getStrip_prefix() {
		return strip_prefix;
	}

	public void setStrip_prefix(int strip_prefix) {
		this.strip_prefix = strip_prefix;
	}

	public String getApi_name() {
		return api_name;
	}

	public void setApi_name(String api_name) {
		this.api_name = api_name;
	}


}
