package com.rsnt.web.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import net.sf.json.JSONObject;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

import com.rsnt.common.exception.RsntException;
import com.rsnt.service.IClientAlertService;
import com.rsnt.service.IFeedbackService;
import com.rsnt.service.ISecurityService;
import com.rsnt.util.common.CommonUtil;
import com.rsnt.util.transferobject.FeedbackDataTO;
import com.rsnt.util.transferobject.FeedbackTOList;
import com.rsnt.util.transferobject.PlanDataTO;


@Path("/feedbackRestAction")
@Name("feedbackRestAction")
public class FeedbackRestAction {

	@Logger
	private Log log;
	
	@In(value=IFeedbackService.SEAM_NAME, create=true)
	private IFeedbackService feedbackService;

	@In(value=IClientAlertService.SEAM_NAME,create=true)
	private IClientAlertService clientAlertService;
	
	@In(create= true)
	private ISecurityService securityService;
	
	
	/*@GET
    @Path("/loadFeedbacks")
    @Produces(MediaType.APPLICATION_JSON)
    public String loadFeedbacks(){
		final Map<String, Object> rootMap = new LinkedHashMap<String, Object>();
    	final List<String> errorArray = new ArrayList<String>(0);
    	final List<String> dataArray = new ArrayList<String>(0);
    	
    	try {
    		final List<Object[]> planDataList = feedbackService.loadFeedbackData();
    		PlanDataTO planDataTO= null;
    		List<PlanDataTO> planDataTOList = new ArrayList<PlanDataTO>();
    		
    		for(Object[] obj: planDataList){
    			planDataTO = new PlanDataTO();
    			planDataTO.setPlanId(obj[0].toString());
    			planDataTO.setPlanDescription(obj[1].toString());
    			planDataTO.setPlanType(obj[2].toString());
    			planDataTOList.add(planDataTO);
    		}
    		
    		rootMap.put("id", -1);
        	rootMap.put("error", "");
        	rootMap.put("fieldErrors", errorArray);
        	rootMap.put("data", dataArray);
        	rootMap.put("aaData",planDataTOList);
        	
        	final JSONObject jsonObject = JSONObject.fromObject(rootMap);
        	return jsonObject.toString();
    	} catch (RsntException e) {
    		e.printStackTrace();
    		log.error("FeedbackRestAction.loadFeedbacks() - failed:", e.getMessage());
    		
    		rootMap.put("id", -1);
        	rootMap.put("error", "System Error - load Ads data failed");
        	rootMap.put("fieldErrors", errorArray);
        	rootMap.put("data", dataArray);
        	
        	final JSONObject jsonObject = JSONObject.fromObject(rootMap);
        	return jsonObject.toString();
    	}
	 }*/
	
	/**
	 *
	 *This method is used by load Feedbacks for the Jboss App.
	 */
	@Path("/loadFeedbackDetail")
	@Produces(MediaType.APPLICATION_JSON)
	@GET
	public String loadFeedbackDetail(@QueryParam("orgId") String orgId,@QueryParam("userId") String userId,
    		@QueryParam("objectName") String objectName){
		final Map<String, Object> rootMap = new LinkedHashMap<String, Object>();
    	final List<String> errorArray = new ArrayList<String>(0);
    	final List<String> dataArray = new ArrayList<String>(0);
    	
    	try {
    		final List<Object[]> planDataList = feedbackService.loadFeedbackDetail(new Long(orgId));
    		Long fdbkEditAccess = securityService.getRoleProtectedObjectForUser(new Long(userId), objectName);
    		PlanDataTO planDataTO= null;
    		List<PlanDataTO> planDataTOList = new ArrayList<PlanDataTO>();
    		
    		for(Object[] obj: planDataList){
    			planDataTO = new PlanDataTO();
    			planDataTO.setPlanId(obj[0].toString());
    			planDataTO.setPlanDescription(obj[1].toString());
    			if(obj[2]!=null)planDataTO.setPlanType(obj[2].toString());
    			if(obj[3]!=null)planDataTO.setActive(obj[3].toString());
    			planDataTO.setAccess(fdbkEditAccess);
    			planDataTOList.add(planDataTO);
    		}
    		
    		rootMap.put("id", -1);
        	rootMap.put("error", "");
        	rootMap.put("fieldErrors", errorArray);
        	rootMap.put("data", dataArray);
        	rootMap.put("aaData",planDataTOList);
        	
        	final JSONObject jsonObject = JSONObject.fromObject(rootMap);
        	return jsonObject.toString();
    	} catch (RsntException e) {
    		e.printStackTrace();
    		log.error("FeedbackRestAction.loadFeedbacks() - failed:", e.getMessage());
    		
    		rootMap.put("id", -1);
        	rootMap.put("error", "System Error - load Ads data failed");
        	rootMap.put("fieldErrors", errorArray);
        	rootMap.put("data", dataArray);
        	
        	final JSONObject jsonObject = JSONObject.fromObject(rootMap);
        	return jsonObject.toString();
    	}
	}
	
