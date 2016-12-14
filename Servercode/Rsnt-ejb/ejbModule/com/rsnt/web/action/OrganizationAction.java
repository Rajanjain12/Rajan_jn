package com.rsnt.web.action;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
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
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.codec.binary.Base64;
import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.remoting.WebRemote;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.log.Log;
import org.jboss.seam.web.IdentityRequestWrapper;

import com.rsnt.common.exception.RsntException;
import com.rsnt.common.service.ILookupService;
import com.rsnt.entity.AdUsageHistoryReport;
import com.rsnt.entity.Address;
import com.rsnt.entity.FeedbackQuestionaire;
import com.rsnt.entity.Lookup;
import com.rsnt.entity.Organization;
import com.rsnt.entity.OrganizationAdCredit;
import com.rsnt.entity.OrganizationAdCreditScheduler;
import com.rsnt.entity.OrganizationCategory;
import com.rsnt.entity.OrganizationOption;
import com.rsnt.entity.OrganizationPlanSubscription;
import com.rsnt.entity.OrganizationPlanSubscriptionInvoice;
import com.rsnt.entity.OrganizationUser;
import com.rsnt.entity.Plan;
import com.rsnt.entity.PlanFeature;
import com.rsnt.entity.PlanPrice;
import com.rsnt.entity.User;
import com.rsnt.entity.UserRole;
import com.rsnt.service.IOrganizationService;
import com.rsnt.service.IPlanService;
import com.rsnt.service.IStaffService;
import com.rsnt.util.common.AppInitializer;
import com.rsnt.util.common.CommonUtil;
import com.rsnt.util.common.Constants;
import com.rsnt.util.common.EmailUtil;
import com.rsnt.web.transferobject.FileUploadTO;
import com.rsnt.web.util.OrganizationImageUploadHelper;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;

//@Scope(ScopeType.PAGE)
@Scope(ScopeType.SESSION)
@Name("organizationAction")
//@Transactional(TransactionPropagationType.REQUIRED)
public class OrganizationAction {

	private Organization organization;
	private User user;
	
	@Logger
	private Log log;
	

	private List<SelectItem> currencyList = new ArrayList<SelectItem>();
	
	private String cardNumber;
	
	private String expiryDate;
	
	private String cvv;
	
	private Long selectedPlanId;
	private List<SelectItem> planList = new ArrayList<SelectItem>();
	
	private String selectedPlanPriceId;
	private List<SelectItem> planPriceList = new ArrayList<SelectItem>();
	
	@In(value=ILookupService.SEAM_NAME,create=true)
	private ILookupService lookupService;
	
	@In(value=IPlanService.SEAM_NAME,create=true)
	private IPlanService planService;
	
	@In(value=IStaffService.SEAM_NAME,create=true)
	private IStaffService staffService;
	
	@In(value=IOrganizationService.SEAM_NAME,create=true)
	private IOrganizationService organizationService;
	
	private String selectedPlanCost;
	
	private Plan selectedPlan;
	
	private PlanPrice selectedPlanPrice;
	
	private List<SelectItem> cardProcessorList = new ArrayList<SelectItem>();
	private String selectedCardProcessorId;
	private String selectedCardProcessorValue;
	
	private List<SelectItem> cuisineTypeChineseList = new ArrayList<SelectItem>();
	private List<SelectItem> cuisineTypeAsianList = new ArrayList<SelectItem>();
	private List<SelectItem> cuisineTypeEurList = new ArrayList<SelectItem>();
	private List<SelectItem> cuisineTypeNAList = new ArrayList<SelectItem>();
	private List<SelectItem> cuisineTypeOthersList = new ArrayList<SelectItem>();
	
	private List<SelectItem> dishTypeList = new ArrayList<SelectItem>();
	private List<SelectItem> rsntTypeList = new ArrayList<SelectItem>();
	private List<SelectItem> seatingTypeList = new ArrayList<SelectItem>();
	
	@In
    private Map messages;
	
	
	private BigDecimal unitsCount;
	private BigDecimal totalUnitsCost;
	
	
	private String orgCuisineData ;
	private String orgDishData;
	private String orgRsntTypeData;
	private String orgseatTypeData;
	
	private boolean planPaymentSucessFlag;
	
	private boolean checkInSpecial;
	private String checkInSpecialText;
	private boolean renderNoteFlag;
	
	private Long freePlanId;
	private String password;
	
	private boolean step1Complete;
	
	private String primaryPhoneStr;
	
	//Setup Logo
	private byte[] logoFile;
	
	public void initialize(){
		try{
			log.info("initalize() called");
			
			//Added by Aditya
			organizationImageUploadHelper.clearData();
			organizationImageUploadHelper.clearImageList();
			organizationImageUploadHelper.resetImageTypeList();
			deleteImgPathList = new ArrayList<String>();
			//End
			
			organization = new Organization();
			organization.setAddress(new Address());
			organization.getAddress().setCountry("USA");
			organization.setOrganizationUserList(new ArrayList<OrganizationUser>());
			organization.setOrganizationOptionList(new ArrayList<OrganizationOption>());
			organization.setOrganizationCategoryList(new ArrayList<OrganizationCategory>());
			organization.setOrganizationPlanSubscriptionList(new ArrayList<OrganizationPlanSubscription>());
			organization.setOrganizationAdCreditSet(new HashSet<OrganizationAdCredit>());
			
			user = new User();
			planList = new ArrayList<SelectItem>();
			
			List<Object[]> planLookupObjArr = lookupService.getPlanLookupList();

			for(Object[] obj : planLookupObjArr ){
				BigDecimal planId = new BigDecimal(obj[0].toString());
				planList.add(new SelectItem(planId.longValue(),obj[1].toString()));
			}
			planList.add(new SelectItem(0,"Select.."));
			
			this.setSelectedPlanId(new Long(0));
			
			initializeOrgAdditionalInformation();
			
		}
		catch(RsntException e){
			e.printStackTrace();
		}
	}
	
