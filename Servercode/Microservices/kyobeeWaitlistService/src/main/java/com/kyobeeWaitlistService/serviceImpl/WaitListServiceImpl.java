package com.kyobeeWaitlistService.serviceImpl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.kyobeeWaitlistService.dao.GuestDAO;
import com.kyobeeWaitlistService.dao.LanguageKeyMappingDAO;
import com.kyobeeWaitlistService.dao.LookupDAO;
import com.kyobeeWaitlistService.dao.OrganizationCustomDAO;
import com.kyobeeWaitlistService.dao.OrganizationDAO;
import com.kyobeeWaitlistService.dao.OrganizationTemplateDAO;
import com.kyobeeWaitlistService.dto.LanguageKeyMappingDTO;
import com.kyobeeWaitlistService.dto.LanguageMasterDTO;
import com.kyobeeWaitlistService.dto.OrgPrefKeyMapDTO;
import com.kyobeeWaitlistService.dto.OrganizationMetricsDTO;
import com.kyobeeWaitlistService.dto.PusherDTO;
import com.kyobeeWaitlistService.dto.SeatingMarketingPrefDTO;
import com.kyobeeWaitlistService.dto.WaitListMetricsDTO;
import com.kyobeeWaitlistService.dto.WaitlistMetrics;
import com.kyobeeWaitlistService.entity.Lookup;
import com.kyobeeWaitlistService.service.WaitListService;
import com.kyobeeWaitlistService.util.LoggerUtil;
import com.kyobeeWaitlistService.util.WaitListServiceConstants;
import com.kyobeeWaitlistService.util.pusherImpl.NotificationUtil;

@Service
@Transactional
public class WaitListServiceImpl implements WaitListService {

	@Autowired
	OrganizationDAO organizationDAO;
	
	@Autowired
	GuestDAO guestDAO;
	
	@Autowired
	LookupDAO lookupDAO;

	@Autowired
	LanguageKeyMappingDAO languageKeyMappingDAO;

	@Autowired
	OrganizationTemplateDAO organizationTemplateDAO;
	
	@Autowired
	OrganizationCustomDAO organizationCustomDAO;
	
	@Override
	public HashMap<String, Object> updateLanguagesPusher() {

		HashMap<String, Object> rootMap = new LinkedHashMap<>();
		Long flagCount = languageKeyMappingDAO.getUpdateFlagCount(WaitListServiceConstants.MARK_AS_LANGUAGE_UPDATED);
		LoggerUtil.logInfo("Update Flag count:" + flagCount);
		//sending pusher if there is any language key map updated
		if (flagCount > 0) {
			rootMap.put("op", "REFRESH_LANGUAGE_PUSHER");
			NotificationUtil.sendMessage(rootMap, WaitListServiceConstants.GLOBAL_CHANNEL_ENV);
			languageKeyMappingDAO.resetUpdateFlagCount(WaitListServiceConstants.MARK_AS_LANGUAGE_RESET);
		}
		return rootMap;
	}

	
	//for fetching organization related matrix
	@Override
	public OrganizationMetricsDTO getOrganizationMetrics(Integer orgId) {
		
		OrganizationMetricsDTO orgMetricsDTO = organizationCustomDAO.getOrganizationMetrics(orgId);
		
		return orgMetricsDTO;
	}


	//for updating organization metrics setting
	
	@Override
	public PusherDTO updateOrgSettings(Integer orgId, Integer perPartyWaitTime, Integer numberOfUsers) {
		WaitlistMetrics waitlistMetrics = organizationCustomDAO.updateOrgSettings(orgId, perPartyWaitTime, numberOfUsers);
		
		//for sending details to pusher
		PusherDTO pusherDTO = new PusherDTO();
		
		pusherDTO.setWaitlistMetrics(waitlistMetrics);
		pusherDTO.setOp("NOTIFY_USER");
		pusherDTO.setFrom("ADMIN");
		pusherDTO.setOrgId(orgId);
		
		NotificationUtil.sendMessage(pusherDTO, WaitListServiceConstants.PUSHER_CHANNEL_ENV+"_"+orgId);
		
		return pusherDTO;
	}


	@Override
	public OrgPrefKeyMapDTO fetchOrgPrefandKeyMap(Integer orgId) {
		//fetch seating pref and marketing pref associated with org
		List<Lookup> lookupList = lookupDAO.fetchSeatingAndMarketingPref(orgId, WaitListServiceConstants.SEATINGPREFID,WaitListServiceConstants.MARKETINGPREFID);
		OrgPrefKeyMapDTO orgPrefDTO=new OrgPrefKeyMapDTO();
		List<SeatingMarketingPrefDTO> seatingPrefList = new ArrayList<>();
		List<SeatingMarketingPrefDTO> marketingPrefList = new ArrayList<>();
		
		List<LanguageMasterDTO> languageList = languageKeyMappingDAO.fetchByLangIsoCodeAndScreenName(WaitListServiceConstants.ENGISOCODE,WaitListServiceConstants.SCREENNAME);
		
		Map<String, String> keymap = new HashMap<>();
		
		
		for(LanguageMasterDTO langMaster:languageList) {
			keymap.put(langMaster.getKeyName(), langMaster.getValue());
			
        }
		
		SeatingMarketingPrefDTO seatingPref;
		SeatingMarketingPrefDTO marketingPref;
		//to separate seating pref and marketing pref 
		for (Lookup lookup : lookupList) {
			if (lookup.getLookuptype().getLookupTypeID() == WaitListServiceConstants.SEATINGPREFID) {
				seatingPref = new SeatingMarketingPrefDTO();
				seatingPref.setPrefValue(lookup.getName());
				seatingPref.setPrefValueId(lookup.getLookupID());
				if (keymap.containsValue(lookup.getName())) {
					String key = null;
					for (Map.Entry<String, String> entry : keymap.entrySet()) {
						if ((lookup.getName()).equals(entry.getValue())) {
							key = entry.getKey();
							seatingPref.setPrefKey(key);
							break;
						}
					}
				}
				seatingPrefList.add(seatingPref);
			} else if (lookup.getLookuptype().getLookupTypeID() == WaitListServiceConstants.MARKETINGPREFID) {
				marketingPref = new SeatingMarketingPrefDTO();
				marketingPref.setPrefValue(lookup.getName());
				marketingPref.setPrefValueId(lookup.getLookupID());
				if (keymap.containsValue(lookup.getName())) {

					String key = null;
					for (Map.Entry<String, String> entry : keymap.entrySet()) {
						if ((lookup.getName()).equals(entry.getValue())) {
							key = entry.getKey();
							marketingPref.setPrefKey(key);
							break;
						}
					}
				}
				marketingPrefList.add(marketingPref);
			}
		

		}
		orgPrefDTO.setMarketingPreference(marketingPrefList);
		orgPrefDTO.setSeatingPreference(seatingPrefList);
		
	// seating marketing pref end
		return orgPrefDTO;
	}
	
	
}
