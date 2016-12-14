package com.rsnt.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.Remote;
import javax.ejb.Stateless;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

import com.rsnt.common.exception.RsntException;
import com.rsnt.entity.AdUsageHistoryReport;
import com.rsnt.entity.Address;
import com.rsnt.entity.Organization;
import com.rsnt.entity.OrganizationAdCredit;
import com.rsnt.entity.OrganizationAdCreditScheduler;
import com.rsnt.entity.OrganizationPlanSubscription;
import com.rsnt.entity.OrganizationPlanSubscriptionInvoice;
import com.rsnt.entity.OrganizationUser;
import com.rsnt.entity.Plan;
import com.rsnt.entity.PlanPrice;
import com.rsnt.service.IOrganizationService;
import com.rsnt.service.IPlanRenewService;
import com.rsnt.service.IPlanService;
import com.rsnt.util.common.AppInitializer;
import com.rsnt.util.common.CommonUtil;
import com.rsnt.util.common.Constants;
import com.rsnt.util.common.EmailUtil;
import com.rsnt.util.transferobject.AdUsageHistoryReportTO;
import com.rsnt.util.transferobject.AdUsageWrapperTO;
import com.rsnt.util.transferobject.EmailAttachment;
import com.stripe.exception.StripeException;

@Stateless
@Scope(ScopeType.STATELESS)
@Name(IPlanRenewService.SEAM_NAME)
@Remote(IPlanRenewService.class)
public class PlanRenewServiceImpl   implements IPlanRenewService {

	@Logger
	private Log log;
	
	@In(value=IOrganizationService.SEAM_NAME,create=true)
	private IOrganizationService organizationService;
	
	
	@In(value=IPlanService.SEAM_NAME,create=true)
	private IPlanService planService;
	
