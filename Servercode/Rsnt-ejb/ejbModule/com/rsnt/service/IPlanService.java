package com.rsnt.service;

import java.util.List;

import javax.ejb.Local;

import com.rsnt.common.exception.RsntException;
import com.rsnt.entity.OrganizationPlanSubscription;
import com.rsnt.entity.OrganizationPlanSubscriptionInvoice;
import com.rsnt.entity.Plan;
import com.rsnt.entity.PlanPrice;

@Local
public interface IPlanService {
	
	public String SEAM_NAME = "planService";
	public String JNDI_NAME="PlanServiceImpl";
	
	
	public  List<PlanPrice> getPlanPriceFromPlan(final long pPlanId) throws RsntException;
	public PlanPrice getPlanPriceEntity(long pPlanPriceId) throws RsntException ;
	public Plan getPlanEntity(long pPlanId) throws RsntException ;

	public List<Object[]> loadPlanData()  throws RsntException;
	public Plan merge(Plan plan) throws RsntException;
	public Plan find(Long pPlanId) throws RsntException;
	public Plan loadPlanDetail(long pPlanId) throws RsntException;
	public List<Plan> findDuplicatePlan (String planName) throws RsntException;
	public OrganizationPlanSubscription loadActiveOrgPlanSubscription() throws RsntException;
	public OrganizationPlanSubscription updateOrgPlanSubscription(OrganizationPlanSubscription pOrganizationPlanSubscription) throws RsntException;
	public void createOrgSubsInvoice(OrganizationPlanSubscriptionInvoice pOrganizationPlanSubscriptionInvoice) 	throws RsntException;
	public String getFreeAdCountCreditedForPlan(Long pOrgId, Long planId) throws RsntException;
	public OrganizationPlanSubscription getOrganizationPlanSubscription(Long pOrgPlanId) throws RsntException;
	public PlanPrice getFreePlanPriceEntity(long pPlanId) throws RsntException;
	public boolean getOrgAutoRenewOption() throws RsntException;
}
