package com.kyobeeWaitlistService.serviceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.kyobeeWaitlistService.dao.GuestCustomDAO;
import com.kyobeeWaitlistService.dao.GuestDAO;
import com.kyobeeWaitlistService.dao.GuestMarketingPreferencesDAO;
import com.kyobeeWaitlistService.dao.LanguageKeyMappingDAO;
import com.kyobeeWaitlistService.dao.LookupDAO;
import com.kyobeeWaitlistService.dao.OrganizationCustomDAO;
import com.kyobeeWaitlistService.dao.OrganizationDAO;
import com.kyobeeWaitlistService.dto.AddUpdateGuestDTO;
import com.kyobeeWaitlistService.dto.GuestDTO;
import com.kyobeeWaitlistService.dto.GuestMarketingPreferenceDTO;
import com.kyobeeWaitlistService.dto.GuestMetricsDTO;
import com.kyobeeWaitlistService.dto.GuestResponseDTO;
import com.kyobeeWaitlistService.dto.GuestWebDTO;
import com.kyobeeWaitlistService.dto.LanguageMasterDTO;
import com.kyobeeWaitlistService.dto.PusherDTO;
import com.kyobeeWaitlistService.dto.ResponseDTO;
import com.kyobeeWaitlistService.dto.SeatingMarketingPrefDTO;
import com.kyobeeWaitlistService.dto.SendSMSDTO;
import com.kyobeeWaitlistService.dto.WaitlistMetrics;
import com.kyobeeWaitlistService.entity.Guest;
import com.kyobeeWaitlistService.entity.GuestMarketingPreferences;
import com.kyobeeWaitlistService.entity.Lookup;
import com.kyobeeWaitlistService.service.GuestService;
import com.kyobeeWaitlistService.util.CommonUtil;
import com.kyobeeWaitlistService.util.LoggerUtil;
import com.kyobeeWaitlistService.util.WaitListServiceConstants;
import com.kyobeeWaitlistService.util.Exeception.InvalidGuestException;
import com.kyobeeWaitlistService.util.pusherImpl.NotificationUtil;

@Service
@Transactional
public class GuestServiceImpl implements GuestService {

	@Autowired
	private OrganizationDAO organizationDAO;

	@Autowired
	private GuestDAO guestDAO;

	@Autowired
	private GuestCustomDAO guestCustomDAO;

	@Autowired
	private LookupDAO lookupDAO;

	@Autowired
	private GuestMarketingPreferencesDAO guestMarketingPreferencesDAO;
	
	@Autowired
	private OrganizationCustomDAO organizationCustomDAO;
	
	@Autowired
	private LanguageKeyMappingDAO languageKeyMappingDAO;
	
	@Autowired
	private RestTemplate restTemplate;

	@Override
	public GuestMetricsDTO getGuestMetrics(Integer guestId, Integer orgId) {
		// need to create proc for this
		Integer orgWaitTime = organizationDAO.getOrganizationWaitTime(orgId);
		Integer guestCount = guestDAO.getGuestCount(orgId, guestId);
		Integer guestRank = guestDAO.getGuestRank(orgId, guestId);

		GuestMetricsDTO metricsDTO = new GuestMetricsDTO();

		metricsDTO.setGuestRank(guestRank);
		metricsDTO.setTotalWaitTime(guestCount <= 0 ? orgWaitTime : ((guestCount) * orgWaitTime) + orgWaitTime);
		metricsDTO.setOrgWaitTime(orgWaitTime);
		metricsDTO.setGuestAheadCount(guestCount <= 0 ? 0 : (guestCount));

		return metricsDTO;
	}

	@Override
	public void resetOrganizationsByOrgId(Long orgId) {

		final HashMap<String, Object> rootMap = new LinkedHashMap<>();
		LoggerUtil.logInfo("In WaitListService : reseting organization");
		// reseting org related guest data
		organizationCustomDAO.resetOrganizationByOrgId(orgId);
		rootMap.put("op", WaitListServiceConstants.RESET_ORGANIZATION_PUSHER);
		rootMap.put("orgId", orgId);
		NotificationUtil.sendMessage(rootMap, WaitListServiceConstants.PUSHER_CHANNEL_ENV + "_" + orgId);

	}

