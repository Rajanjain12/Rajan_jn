package com.rsnt.service.impl;

import java.math.BigInteger;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

import com.rsnt.common.exception.RsntException;
import com.rsnt.entity.AdUsageHistoryReport;
import com.rsnt.entity.Organization;
import com.rsnt.entity.OrganizationAdCredit;
import com.rsnt.entity.OrganizationAdCreditScheduler;
import com.rsnt.entity.OrganizationCategory;
import com.rsnt.entity.OrganizationPlanSubscription;
import com.rsnt.entity.OrganizationUser;
import com.rsnt.service.IOrganizationService;
import com.rsnt.util.common.AppInitializer;
import com.rsnt.util.common.CommonUtil;
import com.rsnt.util.common.Constants;
import com.rsnt.util.common.NativeQueryConstants;
import com.rsnt.util.transferobject.StripeDataTO;

@Stateless(name=IOrganizationService.JNDI_NAME)
@Name(IOrganizationService.SEAM_NAME)
@Scope(ScopeType.STATELESS)
public class OrganizationService  implements IOrganizationService{

	@In
	private EntityManager entityManager;
	
	@Logger
	private Log log;
	
	public Organization createUpdateOrganization(Organization pOrg) throws RsntException{
		log.info("Inside OrgService");
		try{
			/*if(pOrg.getOrganizationId()!=null){
				//setModifiedAtttributes(pOrg);
			}
			else{
				//setCreatedAtttributes(pOrg);
			}
			for(OrganizationUser orgUser: pOrg.getOrganizationUserList() ){
				if(orgUser.getOrganizationUserId()!=null){
					//setModifiedAtttributes(orgUser);
				}
				else{
					//setCreatedAtttributes(orgUser);
				}
			}*/
			
			return entityManager.merge(pOrg);
			//entityManager.flush();
			//entityManager.refresh(pOrg);
			//return pOrg;
		}
		catch(Exception e){
			//entityManager.getTransaction().setRollbackOnly();
			throw new RsntException(e);
		}
		
	}
	

	public String updateOrganizationAndProcessStripe(Organization pOrg, StripeDataTO pDataTO) throws RsntException{
		try{
		//	entityManager.setFlushMode(FlushModeType.COMMIT);
			 //Conversation.instance().changeFlushMode(org.jboss.seam.annotations.FlushModeType.MANUAL);  
			entityManager.merge(pOrg);
			
			String chargeId = CommonUtil.processStripePaymentV2(pDataTO);
			entityManager.flush();
			 //Conversation.instance().changeFlushMode(org.jboss.seam.annotations.FlushModeType.AUTO);
			//entityManager.setFlushMode(FlushModeType.AUTO);
			return chargeId;
			
		}
		catch(Exception e){
			//entityManager.getTransaction().setRollbackOnly();
			//entityManager.getTransaction().setRollbackOnly();
			throw new RsntException(e);
		}
		
	}
	
	
	public void createOrganization(Organization pOrg) throws RsntException{
		try{
			if(pOrg.getOrganizationId()!=null){
				//setModifiedAtttributes(pOrg);
			}
			else{
				//setCreatedAtttributes(pOrg);
			}
			for(OrganizationUser orgUser: pOrg.getOrganizationUserList() ){
				if(orgUser.getOrganizationUserId()!=null){
					//setModifiedAtttributes(orgUser);
				}
				else{
					//setCreatedAtttributes(orgUser);
				}
			}
			
			entityManager.persist(pOrg);
		}
		catch(Exception e){
			throw new RsntException(e);
		}
		
	}
	
