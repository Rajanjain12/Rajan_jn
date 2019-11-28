package com.kyobee.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.kyobee.dao.impl.AddressDAO;
import com.kyobee.dao.impl.OrganizationDAO;
import com.kyobee.dao.impl.UserDAO;
import com.kyobee.dto.common.Credential;
import com.kyobee.entity.LanguageKeyMapping;
import com.kyobee.entity.Organization;
import com.kyobee.entity.OrganizationCategory;
import com.kyobee.entity.OrganizationPlanSubscription;
import com.kyobee.entity.OrganizationUser;
import com.kyobee.entity.Plan;
import com.kyobee.entity.RoleProtectedObject;
import com.kyobee.entity.User;
import com.kyobee.entity.UserRole;
import com.kyobee.exception.NoSuchUsernameException;
import com.kyobee.exception.RsntException;
import com.kyobee.service.ISecurityService;
import com.kyobee.util.AppTransactional;
import com.kyobee.util.EmailUtil;
import com.kyobee.util.PropertyUtility;
import com.kyobee.util.SessionContextUtil;
import com.kyobee.util.common.CommonUtil;
import com.kyobee.util.common.Constants;
import com.kyobee.util.common.LoggerUtil;
import com.kyobee.util.common.NativeQueryConstants;

@AppTransactional
@Repository
public class SecurityServiceImpl implements ISecurityService {
	
	@Autowired
	UserDAO userDao;
	
	@Autowired
	OrganizationDAO organizationDao;
	
	@Autowired
	AddressDAO addressDao;
	
	@Autowired
    private SessionFactory sessionFactory;
	
	@Autowired
	private SessionContextUtil sessionContextUtil;
	
	@Autowired
	EmailUtil emailUtil;
	
	@Value("${smsRouteId}")
	private String smsRoute;
	/*@Logger
	private Log log;*/
	private Logger log = Logger.getLogger(SecurityServiceImpl.class);
	
	
	
    /**
     * @param userLogin
     * @return SecurityUserLoad associated with userLogin
     */
    public User loginAndFetchUser(final String userLogin, final String password, final String clientBase) throws RsntException {
    	try{
    		/*
    		 * ClientBase api validation was added later but not used as of now 2-Mar-2017
    		 */
    		if(!clientBase.equals("api")) {
    		
			return (User) sessionFactory.getCurrentSession().getNamedQuery(User.GET_PROFILE_BY_USERLOGIN)
					.setParameter("username", userLogin.toLowerCase())
					.setParameter("password", CommonUtil.encryptPassword(password))
					.setParameter("clientBase", clientBase).uniqueResult();
    		}
    		else {
    			return (User) sessionFactory.getCurrentSession().getNamedQuery(User.GET_PROFILE_BY_USERLOGINAPI)
    					.setParameter("username", userLogin.toLowerCase())
    					.setParameter("password", CommonUtil.encryptPassword(password))
    					.uniqueResult();
    		}

    	}
    	catch(Exception e){
    		log.error("SecurityServiceImpl.getSecurityUserDetails()", e);
			throw new RsntException("SecurityServiceImpl.getSecurityUserDetails()", e);
    	}
     }
    //pampaniya shweta for forgot password
	public User forgotPassword(String email) throws RsntException 
	{	
		try
		{
			User user = (User) sessionFactory.getCurrentSession().getNamedQuery(User.GET_USER_BY_EMAIL).setParameter("email", email.toLowerCase()).setParameter("active", true).setParameter("oactive", true)
	            .uniqueResult();
			String clientbase = user.getOrganizationUser().getOrganization().getClientBase();
			if(user.getAuthcode()==null){
			String authcode = CommonUtil.generateRandomToken().toString();
			LoggerUtil.logInfo("generated Authcode is for user "+user.getUserName()+" is: "+authcode );
			user.setAuthcode(authcode);
			}
			emailUtil.sendForgotPasswardEmail(user.getEmail(),user.getFirstName(),user.getLastName(),clientbase,user.getAuthcode(),user.getUserId());
			sessionFactory.getCurrentSession().saveOrUpdate(user);
			return user;
		}catch (Exception e){
			log.error("SecurityServiceImpl.forgotPassword()",e);
			throw new RsntException(e);
		}
	}
	//Pampaniya Shweta for check url is right or not
	@Override
	public String getAuthCode(long userId) throws RsntException
	{
		try
		{
			String authCode = userDao.getUserAuthCode(userId);
			return authCode;
		}catch (Exception e)
		{
			throw new RsntException(e);
		}
	}
   	//Pampaniya Shweta for reset password
	@Override
	public User resetPassword(long userId,String password) throws RsntException 
	{
		try
		{
			User user = userDao.resetPassword(userId, password);
			return user;
			
		}catch (Exception e) 
		{
			throw new RsntException(e);
		}
	}
	
