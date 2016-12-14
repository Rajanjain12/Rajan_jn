package com.rsnt.web.action;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.TransactionPropagationType;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.log.Log;

import com.rsnt.common.exception.RsntException;
import com.rsnt.common.service.ILookupService;
import com.rsnt.entity.AdUsageHistoryReport;
import com.rsnt.entity.Lookup;
import com.rsnt.entity.Organization;
import com.rsnt.entity.OrganizationAdCredit;
import com.rsnt.entity.OrganizationAdCreditScheduler;
import com.rsnt.entity.OrganizationPlanSubscription;
import com.rsnt.entity.OrganizationPlanSubscriptionInvoice;
import com.rsnt.entity.Plan;
import com.rsnt.entity.PlanFeature;
import com.rsnt.entity.PlanPrice;
import com.rsnt.entity.User;
import com.rsnt.service.IOrganizationService;
import com.rsnt.service.IPlanService;
import com.rsnt.util.common.AppInitializer;
import com.rsnt.util.common.CommonUtil;
import com.rsnt.util.common.Constants;
import com.rsnt.util.common.EmailUtil;
import com.rsnt.util.transferobject.StripeDataTO;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Customer;

@Name("upgradePlanAction")
@Scope(ScopeType.PAGE)
@Transactional(TransactionPropagationType.REQUIRED)
public class UpgradePlanAction {

	@In
    private Map messages;
	
	@Logger
	private Log log;
	
	@In
    private User loginUser;
	
	@In(create = true)
	private IPlanService planService;
	
	@In(value=ILookupService.SEAM_NAME,create=true)
	private ILookupService lookupService;
	
	@In(value=IOrganizationService.SEAM_NAME, create=true)
	private IOrganizationService organizationService;
	
	private Long selectedPlanId;
	private List<SelectItem> planList = new ArrayList<SelectItem>();
	
	private String selectedPlanPriceId;
	private List<SelectItem> planPriceList = new ArrayList<SelectItem>();
	
	
	private OrganizationPlanSubscription selectedOrgPlanSub;
	private String costPerUnitData;
	private String unitsCountData;
	private boolean upgradePlanEdit;
	private boolean buyMoreAds;
	private boolean cancelSubscription;
	private boolean renewSubscription;
	
	
	private String selectedPlanCost;
	private BigDecimal unitsCount;
	private BigDecimal totalUnitsCost;
	
	private PlanPrice selectedPlanPrice;
	
	private BigDecimal currentPlanAmount;
	
	private BigDecimal oldPlanOnlyRefundAmount;
	private BigDecimal adOnlyAdjustmentCost;
	
	private BigDecimal totalOldPlanRefund;
	
	private BigDecimal oldPlanProratedAmount;
	
	private Plan selectedPlan;
	
	private Long numberOfBuyAds;
	private BigDecimal totalBuyAdsCost;
	
	private List<SelectItem> cardProcessorList = new ArrayList<SelectItem>();
	
	private String cardNumber;
	private String expiryDate;
	private String cvv;
	private String selectedCardProcessorId;
	
	private boolean planPaymentSucessFlag;
	
	private Date renewStartDate;
	private Date renewEndDate;
	
	private boolean useDifferentCard;
	
	private Long freePlanId;
	private boolean autoRenew;
	
	public boolean isAutoRenew() {
		return autoRenew;
	}

	public void setAutoRenew(boolean autoRenew) {
		this.autoRenew = autoRenew;
	}

	public void loadUpgradePlanView(){
		try{
			setUpgradePlanEdit(false);
			setBuyMoreAds(false);
			selectedOrgPlanSub = planService.loadActiveOrgPlanSubscription();
			Lookup currencyLookUp = lookupService.getLookupEntity(selectedOrgPlanSub.getCurrencyId());
			setCostPerUnitData(selectedOrgPlanSub.getAmountPerUnit()+" "+ currencyLookUp.getCode());
			
			Lookup unitTypeLookup = lookupService.getLookupEntity(selectedOrgPlanSub.getUnitId());
			setUnitsCountData(selectedOrgPlanSub.getNumberOfUnits()+" " + unitTypeLookup.getCode());
			
		}
		catch(RsntException e){
			log.error("PlanAction.loadChangePlan()", e);
			
			final FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, messages
                    .get("save.plan.failed").toString(), null);
            FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}
	
	public void loadUpgradePlanEdit(){
		loadUpgradePlanView();
		try{
			planList = new ArrayList<SelectItem>();
			
			List<Object[]> planLookupObjArr = lookupService.getPlanLookupList();

			for(Object[] obj : planLookupObjArr ){
				BigDecimal planId = new BigDecimal(obj[0].toString());
				planList.add(new SelectItem(planId.longValue(),obj[1].toString()));
			}
			planList.add(new SelectItem(0,"Select.."));
			
			this.setSelectedPlanId(new Long(0));
			
			setUpgradePlanEdit(true);
			setCancelSubscription(false);
			setBuyMoreAds(false);
		}
		catch(RsntException e){
			
		}
		
	}
	
	public void dummyMethod(){
		
	}
	
	public void buyMoreAds(){
		try{
			selectedOrgPlanSub = planService.loadActiveOrgPlanSubscription();
			setBuyMoreAds(true);
			setCancelSubscription(false);
			setUpgradePlanEdit(false);
		}
		catch(RsntException e){
			
		}
	}
	
