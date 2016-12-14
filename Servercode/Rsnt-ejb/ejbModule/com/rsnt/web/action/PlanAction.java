package com.rsnt.web.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

import com.rsnt.common.exception.RsntException;
import com.rsnt.common.service.ILookupService;
import com.rsnt.entity.OrganizationPlanSubscription;
import com.rsnt.entity.Plan;
import com.rsnt.entity.PlanFeature;
import com.rsnt.entity.PlanPrice;
import com.rsnt.entity.User;
import com.rsnt.service.IPlanService;
import com.rsnt.util.common.AppInitializer;
import com.rsnt.util.common.Constants;

@Name("planAction")
@Scope(ScopeType.CONVERSATION)
public class PlanAction {
	
	@In
    private User loginUser;
	

	@In
    private Map messages;
	
	@Logger
	private Log log;
	
	private Plan selectedPlan;
	
	private Long selectedPlanId;
	
	@In(create = true)
	private IPlanService planService;
	
	@In(value=ILookupService.SEAM_NAME,create=true)
	private ILookupService lookupService;
	
	private List<SelectItem> currencyList = new ArrayList<SelectItem>();
	
	private Long monthlyCost;
	private Long yearlyCost;
	private Long dailyCost;
	
	private Long monthlyCurrencyId;
	private Long yearlyCurrencyId;
	private Long dailyCurrencyId;
	
	/*private Long monthlyAmountPerUnit;
	private Long yearlyAmountPerUnit;
	private Long dailyAmountPerUnit;
	*/
	private BigDecimal monthlyCostPerAd;
	private BigDecimal yearlyCostPerAd;
	private BigDecimal dailyCostPerAd;
	
	private Long monthlyFreeAds;
	private Long yearlyFreeAds;
	private Long dailyFreeAds;
	
	//private boolean alertsFlag;
	
	private List<SelectItem> planAlertsList = new ArrayList<SelectItem>();
	private String planAlertData;
	

	private List<SelectItem> planFeedbackList = new ArrayList<SelectItem>();
	private Long selectedFeedbackId;

	private boolean promotionalOffers;
	private boolean specialAds;
	private boolean optionCreate;
	
	private List<SelectItem> planReportsList = new ArrayList<SelectItem>();
	private String planReportData;
	
	
	private String selectedPlanName;
	
	private boolean editMode;
	
	private boolean freePlanFlag;
	
	
	public void initializeObject() throws RsntException {
		
		
		try{
			currencyList = new ArrayList<SelectItem>();
			planAlertsList = new ArrayList<SelectItem>();
			planFeedbackList = new ArrayList<SelectItem>();
			planReportsList = new ArrayList<SelectItem>();
			
			/*List<Object[]> planTypeLookupObjArr = lookupService.getLookupsForLookupType(new Long(Constants.CONT_LOOKUPTYPE_PLANTYPE));

			for(Object[] obj : planTypeLookupObjArr ){
				BigDecimal planId = new BigDecimal(obj[0].toString());
				planTypeList.add(new SelectItem(planId.longValue(),obj[1].toString()));
			}*/
			
			
			List<Object[]> currencyLookupObjArr = lookupService.getLookupsForLookupType(new Long(Constants.CONT_LOOKUPTYPE_CURRENCY));

			for(Object[] obj : currencyLookupObjArr ){
				BigDecimal currencyId = new BigDecimal(obj[0].toString());
				currencyList.add(new SelectItem(currencyId.longValue(),obj[1].toString()));
			}
			currencyList.add(new SelectItem(0, "Select.."));
			
			this.setMonthlyCurrencyId(new Long(0));
			this.setYearlyCurrencyId(new Long(0));
			
			List<Object[]> planAllAlertsLookupObjArr = lookupService.getLookupsForLookupType(new Long(Constants.CONT_LOOKUPTYPE_PLANALLALERTTYPE));

			for(Object[] obj : planAllAlertsLookupObjArr ){
				BigDecimal planAlertLookupId = new BigDecimal(obj[0].toString());
				planAlertsList.add(new SelectItem(planAlertLookupId.longValue(),obj[1].toString()));
			}
			
			
			List<Object[]> planFeedbackLookupObjArr = lookupService.getLookupsForLookupType(new Long(Constants.CONT_LOOKUPTYPE_FEATURE_FEEDBACK));

			for(Object[] obj : planFeedbackLookupObjArr ){
				BigDecimal planFeedbackLookupId = new BigDecimal(obj[0].toString());
				planFeedbackList.add(new SelectItem(planFeedbackLookupId.longValue(),obj[1].toString()));
			}
			planFeedbackList.add(new SelectItem(0, "Select.."));
			
			List<Object[]> planReportsLookupObjArr = lookupService.getLookupsForLookupType(new Long(Constants.CONT_LOOKUPTYPE_FEATURE_REPORT));

			for(Object[] obj : planReportsLookupObjArr ){
				BigDecimal planReportLookupId = new BigDecimal(obj[0].toString());
				planReportsList.add(new SelectItem(planReportLookupId.longValue(),obj[1].toString()));
			}
			
			this.setMonthlyCost(null);
			this.setYearlyCost(null);
			this.setDailyCost(null);
			
			this.setMonthlyCurrencyId(null);
			this.setYearlyCurrencyId(null);
			this.setDailyCurrencyId(null);
			
			this.setMonthlyCostPerAd(null);
			this.setYearlyCostPerAd(null);
			this.setDailyCostPerAd(null);
			
			this.setMonthlyFreeAds(null);
			this.setYearlyFreeAds(null);
			this.setDailyFreeAds(null);
		}
		catch(RsntException e){
			log.error("initializeObject()", e);
			throw new RsntException(e);
		}
		
	}
	public String cancelEdit(){
		return "success";
	}
	
