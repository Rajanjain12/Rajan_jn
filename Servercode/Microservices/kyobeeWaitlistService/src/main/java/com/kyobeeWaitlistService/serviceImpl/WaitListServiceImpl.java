package com.kyobeeWaitlistService.serviceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.hibernate.mapping.Array;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kyobeeWaitlistService.dao.GuestDAO;
import com.kyobeeWaitlistService.dao.OrganizationDAO;
import com.kyobeeWaitlistService.dto.GuestDTO;
import com.kyobeeWaitlistService.dto.GuestResponseDTO;
import com.kyobeeWaitlistService.dto.LanguageMasterDTO;
import com.kyobeeWaitlistService.entity.Guest;
import com.kyobeeWaitlistService.entity.LangMaster;
import com.kyobeeWaitlistService.service.WaitListService;
import com.kyobeeWaitlistService.util.LoggerUtil;
import com.kyobeeWaitlistService.util.WaitListServiceConstants;
import com.kyobeeWaitlistService.util.pusherImpl.NotificationUtil;


@Service
public class WaitListServiceImpl implements WaitListService {

	@Autowired
	OrganizationDAO organizationDAO;
	
	@Autowired
	GuestDAO guestDAO;

	@Override
	public void resetOrganizationsByOrgId(Long orgId) {
		
		final HashMap<String, Object> rootMap = new LinkedHashMap<>();
		LoggerUtil.logInfo("In WaitListService : reseting organization");
		organizationDAO.resetOrganizationByOrgId(orgId);
		rootMap.put("OP", "resetOrganizationPusher");
		rootMap.put("orgId", orgId);
		sendPusherMessage(rootMap, WaitListServiceConstants.PUSHER_CHANNEL_ENV + "_" + orgId);

	}

	@Override
	public void sendPusherMessage(Object rootMap, String channel) {
		NotificationUtil.sendMessage(rootMap, channel);

	}

	@Override
	public GuestResponseDTO fetchGuestList(Integer orgId, Integer pageSize, Integer pageNo) {
		Integer startIndex=0;
		if(pageNo!=1) {
			startIndex=pageSize*pageNo;		
		}
		List<Guest> guestList=guestDAO.fetchCheckinGuestList(orgId, pageSize, startIndex);
		List<GuestDTO> guestDTOs=new ArrayList<>();
		GuestResponseDTO guestResponse=new GuestResponseDTO();
		
		LoggerUtil.logInfo("size==="+guestList.size());
		for(Guest guest:guestList) {
			GuestDTO guestDTO=new GuestDTO();
			BeanUtils.copyProperties(guest, guestDTO);
			
		    LanguageMasterDTO languageMasterDTO=new LanguageMasterDTO();
		    languageMasterDTO.setLangId(guest.getLangmaster().getLangID());
		    languageMasterDTO.setLangIsoCode(guest.getLangmaster().getLangIsoCode());
		    languageMasterDTO.setLangName(guest.getLangmaster().getLangName());
		    guestDTO.setLangguagePref(languageMasterDTO);
		    
		    String seatingPref=guest.getSeatingPreference();
		    String[]  stringSeatingPref=seatingPref.split(",");
			guestDTOs.add(guestDTO);
		}
		guestResponse.setPageNo(pageNo);
		guestResponse.setTotalRecords(pageSize);
		guestResponse.setRecords(guestDTOs);
		return guestResponse;
	}

}