	@Override
	public GuestResponseDTO fetchGuestList(Integer orgId, Integer pageSize, Integer pageNo, String searchText) {
		Integer startIndex = 0;
		GuestResponseDTO guestResponse = new GuestResponseDTO();
		guestResponse.setPageNo(pageNo);
		if (pageNo != 1) {
			pageNo=pageNo-1;
			startIndex = pageSize * pageNo;
		}
		List<Guest> guestList;
  
		Integer totalGuest=0;	
			guestList = guestCustomDAO.fetchAllGuestList(orgId, pageSize, startIndex, searchText);
			//for fetching total guest count 
			totalGuest=guestCustomDAO.fetchAllGuestListCount(orgId,searchText);

		List<GuestDTO> guestDTOs = new ArrayList<>();
		

		for (Guest guest : guestList) {
			GuestDTO guestDTO = new GuestDTO();
			BeanUtils.copyProperties(guest, guestDTO);

			// for adding language
			LanguageMasterDTO languageMasterDTO = new LanguageMasterDTO();
			languageMasterDTO.setLangID(guest.getLangmaster().getLangID());
			languageMasterDTO.setLangIsoCode(guest.getLangmaster().getLangIsoCode());
			languageMasterDTO.setLangName(guest.getLangmaster().getLangName());
			guestDTO.setLanguagePref(languageMasterDTO);

			List<SeatingMarketingPrefDTO> seatingPrefList = new ArrayList<>();
			List<SeatingMarketingPrefDTO> marketingPrefList = new ArrayList<>();

			// for arranging seating pref in list
			String seatingPref = guest.getSeatingPreference();
			if (seatingPref != null) {
				String[] stringSeatingPref = seatingPref.split(",");
				List<Lookup> seatingLookup = lookupDAO.fetchLookup(stringSeatingPref);

				for (Lookup lookup : seatingLookup) {
					SeatingMarketingPrefDTO seatingPrefence = new SeatingMarketingPrefDTO();
					seatingPrefence.setPrefValueId(lookup.getLookupID());
					seatingPrefence.setPrefValue(lookup.getName());
					seatingPrefList.add(seatingPrefence);
				}
			}

			guestDTO.setSeatingPreference(seatingPrefList);
			//for fetching marketing pref for guest and arranging in list 
			List<Lookup> marketingLookup = lookupDAO.fetchLookupForGuest(guestDTO.getGuestID());

			for (Lookup lookup : marketingLookup) {
				SeatingMarketingPrefDTO marketingPrefence = new SeatingMarketingPrefDTO();
				marketingPrefence.setPrefValueId(lookup.getLookupID());
				marketingPrefence.setPrefValue(lookup.getName());
				marketingPrefList.add(marketingPrefence);
			}

			guestDTO.setMarketingPreference(marketingPrefList);

			guestDTOs.add(guestDTO);
		}
		guestResponse.setTotalRecords(totalGuest);
		guestResponse.setRecords(guestDTOs);
		return guestResponse;
	}

