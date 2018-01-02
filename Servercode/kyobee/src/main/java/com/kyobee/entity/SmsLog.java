package com.kyobee.entity;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.joda.time.DateTime;

@Entity
@Table(name="SMSLOG")
public class SmsLog {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="SmsLogID")
	private Long smsLogID;
	
	@Column(name="OrgID")
	private Long orgID;
	
	@Column(name="TemplateID")
	private Long templateID;
	
	@Column(name="TemplateLevel")
	private Integer tempLevel;
	
	@Column(name="GuestID")
	private Long guestID;
	
	@Column(name="PhoneNo")
	private String phoneNo;
	
	@Column(name="MessageText")
	private String msgText;
	
	@Column(name="Process")
	private String process;
	
	@Column(name="CreatedBy")
	private String createdBy;
	
	@Column(name="CreatedAt")
	private String createdAt;
	
	@Column(name="ModifiedAt")
	private String modifiedAt;

	public Long getSmsLogID() {
		return smsLogID;
	}

	public void setSmsLogID(Long smsLogID) {
		this.smsLogID = smsLogID;
	}

	public Long getOrgID() {
		return orgID;
	}

	public void setOrgID(Long orgID) {
		this.orgID = orgID;
	}

	public Long getTemplateID() {
		return templateID;
	}

	public void setTemplateID(Long templateID) {
		this.templateID = templateID;
	}

	public Integer getTempLevel() {
		return tempLevel;
	}

	public void setTempLevel(Integer tempLevel) {
		this.tempLevel = tempLevel;
	}

	public Long getGuestID() {
		return guestID;
	}

	public void setGuestID(Long guestID) {
		this.guestID = guestID;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getMsgText() {
		return msgText;
	}

	public void setMsgText(String msgText) {
		this.msgText = msgText;
	}

	public String getProcess() {
		return process;
	}

	public void setProcess(String process) {
		this.process = process;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getModifiedAt() {
		return modifiedAt;
	}

	public void setModifiedAt(String modifiedAt) {
		this.modifiedAt = modifiedAt;
	} 
}
