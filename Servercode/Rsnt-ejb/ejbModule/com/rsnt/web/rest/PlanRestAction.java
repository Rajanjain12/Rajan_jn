package com.rsnt.web.rest;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import net.sf.json.JSONObject;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

import com.rsnt.common.exception.RsntException;
import com.rsnt.service.IPlanService;
import com.rsnt.util.transferobject.PlanDataTO;

@Path("/planRestAction")
@Name("planRestAction")
public class PlanRestAction {
	
	@Logger
	private Log log;
	
	@In(create = true)
	private IPlanService planService;
	
	@GET
    @Path("/loadPlanData")
    @Produces(MediaType.APPLICATION_JSON)
    public String loadPlanData(){
		final Map<String, Object> rootMap = new LinkedHashMap<String, Object>();
    	final List<String> errorArray = new ArrayList<String>(0);
    	final List<String> dataArray = new ArrayList<String>(0);
    	
    	try {
    		final List<Object[]> planDataList = planService.loadPlanData();
    		PlanDataTO planDataTO= null;
    		List<PlanDataTO> planDataTOList = new ArrayList<PlanDataTO>();
    		
    		for(Object[] obj: planDataList){
    			planDataTO = new PlanDataTO();
    			planDataTO.setPlanId(obj[0].toString());
    			planDataTO.setPlanDescription(obj[1].toString());
    			planDataTO.setActive(obj[2].toString());
    			//planDataTO.setPlanType(obj[2].toString());
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
    		log.error("PlanRestAction.loadPlanData() - failed:", e.getMessage());
    		
    		rootMap.put("id", -1);
        	rootMap.put("error", "System Error - load Plan data failed");
        	rootMap.put("fieldErrors", errorArray);
        	rootMap.put("data", dataArray);
        	
        	final JSONObject jsonObject = JSONObject.fromObject(rootMap);
        	return jsonObject.toString();
    	}
	 }

	public IPlanService getPlanService() {
		return planService;
	}

	public void setPlanService(IPlanService planService) {
		this.planService = planService;
	}	

}