	// setup logo
	//Modified by Aditya on 12/07/2015
	public byte[] getVerifiedLogo() {
		byte[] logoFileCheck = null;
		String orgPath = AppInitializer.orgLogoPath;
		System.out.println("orgPath: " + orgPath);
		File fileLogo = null;
		try {
			//Changes to be reverted
			/*if (!organization.getLogoFileName().equals(null)
					|| !organization.getLogoFileName().equals("")) {
				fileLogo = new File(orgPath + "\\logo\\"
						+ organization.getLogoFileName());
			} else {
				fileLogo = new File(orgPath + "\\logo\\default-logo.jpg");
			}*/
			if (organization.getLogoFileName() != null
					&& !organization.getLogoFileName().equals("")) {
				fileLogo = new File(orgPath + File.separator + organization.getLogoFileName());
				System.out.println("Path File Logo: " + fileLogo.getAbsolutePath());
				logoFileCheck = readBytesFromFile(fileLogo);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println("Error in getLogoFile: " + e.getMessage());
			// e.printStackTrace();
			/*try {
				fileLogo = new File(orgPath + "\\logo\\default-logo.jpg");
				logoFileCheck = readBytesFromFile(fileLogo);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return null;
			}*/
		}
		return logoFileCheck;
	}
	
	public static byte[] readBytesFromFile(File file) throws IOException {
	      InputStream is = new FileInputStream(file);
	      
	      // Get the size of the file
	      long length = file.length();
	  
	      // You cannot create an array using a long type.
	      // It needs to be an int type.
	      // Before converting to an int type, check
	      // to ensure that file is not larger than Integer.MAX_VALUE.
	      if (length > Integer.MAX_VALUE) {
	        throw new IOException("Could not completely read file " + file.getName() + " as it is too long (" + length + " bytes, max supported " + Integer.MAX_VALUE + ")");
	      }
	  
	      // Create the byte array to hold the data
	      byte[] bytes = new byte[(int)length];
	  
	      // Read in the bytes
	      int offset = 0;
	      int numRead = 0;
	      while (offset < bytes.length && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
	          offset += numRead;
	      }
	  
	      // Ensure all the bytes have been read in
	      if (offset < bytes.length) {
	          throw new IOException("Could not completely read file " + file.getName());
	      }
	  
	      // Close the input stream and return bytes
	      is.close();
	      return bytes;
	}
	
	private void initializeOrgAdditionalInformation(){
		try{
			
			cuisineTypeChineseList = new ArrayList<SelectItem>();
			cuisineTypeAsianList = new ArrayList<SelectItem>();
			cuisineTypeEurList = new ArrayList<SelectItem>();
			cuisineTypeNAList = new ArrayList<SelectItem>();
			cuisineTypeOthersList = new ArrayList<SelectItem>();
			dishTypeList = new ArrayList<SelectItem>();
			rsntTypeList = new ArrayList<SelectItem>();
			seatingTypeList = new ArrayList<SelectItem>();
			List<Object[]> cuisineLookupObjArr = lookupService.getLookupsForLookupType(new Long(Constants.CONT_LOOKUPTYPE_CUISINETYPE));
			for(Object[] obj : cuisineLookupObjArr ){
				BigDecimal filterId = new BigDecimal(obj[2].toString());
				BigDecimal lookupId = new BigDecimal(obj[0].toString());
				if(filterId.intValue()==1){
					cuisineTypeChineseList.add(new SelectItem(lookupId.longValue(),obj[1].toString()));
					
				}else if(filterId.intValue()==2){
					cuisineTypeAsianList.add(new SelectItem(lookupId.longValue(),obj[1].toString()));
				}else if(filterId.intValue()==3){
					cuisineTypeEurList.add(new SelectItem(lookupId.longValue(),obj[1].toString()));
				}else if(filterId.intValue()==4){
					cuisineTypeNAList.add(new SelectItem(lookupId.longValue(),obj[1].toString()));
				}else if(filterId.intValue()==5){
					cuisineTypeOthersList.add(new SelectItem(lookupId.longValue(),obj[1].toString()));
				}
				
			}
			List<Object[]> dishLookupObjArr = lookupService.getLookupsForLookupType(new Long(Constants.CONT_LOOKUPTYPE_DISHTYPE));
			for(Object[] obj : dishLookupObjArr ){
				BigDecimal dishId = new BigDecimal(obj[0].toString());
				dishTypeList.add(new SelectItem(dishId.longValue(),obj[1].toString()));
			}
			
			List<Object[]> rsntTypeLookupObjArr = lookupService.getLookupsForLookupType(new Long(Constants.CONT_LOOKUPTYPE_RSNTTYPE));
			for(Object[] obj : rsntTypeLookupObjArr ){
				BigDecimal rsntTypeId = new BigDecimal(obj[0].toString());
				rsntTypeList.add(new SelectItem(rsntTypeId.longValue(),obj[1].toString()));
			}
			List<Object[]> seatTypeLookupObjArr = lookupService.getLookupsForLookupType(new Long(Constants.CONT_LOOKUPTYPE_SEATTYPE));
			for(Object[] obj : seatTypeLookupObjArr ){
				BigDecimal rsntTypeId = new BigDecimal(obj[0].toString());
				seatingTypeList.add(new SelectItem(rsntTypeId.longValue(),obj[1].toString()));
			}
			setCheckInSpecial(false);
			setRenderNoteFlag(false);
		}
		catch(RsntException e){
			e.printStackTrace();
		}
		
	}
	
	

	//TODO Setup Logo
	@WebRemote
	public void saveLogo(String imgstr) {
		log.info("saveLogo: "+imgstr);
		try {
			String imageDataBytes = imgstr.substring(imgstr.indexOf(",") + 1);
			byte[] imgByteArray = Base64
					.decodeBase64(imageDataBytes.getBytes());
			//logoFile
			this.setLogoFile(imgByteArray);
			//organization.setLogo(imgByteArray);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("Error in saveLogo: ", e.getMessage());
			e.printStackTrace();
		}
	}
	
	@WebRemote
	public String checkLogo(){
		log.info("checkLogo");
		if(this.getLogoFile()!=null){return "true";}
		else{return "false";}
	}
	//End Setup Logo
	
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
			if(!this.getSelectedPlanPriceId().equalsIgnoreCase("0")){
				selectedPlanPrice =  planService.getPlanPriceEntity(Long.valueOf(this.getSelectedPlanPriceId()));
				//planUnitList = new ArrayList<SelectItem>();
				//this.setSelectedPlanUnitId("");
				
				this.setSelectedPlanCost(selectedPlanPrice.getAmountPerUnit() + " " + selectedPlanPrice.getCurrency().getCode());
				if(this.getUnitsCount()!=null){
					this.setTotalUnitsCost(selectedPlanPrice.getAmountPerUnit().multiply(this.getUnitsCount()));
				}
			}
			
			//this.setPlanCost("100 USD");
		}
		catch(RsntException e){
			e.printStackTrace();
		} 
	}
	
	public void setCardProcessorValue(){
		 try{
			 if(this.getSelectedCardProcessorId()!=null){
				 Lookup cardLookupObj = lookupService.getLookupEntity(new Long(this.getSelectedCardProcessorId()));
				 this.setSelectedCardProcessorValue(cardLookupObj.getCode());
			 }
		 }
		 catch(RsntException e){
			 e.printStackTrace();
		 }
	}
	
	public String navigateToPaymentPage(){
		 return "paymentDetail";
	}
	
	public String navigateToLoginPage(){
		 return "success";
	}
	
	public void selectNextPage(){
		try{
			
			setStep1Complete(false);
			if(this.getUser().getEmail()!=null){
				List<User> userList = staffService.findUserByUserName(this.getUser().getEmail());
				if(userList!=null && userList.size()>0){
					//FacesMessages.instance().add("User Id already exists.Please choose a different User Id.");	
					final FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, messages
		                    .get("organization.user.duplicate").toString(), null);
		            FacesContext.getCurrentInstance().addMessage(null, message);
					return;
				}
			}
			//Setting the username from the email so that when the user is in the confir
			user.setUserName(user.getEmail());
			
			try{
				setOrgLocationData();
			}
			catch(RsntException e){
				if(e.getErrorNumber()!=null && e.getErrorNumber().intValue()==Constants.CONT_ERR_ADDRESS_PARIAL_MATCH){
					String suggestedAddress = e.getMessage();
					final FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,  messages
		                    .get("organization.address.validation").toString() + " Did you mean: "+suggestedAddress+" ?", null);
		            FacesContext.getCurrentInstance().addMessage(null, message);
					return;
				}
				else if(e.getErrorNumber()!=null && e.getErrorNumber().intValue()==Constants.CONT_ERR_ADDRESS_APPROX_MATCH){
					final FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, " Address provided is valid but approximate", null);
		            FacesContext.getCurrentInstance().addMessage(null, message);
					return;
				}
				else{
					final FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, messages
		                    .get("organization.address.validation").toString(), null);
		            FacesContext.getCurrentInstance().addMessage(null, message);
					return;
				}
				
				
			}
			if(this.getSelectedPlanId()!=null){
				
				selectedPlan = planService.getPlanEntity(this.getSelectedPlanId());
				if(this.getTotalUnitsCost().compareTo(BigDecimal.ZERO) == 1){
					cardProcessorList = new ArrayList<SelectItem>();
					
					List<Object[]> cardLookupObjArr = lookupService.getLookupsForLookupType(new Long(Constants.CONT_LOOKUPTYPE_CARDTYPE));

					for(Object[] obj : cardLookupObjArr ){
						BigDecimal cardId = new BigDecimal(obj[0].toString());
						cardProcessorList.add(new SelectItem(cardId.longValue(),obj[1].toString()));
					}
					if(this.getSelectedCardProcessorId()==null)this.setSelectedCardProcessorId(String.valueOf(Constants.CONT_LOOKUP_CARDTYPE_VISA));
					setCardProcessorValue();
				}
				
/*				if(selectedPlan.getPlanType().intValue() ==Constants.CONST_LOOKUP_PLANTYPE_PAID){
					
				}
*/				
			}
			setStep1Complete(true);
		}
		catch(RsntException e)
		{
			e.printStackTrace();
		}
		
	}
	
	public String navigateBackFromConfirmationPage(){
		try{
			if(this.getSelectedPlanId()!=null){
				
				selectedPlan = planService.getPlanEntity(this.getSelectedPlanId());
				
				if(this.getTotalUnitsCost().compareTo(BigDecimal.ZERO) ==1){
					return "paymentDetail";
				}
				/*if(selectedPlan.getPlanType().intValue() ==Constants.CONST_LOOKUP_PLANTYPE_PAID){
					
				}*/
				else{
					 return "createOrgPage";
				}
			
			}
		}
		catch(RsntException e){
			e.printStackTrace();
		}
		return null;
	}
	
	public String navigateToCreateOrgPage(){
		log.info("navigateToCreateOrgPage()");
		return "success";
	}
	
	public String saveNewOrganization(){
	
		try{
			saveOrganization();
			return "success";
		}
		catch(RsntException e){
			e.printStackTrace();
			final FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null);
            FacesContext.getCurrentInstance().addMessage(null, message);
            
			if(isPlanPaymentSucessFlag()){
				EmailUtil emailUtil = (EmailUtil) Component.getInstance(EmailUtil.class);
				try {
					emailUtil.prepareMessageAndSend(this.getUser().getUserName(),AppInitializer.defaultEmailSubject+" Plan Upgrade Failed and payment transaction status: "
								+this.isPlanPaymentSucessFlag(), "Plan Upgrade Failed");
				} catch (RsntException e1) {
					e1.printStackTrace();
				}
			}
		}
		return "error";
	}
	public void  saveOrganization() throws RsntException{
		
		log.info("saveOrganization()");
		
		OrganizationPlanSubscription organizationPlanSubscription = new OrganizationPlanSubscription();
		
		if(!(this.getSelectedPlan().getPlanId().intValue() == new Integer(AppInitializer.freePlanId))){
			String[] expDate = this.getExpiryDate().split("-");
			
			String customerId = CommonUtil.createCustomer(this.getCardNumber(),expDate[0], expDate[1], this.getCvv(),organization.getEmail() );
			log.info("StripeInfo:CustomerID :"+customerId +" createdfor Organization: "+organization.getEmail());
			String chargeId = null;
			try{
				chargeId =CommonUtil.processStripePayment(this.getTotalUnitsCost(),customerId);
				log.info("StripeInfo:Charge:Success Charge ID :"+chargeId +" createdfor Organization: "+organization.getEmail()+" UnitsCost:" +this.getTotalUnitsCost());
			}
			catch(StripeException e){
				e.printStackTrace();
				log.info("StripeInfo:Charge:Failure for Organization: "+organization.getEmail()+" UnitsCost:" +this.getTotalUnitsCost());
				throw new RsntException(e.getMessage(),e);
				
			}
			organization.setLastPaymentTrxDescription("NEW PLAN SUBSCRIPTION");
			organization.setLastPaymentTrxStatus("SUCCESS");
			organization.setStripeCustomerId(customerId);
			organizationPlanSubscription.setChargeId(chargeId);
		}
		else{
			//No charge for free plan
		}
		
			setPlanPaymentSucessFlag(false);
			String primaryPhone = this.getPrimaryPhoneStr().replaceAll("-", "");
			primaryPhone = primaryPhone.replace("(", "");
			primaryPhone = primaryPhone.replace(")", "");
			
			organization.setPrimaryPhone(Long.valueOf(primaryPhone));
			
			organizationPlanSubscription.setOrganization(this.getOrganization());
			organizationPlanSubscription.setPlan(this.getSelectedPlan());
			
			organizationPlanSubscription.setAmountPerUnit(selectedPlanPrice.getAmountPerUnit());
			organizationPlanSubscription.setUnitId(selectedPlanPrice.getUnit().getLookupId());
			organizationPlanSubscription.setCurrencyId(selectedPlanPrice.getCurrency().getLookupId());
			organizationPlanSubscription.setNumberOfUnits(this.getUnitsCount().longValue()); //TODO// Why??
			organizationPlanSubscription.setTotalAmount((this.getTotalUnitsCost()));//TODO //Why??
			//organizationPlanSubscription.setTotalAmountPaid((this.getTotalUnitsCost()));//TODO //Why??
			//organization.setOrganizationName("TODO");
			organizationPlanSubscription.setStartDate(new Date());
			organizationPlanSubscription.setEndDate(CommonUtil.getEndDate(this.getSelectedPlanPrice().getUnit().getLookupId(), 
					new Date(), this.getUnitsCount().intValue(),true));
			//organization.setAdsBalance(new BigDecimal(this.getTotalUnitsCost()));
			organizationPlanSubscription.setCostPerAd(selectedPlanPrice.getCostPerAd());
			organizationPlanSubscription.setNoOfAdsPerUnit(selectedPlanPrice.getNoOfAdsPerUnit());
			organizationPlanSubscription.setCreatedBy(Constants.CREATED_BY_SYSTEM);
			
			organization.setOrganizationTypeId(new Long(1));
			
			
			//setOrgLocationData();
			
			
			//organization.setActiveOrgPlanSubscription(organizationPlanSubscription);
			user.setUserName(user.getEmail()); // Need to check this. We need to the username same as the email of the user.
			
			OrganizationUser orgUser = new OrganizationUser();
			orgUser.setUser(user);
			orgUser.setOrganization(organization);
			
			UserRole userRole = new UserRole();
			userRole.setRoleId(new Long(Constants.CONT_LOOKUP_ROLE_RSNTADMIN));
			userRole.setUser(user);
			
			user.setUserRole(userRole);
			user.setActive(false);//Once activation is done it will be set to true.
			user.setActivationId(CommonUtil.randomString(50));
			user.setPassword(CommonUtil.encryptPassword(this.getPassword()));
			
			organization.getOrganizationUserList().add(orgUser);
			
			//We will not duplicate plan information to organization level. We will always lookup from master plan tables. 
			//So thats why below code is commented.
			
		/*	List<OrganizationOption> optList = lookupService.getDefaultOrgOptions();
			for(OrganizationOption option : optList){
				option.setOrganization(organization);
				organization.getOrganizationOptionList().add(option);
			}*/
			
			//Plan featureDataPlan= (Plan) planService.loadPlanDetail(selectedPlanId);
			
			/*for(PlanFeature feature : featureDataPlan.getPlanFeatureSet()){
				
				OrganizationOption option = new OrganizationOption();
				option.setOptionDescription(feature.get)
				if(feature.getFeatureId().intValue() == Constants.CONT_LOOKUPTYPE_FEATURE_ALERT){
					this.setPlanAlertData(this.getPlanAlertData()+feature.getChildFeatureId() +",");
					
				}
				else if(feature.getFeatureId().intValue() == Constants.CONT_LOOKUPTYPE_FEATURE_REPORT){
					this.setPlanReportData(this.getPlanReportData()+feature.getChildFeatureId()+",");
				}
				
				else if(feature.getFeatureId().intValue() == Constants.CONT_LOOKUPTYPE_FEATURE_FEEDBACK){
					this.setSelectedFeedbackId(feature.getChildFeatureId().toString());
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
			*/
			
			
			processOrganizationAdditionalData();
			
			
			
			organization.setAdsBalance(new BigDecimal(selectedPlanPrice.getNoOfAdsPerUnit()));
			
			//organization.getOrganizationAdCreditSet().add(organizationAdCredit);
			
			Set<PlanFeature> planFeatureSet =  selectedPlan.getPlanFeatureSet();
			
			Iterator<PlanFeature> planFeatureItr = planFeatureSet.iterator();
			
			FeedbackQuestionaire questionaire = new FeedbackQuestionaire();
			questionaire.setOrganization(organization);
			questionaire.setActive(	false);
			questionaire.setFeedbackQuestionaireTitle("Feedback Data");
			organization.setFeedbackQuestionaire(questionaire);
			
			while(planFeatureItr.hasNext()){
				PlanFeature feature = planFeatureItr.next();
				
				if(feature.getFeatureId().intValue() == Constants.CONT_LOOKUPTYPE_FEATURE_FEEDBACK){
					int feedbackTypeId = feature.getChildFeatureId().intValue();
					if(feedbackTypeId == Constants.CONT_LOOKUP_FEEDBACKTYPE_ADVANCED){
						questionaire.setActive(true);
					}
					
				}
			}
			
			setPlanPaymentSucessFlag(true);
			
			organization.getOrganizationPlanSubscriptionList().add(organizationPlanSubscription); //The Active thing below will do the job.
			
			
			//organization.setAdCreditScheduler(adCreditScheduler);
			organization.setActiveFlag(true);
			organization = organizationService.createUpdateOrganization(organization);
			
			organizationService.updateActiveOrganizationPlanSubscription(organization.getOrganizationId(), 
					organization.getOrganizationPlanSubscriptionList().get(0).getOrganizationPlanId());

			
			//Create the Invoice
			OrganizationPlanSubscriptionInvoice invoice = new OrganizationPlanSubscriptionInvoice();
			invoice.setOrganization(organization);
			invoice.setCurrentPlanId(selectedPlan.getPlanId());
			invoice.setCurrentPlanName(selectedPlan.getPlanName());
			invoice.setInvoiceDate(new Date());
			invoice.setAction("CREATE");
			invoice.setTotalActivePlanAmount(this.getTotalUnitsCost());
			invoice.setTotalAmount(this.getTotalUnitsCost());
			invoice.setCreatedBy(Constants.CREATED_BY_SYSTEM);
			
			planService.createOrgSubsInvoice(invoice);
			
			OrganizationAdCreditScheduler adCreditScheduler = new OrganizationAdCreditScheduler();
			adCreditScheduler.setOrganization(organization);
			adCreditScheduler.setNoOfAdsCredited(selectedPlanPrice.getNoOfAdsPerUnit());
			adCreditScheduler.setUnitId(selectedPlanPrice.getUnit().getLookupId());
			adCreditScheduler.setCreditEndDate(organizationPlanSubscription.getEndDate());
			adCreditScheduler.setNextScheduledDate(CommonUtil.getEndDate(this.getSelectedPlanPrice().getUnit().getLookupId(), 
					new Date(),1,false));
			adCreditScheduler.setCreatedBy(Constants.CREATED_BY_SYSTEM);
			adCreditScheduler.setOrganizationPlanId(
					organization.getOrganizationPlanSubscriptionList().get(0).getOrganizationPlanId());
			
			organizationService.createOrganizationAdScheduler(adCreditScheduler);
			
			OrganizationAdCredit organizationAdCredit = new OrganizationAdCredit();
			organizationAdCredit.setOrganization(organization);
			organizationAdCredit.setCostPerAd(selectedPlanPrice.getCostPerAd());
			organizationAdCredit.setCurrencyId(selectedPlanPrice.getCurrency().getLookupId());
			organizationAdCredit.setNoOfAds(selectedPlanPrice.getNoOfAdsPerUnit());
			organizationAdCredit.setOrganizationPlanId(organization.getOrganizationPlanSubscriptionList().get(0).getOrganizationPlanId());
			organizationAdCredit.setCreditType(new Long(Constants.AD_CREDIT_TYPE_FREE));
			//IF he has taken plan for two years then we need to give him the free ads monthly and not in one shot.
			//If he terminates before two years then on a prorated basis his ad cost will be charged.
			//organizationAdCredit.setTotalAmount((this.getTotalUnitsCost()));
			organizationAdCredit.setTransactionDate(new Date());
			organizationAdCredit.setCreatedBy(Constants.CREATED_BY_SYSTEM);
			
			organizationService.createOrganizationAdCredit(organizationAdCredit);
			
			Set<AdUsageHistoryReport> adUsageHistoryReportSet = new HashSet<AdUsageHistoryReport>();
			
			AdUsageHistoryReport adUsageHistoryReport = new AdUsageHistoryReport();
			adUsageHistoryReport.setOrganization(organization);
			adUsageHistoryReport.setAdsBalance(new Long(0));
			adUsageHistoryReport.setActivityRef("OPEN_BAL");
			adUsageHistoryReport.setActivityDesc("Beginning balance");
			adUsageHistoryReport.setActivityDate(new Date());
			adUsageHistoryReport.setActivitySeq(new Long(1));
			adUsageHistoryReportSet.add(adUsageHistoryReport);
			
			AdUsageHistoryReport adUsageHistoryReport2 = new AdUsageHistoryReport();
			adUsageHistoryReport2.setOrganization(organization);
			adUsageHistoryReport2.setAdsBalance(selectedPlanPrice.getNoOfAdsPerUnit());
			adUsageHistoryReport2.setActivityRef("PURC_PLAN");
			adUsageHistoryReport2.setActivityDesc("Purchase plan ("+selectedPlan.getPlanName()+")");
			adUsageHistoryReport2.setAmount(this.getTotalUnitsCost());
			adUsageHistoryReport2.setActivityDate(new Date());
			adUsageHistoryReport2.setNotes("Applied to CC on File");
			adUsageHistoryReport2.setActivitySeq(new Long(2));
			adUsageHistoryReportSet.add(adUsageHistoryReport2);
			
			organizationService.createOrganizationAdUsageHistory(adUsageHistoryReportSet);
			
			final IdentityRequestWrapper identityRequestWrapper = (IdentityRequestWrapper) FacesContext
	         .getCurrentInstance().getExternalContext().getRequest();
			 final HttpServletRequest request = (HttpServletRequest) identityRequestWrapper.getRequest();
			 
			 log.info("Check RequestURL: "+request.getRequestURL());
			 
			 String activationLink =  AppInitializer.domainNameUrl+request.getContextPath()+"/external/activationResult.seam?actId="+user.getActivationId();
			 String activationUrl = "<a href='"+activationLink+"'  target='_blank'>Click here</a>";
			 /*String appLinkAppleUrl = AppInitializer.appLinkAppleText+" "+"<a href='"+AppInitializer.appLinkApple+"'  target='_blank'>Click Here</a>";
			 String appLinkAndroidUrl = AppInitializer.appLinkAndroidText+" "+"<a href='"+AppInitializer.appLinkAndroid+"'  target='_blank'>Click Here</a>";
			 
			 String residualIncUrl = AppInitializer.residualIncText+" "+"<a href='"+AppInitializer.residualInc+"'  target='_blank'>Click Here</a>";*/
			 /*String appLinkAppleUrl = "<a href='"+AppInitializer.appLinkApple+"'  target='_blank'><img src=\""+AppInitializer.appLinkImg+"\"/></a>";
			 String appLinkAndroidUrl = "<a href='"+AppInitializer.appLinkAndroid+"'  target='_blank'><img src=\""+AppInitializer.androidLinkImg+"\"/></a>";
			*/
			//added by vruddhi
				String appLinkAppleUrl = "<a href='"+AppInitializer.appLinkApple+"'  target='_blank'><img src=\""+AppInitializer.domainNameUrl+AppInitializer.appImangeExtPath+"/"+AppInitializer.appLinkImg+"\"/></a>";
				 String appLinkAndroidUrl = "<a href='"+AppInitializer.appLinkAndroid+"'  target='_blank'><img src=\""+AppInitializer.domainNameUrl+AppInitializer.appImangeExtPath+"/"+AppInitializer.androidLinkImg+"\"/></a>";
				//ended by vruddhi
			 String msgBody = 	AppInitializer.rmsWelcome + activationUrl +"<br/><br/>" + appLinkAppleUrl + "<br/><br/>"
			 			+ appLinkAndroidUrl  +"<br/><br/>" + AppInitializer.rmsThankYou;
			 
			 String userEmail = !(AppInitializer.defaultUserEmail.equalsIgnoreCase(""))?AppInitializer.defaultUserEmail:user.getEmail();
			 log.info("Printing msgBody "+msgBody+" to be sent to "+userEmail);	
					
			 EmailUtil emailUtil = (EmailUtil) Component.getInstance(EmailUtil.class);
				try {
					emailUtil.prepareMessageAndSend(userEmail,AppInitializer.defaultEmailSubject+" Account Activation Link", msgBody);
				} catch (RsntException e) {
					e.printStackTrace();
				}
			
			//return "success";
			
		
		//return "error";
	}
	
	
	
	private String createCustomer() throws RsntException{
		Stripe.apiKey = AppInitializer.stripeApiKey;
        Map<String, Object> defaultCardParams = new HashMap<String, Object>();
        Map<String, Object> defaultCustomerParams = new HashMap<String, Object>();
    
        String[] expDate = this.getExpiryDate().split("-");
        
		defaultCardParams.put("number", this.getCardNumber());
		defaultCardParams.put("exp_month", expDate[0]);
		defaultCardParams.put("exp_year", expDate[1]);
		defaultCardParams.put("cvc_check", this.getCvv());

        
		defaultCustomerParams.put("card", defaultCardParams);
		defaultCustomerParams.put("description", organization.getEmail());
		
        try {
        	Customer cust =  Customer.create(defaultCustomerParams);
            System.out.println(cust);
            return cust.getId();
        } catch (StripeException e) {
            e.printStackTrace();
            throw new RsntException(e);
        }
        catch(Exception e){
        	e.printStackTrace();
        	throw new RsntException(e);
        }
	}
	
	public void dummyMethod(){
		
	}
	
	public String activateCustomer(){
		try{
			 final IdentityRequestWrapper identityRequestWrapper = (IdentityRequestWrapper) FacesContext
	         .getCurrentInstance().getExternalContext().getRequest();
			 final HttpServletRequest request = (HttpServletRequest) identityRequestWrapper.getRequest();
			 log.info("Printing Parameter actId+"+request.getParameter("actId"));
			 String actId = request.getParameter("actId");
			 log.info(request.getContextPath());
				 
			//String activationLink =  "http://ringmyserver.com"+request.getContextPath()+"/external/activationResult.seam?user=vish.riyer@gmail.com";
			//String activationUrl = "<a href='"+activationLink+"'  target='_blank'>Click here </a>";
			if(actId!=null){
				
				/* String appLinkAppleUrl = AppInitializer.appLinkAppleText+" "+"<a href='"+AppInitializer.appLinkApple+"'  target='_blank'>Click Here</a>";
				 String appLinkAndroidUrl = AppInitializer.appLinkAndroidText+" "+"<a href='"+AppInitializer.appLinkAndroid+"'  target='_blank'>Click Here</a>";
				 String residualIncUrl = AppInitializer.residualIncText+" "+"<a href='"+AppInitializer.residualInc+"'  target='_blank'>Click Here</a>";*/
				
				 String appLinkAppleUrl = "<a href='"+AppInitializer.appLinkApple+"'  target='_blank'><img src=\""+AppInitializer.domainNameUrl+AppInitializer.appImangeExtPath+"/"+AppInitializer.appLinkImg+"\"/></a>";
				 String appLinkAndroidUrl = "<a href='"+AppInitializer.appLinkAndroid+"'  target='_blank'><img src=\""+AppInitializer.domainNameUrl+AppInitializer.appImangeExtPath+"/"+AppInitializer.androidLinkImg+"\"/></a>";
			
				 String msgBody = AppInitializer.rmsActSuccess + AppInitializer.rmsActAddInfo+ "<br/><br/>"+appLinkAppleUrl + "<br/><br/>"
		 			+ appLinkAndroidUrl +"<br/><br/>" + AppInitializer.rmsThankYou;
					
				 
				 String userEmailDB = organizationService.getUserEmailFromActId(actId);
				
				 if(userEmailDB!=null){
					organizationService.activateUser(actId ,1 );
					
					//String userEmail = AppInitializer.defaultUserEmail!=null?AppInitializer.defaultUserEmail:userEmailDB;
					String userEmail = !(AppInitializer.defaultUserEmail.equalsIgnoreCase(""))?AppInitializer.defaultUserEmail:userEmailDB;
					log.info("Printing msgBody "+msgBody+" to be sent to "+userEmail);	
					EmailUtil emailUtil = (EmailUtil) Component.getInstance(EmailUtil.class);
					emailUtil.prepareMessageAndSend(userEmail,AppInitializer.defaultEmailSubject+" Activation is Complete ", msgBody);
					
				 }
				
			}
			
			 return "success";
		}
		catch(RsntException e){
			log.error(e);
		}
		return null;
	}

	/*private Date getEndDate(Integer pUnitsCount){
		
		final Calendar c1 = Calendar.getInstance();
		c1.setTime(new Date());
		
		
		if(this.getSelectedPlanPrice().getUnit().getLookupId().intValue() == Constants.CONT_LOOKUP_PLAN_UNIT_MONTH){
			c1.add(Calendar.MONTH, pUnitsCount);
		}
		else if(this.getSelectedPlanPrice().getUnit().getLookupId().intValue() == Constants.CONT_LOOKUP_PLAN_UNIT_YEAR){
			c1.add(Calendar.YEAR, pUnitsCount);
		}
		else if(this.getSelectedPlanPrice().getUnit().getLookupId().intValue() == Constants.CONT_LOOKUP_PLAN_UNIT_DAY){
			c1.add(Calendar.DATE, pUnitsCount);
		}
		
		return c1.getTime();
	}*/
	
	private void setOrgLocationData() throws RsntException{

		String address1 = organization.getAddress().getAddressLineOne();
		String address2 = organization.getAddress().getAddressLineTwo();
		String state = organization.getAddress().getState();
		String city= organization.getAddress().getCity();
		Long zip = organization.getAddress().getZipCode();
		String country = organization.getAddress().getCountry();
		
		StringBuffer addrStr = new StringBuffer(address1);
		//if(address2!=null)addrStr.append(","+address2);
		addrStr.append(","+city+","+state+" "+zip+","+country);
		
		JSONObject json = CommonUtil.getJSONByGoogle(addrStr.toString());
		organization.getAddress().setAddressLat(new BigDecimal(json.getDouble("lat")));
		organization.getAddress().setAddressLong(new BigDecimal(json.getDouble("lng")));
	}
	
	
	private List<OrganizationCategory> processOrganizationAdditionalData(){
		
		List<OrganizationCategory> orgCatList = new ArrayList<OrganizationCategory>();
		
		if(this.getOrgCuisineData()!=null && !this.getOrgCuisineData().equals("")){
			OrganizationCategory cat = null;
			String[] arr = this.getOrgCuisineData().split(",");
			for(String arrVar : arr ){
				cat = new OrganizationCategory();
				cat.setOrganization(organization);
				cat.setCategoryTypeId(new Long(Constants.CONT_LOOKUPTYPE_CUISINETYPE));
				cat.setCategoryValueId(new Long(arrVar));
				orgCatList.add(cat);
			}
		}
		
		if(this.getOrgDishData()!=null && !this.getOrgDishData().equals("")){
			OrganizationCategory cat = null;
			String[] arr = this.getOrgDishData().split(",");
			for(String arrVar : arr ){
				cat = new OrganizationCategory();
				cat.setOrganization(organization);
				cat.setCategoryTypeId(new Long(Constants.CONT_LOOKUPTYPE_DISHTYPE));
				cat.setCategoryValueId(new Long(arrVar));
				orgCatList.add(cat);
			}
		}
		
		if(this.getOrgRsntTypeData()!=null && !this.getOrgRsntTypeData().equals("")){
			OrganizationCategory cat = null;
			String[] arr = this.getOrgRsntTypeData().split(",");
			for(String arrVar : arr ){
				cat = new OrganizationCategory();
				cat.setOrganization(organization);
				cat.setCategoryTypeId(new Long(Constants.CONT_LOOKUPTYPE_RSNTTYPE));
				cat.setCategoryValueId(new Long(arrVar));
				orgCatList.add(cat);
			}
		}
		if(this.getOrgseatTypeData()!=null && !this.getOrgseatTypeData().equals("")){
			OrganizationCategory cat = null;
			String[] arr = this.getOrgseatTypeData().split(",");
			for(String arrVar : arr ){
				cat = new OrganizationCategory();
				cat.setOrganization(organization);
				cat.setCategoryTypeId(new Long(Constants.CONT_LOOKUPTYPE_SEATTYPE));
				cat.setCategoryValueId(new Long(arrVar));
				orgCatList.add(cat);
			}
		}
		if(this.isCheckInSpecial()){
			organization.setCheckinSpecialAvailable(true);
			organization.setCheckinSpecialNote(this.getCheckInSpecialText());
		}
		else{
			organization.setCheckinSpecialAvailable(false);
			organization.setCheckinSpecialNote(null);
		}
		
		return orgCatList;
	}
	
	public void renderNote(){
		if(this.isCheckInSpecial())setRenderNoteFlag(true);
		else setRenderNoteFlag(false);
	}
	
	public void updateOrganizationProfile(){
		try{
			
			
			try{
				setOrgLocationData();
			}
			catch(RsntException e){
				if(e.getErrorNumber()!=null && e.getErrorNumber().intValue()==Constants.CONT_ERR_ADDRESS_PARIAL_MATCH){
					String suggestedAddress = e.getMessage();
					final FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,  messages
		                    .get("organization.address.validation").toString() + " Did you mean: "+suggestedAddress+" ?" , null);
		            FacesContext.getCurrentInstance().addMessage(null, message);
					return;
				}
				else if(e.getErrorNumber()!=null && e.getErrorNumber().intValue()==Constants.CONT_ERR_ADDRESS_APPROX_MATCH){
					final FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, " Address provided is valid but approximate", null);
		            FacesContext.getCurrentInstance().addMessage(null, message);
					return;
				}
				else{
					final FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, messages
		                    .get("organization.address.validation").toString(), null);
		            FacesContext.getCurrentInstance().addMessage(null, message);
					return;
				}
				
				
			}
			String primaryPhone = this.getPrimaryPhoneStr().replaceAll("-", "");
			primaryPhone = primaryPhone.replace("(", "");
			primaryPhone = primaryPhone.replace(")", "");
			organization.setPrimaryPhone(Long.valueOf(primaryPhone));
			
			//organization.getOrganizationCategoryList().clear();
			List<OrganizationCategory>  catList = processOrganizationAdditionalData();
			
			//organization = organizationService.createUpdateOrganization(organization);
			System.out.println("====================================================================");
			System.out.println("====================================================================");
			System.out.println("DATA LOGO : " + this.getLogoFile()); //this.setLogoFile
			try {
				
				String logoFileName = organization.getOrganizationId()+".png";
				File someFile = new File(logoFileName);
				FileOutputStream fos = new FileOutputStream(someFile);
				fos.write(this.getLogoFile());
				fos.flush();
				fos.close();
				
				//saveLogoFileSystem(someFile, organization);
				//organization.setLogoFileName(logoFileName);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			finally {
				//Added by Aditya
				saveRichFacesLogoFile();
				//End
			}
			
			//organization = organizationService.createUpdateOrganization(organization);
			
			organization = organizationService.updateOrganizationCatData(organization, catList);
			
			final FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, messages
                    .get("organization.update.success").toString(), null);
            FacesContext.getCurrentInstance().addMessage(null, message);
            
		}
		catch(RsntException e){
			final FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, messages
                    .get("organization.update.error").toString(), null);
            FacesContext.getCurrentInstance().addMessage(null, message);
			
		}
	}
	
	//Added by Aditya
	private void saveRichFacesLogoFile() throws RsntException {
		// TODO Auto-generated method stub
		try{
			String orgPath = AppInitializer.orgLogoPath;
			//Changes to be reverted
			//String filePath = "C:\\Project Documents\\Kyobee\\"+ organization.getOrganizationId()+".png";
			String filePath = orgPath + File.separator + organization.getOrganizationId()+".png";
			List<FileUploadTO> imageTOList = organizationImageUploadHelper.getAdImageTOList();
			//System.out.println("inside saveRichFacesLogoFile filePath = "+filePath + " imageTOList size " + imageTOList.size());
			if(imageTOList != null && imageTOList.size()>0)
			{
				FileUploadTO logoImage = imageTOList.get(0);
				ImageIO.write(logoImage.getImageOut(),logoImage.getMime(),new File(filePath));
				organization.setLogoFileName(organization.getOrganizationId()+".png");
			}
			else
			{
				organization.setLogoFileName(null);
			}

		}
		catch(Exception e){
			throw new RsntException(e.getMessage());
		}
	}
	//End

	public void saveLogoFileSystem(File file, Organization organization) {
		try {
			System.out.println("Intro saveLogoFileSystem... " + file.getName());
			String orgPath = AppInitializer.appExtPathOrganization;
			System.out.println("orgPath: " + orgPath);
			File out = new File(orgPath + "\\logo\\" + organization.getOrganizationName()+".png");

			// Here BufferedInputStream is added for fast reading.
			copy(file, out);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);
    
        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }
	
	

	
	public String cancelOrganizationProfile(){
		organization = null;
		return "cancel";
	}
	
	
	public void updateOrgAddData(){
		//This is a dummy method.
		log.info("updateOrgAddData() called");
		log.info(this.getOrgCuisineData());
	}
	
	public void loadOrganizationProfile(){
		try{
			initializeOrgAdditionalInformation();
			orgCuisineData="";
			orgRsntTypeData="";
			orgDishData="";
			orgseatTypeData ="";
			
		//	this.organization = organizationService.getOrganization(Long.valueOf(Contexts.getSessionContext().get(Constants.CONST_ORGID).toString()));
			this.organization = organizationService.getOrganizationCatDetail(Long.valueOf(Contexts.getSessionContext().get(Constants.CONST_ORGID).toString()));
			
			//setup logo
			//this.setLogoFile(verifiedLogo());
			
			//Added by Aditya
			/*FileUploadTO fileUploadTO = new FileUploadTO();
			fileUploadTO.setData(this.getLogoFile());
			List<FileUploadTO> adImageTOList = new ArrayList<FileUploadTO>();
			adImageTOList.add(fileUploadTO);
			organizationImageUploadHelper.setAdImageTOList(adImageTOList );*/
			
			List<FileUploadTO> adImageTOList = null;
			byte[] verifiedLogo = getVerifiedLogo();
			if(verifiedLogo != null)
			{
				InputStream in = new ByteArrayInputStream(verifiedLogo);
				BufferedImage out = null;
				try {
					out = ImageIO.read(in);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				FileUploadTO imageTO = new  FileUploadTO();
				imageTO.setImageOut(out);
				imageTO.setAdImageId("tmp0");
				imageTO.setImageTypeId(organizationImageUploadHelper.getActiveUploadImageTypeId());
				imageTO.setMime("png");
				adImageTOList = new ArrayList<FileUploadTO>();
				adImageTOList.add(imageTO);
			}
			organizationImageUploadHelper.setAdImageTOList(adImageTOList);
			//End by Aditya
			
			for(OrganizationCategory cat : organization.getOrganizationCategoryList()){
				if(cat.getCategoryTypeId().intValue() == Constants.CONT_LOOKUPTYPE_CUISINETYPE){
					orgCuisineData = orgCuisineData + cat.getCategoryValueId()+",";
				}
				else if(cat.getCategoryTypeId().intValue() == Constants.CONT_LOOKUPTYPE_RSNTTYPE){
					orgRsntTypeData = orgRsntTypeData + cat.getCategoryValueId()+",";
				}
				else if(cat.getCategoryTypeId().intValue() == Constants.CONT_LOOKUPTYPE_DISHTYPE){
					orgDishData = orgDishData + cat.getCategoryValueId()+",";
				}
				else if(cat.getCategoryTypeId().intValue() == Constants.CONT_LOOKUPTYPE_SEATTYPE){
					orgseatTypeData = orgseatTypeData + cat.getCategoryValueId()+",";
				}
				
			}
			this.setPrimaryPhoneStr(String.valueOf(organization.getPrimaryPhone()));
			if(organization.isCheckinSpecialAvailable()){
				this.setCheckInSpecial(true);
				this.setCheckInSpecialText(organization.getCheckinSpecialNote());
				this.setRenderNoteFlag(true);
			}else{
				this.setCheckInSpecial(false);
				this.setRenderNoteFlag(false);
			}
		}
		catch(RsntException e){
			e.printStackTrace();
		}
		
	}
	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}


	public List<SelectItem> getPlanList() {
		return planList;
	}


	public void setPlanList(List<SelectItem> planList) {
		this.planList = planList;
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

	public Long getSelectedPlanId() {
		return selectedPlanId;
	}

	public void setSelectedPlanId(Long selectedPlanId) {
		this.selectedPlanId = selectedPlanId;
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



	public String getSelectedPlanPriceId() {
		return selectedPlanPriceId;
	}

	public void setSelectedPlanPriceId(String selectedPlanPriceId) {
		this.selectedPlanPriceId = selectedPlanPriceId;
	}

	public IPlanService getPlanService() {
		return planService;
	}

	public void setPlanService(IPlanService planService) {
		this.planService = planService;
	}

	
	public String getSelectedPlanCost() {
		return selectedPlanCost;
	}

	public void setSelectedPlanCost(String selectedPlanCost) {
		this.selectedPlanCost = selectedPlanCost;
	}

	public List<SelectItem> getPlanPriceList() {
		return planPriceList;
	}

	public void setPlanPriceList(List<SelectItem> planPriceList) {
		this.planPriceList = planPriceList;
	}

	public Plan getSelectedPlan() {
		return selectedPlan;
	}

	public void setSelectedPlan(Plan selectedPlan) {
		this.selectedPlan = selectedPlan;
	}

	public PlanPrice getSelectedPlanPrice() {
		return selectedPlanPrice;
	}

	public void setSelectedPlanPrice(PlanPrice selectedPlanPrice) {
		this.selectedPlanPrice = selectedPlanPrice;
	}

	public IOrganizationService getOrganizationService() {
		return organizationService;
	}

	public void setOrganizationService(IOrganizationService organizationService) {
		this.organizationService = organizationService;
	}

	public List<SelectItem> getCardProcessorList() {
		return cardProcessorList;
	}

	public void setCardProcessorList(List<SelectItem> cardProcessorList) {
		this.cardProcessorList = cardProcessorList;
	}

	public String getSelectedCardProcessorId() {
		return selectedCardProcessorId;
	}

	public void setSelectedCardProcessorId(String selectedCardProcessorId) {
		this.selectedCardProcessorId = selectedCardProcessorId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public IStaffService getStaffService() {
		return staffService;
	}

	public void setStaffService(IStaffService staffService) {
		this.staffService = staffService;
	}
	public String getSelectedCardProcessorValue() {
		return selectedCardProcessorValue;
	}

	public void setSelectedCardProcessorValue(String selectedCardProcessorValue) {
		this.selectedCardProcessorValue = selectedCardProcessorValue;
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

	

	public List<SelectItem> getCuisineTypeChineseList() {
		return cuisineTypeChineseList;
	}

	public void setCuisineTypeChineseList(List<SelectItem> cuisineTypeChineseList) {
		this.cuisineTypeChineseList = cuisineTypeChineseList;
	}

	public List<SelectItem> getCuisineTypeAsianList() {
		return cuisineTypeAsianList;
	}

	public void setCuisineTypeAsianList(List<SelectItem> cuisineTypeAsianList) {
		this.cuisineTypeAsianList = cuisineTypeAsianList;
	}

	public List<SelectItem> getCuisineTypeEurList() {
		return cuisineTypeEurList;
	}

	public void setCuisineTypeEurList(List<SelectItem> cuisineTypeEurList) {
		this.cuisineTypeEurList = cuisineTypeEurList;
	}

	public List<SelectItem> getCuisineTypeNAList() {
		return cuisineTypeNAList;
	}

	public void setCuisineTypeNAList(List<SelectItem> cuisineTypeNAList) {
		this.cuisineTypeNAList = cuisineTypeNAList;
	}

	public List<SelectItem> getCuisineTypeOthersList() {
		return cuisineTypeOthersList;
	}

	public void setCuisineTypeOthersList(List<SelectItem> cuisineTypeOthersList) {
		this.cuisineTypeOthersList = cuisineTypeOthersList;
	}

	public String getOrgCuisineData() {
		return orgCuisineData;
	}

	public void setOrgCuisineData(String orgCuisineData) {
		this.orgCuisineData = orgCuisineData;
	}

	public String getOrgDishData() {
		return orgDishData;
	}

	public void setOrgDishData(String orgDishData) {
		this.orgDishData = orgDishData;
	}

	public String getOrgRsntTypeData() {
		return orgRsntTypeData;
	}

	public void setOrgRsntTypeData(String orgRsntTypeData) {
		this.orgRsntTypeData = orgRsntTypeData;
	}

	public List<SelectItem> getDishTypeList() {
		return dishTypeList;
	}

	public void setDishTypeList(List<SelectItem> dishTypeList) {
		this.dishTypeList = dishTypeList;
	}

	public List<SelectItem> getRsntTypeList() {
		return rsntTypeList;
	}

	public void setRsntTypeList(List<SelectItem> rsntTypeList) {
		this.rsntTypeList = rsntTypeList;
	}

	public boolean isPlanPaymentSucessFlag() {
		return planPaymentSucessFlag;
	}

	public void setPlanPaymentSucessFlag(boolean planPaymentSucessFlag) {
		this.planPaymentSucessFlag = planPaymentSucessFlag;
	}
	public Long getFreePlanId() {
		return new Long(AppInitializer.freePlanId);
	}

	public void setFreePlanId(Long freePlanId) {
		this.freePlanId = freePlanId;
	}

	public boolean isCheckInSpecial() {
		return checkInSpecial;
	}

	public void setCheckInSpecial(boolean checkInSpecial) {
		this.checkInSpecial = checkInSpecial;
	}

	public String getCheckInSpecialText() {
		return checkInSpecialText;
	}

	public void setCheckInSpecialText(String checkInSpecialText) {
		this.checkInSpecialText = checkInSpecialText;
	}

	public boolean isRenderNoteFlag() {
		return renderNoteFlag;
	}

	public void setRenderNoteFlag(boolean renderNoteFlag) {
		this.renderNoteFlag = renderNoteFlag;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isStep1Complete() {
		return step1Complete;
	}

	public void setStep1Complete(boolean step1Complete) {
		this.step1Complete = step1Complete;
	}

	public String getPrimaryPhoneStr() {
		return primaryPhoneStr;
	}

	public void setPrimaryPhoneStr(String primaryPhoneStr) {
		this.primaryPhoneStr = primaryPhoneStr;
	}

	public List<SelectItem> getSeatingTypeList() {
		return seatingTypeList;
	}

	public void setSeatingTypeList(List<SelectItem> seatingTypeList) {
		this.seatingTypeList = seatingTypeList;
	}

	public String getOrgseatTypeData() {
		return orgseatTypeData;
	}

	public void setOrgseatTypeData(String orgseatTypeData) {
		this.orgseatTypeData = orgseatTypeData;
	}

	public byte[] getLogoFile() {
		return logoFile;
	}

	public void setLogoFile(byte[] logoFile) {
		this.logoFile = logoFile;
	}
	
	
	private String startTime;
	private String endTime;
	
	private String startDate;
	private String endDate;
	
	@In(value="organizationImageUploadHelper", create=true)
	private OrganizationImageUploadHelper organizationImageUploadHelper;

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public OrganizationImageUploadHelper getOrganizationImageUploadHelper() {
		return organizationImageUploadHelper;
	}

	public void setOrganizationImageUploadHelper(
			OrganizationImageUploadHelper organizationImageUploadHelper) {
		this.organizationImageUploadHelper = organizationImageUploadHelper;
	}
	
	
	//Added by Aditya
	private String selectedDelAdImgId;
	List<String> deleteImgPathList = new ArrayList<String>();
	
	public String getSelectedDelAdImgId() {
		return selectedDelAdImgId;
	}

	public void setSelectedDelAdImgId(String selectedDelAdImgId) {
		this.selectedDelAdImgId = selectedDelAdImgId;
	}

	//This function deletes the images and updates the Ad. In the first version. 
	//Delete was followed by saving the Ad itself.Removing the update now. 
	public String deleteAddonImages(){
		try{
			if(this.getSelectedDelAdImgId()!=null){
				String[] imgIdArr = this.getSelectedDelAdImgId().split(",");
				List<String> imgIdList = Arrays.asList(imgIdArr);

				for(String adImageIdVar: imgIdList){
					if(adImageIdVar!=""){
						FileUploadTO delUploadTO = organizationImageUploadHelper.getUploadTO(adImageIdVar);
						if(delUploadTO!=null){
							String filePath = delUploadTO.getAdImageUrl();
							if(filePath!=null){
								//adsDetailService.deleteAdImage(new Long(delUploadTO.getAdImageId()));
								//deleteFileOnServer(filePath);//
								//Vish: Physical Deletion will happen once the user updates the Ad. Else on get Ad the images will be retrieved back.
								deleteImgPathList.add(filePath);
							}
							//organizationImageUploadHelper.getAdImageTOList().remove(new Integer(str).intValue());
							organizationImageUploadHelper.removeUploadTO(delUploadTO);
						}
					}
				}
				//updateAdd();

			}
			return "success";
		}
		catch(Exception e){
			log.error("AdsDetailAction.deleteAddonImages()", e);
			final FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, messages
					.get("get.ad.failed").toString(), null);
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
		return null;
	}

}
