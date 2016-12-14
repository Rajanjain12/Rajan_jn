//This table is Added By Ronak 
package com.rsnt.entity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.jboss.seam.annotations.Name;

import com.rsnt.common.entity.BaseEntity;

@Entity
@Table(name = "OrgWatilistConfig")
@Name("OrgWaitlistConfig")

public class OrganizationWaitlistConfiguration {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "OrganizationId")
	private Long organizationId;


	@OneToOne(mappedBy = "orgWaitlistConfig")
	private Organization organization;

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	@Column(name = "NotifyUserCount")
	private Long notifyUserCount;

	@Column(name = "WaitTime")
	private Long waitTime;
	
	@Column(name = "SmsGateWayTypeId")
	private Long smsGateWayTypeId;
	@Column(name = "SmsGateWayValueId")
	private Long  smsGateWayValueId;

	

	@Column(name = "GatewayIdentifier")
	private String  gatewayIdentifier;

	@Column(name = "TotalWaitTime")
	private Long totalWaitTime;

	@Column(columnDefinition = "TINYINT" , name="LevelOne" ,nullable=false)
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private boolean levelOne;
	
	
	public boolean getLevelOne() {
		return levelOne;
	}

	public void setLevelOne(boolean levelOne) {
		this.levelOne = levelOne;
	}

	@Column(columnDefinition = "TINYINT" , name="LevelTwo" ,nullable=false)
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private boolean levelTwo;
	
	

	@Column(columnDefinition = "TINYINT" , name="LevelThree" ,nullable=false)
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private boolean levelThree;
	

	@Column(name = "SentBy",length=8,nullable=false)
	private String sentBy;
	
	public String getSentBy() {
		return sentBy;
	}

	public void setSentBy(String sentBy) {
		this.sentBy = sentBy;
	}

	public Long getNotifyUserCount() {
		return notifyUserCount;
	}

	public void setNotifyUserCount(Long notifyUserCount) {
		this.notifyUserCount = notifyUserCount;
	}

	public Long getWaitTime() {
		return waitTime;
	}

	public void setWaitTime(Long waitTime) {
		this.waitTime = waitTime;
	}

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}
	public Long getTotalWaitTime() {
		return totalWaitTime;
	}

	public void setTotalWaitTime(Long totalWaitTime) {
		this.totalWaitTime = totalWaitTime;
	}
	
	public boolean getLevelTwo() {
		return levelTwo;
	}

	public void setLevelTwo(boolean levelTwo) {
		this.levelTwo = levelTwo;
	}

	public boolean getLevelThree() {
		return levelThree;
	}

	public void setLevelThree(boolean levelThree) {
		this.levelThree = levelThree;
	}

	public Long getSmsGateWayTypeId() {
		return smsGateWayTypeId;
	}

	public void setSmsGateWayTypeId(Long smsGateWayTypeId) {
		this.smsGateWayTypeId = smsGateWayTypeId;
	}

	public Long getSmsGateWayValueId() {
		return smsGateWayValueId;
	}

	public void setSmsGateWayValueId(Long smsGateWayValueId) {
		this.smsGateWayValueId = smsGateWayValueId;
	}
	public String getGatewayIdentifier() {
		return gatewayIdentifier;
	}

	public void setGatewayIdentifier(String gatewayIdentifier) {
		this.gatewayIdentifier = gatewayIdentifier;
	}
}