	/*@POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/updateFeedbackDetail")
    public String updateFeedbackDetail(@FormParam("feedbackDetailId") final String feedbackDetailId,@FormParam("column") final String column, @FormParam("value") final String value)
    {
		final Map<String, Object> rootMap = new LinkedHashMap<String, Object>();
    	final List<String> errorArray = new ArrayList<String>(0);
    	final List<String> dataArray = new ArrayList<String>(0);
    	
    	try{
    		
    		FeedbackQuestionaireDetail detail=  feedbackService.getFeedbackDetailEntity(new Long(feedbackDetailId));
    		if(column.equalsIgnoreCase("planDescription")){
    			detail.setQuestionText(value);
    		}
    		else if(column.equalsIgnoreCase("planType")){
    			detail.setOptionText(value);
    		}
    		feedbackService.updateFeedbackDetailEntity(detail);
    		return value;
    	}
    	catch(RsntException e){
    		e.printStackTrace();
    		log.error("FeedbackRestAction.updateFeedbackDetail() - failed:", e.getMessage());
    		
    		rootMap.put("id", -1);
        	rootMap.put("error", "System Error - Update Feedback data failed");
        	rootMap.put("fieldErrors", errorArray);
        	rootMap.put("data", dataArray);
        	
        	final JSONObject jsonObject = JSONObject.fromObject(rootMap);
        	return jsonObject.toString();
    	}
	}*/
	
