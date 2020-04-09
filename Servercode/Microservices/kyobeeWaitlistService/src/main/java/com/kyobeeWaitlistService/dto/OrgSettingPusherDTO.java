package com.kyobeeWaitlistService.dto;

import java.util.List;

public class OrgSettingPusherDTO {
	
	private String  op;
	private OrgSettingDTO orgSettingDTO;
	private LanguageKeyMappingDTO langKeyMap;
	List<SmsTemplateDTO> smsTemplateDTO;
	private Integer langID;
	
	public String getOp() {
		return op;
	}
	public void setOp(String op) {
		this.op = op;
	}
	public OrgSettingDTO getOrgSettingDTO() {
		return orgSettingDTO;
	}
	public void setOrgSettingDTO(OrgSettingDTO orgSettingDTO) {
		this.orgSettingDTO = orgSettingDTO;
	}
	public LanguageKeyMappingDTO getLangKeyMap() {
		return langKeyMap;
	}
	public void setLangKeyMap(LanguageKeyMappingDTO langKeyMap) {
		this.langKeyMap = langKeyMap;
	}
	public List<SmsTemplateDTO> getSmsTemplateDTO() {
		return smsTemplateDTO;
	}
	public void setSmsTemplateDTO(List<SmsTemplateDTO> smsTemplateDTO) {
		this.smsTemplateDTO = smsTemplateDTO;
	}
	public Integer getLangID() {
		return langID;
	}
	public void setLangID(Integer langID) {
		this.langID = langID;
	}
	
}
