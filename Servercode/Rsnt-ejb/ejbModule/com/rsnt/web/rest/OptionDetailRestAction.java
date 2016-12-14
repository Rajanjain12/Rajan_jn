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
import com.rsnt.service.IOptionService;
import com.rsnt.util.transferobject.OptionDetailTO;

@Path("/optionDetailRestAction")
@Name("optionDetailRestAction")
public class OptionDetailRestAction {

	@Logger
	private Log log;
	 
	
	@In(create = true)
	private IOptionService optionService;

	@GET
    @Path("/loadOptionData")
    @Produces(MediaType.APPLICATION_JSON)
    public String loadOptionData(@QueryParam("userId") String userId, @QueryParam("objectName") String objectName,@
    		QueryParam("orgId") String orgId){
		final Map<String, Object> rootMap = new LinkedHashMap<String, Object>();
    	final List<String> errorArray = new ArrayList<String>(0);
    	final List<String> dataArray = new ArrayList<String>(0);
    	
    	try {
    		final List<Object[]> optionDataList = optionService.loadOptionData(new Long(orgId));
    		OptionDetailTO optionDetailTO = null;
    		List<OptionDetailTO> optionDetailTOList = new ArrayList<OptionDetailTO>();
    		
    		for(Object[] obj: optionDataList){
    			optionDetailTO = new OptionDetailTO();
    			optionDetailTO.setOrgOptionId(obj[0].toString());
    			optionDetailTO.setDescription(obj[2].toString());
    			optionDetailTOList.add(optionDetailTO);
    		}
    		
    		rootMap.put("id", -1);
        	rootMap.put("error", "");
        	rootMap.put("fieldErrors", errorArray);
        	rootMap.put("data", dataArray);
        	rootMap.put("aaData",optionDetailTOList);
        	
        	final JSONObject jsonObject = JSONObject.fromObject(rootMap);
        	return jsonObject.toString();
    	} catch (RsntException e) {
    		log.error("OptionDetailRestAction.loadOptionData() - failed:", e.getMessage());
    		
    		rootMap.put("id", -1);
        	rootMap.put("error", "System Error - load Option data failed");
        	rootMap.put("fieldErrors", errorArray);
        	rootMap.put("data", dataArray);
        	
        	final JSONObject jsonObject = JSONObject.fromObject(rootMap);
        	return jsonObject.toString();
    	}
	 }	

	public IOptionService getOptionService() {
		return optionService;
	}


	public void setOptionService(IOptionService optionService) {
		this.optionService = optionService;
	}
}
