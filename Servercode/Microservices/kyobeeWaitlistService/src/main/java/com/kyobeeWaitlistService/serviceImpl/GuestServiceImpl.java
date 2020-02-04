package com.kyobeeWaitlistService.serviceImpl;

import java.util.HashMap;
import java.util.LinkedHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kyobeeWaitlistService.dao.GuestDAO;
import com.kyobeeWaitlistService.dao.OrganizationDAO;
import com.kyobeeWaitlistService.dto.GuestMetricsDTO;
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
	
	@Override
	public GuestMetricsDTO getGuestMetrics(Integer guestId, Integer orgId) {
	
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
		organizationDAO.resetOrganizationByOrgId(orgId);
		rootMap.put("OP", "resetOrganizationPusher");
		rootMap.put("orgId", orgId);
		NotificationUtil.sendMessage(rootMap, WaitListServiceConstants.PUSHER_CHANNEL_ENV + "_" + orgId);

	}

}