	/**
	 *
	 *This method is used by load Feedback detail  for the Mobile App.
	 */
	@GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/loadFeedbackMobileData")
	public String loadFeedbackMobileData(@QueryParam("orgLayoutMarkerId") String orgLayoutMarkerId,
			@QueryParam("latData") final String latData, @QueryParam("longData") final String longData){
		final Map<String, Object> rootMap = new LinkedHashMap<String, Object>();
    	final List<String> errorArray = new ArrayList<String>(0);
    	final List<String> dataArray = new ArrayList<String>(0);
    	String checkInSplAvailable = null;
    	String checkInSplNote = null;
    	String optInData = null;
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
				checkInSplAvailable = cordObj[3].toString();
				checkInSplNote = cordObj[4]!=null?cordObj[4].toString():null;
				optInData = cordObj[5]!=null?"Y":"N";
				
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
			
	    	List<FeedbackDataTO> fdDataTOList = new ArrayList<FeedbackDataTO>();
	    	
	    	final List<Object[]> feedBackQnDtl = clientAlertService.loadFeedbackData(new Long(orgLayoutMarkerId));
	    	if(feedBackQnDtl!=null && feedBackQnDtl.size()>0){
	    		Object[] feedQtnArr = feedBackQnDtl.get(0);
	    		String active = feedQtnArr[2]!=null?feedQtnArr[2].toString():null;
	    		String feedBackQtnId = feedQtnArr[0]!=null?feedQtnArr[0].toString():null;
	    		if(active!=null && active.equals("Y")){
	    			List<Object[]> feedbackArr =  clientAlertService.loadFeedbackMobileData(new Long(orgLayoutMarkerId));
		    		if(feedbackArr!=null && feedbackArr.size()>0){
		    			for(Object[] test: feedbackArr){
		    				FeedbackDataTO dataTO = new FeedbackDataTO();
		    				dataTO.setQuestionText(test[0].toString());
		    				dataTO.setOptionTextType(test[1].toString());
		    				if(test[2]!=null)dataTO.setOptionTextVal(test[2].toString());
		    				fdDataTOList.add(dataTO);
		    				
		    			}
		            	rootMap.put("data",fdDataTOList);
		            	rootMap.put("Type", "Advanced");
		            	rootMap.put("QtnId", feedBackQtnId);
		    			rootMap.put("success", 1);
		    		}
	    		}
	    		else if(active!=null && active.equals("N")){
	    			rootMap.put("data",fdDataTOList);
	            	rootMap.put("Type", "Basic");
	            	rootMap.put("QtnId",feedBackQtnId);
	    			rootMap.put("success", 1);
	    		}
	    		else{
	    			//No Feedback Configured for Organization. Error Scenario since every organization atleast needs to have simple feedback
	    			rootMap.put("error", "No Feedback Configured for Organization. Please contact Administrator");
	            	rootMap.put("fieldErrors", errorArray);
	            	final JSONObject jsonObject = JSONObject.fromObject(rootMap);
	            	return jsonObject.toString();
	    		}
	    		
	    		rootMap.put("CheckInSpl", checkInSplAvailable);
				rootMap.put("CheckInSplNote", checkInSplNote);
				rootMap.put("OptIn", optInData);
	    		final JSONObject jsonObject = JSONObject.fromObject(rootMap);
	        	return jsonObject.toString();
	    	}
	    	else{
	    		rootMap.put("error", "No Feedback Configured for Organization. Please contact Administrator");
            	rootMap.put("fieldErrors", errorArray);
            	final JSONObject jsonObject = JSONObject.fromObject(rootMap);
            	return jsonObject.toString();
	    	}
	    		
    	}
    	catch(RsntException e){
    		e.printStackTrace();
    		log.error("FeedbackRestAction.loadFeedbackMobileData() - failed:", e.getMessage());
    		
    		rootMap.put("id", -1);
        	rootMap.put("error", "System Error - Load Feedback data failed");
        	rootMap.put("fieldErrors", errorArray);
        	rootMap.put("data", dataArray);
        	
        	final JSONObject jsonObject = JSONObject.fromObject(rootMap);
        	return jsonObject.toString();
    	}
	}
	
	/*@POST
    @Path("/saveFeedbackDetail")
    public String saveFeedback(@QueryParam("orgLayoutMarkerId") String orgLayoutMarkerId, @QueryParam("latData") final String latData, 
    		@QueryParam("longData") final String longData,
    		@FormParam("feedbackDataList[]") List<String> qtnDetailObj,
    		@FormParam("customerInfoList[]") List<String> custDataObj){
		final Map<String, Object> rootMap = new LinkedHashMap<String, Object>();
    	final List<String> errorArray = new ArrayList<String>(0);
    	final List<String> dataArray = new ArrayList<String>(0);
    	String qtnDetailData = qtnDetailObj!=null?qtnDetailObj.toString():null;
		String custData = custDataObj.toString();
		System.out.println(qtnDetailData);
		System.out.println(custData);
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
			
			clientAlertService.saveFeedbackCustomerDetail(custData, qtnDetailData,new Long(orgLayoutMarkerId));
			//rootMap.put("data",fdDataTOList);
        	//rootMap.put("Type", "Advanced");
        	//rootMap.put("QtnId", feedBackQtnId);
			rootMap.put("success", 1);
			final JSONObject jsonObject = JSONObject.fromObject(rootMap);
        	return jsonObject.toString();
		}
		catch(Exception e){
			e.printStackTrace();
    		log.error("FeedbackRestAction.saveFeedback() - failed:", e.getMessage());
    		
    		rootMap.put("id", -1);
        	rootMap.put("error", "System Error - Save Feedback data failed");
        	rootMap.put("fieldErrors", errorArray);
        	rootMap.put("data", dataArray);
        	
        	final JSONObject jsonObject = JSONObject.fromObject(rootMap);
        	return jsonObject.toString();
		}
	}*/
	
	public static String getBody(HttpServletRequest request) throws IOException {

	    String body = null;
	    StringBuilder stringBuilder = new StringBuilder();
	    BufferedReader bufferedReader = null;

	    try {
	        InputStream inputStream = request.getInputStream();
	        if (inputStream != null) {
	            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
	            char[] charBuffer = new char[128];
	            int bytesRead = -1;
	            while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
	                stringBuilder.append(charBuffer, 0, bytesRead);
	            }
	        } else {
	            stringBuilder.append("");
	        }
	    } catch (IOException ex) {
	        throw ex;
	    } finally {
	        if (bufferedReader != null) {
	            try {
	                bufferedReader.close();
	            } catch (IOException ex) {
	                throw ex;
	            }
	        }
	    }

	    body = stringBuilder.toString();
	    return body;
	}
	
	@POST
    @Path("/saveFeedbackDetail")
    public String saveFeedback(@QueryParam("orgLayoutMarkerId") String orgLayoutMarkerId, @QueryParam("latData") final String latData, 
    		@QueryParam("longData") final String longData ,
    		@Context HttpServletRequest req){
		final Map<String, Object> rootMap = new LinkedHashMap<String, Object>();
    	final List<String> errorArray = new ArrayList<String>(0);
    	final List<String> dataArray = new ArrayList<String>(0);
    	//String qtnDetailData = qtnDetailObj!=null?qtnDetailObj.toString():null;
		//String cusstData = custDataObj.toString();
		//System.out.println("Data Array:"+data);
    	String payloadRequest = null;
		try {
			payloadRequest = getBody(req);
			System.out.println("Payload: "+payloadRequest);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			rootMap.put("error", "Request received with invalid feedback Data");
			rootMap.put("success", 0);
			final JSONObject jsonObject = JSONObject.fromObject(rootMap);
	    	return jsonObject.toString();
		}
		
		
		//System.out.println(custData);
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
			
			clientAlertService.saveFeedbackCustomerDetailV2(payloadRequest,new Long(orgLayoutMarkerId));
			//rootMap.put("data",fdDataTOList);
        	//rootMap.put("Type", "Advanced");
        	//rootMap.put("QtnId", feedBackQtnId);
			rootMap.put("success", 1);
			final JSONObject jsonObject = JSONObject.fromObject(rootMap);
        	return jsonObject.toString();
		}
		catch(Exception e){
			e.printStackTrace();
    		log.error("FeedbackRestAction.saveFeedback() - failed:", e.getMessage());
    		
    		rootMap.put("id", -1);
        	rootMap.put("error", "System Error - Save Feedback data failed");
        	rootMap.put("fieldErrors", errorArray);
        	rootMap.put("data", dataArray);
        	
        	final JSONObject jsonObject = JSONObject.fromObject(rootMap);
        	return jsonObject.toString();
		}
	}
	
	@POST
    @Path("/testJSON")
    public String testJSON(@Context HttpServletRequest req){
		final Map<String, Object> rootMap = new LinkedHashMap<String, Object>();
    	final List<String> errorArray = new ArrayList<String>(0);
    	final List<String> dataArray = new ArrayList<String>(0);
    	//String qtnDetailData = qtnDetailObj!=null?qtnDetailObj.toString():null;
		//String cusstData = custDataObj.toString();
		//System.out.println("Data Array:"+data);
    	String payloadRequest = null;
		try {
			payloadRequest = getBody(req);
			System.out.println("Payload: "+payloadRequest);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			rootMap.put("error", "Request received with invalid feedback Data");
			rootMap.put("success", 0);
			final JSONObject jsonObject = JSONObject.fromObject(rootMap);
	    	return jsonObject.toString();
		}
		ObjectMapper mapper = new ObjectMapper(); 
		/* List<Employee> list2 = mapper.readValue(jsonString, 
			        TypeFactory.collectionType(List.class, Employee.class));*/
		
		 try {
			/*List<FeedbackDataTO> list = mapper.readValue(payloadRequest,
					    TypeFactory.defaultInstance().constructCollectionType(List.class,
					    		FeedbackDataTO.class));
			*/
			 FeedbackTOList employeList = mapper.readValue(payloadRequest,FeedbackTOList.class);
			
			System.out.println("List Des: "+employeList);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		
		return null;
	}
	
	public IFeedbackService getFeedbackService() {
		return feedbackService;
	}

	public void setFeedbackService(IFeedbackService feedbackService) {
		this.feedbackService = feedbackService;
	}

	public IClientAlertService getClientAlertService() {
		return clientAlertService;
	}

	public void setClientAlertService(IClientAlertService clientAlertService) {
		this.clientAlertService = clientAlertService;
	}

	public ISecurityService getSecurityService() {
		return securityService;
	}

	public void setSecurityService(ISecurityService securityService) {
		this.securityService = securityService;
	}
	
	
}
