package com.rsnt.service.impl;

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
import com.rsnt.service.IAnalyticsService;
import com.rsnt.util.common.NativeQueryConstants;

@Stateless(name=IAnalyticsService.JNDI_NAME)
@Name(IAnalyticsService.SEAM_NAME)
@Scope(ScopeType.STATELESS)
public class AnalyticsServiceImpl  implements IAnalyticsService{
	@Logger
	private Log log;
	
	@In
	private EntityManager entityManager;

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getAlertReportData(String fromDate, String toDate)  throws RsntException{
		try{
			return entityManager.createNativeQuery(NativeQueryConstants.GET_ANALYTICKS_REPORT)
			.setParameter(1, fromDate)
			.setParameter(2, toDate).getResultList();
		}
		catch(Exception e){
			log.error("AnalyticsServiceImpl.getAlertReportData()", e);
			throw new RsntException(e);
		}
		
	}
	
	
	
}