	public void createOrganizationPlanSubscription(OrganizationPlanSubscription organizationPlanSubscription) throws RsntException{
		try{
			entityManager.persist(organizationPlanSubscription);
		}
		catch(Exception e){
			throw new RsntException(e);
		}
	}
	@SuppressWarnings("unchecked")
	public List<Object[]> fetchOrganizationMobileData(Long pLayoutMarkerId) throws RsntException{
		try{
			return entityManager.createNativeQuery(NativeQueryConstants.GET_MOBILE_ORG_DATA).
				setParameter(1, pLayoutMarkerId).getResultList();
		}
		catch(Exception e){
			throw new RsntException(e);
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> fetchOrganizationPlanMobileData(Long pLayoutMarkerId) throws RsntException{
		try{
			return entityManager.createNativeQuery(NativeQueryConstants.GET_MOBILE_ORG_PLAN_DATA).
				setParameter(1, pLayoutMarkerId).getResultList();
		}
		catch(Exception e){
			throw new RsntException(e);
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public List<Integer> fetchOrganizationAdBalance(Long pLayoutMarkerId, Long orgAdAccessCharge) throws RsntException{
		try{
			return entityManager.createNativeQuery(NativeQueryConstants.GET_ORG_AD_BALANCE).
				setParameter(1, pLayoutMarkerId).getResultList();
		}
		catch(Exception e){
			throw new RsntException(e);
		}
		
	}
	
	public void updateAdAccessedTimestamp(final Long pOrgLayoutMarkerId, final Long pOrgId) throws RsntException {
		try{
			((Session) entityManager.getDelegate()).doWork(new Work() {
	            public void execute(final Connection connection) throws SQLException {
	                final CallableStatement call;
	                call = connection.prepareCall(Constants.OPEN_BRACKET + "UPDATE_ORG_BALANCE(?,?)"
	                        + Constants.CLOSE_BRACKET);
	                call.setLong(1, pOrgLayoutMarkerId);
	                call.setLong(2, pOrgId);
	                call.execute();

	              
	            }
	        });
	        entityManager.clear();
	        
		}
		catch(Exception e){
			log.error("AdsDetailServiceImpl.updateAdAccessedTimestamp()", e);
			throw new RsntException(e);
		}
        
	}
	
	
	
	public void activateUser(String pActId, int pActiveFlag) throws RsntException{
		
		try{
			entityManager.createNativeQuery(NativeQueryConstants.UPDATE_USER_ACTIVE_FLAG).setParameter(1, pActId)
						.setParameter(2, pActiveFlag)
						.executeUpdate();
		}
		catch(Exception e){
			log.error("OrganizationServiceImpl.activateUser()", e);
			throw new RsntException("OrganizationServiceImpl.activateUser()", e);
		}
		
	}
	
	public void updatePassword(String newPassword, String actId) throws RsntException {
		try{
			entityManager.createNativeQuery(NativeQueryConstants.UPDATE_USER_PASSWORD_ACTID).setParameter(1, newPassword).
				setParameter(2, actId).executeUpdate();	
		}
		catch(Exception e){
			log.error("StaffServiceImpl", e);
			throw new RsntException("StaffServiceImpl.updatePassword()", e);
		}
	}
	
	public void updateUserActivationId(String actId, String userEmailId) throws RsntException {
		try{
			entityManager.createNativeQuery(NativeQueryConstants.UPDATE_USER_ACTID).setParameter(1, actId).
				setParameter(2, userEmailId).executeUpdate();	
		}
		catch(Exception e){
			log.error("StaffServiceImpl", e);
			throw new RsntException("StaffServiceImpl.updateUserActivationId()", e);
		}
	}
	
	
	public Organization getOrganization(Long pOrgId) throws RsntException{
		try{
			return entityManager.find(Organization.class, pOrgId);
		}
		catch(Exception e){
			throw new RsntException(e);
		}
		
	}
	
	public String getUserEmailFromActId(String pActId) throws RsntException{
		try{
			return (String)entityManager.createNativeQuery(NativeQueryConstants.GET_USER_EMAIL).setParameter(1, pActId).getSingleResult();
		}
		catch(Exception e){
			throw new RsntException(e);
		}
		
	}
	
	//Vish: We now use the MD5 hashing from JAVA itself
	/*public String getHashedPasswordSql(String password) throws RsntException{
		try{
			return (String)entityManager.createNativeQuery(NativeQueryConstants.GET_HASHED_PASSWORD).setParameter(1, password).getSingleResult();
		}
		catch(Exception e){
			throw new RsntException(e);
		}
		
	}*/
	
	
	@SuppressWarnings("unchecked")
	public List<Object[]> loadOrgReportData(boolean createdOrgsFlag)  throws RsntException{
		try{
			if(createdOrgsFlag){
				return entityManager.createNativeQuery(NativeQueryConstants.GET_CREATED_ORG_DATA).
				getResultList();
			}
			else{
				return entityManager.createNativeQuery(NativeQueryConstants.GET_EXPIRING_ORG_DATA).
				getResultList();
			}
			
		}
		catch(Exception e){
			log.error("OrganizationService.loadExpOrgData()", e);
			throw new RsntException(e);
		}
		
	}
	
	
	
	public void createOrganizationAdCredit(OrganizationAdCredit pOrgAdCredit) throws RsntException{
		try{
			if(pOrgAdCredit.getOrganization()!=null){
				//setModifiedAtttributes(pOrg);
			}
			else{
				//setCreatedAtttributes(pOrg);
			}
			
			entityManager.merge(pOrgAdCredit);
		}
		catch(Exception e){
			throw new RsntException(e);
		}
		
	}
	
	public void createOrganizationAdUsageHistory(Set<AdUsageHistoryReport> adUsageHistoryReportSet) throws RsntException{
		try{
			for(AdUsageHistoryReport usage: adUsageHistoryReportSet){
				entityManager.merge(usage);
			}
			
		}
		catch(Exception e){
			throw new RsntException(e);
		}
		
	}
	
	public void createOrganizationAdScheduler(OrganizationAdCreditScheduler pOrgAdCreditScheduler) throws RsntException{
		try{
			
			
			entityManager.merge(pOrgAdCreditScheduler);
		}
		catch(Exception e){
			throw new RsntException(e);
		}
		
	}
	
	public void updateActiveOrganizationPlanSubscription(Long pOrganizationId, Long pOrgSubPlanId) throws RsntException{
		try{
			entityManager.createNativeQuery(NativeQueryConstants.UPDATE_ACTIVE_ORG_SUBS_PLAN_ID).setParameter(1, pOrganizationId).
			setParameter(2, pOrgSubPlanId).
			executeUpdate();
			
			entityManager.flush();
			entityManager.clear();
		}
		catch(Exception e){
			throw new RsntException(e);
		}
	}
	
	public Organization getOrganizationDetail (Long pOrgId) throws RsntException{
		try{
			return (Organization) entityManager.createNamedQuery(Organization.GET_ORGANIZATIONAL_DETAIL).setParameter(1,pOrgId).getSingleResult();
		}
		catch(Exception e){
			throw new RsntException(e);
		}
	
	}
	
	public Organization getOrganizationCatDetail (Long pOrgId) throws RsntException{
		try{
			return (Organization) entityManager.createNamedQuery(Organization.GET_ORGANIZATIONAL_CAT_DETAIL).setParameter(1,pOrgId).getSingleResult();
		}
		catch(Exception e){
			throw new RsntException(e);
		}
	
	}
	
	public  List<Object[]>   getPlanSchedulerData() throws RsntException{
		try{
			 return entityManager.createNativeQuery(NativeQueryConstants.GET_SCHEDULER_ORG_DATA).getResultList();
		}
		catch(Exception e){
			throw new RsntException(e);
		}
	}
	
	public  List<Object[]>   getPlanRenewReminderData() throws RsntException{
		try{
			 return entityManager.createNativeQuery(NativeQueryConstants.GET_PLAN_RENEW_REMINDER_DATA)
			 .setParameter(1, AppInitializer.renewReminderDays).getResultList();
		}
		catch(Exception e){
			throw new RsntException(e);
		}
	}
	
	public  List<Object[]>   getMonthlyReportData(Long pStartOrgId) throws RsntException{
		try{
			 return entityManager.createNativeQuery(NativeQueryConstants.GET_MONTHLY_REPORT_DATA).setParameter(1, pStartOrgId)
			.getResultList();
		}
		catch(Exception e){
			throw new RsntException(e);
		}
	}
	
	public  List<Object[]>   getMonthlyReportOrgAdBalance(Long pOrgId) throws RsntException{
		try{
			 return entityManager.createNativeQuery(NativeQueryConstants.GET_LATEST_AD_BALANCE).setParameter(1, pOrgId)
			.getResultList();
		}
		catch(Exception e){
			throw new RsntException(e);
		}
	}
	
	public List<Object[]>  getAdUsageHistoryForOrg(Long pOrgId) throws RsntException{
		try{
			return entityManager.createNativeQuery(NativeQueryConstants.GET_ORGANIZATION_ADUSAGE_HISTORY).setParameter(1, pOrgId)
			.getResultList();
		}
		catch(Exception e){
			throw new RsntException(e);
		}
	}
	
	public boolean checkAdUsageHistExists(Long pOrgId) throws RsntException{
		try{
			BigInteger test = (BigInteger)entityManager.createNativeQuery(NativeQueryConstants.CHECK_ADUSAGE_HIST_EXISTS).setParameter(1, pOrgId)
				.getSingleResult();
			if(test.intValue()!=0) return true;
		}
		catch(Exception e){
			throw new RsntException(e);
		}
		return false;
	}
	
	
	public Organization updateOrganizationCatData(Organization org, List<OrganizationCategory> catList) throws RsntException{
		try{
			//Organization orgCat = getOrganizationCatDetail(org.getOrganizationId());
			org.getOrganizationCategoryList().clear();
			for(OrganizationCategory cat : catList){
				cat.setOrganization(org);
				org.getOrganizationCategoryList().add(cat);
			}
			
			entityManager.merge(org);
			 entityManager.flush();
			//entityManager.refresh(org);
			 return org;
		}
		catch(Exception e){
			e.printStackTrace();
			throw new RsntException(e);
		}
	}
	


	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
}
