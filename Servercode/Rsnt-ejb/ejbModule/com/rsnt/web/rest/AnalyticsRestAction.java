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
import com.rsnt.service.IAnalyticsService;
import com.rsnt.util.transferobject.GenericReportDTO;
@Path("/analyticsRestAction")
@Name("analyticsRestAction")
public class AnalyticsRestAction {
	@Logger
	private Log log;

	@In(value=IAnalyticsService.SEAM_NAME, create=true)
	private IAnalyticsService analyticsService;

	
	@GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/alerts")
	public String generateAlertsReport(@QueryParam("stratDate") String startDate,
			@QueryParam("endDate")  String endDate){
		List<Object[]> alertDataList = null;
		GenericReportDTO genericReportDTO = null;
		List<GenericReportDTO> reportDTOs =  null;
		final Map<String, Object> rootMap = new LinkedHashMap<String, Object>();
		JSONObject jsonObject = null;
		try {
			alertDataList = analyticsService.getAlertReportData(startDate, endDate);
			if(null != alertDataList && alertDataList.size() > 0){
				reportDTOs = new ArrayList<GenericReportDTO>(alertDataList.size());
				for (Object[] alertData : alertDataList) {
					genericReportDTO = new GenericReportDTO();
					genericReportDTO.setId(Long.valueOf(alertData[0].toString()));
					genericReportDTO.setDescription(alertData[1].toString());
					genericReportDTO.setxAxisValue(alertData[2].toString());
					genericReportDTO.setyAxisValue(Integer.parseInt(alertData[3].toString()));
					reportDTOs.add(genericReportDTO);
				}
	        	rootMap.put("error", "");
	        	rootMap.put("data", reportDTOs);
			}else {
				rootMap.put("error", "No Data Found");
	        	rootMap.put("data", null);
			}
			jsonObject = JSONObject.fromObject(rootMap);
			
		} catch (RsntException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			rootMap.put("error", e.getMessage());
        	rootMap.put("data", null);
			jsonObject = JSONObject.fromObject(rootMap);
		}catch (Exception e) {
			rootMap.put("error", e.getMessage());
        	rootMap.put("data", null);
        	jsonObject = JSONObject.fromObject(rootMap);
			e.printStackTrace();
		}
		return jsonObject.toString();
	}

}
