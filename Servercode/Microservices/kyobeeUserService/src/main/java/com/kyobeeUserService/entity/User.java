package com.kyobeeUserService.entity;

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
@NamedQuery(name="User.findAll", query="SELECT u FROM User u")
public class User {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="UserID")
	private int userID;

	@Column(name="ActivationID")
	private String activationID;

	@Column(name="Active")
	private byte active;

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
	@OneToMany(mappedBy="user")
	private List<Organizationlayoutdashboard> organizationlayoutdashboards;

	//bi-directional many-to-one association to Organizationuser
	@OneToMany(mappedBy="user1")
	private List<Organizationuser> organizationusers1;

	//bi-directional many-to-one association to Organizationuser
	@OneToMany(mappedBy="user2")
	private List<Organizationuser> organizationusers2;

	//bi-directional many-to-one association to Address
	@ManyToOne
	@JoinColumn(name="AddressId")
	private Address addressBean;

	//bi-directional many-to-one association to Userrole
	@OneToMany(mappedBy="user1")
	private List<Userrole> userroles1;

	//bi-directional many-to-one association to Userrole
	@OneToMany(mappedBy="user2")
	private List<Userrole> userroles2;

	public User() {
	}

	public int getUserID() {
		return this.userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public String getActivationID() {
		return this.activationID;
	}

	public void setActivationID(String activationID) {
		this.activationID = activationID;
	}

	public byte getActive() {
		return this.active;
	}

	public void setActive(byte active) {
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

	public List<Organizationlayoutdashboard> getOrganizationlayoutdashboards() {
		return this.organizationlayoutdashboards;
	}

	public void setOrganizationlayoutdashboards(List<Organizationlayoutdashboard> organizationlayoutdashboards) {
		this.organizationlayoutdashboards = organizationlayoutdashboards;
	}

	public Organizationlayoutdashboard addOrganizationlayoutdashboard(Organizationlayoutdashboard organizationlayoutdashboard) {
		getOrganizationlayoutdashboards().add(organizationlayoutdashboard);
		organizationlayoutdashboard.setUser(this);

		return organizationlayoutdashboard;
	}

	public Organizationlayoutdashboard removeOrganizationlayoutdashboard(Organizationlayoutdashboard organizationlayoutdashboard) {
		getOrganizationlayoutdashboards().remove(organizationlayoutdashboard);
		organizationlayoutdashboard.setUser(null);

		return organizationlayoutdashboard;
	}

	public List<Organizationuser> getOrganizationusers1() {
		return this.organizationusers1;
	}

	public void setOrganizationusers1(List<Organizationuser> organizationusers1) {
		this.organizationusers1 = organizationusers1;
	}

	public Organizationuser addOrganizationusers1(Organizationuser organizationusers1) {
		getOrganizationusers1().add(organizationusers1);
		organizationusers1.setUser1(this);

		return organizationusers1;
	}

	public Organizationuser removeOrganizationusers1(Organizationuser organizationusers1) {
		getOrganizationusers1().remove(organizationusers1);
		organizationusers1.setUser1(null);

		return organizationusers1;
	}

	public List<Organizationuser> getOrganizationusers2() {
		return this.organizationusers2;
	}

	public void setOrganizationusers2(List<Organizationuser> organizationusers2) {
		this.organizationusers2 = organizationusers2;
	}

	public Organizationuser addOrganizationusers2(Organizationuser organizationusers2) {
		getOrganizationusers2().add(organizationusers2);
		organizationusers2.setUser2(this);

		return organizationusers2;
	}

	public Organizationuser removeOrganizationusers2(Organizationuser organizationusers2) {
		getOrganizationusers2().remove(organizationusers2);
		organizationusers2.setUser2(null);

		return organizationusers2;
	}

	public Address getAddressBean() {
		return this.addressBean;
	}

	public void setAddressBean(Address addressBean) {
		this.addressBean = addressBean;
	}

	public List<Userrole> getUserroles1() {
		return this.userroles1;
	}

	public void setUserroles1(List<Userrole> userroles1) {
		this.userroles1 = userroles1;
	}

	public Userrole addUserroles1(Userrole userroles1) {
		getUserroles1().add(userroles1);
		userroles1.setUser1(this);

		return userroles1;
	}

	public Userrole removeUserroles1(Userrole userroles1) {
		getUserroles1().remove(userroles1);
		userroles1.setUser1(null);

		return userroles1;
	}

	public List<Userrole> getUserroles2() {
		return this.userroles2;
	}

	public void setUserroles2(List<Userrole> userroles2) {
		this.userroles2 = userroles2;
	}

	public Userrole addUserroles2(Userrole userroles2) {
		getUserroles2().add(userroles2);
		userroles2.setUser2(this);

		return userroles2;
	}

	public Userrole removeUserroles2(Userrole userroles2) {
		getUserroles2().remove(userroles2);
		userroles2.setUser2(null);

		return userroles2;
	}

	
}
