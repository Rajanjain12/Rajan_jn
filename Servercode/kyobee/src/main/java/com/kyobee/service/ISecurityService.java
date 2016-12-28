package com.kyobee.service;

import java.util.List;

import com.kyobee.entity.Organization;
import com.kyobee.entity.User;
import com.kyobee.exception.RsntException;

public interface ISecurityService {

/*	public String SEAM_NAME = "securityService";
	public String JNDI_NAME="SecurityServiceImpl";*/

	

	 public User getSecurityUserDetails(final String userLogin) throws RsntException;
	 
	 public Long getUserRoleDetail(final String userLogin) throws RsntException;
	 
	 public Organization getUserOrganization(final Long userId) throws RsntException;
	 
	 public User getUserFromEmail(String emailId)throws RsntException;
	 
	 public List<String> getUserPermissions(final Long userLogin);
	 
	 public Long  getRoleProtectedObjectForUser(Long userId, String protectedObjectName) throws RsntException;
	 
	 public List<Object[]> getRoleProtectedObjectForRole(Long roleId) throws RsntException;
	 
	 public List<Object[]> getAllProtectedObject() throws RsntException;
	 
	 public void deleteRoleProtectedObjectMapping(Long roleId)  throws RsntException;
	 
	 public void addRoleProtectedObjMapping(Long roleId, List<String> protectedObjectList, String user)  throws RsntException;
}