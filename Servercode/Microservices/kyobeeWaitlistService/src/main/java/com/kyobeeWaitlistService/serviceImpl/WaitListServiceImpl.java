package com.kyobeeWaitlistService.serviceImpl;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kyobeeWaitlistService.dao.GuestDAO;
import com.kyobeeWaitlistService.dao.LanguageKeyMappingDAO;
import com.kyobeeWaitlistService.dao.OrganizationDAO;
import com.kyobeeWaitlistService.dao.OrganizationTemplateDAO;
import com.kyobeeWaitlistService.dto.OrganizationMetricsDTO;
import com.kyobeeWaitlistService.service.WaitListService;
import com.kyobeeWaitlistService.util.LoggerUtil;
import com.kyobeeWaitlistService.util.WaitListServiceConstants;
import com.kyobeeWaitlistService.util.pusherImpl.NotificationUtil;

@Service
public class WaitListServiceImpl implements WaitListService {

	@Autowired
	OrganizationDAO organizationDAO;

	@Autowired
	LanguageKeyMappingDAO languageKeyMappingDAO;

	@Autowired
	OrganizationTemplateDAO organizationTemplateDAO;

	@Autowired
	GuestDAO guestDAO;
	
	@Override
	public HashMap<String, Object> updateLanguagesPusher() {

		HashMap<String, Object> rootMap = new LinkedHashMap<>();
		Long flagCount = languageKeyMappingDAO.getUpdateFlagCount(WaitListServiceConstants.MARK_AS_LANGUAGE_UPDATED);

		LoggerUtil.logInfo("Update Flag count:" + flagCount);

		if (flagCount > 0) {		
			rootMap.put("OP", "REFRESH_LANGUAGE_PUSHER");
			//rootMap.put("name", "REFRESH_LANGUAGE_PUSHER");
			NotificationUtil.sendMessage(rootMap, WaitListServiceConstants.GLOBAL_CHANNEL_ENV);
			languageKeyMappingDAO.resetUpdateFlagCount(WaitListServiceConstants.MARK_AS_LANGUAGE_RESET);
		}
		return rootMap;
	}

	@Override
	public OrganizationMetricsDTO getOrganizationMetrics(Integer orgId) {
		Map<String, Object> orgMetricsDTO = guestDAO.getOrganizationMetrics(orgId);
		LoggerUtil.logInfo("orgMetricsDTO" + orgMetricsDTO);
		OrganizationMetricsDTO metricsDTO = new OrganizationMetricsDTO();

		metricsDTO.setGuestMinRank(orgMetricsDTO.get("OP_NOWSERVERINGPARTY") !=null ?(int)orgMetricsDTO.get("OP_NOWSERVERINGPARTY"): null );
		metricsDTO.setGuestNotifiedWaitTime(orgMetricsDTO.get("OP_GUESTNOTIFIEDWAITTIME")!=null ?(int)orgMetricsDTO.get("OP_GUESTNOTIFIEDWAITTIME"): null );
		metricsDTO.setGuestToBeNotified(orgMetricsDTO.get("OP_GUESTTOBENOTIFIED")!=null ?orgMetricsDTO.get("OP_GUESTTOBENOTIFIED").toString(): null );
		metricsDTO.setNotifyUserCount(orgMetricsDTO.get("OP_NOTIFYUSERCOUNT")!=null ?(int) orgMetricsDTO.get("OP_NOTIFYUSERCOUNT"): null );
		metricsDTO.setOrgGuestCount(orgMetricsDTO.get("OP_TOTALWAITINGGUEST")!=null ?(int)orgMetricsDTO.get("OP_TOTALWAITINGGUEST"): null );
		metricsDTO.setOrgTotalWaitTime(orgMetricsDTO.get("OP_TOTALWAITTIME")!=null?(int)orgMetricsDTO.get("OP_TOTALWAITTIME"): null );
		metricsDTO.setPerPartyWaitTime(orgMetricsDTO.get("OP_PERPARTYWAITTIME")!=null?(int)orgMetricsDTO.get("OP_PERPARTYWAITTIME"): null );

		return metricsDTO;
	}
}
