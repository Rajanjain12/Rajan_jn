package com.rsnt.service;

import java.util.List;

import javax.ejb.Local;

@Local
public interface ILoginAuthService {
	public String SEAM_NAME = "LoginAuthService";
	public String JNDI_NAME="LoginAuthServiceImpl";

	public List<Object[]>  loginCredAuth(String userName, String password) ;

	
}