	public void runRemoteScheduler(){
		log.info("Remote method invoked");
		
		try{
			processAllOrgData();
			processReminders();
			processMonthlyStatement();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	private void processAllOrgData(){
		try{
			List<Object[]>  orgData = organizationService.getPlanSchedulerData();
			
			for(Object[] obj : orgData){
				String orgId = obj[0].toString();
				String orgPlanId = obj[1].toString();
				String stripeCustId = obj[2]!=null?obj[2].toString():null;
				boolean pAutoRenew = Boolean.valueOf(obj[3].toString());
				log.info("Starting job for OrgId: "+orgId+" orgPlanId: "+orgPlanId);
				try{
					if(pAutoRenew){
						log.info("Inside AutoRenew job for OrgId: "+orgId+" orgPlanId: "+orgPlanId);
						OrganizationPlanSubscription currentOrgPlanSub = planService.getOrganizationPlanSubscription(new Long(orgPlanId));
						
						//Plan selectedPlan = planService.getPlanEntity(currentOrgPlanSub.getPlan().getPlanId());
						//Date planStartDate = selectedOrgPlanSub.getStartDate();
										
						BigDecimal renewCost  = currentOrgPlanSub.getAmountPerUnit().multiply(new BigDecimal(currentOrgPlanSub.getNumberOfUnits()));
						log.info("Renew Cost For OrgId: "+orgId+" RenewCost: "+renewCost);
						//this.setTotalUnitsCost(renewCost);
						
						//Setting the old Id first. all further refunds happen on the same id.
						String chargeId =null;
						
						try{
							if(renewCost.compareTo(BigDecimal.ZERO)==1 && stripeCustId!=null)
								chargeId = CommonUtil.processStripePayment(renewCost,stripeCustId);
						}
						
						catch(StripeException e){
							e.printStackTrace();
						}
						log.info("Renew Cost Charged For OrgId: "+orgId+" ChargeId: "+chargeId);
						if(chargeId==null){
							//Free Plan By Force
							processFeeOrgSubscription(new Long(orgId), new Long(orgPlanId), true);
						}
						else{
							//Paid plan 
							processPaidPlanorgSubscription(new Long(orgId), new Long(orgPlanId), chargeId,renewCost);
							
						}
					}
					else{
						//Free Plan
						processFeeOrgSubscription(new Long(orgId), new Long(orgPlanId), false);
					}
				}
				catch(RsntException e){
					e.printStackTrace();
				}
			}
		
		}
		
		catch(RsntException e){
			e.printStackTrace();
		}
		
	}
	
	
	private void processPaidPlanorgSubscription(Long pOrgId, Long pOrgPlanId, String chargeId, BigDecimal renewCost) throws RsntException{
		
		OrganizationPlanSubscription currentOrgPlanSub = planService.getOrganizationPlanSubscription(new Long(pOrgPlanId));
		
		Date planEndDate = currentOrgPlanSub.getEndDate();
		Calendar renewStartDate = Calendar.getInstance();
		renewStartDate.setTime(planEndDate);
		renewStartDate.add(Calendar.DATE, 1);
		
		//this.setUnitsCount(new BigDecimal(selectedOrgPlanSub.getNumberOfUnits()));
		Date renewEndDate = CommonUtil.getEndDate(currentOrgPlanSub.getUnitId(), renewStartDate.getTime(),currentOrgPlanSub.getNumberOfUnits().intValue(),true);
		//this.setRenewEndDate(renewEndDate);

		
		//Plan selectedPlan = planService.getPlanEntity(currentOrgPlanSub.getp);//This is the free Plan
		
		Organization org = organizationService.getOrganizationDetail(new Long(pOrgId));
		
		
		OrganizationPlanSubscription newSubscription = new OrganizationPlanSubscription();
		newSubscription.setPlan(currentOrgPlanSub.getPlan());
		newSubscription.setOrganization(org);
		newSubscription.setChargeId(chargeId);
		
		newSubscription.setAmountPerUnit(currentOrgPlanSub.getAmountPerUnit());
		newSubscription.setUnitId(currentOrgPlanSub.getUnitId());
		newSubscription.setCurrencyId(currentOrgPlanSub.getCurrencyId());
		newSubscription.setNumberOfUnits(currentOrgPlanSub.getNumberOfUnits()); 
		newSubscription.setTotalAmount(renewCost);
		//newSubscription.setTotalAmountPaid(this.getCurrentPlanAdjustmentAmount());//This is the what the customer has to additionally pay.
		//organization.setOrganizationName("TODO");
		newSubscription.setStartDate(renewStartDate.getTime());
		newSubscription.setEndDate(renewEndDate);
		//organization.setAdsBalance(new BigDecimal(this.getTotalUnitsCost()));
		newSubscription.setCostPerAd(currentOrgPlanSub.getCostPerAd());
		newSubscription.setNoOfAdsPerUnit(currentOrgPlanSub.getNoOfAdsPerUnit());
		
		newSubscription.setCreatedBy("SYS");
		//org.getOrganizationPlanSubscriptionList().add(newSubscription);
		
		OrganizationPlanSubscriptionInvoice invoice = new OrganizationPlanSubscriptionInvoice();
		invoice.setOrganization(org);
		invoice.setPreviousPlanId(currentOrgPlanSub.getPlan().getPlanId());
		invoice.setPreviousPlanName(currentOrgPlanSub.getPlan().getPlanName());
		invoice.setCurrentPlanId(currentOrgPlanSub.getPlan().getPlanId());
		invoice.setCurrentPlanName(currentOrgPlanSub.getPlan().getPlanName());
		invoice.setInvoiceDate(new Date());
		invoice.setAction("RENEW");
		
		invoice.setTotalActivePlanAmount(renewCost);
		invoice.setTotalAmount(renewCost);
		
		OrganizationAdCreditScheduler adCreditScheduler =   org.getAdCreditScheduler();
		adCreditScheduler.setCreditEndDate(renewEndDate);
		
		
		org.setAdCreditScheduler(adCreditScheduler);
		
		org.setActiveOrgPlanSubscription(newSubscription);
		
		org.setAdsBalance(org.getAdsBalance().add(new BigDecimal(currentOrgPlanSub.getNoOfAdsPerUnit())));
		
		org = organizationService.createUpdateOrganization(org);
		
		OrganizationAdCredit organizationAdCredit = new OrganizationAdCredit();
		organizationAdCredit.setOrganization(org);
		organizationAdCredit.setCostPerAd(currentOrgPlanSub.getCostPerAd());
		organizationAdCredit.setCurrencyId(currentOrgPlanSub.getCurrencyId());
		organizationAdCredit.setNoOfAds(currentOrgPlanSub.getNoOfAdsPerUnit());
		organizationAdCredit.setTotalAmount(new BigDecimal(0));//Not Sure why this is required.
		organizationAdCredit.setCreditType(new Long(Constants.AD_CREDIT_TYPE_FREE));
		organizationAdCredit.setOrganizationPlanId(org.getActiveOrgPlanSubscription().getOrganizationPlanId());
		organizationAdCredit.setTransactionDate(new Date());
		
		organizationService.createOrganizationAdCredit(organizationAdCredit);
		
		//organizationService.createOrganizationPlanSubscription(newSubscription);
		
		currentOrgPlanSub.setTerminateDate(new Date());
		currentOrgPlanSub.setModifiedBy("BATCH");
		planService.updateOrgPlanSubscription(currentOrgPlanSub);
		
		planService.createOrgSubsInvoice(invoice);
	}
	
	//This method runs daily in the night and checks for organization expired and not renewed. 
	//This will automatically set them to free plan is autorenw flag for the org is not turned on.
	private void processFeeOrgSubscription(Long pOrgId, Long pOrgPlanId, boolean pPaymentFailure) throws RsntException{
		//Plan selectedPlan = planService.getPlanEntity(new Long(3));//This is the free Plan
		
		Plan selectedPlan = planService.getPlanEntity(new Long(AppInitializer.freePlanId));//This is the free Plan
		
		PlanPrice selectedPlanPrice  = planService.getFreePlanPriceEntity(selectedPlan.getPlanId());
		
		//PlanPrice selectedPlanPrice =  planService.getPlanPriceEntity(new Long(61));// This is the plan price for Year Unit which can be configured
		
		Organization org = organizationService.getOrganizationDetail(new Long(pOrgId));
		
		OrganizationPlanSubscription currentOrgPlanSub = planService.getOrganizationPlanSubscription(new Long(pOrgPlanId));
		
		Date planEndDate = currentOrgPlanSub.getEndDate();
		
		Calendar renewStartDate = Calendar.getInstance();
		renewStartDate.setTime(planEndDate);
		renewStartDate.add(Calendar.DATE, 1);
		
		Date renewEndDate = CommonUtil.getEndDate(selectedPlanPrice.getUnit().getLookupId(), renewStartDate.getTime(), 1,true);
		
		//Calendar renewEndDate = Calendar.getInstance();
		//renewEndDate.setTime(renewStartDate.getTime());
		//renewEndDate.add(Calendar.MONTH, 1);
		
		OrganizationPlanSubscription newSubscription = new OrganizationPlanSubscription();
		newSubscription.setPlan(selectedPlan);
		newSubscription.setOrganization(org);
		newSubscription.setChargeId(null);
		
		newSubscription.setAmountPerUnit(selectedPlanPrice.getAmountPerUnit());
		newSubscription.setUnitId(selectedPlanPrice.getUnit().getLookupId());// This is the free year
		newSubscription.setCurrencyId(selectedPlanPrice.getCurrency().getLookupId());
		newSubscription.setNumberOfUnits(new Long(1)); //This is currently hardcoded to 1
		newSubscription.setTotalAmount(new BigDecimal(0));
		//newSubscription.setTotalAmountPaid(this.getCurrentPlanAdjustmentAmount());//This is the what the customer has to additionally pay.
		//organization.setOrganizationName("TODO");
		newSubscription.setStartDate(renewStartDate.getTime());
		newSubscription.setEndDate(renewEndDate);
		//organization.setAdsBalance(new BigDecimal(this.getTotalUnitsCost()));
		newSubscription.setCostPerAd(selectedPlanPrice.getCostPerAd());
		newSubscription.setNoOfAdsPerUnit(selectedPlanPrice.getNoOfAdsPerUnit());
		newSubscription.setCreatedBy("SYS");
		
	
		//org.setAdCreditScheduler(adCreditScheduler);
		org.setActiveOrgPlanSubscription(newSubscription);
		
		if(pPaymentFailure){
			org.setLastPaymentTrxDescription("PLAN AUTO RENEW FAILURE");
			org.setLastPaymentTrxStatus("FAIL");
		}
		
		org.setAdsBalance(org.getAdsBalance().add(new BigDecimal(selectedPlanPrice.getNoOfAdsPerUnit())));
		
		org = organizationService.createUpdateOrganization(org);
		
		OrganizationAdCreditScheduler adCreditScheduler =   org.getAdCreditScheduler();
		adCreditScheduler.setCreditEndDate(renewEndDate);
		adCreditScheduler.setNoOfAdsCredited(selectedPlanPrice.getNoOfAdsPerUnit());
		adCreditScheduler.setOrganizationPlanId(org.getActiveOrgPlanSubscription().getOrganizationPlanId());
		adCreditScheduler.setUnitId(selectedPlanPrice.getUnit().getLookupId());
		
		organizationService.createOrganizationAdScheduler(adCreditScheduler);
		
		//organizationService.createOrganizationPlanSubscription(newSubscription);
		
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
		
		OrganizationPlanSubscriptionInvoice invoice = new OrganizationPlanSubscriptionInvoice();
		invoice.setOrganization(org);
		invoice.setPreviousPlanId(currentOrgPlanSub.getPlan().getPlanId());
		invoice.setPreviousPlanName(currentOrgPlanSub.getPlan().getPlanName());
		invoice.setCurrentPlanId(currentOrgPlanSub.getPlan().getPlanId());
		invoice.setCurrentPlanName(currentOrgPlanSub.getPlan().getPlanName());
		invoice.setInvoiceDate(new Date());
		invoice.setAction("RENEW");
		
		invoice.setTotalActivePlanAmount(new BigDecimal(0));
		invoice.setTotalAmount(new BigDecimal(0));
		
		currentOrgPlanSub.setTerminateDate(new Date());
		currentOrgPlanSub.setModifiedBy("BATCH");
		planService.updateOrgPlanSubscription(currentOrgPlanSub);
		
		planService.createOrgSubsInvoice(invoice);
	}
	
	//This method runs daily and sends email to Orgs expiring X days from today. This X is configured in rsnt.properties - renewReminderDays.
	public void processReminders() throws RsntException{
		try{
			List<Object[]>  orgData = organizationService.getPlanRenewReminderData();
			
			Plan selectedPlan = planService.getPlanEntity(new Long(AppInitializer.freePlanId));//This is the free Plan
			
			PlanPrice selectedPlanPrice  = planService.getFreePlanPriceEntity(selectedPlan.getPlanId());
			
			String adminEmail = null;
			List<String> staffEmailList = new ArrayList<String>();
			
			for(Object[] obj : orgData){
				String orgId = obj[0].toString();
				//String orgPlanId = obj[1].toString();
				
				Organization org = organizationService.getOrganizationDetail(new Long(orgId));
				log.info("Processing Reminder for OrgId: "+orgId + "; Org Name: "+org.getEmail());
				List<OrganizationUser> orgUserList =  org.getOrganizationUserList();
				for(OrganizationUser usr: orgUserList){
					if(usr.getUser().getUserRole().getRoleId().intValue()==Constants.CONT_LOOKUP_ROLE_RSNTADMIN)
						adminEmail = usr.getUser().getEmail();
					else 
						staffEmailList.add(usr.getUser().getEmail());
				}
				
				StringBuffer upgradePlanHdr = new StringBuffer("Dear Valued Customer, <br/>");
				upgradePlanHdr.append(AppInitializer.reminderText+"<br/><br/>"+AppInitializer.rmsThankYou);
				String upgradePlanHdrStr = upgradePlanHdr.toString();
				
				upgradePlanHdrStr = upgradePlanHdrStr.replace("YY", org.getAdsBalance().toString());
				upgradePlanHdrStr = upgradePlanHdrStr.replace("XXX", selectedPlanPrice.getNoOfAdsPerUnit().toString());
				
				String userEmail = !(AppInitializer.defaultUserEmail.equalsIgnoreCase(""))?AppInitializer.defaultUserEmail:adminEmail;
				 
			   /* String appLinkAppleUrl = AppInitializer.appLinkAppleText+" "+"<a href='"+AppInitializer.appLinkApple+"'  target='_blank'>Click Here</a>";
			    String appLinkAndroidUrl = AppInitializer.appLinkAndroidText+" "+"<a href='"+AppInitializer.appLinkAndroid+"'  target='_blank'>Click Here</a>";
					 
			    String residualIncUrl = AppInitializer.residualIncText+" "+"<a href='"+AppInitializer.residualInc+"'  target='_blank'>Click Here</a>";
					
			    String msgBody = upgradePlanHdrStr+"<br/><br/>"  + appLinkAppleUrl + "<br/><br/>"
		 			+ appLinkAndroidUrl +"<br/><br/>"+residualIncUrl +"<br/><br/>";*/
				
				 String appLinkAppleUrl = "<a href='"+AppInitializer.appLinkApple+"'  target='_blank'><img src=\""+AppInitializer.domainNameUrl+AppInitializer.appImangeExtPath+"/"+AppInitializer.appLinkImg+"\"/></a>";
				 String appLinkAndroidUrl = "<a href='"+AppInitializer.appLinkAndroid+"'  target='_blank'><img src=\""+AppInitializer.domainNameUrl+AppInitializer.appImangeExtPath+"/"+AppInitializer.androidLinkImg+"\"/></a>";
				 				
				
				 String msgBody = upgradePlanHdrStr+"<br/><br/>"  + appLinkAppleUrl + "<br/><br/>"
		 			+ appLinkAndroidUrl +"<br/><br/>"+AppInitializer.rmsThankYou;;
		            
				log.info("Printing msgBody "+msgBody+" to be sent to "+userEmail);	
	            
	            EmailUtil emailUtil = (EmailUtil) Component.getInstance(EmailUtil.class);
				try {
					emailUtil.prepareMessageAndSend(userEmail,AppInitializer.defaultEmailSubject+" Account Renewal Reminder Notice", msgBody);
				} catch (RsntException e) {
					e.printStackTrace();
				}
				
			}
			
		}
		catch(RsntException e){
			e.printStackTrace();
		}
		
	}
	
	//This method runs on the month end and sends email to Orgs with all the Ad Usage details for the month.
	public void processMonthlyStatement() throws RsntException{
		try{
			List<Object[]>  orgData = organizationService.getMonthlyReportData(new Long(AppInitializer.monthlyReportOrgIdStart));
			String adminEmail = null;
			List<String> staffEmailList = new ArrayList<String>();
			
			for(Object[] obj : orgData){
				String orgId = obj[0].toString();
				//String orgPlanId = obj[1].toString();
				List<Object[]> objArr = organizationService.getMonthlyReportOrgAdBalance(new Long(orgId));
				Integer adsBalanceBeforeReport = null;
				Integer currentSeq = 0;
				if(objArr!=null && objArr.size()>0){
					Object[] adDataArr = objArr.get(0);
					adsBalanceBeforeReport= (Integer)adDataArr[1];
					currentSeq= (Integer)adDataArr[0];
				}
				
				Organization org = organizationService.getOrganizationDetail(new Long(orgId));
				Address add = org.getAddress();
				
				log.info("Processing Montly Report for OrgId: "+orgId + "; Org Name: "+org.getEmail());
				List<OrganizationUser> orgUserList =  org.getOrganizationUserList();
				for(OrganizationUser usr: orgUserList){
					if(usr.getUser().getUserRole().getRoleId().intValue()==Constants.CONT_LOOKUP_ROLE_RSNTADMIN)
						adminEmail = usr.getUser().getEmail();
					else 
						staffEmailList.add(usr.getUser().getEmail());
				}
				
				if(!organizationService.checkAdUsageHistExists(new Long(orgId))){
					log.info("checkAdUsageHistExists False. So now Inserting for  "+orgId + "; Org Name: "+org.getEmail());
					Set<AdUsageHistoryReport> adUsageHistoryReportSet = new HashSet<AdUsageHistoryReport>();
					
					AdUsageHistoryReport adUsageHistoryReport = new AdUsageHistoryReport();
					adUsageHistoryReport.setOrganization(org);
					adUsageHistoryReport.setAdsBalance(org.getAdsBalance().longValue());
					if(adsBalanceBeforeReport!=null)
						adUsageHistoryReport.setAdsCredit(adsBalanceBeforeReport-org.getAdsBalance().longValue());//Ads Consumed: Total As before - Current Org Ad balance
					else 
						adUsageHistoryReport.setAdsCredit(new Long(0));
					
					adUsageHistoryReport.setActivityRef("CURR_MONTH_USAGE");
					adUsageHistoryReport.setActivityDesc("Current Month Ads Usage ("+org.getActiveOrgPlanSubscription().getPlan().getPlanName()+")");
					adUsageHistoryReport.setActivityDate(new Date());
					adUsageHistoryReport.setActivitySeq(new Long(++currentSeq));
					adUsageHistoryReportSet.add(adUsageHistoryReport);
					
					AdUsageHistoryReport adUsageHistoryReport2 = new AdUsageHistoryReport();
					adUsageHistoryReport2.setOrganization(org);
					adUsageHistoryReport2.setAdsBalance(org.getAdsBalance().longValue());
					//adUsageHistoryReport2.setAdsCredit(new Long(balanceAdDeductible));
					adUsageHistoryReport2.setActivityRef("END_BALANCE");
					adUsageHistoryReport2.setActivityDesc("Ending Balance");
					adUsageHistoryReport2.setActivityDate(new Date());
					adUsageHistoryReport2.setActivitySeq(new Long(++currentSeq));
					adUsageHistoryReportSet.add(adUsageHistoryReport2);
					
					AdUsageHistoryReport adUsageHistoryReport3 = new AdUsageHistoryReport();
					adUsageHistoryReport3.setOrganization(org);
					adUsageHistoryReport3.setAdsBalance(org.getAdsBalance().longValue());
					//adUsageHistoryReport3.setAdsCredit(selectedPlanPrice.getNoOfAdsPerUnit());
					adUsageHistoryReport3.setActivityRef("OPEN_BAL");
					adUsageHistoryReport3.setActivityDesc("Beginning Balance");
					adUsageHistoryReport3.setActivityDate(CommonUtil.getFirstDayOfNextMonth(new Date()));
					adUsageHistoryReport3.setActivitySeq(new Long(++currentSeq));
					adUsageHistoryReportSet.add(adUsageHistoryReport3);
					
					organizationService.createOrganizationAdUsageHistory(adUsageHistoryReportSet);
				}
				
				
				List<Object[]> adUsageHistoryReportSetDB =  organizationService.getAdUsageHistoryForOrg(org.getOrganizationId());
				
				AdUsageWrapperTO adUsageWrapperTO = new AdUsageWrapperTO(); 
				adUsageWrapperTO.setAddress(add.getAddressLineOne()+", "+add.getCity()+", "+add.getState()+", "+add.getZipCode());
				adUsageWrapperTO.setPlanName(org.getActiveOrgPlanSubscription().getPlan().getPlanName());
				String dateSpan = CommonUtil.convertDateToFormattedString(CommonUtil.getFirstDayOfMonth(), "yyyy/MM/dd") + " - "+ 
									CommonUtil.convertDateToFormattedString(CommonUtil.getLastDayOfMonth(), "yyyy/MM/dd");
				adUsageWrapperTO.setDateSpan(dateSpan);
				List<AdUsageHistoryReportTO> adUsageHistoryReportTOList= new ArrayList<AdUsageHistoryReportTO>(); 
				
				AdUsageHistoryReportTO adUsageHistoryReportTO =null;
				for(Object[] data: adUsageHistoryReportSetDB){
					adUsageHistoryReportTO= new AdUsageHistoryReportTO();
					
					adUsageHistoryReportTO.setActivityDate(CommonUtil.convertDateToFormattedString((Date)data[0],"MM/dd/yyyy"));
					adUsageHistoryReportTO.setActivityDesc((String)data[1]);
					if(data[2]!=null)adUsageHistoryReportTO.setAdsCredit(((Integer)data[2]).longValue());
					if(data[3]!=null)adUsageHistoryReportTO.setAdsBalance(((Integer)data[3]).longValue());
					if(data[4]!=null)adUsageHistoryReportTO.setAmount((BigDecimal)data[4]);
					if(data[5]!=null)adUsageHistoryReportTO.setNotes((String)data[5]);
					
					//BeanUtils.copyProperties(adUsageHistoryReportTO, data);
						
					adUsageHistoryReportTOList.add(adUsageHistoryReportTO);
				}
				adUsageWrapperTO.setAdUsageHistoryReportTOList(adUsageHistoryReportTOList);
				
				if(adUsageHistoryReportTOList.size()>0){
					log.info("Processing Histoy Data Report for OrgId: "+orgId + "; Org Name: "+org.getEmail());
					ByteArrayOutputStream boeOutput = new ByteArrayOutputStream();  
					CommonUtil.createPdfDocument(boeOutput, adUsageWrapperTO);
					
					EmailAttachment pdfAttachment = 
						new EmailAttachment(new ByteArrayInputStream(boeOutput.toByteArray()), orgId);
					
					StringBuffer upgradePlanHdr = new StringBuffer("Dear Valued Customer, <br/>");
					upgradePlanHdr.append(AppInitializer.monthlyStatementText+"<br/>");
					String upgradePlanHdrStr = upgradePlanHdr.toString();
					
					String userEmail = !(AppInitializer.defaultUserEmail.equalsIgnoreCase(""))?AppInitializer.defaultUserEmail:adminEmail;
					 
				    /*String appLinkAppleUrl = AppInitializer.appLinkAppleText+" "+"<a href='"+AppInitializer.appLinkApple+"'  target='_blank'>Click Here</a>";
				    String appLinkAndroidUrl = AppInitializer.appLinkAndroidText+" "+"<a href='"+AppInitializer.appLinkAndroid+"'  target='_blank'>Click Here</a>";
						*/
					//added by vruddhi
					String appLinkAppleUrl = "<a href='"+AppInitializer.appLinkApple+"'  target='_blank'><img src=\""+AppInitializer.domainNameUrl+AppInitializer.appImangeExtPath+"/"+AppInitializer.appLinkImg+"\"/></a>";
					 String appLinkAndroidUrl = "<a href='"+AppInitializer.appLinkAndroid+"'  target='_blank'><img src=\""+AppInitializer.domainNameUrl+AppInitializer.appImangeExtPath+"/"+AppInitializer.androidLinkImg+"\"/></a>";
					//ended by vruddhi 				
					
				    String residualIncUrl = AppInitializer.residualIncText+" "+"<a href='"+AppInitializer.residualInc+"'  target='_blank'>Click Here</a>";
						
				    String msgBody = upgradePlanHdrStr+"<br/><br/>"  + appLinkAppleUrl + "<br/><br/>"
			 			+ appLinkAndroidUrl +"<br/><br/>"+residualIncUrl +"<br/><br/>" + AppInitializer.rmsThankYou;; 
			            
					log.info("Printing msgBody "+msgBody+" to be sent to "+userEmail);	
		            
		            EmailUtil emailUtil = (EmailUtil) Component.getInstance(EmailUtil.class);
					try {
						emailUtil.prepareMessageAndSendWithPdfAttachment(userEmail,AppInitializer.defaultEmailSubject+" Monthly Statement", msgBody,pdfAttachment);
					} catch (RsntException e) {
						e.printStackTrace();
					}
					
				}
				
				
			}
			
		}
		catch(RsntException e){
			e.printStackTrace();
		}
		
	}

	public IOrganizationService getOrganizationService() {
		return organizationService;
	}

	public void setOrganizationService(IOrganizationService organizationService) {
		this.organizationService = organizationService;
	}

	public IPlanService getPlanService() {
		return planService;
	}

	public void setPlanService(IPlanService planService) {
		this.planService = planService;
	}
}
