package com.kyobeeWaitlistService.serviceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kyobeeWaitlistService.dao.GuestDAO;
import com.kyobeeWaitlistService.dao.LookupDAO;
import com.kyobeeWaitlistService.dao.OrganizationDAO;
import com.kyobeeWaitlistService.dto.GuestDTO;
import com.kyobeeWaitlistService.dto.GuestMetricsDTO;
import com.kyobeeWaitlistService.dto.GuestResponseDTO;
import com.kyobeeWaitlistService.dto.LanguageMasterDTO;
import com.kyobeeWaitlistService.dto.SeatingMarketingPrefDTO;
import com.kyobeeWaitlistService.entity.Guest;
import com.kyobeeWaitlistService.entity.Lookup;
import com.kyobeeWaitlistService.service.GuestService;
import com.kyobeeWaitlistService.util.LoggerUtil;
import com.kyobeeWaitlistService.util.WaitListServiceConstants;
import com.kyobeeWaitlistService.util.pusherImpl.NotificationUtil;

@Service
public class GuestServiceImpl implements GuestService {
	
	@Autowired
	OrganizationDAO organizationDAO;
	
	@Autowired
	GuestDAO guestDAO;
	
	@Autowired
	LookupDAO lookupDAO;
	
	@Override
	public GuestMetricsDTO getGuestMetrics(Integer guestId, Integer orgId) {
		//need to create proc for this 
		Integer orgWaitTime = organizationDAO.getOrganizationWaitTime(orgId);
		Integer guestCount = guestDAO.getGuestCount(orgId , guestId);
		Integer guestRank = guestDAO.getGuestRank(orgId, guestId);		
		
		GuestMetricsDTO metricsDTO = new GuestMetricsDTO();
		
		metricsDTO.setGuestRank(guestRank);
		metricsDTO.setTotalWaitTime(guestCount <=0 ?orgWaitTime:((guestCount)*orgWaitTime)+orgWaitTime);
		metricsDTO.setOrgWaitTime(orgWaitTime);
		metricsDTO.setGuestAheadCount(guestCount <= 0 ? 0 : (guestCount));

		return metricsDTO;
	}
	
	@Override
	public void resetOrganizationsByOrgId(Long orgId) {

		final HashMap<String, Object> rootMap = new LinkedHashMap<>();
		LoggerUtil.logInfo("In WaitListService : reseting organization");
		//reseting org related guest data 
		organizationDAO.resetOrganizationByOrgId(orgId);
		rootMap.put("OP", "resetOrganizationPusher");
		rootMap.put("orgId", orgId);
		NotificationUtil.sendMessage(rootMap, WaitListServiceConstants.PUSHER_CHANNEL_ENV + "_" + orgId);

	}

	@Override
	public GuestResponseDTO fetchGuestList(Integer orgId, Integer pageSize, Integer pageNo,String searchText) {
		Integer startIndex=0;
		if(pageNo!=1) {
			startIndex=pageSize*pageNo;		
		}
		List<Guest> guestList;
		
		if(searchText!=null) {
			// for fetching data related to search text 
			searchText="%"+searchText+"%";
			guestList=guestDAO.fetchCheckinGuestBySearchText(orgId, pageSize, startIndex,searchText);
		}
		else {
			//for fetching whole list of guest
			guestList=guestDAO.fetchCheckinGuestList(orgId, pageSize, startIndex);
		}
	
		List<GuestDTO> guestDTOs=new ArrayList<>();
		GuestResponseDTO guestResponse=new GuestResponseDTO();
		
		LoggerUtil.logInfo("size==="+guestList.size());
		for(Guest guest:guestList) {
			GuestDTO guestDTO=new GuestDTO();
			BeanUtils.copyProperties(guest, guestDTO);
			
			//for adding language 
		    LanguageMasterDTO languageMasterDTO=new LanguageMasterDTO();
		    languageMasterDTO.setLangId(guest.getLangmaster().getLangID());
		    languageMasterDTO.setLangIsoCode(guest.getLangmaster().getLangIsoCode());
		    languageMasterDTO.setLangName(guest.getLangmaster().getLangName());
		    guestDTO.setLangguagePref(languageMasterDTO);
		    
		    List<SeatingMarketingPrefDTO> seatingPrefList=new ArrayList<>();
		    List<SeatingMarketingPrefDTO> marketingPrefList=new ArrayList<>();
		    
		    //for arranging seating pref in list
		    String seatingPref=guest.getSeatingPreference();
		    if(seatingPref!=null) {
		    String[]  stringSeatingPref=seatingPref.split(",");
				    List<Lookup> seatingLookup= lookupDAO.fetchLookup(stringSeatingPref);
				    
				    for(Lookup lookup:seatingLookup) {
				    	SeatingMarketingPrefDTO seatingPrefence=new SeatingMarketingPrefDTO();
					    seatingPrefence.setPrefValueId(lookup.getLookupID());
					    seatingPrefence.setPrefValue(lookup.getName());
					    seatingPrefList.add(seatingPrefence);
				    }
		    }
		   
		    guestDTO.setSeatingPreference(seatingPrefList);
		    
		  //for arranging marketing pref in list
		    String marketingPref=guest.getMarketingPreference();
		    if(marketingPref!=null) {
		    	String[] stringMarketingPref=marketingPref.split(",");
			    List<Lookup> marketingLookup= lookupDAO.fetchLookup(stringMarketingPref);
			    
			    for(Lookup lookup:marketingLookup) {
			    	SeatingMarketingPrefDTO marketingPrefence=new SeatingMarketingPrefDTO();
				    marketingPrefence.setPrefValueId(lookup.getLookupID());
				    marketingPrefence.setPrefValue(lookup.getName());
				    marketingPrefList.add(marketingPrefence);
			    }
		    }
		    guestDTO.setMarketingPreference(marketingPrefList);
		    LoggerUtil.logInfo("size lookup ==="+marketingPrefList.size());
			guestDTOs.add(guestDTO);
		}
		guestResponse.setPageNo(pageNo);
		guestResponse.setTotalRecords(pageSize);
		guestResponse.setRecords(guestDTOs);
		return guestResponse;
	}

}
