package com.kyobee.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.kyobee.dto.UserDTO;
import com.kyobee.dto.common.Credential;
import com.kyobee.entity.Organization;
import com.kyobee.entity.User;
import com.kyobee.exception.NoSuchUsernameException;
import com.kyobee.exception.RsntException;

public interface ISecurityService {

/*	public String SEAM_NAME = "securityService";
	public String JNDI_NAME="SecurityServiceImpl";*/

	 public User loginAndFetchUser(final String userLogin, final String password, final String clientBase) throws RsntException;
	 
	 public Long getUserRoleDetail(final String userLogin) throws RsntException;
	 
	 public Organization getUserOrganization(final Long userId) throws RsntException;
	 
	 public User getUserFromEmail(String emailId)throws RsntException;
	 
	 public List<String> getUserPermissions(final Long userLogin);
	 
	 public Long  getRoleProtectedObjectForUser(Long userId, String protectedObjectName) throws RsntException;
	 
	 public List<Object[]> getRoleProtectedObjectForRole(Long roleId) throws RsntException;
	 
	 public List<Object[]> getAllProtectedObject() throws RsntException;
	 
	 public void deleteRoleProtectedObjectMapping(Long roleId)  throws RsntException;
	 
	 public void addRoleProtectedObjMapping(Long roleId, List<String> protectedObjectList, String user)  throws RsntException;

	List<Object[]> loginCredAuth(String userName, String password) throws NoSuchUsernameException;
	
	public Boolean isDuplicateUser(String userName) throws RsntException;

	public User signupUser(Credential credentials) throws RsntException;
	public User signupUserV2(Credential credentials) throws RsntException;

	public Boolean isDuplicateOrganization(String userName) throws RsntException;
	
	//Pampaniya Shweta for forgot password.
	public User forgotPassword(String Email)throws RsntException;
	//pampaniya shweta for check url validation
	public String getAuthCode(long userId) throws RsntException;
	//pampaniya shweta for reset password
	public User resetPassword(long userId,String password) throws RsntException;
    //User Account Activation By Aarshi(11/03/2019)
	public Boolean authVerification(Integer userId,String authCode)throws RsntException;
	//Send authentication mail to user By Aarshi(12/03/2019)
	public void sendActivationMail(Integer userId)throws RsntException;
    //Change password  by Aarshi(13/03/2019)
	public Boolean changePassword(Integer userId,String oldPassword,String newPassowrd) throws RsntException;
	//Check the userName and email id of User  by Aarshi(19/03/2019)
	public String checkIfExistingUser(String userId,String userName,String email)throws RsntException;
	
	public Map<String, String> languageLocalization();
	
}