    @SuppressWarnings("unchecked")
    public List<String> getUserPermissions(final Long userId) {
        List<String> permissions = null;
        try {
            permissions = (List<String>) sessionFactory.getCurrentSession()
                    .getNamedQuery(RoleProtectedObject.FETCH_ROLE_PERMISSIONS_BY_USER_ID)
                    .setParameter("userId", userId).list();

        } catch (Exception e) {
            permissions = null;
            log.error("SecurityServiceImpl.getUserPermissions()",e);
        } finally {
        }
        return permissions;
    }
    
    public User getUserFromEmail(String emailId) throws RsntException{
    	try{
    		return (User) sessionFactory.getCurrentSession().getNamedQuery(User.GET_USER_BY_EMAIL)
            .setParameter("email", emailId.toLowerCase()).list();

    	}
    	catch(Exception e){
    		log.error("SecurityServiceImpl.getSecurityUserDetails()", e);
    		throw new RsntException("SecurityServiceImpl.getUserFromEmail()", e);
    	}
    	
    }
    
    public Organization getUserOrganization(final Long userId) throws RsntException {
    	
    	try{
    		Organization org = (Organization) sessionFactory.getCurrentSession().getNamedQuery(User.GET_USER_ORGANIZATION)
            .setParameter("userId", userId).uniqueResult();
    		if (org == null)
				throw new RsntException("Unable to Find User " + userId);
			sessionContextUtil.put(Constants.CONST_ORGID, org.getOrganizationId());
			sessionContextUtil.put(Constants.CONST_ORGNAME, org.getOrganizationName());
			sessionContextUtil.put(Constants.CONST_ORGSUBSCRIBEDPLANNAME,
					org.getActiveOrgPlanSubscription().getPlan().getPlanName());
			sessionContextUtil.put(Constants.CONST_ORGSUBSCRIPTIONPLANID,
					org.getActiveOrgPlanSubscription().getOrganizationPlanId());
			sessionContextUtil.put(Constants.CONST_ORGADSBALANCE, org.getAdsBalance());
    		return org;

    	}
    	catch(Exception e){
    		log.error("SecurityServiceImpl.getSecurityUserDetails()", e);
			throw new RsntException("SecurityServiceImpl.getSecurityUserDetails()", e);
    	}
    	
    }
    
    public Long  getRoleProtectedObjectForUser(Long userId, String protectedObjectName) throws RsntException{
		
		try{
			return ((BigInteger)sessionFactory.getCurrentSession().createSQLQuery(NativeQueryConstants.GET_USER_PROTECTED_OBJECT_DATA)
					.setParameter(1, userId)
					.setParameter(2, protectedObjectName).uniqueResult()).longValue();
		}
		catch(Exception e){
			log.error("OrganizationService.getRoleProtectedObjectForUser()", e);
			throw new RsntException("OrganizationService.getRoleProtectedObjectForUser()", e);
		}
	}