	@Override
	public GuestResponseDTO fetchGuestHistoryList(Integer orgId,Integer pageSize,Integer pageNo,String searchText,String clientTimezone,Integer sliderMaxTime,Integer sliderMinTime,String statusOption) {
		
		GuestResponseDTO guestResponse = new GuestResponseDTO();
		guestResponse.setPageNo(pageNo);
		Integer startIndex = 0;
		Integer totalGuest=0;
		if (pageNo != 1) {
			pageNo=pageNo-1;
			startIndex = pageSize * pageNo;
		}
		List<Guest> guestList;
		LoggerUtil.logInfo(clientTimezone);
		guestList = guestCustomDAO.fetchAllGuestHistoryList(orgId,pageSize,startIndex,searchText,clientTimezone,sliderMaxTime,sliderMinTime,statusOption);
		//for fetching total guest count 
		totalGuest= guestCustomDAO.fetchAllGuestHistoryListCount(orgId, searchText, clientTimezone, sliderMaxTime, sliderMinTime, statusOption);
		List<GuestDTO> guestDTOs = new ArrayList<>();
		

		for (Guest guest : guestList) {
			GuestDTO guestDTO = new GuestDTO();
			BeanUtils.copyProperties(guest, guestDTO);

			// for adding language
			LanguageMasterDTO languageMasterDTO = new LanguageMasterDTO();
			languageMasterDTO.setLangID(guest.getLangmaster().getLangID());
			languageMasterDTO.setLangIsoCode(guest.getLangmaster().getLangIsoCode());
			languageMasterDTO.setLangName(guest.getLangmaster().getLangName());
			guestDTO.setLanguagePref(languageMasterDTO);

			List<SeatingMarketingPrefDTO> seatingPrefList = new ArrayList<>();
			List<SeatingMarketingPrefDTO> marketingPrefList = new ArrayList<>();

			// for arranging seating pref in list
			String seatingPref = guest.getSeatingPreference();
			if (seatingPref != null) {
				String[] stringSeatingPref = seatingPref.split(",");
				List<Lookup> seatingLookup = lookupDAO.fetchLookup(stringSeatingPref);

				for (Lookup lookup : seatingLookup) {
					SeatingMarketingPrefDTO seatingPrefence = new SeatingMarketingPrefDTO();
					seatingPrefence.setPrefValueId(lookup.getLookupID());
					seatingPrefence.setPrefValue(lookup.getName());
					seatingPrefList.add(seatingPrefence);
				}
			}

			guestDTO.setSeatingPreference(seatingPrefList);

			// for arranging marketing pref in list

			List<Lookup> marketingLookup = lookupDAO.fetchLookupForGuest(guestDTO.getGuestID());

			for (Lookup lookup : marketingLookup) {
				SeatingMarketingPrefDTO marketingPrefence = new SeatingMarketingPrefDTO();
				marketingPrefence.setPrefValueId(lookup.getLookupID());
				marketingPrefence.setPrefValue(lookup.getName());
				marketingPrefList.add(marketingPrefence);
			}

			guestDTO.setMarketingPreference(marketingPrefList);

			guestDTOs.add(guestDTO);
		}
		
		guestResponse.setTotalRecords(totalGuest);
		guestResponse.setRecords(guestDTOs);
		return guestResponse;
	}

	@Override
	public AddUpdateGuestDTO addGuest(GuestDTO guestDTO) {
		String seatingPref;

		String tinyURL = "";
		String guestUUID = UUID.randomUUID().toString().substring(0, 8);
		seatingPref = convertToString(guestDTO.getSeatingPreference());
		guestDTO.setUuid(guestUUID);
		WaitlistMetrics waitlistMetrics = guestCustomDAO.addGuest(guestDTO, seatingPref);
		AddUpdateGuestDTO addUpdateGuestDTO = new AddUpdateGuestDTO();
		addUpdateGuestDTO.setWaitlistMetrics(waitlistMetrics);
		addUpdateGuestDTO.setGuestUUID(guestUUID);
		addUpdateGuestDTO.setLanguagePref(guestDTO.getLanguagePref());
		addUpdateGuestDTO.setOrgId(guestDTO.getOrganizationID());
		addUpdateGuestDTO.setOp(WaitListServiceConstants.ADD_STATUS);
        LoggerUtil.logInfo("client base:"+waitlistMetrics.getClientBase());
		tinyURL = CommonUtil.buildURL(waitlistMetrics.getClientBase(), guestUUID);
		addUpdateGuestDTO.setTinyURL(tinyURL);
		List<GuestMarketingPreferences> guestMarketingPref=new ArrayList<>();
		GuestMarketingPreferences guestMarketingPreferences;
		Optional<Guest> guest = guestDAO.findById(waitlistMetrics.getGuestId());

		List<SeatingMarketingPrefDTO> marketingPreference = guestDTO.getMarketingPreference();
		for (SeatingMarketingPrefDTO pref : marketingPreference) {

			guestMarketingPreferences = new GuestMarketingPreferences();
			if (guest.isPresent()) {
				Guest guestDeatils = guest.get();
				LoggerUtil.logInfo(" id " + guestDeatils.getGuestID());
				guestMarketingPreferences.setGuest(guestDeatils);
			}
			Integer lookupId = pref.getPrefValueId();
			Optional<Lookup> lookup = lookupDAO.findById(lookupId);
			if (lookup.isPresent()) {
				guestMarketingPreferences.setLookup(lookup.get());
			}
			guestMarketingPref.add(guestMarketingPreferences);
		}
		guestMarketingPreferencesDAO.saveAll(guestMarketingPref);
		NotificationUtil.sendMessage(addUpdateGuestDTO, WaitListServiceConstants.PUSHER_CHANNEL_ENV+"_"+guestDTO.getOrganizationID());
		
		//API call for sending sms to Guest
		
		SendSMSDTO sendSMSDTO = new SendSMSDTO();
		sendSMSDTO.setGuestId(waitlistMetrics.getGuestId());
		sendSMSDTO.setTemplateLevel(WaitListServiceConstants.TEMP_LEVEL_FIRST);
		sendSMSDTO.setOrgId(guestDTO.getOrganizationID());
	    restTemplate.postForObject(	WaitListServiceConstants.SEND_SMS_API, sendSMSDTO, ResponseDTO.class);
	    
		return addUpdateGuestDTO;
	}

