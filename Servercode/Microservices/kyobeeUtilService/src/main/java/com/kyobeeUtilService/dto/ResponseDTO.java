package com.kyobeeUtilService.dto;

public class ResponseDTO {


	private Integer success;
    private String message;
    Object serviceResult;
    
	public Integer getSuccess() {
		return success;
	}
	public void setSuccess(Integer success) {
		this.success = success;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Object getServiceResult() {
		return serviceResult;
	}
	public void setServiceResult(Object serviceResult) {
		this.serviceResult = serviceResult;
	}
    
    
}
