package com.kyobee.dto;

public class SendSMSWrapper {
	private Long guestId;
	private Long orgId;
	private Long templateId;
	private String smsContent;
	private WaitlistMetrics metrics;
	private Integer templateLevel;
	
	public Long getGuestId() {
		return guestId;
	}
	public void setGuestId(Long guestId) {
		this.guestId = guestId;
	}
	
	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	
	public Long getTemplateId() {
		return templateId;
	}
	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}
	
	public String getSmsContent() {
		return smsContent;
	}
	public void setSmsContent(String smsContent) {
		this.smsContent = smsContent;
	}
	
	public WaitlistMetrics getMetrics() {
		return metrics;
	}
	public void setMetrics(WaitlistMetrics metrics) {
		this.metrics = metrics;
	}
	public Integer getTemplateLevel() {
		return templateLevel;
	}
	public void setTemplateLevel(Integer templateLevel) {
		this.templateLevel = templateLevel;
	}
}
