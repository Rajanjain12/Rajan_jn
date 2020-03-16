package com.kyobeeWaitlistService.serviceImpl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kyobeeWaitlistService.dao.GuestDAO;
import com.kyobeeWaitlistService.dao.LangMasterDAO;
import com.kyobeeWaitlistService.dao.LanguageKeyMappingDAO;
import com.kyobeeWaitlistService.dao.LookupDAO;
import com.kyobeeWaitlistService.dao.OrganizationCategoryDAO;
import com.kyobeeWaitlistService.dao.OrganizationCustomDAO;
import com.kyobeeWaitlistService.dao.OrganizationDAO;
import com.kyobeeWaitlistService.dao.OrganizationTemplateDAO;
import com.kyobeeWaitlistService.dao.SmsLogDAO;
import com.kyobeeWaitlistService.dto.GuestDTO;
import com.kyobeeWaitlistService.dto.GuestNotificationDTO;
import com.kyobeeWaitlistService.dto.LanguageKeyMappingDTO;
import com.kyobeeWaitlistService.dto.LanguageMasterDTO;
import com.kyobeeWaitlistService.dto.OrgPrefKeyMapDTO;
import com.kyobeeWaitlistService.dto.OrgSettingDTO;
import com.kyobeeWaitlistService.dto.OrganizationTemplateDTO;
import com.kyobeeWaitlistService.dto.PusherDTO;
import com.kyobeeWaitlistService.dto.SeatingMarketingPrefDTO;
import com.kyobeeWaitlistService.dto.SendSMSDTO;
import com.kyobeeWaitlistService.dto.SmsTemplateDTO;
import com.kyobeeWaitlistService.dto.WaitlistMetrics;
import com.kyobeeWaitlistService.entity.LangMaster;
import com.kyobeeWaitlistService.entity.Languagekeymapping;
import com.kyobeeWaitlistService.entity.Lookup;
import com.kyobeeWaitlistService.entity.Organization;
import com.kyobeeWaitlistService.entity.OrganizationCategory;
import com.kyobeeWaitlistService.entity.OrganizationTemplate;
import com.kyobeeWaitlistService.entity.SmsLog;
import com.kyobeeWaitlistService.service.WaitListService;
import com.kyobeeWaitlistService.util.CommonUtil;
import com.kyobeeWaitlistService.util.LoggerUtil;
import com.kyobeeWaitlistService.util.WaitListServiceConstants;
import com.kyobeeWaitlistService.util.Exeception.InvalidGuestException;
import com.kyobeeWaitlistService.util.pusherImpl.NotificationUtil;
import com.kyobeeWaitlistService.util.smsImpl.SMSUtil;
import com.kyobeeWaitlistService.service.GuestService;

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

	@Autowired
	GuestService guestService;

	@Autowired
	OrganizationCategoryDAO organizationCategoryDAO;
	
	@Autowired
	LangMasterDAO langMasterDAO;

	@Override
	public HashMap<String, Object> updateLanguagesPusher() {

		HashMap<String, Object> rootMap = new LinkedHashMap<>();
		Long flagCount = languageKeyMappingDAO.getUpdateFlagCount(WaitListServiceConstants.MARK_AS_LANGUAGE_UPDATED);
		LoggerUtil.logInfo("Update Flag count:" + flagCount);
		// sending pusher if there is any language key map updated
		if (flagCount > 0) {
			rootMap.put("op", "REFRESH_LANGUAGE_PUSHER");
			NotificationUtil.sendMessage(rootMap, WaitListServiceConstants.GLOBAL_CHANNEL_ENV);
			languageKeyMappingDAO.resetUpdateFlagCount(WaitListServiceConstants.MARK_AS_LANGUAGE_RESET);
		}
		return rootMap;
	}

	// for fetching organization related matrix
	@Override
	public WaitlistMetrics getOrganizationMetrics(Integer orgId) {

		WaitlistMetrics waitListMetricsDTO = organizationCustomDAO.getOrganizationMetrics(orgId);

		return waitListMetricsDTO;
	}

	// for updating organization metrics setting

	@Override
	public PusherDTO updateOrgSettings(Integer orgId, Integer perPartyWaitTime, Integer numberOfUsers) {
		WaitlistMetrics waitlistMetrics = organizationCustomDAO.updateOrgSettings(orgId, perPartyWaitTime,
				numberOfUsers);

		// for sending details to pusher
		PusherDTO pusherDTO = new PusherDTO();

		pusherDTO.setWaitlistMetrics(waitlistMetrics);
		pusherDTO.setOp("NOTIFY_USER");
		pusherDTO.setFrom("ADMIN");
		pusherDTO.setOrgId(orgId);

		NotificationUtil.sendMessage(pusherDTO, WaitListServiceConstants.PUSHER_CHANNEL_ENV + "_" + orgId);

		return pusherDTO;
	}

	@Override
	public OrgPrefKeyMapDTO fetchOrgPrefandKeyMap(Integer orgId) {
		// fetch seating pref and marketing pref associated with org
		List<Lookup> lookupList = lookupDAO.fetchOrgSeatingAndMarketingPref(orgId,
				WaitListServiceConstants.SEATING_PREF_ID, WaitListServiceConstants.MARKETING_PREF_ID);
		OrgPrefKeyMapDTO orgPrefDTO = new OrgPrefKeyMapDTO();
		List<SeatingMarketingPrefDTO> seatingPrefList = new ArrayList<>();
		List<SeatingMarketingPrefDTO> marketingPrefList = new ArrayList<>();

		List<LanguageMasterDTO> languageList = languageKeyMappingDAO.fetchByLangIsoCodeAndScreenName(
				WaitListServiceConstants.ENG_ISO_CODE, WaitListServiceConstants.SCREEN_NAME);

		Map<String, String> keymap = new HashMap<>();

		for (LanguageMasterDTO langMaster : languageList) {
			keymap.put(langMaster.getKeyName(), langMaster.getValue());

		}

		SeatingMarketingPrefDTO seatingPref;
		SeatingMarketingPrefDTO marketingPref;
		// to separate seating pref and marketing pref
		for (Lookup lookup : lookupList) {
			if (lookup.getLookuptype().getLookupTypeID() == WaitListServiceConstants.SEATING_PREF_ID) {
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
			} else if (lookup.getLookuptype().getLookupTypeID() == WaitListServiceConstants.MARKETING_PREF_ID) {
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

		signature = organizationDAO.getSmsDetails(guestDTO.getOrganizationID());
		GuestNotificationDTO guestNotification = new GuestNotificationDTO();

		// copying the guest details
		BeanUtils.copyProperties(guestDTO, guestNotification);
		guestNotification.setSmsSignature(signature);

		// preparing sms content
		msg = sendSMSDTO.getSmsContent();
		if (guestNotification.getSmsSignature() != null && !guestNotification.getSmsSignature().equals("")
				&& !guestNotification.getSmsSignature().equals(WaitListServiceConstants.SMS_SIGNATURE)) {
			signature = " - " + guestNotification.getSmsSignature() + "\n";
			msg = msg + signature;
		}

		SMSUtil.sendSMS(guestNotification.getContactNo(), msg);
	}

	@Override
	public OrganizationTemplateDTO getOrganizationTemplateByLevel(GuestDTO guestDTO, SendSMSDTO sendSMSDTO) {

		OrganizationTemplate orgTemplate = organizationTemplateDAO.fetchSmsTemplateForOrgByLevel(
				guestDTO.getOrganizationID(), guestDTO.getLanguagePref().getLangID(), sendSMSDTO.getTemplateLevel());
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
		log.setPhoneNo(guestDTO.getContactNo());
		log.setMsgText(sendSMSDTO.getSmsContent());
		log.setProcess(WaitListServiceConstants.PROCESS);
		log.setCreatedBy(WaitListServiceConstants.ADMIN);
		log.setCreatedAt(dateFormat.format(date));
		log.setModifiedAt(dateFormat.format(date));

		smsLogDAO.save(log);

	}

	@Override
	public void sendSMS(SendSMSDTO sendSMSDTO) throws InvalidGuestException {

		GuestDTO guest = guestService.fetchGuestDetails(sendSMSDTO.getGuestId(), null);
		if (sendSMSDTO.getSmsContent() == null) {
			WaitlistMetrics waitlistMetrics = getOrganizationMetrics(sendSMSDTO.getOrgId());
			OrganizationTemplateDTO smsTemplate = getOrganizationTemplateByLevel(guest, sendSMSDTO);
			sendSMSDTO = getSmsContentByLevel(guest, smsTemplate, waitlistMetrics);
		}
		getSMSDetails(guest, sendSMSDTO);
		saveSmsLog(guest, sendSMSDTO);
	}

	@Override
	public void updateOrgSetting(OrgSettingDTO orgSettingDTO) {

		// deleting old seating and marketing preference
		organizationCategoryDAO.deleteOrgPreference(orgSettingDTO.getOrgId());

		// Inserting seating and marketing preference
		List<SeatingMarketingPrefDTO> seatingPrefDTO = orgSettingDTO.getSeatingPreference();
		List<SeatingMarketingPrefDTO> marketingPrefDTO = orgSettingDTO.getMarketingPreference();
		List<SeatingMarketingPrefDTO> seatingMarketingPrefList = Stream.of(seatingPrefDTO, marketingPrefDTO)
				.flatMap(x -> x.stream()).collect(Collectors.toList());

		List<Integer> prefIdList = seatingMarketingPrefList.stream().map(prefDTO -> prefDTO.getPrefValueId())
				.collect(Collectors.toList());
		LoggerUtil.logInfo("combined id:" + prefIdList);

		List<Lookup> lookupList = lookupDAO.findByLookupIDIn(prefIdList);
		LoggerUtil.logInfo("lookup list:" + lookupList);

		Organization org = organizationDAO.findByOrganizationID(orgSettingDTO.getOrgId());
		LoggerUtil.logInfo("org :" + org);

		List<OrganizationCategory> orgCategoryList = new ArrayList<>();

		OrganizationCategory orgCategory;
		for (Lookup lookup : lookupList) {
			orgCategory = new OrganizationCategory();
			orgCategory.setOrganization(org);
			orgCategory.setLookup(lookup);
			orgCategory.setLookuptype(lookup.getLookuptype());
			orgCategoryList.add(orgCategory);
		}

		organizationCategoryDAO.saveAll(orgCategoryList);

		// update notify first setting
		organizationDAO.updateOrgNotifyFirstAndDefLang(orgSettingDTO.getNotifyFirst(), orgSettingDTO.getOrgId(),orgSettingDTO.getDefaultLanguage());

		// update template text for organization
		List<SmsTemplateDTO> smsTemplateList = orgSettingDTO.getSmsTemplateDTO();

		for (SmsTemplateDTO smsTemplate : smsTemplateList) {
			organizationTemplateDAO.updateSmsTemplateForOrgByLevel(orgSettingDTO.getOrgId(),
					smsTemplate.getLanguageID(), smsTemplate.getLevel(), smsTemplate.getTemplateText());
		}
	}

	@Override
	public OrgSettingDTO fetchOrgSetting() {

		OrgSettingDTO orgSettingDTO = new OrgSettingDTO();

		// fetching seating pref and marketing pref
		List<Lookup> lookupList = lookupDAO.fetchSeatingAndMarketingPref(WaitListServiceConstants.SEATING_PREF_ID,
				WaitListServiceConstants.MARKETING_PREF_ID);
		List<SeatingMarketingPrefDTO> seatingPrefList = new ArrayList<>();
		List<SeatingMarketingPrefDTO> marketingPrefList = new ArrayList<>();

		SeatingMarketingPrefDTO seatingPref;
		SeatingMarketingPrefDTO marketingPref;

		for (Lookup lookup : lookupList) {
			if (lookup.getLookuptype().getLookupTypeID().equals(WaitListServiceConstants.SEATING_PREF_ID)) {
				seatingPref = new SeatingMarketingPrefDTO();
				seatingPref.setPrefValue(lookup.getName());
				seatingPref.setPrefValueId(lookup.getLookupID());
				seatingPrefList.add(seatingPref);
			} else {
				marketingPref = new SeatingMarketingPrefDTO();
				marketingPref.setPrefValue(lookup.getName());
				marketingPref.setPrefValueId(lookup.getLookupID());
				marketingPrefList.add(marketingPref);
			}

		}
		orgSettingDTO.setSeatingPreference(seatingPrefList);
		orgSettingDTO.setMarketingPreference(marketingPrefList);

		List<LanguageKeyMappingDTO> langDTOList=langMasterDAO.getLanguageDetails();
		
		orgSettingDTO.setLanguageList(langDTOList);

		return orgSettingDTO;
	}

	@Override
	public List<LanguageKeyMappingDTO> fetchOrgLangKeyMap(Integer orgId) {
		List<LanguageMasterDTO> languageList = languageKeyMappingDAO
				.fetchLangKeyMapForOrganization(orgId);
		List<LanguageKeyMappingDTO> langkeyMapList = new ArrayList<>();
		LanguageKeyMappingDTO languageKeyMappingDTO;

		Map<Integer, List<LanguageMasterDTO>> langListById = languageList.stream()
				.collect(Collectors.groupingBy(LanguageMasterDTO::getLangID));
		
		for (Map.Entry<Integer, List<LanguageMasterDTO>> entry : langListById.entrySet()) {

			LanguageMasterDTO first = entry.getValue().get(0);
			languageKeyMappingDTO = new LanguageKeyMappingDTO();
			languageKeyMappingDTO.setLangId(entry.getKey());
			languageKeyMappingDTO.setLangName(first.getLangName());
			languageKeyMappingDTO.setLangIsoCode(first.getLangIsoCode());
			Map<String, String> keymap = new HashMap<>();
			keymap.put(first.getKeyName(), first.getValue());

			for (LanguageMasterDTO languageMasterDTO : entry.getValue()) {
				keymap.put(languageMasterDTO.getKeyName(), languageMasterDTO.getValue());
			}

			languageKeyMappingDTO.setLanguageMap(keymap);
			langkeyMapList.add(languageKeyMappingDTO);
		}
		return langkeyMapList;

	}
}
