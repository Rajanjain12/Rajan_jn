package com.kyobeeWaitlistService.dto;

public class PusherDTO {
	
	private String op;
	private WaitlistMetrics waitlistMetrics;
    private Integer orgId;
    private String from;
	
	public String getOp() {
		return op;
	}
	public void setOp(String op) {
		this.op = op;
	}
	public WaitlistMetrics getWaitlistMetrics() {
		return waitlistMetrics;
	}
	public void setWaitlistMetrics(WaitlistMetrics waitlistMetrics) {
		this.waitlistMetrics = waitlistMetrics;
	}
	public Integer getOrgId() {
		return orgId;
	}
	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	
	

}
