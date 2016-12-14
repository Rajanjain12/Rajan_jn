package com.rsnt.common.security;

import org.jboss.security.auth.spi.DatabaseServerLoginModule;

import com.rsnt.util.common.CommonUtil;

public class RsntSecurityCheck extends DatabaseServerLoginModule {

	@Override
	protected boolean validatePassword(String inputPassword,
			String expectedPassword) {
		// byte[] defaultBytes = inputPassword.getBytes();
		// System.out.println("defaultBytes ::"+defaultBytes);
		try {
			System.out.println("InputPassword: "+inputPassword+" ::expectedPassword: "+expectedPassword);	
			//String inputEncrypPassword = CommonUtil.encryptPassword(inputPassword);
			return super.validatePassword(inputPassword, expectedPassword);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;

	}
}
