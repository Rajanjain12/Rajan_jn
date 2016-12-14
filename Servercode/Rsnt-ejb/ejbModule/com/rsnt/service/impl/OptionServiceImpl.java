package com.rsnt.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
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
import com.rsnt.common.service.impl.RsntBaseService;
import com.rsnt.entity.OrganizationOption;
import com.rsnt.service.IOptionService;
import com.rsnt.util.common.Constants;
import com.rsnt.util.common.NativeQueryConstants;

@Stateless(name = IOptionService.JNDI_NAME)
@Scope(ScopeType.STATELESS)
@Name(IOptionService.SEAM_NAME)
public class OptionServiceImpl extends RsntBaseService implements IOptionService {

	@In
	private EntityManager entityManager;
	
	@Logger
	private Log log;
	
	@SuppressWarnings("unchecked")
	public List<Object[]> loadOptionData(Long pOrgId) throws RsntException{
		
		try{
			//final Long orgId = (Long) Contexts.getSessionContext().get(Constants.CONST_ORGID);
			
			if(pOrgId != null) {
				entityManager.clear();
				return entityManager.createNativeQuery(NativeQueryConstants.GET_OPTION_DATA)
				.setParameter(1, pOrgId).getResultList();
			}
			return null;
		}
		catch(Exception e){
			log.error("OptionServiceImpl.loadStaffData()", e);
			throw new RsntException("OptionServiceImpl.loadStaffData()", e);
		}
	}
	
	
	/*public Object persist(Object object) throws RsntException {
		try{
			Object obj = entityManager.persist(object);
			entityManager.flush();
			return obj;
		}
		catch(Exception e){
			log.error("OptionServiceImpl", e);
			throw new RsntException("OptionServiceImpl.persist()", e);
		}
	}*/

	public Object merge(Object object) throws RsntException {
		try{
			Object obj = entityManager.merge(object);
			entityManager.flush();
			return obj;
		}
		catch(Exception e){
			log.error("OptionServiceImpl", e);
			throw new RsntException("OptionServiceImpl.merge()", e);
		}
	}
	
	public void deleteOption(Object object) throws RsntException {
		try{
			entityManager.remove(object);
			entityManager.flush();
		}
		catch(Exception e){
			log.error("OptionServiceImpl", e);
			throw new RsntException("OptionServiceImpl.merge()", e);
		}
	}
	
	public Object find(Long orgOptionId) throws RsntException {
		try{
			return entityManager.find(OrganizationOption.class, orgOptionId);
		}
		catch(Exception e){
			log.error("OptionServiceImpl", e);
			throw new RsntException("OptionServiceImpl.find()", e);
		}
	}

	public int findDuplicateOption(String optionName,Long orgOptionId) throws RsntException {
		try{
			String additionalWhereClause = " and ot.OrganizationOptionid <> ?3";
			BigInteger cnt = null;
			StringBuffer query = new StringBuffer(NativeQueryConstants.GET_DUPLICATE_OPTION);
			if(orgOptionId!=null){
				query.append(additionalWhereClause);
				cnt= (BigInteger) entityManager.createNativeQuery(query.toString()).setParameter(1, Long.valueOf(Contexts.getSessionContext().get(Constants.CONST_ORGID).toString()))
					.setParameter(2, optionName.toLowerCase())
					.setParameter(3, orgOptionId).getSingleResult();
			}else{
				 cnt = (BigInteger) entityManager.createNativeQuery(query.toString()).setParameter(1, Long.valueOf(Contexts.getSessionContext().get(Constants.CONST_ORGID).toString()))
				.setParameter(2, optionName.toLowerCase()).getSingleResult();
			}
			return cnt.intValue();
		}
		catch(Exception e){
			log.error("OptionServiceImpl", e);
			throw new RsntException("OptionServiceImpl.find()", e);
		}
	}

	
	public EntityManager getEntityManager() {
		return entityManager;
	}


	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
}
