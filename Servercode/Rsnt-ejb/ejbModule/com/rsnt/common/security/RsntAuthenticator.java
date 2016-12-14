package com.rsnt.common.security;

import java.util.List;

import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.AuthorizationException;
import org.jboss.seam.security.Credentials;
import org.jboss.seam.security.Identity;
import org.jboss.seam.web.IdentityRequestWrapper;
import org.jboss.seam.web.Session;

import com.rsnt.entity.Organization;
import com.rsnt.entity.ProtectedObject;
import com.rsnt.entity.User;
import com.rsnt.service.ISecurityService;
import com.rsnt.util.common.CommonUtil;
import com.rsnt.util.common.Constants;
import com.rsnt.web.action.SecurityAction;


@Name("rsntAuthenticator")
public class RsntAuthenticator {

    @Logger
    private Log log;

    @In
    private Identity identity;
    
	@In
	private static EntityManager entityManager;

	 @In 
	 private Credentials credentials;
	   
    @In(create = true, required = false)
    @Out(scope=ScopeType.SESSION, required=false)
    private User loginUser;
    

    @In(create = true)
   private ISecurityService securityService;
    
   /* @In(create = true)
    private SecurityManager securityManager;
    
    @In(create = true, required = false)
    @Out(scope=ScopeType.SESSION, required=false)
    private SecurityUserLoad loginUser;
    
    @In(create = true)
    private IUserPreferenceService userPreferenceServiceImpl;
    
    @In(value = "cacheService", create = true, required = false)
    CacheService cacheService;*/
    
    /**
     * @return true when authorization is successful and false when authorization is failed.
     * @throws AuthorizationException
     * @throws Exception
     */
    public boolean login() throws Exception{
        boolean isAuthenticated = true;
        try {
            final IdentityRequestWrapper identityRequestWrapper = (IdentityRequestWrapper) FacesContext
                    .getCurrentInstance().getExternalContext().getRequest();
            final HttpServletRequest request = (HttpServletRequest) identityRequestWrapper.getRequest();
            log.info("User Principal...", request.getUserPrincipal());
            
            if (request.getUserPrincipal() != null && !request.getUserPrincipal().getName().isEmpty()) {
            	 identity.logout();
                identity.getCredentials().setUsername(request.getUserPrincipal().getName().trim().toLowerCase());
                identity.getCredentials().setPassword("*");
                identity.login();
                //identity.isLoggedIn(true);
                //identity.instance().s
                isAuthenticated = identity.isLoggedIn();
                log.info("Authenticated value:" + isAuthenticated);
                if (!isAuthenticated) {
                    throw new AuthorizationException("User does not exists or is Inactive");
                }
                log.info("Authenticated by IDM:" + request.getUserPrincipal().getName());
            } else {
                // Local workaround...
                identity.getCredentials().setUsername("et1");
                identity.getCredentials().setPassword("et1");
                //identity.login();
                log.info("Local Workaround...................... : et1");
                isAuthenticated = true;
            }
            loginUser = securityService.getSecurityUserDetails(identity.getCredentials().getUsername());
            Long roleId= securityService.getUserRoleDetail(identity.getCredentials().getUsername());
            Contexts.getSessionContext().set(Constants.CONST_USERROLEID,roleId);
            if(loginUser==null) throw new AuthorizationException("Unable to Find User "+ identity.getCredentials().getUsername());
            
            
            addIdentityPermissions(loginUser);
        } catch (AuthorizationException e) {
            log.error("Exception: ", e);
            throw e;
        } catch (Exception e) {
        	e.printStackTrace();
            log.error("Exception: ", e);
            throw e;
        }

        FacesMessages.instance().clear();
        return isAuthenticated;
    }
    
    public void loadOrgContextData() throws Exception{
    	try{
    		Organization org = securityService.getUserOrganization(loginUser.getUserId());
            //Long orgId = new Long(1);
            if(org==null) throw new AuthorizationException("Unable to Find User "+ identity.getCredentials().getUsername());
            Contexts.getSessionContext().set(Constants.CONST_ORGID,org.getOrganizationId());
            Contexts.getSessionContext().set(Constants.CONST_ORGNAME,org.getOrganizationName());
            Contexts.getSessionContext().set(Constants.CONST_ORGSUBSCRIBEDPLANNAME,org.getActiveOrgPlanSubscription().getPlan().getPlanName());
            Contexts.getSessionContext().set(Constants.CONST_ORGSUBSCRIPTIONPLANID,org.getActiveOrgPlanSubscription().getOrganizationPlanId());
            Contexts.getSessionContext().set(Constants.CONST_ORGADSBALANCE,org.getAdsBalance());
    	}
    	catch(Exception e){
    		  log.error("Exception: ", e);
    		  throw e;
    	}
    	
    }