	// to convert seating or marketing pref
	String convertToString(List<SeatingMarketingPrefDTO> seatingOrMarketingPref) {
		StringBuilder stringPref = new StringBuilder();
		for (SeatingMarketingPrefDTO pref : seatingOrMarketingPref) {
			stringPref.append(pref.getPrefValueId());
			stringPref.append(",");
		}
		return stringPref.toString();
	}

	@Override
	public AddUpdateGuestDTO updateGuestDetails(GuestDTO guestDTO) {

		String seatingPref;
		String tinyURL = "";
		
		seatingPref = convertToString(guestDTO.getSeatingPreference());
		
		
		WaitlistMetrics waitlistMetrics = guestCustomDAO.updateGuestDetails(guestDTO, seatingPref);
		AddUpdateGuestDTO addUpdateGuestDTO = new AddUpdateGuestDTO();
		addUpdateGuestDTO.setWaitlistMetrics(waitlistMetrics);
		addUpdateGuestDTO.setLanguagePref(guestDTO.getLanguagePref());
		addUpdateGuestDTO.setOrgId(guestDTO.getOrganizationID());
		addUpdateGuestDTO.setOp(WaitListServiceConstants.UPDATE_STATUS);
		tinyURL = CommonUtil.buildURL(waitlistMetrics.getClientBase(), guestDTO.getUuid());
		addUpdateGuestDTO.setTinyURL(tinyURL);

		guestMarketingPreferencesDAO.deleteGuestMarketingPref(guestDTO.getGuestID());
		// Guest marketing pref
		GuestMarketingPreferences guestMarketingPreferences;
		Optional<Guest> guest = guestDAO.findById(guestDTO.getGuestID());

		List<SeatingMarketingPrefDTO> marketingPreference = guestDTO.getMarketingPreference();
		List<GuestMarketingPreferences> guestMarketingPref=new ArrayList<>();
		for (SeatingMarketingPrefDTO pref : marketingPreference) {

			guestMarketingPreferences = new GuestMarketingPreferences();
			if (guest.isPresent()) {
				LoggerUtil.logInfo("" + guest.get().getGuestID());
				guestMarketingPreferences.setGuest(guest.get());
			}
			Integer lookupId = pref.getPrefValueId();
			Optional<Lookup> lookup = lookupDAO.findById(lookupId);
			if (lookup.isPresent()) {
				guestMarketingPreferences.setLookup(lookup.get());
			}
			guestMarketingPref.add(guestMarketingPreferences);
		}
		guestMarketingPreferencesDAO.saveAll(guestMarketingPref);
		NotificationUtil.sendMessage(addUpdateGuestDTO, WaitListServiceConstants.PUSHER_CHANNEL_ENV+"_"+guestDTO.getOrganizationID());
		return addUpdateGuestDTO;
	}

