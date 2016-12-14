package com.rsnt.service.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.log.Log;

import com.rsnt.common.exception.RsntException;
import com.rsnt.entity.User;
import com.rsnt.service.IStaffService;
import com.rsnt.util.common.Constants;
import com.rsnt.util.common.NativeQueryConstants;
@Stateless(name=IStaffService.JNDI_NAME)
@Scope(ScopeType.STATELESS)
@Name(IStaffService.SEAM_NAME)
public class StaffServiceImpl  implements IStaffService{

	@In
	private EntityManager entityManager;
	
	@Logger
	private Log log;
	
	@SuppressWarnings("unchecked")
	public List<Object[]> loadStaffData(Long orgId) throws RsntException{
		
		try{
			//final Long orgId = (Long) Contexts.getSessionContext().get(Constants.CONST_ORGID);
			
			if(orgId != null) {
				entityManager.clear();
				return entityManager.createNativeQuery(NativeQueryConstants.GET_STAFF_DATA)
				.setParameter(1, orgId).getResultList();
			}
			return null;
		}
		catch(Exception e){
			log.error("StaffServiceImpl.loadStaffData()", e);
			throw new RsntException("StaffServiceImpl.loadStaffData()", e);
		}
	}
	
	public void persist(Object object) throws RsntException {
		try{
			entityManager.persist(object);
			entityManager.flush();
		}
		catch(Exception e){
			log.error("StaffServiceImpl", e);
			throw new RsntException("StaffServiceImpl.persist()", e);
		}
	}
	
	public Object merge(Object object) throws RsntException {
		try{
			entityManager.merge(object);
			entityManager.flush();
			return object;
		}
		catch(Exception e){
			log.error("StaffServiceImpl", e);
			throw new RsntException("StaffServiceImpl.merge()", e);
		}
	}
	
	public Object find(Long id) throws RsntException {
		try{
			return entityManager.find(User.class,id);
		}
		catch(Exception e){
			log.error("StaffServiceImpl", e);
			throw new RsntException("StaffServiceImpl.find()", e);
		}
	}
	
	public List<User> findUserByUserName(String userName){
	
		try{
			return entityManager.createNamedQuery(User.GET_USER_BY_USERNAME_ONLY).setParameter(1, userName.toLowerCase())
					//.setParameter(2, Long.valueOf(Contexts.getSessionContext().get(Constants.CONST_ORGID).toString()))
					.getResultList();
		}
		catch(Exception e){
			log.error("StaffServiceImpl", e);
			return null;
		}
		
	}
	
	public void updatePassword(String newPassword, Long userId) throws RsntException {
		try{
			entityManager.createNativeQuery(NativeQueryConstants.UPDATE_USER_PASSWORD_USERID).setParameter(1, newPassword).setParameter(2, userId).executeUpdate();	
		}
		catch(Exception e){
			log.error("StaffServiceImpl", e);
			throw new RsntException("StaffServiceImpl.updatePassword()", e);
		}
	}
	

}
