package com.rsnt.util.transferobject;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;
import org.jboss.seam.annotations.Name;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Name("staffDataTO")
public class StaffDataTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1775560367300895158L;
	
	@JsonProperty
	private Long userId;
	
	@JsonProperty
	private String userName;
	
	@JsonProperty
	private String role;
	
	
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@JsonProperty
	private String firstName;
	
	@JsonProperty
	private String lastName;
	
	@JsonProperty
	private String address;
	
	@JsonProperty
	private String primaryContactNo;
	
	@JsonProperty
	private String alternateContactNo;
	
	@JsonProperty
	private String email;
	
	@JsonProperty
	private String activeStatus;
	
	@JsonProperty
	private Long access;
	

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPrimaryContactNo() {
		return primaryContactNo;
	}

	public void setPrimaryContactNo(String primaryContactNo) {
		this.primaryContactNo = primaryContactNo;
	}

	public String getAlternateContactNo() {
		return alternateContactNo;
	}

	public void setAlternateContactNo(String alternateContactNo) {
		this.alternateContactNo = alternateContactNo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(String activeStatus) {
		this.activeStatus = activeStatus;
	}

	public Long getAccess() {
		return access;
	}

	public void setAccess(Long access) {
		this.access = access;
	}

	
}
