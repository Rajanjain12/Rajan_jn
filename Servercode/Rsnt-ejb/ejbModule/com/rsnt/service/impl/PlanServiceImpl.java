package com.rsnt.service.impl;

import java.math.BigDecimal;
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
import com.rsnt.entity.OrganizationPlanSubscription;
import com.rsnt.entity.OrganizationPlanSubscriptionInvoice;
import com.rsnt.entity.Plan;
import com.rsnt.entity.PlanPrice;
import com.rsnt.service.IPlanService;
import com.rsnt.util.common.Constants;
import com.rsnt.util.common.NativeQueryConstants;

@Stateless(name=IPlanService.JNDI_NAME)
@Name(IPlanService.SEAM_NAME)
@Scope(ScopeType.STATELESS)
public class PlanServiceImpl  implements IPlanService{

	@Logger
	private Log log;
	
	@In
	private EntityManager entityManager;
	
	public boolean getOrgAutoRenewOption() throws RsntException{
		try{
			return ((Boolean)entityManager.createNativeQuery(NativeQueryConstants.GET_ORG_AUTO_RENEW_OPTION).
			setParameter(1, Contexts.getSessionContext().get(Constants.CONST_ORGID).toString()).getSingleResult()).booleanValue();
			 
		}
		catch(Exception e){
			log.error("PlanServiceImpl.loadPlanData()", e);
			throw new RsntException(e);
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> loadPlanData()  throws RsntException{
		try{
			return entityManager.createNativeQuery(NativeQueryConstants.GET_PLAN_DATA).getResultList();
		}
		catch(Exception e){
			log.error("PlanServiceImpl.loadPlanData()", e);
			throw new RsntException(e);
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public List<PlanPrice> getPlanPriceFromPlan(long pPlanId) throws RsntException {
		try{
		
			return entityManager.createNamedQuery(PlanPrice.GET_PLAN_PRICE_FROM_PLAN).setParameter(1, pPlanId).getResultList();
		}
		catch(Exception e){
			log.error("PlanServiceImpl.getPlanPriceEntity()", e);
			throw new RsntException(e);
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public PlanPrice getPlanPriceEntity(long pPlanPriceId) throws RsntException {
		try{
		
			return (PlanPrice)entityManager.createNamedQuery(PlanPrice.GET_PLAN_PRICE_ENTITY).setParameter(1, pPlanPriceId).getSingleResult();
		}
		catch(Exception e){
			log.error("PlanServiceImpl.getPlanPriceEntity()", e);
			throw new RsntException(e);
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public PlanPrice getFreePlanPriceEntity(long pPlanId) throws RsntException {
		try{
		
			return (PlanPrice) entityManager.createNamedQuery(PlanPrice.GET_FREE_PLAN_PRICE_ENTITY).setParameter(1, pPlanId).getResultList().get(0);
		}
		catch(Exception e){
			log.error("PlanServiceImpl.getFreePlanPriceEntity()", e);
			throw new RsntException(e);
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public Plan getPlanEntity(long pPlanId) throws RsntException {
		try{
		
			return (Plan)entityManager.createNamedQuery(Plan.GET_PLAN_DETAIL).setParameter(1, pPlanId).getSingleResult();
		}
		catch(Exception e){
			log.error("PlanServiceImpl.getPlanEntity()", e);
			throw new RsntException(e);
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public Plan loadPlanDetail(long pPlanId) throws RsntException {
		try{
		
			return (Plan)entityManager.createNamedQuery(Plan.GET_PLAN_DETAIL).setParameter(1, pPlanId).getSingleResult();
		}
		catch(Exception e){
			log.error("PlanServiceImpl.getPlanEntity()", e);
			throw new RsntException(e);
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public OrganizationPlanSubscription loadActiveOrgPlanSubscription() throws RsntException {
		try{
		
			return (OrganizationPlanSubscription)entityManager.createNamedQuery(OrganizationPlanSubscription.GET_ACTIVE_ORGANIZATION_PLAN_SUBSCRIPTION).
					setParameter(1, Contexts.getSessionContext().get(Constants.CONST_ORGSUBSCRIPTIONPLANID)).getSingleResult();
		}
		catch(Exception e){
			log.error("PlanServiceImpl.loadActiveOrgPlanSubscription()", e);
			throw new RsntException(e);
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public OrganizationPlanSubscription getOrganizationPlanSubscription(Long pOrgPlanId) throws RsntException {
		try{
		
			return (OrganizationPlanSubscription)entityManager.createNamedQuery(OrganizationPlanSubscription.GET_ORGANIZATION_PLAN_SUBSCRIPTION).
					setParameter(1, pOrgPlanId).getSingleResult();
		}
		catch(Exception e){
			log.error("PlanServiceImpl.getOrganizationPlanSubscription()", e);
			throw new RsntException(e);
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public OrganizationPlanSubscription updateOrgPlanSubscription(OrganizationPlanSubscription pOrganizationPlanSubscription) throws RsntException {
		try{
		
			return entityManager.merge(pOrganizationPlanSubscription);
		}
		catch(Exception e){
			log.error("PlanServiceImpl.updateOrgPlanSubscription()", e);
			throw new RsntException(e);
		}
		
	}
	
	public Plan merge(Plan plan) throws RsntException {
		try{
				return entityManager.merge(plan);
		}
		catch(Exception e){
			log.error("PlanServiceImpl.merge", e);
			throw new RsntException(e);
		}
	}
	
	public void createOrgSubsInvoice(OrganizationPlanSubscriptionInvoice pOrganizationPlanSubscriptionInvoice) 
						throws RsntException {
		try{
				 entityManager.persist(pOrganizationPlanSubscriptionInvoice);
		}
		catch(Exception e){
			log.error("PlanServiceImpl.createOrgSubsInvoice", e);
			throw new RsntException(e);
		}
	}
	
	public Plan find (Long pPlanId) throws RsntException {
		try{
				return entityManager.find(Plan.class, pPlanId);
		}
		catch(Exception e){
			log.error("PlanServiceImpl.find", e);
			throw new RsntException(e);
		}
	}
	
	public List<Plan> findDuplicatePlan (String planName) throws RsntException {
		try{
				return entityManager.createNamedQuery(Plan.GET_DUPLICATE_PLAN).setParameter(1,planName).getResultList();
		}
		catch(Exception e){
			log.error("PlanServiceImpl.findDuplicatePlan", e);
			throw new RsntException(e);
		}
	}
	
	public String getFreeAdCountCreditedForPlan(Long pOrgId, Long planId) throws RsntException{
		
		try{
			BigDecimal adCount = 
			(BigDecimal) entityManager.createNativeQuery(NativeQueryConstants.GET_FREE_AD_COUNT_CREDITED_FOR_PLAN).
				setParameter(1, pOrgId)
				.setParameter(2, planId)
				.getSingleResult();
			return String.valueOf((adCount));
		}
		catch(Exception e){
			log.error("PlanServiceImpl.getFreeAdCountCreditedForPlan", e);
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
