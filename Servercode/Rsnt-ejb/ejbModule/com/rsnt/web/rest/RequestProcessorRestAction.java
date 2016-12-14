package com.rsnt.web.rest;

import ibt.ortc.api.Ortc;
import ibt.ortc.extensibility.OnConnected;
import ibt.ortc.extensibility.OrtcClient;
import ibt.ortc.extensibility.OrtcFactory;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import net.sf.json.JSONObject;

import org.apache.commons.beanutils.BeanUtils;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

import com.rsnt.common.exception.RsntException;
import com.rsnt.entity.Ads;
import com.rsnt.entity.AdsImage;
import com.rsnt.entity.OrganizationLayoutMarkerDeviceMap;
import com.rsnt.service.IClientAlertService;
import com.rsnt.service.IOrganizationService;
import com.rsnt.util.common.AppInitializer;
import com.rsnt.util.common.CommonUtil;
import com.rsnt.util.common.Constants;
import com.rsnt.util.transferobject.AdsDataTO;
import com.rsnt.util.transferobject.AdsImageTO;
import com.rsnt.web.util.RealtimefameworkPusher;

@Path("/requestProcessor")
@Name("requestProcessor")
public class RequestProcessorRestAction {


	
	@In(value=IOrganizationService.SEAM_NAME, create = true)
	private IOrganizationService organizationService;
	

	@Logger
	private Log log;
	
	@In(value=IClientAlertService.SEAM_NAME,create=true)
	private IClientAlertService clientAlertService;
	
	/*@GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/fetchOrgData")
    @Begin
	public String fetchOrgData(@QueryParam("orgLayoutMarkerId") final String orgLayoutMarkerId){
		
		final Map<String, Object> rootMap = new LinkedHashMap<String, Object>();
    	final List<String> errorArray = new ArrayList<String>(0);
    	final List<String> dataArray = new ArrayList<String>(0);
    	dataArray.add(Manager.instance().getCurrentConversationId());
    	dataArray.add(orgLayoutMarkerId);
    	rootMap.put("id", -1);
    	rootMap.put("error", "");
    	rootMap.put("fieldErrors", errorArray);
    	rootMap.put("data", dataArray);
    	rootMap.put("aaData","test");
    	
    	final List<Object> alertArray = new ArrayList<Object>();
    	final Map<String, Object> alertMap= new LinkedHashMap<String, Object>();
    	alertMap.put("2", "Call Server1");
    	alertMap.put("3", "Call Server2");
    	alertMap.put("4", "Call Server3");
    	alertArray.add(alertMap);
    	
    	final Map<String, Object> feedbackMap= new LinkedHashMap<String, Object>();
    	feedbackMap.put("3", "SimpleFeedback");
    	alertArray.add(feedbackMap);
    	
    	rootMap.put("featureData",alertArray);
    	
    	final JSONObject jsonObject = JSONObject.fromObject(rootMap);
    	log.info(jsonObject.toString());
    	return jsonObject.toString();
    	
	}*/
	
