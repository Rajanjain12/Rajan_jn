package com.kyobee.dto.common;

import java.io.Serializable;

public class Credential implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3073325363363636663L;

	private String username;
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
