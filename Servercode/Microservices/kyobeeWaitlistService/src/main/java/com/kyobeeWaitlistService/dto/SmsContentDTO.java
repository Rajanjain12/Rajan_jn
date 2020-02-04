package com.kyobeeWaitlistService.dto;

public class SmsContentDTO {
	
	private Integer orgId;
	private Integer guestId;
	private String guestName;
	private Integer langId;
	private Integer guestRank;
	private String clientBase;
	private String guestUuid;
	private Integer tempLevel;


	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}
	public Integer getGuestId() {
		return guestId;
	}

	public void setGuestId(Integer guestId) {
		this.guestId = guestId;
	}

	public String getGuestName() {
		return guestName;
	}

	public void setGuestName(String guestName) {
		this.guestName = guestName;
	}

	public Integer getLangId() {
		return langId;
	}

	public void setLangId(Integer langId) {
		this.langId = langId;
	}
	public Integer getGuestRank() {
		return guestRank;
	}

	public void setGuestRank(Integer guestRank) {
		this.guestRank = guestRank;
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

	public Integer getTempLevel() {
		return tempLevel;
	}

	public void setTempLevel(Integer tempLevel) {
		this.tempLevel = tempLevel;
	}

}
