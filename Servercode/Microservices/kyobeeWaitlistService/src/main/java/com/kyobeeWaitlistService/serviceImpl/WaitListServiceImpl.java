package com.kyobeeWaitlistService.serviceImpl;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.kyobeeWaitlistService.dao.GuestDAO;
import com.kyobeeWaitlistService.dao.LanguageKeyMappingDAO;
import com.kyobeeWaitlistService.dao.LookupDAO;
import com.kyobeeWaitlistService.dao.OrganizationCustomDAO;
import com.kyobeeWaitlistService.dao.OrganizationDAO;
import com.kyobeeWaitlistService.dao.OrganizationTemplateDAO;
import com.kyobeeWaitlistService.dao.SmsLogDAO;
import com.kyobeeWaitlistService.dto.GuestDTO;
import com.kyobeeWaitlistService.dto.GuestNotificationDTO;
import com.kyobeeWaitlistService.dto.LanguageMasterDTO;
import com.kyobeeWaitlistService.dto.OrgPrefKeyMapDTO;
import com.kyobeeWaitlistService.dto.OrganizationMetricsDTO;
import com.kyobeeWaitlistService.dto.OrganizationTemplateDTO;
import com.kyobeeWaitlistService.dto.PusherDTO;
import com.kyobeeWaitlistService.dto.SeatingMarketingPrefDTO;
import com.kyobeeWaitlistService.dto.SendSMSDTO;
import com.kyobeeWaitlistService.dto.SmsDetailsDTO;
import com.kyobeeWaitlistService.dto.WaitListMetricsDTO;
import com.kyobeeWaitlistService.dto.WaitlistMetrics;
import com.kyobeeWaitlistService.entity.Lookup;
import com.kyobeeWaitlistService.entity.OrganizationTemplate;
import com.kyobeeWaitlistService.entity.SmsLog;
import com.kyobeeWaitlistService.service.WaitListService;
import com.kyobeeWaitlistService.util.CommonUtil;
import com.kyobeeWaitlistService.util.LoggerUtil;
import com.kyobeeWaitlistService.util.WaitListServiceConstants;
import com.kyobeeWaitlistService.util.pusherImpl.NotificationUtil;
import com.kyobeeWaitlistService.util.smsImpl.SMSUtil;

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
	
	@Autowired
	SmsLogDAO smsLogDAO;
	
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
	public WaitlistMetrics getOrganizationMetrics(Integer orgId) {
		
		WaitlistMetrics waitListMetricsDTO = organizationCustomDAO.getOrganizationMetrics(orgId);
		
		return waitListMetricsDTO;
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


	@Override
	public void getSMSDetails(GuestDTO guestDTO, SendSMSDTO sendSMSDTO) {
		String msg = "";
		String signature = "";

		SmsDetailsDTO smsDetails = organizationDAO.getSmsDetails(guestDTO.getOrganizationID());
		GuestNotificationDTO guestNotification = new GuestNotificationDTO();

		// copying the guest details
		BeanUtils.copyProperties(guestDTO, guestNotification);
		// copying the sms details
		BeanUtils.copyProperties(smsDetails, guestNotification);

		// preparing sms content
		msg = sendSMSDTO.getSmsContent();
		if (guestNotification.getSmsSignature() != null && !guestNotification.getSmsSignature().equals("")
				&& !guestNotification.getSmsSignature().equals(WaitListServiceConstants.SMS_SIGNATURE)) {
			signature = " - " + guestNotification.getSmsSignature() + "\n";
			msg = msg + signature;
		}

		SMSUtil.sendSMS(guestNotification.getSms(), msg);
	}


	@Override
	public OrganizationTemplateDTO getOrganizationTemplateByLevel(GuestDTO guestDTO,SendSMSDTO sendSMSDTO) {
		
		OrganizationTemplate orgTemplate= organizationTemplateDAO.fetchSmsTemplateForOrgByLevel(guestDTO.getOrganizationID(), guestDTO.getLanguagePref().getLangId(),sendSMSDTO.getTemplateLevel());
		OrganizationTemplateDTO orgTempalteDTO = new OrganizationTemplateDTO();
		BeanUtils.copyProperties(orgTemplate, orgTempalteDTO);
		return orgTempalteDTO;
	}


	@Override
	public SendSMSDTO getSmsContentByLevel(GuestDTO guestDTO, OrganizationTemplateDTO organizationTemplateDTO,
			WaitlistMetrics waitlistMetrics) {
		
		SendSMSDTO smsDTO = new SendSMSDTO();
		String smsContent = organizationTemplateDTO.getTemplateText();
		LoggerUtil.logInfo("smsContent " + smsContent);
		smsContent = smsContent.replace("G_name", guestDTO.getName());
		smsContent = smsContent.replace("#G_name", guestDTO.getName());
		smsContent = smsContent.replace("G_rank", guestDTO.getRank().toString());
		smsContent = smsContent.replace("Turl",
				CommonUtil.buildURL(waitlistMetrics.getClientBase(), guestDTO.getUuid()));
		smsContent = smsContent.replace("P_ahead", String.valueOf(waitlistMetrics.getTotalWaitingGuest() - 1));
		smsContent = smsContent.replace("W_time", waitlistMetrics.getTotalWaitTime().toString());
		smsDTO.setSmsContent(smsContent);
		
		return smsDTO;
	}


	@Override
	public void saveSmsLog(GuestDTO guestDTO, SendSMSDTO sendSMSDTO) {
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
 		Date date = new Date();
 		
		SmsLog log = new SmsLog();
		log.setOrgID(guestDTO.getOrganizationID());
		log.setGuestID(guestDTO.getGuestID());
		log.setPhoneNo(guestDTO.getSms());
		log.setMsgText(sendSMSDTO.getSmsContent());
		log.setProcess(WaitListServiceConstants.PROCESS);
		log.setCreatedBy(WaitListServiceConstants.ADMIN);
		log.setCreatedAt(dateFormat.format(date));
		log.setModifiedAt(dateFormat.format(date));
		
		smsLogDAO.save(log);
		
	}
	
	
}
