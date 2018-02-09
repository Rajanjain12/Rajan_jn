package com.kyobee.dto;

public class SmsContentParamDTO {
	private Long orgId;
	private Long guestId;
	private Long langId;
	private Integer tempLevel;
	private Integer gusetRank;
	private String clientBase;
	private String guestUuid;
	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	public Long getGuestId() {
		return guestId;
	}
	public void setGuestId(Long guestId) {
		this.guestId = guestId;
	}
	public Long getLangId() {
		return langId;
	}
	public void setLangId(Long langId) {
		this.langId = langId;
	}
	public Integer getTempLevel() {
		return tempLevel;
	}
	public void setTempLevel(Integer tempLevel) {
		this.tempLevel = tempLevel;
	}
	public Integer getGusetRank() {
		return gusetRank;
	}
	public void setGusetRank(Integer gusetRank) {
		this.gusetRank = gusetRank;
	}
	public String getClientBase() {
		return clientBase;
	}
	public void setClientBase(String clientBase) {
		this.clientBase = clientBase;
	}
	public String getGuestUuid() {
		return guestUuid;
	}
	public void setGuestUuid(String guestUuid) {
		this.guestUuid = guestUuid;
	}
}
