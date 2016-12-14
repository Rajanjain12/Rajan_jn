package com.rsnt.common.service;

import java.util.List;

import javax.ejb.Local;

import com.rsnt.common.exception.RsntException;
import com.rsnt.entity.Lookup;
import com.rsnt.entity.OrganizationOption;

@Local
public interface ILookupService {
	
	public String SEAM_NAME = "lookupService";
	public String JNDI_NAME="LookupServiceImpl";
	
	public List<Object[]> getPlanLookupList() throws RsntException;
	
	public List<OrganizationOption> getDefaultOrgOptions() throws RsntException;
	
	//public List<Object[]> getPlanTypeLookupList() throws RsntException ;
	
	//public List<Object[]> getCurrencyLookupList() throws RsntException;
	
	//public List<Object[]> getPlanAllAlertsLookups() throws RsntException;
	
	public Lookup getLookupEntity(Long pLookupId)  throws RsntException;
	
	public List<Object[]> getLookupsForLookupType(final Long pLookupTypeId) throws RsntException;

}