	public String confirmBuyMoreAds(){
		try {
				cardProcessorList = new ArrayList<SelectItem>();
				
				List<Object[]> cardLookupObjArr = lookupService.getLookupsForLookupType(new Long(Constants.CONT_LOOKUPTYPE_CARDTYPE));
	
				for(Object[] obj : cardLookupObjArr ){
					BigDecimal cardId = new BigDecimal(obj[0].toString());
					cardProcessorList.add(new SelectItem(cardId.longValue(),obj[1].toString()));
				}
				this.setSelectedCardProcessorId(String.valueOf(Constants.CONT_LOOKUP_CARDTYPE_VISA));
				loadCustomerCardData();
				if(this.getCardNumber()!=null)setUseDifferentCard(false);
				else setUseDifferentCard(true);
				
				
			return "success";
		} catch (RsntException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	private void loadCustomerCardData() throws RsntException{
		try{
			selectedOrgPlanSub = planService.loadActiveOrgPlanSubscription();
			if(selectedOrgPlanSub.getOrganization().getStripeCustomerId()!=null){
				Customer retrievedCustomer = CommonUtil.retrieveCustomer(selectedOrgPlanSub.getOrganization().getStripeCustomerId());
				if(planService.getOrgAutoRenewOption()){
					this.setCardNumber("************"+retrievedCustomer.getActiveCard().getLast4());
					this.setExpiryDate(retrievedCustomer.getActiveCard().getExpMonth()+ "-"+retrievedCustomer.getActiveCard().getExpYear());
					this.setCvv("***");
					setAutoRenew(true);
				}else{
					this.setCardNumber(null);
					this.setExpiryDate(null);
					this.setCvv(null);
					setAutoRenew(false);
				}
			}
		}
		catch(Exception e){
			throw new RsntException("Unable to load customer card Details" +e.getMessage());
		}
		
	}
	
	public String confirmBuyMoreAdsPage2() throws RsntException{
		try {
			confirmBuyMoreAdsServer();
			return "success";
		} catch (RsntException e) {
			e.printStackTrace();
			final FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Your Purchase Ads Transation failed: "+e.getMessage(), null);
            FacesContext.getCurrentInstance().addMessage(null, message);
            
			if(planPaymentSucessFlag){//TODO: Mail all the details so that manually do the transaction
				EmailUtil emailUtil = (EmailUtil) Component.getInstance(EmailUtil.class);
				emailUtil.prepareMessageAndSend(this.getSelectedOrgPlanSub().getOrganization().getOrganizationId().toString(),"Buy Ads Failed and payment transaction status: "
							+this.isPlanPaymentSucessFlag(), "Buy Ads Failes");
			}
			
		}
		return null;
	}
	
	/*private void confirmBuyMoreAdsServer() throws RsntException{
		if(this.getTotalBuyAdsCost().compareTo(new BigDecimal(0))==1){
			setPlanPaymentSucessFlag(false);
			
			Organization org = organizationService.getOrganizationDetail(new Long(Contexts.getSessionContext().get(Constants.CONST_ORGID).toString()));
			
			String chargeid = null;;
			try {
				if(this.useDifferentCard){
					   String[] expDate = this.getExpiryDate().split("-");
					chargeid = CommonUtil.processStripePayment(this.getTotalBuyAdsCost(), this.getCardNumber(),expDate[0],expDate[1],this.getCvv());
				}
				else{
					chargeid = CommonUtil.processStripePayment(this.getTotalBuyAdsCost(), org.getStripeCustomerId());
				}
				
			} catch (StripeException e) {
				e.printStackTrace();
				throw new RsntException(e);
			}
			//if(chargeid!=null){
				//setPlanPaymentSucessFlag(true);
				BigDecimal originalAds = org.getAdsBalance();
				
				OrganizationAdCredit organizationAdCredit = new OrganizationAdCredit();
				organizationAdCredit.setOrganization(org);
				organizationAdCredit.setCostPerAd(org.getActiveOrgPlanSubscription().getCostPerAd());
				organizationAdCredit.setCurrencyId(org.getActiveOrgPlanSubscription().getCurrencyId());
				organizationAdCredit.setNoOfAds(this.getNumberOfBuyAds());
				organizationAdCredit.setTotalAmount(this.getTotalBuyAdsCost());
				
				//organizationAdCredit.setTotalAmount(new BigDecimal(0));//Not Sure why this is required.
				organizationAdCredit.setCreditType(new Long(Constants.AD_CREDIT_TYPE_PAID));
				//organizationAdCredit.setChargeId(chargeid);//TODO
				organizationAdCredit.setOrganizationPlanId(org.getActiveOrgPlanSubscription().getOrganizationPlanId());
				organizationAdCredit.setTransactionDate(new Date());
				
				org.setAdsBalance(org.getAdsBalance().add(new BigDecimal(this.getNumberOfBuyAds())));
				
				org.getOrganizationAdCreditSet().add(organizationAdCredit);
				
				//String chargeid = null;;
				StripeDataTO dataTO = null;
				if(this.useDifferentCard){
					   String[] expDate = this.getExpiryDate().split("-");
					//chargeid = CommonUtil.processStripePayment(this.getTotalBuyAdsCost(), this.getCardNumber(),expDate[0],expDate[1],this.getCvv());
					   dataTO = new StripeDataTO(this.getTotalBuyAdsCost(), "4000000000000002",expDate[0],expDate[1],this.getCvv());
				}
				else{
					//chargeid = CommonUtil.processStripePayment(this.getTotalBuyAdsCost(), org.getStripeCustomerId());
					dataTO = new StripeDataTO(this.getTotalBuyAdsCost(), org.getStripeCustomerId());
				}
					
				
				
				String chargeId = organizationService.updateOrganizationAndProcessStripe(org, dataTO);
				log.info("ChargeId Value "+chargeId+" created for Organization: "+org.getOrganizationId()+" with customerId: "+org.getStripeCustomerId());	
				
				//Now processing the stripe charge if above does update does not throw exception.
				
				StringBuffer buyAdsHdr = new StringBuffer("Hi "+loginUser.getFirstName()+", <br/>");
				buyAdsHdr.append("An amount  of $"+this.getTotalBuyAdsCost()+" has been charged to the credit card on file.<br/>");
				
				buyAdsHdr.append("Your previous ad balance was: "+originalAds+"<br/>");	
				buyAdsHdr.append("Your new ad balance is: "+org.getAdsBalance()+"<br/><br/>");	
			
				String appLinkAppleUrl = AppInitializer.appLinkAppleText+" "+"<a href='"+AppInitializer.appLinkApple+"'  target='_blank'>Click Here</a>";
				String appLinkAndroidUrl = AppInitializer.appLinkAndroidText+" "+"<a href='"+AppInitializer.appLinkAndroid+"'  target='_blank'>Click Here</a>";
			 
				String residualIncUrl = AppInitializer.residualIncText+" "+"<a href='"+AppInitializer.residualInc+"'  target='_blank'>Click Here</a>";
			
				String msgBody = buyAdsHdr.toString()+"<br/><br/>"  + appLinkAppleUrl + "<br/><br/>"
				+ appLinkAndroidUrl +"<br/><br/>"+residualIncUrl +"<br/><br/>" +AppInitializer.rmsThankYou;
 			
				String userEmail = !(AppInitializer.defaultUserEmail.equalsIgnoreCase(""))?AppInitializer.defaultUserEmail:loginUser.getEmail();
				log.info("Printing msgBody "+msgBody+" to be sent to "+userEmail);	
	            
	            EmailUtil emailUtil = (EmailUtil) Component.getInstance(EmailUtil.class);
				try {
					emailUtil.prepareMessageAndSend(userEmail,AppInitializer.defaultEmailSubject+" Ads Purchase Info", msgBody);
				} catch (RsntException e) {
					e.printStackTrace();
				}
						
			//}
			
		}
	}*/

	private void confirmBuyMoreAdsServer() throws RsntException{
		if(this.getTotalBuyAdsCost().compareTo(new BigDecimal(0))==1){
			setPlanPaymentSucessFlag(false);
			
			Organization org = organizationService.getOrganizationDetail(new Long(Contexts.getSessionContext().get(Constants.CONST_ORGID).toString()));
			List<Object[]> objArr = organizationService.getMonthlyReportOrgAdBalance(new Long(org.getOrganizationId()));
			Integer adsBalanceBeforeReport = null;
			Integer currentSeq = 0;
			if(objArr!=null && objArr.size()>0){
				Object[] adDataArr = objArr.get(0);
				adsBalanceBeforeReport= (Integer)adDataArr[1];
				currentSeq= (Integer)adDataArr[0];
			}
			
			String chargeid = null;;
			try {
				if(this.useDifferentCard){
					String[] expDate = this.getExpiryDate().split("-");
					log.info("confirmBuyMoreAdsServer():StripeInfo:Charge:Start Charge for New Card for Organization: "+org.getEmail()+" getTotalBuyAdsCost: " +this.getTotalBuyAdsCost());
					chargeid = CommonUtil.processStripePayment(this.getTotalBuyAdsCost(), this.getCardNumber(),expDate[0],expDate[1],this.getCvv());
					log.info("confirmBuyMoreAdsServer():StripeInfo:Charge:Success Charge ID :"+chargeid +" createdfor Organization: "+org.getEmail()+" getTotalBuyAdsCost:" +this.getTotalBuyAdsCost());
				}
				else{
					log.info("confirmBuyMoreAdsServer():StripeInfo:Charge:Start Charge for Existing Customer for Organization: "+org.getEmail()+" getTotalBuyAdsCost: " +this.getTotalBuyAdsCost());
					chargeid = CommonUtil.processStripePayment(this.getTotalBuyAdsCost(), org.getStripeCustomerId());
					log.info("confirmBuyMoreAdsServer():StripeInfo:Charge:Success Charge ID :"+chargeid +" createdfor Organization: "+org.getEmail()+" getTotalBuyAdsCost:" +this.getTotalBuyAdsCost());
				}
				
			} catch (StripeException e) {
				e.printStackTrace();
				throw new RsntException(e);
			}
			if(chargeid!=null){
				setPlanPaymentSucessFlag(true);
				BigDecimal originalAds = org.getAdsBalance();
				
				OrganizationAdCredit organizationAdCredit = new OrganizationAdCredit();
				organizationAdCredit.setOrganization(org);
				organizationAdCredit.setCostPerAd(org.getActiveOrgPlanSubscription().getCostPerAd());
				organizationAdCredit.setCurrencyId(org.getActiveOrgPlanSubscription().getCurrencyId());
				organizationAdCredit.setNoOfAds(this.getNumberOfBuyAds());
				organizationAdCredit.setTotalAmount(this.getTotalBuyAdsCost());
				
				//organizationAdCredit.setTotalAmount(new BigDecimal(0));//Not Sure why this is required.
				organizationAdCredit.setCreditType(new Long(Constants.AD_CREDIT_TYPE_PAID));
				organizationAdCredit.setChargeId(chargeid);
				organizationAdCredit.setOrganizationPlanId(org.getActiveOrgPlanSubscription().getOrganizationPlanId());
				organizationAdCredit.setTransactionDate(new Date());
				
				org.setAdsBalance(org.getAdsBalance().add(new BigDecimal(this.getNumberOfBuyAds())));
				
				org.getOrganizationAdCreditSet().add(organizationAdCredit);
				
				org = organizationService.createUpdateOrganization(org);
				
				Set<AdUsageHistoryReport> adUsageHistoryReportSet = new HashSet<AdUsageHistoryReport>();
				
				AdUsageHistoryReport adUsageHistoryReport = new AdUsageHistoryReport();
				adUsageHistoryReport.setOrganization(org);
				adUsageHistoryReport.setAdsBalance(adsBalanceBeforeReport+ this.getNumberOfBuyAds());
				adUsageHistoryReport.setAdsCredit(new Long(this.getNumberOfBuyAds()));//Ads Consumed: Total As before - Current Org Ad balance
				adUsageHistoryReport.setActivityRef("PURC_ADS");
				adUsageHistoryReport.setActivityDesc("Current Plan Ads Purchase ("+org.getActiveOrgPlanSubscription().getPlan().getPlanName()+")");
				adUsageHistoryReport.setActivityDate(new Date());
				adUsageHistoryReport.setNotes("Applied to CC on file");
				adUsageHistoryReport.setAmount(this.getTotalBuyAdsCost());
				adUsageHistoryReport.setActivitySeq(new Long(++currentSeq));
				adUsageHistoryReportSet.add(adUsageHistoryReport);
				
				organizationService.createOrganizationAdUsageHistory(adUsageHistoryReportSet);
				
				StringBuffer buyAdsHdr = new StringBuffer("Hi "+loginUser.getFirstName()+", <br/>");
				buyAdsHdr.append("An amount  of $"+this.getTotalBuyAdsCost()+" has been charged to the credit card on file.<br/>");
				
				buyAdsHdr.append("Your previous ad balance was: "+originalAds+"<br/>");	
				buyAdsHdr.append("Your new ad balance is: "+org.getAdsBalance()+"<br/><br/>");	
			
				/*String appLinkAppleUrl = AppInitializer.appLinkAppleText+" "+"<a href='"+AppInitializer.appLinkApple+"'  target='_blank'>Click Here</a>";
				String appLinkAndroidUrl = AppInitializer.appLinkAndroidText+" "+"<a href='"+AppInitializer.appLinkAndroid+"'  target='_blank'>Click Here</a>";
			 
				String residualIncUrl = AppInitializer.residualIncText+" "+"<a href='"+AppInitializer.residualInc+"'  target='_blank'>Click Here</a>";
				*/
				
				 String appLinkAppleUrl = "<a href='"+AppInitializer.appLinkApple+"'  target='_blank'><img src=\""+AppInitializer.domainNameUrl+AppInitializer.appImangeExtPath+"/"+AppInitializer.appLinkImg+"\"/></a>";
				 String appLinkAndroidUrl = "<a href='"+AppInitializer.appLinkAndroid+"'  target='_blank'><img src=\""+AppInitializer.domainNameUrl+AppInitializer.appImangeExtPath+"/"+AppInitializer.androidLinkImg+"\"/></a>";
			
				String msgBody = buyAdsHdr.toString()+"<br/><br/>"  + appLinkAppleUrl + "<br/><br/>"
				+ appLinkAndroidUrl  +"<br/><br/>" +AppInitializer.rmsThankYou;
 			
				String userEmail = !(AppInitializer.defaultUserEmail.equalsIgnoreCase(""))?AppInitializer.defaultUserEmail:loginUser.getEmail();
				log.info("Printing msgBody "+msgBody+" to be sent to "+userEmail);	
	            
	            EmailUtil emailUtil = (EmailUtil) Component.getInstance(EmailUtil.class);
				try {
					emailUtil.prepareMessageAndSend(userEmail,AppInitializer.defaultEmailSubject+" Ads Purchase Info", msgBody);
				} catch (RsntException e) {
					e.printStackTrace();
				}
						
			}
			
		}
	}
	public String navigateToPaymentPage(){
		try{
			
			if(this.getTotalUnitsCost().compareTo(BigDecimal.ZERO) ==0){
				//return confirmUpgrade();
			}
			
			cardProcessorList = new ArrayList<SelectItem>();
			
			List<Object[]> cardLookupObjArr = lookupService.getLookupsForLookupType(new Long(Constants.CONT_LOOKUPTYPE_CARDTYPE));

			for(Object[] obj : cardLookupObjArr ){
				BigDecimal cardId = new BigDecimal(obj[0].toString());
				cardProcessorList.add(new SelectItem(cardId.longValue(),obj[1].toString()));
			}
			//To make default payment card as Visa
			this.setSelectedCardProcessorId(String.valueOf(Constants.CONT_LOOKUP_CARDTYPE_VISA));
			loadCustomerCardData();
			if(this.getCardNumber()!=null)setUseDifferentCard(false);
			else setUseDifferentCard(true);
			
			return "paymentDetail";
		}
		catch(RsntException e){
			e.printStackTrace();
		}
		return null;
		
	}
	
	public String navigateToHome(){
		return "success";
	}
	
	public void onPlanChange(){
		try{
			log.info("selectedPlanId: "+this.getSelectedPlanId());
			List<PlanPrice> planPriceEntityList =  planService.getPlanPriceFromPlan(this.getSelectedPlanId());
			planPriceList = new ArrayList<SelectItem>();
			this.setSelectedPlanPriceId("0");
			this.setSelectedPlanCost("");
			this.setUnitsCount(null);
			this.setTotalUnitsCost(null);
			for(PlanPrice planPrice : planPriceEntityList){
				planPriceList.add(new SelectItem(planPrice.getPlanPriceId(),planPrice.getUnit().getCode()));
			}
			planPriceList.add(new SelectItem(0,"Select.."));
			if(this.getSelectedPlanId().intValue() == Integer.valueOf(AppInitializer.freePlanId)){
				this.setUnitsCount(new BigDecimal(12));
			}
			
		}
		catch(RsntException e){
			e.printStackTrace();
		} 
	}
	
	public void onPlanPriceChange(){
		try{
			log.info("selectedPlanPriceId: "+this.getSelectedPlanPriceId());
			selectedPlanPrice =  planService.getPlanPriceEntity(Long.valueOf(this.getSelectedPlanPriceId()));
			//planUnitList = new ArrayList<SelectItem>();
			//this.setSelectedPlanUnitId("");
			
			this.setSelectedPlanCost(selectedPlanPrice.getAmountPerUnit() + " " + selectedPlanPrice.getCurrency().getCode());
			if(this.getUnitsCount()!=null){
				this.setTotalUnitsCost(selectedPlanPrice.getAmountPerUnit().multiply(this.getUnitsCount()));
			}
			//this.setPlanCost("100 USD");
		}
		catch(RsntException e){
			e.printStackTrace();
		} 
	}
	
	public void onBuyAdsChange(){
		try{
			log.info("buyAdsCount: "+this.getNumberOfBuyAds());
			if(this.getNumberOfBuyAds()!=null){
				this.setTotalBuyAdsCost(selectedOrgPlanSub.getCostPerAd().multiply(new BigDecimal(this.getNumberOfBuyAds())));
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	

	
	public String startUpgradePlan(){
		
		//The same plan check is only applicable for free plan because for other plans he can change the term of the subscription. So that case is valid.
		if(this.getSelectedPlanId().intValue() == Integer.valueOf(AppInitializer.freePlanId)
				&& this.getSelectedPlanId().intValue() == selectedOrgPlanSub.getPlan().getPlanId().intValue() ){
			final FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, messages.get("target.planpick.error").toString(), null);
	        FacesContext.getCurrentInstance().addMessage(null, message);
	        return null;
		}
                
        
		try{
		
			Organization org = organizationService.getOrganizationDetail(new Long(Contexts.getSessionContext().get(Constants.CONST_ORGID).toString()));
			
			selectedPlan = planService.getPlanEntity(this.getSelectedPlanId());
			
			Date today = Calendar.getInstance().getTime();
			Date yesterday = CommonUtil.getPreviousDay(today);
			Date planStartDate = selectedOrgPlanSub.getStartDate();
			Date planEndDate = selectedOrgPlanSub.getEndDate();
			
			int planDateDiff = CommonUtil.getDateDifferenceInDay(planEndDate, planStartDate)<=0?1:CommonUtil.getDateDifferenceInDay(planEndDate, planStartDate);
			BigDecimal planPerDayCost = selectedOrgPlanSub.getTotalAmount().divide(new BigDecimal(planDateDiff), 2, RoundingMode.HALF_UP);
			
			int planActualDiff = CommonUtil.getDateDifferenceInDay(yesterday,planStartDate)<=0?1:CommonUtil.getDateDifferenceInDay(yesterday,planStartDate);
			BigDecimal actualPlanCost= planPerDayCost.multiply(new BigDecimal(planActualDiff));
			BigDecimal totalAmtPaidPreviously = selectedOrgPlanSub.getTotalAmount();
			//.compareTo(new BigDecimal(0))==-1 ? new BigDecimal(0) 
				//											: selectedOrgPlanSub.getTotalAmountPaid();
			BigDecimal oldPlanRefund = totalAmtPaidPreviously.subtract(actualPlanCost); //This is based on the actual paid cost.
			this.setOldPlanOnlyRefundAmount(oldPlanRefund); //This val will always be positive
			
			//Do Ad balance calculation if only he has plan units - Month/Year.
			BigDecimal adAddCost = new BigDecimal(0);
			int orgAdBalance = org.getAdsBalance().intValue();
			Integer plannedAdConsumptionPerDay = getCountOfFreeAdsPerDay();
			Integer balanceAdDeductible =0;
			
			if(plannedAdConsumptionPerDay!=null){
				Integer allowedConsumption = planActualDiff* plannedAdConsumptionPerDay;
				
						
				String adCreditStr = planService.getFreeAdCountCreditedForPlan
						(org.getOrganizationId(), selectedOrgPlanSub.getOrganizationPlanId());
				Integer adsCredited = new Integer(0);
				if(adCreditStr!=null)adsCredited= new Integer(adCreditStr);
				
				balanceAdDeductible= adsCredited - allowedConsumption;
				
				if(orgAdBalance<balanceAdDeductible){
					adAddCost = selectedOrgPlanSub.getCostPerAd().multiply(new BigDecimal(orgAdBalance-balanceAdDeductible));
					//The above val will be negative.
					this.setAdOnlyAdjustmentCost(adAddCost);
				}
				else{
					//org.setAdsBalance(new BigDecimal(orgAdBalance-balanceAdDeductible)); 
					this.setAdOnlyAdjustmentCost(adAddCost);
				}
				
			}
			
			oldPlanRefund = oldPlanRefund.add(adAddCost);
			this.setTotalOldPlanRefund(oldPlanRefund);
			
			this.setOldPlanProratedAmount(actualPlanCost);
			
			this.setCurrentPlanAmount(this.getTotalUnitsCost());
			
			return "success";
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public String confirmPlanUpgrade() throws RsntException{
		try{
			return confirmUpgrade();
		}
		catch(RsntException e){
			e.printStackTrace();
			final FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, messages.get("plan.upgrade.failed").toString(), null);
	        FacesContext.getCurrentInstance().addMessage(null, message);

			if(isPlanPaymentSucessFlag()){
				EmailUtil emailUtil = (EmailUtil) Component.getInstance(EmailUtil.class);
				emailUtil.prepareMessageAndSend(this.getSelectedOrgPlanSub().getOrganization().getOrganizationId().toString(),"Plan Upgrade Failed and payment transaction status: "
							+this.isPlanPaymentSucessFlag(), "Plan Upgrade Failed");
			}
		}
		
		return null;
		
	}
	
	private String confirmUpgrade() throws RsntException{
		
			Organization org = organizationService.getOrganizationDetail(new Long(Contexts.getSessionContext().get(Constants.CONST_ORGID).toString()));
			List<Object[]> objArr = organizationService.getMonthlyReportOrgAdBalance(new Long(Contexts.getSessionContext().get(Constants.CONST_ORGID).toString()));
			Integer adsBalanceBeforeUpgrade = null;
			Long balanceAfterProrata = null;
			Integer currentSeq = 0;
			if(objArr!=null && objArr.size()>0){
				Object[] adDataArr = objArr.get(0);
				adsBalanceBeforeUpgrade= (Integer)adDataArr[1];
				currentSeq= (Integer)adDataArr[0];
			}
			/*Integer adsCredited = new Integer(planService.getFreeAdCountCreditedForPlan
					(org.getOrganizationId(), selectedOrgPlanSub.getPlan().getPlanId()));*/
			
			String adCreditStr = planService.getFreeAdCountCreditedForPlan
			(org.getOrganizationId(), selectedOrgPlanSub.getOrganizationPlanId());
			Integer adsCredited = new Integer(0);
			if(adCreditStr!=null)adsCredited= new Integer(adCreditStr);
	
			
			OrganizationPlanSubscription newSubscription = new OrganizationPlanSubscription();
			newSubscription.setPlan(selectedPlan);
			newSubscription.setOrganization(org);
			
			newSubscription.setAmountPerUnit(selectedPlanPrice.getAmountPerUnit());
			newSubscription.setUnitId(selectedPlanPrice.getUnit().getLookupId());
			newSubscription.setCurrencyId(selectedPlanPrice.getCurrency().getLookupId());
			newSubscription.setNumberOfUnits(this.getUnitsCount().longValue()); 
			newSubscription.setTotalAmount((this.getTotalUnitsCost()));//TODO //Why??
			//newSubscription.setTotalAmountPaid(this.getCurrentPlanAdjustmentAmount());//This is the what the customer has to additionally pay.
			//organization.setOrganizationName("TODO");
			newSubscription.setStartDate(new Date());
			newSubscription.setEndDate(CommonUtil.getEndDate(this.getSelectedPlanPrice().getUnit().getLookupId(), new Date(),this.getUnitsCount().intValue(),true));
			//organization.setAdsBalance(new BigDecimal(this.getTotalUnitsCost()));
			newSubscription.setCostPerAd(selectedPlanPrice.getCostPerAd());
			newSubscription.setNoOfAdsPerUnit(selectedPlanPrice.getNoOfAdsPerUnit());
			
			//org.getOrganizationPlanSubscriptionList().add(newSubscription);
			
			
			//Create the Invoice
			Date today = Calendar.getInstance().getTime();
			Date yesterday = CommonUtil.getPreviousDay(today);
			Date planStartDate = selectedOrgPlanSub.getStartDate();
			Date planEndDate = selectedOrgPlanSub.getEndDate();
			
			int planDateDiff = CommonUtil.getDateDifferenceInDay(planEndDate, planStartDate)<=0?1:CommonUtil.getDateDifferenceInDay(planEndDate, planStartDate);
			BigDecimal planPerDayCost = selectedOrgPlanSub.getTotalAmount().divide(new BigDecimal(planDateDiff), 2, RoundingMode.HALF_UP);
			
			int planActualDiff = CommonUtil.getDateDifferenceInDay(yesterday,planStartDate)<=0?1:CommonUtil.getDateDifferenceInDay(yesterday,planStartDate);
			BigDecimal actualPlanCost= planPerDayCost.multiply(new BigDecimal(planActualDiff));
			BigDecimal totalAmtPaidPreviously = selectedOrgPlanSub.getTotalAmount();
			//.compareTo(new BigDecimal(0))==-1 ? new BigDecimal(0) 
				//											: selectedOrgPlanSub.getTotalAmountPaid();
			BigDecimal oldPlanRefund = totalAmtPaidPreviously.subtract(actualPlanCost); //This is based on the actual paid cost.
			
			OrganizationPlanSubscriptionInvoice invoice = new OrganizationPlanSubscriptionInvoice();
			invoice.setOrganization(org);
			invoice.setPreviousPlanId(selectedOrgPlanSub.getPlan().getPlanId());
			invoice.setPreviousPlanName(selectedOrgPlanSub.getPlan().getPlanName());
			invoice.setCurrentPlanId(selectedPlan.getPlanId());
			invoice.setCurrentPlanName(selectedPlan.getPlanName());
			invoice.setInvoiceDate(new Date());
			invoice.setAction("CHANGE");
			
			invoice.setNoOfPlanDaysAdjustment(new Long(planActualDiff));
			invoice.setCostPerDayAdjustment(planPerDayCost);
			//invoice.setPlanAmountAdjustment(oldPlanRefund);
			
			
			//Do Ad balance calculation if only he has plan units - Month/Year.
			BigDecimal adAddCost = new BigDecimal(0);
			int orgAdBalance = org.getAdsBalance().intValue();
			Integer plannedAdConsumptionPerDay = getCountOfFreeAdsPerDay();
			Integer balanceAdDeductible =0;
			
			if(plannedAdConsumptionPerDay!=null){
				Integer allowedConsumption = planActualDiff* plannedAdConsumptionPerDay;
				//Integer adsCredited = new Integer(planService.getFreeAdCountCreditedForPlan(org.getOrganizationId()).toString());
				balanceAdDeductible= adsCredited - allowedConsumption;
				
				if(orgAdBalance<balanceAdDeductible){
					adAddCost = selectedOrgPlanSub.getCostPerAd().multiply(new BigDecimal(orgAdBalance-balanceAdDeductible));
					this.setAdOnlyAdjustmentCost(adAddCost);
				}
				else{
					//org.setAdsBalance(new BigDecimal(orgAdBalance-balanceAdDeductible));
					this.setAdOnlyAdjustmentCost(adAddCost);
				}
				
			}
			
			invoice.setPlanAmountAdjustment(oldPlanRefund);
			invoice.setAdsAmountAdjustment(adAddCost);
			
			oldPlanRefund = oldPlanRefund.add(adAddCost);
			
			this.setOldPlanProratedAmount(actualPlanCost);
			this.setCurrentPlanAmount(this.getTotalUnitsCost());
			
			
			invoice.setTotalActivePlanAmount(this.getTotalUnitsCost());
			invoice.setTotalAmount(this.getTotalUnitsCost());
			
			
			invoice.setNoOfAdsAdjustment(new Long(balanceAdDeductible));
			invoice.setCostPerAdAdjustment(selectedOrgPlanSub.getCostPerAd());
			
			setPlanPaymentSucessFlag(false);
			//Refund the money for the old plan using the old charge. And charge him for the new plan.
			
			/*String chargeId = selectedOrgPlanSub.getChargeId();
			if(this.getCurrentPlanAdjustmentAmount().compareTo(new BigDecimal(0))==-1){
				//Need to refund
				refundStripePayment(selectedOrgPlanSub.getChargeId(), invoice.getTotalAmount());
			}
			else if(this.getCurrentPlanAdjustmentAmount().compareTo(new BigDecimal(0))==1){
				chargeId =processStripePayment(this.getCurrentPlanAdjustmentAmount());
			}
			if(chargeId==null){
				throw new RsntException("Unable to Process Payment");
			}*/
			
			log.info("confirmUpgrade():StripeInfo:Refund Start :for Organization: "+org.getEmail()+" oldPlanRefund:" +oldPlanRefund);
			refundStripePayment(selectedOrgPlanSub.getChargeId(),oldPlanRefund);
			log.info("confirmUpgrade():StripeInfo:Refund Success :for Organization: "+org.getEmail()+" oldPlanRefund:" +oldPlanRefund);
			/*String chargeId;
			try {
				chargeId = CommonUtil.processStripePayment(this.getCurrentPlanAmount(),org.getStripeCustomerId());
			} catch (StripeException e) {
				e.printStackTrace();
				throw new RsntException(e);
			}
			*/
			
			String chargeid = null;;
			try {
				if(this.getCurrentPlanAmount().compareTo(BigDecimal.ZERO)==1){
					if(this.useDifferentCard){
						log.info("Using a different card with exp date: "+this.getExpiryDate());
						String[] expDate = this.getExpiryDate().split("-");
						String newCust = CommonUtil.createCustomer(this.getCardNumber(), expDate[0], expDate[1], this.getCvv(), org.getEmail());
						log.info("Created Cust for new Card : "+newCust);
						//chargeid = CommonUtil.processStripePayment(this.getCurrentPlanAmount(), this.getCardNumber(),expDate[0],expDate[1],this.getCvv());
						log.info("confirmUpgrade():StripeInfo:Charge:Start Charge for New Customer for Organization: "+org.getEmail()+" CurrentPlanAmount: " +this.getCurrentPlanAmount());
						chargeid = CommonUtil.processStripePayment(this.getCurrentPlanAmount(), newCust);
						log.info("confirmUpgrade():StripeInfo:Charge:Success Charge ID :"+chargeid +" createdfor Organization: "+org.getEmail()+" CurrentPlanAmount:" +this.getCurrentPlanAmount());
						org.setStripeCustomerId(newCust);
					}
					else{
						log.info("confirmUpgrade():StripeInfo:Charge:Start Charge for Existing Customer for Organization: "+org.getEmail()+" CurrentPlanAmount: " +this.getCurrentPlanAmount());
						chargeid = CommonUtil.processStripePayment(this.getCurrentPlanAmount(), org.getStripeCustomerId());
						log.info("confirmUpgrade():StripeInfo:Charge:Success Charge ID :"+chargeid +" createdfor Organization: "+org.getEmail()+" CurrentPlanAmount:" +this.getCurrentPlanAmount());
					}
				}
				
				
			} catch (StripeException e) {
				e.printStackTrace();
				throw new RsntException(e);
			}
			
			Set<PlanFeature> planFeatureSet =  selectedPlan.getPlanFeatureSet();
			
			Iterator<PlanFeature> planFeatureItr = planFeatureSet.iterator();
			
			while(planFeatureItr.hasNext()){
				PlanFeature feature = planFeatureItr.next();
				
				if(feature.getFeatureId().intValue() == Constants.CONT_LOOKUPTYPE_FEATURE_FEEDBACK){
					int feedbackTypeId = feature.getChildFeatureId().intValue();
					log.info("feature.getChildFeatureId():"+feature.getChildFeatureId().intValue());
					if(feedbackTypeId == Constants.CONT_LOOKUP_FEEDBACKTYPE_ADVANCED){
						org.getFeedbackQuestionaire().setActive(true);
						log.info("Inside advance block");
					}
					else{
						org.getFeedbackQuestionaire().setActive(false);
						log.info("Inside basic block");

					}
					
				}
			}
			
			org.setLastPaymentTrxDescription("PLAN UPGRADE CHARGE");
			org.setLastPaymentTrxStatus("SUCCESS");
			
			setPlanPaymentSucessFlag(true);
			newSubscription.setChargeId(chargeid);
			//Setting this here so that if the payment above fails then we dont end up creating the records below.
			org.setActiveOrgPlanSubscription(newSubscription);
			//org.getOrganizationAdCreditSet().add(organizationAdCredit);
			
			
			if(orgAdBalance<balanceAdDeductible){
				org.setAdsBalance(new BigDecimal(0)); // What we do here. We have charged him above for counts of ads which is less. Now the dedeuction 
				//of the ad count also needs to be done from the org.
				balanceAfterProrata = new Long(0);
			}
			else{
				org.setAdsBalance(new BigDecimal(orgAdBalance-balanceAdDeductible));
				balanceAfterProrata = new Long(orgAdBalance-balanceAdDeductible);
				
			}
			//Add the credit for the new Plan subscription.
			org.setAdsBalance(org.getAdsBalance().add(new BigDecimal(selectedPlanPrice.getNoOfAdsPerUnit())));
			
			
			//org.setAdCreditScheduler(adCreditScheduler);
			
			org = organizationService.createUpdateOrganization(org);
		
			OrganizationAdCreditScheduler adCreditScheduler =   org.getAdCreditScheduler();
			adCreditScheduler.setNoOfAdsCredited(selectedPlanPrice.getNoOfAdsPerUnit());
			adCreditScheduler.setCreditEndDate(newSubscription.getEndDate());
			adCreditScheduler.setUnitId(selectedPlanPrice.getUnit().getLookupId());
			adCreditScheduler.setNextScheduledDate(CommonUtil.getEndDate(this.getSelectedPlanPrice().getUnit().getLookupId(), new Date(),1,false));
			adCreditScheduler.setOrganizationPlanId(org.getActiveOrgPlanSubscription().getOrganizationPlanId());
			
			
			selectedOrgPlanSub.setTerminateDate(new Date());
			planService.updateOrgPlanSubscription(selectedOrgPlanSub);
			
			
			planService.createOrgSubsInvoice(invoice);
			
			organizationService.createOrganizationAdScheduler(adCreditScheduler);
			
			OrganizationAdCredit organizationAdCredit = new OrganizationAdCredit();
			organizationAdCredit.setOrganization(org);
			organizationAdCredit.setCostPerAd(selectedPlanPrice.getCostPerAd());
			organizationAdCredit.setCurrencyId(selectedPlanPrice.getCurrency().getLookupId());
			organizationAdCredit.setNoOfAds(selectedPlanPrice.getNoOfAdsPerUnit());
			//IF he has taken plan for two years then we need to give him the free ads monthly and not in one shot.
			//If he terminates before two years then on a prorated basis his ad cost will be charged.
			//organizationAdCredit.setTotalAmount((this.getTotalUnitsCost()));
			organizationAdCredit.setTotalAmount(new BigDecimal(0));//Not Sure why this is required.
			organizationAdCredit.setCreditType(new Long(Constants.AD_CREDIT_TYPE_FREE));
			organizationAdCredit.setOrganizationPlanId(org.getActiveOrgPlanSubscription().getOrganizationPlanId());
			organizationAdCredit.setTransactionDate(new Date());
			
			organizationService.createOrganizationAdCredit(organizationAdCredit);
			
			/*organizationService.updateActiveOrganizationPlanSubscription(org.getOrganizationId(), 
					organization.getOrganizationPlanSubscriptionList().get(0).getOrganizationPlanId());*/
			
			Set<AdUsageHistoryReport> adUsageHistoryReportSet = new HashSet<AdUsageHistoryReport>();
			
			AdUsageHistoryReport adUsageHistoryReport = new AdUsageHistoryReport();
			adUsageHistoryReport.setOrganization(org);
			adUsageHistoryReport.setAdsBalance(new Long(orgAdBalance));
			adUsageHistoryReport.setAdsCredit(new Long(adsBalanceBeforeUpgrade-orgAdBalance));//Ads Consumed: Total As before - Current Org Ad balance
			adUsageHistoryReport.setActivityRef("UPGRD_OLD_USAGE");
			adUsageHistoryReport.setActivityDesc("Old Plan Ads Usage for Current Month ("+selectedOrgPlanSub.getPlan().getPlanName()+")");
			adUsageHistoryReport.setActivityDate(new Date());
			adUsageHistoryReport.setActivitySeq(new Long(++currentSeq));
			adUsageHistoryReportSet.add(adUsageHistoryReport);
			
			AdUsageHistoryReport adUsageHistoryReport2 = new AdUsageHistoryReport();
			adUsageHistoryReport2.setOrganization(org);
			adUsageHistoryReport2.setAdsBalance(new Long(balanceAfterProrata));
			adUsageHistoryReport2.setAdsCredit(new Long(balanceAdDeductible));
			adUsageHistoryReport2.setActivityRef("UPGRD_OLD_PRORATE");
			adUsageHistoryReport2.setActivityDesc("Old Plan Ads Pro-rate Debit ("+selectedOrgPlanSub.getPlan().getPlanName()+")");
			adUsageHistoryReport2.setActivityDate(new Date());
			adUsageHistoryReport2.setActivitySeq(new Long(++currentSeq));
			adUsageHistoryReportSet.add(adUsageHistoryReport2);
			
			AdUsageHistoryReport adUsageHistoryReport3 = new AdUsageHistoryReport();
			adUsageHistoryReport3.setOrganization(org);
			adUsageHistoryReport3.setAdsBalance(org.getAdsBalance().longValue());
			adUsageHistoryReport3.setAdsCredit(selectedPlanPrice.getNoOfAdsPerUnit());
			adUsageHistoryReport3.setActivityRef("UPGRD_NEW_PLAN_ADS");
			adUsageHistoryReport3.setActivityDesc("New Plan Ads Credit ("+selectedPlan.getPlanName()+")");
			adUsageHistoryReport3.setActivityDate(new Date());
			adUsageHistoryReport3.setActivitySeq(new Long(++currentSeq));
			adUsageHistoryReportSet.add(adUsageHistoryReport3);
			
			organizationService.createOrganizationAdUsageHistory(adUsageHistoryReportSet);
			
			
			StringBuffer upgradePlanHdr = new StringBuffer("Hi "+loginUser.getFirstName()+", <br/>");
			if(this.getTotalOldPlanRefund().compareTo(new BigDecimal(0))>0){
				upgradePlanHdr.append("A credit of $"+this.getTotalOldPlanRefund()+" calculated on a prorated basis for your existing plan for your account and applied to the credit card on file.<br/>");
				
			}
			
			if(newSubscription.getTotalAmount().compareTo(new BigDecimal(0))>0){
				upgradePlanHdr.append("An amount  of $"+newSubscription.getTotalAmount()+" has been charged to the credit card on file for the new plan subscription.<br/>");
				
			}
			
			upgradePlanHdr.append("Your plan has now been changed to: <br/><br/>");
			upgradePlanHdr.append(this.getSelectedPlan().getPlanName()+" ("+selectedPlanPrice.getNoOfAdsPerUnit()+" free ads/"+this.getSelectedPlanPrice().getUnit().getCode()+")<br/>");
			upgradePlanHdr.append("Your previous ad balance was: "+orgAdBalance+"<br/>");	
			upgradePlanHdr.append("Your new ad balance is: "+org.getAdsBalance()+"<br/><br/>");	
			
			upgradePlanHdr.append("Below are the details of your transaction: <br/><br/>");
			
			upgradePlanHdr.append("Initial ad balance: "+orgAdBalance+" ("+selectedOrgPlanSub.getNoOfAdsPerUnit()+
					" ads for "+selectedOrgPlanSub.getPlan().getPlanName()+")<br/>");
			upgradePlanHdr.append(selectedOrgPlanSub.getPlan().getPlanName()+" =$"+selectedOrgPlanSub.getAmountPerUnit()+" for "+selectedOrgPlanSub.getNoOfAdsPerUnit()+" ads <br/>");
			upgradePlanHdr.append("Pro-rated Amount =$"+oldPlanRefund+"<br/>");
			upgradePlanHdr.append(newSubscription.getPlan().getPlanName()+" =$"+newSubscription.getAmountPerUnit()+" for "+newSubscription.getNoOfAdsPerUnit()+" ads <br/>");
			
			upgradePlanHdr.append("Net: ($"+oldPlanRefund +" pro-rated credit) and $("+newSubscription.getTotalAmount()+" charge for new plan) <br/>");
			 
           /* String appLinkAppleUrl = AppInitializer.appLinkAppleText+" "+"<a href='"+AppInitializer.appLinkApple+"'  target='_blank'>Click Here</a>";
            String appLinkAndroidUrl = AppInitializer.appLinkAndroidText+" "+"<a href='"+AppInitializer.appLinkAndroid+"'  target='_blank'>Click Here</a>";
			 
            String residualIncUrl = AppInitializer.residualIncText+" "+"<a href='"+AppInitializer.residualInc+"'  target='_blank'>Click Here</a>";*/
			 /*String appLinkAppleUrl = "<a href='"+AppInitializer.appLinkApple+"'  target='_blank'><img src=\""+AppInitializer.appLinkImg+"\"/></a>";
			 String appLinkAndroidUrl = "<a href='"+AppInitializer.appLinkAndroid+"'  target='_blank'><img src=\""+AppInitializer.androidLinkImg+"\"/></a>";
			*/
			//added by vruddhi
			String appLinkAppleUrl = "<a href='"+AppInitializer.appLinkApple+"'  target='_blank'><img src=\""+AppInitializer.domainNameUrl+AppInitializer.appImangeExtPath+"/"+AppInitializer.appLinkImg+"\"/></a>";
			String appLinkAndroidUrl = "<a href='"+AppInitializer.appLinkAndroid+"'  target='_blank'><img src=\""+AppInitializer.domainNameUrl+AppInitializer.appImangeExtPath+"/"+AppInitializer.androidLinkImg+"\"/></a>";
			//ended by vruddhi
			
            String msgBody = upgradePlanHdr.toString()+"<br/><br/>"  + appLinkAppleUrl + "<br/><br/>"
 			+ appLinkAndroidUrl  +"<br/><br/>" +AppInitializer.rmsThankYou;
 			
            String userEmail = !(AppInitializer.defaultUserEmail.equalsIgnoreCase(""))?AppInitializer.defaultUserEmail:loginUser.getEmail();
            log.info("Printing msgBody "+msgBody+" to be sent to "+userEmail);	
	            
            EmailUtil emailUtil = (EmailUtil) Component.getInstance(EmailUtil.class);
			try {
				emailUtil.prepareMessageAndSend(userEmail,AppInitializer.defaultEmailSubject+" Account Upgrade Info", msgBody);
			} catch (RsntException e) {
				e.printStackTrace();
			}
			
			return "success";
	}
	
	public String confirmCancelSubscription(){
		try {
			cancelSubscriptionServer();
			return "success";
		} catch (RsntException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	public String startCancelSubscription(){
		try{
			setCancelSubscription(true);
			
			Organization org = organizationService.getOrganizationDetail(new Long(Contexts.getSessionContext().get(Constants.CONST_ORGID).toString()));
			selectedOrgPlanSub = planService.loadActiveOrgPlanSubscription();
			selectedPlan = null;
			this.setTotalUnitsCost(new BigDecimal(0));//Setting it to 0 to render the paypanel 
			
			Date today = Calendar.getInstance().getTime();
			Date yesterday = CommonUtil.getPreviousDay(today);
			Date planStartDate = selectedOrgPlanSub.getStartDate();
			Date planEndDate = selectedOrgPlanSub.getEndDate();
			
			int planDateDiff = CommonUtil.getDateDifferenceInDay(planEndDate, planStartDate)<=0?1:CommonUtil.getDateDifferenceInDay(planEndDate, planStartDate);
			BigDecimal planPerDayCost = selectedOrgPlanSub.getTotalAmount().divide(new BigDecimal(planDateDiff), 2, RoundingMode.HALF_UP);
			
			int planActualDiff = CommonUtil.getDateDifferenceInDay(yesterday,planStartDate)<=0?1:CommonUtil.getDateDifferenceInDay(yesterday,planStartDate);
			BigDecimal actualPlanCost= planPerDayCost.multiply(new BigDecimal(planActualDiff));
			BigDecimal totalAmtPaidPreviously = selectedOrgPlanSub.getTotalAmount();
			
			BigDecimal oldPlanRefund = totalAmtPaidPreviously.subtract(actualPlanCost); //This is based on the actual paid cost.
			this.setOldPlanOnlyRefundAmount(oldPlanRefund);
			
			//Do Ad balance calculation if only he has plan units - Month/Year.
			BigDecimal adAddCost = new BigDecimal(0);
			int orgAdBalance = org.getAdsBalance().intValue();
			Integer plannedAdConsumptionPerDay = getCountOfFreeAdsPerDay();
			Integer balanceAdDeductible =0;
			
			if(plannedAdConsumptionPerDay!=null){
				Integer allowedConsumption = planActualDiff* plannedAdConsumptionPerDay;
				
						
				String adCreditStr = planService.getFreeAdCountCreditedForPlan
						(org.getOrganizationId(), selectedOrgPlanSub.getOrganizationPlanId());
				Integer adsCredited = new Integer(0);
				if(adCreditStr!=null)adsCredited= new Integer(adCreditStr);
				
				balanceAdDeductible= adsCredited - allowedConsumption;
				
				if(orgAdBalance<balanceAdDeductible){
					adAddCost = selectedOrgPlanSub.getCostPerAd().multiply(new BigDecimal(orgAdBalance-balanceAdDeductible));
					this.setAdOnlyAdjustmentCost(adAddCost);
				}
				else{
					//org.setAdsBalance(new BigDecimal(orgAdBalance-balanceAdDeductible)); 
					this.setAdOnlyAdjustmentCost(adAddCost);
				}
				
			}
			
			BigDecimal oldPlanRefundWithAdd  = oldPlanRefund.add(adAddCost);
			this.setTotalOldPlanRefund(oldPlanRefundWithAdd);
			this.setOldPlanProratedAmount(actualPlanCost);
			 
			return "success";
			
		}
		catch(RsntException e){
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	
	public void cancelSubscriptionServer() throws RsntException {
		Organization org = organizationService.getOrganizationDetail(new Long(Contexts.getSessionContext().get(Constants.CONST_ORGID).toString()));
		selectedOrgPlanSub = planService.loadActiveOrgPlanSubscription();
		selectedPlan = null;
		
		Date today = Calendar.getInstance().getTime();
		Date yesterday = CommonUtil.getPreviousDay(today);
		Date planStartDate = selectedOrgPlanSub.getStartDate();
		Date planEndDate = selectedOrgPlanSub.getEndDate();
		
		int planDateDiff = CommonUtil.getDateDifferenceInDay(planEndDate, planStartDate)<=0?1:CommonUtil.getDateDifferenceInDay(planEndDate, planStartDate);
		BigDecimal planPerDayCost = selectedOrgPlanSub.getTotalAmount().divide(new BigDecimal(planDateDiff), 2, RoundingMode.HALF_UP);
		
		int planActualDiff = CommonUtil.getDateDifferenceInDay(yesterday,planStartDate)<=0?1:CommonUtil.getDateDifferenceInDay(yesterday,planStartDate);
		
		BigDecimal actualPlanCost= planPerDayCost.multiply(new BigDecimal(planActualDiff));
		BigDecimal totalAmtPaidPreviously = selectedOrgPlanSub.getTotalAmount();
		
		BigDecimal oldPlanRefund = totalAmtPaidPreviously.subtract(actualPlanCost); //This is based on the actual paid cost.
		this.setOldPlanOnlyRefundAmount(oldPlanRefund);
		
		//Do Ad balance calculation if only he has plan units - Month/Year.
		BigDecimal adAddCost = new BigDecimal(0);
		int orgAdBalance = org.getAdsBalance().intValue();
		Integer plannedAdConsumptionPerDay = getCountOfFreeAdsPerDay();
		Integer balanceAdDeductible =0;
		Integer finalOrgAdBalance = 0;
		
		if(plannedAdConsumptionPerDay!=null){
			Integer allowedConsumption = planActualDiff* plannedAdConsumptionPerDay;
			
					
			String adCreditStr = planService.getFreeAdCountCreditedForPlan
					(org.getOrganizationId(), selectedOrgPlanSub.getOrganizationPlanId());
			Integer adsCredited = new Integer(0);
			if(adCreditStr!=null)adsCredited= new Integer(adCreditStr);
			
			balanceAdDeductible= adsCredited - allowedConsumption;
			
			if(orgAdBalance<balanceAdDeductible){
				adAddCost = selectedOrgPlanSub.getCostPerAd().multiply(new BigDecimal(orgAdBalance-balanceAdDeductible));
				this.setAdOnlyAdjustmentCost(adAddCost);
				finalOrgAdBalance = 0;
			}
			else{
				//org.setAdsBalance(new BigDecimal(orgAdBalance-balanceAdDeductible)); 
				this.setAdOnlyAdjustmentCost(adAddCost);
				finalOrgAdBalance = orgAdBalance-balanceAdDeductible;
			}
			
		}
		
		BigDecimal oldPlanRefundWithAdd  = oldPlanRefund.add(adAddCost);
		
		//if(oldPlanRefundWithAdd.compareTo(new BigDecimal(0))==-1){
		setPlanPaymentSucessFlag(false);
			
		
		log.info("cancelSubscriptionServer():StripeInfo:Refund Start :for Organization: "+org.getEmail()+" oldPlanRefundWithAdd:" +oldPlanRefundWithAdd);
		refundStripePayment(selectedOrgPlanSub.getChargeId(),oldPlanRefundWithAdd);
		log.info("cancelSubscriptionServer():StripeInfo:Refund Success :for Organization: "+org.getEmail()+" oldPlanRefundWithAdd:" +oldPlanRefundWithAdd);
		
		
		//}
		
		org.setLastPaymentTrxDescription("PLAN CANCEL REFUND");
		org.setLastPaymentTrxStatus("SUCCESS");
		
		
		OrganizationPlanSubscriptionInvoice invoice = new OrganizationPlanSubscriptionInvoice();
		
		invoice.setOrganization(org);
		invoice.setPreviousPlanId(selectedOrgPlanSub.getPlan().getPlanId());
		invoice.setPreviousPlanName(selectedOrgPlanSub.getPlan().getPlanName());
		invoice.setCurrentPlanId(null);
		invoice.setCurrentPlanName(null);
		invoice.setInvoiceDate(new Date());
		invoice.setAction("CANCEL");
		
		invoice.setNoOfPlanDaysAdjustment(new Long(planActualDiff));
		invoice.setCostPerDayAdjustment(planPerDayCost);
		
		
			
	
		invoice.setAdsAmountAdjustment(adAddCost);
		
		//invoice.setTotalActivePlanAmount(this.getTotalUnitsCost());
		invoice.setPlanAmountAdjustment(oldPlanRefund);
		invoice.setTotalAmount(oldPlanRefundWithAdd);
		
		
		invoice.setNoOfAdsAdjustment(new Long(balanceAdDeductible));
		invoice.setCostPerAdAdjustment(selectedOrgPlanSub.getCostPerAd());
		
		OrganizationAdCreditScheduler adCreditScheduler =   org.getAdCreditScheduler();
		adCreditScheduler.setCreditEndDate(new Date());
		adCreditScheduler.setNextScheduledDate(new Date());
		
		org.setAdCreditScheduler(adCreditScheduler);
		
		
		selectedOrgPlanSub.setTerminateDate(new Date());
		planService.updateOrgPlanSubscription(selectedOrgPlanSub);
		planService.createOrgSubsInvoice(invoice);
		
		org.setActiveOrgPlanSubscription(null);
		org.setAdsBalance(new BigDecimal(finalOrgAdBalance));
		org.setActiveFlag(false);
		organizationService.createUpdateOrganization(org);
		
	}
	
	/*public String processStripePayment(BigDecimal amt, String customerId) throws RsntException{
		//String[] expDate = this.getExpiryDate().split("-");
		
		Stripe.apiKey = "sk_test_wPF3HJKjUoQEV4FGMYMCquLN";
        Map<String, Object> chargeMap = new HashMap<String, Object>();
        chargeMap.put("amount", CommonUtil.getStripeAmountFormat(amt));
        chargeMap.put("currency", "usd");
        chargeMap.put("customer", customerId);
        Map<String, Object> cardMap = new HashMap<String, Object>();
        cardMap.put("number", this.getCardNumber());
        cardMap.put("exp_month", expDate[0]);
        cardMap.put("exp_year", expDate[1]);
        cardMap.put("cvc_check", this.getCvv());
       // chargeMap.put("card", cardMap);
        try {
            Charge charge = Charge.create(chargeMap);
            System.out.println(charge);
            return charge.getId();
        } catch (StripeException e) {
           throw new RsntException(e);
        }
        catch(Exception e){
        	throw new RsntException(e);
        }
	}
*/	
	private Boolean refundStripePayment(String chargeId, BigDecimal amt) throws RsntException{
		try{
			if(amt.compareTo(new BigDecimal(0))>0){
				Stripe.apiKey = AppInitializer.stripeApiKey;
				Charge charge = Charge.retrieve(chargeId);
				Map<String, Object> chargeMap = new HashMap<String, Object>();
			    chargeMap.put("amount", CommonUtil.getStripeAmountFormat(amt));
			    Charge charge2 =  charge.refund(chargeMap);
			    System.out.println(charge2);
			    return charge2.getPaid();
			}
			return true;
		}catch (StripeException e) {
           throw new RsntException(e);
        }
        catch(Exception e){
        	throw new RsntException(e);
        }
	}


	public void navigateToEditPlanPage(){
		
	}
	
	private Integer getCountOfFreeAdsPerDay(){
		if(selectedOrgPlanSub.getUnitId().intValue() ==Constants.CONT_LOOKUP_PLAN_UNIT_MONTH){
			return Math.round(selectedOrgPlanSub.getNoOfAdsPerUnit()/30);
		}
		else if(selectedOrgPlanSub.getUnitId().intValue() ==Constants.CONT_LOOKUP_PLAN_UNIT_DAY){
			return null;
		}
		else if(selectedOrgPlanSub.getUnitId().intValue() ==Constants.CONT_LOOKUP_PLAN_UNIT_YEAR){
			return  Math.round(selectedOrgPlanSub.getNoOfAdsPerUnit()/365);
		}
		return null;
	}
	
	
	public OrganizationPlanSubscription getSelectedOrgPlanSub() {
		log.info("Calling getgetSelectedOrgPlanSub "+selectedOrgPlanSub);
		return selectedOrgPlanSub;
	}
	public void setSelectedOrgPlanSub(
			OrganizationPlanSubscription selectedOrgPlanSub) {
		this.selectedOrgPlanSub = selectedOrgPlanSub;
	}
	public boolean isUpgradePlanEdit() {
		return upgradePlanEdit;
	}
	public void setUpgradePlanEdit(boolean upgradePlanEdit) {
		this.upgradePlanEdit = upgradePlanEdit;
	}

	public Long getSelectedPlanId() {
		return selectedPlanId;
	}

	public void setSelectedPlanId(Long selectedPlanId) {
		this.selectedPlanId = selectedPlanId;
	}

	public List<SelectItem> getPlanList() {
		return planList;
	}

	public void setPlanList(List<SelectItem> planList) {
		this.planList = planList;
	}

	public String getSelectedPlanPriceId() {
		return selectedPlanPriceId;
	}

	public void setSelectedPlanPriceId(String selectedPlanPriceId) {
		this.selectedPlanPriceId = selectedPlanPriceId;
	}

	public List<SelectItem> getPlanPriceList() {
		return planPriceList;
	}

	public void setPlanPriceList(List<SelectItem> planPriceList) {
		this.planPriceList = planPriceList;
	}

	public String getSelectedPlanCost() {
		return selectedPlanCost;
	}

	public void setSelectedPlanCost(String selectedPlanCost) {
		this.selectedPlanCost = selectedPlanCost;
	}

	public BigDecimal getUnitsCount() {
		return unitsCount;
	}

	public void setUnitsCount(BigDecimal unitsCount) {
		this.unitsCount = unitsCount;
	}

	public BigDecimal getTotalUnitsCost() {
		return totalUnitsCost;
	}

	public void setTotalUnitsCost(BigDecimal totalUnitsCost) {
		this.totalUnitsCost = totalUnitsCost;
	}

	public PlanPrice getSelectedPlanPrice() {
		return selectedPlanPrice;
	}

	public void setSelectedPlanPrice(PlanPrice selectedPlanPrice) {
		this.selectedPlanPrice = selectedPlanPrice;
	}

	public String getCostPerUnitData() {
		return costPerUnitData;
	}

	public void setCostPerUnitData(String costPerUnitData) {
		this.costPerUnitData = costPerUnitData;
	}

	public String getUnitsCountData() {
		return unitsCountData;
	}

	public void setUnitsCountData(String unitsCountData) {
		this.unitsCountData = unitsCountData;
	}

	
	public Plan getSelectedPlan() {
		return selectedPlan;
	}

	public void setSelectedPlan(Plan selectedPlan) {
		this.selectedPlan = selectedPlan;
	}

	public BigDecimal getOldPlanProratedAmount() {
		return oldPlanProratedAmount;
	}

	public void setOldPlanProratedAmount(BigDecimal oldPlanProratedAmount) {
		this.oldPlanProratedAmount = oldPlanProratedAmount;
	}



	public BigDecimal getOldPlanOnlyRefundAmount() {
		return oldPlanOnlyRefundAmount;
	}

	public void setOldPlanOnlyRefundAmount(BigDecimal oldPlanOnlyRefundAmount) {
		this.oldPlanOnlyRefundAmount = oldPlanOnlyRefundAmount;
	}

	public boolean isBuyMoreAds() {
		return buyMoreAds;
	}

	public void setBuyMoreAds(boolean buyMoreAds) {
		this.buyMoreAds = buyMoreAds;
	}

	public Long getNumberOfBuyAds() {
		return numberOfBuyAds;
	}

	public void setNumberOfBuyAds(Long numberOfBuyAds) {
		this.numberOfBuyAds = numberOfBuyAds;
	}

	public BigDecimal getTotalBuyAdsCost() {
		return totalBuyAdsCost;
	}

	public void setTotalBuyAdsCost(BigDecimal totalBuyAdsCost) {
		this.totalBuyAdsCost = totalBuyAdsCost;
	}

	public List<SelectItem> getCardProcessorList() {
		return cardProcessorList;
	}

	public void setCardProcessorList(List<SelectItem> cardProcessorList) {
		this.cardProcessorList = cardProcessorList;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(String expiryDate) {
		log.info("setExpiryDate called with: "+expiryDate);
		this.expiryDate = expiryDate;
	}

	public String getCvv() {
		return cvv;
	}

	public void setCvv(String cvv) {
		this.cvv = cvv;
	}

	public String getSelectedCardProcessorId() {
		return selectedCardProcessorId;
	}

	public void setSelectedCardProcessorId(String selectedCardProcessorId) {
		this.selectedCardProcessorId = selectedCardProcessorId;
	}

	public boolean isPlanPaymentSucessFlag() {
		return planPaymentSucessFlag;
	}

	public void setPlanPaymentSucessFlag(boolean planPaymentSucessFlag) {
		this.planPaymentSucessFlag = planPaymentSucessFlag;
	}

	public boolean isRenewSubscription() {
		return renewSubscription;
	}

	public void setRenewSubscription(boolean renewSubscription) {
		this.renewSubscription = renewSubscription;
	}

	public Date getRenewStartDate() {
		return renewStartDate;
	}

	public void setRenewStartDate(Date renewStartDate) {
		this.renewStartDate = renewStartDate;
	}

	public Date getRenewEndDate() {
		return renewEndDate;
	}

	public void setRenewEndDate(Date renewEndDate) {
		this.renewEndDate = renewEndDate;
	}

	public BigDecimal getAdOnlyAdjustmentCost() {
		return adOnlyAdjustmentCost;
	}

	public void setAdOnlyAdjustmentCost(BigDecimal adOnlyAdjustmentCost) {
		this.adOnlyAdjustmentCost = adOnlyAdjustmentCost;
	}

	public BigDecimal getCurrentPlanAmount() {
		return currentPlanAmount;
	}

	public void setCurrentPlanAmount(BigDecimal currentPlanAmount) {
		this.currentPlanAmount = currentPlanAmount;
	}

	public BigDecimal getTotalOldPlanRefund() {
		return totalOldPlanRefund;
	}

	public void setTotalOldPlanRefund(BigDecimal totalOldPlanRefund) {
		this.totalOldPlanRefund = totalOldPlanRefund;
	}
	
	public boolean isUseDifferentCard() {
		log.info("Inside getUseDifferentCard: "+useDifferentCard);
		return useDifferentCard;
	}

	public void setUseDifferentCard(boolean pUseDifferentCard) {
		log.info("Inside setUseDifferentCard: "+pUseDifferentCard);
		this.useDifferentCard = pUseDifferentCard;
	}

	public boolean isCancelSubscription() {
		return cancelSubscription;
	}

	public void setCancelSubscription(boolean cancelSubscription) {
		this.cancelSubscription = cancelSubscription;
	}

	public Long getFreePlanId() {
		return new Long(AppInitializer.freePlanId);
	}

	public void setFreePlanId(Long freePlanId) {
		this.freePlanId = freePlanId;
	}

	public User getLoginUser() {
		return loginUser;
	}

	public void setLoginUser(User loginUser) {
		this.loginUser = loginUser;
	}

	
}
