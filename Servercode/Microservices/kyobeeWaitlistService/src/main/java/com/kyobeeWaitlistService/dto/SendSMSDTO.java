package com.kyobeeWaitlistService.dto;

public class SendSMSDTO {
	private Integer guestId;
	private Integer orgId;
	private Integer templateId;
	private String smsContent;
	private Integer templateLevel;
	public Integer getGuestId() {
		return guestId;
	}
	public void setGuestId(Integer guestId) {
		this.guestId = guestId;
	}
	public Integer getOrgId() {
		return orgId;
	}
	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}
	public Integer getTemplateId() {
		return templateId;
	}
	public void setTemplateId(Integer templateId) {
		this.templateId = templateId;
	}
	public String getSmsContent() {
		return smsContent;
	}
	public void setSmsContent(String smsContent) {
		this.smsContent = smsContent;
	}
	public Integer getTemplateLevel() {
		return templateLevel;
	}
	public void setTemplateLevel(Integer templateLevel) {
		this.templateLevel = templateLevel;
	}

}