	@Override
	public void updateGuestStatus(Integer guestId, Integer orgId, String status) throws InvalidGuestException {

		WaitlistMetrics waitlistMetrics = guestCustomDAO.updateGuestStatus(guestId, orgId, status);
		PusherDTO pusherDTO=new PusherDTO();
		pusherDTO.setFrom(WaitListServiceConstants.FROM_ADMIN);
		pusherDTO.setOrgId(orgId);
		pusherDTO.setOp(status);
		pusherDTO.setWaitlistMetrics(waitlistMetrics);
		NotificationUtil.sendMessage(pusherDTO, WaitListServiceConstants.PUSHER_CHANNEL_ENV+"_"+orgId);
		
		LoggerUtil.logInfo("guest to be notified:"+waitlistMetrics.getGuestToBeNotified());
		if(guestId < waitlistMetrics.getGuestToBeNotified())
		{
			GuestDTO guestToBeNotified = fetchGuestDetails(waitlistMetrics.getGuestToBeNotified(), null);

			SendSMSDTO sendSMSDTO = new SendSMSDTO();
			sendSMSDTO.setGuestId(guestToBeNotified.getGuestID());
			sendSMSDTO.setTemplateLevel(WaitListServiceConstants.TEMP_LEVEL_SECOND);
			sendSMSDTO.setOrgId(guestToBeNotified.getOrganizationID());

			restTemplate.postForObject(WaitListServiceConstants.SEND_SMS_API, sendSMSDTO, ResponseDTO.class);	
		}
		
		/*
		 * StatusUpdateResponseDTO statusUpdateResponseDTO=new StatusUpdateResponseDTO();
		 * Optional<Guest> guest = guestDAO.findById(guestId); if (guest.isPresent()) {
		 * statusUpdateResponseDTO.setGuest(guest.get()); }
		 * statusUpdateResponseDTO.setOrgId(orgId);
		 * statusUpdateResponseDTO.setWaitlistMetrics(waitlistMetrics);
		 */
	}

	public GuestDTO fetchGuestDetails(Integer guestID, String guestUUID) throws InvalidGuestException{

		Guest guest = null;

		if (guestID != null) {
			LoggerUtil.logInfo("Fetching guest details of guestID:" + guestID);
			guest = guestDAO.fetchGuestById(guestID);
		} else {
			LoggerUtil.logInfo("Fetching guest details of guestUUID:" + guestUUID);
			guest = guestDAO.fetchGuestByUUID(guestUUID);
		}

		GuestDTO guestDTO = new GuestDTO();
		if(guest!=null) {
			BeanUtils.copyProperties(guest, guestDTO);

			// for adding language
			LanguageMasterDTO languageMasterDTO = new LanguageMasterDTO();
			languageMasterDTO.setLangID(guest.getLangmaster().getLangID());
			languageMasterDTO.setLangIsoCode(guest.getLangmaster().getLangIsoCode());
			languageMasterDTO.setLangName(guest.getLangmaster().getLangName());
			guestDTO.setLanguagePref(languageMasterDTO);

			List<SeatingMarketingPrefDTO> seatingPrefList = new ArrayList<>();
			List<SeatingMarketingPrefDTO> marketingPrefList = new ArrayList<>();

			// for arranging seating pref in list
			String seatingPref = guest.getSeatingPreference();
			if (seatingPref != null) {
				String[] stringSeatingPref = seatingPref.split(",");
				List<Lookup> seatingLookup = lookupDAO.fetchLookup(stringSeatingPref);

				for (Lookup lookup : seatingLookup) {
					SeatingMarketingPrefDTO seatingPrefence = new SeatingMarketingPrefDTO();
					seatingPrefence.setPrefValueId(lookup.getLookupID());
					seatingPrefence.setPrefValue(lookup.getName());
					seatingPrefList.add(seatingPrefence);
				}
			}

			guestDTO.setSeatingPreference(seatingPrefList);

			// for arranging marketing pref in list

			List<Lookup> marketingLookup = lookupDAO.fetchLookupForGuest(guestDTO.getGuestID());

			for (Lookup lookup : marketingLookup) {
				SeatingMarketingPrefDTO marketingPrefence = new SeatingMarketingPrefDTO();
				marketingPrefence.setPrefValueId(lookup.getLookupID());
				marketingPrefence.setPrefValue(lookup.getName());
				marketingPrefList.add(marketingPrefence);
			}

			guestDTO.setMarketingPreference(marketingPrefList);

		}
		else {
			throw new InvalidGuestException("Requested Guest does not exists.");
		}
	
		return guestDTO;
	}
	

