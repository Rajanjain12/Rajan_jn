package com.kyobeeAccountService.service;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import com.kyobeeAccountService.dto.OrganizationDTO;
import com.kyobeeAccountService.util.Exception.PasswordNotMatchException;

public interface AccountService {
	public OrganizationDTO fetchAccountDetails(Integer orgId);
	public void updateAccountDetails(HttpServletRequest request) throws IOException;
	public void updatePassword(String oldPassword,String newPassword,Integer userId) throws PasswordNotMatchException;
	public void deleteAccount(Integer orgId,Integer userId);

}
