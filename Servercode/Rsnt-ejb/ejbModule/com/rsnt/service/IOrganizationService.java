package com.rsnt.service;

import java.util.List;
import java.util.Set;

import javax.ejb.Local;

import com.rsnt.common.exception.RsntException;
import com.rsnt.entity.AdUsageHistoryReport;
import com.rsnt.entity.Organization;
import com.rsnt.entity.OrganizationAdCredit;
import com.rsnt.entity.OrganizationAdCreditScheduler;
import com.rsnt.entity.OrganizationCategory;
import com.rsnt.entity.OrganizationPlanSubscription;
import com.rsnt.util.transferobject.StripeDataTO;

@Local
public interface IOrganizationService {

	public String SEAM_NAME = "organizationService";
	public String JNDI_NAME="OrganizationServiceImpl";
	
	public Organization createUpdateOrganization(Organization pOrg) throws RsntException;
	
	public Organization getOrganization(Long pOrgId) throws RsntException;
	
	public List<Object[]> fetchOrganizationMobileData(Long pLayoutMarkerId) throws RsntException;
	
	public List<Object[]> fetchOrganizationPlanMobileData(Long pLayoutMarkerId) throws RsntException;
	
	public void createOrganizationAdCredit(OrganizationAdCredit pOrgAdCredit) throws RsntException;
	
	public List<Integer> fetchOrganizationAdBalance(Long pLayoutMarkerId, Long adAccessCharge) throws RsntException;
	
	//public void deductOrgAdBalance(Long pOrgId) throws RsntException;
	public void updateActiveOrganizationPlanSubscription(Long pOrganizationId, Long pOrgSubPlanId) throws RsntException;
	public Organization getOrganizationDetail (Long pOrgId) throws RsntException;
	public void createOrganization(Organization pOrg) throws RsntException;
	public void createOrganizationPlanSubscription(OrganizationPlanSubscription organizationPlanSubscription) throws RsntException;
	public  List<Object[]>   getPlanSchedulerData() throws RsntException;
	public void createOrganizationAdScheduler(OrganizationAdCreditScheduler pOrgAdCreditScheduler) throws RsntException;
	public void updateAdAccessedTimestamp(final Long pOrgLayoutMarkerId, final Long pOrgId) throws RsntException;
	public String getUserEmailFromActId(String pActId) throws RsntException;
	public void activateUser(String pActId, int pActiveFlag) throws RsntException;
	public void updatePassword(String newPassword, String actId) throws RsntException;
	public void updateUserActivationId(String actId, String userEmailId) throws RsntException;
	public  List<Object[]>   getPlanRenewReminderData() throws RsntException;
	public  List<Object[]>   getMonthlyReportData(Long pStartOrgId) throws RsntException;
	public Organization getOrganizationCatDetail (Long pOrgId) throws RsntException;
	public Organization updateOrganizationCatData(Organization org, List<OrganizationCategory> catList) throws RsntException;
	public String updateOrganizationAndProcessStripe(Organization pOrg, StripeDataTO pDataTO) throws RsntException;
	public List<Object[]> loadOrgReportData(boolean createdOrgsFlag)  throws RsntException;
	public void createOrganizationAdUsageHistory(Set<AdUsageHistoryReport> adUsageHistoryReportSet) throws RsntException;
	public  List<Object[]>   getMonthlyReportOrgAdBalance(Long pOrgId) throws RsntException;
	public List<Object[]>  getAdUsageHistoryForOrg(Long pOrgId) throws RsntException;
	public boolean checkAdUsageHistExists(Long pOrgId) throws RsntException;
	
}
