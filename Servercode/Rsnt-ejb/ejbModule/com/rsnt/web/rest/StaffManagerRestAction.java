package com.rsnt.web.rest;

import java.util.ArrayList;
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
import com.rsnt.entity.User;
import com.rsnt.service.ISecurityService;
import com.rsnt.service.IStaffService;
import com.rsnt.util.common.CommonUtil;
import com.rsnt.util.transferobject.StaffDataTO;


@Path("/staffManagerRestAction")
@Name("staffManagerRestAction")
public class StaffManagerRestAction {

	@Logger
	private Log log;
	 
	@In(create= true)
	private IStaffService staffService; 
	
	@In(create= true)
	private ISecurityService securityService;
	 
	@GET
    @Path("/loadStaffData")
    @Produces(MediaType.APPLICATION_JSON)
    public String loadStaffData(@QueryParam("orgId") String orgId, @QueryParam("userId") String userId, @QueryParam("objectName") String objectName){
		final Map<String, Object> rootMap = new LinkedHashMap<String, Object>();
    	final List<String> errorArray = new ArrayList<String>(0);
    	final List<String> dataArray = new ArrayList<String>(0);
    	
    	try {
    		if(orgId!=null){
    			final List<Object[]> staffDataList = staffService.loadStaffData(new Long(orgId));
    			Long staffEditAccess = securityService.getRoleProtectedObjectForUser(new Long(userId), objectName);
        		rootMap.put("id", -1);
            	rootMap.put("error", "");
            	rootMap.put("fieldErrors", errorArray);
            	rootMap.put("data", dataArray);
            	rootMap.put("aaData", populateStaffDataTO(staffDataList, staffEditAccess));
            	
            	final JSONObject jsonObject = JSONObject.fromObject(rootMap);
            	return jsonObject.toString();
    		}
    		return null;
    		
    	} catch (RsntException e) {
    		log.error("StaffManagerRestAction.loadStaffData() - failed:", e.getMessage());
    		
    		rootMap.put("id", -1);
        	rootMap.put("error", "System Error - load staff data failed");
        	rootMap.put("fieldErrors", errorArray);
        	rootMap.put("data", dataArray);
        	
        	final JSONObject jsonObject = JSONObject.fromObject(rootMap);
        	return jsonObject.toString();
    	}
	 }
	
	@GET
    @Path("/checkDuplicateUserId")
    @Produces(MediaType.APPLICATION_JSON)
    public String checkDuplicateUserId(@QueryParam("userId") String userId){
		try{
			System.out.println("Input UserId "+userId);
			User user = securityService.getSecurityUserDetails(userId);
			if(user!=null) return "1";
			else return "0";
		}
		catch(RsntException e){
			return "0";
		}
	 }
	
	
	 private List<StaffDataTO> populateStaffDataTO(List<Object[]> staffDataList, Long accessDetail) {
		 List<StaffDataTO> staffDataTOs = new ArrayList<StaffDataTO>(0);
		 
		 if(!CommonUtil.isNullOrEmpty(staffDataList)) {
	    		for(Object object[] : staffDataList) {
	        		int counter = 0;
	        		StaffDataTO staffDataTO = new StaffDataTO();
	        		staffDataTO.setUserId(Long.valueOf(CommonUtil.toStr(object[counter++])));
	        		staffDataTO.setUserName(CommonUtil.toStr(object[counter++]));
	        		counter++;
	        		staffDataTO.setFirstName(CommonUtil.toStr(object[counter++]));
	        		staffDataTO.setLastName(CommonUtil.toStr(object[counter++]));
	        		staffDataTO.setAddress(CommonUtil.toStr(object[counter++]));
	        		staffDataTO.setPrimaryContactNo(CommonUtil.toStr(object[counter++]));
	        		staffDataTO.setAlternateContactNo(CommonUtil.toStr(object[counter++]));
	        		staffDataTO.setEmail(CommonUtil.toStr(object[counter++]));
	        		staffDataTO.setActiveStatus(CommonUtil.getYesNoFlag(object[counter++]));
	        		//staffDataTO.setActiveStatus(CommonUtil.toStr(object[counter++]).equals("Y") ? "Yes" : "No");
	        		staffDataTO.setRole(CommonUtil.toStr(object[15]));
	        		staffDataTO.setAccess(accessDetail);
	        		staffDataTOs.add(staffDataTO);
	    		}
		 }
		 return staffDataTOs;
	 }

	public ISecurityService getSecurityService() {
		return securityService;
	}

	public void setSecurityService(ISecurityService securityService) {
		this.securityService = securityService;
	}
	
	 
	 
}
