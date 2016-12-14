package com.rsnt.service.impl;

import java.util.List;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

import com.rsnt.service.IWaitListScheduler;
import com.rsnt.service.IWaitListService;
import com.rsnt.util.common.NativeQueryConstants;

@Stateless(name=IWaitListScheduler.JNDI_NAME)
@Name(IWaitListScheduler.SEAM_NAME)
@Scope(ScopeType.STATELESS)
@Remote(IWaitListScheduler.class)
public class WaitlistSchedularImpl {
	@Logger
	private Log log;
	@In
	private EntityManager entityManager;
	@In(value=IWaitListService.SEAM_NAME, create=true)
	private IWaitListService waitListService;
	
	public void runRemoteScheduler(){
		log.info("Remote method invoked for waitlist");
		//WaitListServiceImpl wait = new WaitListServiceImpl();
		try{
			List<Integer> list = entityManager.createNativeQuery(NativeQueryConstants.GET_ALL_ORG).getResultList();
			log.info(list.size());
			//wait.resetOrganizationsByOrgid(new Long(40));
			for(Integer obj :list){
				log.info(Long.parseLong(obj.toString()));
				int i = waitListService.resetOrganizationsByOrgid(Long.parseLong(obj.toString()));
				//Thread.sleep(1000);
				
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
}
