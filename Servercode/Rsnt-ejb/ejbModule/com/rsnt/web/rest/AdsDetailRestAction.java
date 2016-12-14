package com.rsnt.web.rest;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import net.sf.json.JSONObject;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

import com.rsnt.common.exception.RsntException;
import com.rsnt.service.IAdsDetailService;
import com.rsnt.service.ISecurityService;
import com.rsnt.util.common.AppInitializer;
import com.rsnt.util.common.CommonUtil;
import com.rsnt.util.transferobject.AdsDataTO;

@Path("/adsDetailRestAction")
@Name("adsDetailRestAction")
public class AdsDetailRestAction {

	@Logger
	private Log log;
	
	@In(value=IAdsDetailService.SEAM_NAME, create=true)
	private IAdsDetailService adsDetailService;
	
	@In(create= true)
	private ISecurityService securityService;
	
	@GET
    @Path("/loadAdsData")
    @Produces(MediaType.APPLICATION_JSON)
    public String loadAdsData(@QueryParam("active") String activeFlag, @QueryParam("orgId") String orgId, @QueryParam("userId") String userId,
    		@QueryParam("objectName") String objectName){
		final Map<String, Object> rootMap = new LinkedHashMap<String, Object>();
    	final List<String> errorArray = new ArrayList<String>(0);
    	final List<String> dataArray = new ArrayList<String>(0);
    	
    	try {
    		final List<Object[]> adDataList = adsDetailService.loadAdsData(new Long(activeFlag), new Long(orgId));
    		Long adEditAccess = securityService.getRoleProtectedObjectForUser(new Long(userId), objectName);
    		
    		AdsDataTO adDataTO= null;
    		List<AdsDataTO> adDataTOList = new ArrayList<AdsDataTO>();
    		
    		  /*Properties extProperties = new Properties();
              
              extProperties.load(this.getClass().getClassLoader().getResourceAsStream("rsnt.properties"));
              String domainNameUrl = extProperties.getProperty("domainNameUrl");*/
    		
    		for(Object[] obj: adDataList){
    			adDataTO = new AdsDataTO();
    			adDataTO.setAdsId(obj[0].toString());
    			adDataTO.setTitle(obj[1].toString());
    			adDataTO.setActive(obj[2].toString());
    			if(obj[3]!=null)adDataTO.setWeekdayRun(CommonUtil.convertWeekNumberToString(obj[3].toString()));
    			if(obj[4]!=null){
    				Timestamp ts = (Timestamp) obj[4];
    				adDataTO.setStartDate(CommonUtil.convertDateToFormattedString(new Date(ts.getTime()),"MM/dd/yyyy"));
    			}
    			if(obj[5]!=null){
    				Timestamp ts = (Timestamp) obj[5];
    				adDataTO.setEndDate(CommonUtil.convertDateToFormattedString(new Date(ts.getTime()),"MM/dd/yyyy"));
    			}
    			if(obj[4]!=null){
    				Timestamp ts = (Timestamp) obj[4];
    				adDataTO.setStartTime(CommonUtil.convertDateToFormattedString(new Date(ts.getTime()),"hh:mm a"));
    			}
    			if(obj[5]!=null){
    				Timestamp ts = (Timestamp) obj[5];
    				adDataTO.setEndTime(CommonUtil.convertDateToFormattedString(new Date(ts.getTime()),"hh:mm a"));
    			}
    			
      			//if(obj[4]!=null)adDataTO.setStartTime(CommonUtil.convertDateStrToFormattedDateStr(obj[4].toString(),"MM/dd/yyyy HH:mm","hh:mm a"));
    			//if(obj[5]!=null)adDataTO.setEndTime(CommonUtil.convertDateStrToFormattedDateStr(obj[5].toString(),"MM/dd/yyyy HH:mm","hh:mm a"));
    			adDataTO.setTillCreditAvailable(obj[6].toString());
    			adDataTO.setAccess(adEditAccess);
    			String bannerImageUrl = obj[7].toString();
    			//bannerImageUrl = bannerImageUrl.replace("/home/buzzbell/public_html", "http://ringmyserver.com");//Cannot read the property file in Rest Action
    			//bannerImageUrl = bannerImageUrl.replace(AppInitializer.appImgResourcePath, AppInitializer.domainNameUrl);//Cannot read the property file in Rest Action

    			adDataTO.setAdBannerImage(bannerImageUrl);
    			//this.setEndDate(CommonUtil.convertDateToFormattedString(selectedAd.getEndDateTime(),"MM/dd/yyyy"));
				//this.setStartTime(CommonUtil.convertDateToFormattedString(selectedAd.getStartDateTime(),"HH:mm"));
				//this.setEndTime(CommonUtil.convertDateToFormattedString(selectedAd.getEndDateTime(),"HH:mm"));
				
    			adDataTOList.add(adDataTO);
    		}
    		
    		rootMap.put("id", -1);
        	rootMap.put("error", "");
        	rootMap.put("fieldErrors", errorArray);
        	rootMap.put("data", dataArray);
        	rootMap.put("aaData",adDataTOList);
        	
        	final JSONObject jsonObject = JSONObject.fromObject(rootMap);
        	return jsonObject.toString();
    	} 
    	
    	catch (RsntException e) {
    		e.printStackTrace();
    		log.error("AdsDetailRestAction.loadAdsData() - failed:", e.getMessage());
    		
    		rootMap.put("id", -1);
        	rootMap.put("error", "System Error - load Ads data failed");
        	rootMap.put("fieldErrors", errorArray);
        	rootMap.put("data", dataArray);
        	
        	final JSONObject jsonObject = JSONObject.fromObject(rootMap);
        	return jsonObject.toString();
    	}
	 }

	public IAdsDetailService getAdsDetailService() {
		return adsDetailService;
	}

	public void setAdsDetailService(IAdsDetailService adsDetailService) {
		this.adsDetailService = adsDetailService;
	}

	public ISecurityService getSecurityService() {
		return securityService;
	}

	public void setSecurityService(ISecurityService securityService) {
		this.securityService = securityService;
	}
}