	@Override
	public GuestDTO fetchGuestByContact(Integer orgID, String contactNo) throws InvalidGuestException {

		List<Guest> guest = guestCustomDAO.fetchGuestByContact(orgID, contactNo);

		if (!(guest.isEmpty())) {
			GuestDTO guestDTO = new GuestDTO();
			BeanUtils.copyProperties(guest.get(0), guestDTO);

			// for adding language
			LanguageMasterDTO languageMasterDTO = new LanguageMasterDTO();
			languageMasterDTO.setLangID(guest.get(0).getLangmaster().getLangID());
			guestDTO.setLanguagePref(languageMasterDTO);

			List<SeatingMarketingPrefDTO> seatingPrefList = new ArrayList<>();
			List<SeatingMarketingPrefDTO> marketingPrefList = new ArrayList<>();

			// for arranging seating pref in list
			String seatingPref = guest.get(0).getSeatingPreference();
			if (seatingPref != null) {
				String[] stringSeatingPref = seatingPref.split(",");
				List<Lookup> seatingLookup = lookupDAO.fetchLookup(stringSeatingPref);

				for (Lookup lookup : seatingLookup) {
					SeatingMarketingPrefDTO seatingPrefence = new SeatingMarketingPrefDTO();
					seatingPrefence.setPrefValueId(lookup.getLookupID());
					seatingPrefence.setPrefValue(lookup.getName());
					seatingPrefList.add(seatingPrefence);
				}
			}
			guestDTO.setSeatingPreference(seatingPrefList);

			// for arranging marketing pref in list

			List<Lookup> marketingLookup = lookupDAO.fetchLookupForGuest(guestDTO.getGuestID());

			for (Lookup lookup : marketingLookup) {
				SeatingMarketingPrefDTO marketingPrefence = new SeatingMarketingPrefDTO();
				marketingPrefence.setPrefValueId(lookup.getLookupID());
				marketingPrefence.setPrefValue(lookup.getName());
				marketingPrefList.add(marketingPrefence);
			}

			guestDTO.setMarketingPreference(marketingPrefList);
			return guestDTO;

		} else {
			throw new InvalidGuestException("Requested Guest does not exists.");

		}
	}

	@Override
	public void addMarketingPref(GuestMarketingPreferenceDTO marketingPrefDTO) {
		//for saving marketing pref of guest after saving guest data
		List<GuestMarketingPreferences> guestMarketingPref = new ArrayList<GuestMarketingPreferences>();
		GuestMarketingPreferences guestMarketingPreferences = null;
		Optional<Guest> guest = guestDAO.findById(marketingPrefDTO.getGuestID());

		List<SeatingMarketingPrefDTO> marketingPreference = marketingPrefDTO.getMarketingPreference();
		for (SeatingMarketingPrefDTO pref : marketingPreference) {

			guestMarketingPreferences = new GuestMarketingPreferences();
			if (guest.isPresent()) {
				Guest guestDeatils = guest.get();
				LoggerUtil.logInfo(" id " + guestDeatils.getGuestID());
				guestMarketingPreferences.setGuest(guestDeatils);
			}
			Integer lookupId = pref.getPrefValueId();
			Optional<Lookup> lookup = lookupDAO.findById(lookupId);
			if (lookup.isPresent()) {
				guestMarketingPreferences.setLookup(lookup.get());
			}
			guestMarketingPref.add(guestMarketingPreferences);
		}
		guestMarketingPreferencesDAO.saveAll(guestMarketingPref);

	}

	@Override
	public GuestWebDTO fetchguestDetails(String guestUUID) throws InvalidGuestException {
		
		GuestDTO guest = fetchGuestDetails(null, guestUUID);
		GuestWebDTO guestWeb=new GuestWebDTO();
		if(guest!=null) {
			BeanUtils.copyProperties(guest, guestWeb);
			Map<String,String> languageMap = new HashMap<>();
			List<LanguageMasterDTO> languageKeyMap=languageKeyMappingDAO.fetchByLangIsoCode("en");
			languageKeyMap.forEach(r -> {languageMap.put(r.getKeyName(),r.getValue());});
			guestWeb.setLanguageKeyMap(languageMap);
		}

		return guestWeb;
	}

}