	public String addNewPlan() {
		try {
			setEditMode(true);
			selectedPlan= new Plan();
			selectedPlan.setPlanFeatureSet(new TreeSet<PlanFeature>());
			selectedPlan.setPlanPriceList(new ArrayList<PlanPrice>());
			initializeObject();
			setSelectedPlanName(null);
			//setSelectedPlanType(new Long(Constants.CONST_LOOKUP_PLANTYPE_FREE));
			setSelectedFeedbackId(new Long(0));
			setPlanAlertData("");
			setPlanReportData("");
			setFreePlanFlag(false);
			return "success";
			
		} catch (RsntException re) {
			log.error("PlanAction.addNewPlan()", re);
			final FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, messages
                    .get("add.plan.failed").toString(), null);
            FacesContext.getCurrentInstance().addMessage(null, message);
		}
		
		return null;
	}
	
	public void updatePlanAlertData(){
		log.info("updatePlanAlertData called");
		log.info(this.getPlanAlertData());
	}
	
	public void updatePlanReportData(){
		log.info("updatePlanReportData called");
		log.info(this.getPlanReportData());
	}

	
	public String fetchPlanData() {
		try {
			initializeObject();
			if(selectedPlanId != null) {
				selectedPlan = (Plan) planService.loadPlanDetail(selectedPlanId);
				this.setEditMode(false);
				if(selectedPlanId.intValue()==Integer.parseInt(AppInitializer.freePlanId))freePlanFlag= true;
				else
					freePlanFlag=false;
			}
			
			for (PlanPrice planPrice: selectedPlan.getPlanPriceList()){
				if(planPrice.getUnit().getLookupId().intValue() == Constants.CONT_LOOKUP_PLAN_UNIT_MONTH){
					this.setMonthlyCost(planPrice.getAmountPerUnit().longValue());
					this.setMonthlyCurrencyId(planPrice.getCurrency().getLookupId());
					//this.setMonthlyAmountPerUnit(planPrice.getAmountPerUnit().longValue());
					this.setMonthlyCostPerAd(planPrice.getCostPerAd());
					this.setMonthlyFreeAds(planPrice.getNoOfAdsPerUnit());
					
					
				}
				else if(planPrice.getUnit().getLookupId().intValue() == Constants.CONT_LOOKUP_PLAN_UNIT_YEAR){
					this.setYearlyCost(planPrice.getAmountPerUnit().longValue());
					this.setYearlyCurrencyId(planPrice.getCurrency().getLookupId());
					//this.setYearlyAmountPerUnit(planPrice.getAmountPerUnit().longValue());
					this.setYearlyCostPerAd(planPrice.getCostPerAd());
					this.setYearlyFreeAds(planPrice.getNoOfAdsPerUnit());
				}
				else if(planPrice.getUnit().getLookupId().intValue() == Constants.CONT_LOOKUP_PLAN_UNIT_DAY){
					this.setDailyCost(planPrice.getAmountPerUnit().longValue());
					this.setDailyCurrencyId(planPrice.getCurrency().getLookupId());
					//this.setDailyAmountPerUnit(planPrice.getAmountPerUnit().longValue());
					this.setDailyCostPerAd(planPrice.getCostPerAd());
					this.setDailyFreeAds(planPrice.getNoOfAdsPerUnit());
				}
			}
			
			setPlanAlertData("");
			setPlanReportData("");
			this.setPromotionalOffers(false);
			this.setSpecialAds(false);
			this.setOptionCreate(false);
			
			for(PlanFeature feature : selectedPlan.getPlanFeatureSet()){
			
				if(feature.getFeatureId().intValue() == Constants.CONT_LOOKUPTYPE_FEATURE_ALERT){
					this.setPlanAlertData(this.getPlanAlertData()+feature.getChildFeatureId() +",");
					
				}
				else if(feature.getFeatureId().intValue() == Constants.CONT_LOOKUPTYPE_FEATURE_REPORT){
					this.setPlanReportData(this.getPlanReportData()+feature.getChildFeatureId()+",");
				}
				
				else if(feature.getFeatureId().intValue() == Constants.CONT_LOOKUPTYPE_FEATURE_FEEDBACK){
					this.setSelectedFeedbackId(feature.getChildFeatureId());
				}
				else if(feature.getFeatureId().intValue() == Constants.CONT_LOOKUPTYPE_FEATURE_PROMOTIONALOFFERS){
					this.setPromotionalOffers(true);
				}
				else if(feature.getFeatureId().intValue() == Constants.CONT_LOOKUPTYPE_FEATURE_SPECIALADS){
					this.setSpecialAds(true);
				}	
				else if(feature.getFeatureId().intValue() == Constants.CONT_LOOKUPTYPE_FEATURE_OPTIONCREATE){
					this.setOptionCreate(true);
				}	
			}
			
			this.setSelectedPlanName(selectedPlan.getPlanName());
			//this.setSelectedPlanType(selectedPlan.getPlanType());
			return "success";
			
		} catch (RsntException re) {
			log.error("PlanAction.fetchPlanData()", re);
			final FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, messages
                    .get("fetch.plan.failed").toString(), null);
            FacesContext.getCurrentInstance().addMessage(null, message);
		}
		return null;
	}
	
	//For Edit Save clear the list and add all.
	public String savePlan() throws RsntException{
		try 
		{
			
			if(this.getSelectedPlanName()!=null && this.getSelectedPlan().getPlanId()==null ){
				List<Plan> planDupList = planService.findDuplicatePlan(this.getSelectedPlanName().toLowerCase());
				if(planDupList.size()>0){
					//FacesMessages.instance().add("User Id already exists.Please choose a different User Id.");	
					final FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, messages
		                    .get("plan.name.duplicate").toString(), null);
		            FacesContext.getCurrentInstance().addMessage(null, message);
					return null;
				}
			}
			//final Long orgId = (Long) Contexts.getSessionContext().get(Constants.CONST_ORGID);
			selectedPlan.getPlanPriceList().clear();	
			
			//if(this.getSelectedPlanType().intValue() == Constants.CONST_LOOKUP_PLANTYPE_PAID){
				if(this.getMonthlyCost()!=null && this.getMonthlyCurrencyId().intValue()!=0){
					PlanPrice planPrice = new PlanPrice();
					planPrice.setPlan(selectedPlan);
					planPrice.setUnit(lookupService.getLookupEntity(new Long(Constants.CONT_LOOKUP_PLAN_UNIT_MONTH)));
					planPrice.setAmountPerUnit(new BigDecimal(this.getMonthlyCost()));
					planPrice.setCurrency(lookupService.getLookupEntity(this.getMonthlyCurrencyId()));
					//planPrice.setAmountPerUnit(new BigDecimal(this.getMonthlyAmountPerUnit()));
					planPrice.setCostPerAd(this.getMonthlyCostPerAd());
					planPrice.setNoOfAdsPerUnit(this.getMonthlyFreeAds());
					planPrice.setCreatedBy(loginUser.getUserName());
					
					selectedPlan.getPlanPriceList().add(planPrice);
				}
				
				if(this.getYearlyCost()!=null && this.getYearlyCurrencyId().intValue()!=0){
					PlanPrice planPrice = new PlanPrice();
					planPrice.setPlan(selectedPlan);
					planPrice.setUnit(lookupService.getLookupEntity(new Long(Constants.CONT_LOOKUP_PLAN_UNIT_YEAR)));
					planPrice.setCurrency(lookupService.getLookupEntity(this.getYearlyCurrencyId()));
					planPrice.setAmountPerUnit(new BigDecimal(this.getYearlyCost()));
					//planPrice.setAmountPerUnit(new BigDecimal(this.getYearlyAmountPerUnit()));
					planPrice.setCostPerAd(this.getYearlyCostPerAd());
					planPrice.setNoOfAdsPerUnit(this.getYearlyFreeAds());
					planPrice.setCreatedBy(loginUser.getUserName());
					
					selectedPlan.getPlanPriceList().add(planPrice);
				}
				
				if(this.getDailyCost()!=null && this.getDailyCurrencyId().intValue()!=0){
					PlanPrice planPrice = new PlanPrice();
					planPrice.setPlan(selectedPlan);
					planPrice.setUnit(lookupService.getLookupEntity(new Long(Constants.CONT_LOOKUP_PLAN_UNIT_DAY)));
					planPrice.setCurrency(lookupService.getLookupEntity(this.getDailyCurrencyId()));
					planPrice.setAmountPerUnit(new BigDecimal(this.getDailyCost()));
					//planPrice.setAmountPerUnit(new BigDecimal(this.getDailyAmountPerUnit()));
					planPrice.setCostPerAd(this.getDailyCostPerAd());
					planPrice.setNoOfAdsPerUnit(this.getDailyFreeAds());
					planPrice.setCreatedBy(loginUser.getUserName());
					
					selectedPlan.getPlanPriceList().add(planPrice);
				}
				
			//}
			/*else{
				//For Free Plan putting a free entry in PlanPrice.
				PlanPrice planPrice = new PlanPrice();
				planPrice.setPlan(selectedPlan);
				planPrice.setUnit(lookupService.getLookupEntity(new Long(Constants.CONT_LOOKUP_PLAN_UNIT_MONTH)));
				planPrice.setAmountPerUnit(new BigDecimal(0));
				planPrice.setCurrency(lookupService.getLookupEntity(new Long(Constants.CONT_LOOKUP_CURRENCY_USD)));
				planPrice.setAmountPerUnit(new BigDecimal(0));
				planPrice.setCostPerAd(new BigDecimal(0));
				planPrice.setNoOfAdsPerUnit(new Long(0));
				planPrice.setCreatedBy(loginUser.getUserName());
				
				selectedPlan.getPlanPriceList().add(planPrice);
			}*/
				
			
			if(selectedPlan.getPlanId()!=null){
				selectedPlan.setModifiedBy(loginUser.getUserName());
			}
			else{
				selectedPlan.setCreatedBy(loginUser.getUserName());
			}
			
			selectedPlan.setPlanName(this.getSelectedPlanName());
			//selectedPlan.setPlanType(this.getSelectedPlanType());			
			selectedPlan.getPlanFeatureSet().clear();
			
			
			if(this.getPlanAlertData()!=null && !this.getPlanAlertData().equalsIgnoreCase("")){
				String[] arr = this.getPlanAlertData().split(",");
				for(String arrVar : arr ){
					PlanFeature planFeature = new PlanFeature();
					planFeature.setPlan(selectedPlan);
					planFeature.setChildFeatureId(new Long(arrVar));
					planFeature.setFeatureId(new Long(Constants.CONT_LOOKUPTYPE_FEATURE_ALERT));
					planFeature.setCreatedBy(loginUser.getUserName());
					selectedPlan.getPlanFeatureSet().add(planFeature);
				}
				
			}
			
			if(this.getPlanReportData()!=null && !this.getPlanReportData().equalsIgnoreCase("")){
				String[] arr = this.getPlanReportData().split(",");
				for(String arrVar : arr ){
					PlanFeature planFeature = new PlanFeature();
					planFeature.setPlan(selectedPlan);
					planFeature.setChildFeatureId(new Long(arrVar)); //For Reports this is the report Id
					planFeature.setFeatureId(new Long(Constants.CONT_LOOKUPTYPE_FEATURE_REPORT));
					planFeature.setCreatedBy(loginUser.getUserName());
					selectedPlan.getPlanFeatureSet().add(planFeature);
				}
			}
			
			if(this.getSelectedFeedbackId()!=null && this.getSelectedFeedbackId().intValue()!=0 ){
				PlanFeature planFeature = new PlanFeature();
				planFeature.setPlan(selectedPlan);
				planFeature.setChildFeatureId(new Long(this.getSelectedFeedbackId())); 
				planFeature.setFeatureId(new Long(Constants.CONT_LOOKUPTYPE_FEATURE_FEEDBACK));
				planFeature.setCreatedBy(loginUser.getUserName());
				selectedPlan.getPlanFeatureSet().add(planFeature);
			}
			if(this.isPromotionalOffers()) {
				PlanFeature planFeature = new PlanFeature();
				planFeature.setPlan(selectedPlan);
				//planFeature.setChildFeatureId(new Long(arrVar)); 
				planFeature.setFeatureId(new Long(Constants.CONT_LOOKUPTYPE_FEATURE_PROMOTIONALOFFERS));
				planFeature.setCreatedBy(loginUser.getUserName());
				selectedPlan.getPlanFeatureSet().add(planFeature);
			}
			
			if(this.isSpecialAds()) {
				PlanFeature planFeature = new PlanFeature();
				planFeature.setPlan(selectedPlan);
				//planFeature.setChildFeatureId(new Long(arrVar)); 
				planFeature.setFeatureId(new Long(Constants.CONT_LOOKUPTYPE_FEATURE_SPECIALADS));
				planFeature.setCreatedBy(loginUser.getUserName());
				selectedPlan.getPlanFeatureSet().add(planFeature);
			}
			
			if(this.isOptionCreate()) {
				PlanFeature planFeature = new PlanFeature();
				planFeature.setPlan(selectedPlan);
				//planFeature.setChildFeatureId(new Long(arrVar)); 
				planFeature.setFeatureId(new Long(Constants.CONT_LOOKUPTYPE_FEATURE_OPTIONCREATE));
				planFeature.setCreatedBy(loginUser.getUserName());
				selectedPlan.getPlanFeatureSet().add(planFeature);
			}
			
			selectedPlan= (Plan)planService.merge(selectedPlan);
			
		} catch (RsntException re) {
			log.error("PlanAction.updatePlanData()", re);
			
			final FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, messages
                    .get("save.plan.failed").toString(), null);
            FacesContext.getCurrentInstance().addMessage(null, message);
            throw new RsntException(re);
		}
		return "success";
	}
	
	
	
	public User getLoginUser() {
		return loginUser;
	}

	public void setLoginUser(User loginUser) {
		this.loginUser = loginUser;
	}

	public Plan getSelectedPlan() {
		return selectedPlan;
	}

	public void setSelectedPlan(Plan selectedPlan) {
		this.selectedPlan = selectedPlan;
	}

	public Long getSelectedPlanId() {
		return selectedPlanId;
	}

	public void setSelectedPlanId(Long selectedPlanId) {
		this.selectedPlanId = selectedPlanId;
	}

	public IPlanService getPlanService() {
		return planService;
	}

	public void setPlanService(IPlanService planService) {
		this.planService = planService;
	}


	public ILookupService getLookupService() {
		return lookupService;
	}

	public void setLookupService(ILookupService lookupService) {
		this.lookupService = lookupService;
	}

	public List<SelectItem> getCurrencyList() {
		return currencyList;
	}

	public void setCurrencyList(List<SelectItem> currencyList) {
		this.currencyList = currencyList;
	}

	public Long getMonthlyCurrencyId() {
		return monthlyCurrencyId;
	}

	public void setMonthlyCurrencyId(Long monthlyCurrencyId) {
		this.monthlyCurrencyId = monthlyCurrencyId;
	}

	public Long getYearlyCurrencyId() {
		return yearlyCurrencyId;
	}

	public void setYearlyCurrencyId(Long yearlyCurrencyId) {
		this.yearlyCurrencyId = yearlyCurrencyId;
	}

	public Long getMonthlyCost() {
		return monthlyCost;
	}

	public void setMonthlyCost(Long monthlyCost) {
		this.monthlyCost = monthlyCost;
	}

	public Long getYearlyCost() {
		return yearlyCost;
	}

	public void setYearlyCost(Long yearlyCost) {
		this.yearlyCost = yearlyCost;
	}


	public List<SelectItem> getPlanAlertsList() {
		return planAlertsList;
	}

	public void setPlanAlertsList(List<SelectItem> planAlertsList) {
		this.planAlertsList = planAlertsList;
	}


	public String getPlanAlertData() {
		return planAlertData;
	}

	public void setPlanAlertData(String planAlertData) {
		this.planAlertData = planAlertData;
	}

	public List<SelectItem> getPlanFeedbackList() {
		return planFeedbackList;
	}

	public void setPlanFeedbackList(List<SelectItem> planFeedbackList) {
		this.planFeedbackList = planFeedbackList;
	}

	public Long getSelectedFeedbackId() {
		return selectedFeedbackId;
	}

	public void setSelectedFeedbackId(Long selectedFeedbackId) {
		this.selectedFeedbackId = selectedFeedbackId;
	}

	
	public List<SelectItem> getPlanReportsList() {
		return planReportsList;
	}

	public void setPlanReportsList(List<SelectItem> planReportsList) {
		this.planReportsList = planReportsList;
	}

	public String getPlanReportData() {
		return planReportData;
	}

	public void setPlanReportData(String planReportData) {
		this.planReportData = planReportData;
	}

	public boolean isPromotionalOffers() {
		return promotionalOffers;
	}

	public void setPromotionalOffers(boolean promotionalOffers) {
		this.promotionalOffers = promotionalOffers;
	}

	public boolean isSpecialAds() {
		return specialAds;
	}

	public void setSpecialAds(boolean specialAds) {
		this.specialAds = specialAds;
	}

	public boolean isOptionCreate() {
		return optionCreate;
	}

	public void setOptionCreate(boolean optionCreate) {
		this.optionCreate = optionCreate;
	}

	public String getSelectedPlanName() {
		return selectedPlanName;
	}

	public void setSelectedPlanName(String selectedPlanName) {
		this.selectedPlanName = selectedPlanName;
	}
	public Long getDailyCost() {
		return dailyCost;
	}
	public void setDailyCost(Long dailyCost) {
		this.dailyCost = dailyCost;
	}
	public Long getDailyCurrencyId() {
		return dailyCurrencyId;
	}
	public void setDailyCurrencyId(Long dailyCurrencyId) {
		this.dailyCurrencyId = dailyCurrencyId;
	}
	
	public boolean isEditMode() {
		return editMode;
	}
	public void setEditMode(boolean editMode) {
		this.editMode = editMode;
	}
	
	public BigDecimal getMonthlyCostPerAd() {
		return monthlyCostPerAd;
	}
	public void setMonthlyCostPerAd(BigDecimal monthlyCostPerAd) {
		this.monthlyCostPerAd = monthlyCostPerAd;
	}
	public Long getMonthlyFreeAds() {
		return monthlyFreeAds;
	}
	public void setMonthlyFreeAds(Long monthlyFreeAds) {
		this.monthlyFreeAds = monthlyFreeAds;
	}
	public BigDecimal getYearlyCostPerAd() {
		return yearlyCostPerAd;
	}
	public void setYearlyCostPerAd(BigDecimal yearlyCostPerAd) {
		this.yearlyCostPerAd = yearlyCostPerAd;
	}
	public Long getYearlyFreeAds() {
		return yearlyFreeAds;
	}
	public void setYearlyFreeAds(Long yearlyFreeAds) {
		this.yearlyFreeAds = yearlyFreeAds;
	}
	public BigDecimal getDailyCostPerAd() {
		return dailyCostPerAd;
	}
	public void setDailyCostPerAd(BigDecimal dailyCostPerAd) {
		this.dailyCostPerAd = dailyCostPerAd;
	}
	public Long getDailyFreeAds() {
		return dailyFreeAds;
	}
	public void setDailyFreeAds(Long dailyFreeAds) {
		this.dailyFreeAds = dailyFreeAds;
	}
	public boolean isFreePlanFlag() {
		return freePlanFlag;
	}
	public void setFreePlanFlag(boolean freePlanFlag) {
		this.freePlanFlag = freePlanFlag;
	}
	



}
