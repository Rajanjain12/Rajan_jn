package com.rsnt.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.jboss.seam.annotations.Name;

import com.rsnt.common.entity.BaseEntity;

@Entity
@Name("OrganizationUser")
@Table(name="ORGANIZATIONUSER")

public class OrganizationUser extends BaseEntity implements Serializable{

	private static final long serialVersionUID = -5466178340214031541L;
	

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="OrganizationUserId")
	private Long organizationUserId;
	
	@JoinColumn(name="OrganizationId")
	@ManyToOne(fetch = FetchType.LAZY,cascade={CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE})
	private Organization organization;
	
	@OneToOne(fetch = FetchType.LAZY, 
			 cascade={CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE})
	@JoinColumn(name = "UserID")
	private User user;

	public Long getOrganizationUserId() {
		return organizationUserId;
	}

	public void setOrganizationUserId(Long organizationUserId) {
		this.organizationUserId = organizationUserId;
	}

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	

}