	/*@GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/fetchOrgData")
    @Begin
	public String fetchQueryOrgData(@QueryParam("orgLayoutMarkerId") final String orgLayoutMarkerId, @QueryParam("latData") final String latData, 
			@QueryParam("longData") final String longData){
		final Map<String, Object> rootMap = new LinkedHashMap<String, Object>();
    	final List<String> errorArray = new ArrayList<String>(0);
    	final List<Object> dataArray = new ArrayList<Object>(0);
    	
    	final List<Object> alertArray = new ArrayList<Object>(0);
    	final List<Object> feedBackArray = new ArrayList<Object>(0);
    	final List<Object> reportArray = new ArrayList<Object>(0);
    	
    	//Fetch the Alerts and the custome options for the Organization.
		try{
			
			if(CommonUtil.isNullOrEmpty(latData) || CommonUtil.isNullOrEmpty(longData)){
				rootMap.put("error", "Request received with invalid parameters or invalid location");
				rootMap.put("success", 0);
				final JSONObject jsonObject = JSONObject.fromObject(rootMap);
		    	return jsonObject.toString();
			}
			
			List<Object[]> cordData = clientAlertService.getOrgCords(new Long(orgLayoutMarkerId));
			if(cordData.size()>0){
				Object[] cordObj = cordData.get(0);
				
				if(cordObj[0]!=null && cordObj[1]!=null){
					Double lat = new BigDecimal(cordObj[0].toString()).doubleValue();
					Double lng= new BigDecimal(cordObj[1].toString()).doubleValue();
					if(CommonUtil.checkClientPromity(lat, lng, new Double(latData), new Double(longData))){
						//rootMap.put("error", "Successfully Processed");
					}
					else{
						rootMap.put("error", "Request received with invalid parameters or invalid location");
						rootMap.put("success", 0);
						final JSONObject jsonObject = JSONObject.fromObject(rootMap);
				    	return jsonObject.toString();
					}
				}
			}
			else{
				rootMap.put("error", "Request received with invalid parameters or invalid location");
				rootMap.put("success", 0);
				final JSONObject jsonObject = JSONObject.fromObject(rootMap);
		    	return jsonObject.toString();
			}
			
			List<Object[]> dataArr = organizationService.fetchOrganizationMobileData(new Long(orgLayoutMarkerId));
			
			for(Object[] obj : dataArr){
				HashMap<String, String> alertMap = new HashMap<String, String>();
				
				Long lookupTypeId = null;
				if(obj[2]!=null) lookupTypeId = new Long(obj[2].toString());
				
				alertMap.put("Id", obj[0].toString());
				alertMap.put("caption", obj[1].toString());
				alertMap.put("feature", "Alert");
				if(lookupTypeId!=null && lookupTypeId.intValue()==1)
					alertMap.put("iconUrl", "www.ringmyserver.com/images/"+obj[0].toString()+".png");
				else
					alertMap.put("iconUrl", "www.ringmyserver.com/images/99.png");

				dataArray.add(alertMap);
			}
			
			
			//Fetch the other data - Feedback and Special Add.
			List<Object[]> planDetailArr = organizationService.fetchOrganizationPlanMobileData(new Long(orgLayoutMarkerId));
			for(Object[] obj : planDetailArr){
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("id",obj[1].toString());
				map.put("caption", obj[2].toString());
				map.put("iconUrl", "www.ringmyserver.com/images/"+obj[1].toString()+".png");
				
				Integer lookupType = new Integer(obj[0].toString());
				if(lookupType.intValue()== Constants.CONT_LOOKUPTYPE_FEATURE_FEEDBACK){
					map.put("feature", "Feedback");
					dataArray.add(map);
				}
				else if(lookupType.intValue()== Constants.CONT_LOOKUPTYPE_FEATURE_SPECIALADS){
					map.put("feature", "SplAd");
					dataArray.add(map);
				}
				else if(lookupType.intValue()== Constants.CONT_LOOKUPTYPE_FEATURE_REPORT){
					map.put("feature", "Report");
					dataArray.add(map);
				}
				else if(lookupType.intValue()== Constants.CONT_LOOKUPTYPE_FEATURE_PROMOTIONALOFFERS){
					map.put("feature", "Promo");
					dataArray.add(map);
				}
			}
			rootMap.put("data", dataArray);
			rootMap.put("success", 1);
			
		}
		catch (Exception e) {
			
			log.error("OptionDetailRestAction.loadOptionData() - failed:", e.getMessage());
    		
    		rootMap.put("success", 0);
        	rootMap.put("error", e.getMessage());
        	
        	
		}
		
		final JSONObject jsonObject = JSONObject.fromObject(rootMap);
    	return jsonObject.toString();
	}*/
	
