package com.kyobeeWaitlistService.serviceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kyobeeWaitlistService.dao.GuestDAO;
import com.kyobeeWaitlistService.dao.LanguageKeyMappingDAO;
import com.kyobeeWaitlistService.dao.LookupDAO;
import com.kyobeeWaitlistService.dao.OrganizationDAO;
import com.kyobeeWaitlistService.dao.OrganizationTemplateDAO;
import com.kyobeeWaitlistService.dto.GuestDTO;
import com.kyobeeWaitlistService.dto.OrganizationMetricsDTO;
import com.kyobeeWaitlistService.dto.GuestResponseDTO;
import com.kyobeeWaitlistService.dto.LanguageMasterDTO;
import com.kyobeeWaitlistService.dto.SeatingMarketingPrefDTO;
import com.kyobeeWaitlistService.entity.Guest;
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
	
	@Override
	public HashMap<String, Object> updateLanguagesPusher() {

		HashMap<String, Object> rootMap = new LinkedHashMap<>();
		Long flagCount = languageKeyMappingDAO.getUpdateFlagCount(WaitListServiceConstants.MARK_AS_LANGUAGE_UPDATED);
		LoggerUtil.logInfo("Update Flag count:" + flagCount);
		//sending pusher if there is any language key map updated
		if (flagCount > 0) {
			rootMap.put("OP", "REFRESH_LANGUAGE_PUSHER");
			NotificationUtil.sendMessage(rootMap, WaitListServiceConstants.GLOBAL_CHANNEL_ENV);
			languageKeyMappingDAO.resetUpdateFlagCount(WaitListServiceConstants.MARK_AS_LANGUAGE_RESET);
		}
		return rootMap;
	}

	
	//for fetching organization related matrix
	@Override
	public OrganizationMetricsDTO getOrganizationMetrics(Integer orgId) {
		Map<String, Object> orgMetricsDTO = guestDAO.getOrganizationMetrics(orgId);
		LoggerUtil.logInfo("orgMetricsDTO" + orgMetricsDTO);
		OrganizationMetricsDTO metricsDTO = new OrganizationMetricsDTO();
		//for converting map to DTO
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
