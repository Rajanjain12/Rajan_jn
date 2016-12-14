package com.rsnt.common.service.impl;

import java.util.ArrayList;
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
import com.rsnt.common.service.ILookupService;
import com.rsnt.entity.Lookup;
import com.rsnt.entity.OrganizationOption;
import com.rsnt.util.common.NativeQueryConstants;

@Stateless(name=ILookupService.JNDI_NAME)
@Scope(ScopeType.STATELESS)
@Name(ILookupService.SEAM_NAME)
public class LookupService implements ILookupService{

	@In
	private EntityManager entityManager;
	
	@Logger
	private Log log;
	
	public List<Object[]> getPlanLookupList() throws RsntException {
		try{
			
			return entityManager.createNativeQuery(NativeQueryConstants.GET_PLAN_LOOKUPS).getResultList();
		}catch(Exception e){
			log.error("LookupService.getPlanLookupList()" + e);
			throw new RsntException(e);
		}
		
	}
	
	public Lookup getLookupEntity(Long pLookupId)  throws RsntException{
		try{
					
			return entityManager.find(Lookup.class, pLookupId);
		}catch(Exception e){
			log.error("LookupService.getLookupEntity()" + e);
			throw new RsntException(e);
		}
	}
	
	/*public List<Object[]> getPlanAllAlertsLookups() throws RsntException {
		try{
			
			return entityManager.createNativeQuery(NativeQueryConstants.GET_PLAN_ALL_ALERT_LOOKUPS).getResultList();
		}catch(Exception e){
			log.error("LookupService.getPlanAllAlertsLookups()" + e);
			throw new RsntException(e);
		}
		
	}

	public List<Object[]> getCurrencyLookupList() throws RsntException {
		try{
			
			return entityManager.createNativeQuery(NativeQueryConstants.GET_CURRENCY_LOOKUPS).getResultList();
		}catch(Exception e){
			log.error("LookupService.getCurrencyLookupList()" + e);
			throw new RsntException(e);
		}
		
	}
	
	public List<Object[]> getPlanTypeLookupList() throws RsntException {
		try{
			
			return entityManager.createNativeQuery(NativeQueryConstants.GET_PLAN_TYPE_LOOKUPS).getResultList();
		}catch(Exception e){
			log.error("LookupService.getPlanTypeLookupList()" + e);
			throw new RsntException(e);
		}
		
	}*/
	
	public List<Object[]> getLookupsForLookupType(final Long pLookupTypeId) throws RsntException {
		try{
			return entityManager.createNativeQuery(NativeQueryConstants.GET_LOOKUPS_FOR_LOOKUPTYPE).setParameter(1, pLookupTypeId).getResultList();
		}catch(Exception e){
			log.error("LookupService.getPlanTypeLookupList()" + e);
			throw new RsntException(e);
		}
		
	}
	
	
	public List<OrganizationOption> getDefaultOrgOptions() throws RsntException {
		try{
			
			List<OrganizationOption> organizationOptionList = new ArrayList<OrganizationOption>();
			
			List<String> defaultOrgOptionLookups =  entityManager.createNativeQuery(NativeQueryConstants.GET_DEFAULT_ORG_OPTION_LOOKUPS).getResultList();
			for(String obj : defaultOrgOptionLookups){
				OrganizationOption option = new OrganizationOption();
				option.setOptionDescription(obj.toString());
				organizationOptionList.add(option);
			}
			
			return organizationOptionList;
		}catch(Exception e){
			log.error("LookupService.getDefaultOrgOptions()" + e);
			throw new RsntException(e);
		}
		
	}

}
