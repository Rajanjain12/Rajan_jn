package com.rsnt.service;

import java.util.List;

import javax.ejb.Local;

import com.rsnt.common.exception.RsntException;
import com.rsnt.entity.Organization;
import com.rsnt.entity.ProtectedObject;
import com.rsnt.entity.User;

@Local
public interface ISecurityService {
	
	public String SEAM_NAME = "securityService";
	public String JNDI_NAME="SecurityServiceImpl";
	
	

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
