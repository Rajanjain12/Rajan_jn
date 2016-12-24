package com.kyobee.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="ADDRESS")
public class Address extends BaseEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="AddressId")
	private Long addressId;
	
	@Column(name="AddressLineOne")
	private String addressLineOne;
	
	@Column(name="AddressLineTwo")
	private String addressLineTwo;
	
	@Column(name="City")
	private String city;
	
	@Column(name="State")
	private String state;
	
	@Column(name="Country")
	private String country;
	
	@Column(name="ZipCode")
	private Long zipCode;
	
	@Column(name="Latitude")
	private BigDecimal addressLat;
	
	@Column(name="Longitude")
	private BigDecimal addressLong;
	
	/*@OneToOne(mappedBy="Address", fetch= FetchType.LAZY, cascade={CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE})
	private Organization organization;
*/
	public Long getAddressId() {
		return addressId;
	}

	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}

	public String getAddressLineOne() {
		return addressLineOne;
	}

	public void setAddressLineOne(String addressLineOne) {
		this.addressLineOne = addressLineOne;
	}

	public String getAddressLineTwo() {
		return addressLineTwo;
	}

	public void setAddressLineTwo(String addressLineTwo) {
		this.addressLineTwo = addressLineTwo;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Long getZipCode() {
		return zipCode;
	}

	public void setZipCode(Long zipCode) {
		this.zipCode = zipCode;
	}

	public BigDecimal getAddressLat() {
		return addressLat;
	}

	public void setAddressLat(BigDecimal addressLat) {
		this.addressLat = addressLat;
	}

	public BigDecimal getAddressLong() {
		return addressLong;
	}

	public void setAddressLong(BigDecimal addressLong) {
		this.addressLong = addressLong;
	}

	

	
}
