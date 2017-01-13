package com.kyobee.service.impl;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import org.hibernate.SessionFactory;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kyobee.entity.Organization;
import com.kyobee.entity.RoleProtectedObject;
import com.kyobee.entity.User;
import com.kyobee.exception.RsntException;
import com.kyobee.service.ISecurityService;
import com.kyobee.util.AppTransactional;
import com.kyobee.util.SessionContextUtil;
import com.kyobee.util.common.CommonUtil;
import com.kyobee.util.common.Constants;
import com.kyobee.util.common.NativeQueryConstants;

@AppTransactional
@Repository
public class SecurityServiceImpl implements ISecurityService {

	@Autowired
    private SessionFactory sessionFactory;
	
	@Autowired
	private SessionContextUtil sessionContextUtil;

	/*@Logger
	private Log log;*/
	private Logger log = Logger.getLogger(SecurityServiceImpl.class);
	
	
	
    /**
     * @param userLogin
     * @return SecurityUserLoad associated with userLogin
     */
    public User loginAndFetchUser(final String userLogin, final String password) throws RsntException {
    	try{
			return (User) sessionFactory.getCurrentSession().getNamedQuery(User.GET_PROFILE_BY_USERLOGIN)
					.setParameter("username", userLogin.toLowerCase())
					.setParameter("password", CommonUtil.encryptPassword(password)).uniqueResult();

    	}
    	catch(Exception e){
    		log.error("SecurityServiceImpl.getSecurityUserDetails()", e);
			throw new RsntException("SecurityServiceImpl.getSecurityUserDetails()", e);
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
            .setParameter(1, emailId.toLowerCase()).list();

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
    
    @Override
	public List<Object[]> loginCredAuth(String userName, String password) {
		List<Object[]> result =  sessionFactory.getCurrentSession().createSQLQuery(NativeQueryConstants.GET_USER_LOGIN_AUTH).setParameter("username", userName.toLowerCase()).list();
   		Object[] loginDetail = result.get(0);
   		
		System.out.println(loginDetail[0].toString());
		System.out.println(loginDetail[1].toString());
		System.out.println(loginDetail[2].toString());
		System.out.println(loginDetail[3].toString());
		//new BigDecimal(fdbackQtnrData[1].toString()).longValue()
		
		return result;
	}

    
}