    @SuppressWarnings("unchecked")
	public List<Object[]> getAllProtectedObject() throws RsntException{
		
		try{
			return sessionFactory.getCurrentSession().createSQLQuery(NativeQueryConstants.GET_ALL_PROTECTED_OBJECT_DATA)
					.list();
		}
		catch(Exception e){
			log.error("OrganizationService.getAllProtectedObject()", e);
			throw new RsntException("OrganizationService.getAllProtectedObject)", e);
		}
	}
 
    @SuppressWarnings("unchecked")
	public List<Object[]> getRoleProtectedObjectForRole(Long roleId) throws RsntException{
		
		try{
			return sessionFactory.getCurrentSession().createSQLQuery(NativeQueryConstants.GET_ROLE_PROTECTED_OBJECT_DATA)
					.setParameter(1, roleId)
					.list();
		}
		catch(Exception e){
			log.error("OrganizationService.getRoleProtectedObjectForRole()", e);
			throw new RsntException("OrganizationService.getRoleProtectedObjectForRole()", e);
		}
	}
    
    public void deleteRoleProtectedObjectMapping(Long roleId)  throws RsntException{
    	try{
    		sessionFactory.getCurrentSession().createSQLQuery(NativeQueryConstants.DELETE_ALL_ROLE_PROTECTED_OBJECT_DATA)
					.setParameter(1, roleId)
					.executeUpdate();
		}
		catch(Exception e){
			log.error("OrganizationService.deleteRoleProtectedObjectMapping()", e);
			throw new RsntException("OrganizationService.deleteRoleProtectedObjectMapping()", e);
		}
    }
    
    public void addRoleProtectedObjMapping(Long roleId, List<String> protectedObjectList, String user)  throws RsntException{
    	try{
    		for(String pObj : protectedObjectList){
    			RoleProtectedObject roleProtectedObject = new RoleProtectedObject();
    			roleProtectedObject.setRoleId(roleId);
    			roleProtectedObject.setProtectedObjectId(new Long(pObj));
    			roleProtectedObject.setCreatedBy(user);
    			roleProtectedObject.setCreatedDate(new Date());
    			sessionFactory.getCurrentSession().persist(roleProtectedObject);
    		}
		
		}
		catch(Exception e){
			log.error("OrganizationService.deleteRoleProtectedObjectMapping()", e);
			throw new RsntException("OrganizationService.deleteRoleProtectedObjectMapping()", e);
		}
    }
    
    public Long getUserRoleDetail(final String userLogin) throws RsntException {
    	try{
    		return ((Integer) sessionFactory.getCurrentSession().createSQLQuery(NativeQueryConstants.GET_USER_ROLE_ID)
            .setParameter(1, userLogin.toLowerCase()).uniqueResult()).longValue();

    	}
    	catch(Exception e){
    		log.error("SecurityServiceImpl.getUserRoleDetail()", e);
			throw new RsntException("SecurityServiceImpl.getSecurityUserDetails()", e);
    	}
     }

    @SuppressWarnings("unchecked")
	@Override
	public List<Object[]> loginCredAuth(String userName, String password) throws NoSuchUsernameException{
		List<Object[]> result =  sessionFactory.getCurrentSession().createSQLQuery(NativeQueryConstants.GET_USER_LOGIN_AUTH).setParameter("username", userName.toLowerCase()).list();
		if(result.size()>0) {
	   		Object[] loginDetail = result.get(0);
			//new BigDecimal(fdbackQtnrData[1].toString()).longValue()
			
			return result;
		}
		else 
			throw new NoSuchUsernameException("Username is not valid.");
	}
	
