package com.kyobeeWaitlistService.dto;

import java.util.List;

public class OrgSettingDTO {
	
	private Integer orgId;
	private List<SeatingMarketingPrefDTO> seatingPreference;
	private List<SeatingMarketingPrefDTO> marketingPreference;
	private Integer notifyFirst;
	private List<SmsTemplateDTO> smsTemplateDTO;
	private List<LanguageKeyMappingDTO> languageList;
	private Integer defaultLanguage;	
	
	public Integer getOrgId() {
		return orgId;
	}
	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}
	public List<SeatingMarketingPrefDTO> getSeatingPreference() {
		return seatingPreference;
	}
	public void setSeatingPreference(List<SeatingMarketingPrefDTO> seatingPreference) {
		this.seatingPreference = seatingPreference;
	}
	public List<SeatingMarketingPrefDTO> getMarketingPreference() {
		return marketingPreference;
	}
	public void setMarketingPreference(List<SeatingMarketingPrefDTO> marketingPreference) {
		this.marketingPreference = marketingPreference;
	}
	public Integer getNotifyFirst() {
		return notifyFirst;
	}
	public void setNotifyFirst(Integer notifyFirst) {
		this.notifyFirst = notifyFirst;
	}
	public List<SmsTemplateDTO> getSmsTemplateDTO() {
		return smsTemplateDTO;
	}
	public void setSmsTemplateDTO(List<SmsTemplateDTO> smsTemplateDTO) {
		this.smsTemplateDTO = smsTemplateDTO;
	}
	public List<LanguageKeyMappingDTO> getLanguageList() {
		return languageList;
	}
	public void setLanguageList(List<LanguageKeyMappingDTO> languageList) {
		this.languageList = languageList;
	}
	public Integer getDefaultLanguage() {
		return defaultLanguage;
	}
	public void setDefaultLanguage(Integer defaultLanguage) {
		this.defaultLanguage = defaultLanguage;
	}
	
}
