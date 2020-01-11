package com.kyobeeUserService.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="USER")
public class User implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="UserID")
	private Integer userID;

	@Column(name="ActivationID")
	private String activationID;

	@Column(name="Active")
	private Byte active;

	@Column(name="Address")
	private String address;

	@Column(name="AuthCode")
	private String authCode;

	@Column(name="ContactNoOne")
	private String contactNoOne;

	@Column(name="ContactNoTwo")
	private String contactNoTwo;

	@Temporal(TemporalType.DATE)
	@Column(name="CreatedAt")
	private Date createdAt;

	@Column(name="CreatedBy")
	private String createdBy;

	@Column(name="Email")
	private String email;

	@Column(name="FirstName")
	private String firstName;

	@Column(name="LastName")
	private String lastName;

	@Temporal(TemporalType.DATE)
	@Column(name="ModifiedAt")
	private Date modifiedAt;

	@Column(name="ModifiedBy")
	private String modifiedBy;

	@Column(name="Password")
	private String password;

	@Column(name="UserName")
	private String userName;

	//bi-directional many-to-one association to Organizationlayoutdashboard
	/*
	 * @OneToMany(mappedBy="user") private List<Organizationlayoutdashboard>
	 * organizationlayoutdashboards;
	 */

	//bi-directional many-to-one association to Organizationuser
	@OneToMany
	private List<OrganizationUser> organizationusers;

	//bi-directional many-to-one association to Organizationuser
	/*
	 * @OneToMany(mappedBy="user2") private List<OrganizationUser>
	 * organizationusers2;
	 */

	//bi-directional many-to-one association to Address
	@ManyToOne
	@JoinColumn(name="\"AddressId\"")
	private Address addressBean;

	//bi-directional many-to-one association to Userrole
	@OneToMany
	private List<Userrole> userroles;

	//bi-directional many-to-one association to Userrole
	/*
	 * @OneToMany(mappedBy="user2") private List<Userrole> userroles2;
	 */

	public User() {
	}

	public Integer getUserID() {
		return this.userID;
	}

	public void setUserID(Integer userID) {
		this.userID = userID;
	}

	public String getActivationID() {
		return this.activationID;
	}

	public void setActivationID(String activationID) {
		this.activationID = activationID;
	}

	public Byte getActive() {
		return this.active;
	}

	public void setActive(Byte active) {
		this.active = active;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAuthCode() {
		return this.authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	public String getContactNoOne() {
		return this.contactNoOne;
	}

	public void setContactNoOne(String contactNoOne) {
		this.contactNoOne = contactNoOne;
	}

	public String getContactNoTwo() {
		return this.contactNoTwo;
	}

	public void setContactNoTwo(String contactNoTwo) {
		this.contactNoTwo = contactNoTwo;
	}

	public Date getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Date getModifiedAt() {
		return this.modifiedAt;
	}

	public void setModifiedAt(Date modifiedAt) {
		this.modifiedAt = modifiedAt;
	}

	public String getModifiedBy() {
		return this.modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	/*
	 * public List<Organizationlayoutdashboard> getOrganizationlayoutdashboards() {
	 * return this.organizationlayoutdashboards; }
	 * 
	 * public void setOrganizationlayoutdashboards(List<Organizationlayoutdashboard>
	 * organizationlayoutdashboards) { this.organizationlayoutdashboards =
	 * organizationlayoutdashboards; }
	 * 
	 * public Organizationlayoutdashboard
	 * addOrganizationlayoutdashboard(Organizationlayoutdashboard
	 * organizationlayoutdashboard) {
	 * getOrganizationlayoutdashboards().add(organizationlayoutdashboard);
	 * organizationlayoutdashboard.setUser(this);
	 * 
	 * return organizationlayoutdashboard; }
	 * 
	 * public Organizationlayoutdashboard
	 * removeOrganizationlayoutdashboard(Organizationlayoutdashboard
	 * organizationlayoutdashboard) {
	 * getOrganizationlayoutdashboards().remove(organizationlayoutdashboard);
	 * organizationlayoutdashboard.setUser(null);
	 * 
	 * return organizationlayoutdashboard; }
	 */

	public List<OrganizationUser> getOrganizationusers() {
		return this.organizationusers;
	}

	public void setOrganizationusers(List<OrganizationUser> organizationusers) {
		this.organizationusers = organizationusers;
	}

	public OrganizationUser addOrganizationusers(OrganizationUser organizationusers) {
		getOrganizationusers().add(organizationusers);
		organizationusers.setUser(this);

		return organizationusers;
	}

	public OrganizationUser removeOrganizationusers(OrganizationUser organizationusers) {
		getOrganizationusers().remove(organizationusers);
		organizationusers.setUser(null);

		return organizationusers;
	}

	/*
	 * public List<OrganizationUser> getOrganizationusers() { return
	 * this.organizationusers; }
	 * 
	 * public void setOrganizationusers(List<OrganizationUser> organizationusers) {
	 * this.organizationusers = organizationusers; }
	 * 
	 * public OrganizationUser addOrganizationusers(OrganizationUser
	 * organizationusers) { getOrganizationusers().add(organizationusers);
	 * organizationusers.setUser(this);
	 * 
	 * return organizationusers; }
	 * 
	 * public OrganizationUser removeOrganizationusers(OrganizationUser
	 * organizationusers) { getOrganizationusers().remove(organizationusers);
	 * organizationusers.setUser(null);
	 * 
	 * return organizationusers; }
	 */

	public Address getAddressBean() {
		return this.addressBean;
	}

	public void setAddressBean(Address addressBean) {
		this.addressBean = addressBean;
	}

	public List<Userrole> getUserroles() {
		return this.userroles;
	}

	public void setUserroles(List<Userrole> userroles) {
		this.userroles = userroles;
	}

	public Userrole addUserroles(Userrole userroles) {
		getUserroles().add(userroles);
		userroles.setUser(this);

		return userroles;
	}

	public Userrole removeUserroles(Userrole userroles) {
		getUserroles().remove(userroles);
		userroles.setUser(null);

		return userroles;
	}

	/*
	 * public List<Userrole> getUserroles2() { return this.userroles2; }
	 * 
	 * public void setUserroles2(List<Userrole> userroles2) { this.userroles2 =
	 * userroles2; }
	 */

	/*
	 * public Userrole addUserroles2(Userrole userroles2) {
	 * getUserroles2().add(userroles2); userroles2.setUser2(this);
	 * 
	 * return userroles2; }
	 * 
	 * public Userrole removeUserroles2(Userrole userroles2) {
	 * getUserroles2().remove(userroles2); userroles2.setUser2(null);
	 * 
	 * return userroles2; }
	 */

	
}