	@Override
	public Boolean isDuplicateUser(String userName) throws RsntException {
		User user = (User) sessionFactory.getCurrentSession().createQuery(NativeQueryConstants.CHECK_USER_IF_EXISTS)
				.setParameter("userName", userName).uniqueResult();
		if (user != null) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public Boolean isDuplicateOrganization(String orgName) throws RsntException {
		Organization org = (Organization) sessionFactory.getCurrentSession().createQuery(NativeQueryConstants.CHECK_ORGANIZATION_IF_EXISTS)
		.setParameter("orgName", orgName).uniqueResult();
		if(org != null) {
			return true;
		} else return false;
	}
	
    @Override
	public User signupUser(Credential credentials) throws RsntException {
		try {
			// Creating User
			User signUpUser = new User();
			signUpUser.setUserName(credentials.getUsername());
			signUpUser.setEmail(credentials.getUsername());
			signUpUser.setPassword(credentials.getPassword());
			signUpUser.setPrimaryContactNo(credentials.getCompanyPrimaryPhone());
			signUpUser.setFirstName(credentials.getFirstName());
			signUpUser.setLastName(credentials.getLastName());
			signUpUser.setUserRole(new UserRole());
			
			
			signUpUser.setActive(false);
			//Audit User
			signUpUser.setCreatedDate(new Date());
			signUpUser.setModifiedDate(new Date());
			signUpUser.setCreatedBy(signUpUser.getUserName());
			signUpUser.setModifiedBy(signUpUser.getUserName());
			
			UserRole userRole = new UserRole();
			userRole.setRoleId((long) Constants.CONT_LOOKUP_ROLE_RSNTADMIN);
			userRole.setCreatedBy(credentials.getUsername());
			userRole.setCreatedDate(new Date());
			userRole.setUser(signUpUser);
			signUpUser.setUserRole(userRole);
			
			Organization organization = new Organization();
			organization.setOrganizationName(credentials.getCompanyName());
			organization.setEmail(credentials.getCompanyEmail());
			organization.setPromotionalCode(credentials.getPromotionalCode());
			organization.setPrimaryPhone(Long.parseLong(credentials.getCompanyPrimaryPhone()));
			organization.setActiveFlag(true);
			organization.setOrganizationTypeId((long) Constants.CONST_ORG_TYPE_ID);
			organization.setCreatedBy(credentials.getUsername());
			organization.setCreatedDate(new Date());
			organization.setClientBase(credentials.getClientBase());
			organization.setSmsSignature(organization.getOrganizationName());
		
			organization.setSmsRoute(smsRoute);

			OrganizationPlanSubscription organizationPlanSubscription = new OrganizationPlanSubscription();
			organizationPlanSubscription.setAmountPerUnit(new BigDecimal(0.0));
			organizationPlanSubscription.setCostPerAd(new BigDecimal(0.0));
			organizationPlanSubscription.setCreatedBy(credentials.getUsername());
			organizationPlanSubscription.setCreatedDate(new Date());
			organizationPlanSubscription.setCurrencyId(11L);
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			cal.add(Calendar.MONTH, 1);
			organizationPlanSubscription.setEndDate(cal.getTime());
			organizationPlanSubscription.setModifiedBy(credentials.getUsername());
			organizationPlanSubscription.setModifiedDate(new Date());
			organizationPlanSubscription.setNoOfAdsPerUnit(0L);
			organizationPlanSubscription.setNumberOfUnits(0L);
			organizationPlanSubscription.setOrganization(organization);
			Plan plan = new Plan();
			plan.setPlanId(3L);
			organizationPlanSubscription.setPlan(plan);
			organizationPlanSubscription.setStartDate(new Date());
			organizationPlanSubscription.setTerminateDate(null);
			organizationPlanSubscription.setTotalAmount(new BigDecimal(0.0));
			organizationPlanSubscription.setUnitId(9L);
			organization.setActiveOrgPlanSubscription(organizationPlanSubscription);
			organization.setOrganizationPlanSubscriptionList(new ArrayList<>());
			organization.getOrganizationPlanSubscriptionList().add(organizationPlanSubscription);
			
			organization.setOrganizationCategoryList(new ArrayList<>());
			for(long i=39; i<=43; i++){
				OrganizationCategory orgCat = new OrganizationCategory();
				orgCat.setOrganization(organization);
				orgCat.setCategoryTypeId(18L);
				orgCat.setCategoryValueId(i);
				organization.getOrganizationCategoryList().add(orgCat);
			}
			
			BigDecimal adsBalance = new BigDecimal(0);
			organization.setAdsBalance(adsBalance);
			
			OrganizationUser organizationUser = new OrganizationUser();
			organizationUser.setOrganization(organization);
			organizationUser.setCreatedBy(credentials.getUsername());
			organizationUser.setCreatedDate(new Date());
			organizationUser.setUser(signUpUser);
			List<OrganizationUser> organizationUserList = new ArrayList<>();
			organizationUserList.add(organizationUser);
			organization.setOrganizationUserList(organizationUserList);
			signUpUser.setOrganizationUser(organizationUser);
		
			
			Long i = (Long) sessionFactory.getCurrentSession().save(signUpUser); 
			
			if (i != null) {	
				emailUtil.sendWelcomeEmail(signUpUser.getUserName(), signUpUser.getFirstName() + " " + signUpUser.getLastName());
				return signUpUser;
			}
			return null;
		} catch(Exception e){
    		log.error("SecurityServiceImpl.signupUser()", e);
			throw new RsntException("SecurityServiceImpl.signupUser()", e);
    	}
	}
    //	User Account Activation  by Aarshi(11/03/2019)
	public Boolean authVerification(Integer userId, String authCode) throws RsntException {

		try {
			Boolean authVerification = userDao.Verifyauthcode(userId, authCode);
		

			if (authVerification.equals(true)) {
				User user=userDao.find(userId.longValue());
				user.setActive(true);
				user.setModifiedBy(user.getUserName());
				user.setModifiedDate(new Date());
				System.out.println("The PassWord is" +user.getPassword());
				user.setAuthcode(null);
				userDao.update(user);
				return true;
			} else {
				return false;
			}
		} catch (Exception ex) {
			LoggerUtil.logError("Error During Verification AuthCode", ex);
			throw new RsntException("UserAccount Activication"+userId, ex);

		}
	}
   //Send authentication mail to user by Aarshi(12/03/2019)
	@Override
	public void sendActivationMail(Integer userId)throws RsntException {
		// TODO Auto-generated method stub
		
		try
		{
			User user=userDao.find(userId.longValue());
            emailUtil.senAuthCodeEmail(user);
		    
		}catch(Exception ex){
	      LoggerUtil.logError("Error while Send AuthCode", ex);
	      throw new RsntException("SendAuthCode Mail"+userId, ex);
	        
		}
		
	}
	//Change password  by Aarshi(13/03/2019)
	@Override
	public Boolean changePassword(Integer userId,String oldPassword,String newPassowrd) throws RsntException {
		// TODO Auto-generated method stub
		try{
			 String password=CommonUtil.encryptPassword(oldPassword);
			 User user=userDao.find(userId.longValue());
			 if(password.equals(user.getPassword())){
				user.setPassword(CommonUtil.encryptPassword(newPassowrd));
				user.setModifiedBy(user.getUserName());
				user.setModifiedDate(new Date());
				userDao.update(user);
			    return true;
			}else{
				return false;
			}
			 
		}catch(Exception ex){
			LoggerUtil.logError("Error while Change Password", ex);
		    throw new RsntException("ChangePassword"+userId, ex);
		        
		}
	}
	@Override
	public User signupUserV2(Credential credentials) throws RsntException {
		Properties oProperties=new Properties();
		try {
			oProperties = PropertyUtility.fetchPropertyFile(this.getClass(),Constants.RSNTPROPERTIES);
		} catch (IOException e1) {
			LoggerUtil.logError("unable to fetch property file");
		}
		try {
			// Creating User
			User signUpUser = new User();
			signUpUser.setUserName(credentials.getUsername());
			signUpUser.setEmail(credentials.getUsername());
			signUpUser.setPassword(credentials.getPassword());
			signUpUser.setPrimaryContactNo(credentials.getCompanyPrimaryPhone());
			signUpUser.setFirstName(credentials.getFirstName());
			signUpUser.setLastName(credentials.getLastName());
			signUpUser.setUserRole(new UserRole());
			
			
			signUpUser.setActive(false);
			//Audit User
			signUpUser.setCreatedDate(new Date());
			signUpUser.setModifiedDate(new Date());
			signUpUser.setCreatedBy(signUpUser.getUserName());
			signUpUser.setModifiedBy(signUpUser.getUserName());
			
			UserRole userRole = new UserRole();
			userRole.setRoleId((long) Constants.CONT_LOOKUP_ROLE_RSNTADMIN);
			userRole.setCreatedBy(credentials.getUsername());
			userRole.setCreatedDate(new Date());
			userRole.setUser(signUpUser);
			signUpUser.setUserRole(userRole);
			
			Organization organization = new Organization();
			organization.setOrganizationName(credentials.getCompanyName());
			organization.setEmail(credentials.getCompanyEmail());
			organization.setPromotionalCode(credentials.getPromotionalCode());
			organization.setPrimaryPhone(Long.parseLong(credentials.getCompanyPrimaryPhone()));
			organization.setActiveFlag(true);
			organization.setOrganizationTypeId((long) Constants.CONST_ORG_TYPE_ID);
			organization.setCreatedBy(credentials.getUsername());
			organization.setCreatedDate(new Date());
			organization.setClientBase(credentials.getClientBase());
			organization.setSmsSignature(organization.getOrganizationName());
			
			organization.setSmsRoute(smsRoute);
		    organization.setMaxParty(Integer.parseInt(oProperties.getProperty(Constants.MAXPARTY)));
			organization.setSmsRouteNo(oProperties.getProperty(Constants.ROUTRNO));
		    organization.setWaitTime(Long.parseLong(oProperties.getProperty(Constants.WAITTIME)));
			organization.setNotifyUserCount(Long.parseLong(oProperties.getProperty(Constants.NOTIFYUSERCOUNT)));

			OrganizationPlanSubscription organizationPlanSubscription = new OrganizationPlanSubscription();
			organizationPlanSubscription.setAmountPerUnit(new BigDecimal(0.0));
			organizationPlanSubscription.setCostPerAd(new BigDecimal(0.0));
			organizationPlanSubscription.setCreatedBy(credentials.getUsername());
			organizationPlanSubscription.setCreatedDate(new Date());
			organizationPlanSubscription.setCurrencyId(11L);
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			cal.add(Calendar.MONTH, 1);
			organizationPlanSubscription.setEndDate(cal.getTime());
			organizationPlanSubscription.setModifiedBy(credentials.getUsername());
			organizationPlanSubscription.setModifiedDate(new Date());
			organizationPlanSubscription.setNoOfAdsPerUnit(0L);
			organizationPlanSubscription.setNumberOfUnits(0L);
			organizationPlanSubscription.setOrganization(organization);
			Plan plan = new Plan();
			plan.setPlanId(3L);
			organizationPlanSubscription.setPlan(plan);
			organizationPlanSubscription.setStartDate(new Date());
			organizationPlanSubscription.setTerminateDate(null);
			organizationPlanSubscription.setTotalAmount(new BigDecimal(0.0));
			organizationPlanSubscription.setUnitId(9L);
			organization.setActiveOrgPlanSubscription(organizationPlanSubscription);
			organization.setOrganizationPlanSubscriptionList(new ArrayList<>());
			organization.getOrganizationPlanSubscriptionList().add(organizationPlanSubscription);
			
			organization.setOrganizationCategoryList(new ArrayList<>());
			for(long i=39; i<=43; i++){
				OrganizationCategory orgCat = new OrganizationCategory();
				orgCat.setOrganization(organization);
				orgCat.setCategoryTypeId(18L);
				orgCat.setCategoryValueId(i);
				organization.getOrganizationCategoryList().add(orgCat);
			}
			
			BigDecimal adsBalance = new BigDecimal(0);
			organization.setAdsBalance(adsBalance);
			
			OrganizationUser organizationUser = new OrganizationUser();
			organizationUser.setOrganization(organization);
			organizationUser.setCreatedBy(credentials.getUsername());
			organizationUser.setCreatedDate(new Date());
			organizationUser.setUser(signUpUser);
			List<OrganizationUser> organizationUserList = new ArrayList<>();
			organizationUserList.add(organizationUser);
			organization.setOrganizationUserList(organizationUserList);
			signUpUser.setOrganizationUser(organizationUser);
		
			
			Long i = (Long) sessionFactory.getCurrentSession().save(signUpUser); 
			
			if (i != null) {	
				//Comment done by aarshi 
				//because error give to send mail in local
				emailUtil.sendWelcomeEmail(signUpUser.getUserName(), signUpUser.getFirstName() + " " + signUpUser.getLastName());
				return signUpUser;
			}
			return null;
		} catch(Exception e){
    		log.error("SecurityServiceImpl.signupUser()", e);
			throw new RsntException("SecurityServiceImpl.signupUser()", e);
    	}
	}
	//Check the userName and email id of User  by Aarshi(19/03/2019)
	@Override
	public String checkIfExistingUser(String userId, String userName,String email) throws RsntException {
		// TODO Auto-generated method stub
		//checkIfExistingUser
		try{
				User user=userDao.find(Long.parseLong(userId));
				if(user.getEmail().equals(email) && user.getUserName().equals(userName)){
					return Constants.STATUS;
				}else{
					BigInteger countUserName=(BigInteger)sessionFactory.getCurrentSession().createSQLQuery(NativeQueryConstants.COUNT_USERNAME).setParameter("username", userName).uniqueResult();
					BigInteger countUserEmail=(BigInteger)sessionFactory.getCurrentSession().createSQLQuery(NativeQueryConstants.COUNT_USEREMAIL).setParameter("email",email.toLowerCase()).uniqueResult();
		
					if((countUserName.intValue())>=2) {
						return Constants.CHECK_USER;
					}else if((countUserEmail.intValue())>=2){
						return Constants.CHECK_EMAIL;
					}else{
						return  Constants.STATUS;
					}
			
				}
			
		}catch(Exception ex){
			log.error("Error while check username end email", ex);
			throw new RsntException("Error while checkIfExistingUser", ex);
		}
		
	}

	
	
	/* this service return the object of language preference by arjun 26/11/2019 */
		@SuppressWarnings("unchecked")
		public Map<String, String> languageLocalization(String langIsoCode){
		
		Map<String, String> LanguageMap = new HashMap<String, String>();
		
		
		
		Criteria criteria =sessionFactory.getCurrentSession().createCriteria(LanguageKeyMapping.class);
		
		criteria.add(Restrictions.eq("langIsoCode", langIsoCode));
		
		List<LanguageKeyMapping> result =criteria.list();
		
		//createSQLQuery(NativeQueryConstants.GET_KEY_VALUE_BY_ISO).setParameter("langIsoCode", langIsoCode).list();
		 
		// Criteria criteria = sessionFactory.createCriteria(Employee.class);
         
		 
		 for (LanguageKeyMapping languageKeyMapping : result) {
			 
			 //Translator translate = Translator.getInstance();
			 LanguageMap.put(languageKeyMapping.getKeyName(), languageKeyMapping.getValue().toString());	
			 System.out.println("language mapping "+languageKeyMapping.getValue());
		}
			
		return LanguageMap;
	}
}	