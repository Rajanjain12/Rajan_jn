package com.kyobeeWaitlistService.serviceImpl;

import java.util.HashMap;
import java.util.LinkedHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kyobeeWaitlistService.dao.OrganizationDAO;
import com.kyobeeWaitlistService.service.WaitListService;
import com.kyobeeWaitlistService.util.LoggerUtil;
import com.kyobeeWaitlistService.util.WaitListServiceConstants;
import com.kyobeeWaitlistService.util.pusherImpl.NotificationUtil;


@Service
public class WaitListServiceImpl implements WaitListService {

	@Autowired
	OrganizationDAO organizationDAO;

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

}
