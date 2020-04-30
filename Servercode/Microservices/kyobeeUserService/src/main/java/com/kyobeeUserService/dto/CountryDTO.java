package com.kyobeeUserService.dto;

public class CountryDTO {
	private String countryName;
	private String ISOCode;

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getISOCode() {
		return ISOCode;
	}

	public void setISOCode(String iSOCode) {
		ISOCode = iSOCode;
	}

	public CountryDTO(String countryName, String iSOCode) {
		this.countryName = countryName;
		ISOCode = iSOCode;
	}

}