    /**
     * This method does the authorization when identity.login() is called.
     * SecurityUserLoad and List of UserObjectPermission will be kept in session so as to access across the
     * application.
     * @throws Exception 
     */
    public boolean authenticate() throws Exception {
        boolean isAuthenticated = true;

    	/*log.info("RpmAuthenticator.authenticate().Username = "+ credentials.getUsername());
    	log.info("RpmAuthenticator.authenticate().password = "+ credentials.getPassword());
    	log.info("RpmAuthenticator.authenticate().Username = "+ identity.getCredentials().getUsername());
    	log.info("RpmAuthenticator.authenticate().password = "+ identity.getCredentials().getPassword());
    	   User user = (User) entityManager.createQuery(

                 "from User where username = :userName and password = :password")

                 .setParameter("userName", identity.getCredentials().getUsername())

                 .setParameter("password", CommonUtil.encryptPassword(identity.getCredentials().getPassword()))

                 .getSingleResult();
         
         log.info("Authenticated by IDM:" +  user );
   
     loginUser = securityService.getSecurityUserDetails(identity.getCredentials().getUsername());
     Long roleId= securityService.getUserRoleDetail(identity.getCredentials().getUsername());
     Contexts.getSessionContext().set(Constants.CONST_USERROLEID,roleId);
     if(loginUser==null) throw new AuthorizationException("Unable to Find User "+ identity.getCredentials().getUsername());
     addIdentityPermissions(loginUser);*/
    	// identity.addRole("admin");
        //loginUser = securityManager.getSecurityUserDetails(identity.getCredentials().getUsername());
        //log.info("RpmAuthenticator.authenticate().loginUser = "+ loginUser);
        //log.info("RpmAuthenticator.authenticate().Userlogin = "+ loginUser.getUserLogin());
        /*final List<UserObjectPermission> userObjectPermissions = securityManager.getUserObjectPermissions(loginUser.getUserLogin());
        log.info("RpmAuthenticator.authenticate().userObjectPermissions = "+ userObjectPermissions+"\t Size = "+userObjectPermissions.size());
        List<Long> businessDivisionIdList = new ArrayList<Long>();
        List<Long> salesDivisionIdList = new ArrayList<Long>();
        try {
            final List<SecurityGroupLookupPermission> securityGroupLookupPermissionList = securityManager.getBusinessDivisionId(loginUser.getUserLogin());
            log.info("RpmAuthenticator.authenticate().securityGroupLookupPermissionList = "+ securityGroupLookupPermissionList+"\t Size = "+securityGroupLookupPermissionList.size());
            if (securityGroupLookupPermissionList != null && securityGroupLookupPermissionList.size()>0 && 
                    securityGroupLookupPermissionList.get(0).getSecurityGroupLookupPermissionPK() != null) {
                Contexts.getSessionContext().set("businessDivisionId", securityGroupLookupPermissionList.get(0).getSecurityGroupLookupPermissionPK().getLookupId());
                for(SecurityGroupLookupPermission obj : securityGroupLookupPermissionList) {
                	if (!businessDivisionIdList.contains(obj.getSecurityGroupLookupPermissionPK().getLookupId())) {
                		businessDivisionIdList.add(obj.getSecurityGroupLookupPermissionPK().getLookupId());
                	}
                }
                Contexts.getSessionContext().set("businessDivisionIdList",businessDivisionIdList);
                
                Contexts.getSessionContext().set(RPMConstants.ALL_USER_PREF_SESSION_VAR,
                        userPreferenceServiceImpl.getAllUserPreferences());
                Contexts.getSessionContext().set(RPMConstants.USER_HAS_USER_PREF_SESSION_VAR,
                        userPreferenceServiceImpl.getPreferencesForUser(loginUser.getUserLogin()));
            } else {
            	return false;
            }
            
            List<SecurityGroupLookupPermission> salesGroupLookupPermissionList = securityManager.getSalesDivisionId(loginUser.getUserLogin());
            if (salesGroupLookupPermissionList != null && salesGroupLookupPermissionList.size()>0 && 
            		salesGroupLookupPermissionList.get(0).getSecurityGroupLookupPermissionPK() != null) {
            	
            	for(SecurityGroupLookupPermission obj : salesGroupLookupPermissionList) {
                	if (!salesDivisionIdList.contains(obj.getSecurityGroupLookupPermissionPK().getLookupId())) {
                		salesDivisionIdList.add(obj.getSecurityGroupLookupPermissionPK().getLookupId());
                	}
                }
                Contexts.getSessionContext().set("salesDivisionIdList",salesDivisionIdList);
                
            }
            
        } catch (final RPMException rpmException) {
        	log.error(rpmException);
        	return false;
        }
        */
        //addIdentityPermissions(loginUser);
        //addRpmLoginDetails(loginUser);
        //loadDefaultColumnModels();
        return isAuthenticated;
    }
    
    public void logout() {

        if (identity.isLoggedIn()) {
            identity.logout();
        }
        identity.logout();
        Contexts.getSessionContext().remove("loginUser");
        Session.instance().invalidate();
    }

    /**
     * @param userObjectPermissions
     *            This method will add the permission details in the identity object which
     *            can be access across the application for the logged in user.
     */
    private void addIdentityPermissions(final User loginUser) {
        final List<String> userPermissions = securityService.getUserPermissions(loginUser.getUserId());
        if (userPermissions != null && !userPermissions.isEmpty()) {
            for (final String permission : userPermissions) {
                identity.addRole(permission);
            }
        }
    }

