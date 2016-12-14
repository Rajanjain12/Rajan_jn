package com.rsnt.service.impl;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

import com.rsnt.common.exception.RsntException;
import com.rsnt.entity.Organization;
import com.rsnt.entity.ProtectedObject;
import com.rsnt.entity.RoleProtectedObject;
import com.rsnt.entity.User;
import com.rsnt.service.ISecurityService;
import com.rsnt.util.common.NativeQueryConstants;

@Stateless(name=ISecurityService.JNDI_NAME)
@Scope(ScopeType.STATELESS)
@Name(ISecurityService.SEAM_NAME)
public class SecurityServiceImpl implements ISecurityService {

	@In
    private EntityManager entityManager;

	@Logger
	private Log log;
	
	
	
    /**
     * @param userLogin
     * @return SecurityUserLoad associated with userLogin
     */
    public User getSecurityUserDetails(final String userLogin) throws RsntException {
    	try{
    		return (User) entityManager.createNamedQuery(User.GET_PROFILE_BY_USERLOGIN)
            .setParameter(1, userLogin.toLowerCase()).getSingleResult();

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
            permissions = (List<String>) entityManager
                    .createNamedQuery(RoleProtectedObject.FETCH_ROLE_PERMISSIONS_BY_USER_ID)
                    .setParameter("userId", userId).getResultList();

        } catch (Exception e) {
            permissions = null;
            log.error("SecurityServiceImpl.getUserPermissions()",e);
        } finally {
        }
        return permissions;
    }
    
    public User getUserFromEmail(String emailId) throws RsntException{
    	try{
    		return (User) entityManager.createNamedQuery(User.GET_USER_BY_EMAIL)
            .setParameter(1, emailId.toLowerCase()).getSingleResult();

    	}
    	catch(Exception e){
    		log.error("SecurityServiceImpl.getSecurityUserDetails()", e);
    		throw new RsntException("SecurityServiceImpl.getUserFromEmail()", e);
    	}
    	
    }
    
    public Organization getUserOrganization(final Long userId) throws RsntException {
    	
    	try{
    		return (Organization) entityManager.createNamedQuery(User.GET_USER_ORGANIZATION)
            .setParameter(1, userId).getSingleResult();

    	}
    	catch(Exception e){
    		log.error("SecurityServiceImpl.getSecurityUserDetails()", e);
			throw new RsntException("SecurityServiceImpl.getSecurityUserDetails()", e);
    	}
    	
    }
    
    public Long  getRoleProtectedObjectForUser(Long userId, String protectedObjectName) throws RsntException{
		
		try{
			return ((BigInteger)entityManager.createNativeQuery(NativeQueryConstants.GET_USER_PROTECTED_OBJECT_DATA)
					.setParameter(1, userId)
					.setParameter(2, protectedObjectName).getSingleResult()).longValue();
		}
		catch(Exception e){
			log.error("OrganizationService.getRoleProtectedObjectForUser()", e);
			throw new RsntException("OrganizationService.getRoleProtectedObjectForUser()", e);
		}
	}

    public List<Object[]> getAllProtectedObject() throws RsntException{
		
		try{
			return entityManager.createNativeQuery(NativeQueryConstants.GET_ALL_PROTECTED_OBJECT_DATA)
					.getResultList();
		}
		catch(Exception e){
			log.error("OrganizationService.getAllProtectedObject()", e);
			throw new RsntException("OrganizationService.getAllProtectedObject)", e);
		}
	}
 
    public List<Object[]> getRoleProtectedObjectForRole(Long roleId) throws RsntException{
		
		try{
			return entityManager.createNativeQuery(NativeQueryConstants.GET_ROLE_PROTECTED_OBJECT_DATA)
					.setParameter(1, roleId)
					.getResultList();
		}
		catch(Exception e){
			log.error("OrganizationService.getRoleProtectedObjectForRole()", e);
			throw new RsntException("OrganizationService.getRoleProtectedObjectForRole()", e);
		}
	}
    
    public void deleteRoleProtectedObjectMapping(Long roleId)  throws RsntException{
    	try{
			 entityManager.createNativeQuery(NativeQueryConstants.DELETE_ALL_ROLE_PROTECTED_OBJECT_DATA)
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
    			entityManager.persist(roleProtectedObject);
    		}
		
		}
		catch(Exception e){
			log.error("OrganizationService.deleteRoleProtectedObjectMapping()", e);
			throw new RsntException("OrganizationService.deleteRoleProtectedObjectMapping()", e);
		}
    }
    
    public Long getUserRoleDetail(final String userLogin) throws RsntException {
    	try{
    		return ((Integer) entityManager.createNativeQuery(NativeQueryConstants.GET_USER_ROLE_ID)
            .setParameter(1, userLogin.toLowerCase()).getSingleResult()).longValue();

    	}
    	catch(Exception e){
    		log.error("SecurityServiceImpl.getUserRoleDetail()", e);
			throw new RsntException("SecurityServiceImpl.getSecurityUserDetails()", e);
    	}
     }

    
}