	//Once we get the parameters for device then the v1 method needs to be removed.
	@GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/fetchOrgData")
    @Begin
	public String fetchQueryOrgData(@QueryParam("orgLayoutMarkerId") final String orgLayoutMarkerId, @QueryParam("latData") final String latData, 
			@QueryParam("longData") final String longData, @QueryParam("deviceId") final String deviceId, @QueryParam("deviceMake") final String deviceMake,
			@QueryParam("sysDateTime") final String sysDateTime){
		final Map<String, Object> rootMap = new LinkedHashMap<String, Object>();
    	final List<String> errorArray = new ArrayList<String>(0);
    	final List<Object> dataArray = new ArrayList<Object>(0);
    	final List<Object> adArray = new ArrayList<Object>(0);
    	String currecyCode = null;
    	String checkInSplAvailable = null;
    	String checkInSplNote = null;
    /*	final List<Object> alertArray = new ArrayList<Object>(0);
    	final List<Object> feedBackArray = new ArrayList<Object>(0);
    	final List<Object> reportArray = new ArrayList<Object>(0);
    	*/
    	//Fetch the Alerts and the custome options for the Organization.
		try{
			
			if(CommonUtil.isNullOrEmpty(latData) || CommonUtil.isNullOrEmpty(longData)){
				rootMap.put("error", "Request received with invalid parameters or invalid location");
				rootMap.put("success", 0);
				final JSONObject jsonObject = JSONObject.fromObject(rootMap);
		    	return jsonObject.toString();
			}
			
			if(CommonUtil.isNullOrEmpty(deviceId) || CommonUtil.isNullOrEmpty(deviceMake)){
				rootMap.put("error", "Request received with invalid parameters or invalid Device Data");
				rootMap.put("success", 0);
				final JSONObject jsonObject = JSONObject.fromObject(rootMap);
		    	return jsonObject.toString();
			}
			
			
			
			List<Object[]> cordData = clientAlertService.getOrgCords(new Long(orgLayoutMarkerId));
			if(cordData.size()>0){
				Object[] cordObj = cordData.get(0);
				
				currecyCode = cordObj[2].toString();
				checkInSplAvailable = cordObj[3].toString();
				checkInSplNote = cordObj[4]!=null?cordObj[4].toString():null;
				
				if(cordObj[0]!=null && cordObj[1]!=null){
					Double lat = new BigDecimal(cordObj[0].toString()).doubleValue();
					Double lng= new BigDecimal(cordObj[1].toString()).doubleValue();
					if(CommonUtil.checkClientPromity(lat, lng, new Double(latData), new Double(longData))){
						//rootMap.put("error", "Successfully Processed");
					}
					else{
						rootMap.put("error", "Request received with invalid parameters or invalid location");
						rootMap.put("success", 0);
						final JSONObject jsonObject = JSONObject.fromObject(rootMap);
				    	return jsonObject.toString();
					}
				}
			}
			else{
				rootMap.put("error", "Request received with invalid parameters or invalid location");
				rootMap.put("success", 0);
				final JSONObject jsonObject = JSONObject.fromObject(rootMap);
		    	return jsonObject.toString();
			}
			
			//Create the Device Map
			
			OrganizationLayoutMarkerDeviceMap deviceMap = new OrganizationLayoutMarkerDeviceMap();
			deviceMap.setDeviceId(deviceId);
			deviceMap.setDeviceMake(deviceMake);
			deviceMap.setRequestDate(new Date());
			deviceMap.setOrganizationLayoutMarkerId(new Long(orgLayoutMarkerId));
			clientAlertService.registerDevice(deviceMap);
			
			List<Object[]> dataArr = organizationService.fetchOrganizationMobileData(new Long(orgLayoutMarkerId));
			
			for(Object[] obj : dataArr){
				HashMap<String, String> alertMap = new HashMap<String, String>();
				
				Long lookupTypeId = null;
				if(obj[2]!=null) lookupTypeId = new Long(obj[2].toString());
				
				alertMap.put("Id", obj[0].toString());
				alertMap.put("caption", obj[1].toString());
				alertMap.put("feature", "Alert");
				if(lookupTypeId!=null && lookupTypeId.intValue()==1)
					alertMap.put("iconUrl", AppInitializer.domainNameUrl+AppInitializer.appImangeExtPath+"/"+obj[0].toString()+".png");
				else
					alertMap.put("iconUrl", AppInitializer.domainNameUrl+AppInitializer.appImangeExtPath+"/99.png");

				dataArray.add(alertMap);
			}
			
			
			//Fetch the other data - Feedback and Special Add.
			List<Object[]> planDetailArr = organizationService.fetchOrganizationPlanMobileData(new Long(orgLayoutMarkerId));
			for(Object[] obj : planDetailArr){
				HashMap<String, String> map = new HashMap<String, String>();
				String idVal  = obj[2]!=null? obj[2].toString():obj[0].toString();
				String caption= obj[2]!=null? obj[3].toString():obj[1].toString();
				map.put("id",idVal);
				map.put("caption",caption);
				map.put("iconUrl", AppInitializer.domainNameUrl+AppInitializer.appImangeExtPath+"/"+idVal+".png");
				
				Integer lookupType = new Integer(obj[0].toString());
				if(lookupType.intValue()== Constants.CONT_LOOKUPTYPE_FEATURE_FEEDBACK){
					map.put("feature", "Feedback");
					dataArray.add(map);
				}
				else if(lookupType.intValue()== Constants.CONT_LOOKUPTYPE_FEATURE_SPECIALADS){
					map.put("feature", "SplAd");
					dataArray.add(map);
				}
				/*else if(lookupType.intValue()== Constants.CONT_LOOKUPTYPE_FEATURE_REPORT){
					map.put("feature", "Report");
					dataArray.add(map);
				}*/
				/*else if(lookupType.intValue()== Constants.CONT_LOOKUPTYPE_FEATURE_PROMOTIONALOFFERS){
					map.put("feature", "Promo");
					dataArray.add(map);
				}*/
			}
			
			//Fetch the Ads List for The Organization
			String[] dateObj = sysDateTime.split(" ");
			
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Date inputDate = df.parse(dateObj[0]);
			Calendar cal = Calendar.getInstance();
			cal.setTime(inputDate);
			final int week = cal.get(Calendar.DAY_OF_WEEK);
			
			List<AdsDataTO> adsList =  clientAlertService.getSpecialAdList(new Long(orgLayoutMarkerId), week, dateObj[0], dateObj[1].replaceAll(":",""));
			
			
			for(AdsDataTO dataTo : adsList){
				HashMap<String, String> adMap = new HashMap<String, String>();
				adMap.put("id", dataTo.getAdsId());
				String rsImagePath = dataTo.getImageUrl()!=null? dataTo.getImageUrl().replace(AppInitializer.appImgResourcePath, AppInitializer.domainNameUrl):null;
				adMap.put("image", rsImagePath);
				adMap.put("title", dataTo.getTitle());
				adArray.add(adMap);
			}
			
			/*if(adsList.size() >1){
				//dataArray.addAll(adsList);
				adMap.put("adData", adsList);
				rootMap.put("data", dataArray);
			}*/
			
			rootMap.put("data", dataArray);
			rootMap.put("adData", adArray);
			rootMap.put("CUR", currecyCode);
			rootMap.put("CheckInSpl", checkInSplAvailable);
			rootMap.put("CheckInSplNote", checkInSplNote);
			rootMap.put("success", 1);
			
		}
		catch (Exception e) {
			
			log.error("OptionDetailRestAction.loadOptionData() - failed:", e.getMessage());
    		
    		rootMap.put("success", 0);
        	rootMap.put("error", e.getMessage());
        	
        	
		}
		
		final JSONObject jsonObject = JSONObject.fromObject(rootMap);
    	return jsonObject.toString();
	}
	
