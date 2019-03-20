package com.kyobee.dto;

import java.math.BigDecimal;

import javax.persistence.Column;

public class AddressDTO {

	private String addressId;
	

	private String addressLine1;
	
	private String addressLine2;
	
	private String city;
	
	private String state;
	
	private String country;
	
	private String zipCode;

	private String addressLat;
	
	@Override
	public String toString() {
		return "AddressDTO [addressId=" + addressId + ", addressLine1=" + addressLine1 + ", addressLine2="
				+ addressLine2 + ", city=" + city + ", state=" + state + ", country=" + country + ", zipCode=" + zipCode
				+ ", addressLat=" + addressLat + ", addressLong=" + addressLong + "]";
	}

	private String addressLong;

	public String getAddressId() {
		return addressId;
	}

	public String getAddressLat() {
		return addressLat;
	}

	public void setAddressLat(String addressLat) {
		this.addressLat = addressLat;
	}

	public String getAddressLong() {
		return addressLong;
	}

	public void setAddressLong(String addressLong) {
		this.addressLong = addressLong;
	}

	public void setAddressId(String addressId) {
		this.addressId = addressId;
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
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

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	
}