   /*
    *//**
     * @param securityUserLoad
     *            This method will persist the RpmUserEventLog table with the login details.
     *//*
    private void addRpmLoginDetails(final SecurityUserLoad securityUserLoad) {
        final RpmUserEventLog rpmUserEventLog = new RpmUserEventLog();
        rpmUserEventLog.setUserLogin(securityUserLoad.getUserLogin());
        rpmUserEventLog.setLoginDate(new Date());
        securityManager.create(rpmUserEventLog);
    }
    
    *//**
     * Load default column models.
     * @throws ClassNotFoundException 
     * @throws IOException 
     *
     * @throws RPMException the rPM exception
     *//*
    private void loadDefaultColumnModels()  {
        if (!CommonUtil.isNullOrEmpty(loginUser.getDealTitlesJSONString())) {
            return;
        }
		Map<String, Map<Integer, String>> columnModelMap = null;
		
		try {
			columnModelMap = (Map<String, Map<Integer, String>>) cacheService.get(Constants.DEFAULT_COLUMN_MODEL_CACHE_KEY);
			String availsJSONString = null;
			
			String proposedTitlesJSONString = null;
			String dealTitlesJSONString = null;
			
	        if (!CommonUtil.isNullOrEmpty(columnModelMap.get(loginUser.getUserLogin()))) {
	        	// User has his own view.
	            final String availsJSONStringTemp = columnModelMap.get(loginUser.getUserLogin()).get(
	                    Constants.ADHOC_METADATA_QUERY_AVAILS);
	            if (!CommonUtil.isNullOrEmpty(availsJSONStringTemp)) {
	                availsJSONString = availsJSONStringTemp;
	            }
	            final String proposedTitlesJSONStringTemp = columnModelMap.get(loginUser.getUserLogin()).get(
	                    Constants.ADHOC_METADATA_QUERY_PROPOSED_TITLES);
	            if (!CommonUtil.isNullOrEmpty(proposedTitlesJSONStringTemp)) {
	                proposedTitlesJSONString = proposedTitlesJSONStringTemp;
	            }
	            final String dealTitlesJSONStringTemp = columnModelMap.get(loginUser.getUserLogin()).get(
	                    Constants.ADHOC_METADATA_QUERY_DEAL_TITLES);
	            if (!CommonUtil.isNullOrEmpty(dealTitlesJSONStringTemp)) {
	                dealTitlesJSONString = dealTitlesJSONStringTemp;
	            }
	        } else {
	        	// Assign user to a default view
	        	availsJSONString = columnModelMap.get(Constants.SYSTEM_DEFAULT).get(
		                Constants.ADHOC_METADATA_QUERY_AVAILS);
		        proposedTitlesJSONString = columnModelMap.get(Constants.SYSTEM_DEFAULT).get(
		                Constants.ADHOC_METADATA_QUERY_PROPOSED_TITLES);
		        dealTitlesJSONString = columnModelMap.get(Constants.SYSTEM_DEFAULT).get(
		                Constants.ADHOC_METADATA_QUERY_DEAL_TITLES);
	        }
	        loginUser.setAvailsColumnsJSONString(availsJSONString);
	        loginUser.setProposedTitlesColumnsJSONString(proposedTitlesJSONString);
	        loginUser.setDealTitlesJSONString(dealTitlesJSONString);
	        loginUser.setDealTitlesSortString(CommonUtil.fetchDefaultSortingInfo(dealTitlesJSONString));
	        loginUser.setProposedTitlesSortString(CommonUtil.fetchDefaultSortingInfo(proposedTitlesJSONString));
	        loginUser.setAvailsSortString(CommonUtil.fetchDefaultSortingInfo(availsJSONString));
	        
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("RpmAuthenticator.loadDefaultColumnModels() Exception" + e.getMessage());
		}
    }
    
    *//**
     * This will logout the user from the application.
     *//*
   

    *//**
     * This method will update the RpmUserEventLog for logout history when logout is done.
     *//*
    private void updateRpmLogoutDetails(final String userLogin) {
        final RpmUserEventLog rpmUserEventLog = securityManager.getRpmEventLogByMaxId(userLogin);
        rpmUserEventLog.setLogoutDate(new Date());
        securityManager.update(rpmUserEventLog);
    }

	public SecurityUserLoad getLoginUser() {
		return loginUser;
	}

	public void setLoginUser(final SecurityUserLoad loginUser) {
		this.loginUser = loginUser;
	}

	public void setCacheService(CacheService cacheService) {
		this.cacheService = cacheService;
	}*/
    
    public User getLoginUser() {
		return loginUser;
	}

	public void setLoginUser(User loginUser) {
		this.loginUser = loginUser;
	}

	public ISecurityService getSecurityService() {
		return securityService;
	}

	public void setSecurityService(ISecurityService securityService) {
		this.securityService = securityService;
	}
    
    
}