	//Vish : This method will be called when the user from the app clicks on any layout options - Call Server. Call Water etc.
	@GET
    @Path("/registerAlert")
    @Produces(MediaType.APPLICATION_JSON)
	public String registerLayoutMarkerAlert(@QueryParam("orgLayoutMarkerId") final String orgLayoutMarkerId,
			@QueryParam("alertId") final String alertId, @QueryParam("latData") final String latData, @QueryParam("longData") final String longData,
			 @QueryParam("deviceId") final String deviceId,@QueryParam("deviceMake") final String deviceMake){
		final Map<String, Object> rootMap = new LinkedHashMap<String, Object>();
	    final List<String> btnIdList = new ArrayList<String>();
		 JSONObject jsonObject = null;
		try{
			List<Object[]> cordData = clientAlertService.getOrgCords(new Long(orgLayoutMarkerId));
			if(cordData.size()>0){
				Object[] cordObj = cordData.get(0);
				
				if(cordObj[0]!=null && cordObj[1]!=null){
					Double lat = new BigDecimal(cordObj[0].toString()).doubleValue();
					Double lng= new BigDecimal(cordObj[1].toString()).doubleValue();
					if(CommonUtil.checkClientPromity(lat, lng, new Double(latData), new Double(longData))){
						
						clientAlertService.checkDuplicateDashBoardAlert(new Long(orgLayoutMarkerId),new Long(alertId), deviceId);
						
						clientAlertService.processOrgMarkerDashBoardAlert(new Long(orgLayoutMarkerId),new Long(alertId), deviceId, deviceMake);
						
						/*
						 * on success we need to call realtime api and publish the code.
						 */
						
				    	  List<Object[]> alertDataList = clientAlertService.getDashBoardAlertData(new Long(cordObj[6].toString()));
				    	  for(Object[] inn: alertDataList){
				    			//btnIdList.add(obj[0].toString()+"-"+obj[1].toString());
				    			btnIdList.add(inn[0]+"-"+inn[1]);
				    		}   
				    	  
				    	
				
				    
				      rootMap.put("data", "Sent!");
					  rootMap.put("success", 1);
					  rootMap.put("aaData",btnIdList); 
					  rootMap.put("orgLayoutMarkerId", orgLayoutMarkerId);
					  rootMap.put("alertId",alertId);
					  
					 System.out.println((JSONObject.fromObject(rootMap)).toString());
					  Ortc api = new Ortc();
			          OrtcFactory factory = null;
						try {
							factory = api.loadOrtcFactory("IbtRealtimeSJ");
						} catch (InstantiationException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (ClassNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			             
			           
			           
					 RealtimefameworkPusher.client = factory.createClient();
					 RealtimefameworkPusher.client.setClusterUrl(RealtimefameworkPusher.serverUrl);
					 RealtimefameworkPusher.client.setConnectionMetadata("UserConnectionMetadata"); 
					
						 RealtimefameworkPusher.client.onConnected = new OnConnected() {
							    @Override
							    public void run(OrtcClient sender) {
							        // Messaging client is connected
							    	 RealtimefameworkPusher.client.send(AppInitializer.dashBoardChannel,  (JSONObject.fromObject(rootMap)).toString());
							      System.out.println("messge send");
							    }
							}; 

						 RealtimefameworkPusher.client.connect(RealtimefameworkPusher.applicationKey, RealtimefameworkPusher.authenticationToken);
						 System.out.println("done");
						 //RealtimefameworkPusher.client.send(RealtimefameworkPusher.channel, (JSONObject.fromObject(rootMap)).toString());
					 /*}
					 else
					 {
						 RealtimefameworkPusher.client.connect(RealtimefameworkPusher.applicationKey, RealtimefameworkPusher.authenticationToken);
						 RealtimefameworkPusher.client.send(RealtimefameworkPusher.channel, (JSONObject.fromObject(rootMap)).toString());
					 } */
				  // RealtimefameworkPusher.client.send(RealtimefameworkPusher.channel, (JSONObject.fromObject(rootMap)).toString());
				    	 
			
		         
				    	/*rootMap.put("id", -1);
				    	rootMap.put("aaData",btnIdList );    	
				    	System.out.println("rootmapdata::"+rootMap);    	    	             
				    	System.out.println("rootmapdata::"+rootMap);
				    	
				    	final JSONObject jsonObject = JSONObject.fromObject(rootMap);
				    	return jsonObject.toString();*/
				    	 
					}
					else{
						rootMap.put("success", 0);
						rootMap.put("error", "System Error - Client proximity Check Fail");
					}
				}
				else{
					rootMap.put("success", 0);
					rootMap.put("error", "System Error - Client proximity Check Fail");
				}
				
			}
			else{
				//Unable to process Request
				rootMap.put("success", 0);
				rootMap.put("error", "System Error Client proximity check fail");

			}
			
		}
		catch(Exception e){
		e.printStackTrace();
		log.error("OptionDetailRestAction.loadOptionData() - failed:", e.getMessage());
    		
    		rootMap.put("success", 0);
        	rootMap.put("error", e.getMessage());
		}
		 jsonObject = new JSONObject();
		 jsonObject = JSONObject.fromObject(rootMap);
    	return jsonObject.toString();
	}
	
	private boolean checkClientMarkerProximity(String orgLayoutMarkerId, Double reqLat2,Double reqLng2){
		
		try{
			List<Object[]> cordData = clientAlertService.getOrgCords(new Long(orgLayoutMarkerId));
			if(cordData.size()>0){
				Object[] cordObj = cordData.get(0);
				
				if(cordObj[0]!=null && cordObj[1]!=null){
					Double lat = new BigDecimal(cordObj[0].toString()).doubleValue();
					Double lng= new BigDecimal(cordObj[1].toString()).doubleValue();
					if(!CommonUtil.checkClientPromity(lat, lng, new Double(reqLat2), new Double(reqLng2))){
						
						return false;
					}
					else{
						return true;
					}
					
				}
				else{
					return false;
				}
				
			}
			else{
				//Unable to process Request
				return false;

			}
		}
		catch(RsntException e){
			log.error("OptionDetailRestAction.checkClientMarkerProximity() - failed:", e.getMessage());
			return false;
		}
	}
	
	/*@GET
    @Path("/registerMarkerDevice")
    @Produces(MediaType.APPLICATION_JSON)
	public String registerLayoutMarkerDevice(@QueryParam("orgLayoutMarkerId") final String orgLayoutMarkerId,
			@QueryParam("deviceId") final String deviceId,@QueryParam("deviceMake") final String deviceMake,
			@QueryParam("latData") final String latData, @QueryParam("longData") final String longData){
		final Map<String, Object> rootMap = new LinkedHashMap<String, Object>();
	}*/
	
	@GET
    @Path("/getSpecialAdList")
    @Produces(MediaType.APPLICATION_JSON)
	public String getSpecialAdList(@QueryParam("orgLayoutMarkerId") final String orgLayoutMarkerId,@QueryParam("sysDateTime") final String sysDateTime,
			@QueryParam("latData") final String latData,@QueryParam("longData") final String longData) {

		
		final Map<String, Object> rootMap = new LinkedHashMap<String, Object>();
		final List<Object> dataArray = new ArrayList<Object>(0);
		final List<Object> adDataArray = new ArrayList<Object>(0);
		try {
			if(!checkClientMarkerProximity(orgLayoutMarkerId, Double.valueOf(latData), Double.valueOf(longData)))
			{
				rootMap.put("error", "Request received with invalid parameters or invalid location");
				rootMap.put("success", 0);
				final JSONObject jsonObject = JSONObject.fromObject(rootMap);
		    	return jsonObject.toString();
			}
			
			String[] dateObj = sysDateTime.split(" ");
			
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Date inputDate = df.parse(dateObj[0]);
			Calendar cal = Calendar.getInstance();
			cal.setTime(inputDate);
			final int week = cal.get(Calendar.DAY_OF_WEEK);
			
			List<Integer> adOrgBalList =  organizationService.fetchOrganizationAdBalance(new Long(orgLayoutMarkerId), new Long(AppInitializer.adAccessCharge));
			if(adOrgBalList!=null && adOrgBalList.size()>0 ){
				List<AdsDataTO> adsList =  clientAlertService.getSpecialAdList(new Long(orgLayoutMarkerId), week, dateObj[0], dateObj[1].replaceAll(":",""));
			
				List<Long> adsIdList = new ArrayList<Long>();
				if(adsList.size() >0){
					
					for(AdsDataTO dataTO : adsList){
						adsIdList.add(new Long(dataTO.getAdsId()));
					}
					List<Ads> adsObjList =  clientAlertService.getAdDetails(adsIdList);
					
					for(Ads ad : adsObjList){
						AdsDataTO dataTo = new AdsDataTO();
						BeanUtils.copyProperties(dataTo, ad);
						for(AdsImage amg : ad.getAdsImageList()){
							AdsImageTO imageTO = new AdsImageTO();
							imageTO.setAdsImageId(amg.getAdsImageId().toString());
							
							String rsImagePath = amg.getImage()!=null? amg.getImage().replace(AppInitializer.appImgResourcePath, AppInitializer.domainNameUrl):null;
							imageTO.setImage(rsImagePath);
							imageTO.setImageTypeId(amg.getImageTypeId().toString());
							dataTo.getAdsImageTOList().add(imageTO);
						}
						adDataArray.add(dataTo);
					}
					organizationService.updateAdAccessedTimestamp(new Long(orgLayoutMarkerId), new Long(adOrgBalList.get(0)));
				}	
			}
			else{
				rootMap.put("success", 0);
	        	rootMap.put("error", "Invalid Ad Balance");
			}
			
			
			
			/*for(Ads ad :adsObjList){
				Map<String, String>  adsMap = new HashMap<String, String>();
				
				adsMap.put("id", ad.getAdsId().toString());
				adsMap.put("caption", ad.getCaption());
				adDataArray.add(adsMap);
				
			}*/
			
			//dataArray.addAll(adsList);
			
			rootMap.put("data", adDataArray);
			//rootMap.put("adData", adDataArray);
			rootMap.put("success", 1);
			//}
			
			
		} catch (RsntException e) {
			rootMap.put("success", 0);
        	rootMap.put("error", e.getMessage());
			e.printStackTrace();
		}
		catch(Exception e){
			rootMap.put("success", 0);
        	rootMap.put("error", e.getMessage());
			e.printStackTrace();
		}
		final JSONObject jsonObject = JSONObject.fromObject(rootMap);
    	return jsonObject.toString();
		
	}


	/*@GET
    @Path("/getSpecialAdDetail")
    @Produces(MediaType.APPLICATION_JSON)
	public String getSpecialAdDetail(@QueryParam("orgLayoutMarkerId") final String orgLayoutMarkerId,@QueryParam("adId") final String pAdId,
			@QueryParam("latData") final String latData,@QueryParam("longData") final String longData) {
		final Map<String, Object> rootMap = new LinkedHashMap<String, Object>();
		final List<Object> dataArray = new ArrayList<Object>(0);
		try {
			if(!checkClientMarkerProximity(orgLayoutMarkerId, Double.valueOf(latData), Double.valueOf(longData)))
			{
				rootMap.put("error", "Request received with invalid parameters or invalid location");
				rootMap.put("success", 0);
				final JSONObject jsonObject = JSONObject.fromObject(rootMap);
		    	return jsonObject.toString();
			}
			
			if(!CommonUtil.isNullOrEmpty(pAdId)){
				List<Integer> adOrgBalList =  organizationService.fetchOrganizationAdBalance(new Long(orgLayoutMarkerId), new Long(AppInitializer.adAccessCharge));
				if(adOrgBalList!=null && adOrgBalList.size()>0 ){
					Ads ad = clientAlertService.getSpecialAdDetail(new Long(pAdId));
					if(ad!=null){
						AdsDataTO adsDataTO = new AdsDataTO();
						adsDataTO.setAdsId(ad.getAdsId().toString());
						adsDataTO.setCaption(ad.getCaption());
						adsDataTO.setDescription(ad.getDescription());
						adsDataTO.setTitle(ad.getTitle());
						//adsDataTO.setPrice(String.valueOf(ad.getOriginalPrice()));
						//if(ad.getAdsImage()!=null)adsDataTO.setImageUrl(ad.getAdsImage().getImage());
						dataArray.add(adsDataTO);
						rootMap.put("data", dataArray);
						rootMap.put("success", 1);
						
						//organizationService.deductOrgAdBalance(new Long(adOrgBalList.get(0)));
						organizationService.updateAdAccessedTimestamp(new Long(orgLayoutMarkerId), new Long(adOrgBalList.get(0)));
						//clientAlertService.saveAdAnalytics(new Long(orgLayoutMarkerId), new Long(pAdId), new Long(adOrgBalList.get(0)));
					}
					else{
						rootMap.put("success", 0);
			        	rootMap.put("error", "Invalid Ad Data Request");
					}
					
				}
				else{
					rootMap.put("success", 0);
		        	rootMap.put("error", "Invalid Ad Balance");
				}
				
			}
		}
		catch(Exception e){
			rootMap.put("success", 0);
        	rootMap.put("error", e.getMessage());
			e.printStackTrace();
		}
		final JSONObject jsonObject = JSONObject.fromObject(rootMap);
    	return jsonObject.toString();
	}*/
	public IOrganizationService getOrganizationService() {
		return organizationService;
	}

	public void setOrganizationService(IOrganizationService organizationService) {
		this.organizationService = organizationService;
	}

	public IClientAlertService getClientAlertService() {
		return clientAlertService;
	}

	public void setClientAlertService(IClientAlertService clientAlertService) {
		this.clientAlertService = clientAlertService;
	}

	

}
