/**
 * Provides rest services for Admin guest module 
 * services offered are: Basic CRUD on Guest Entity , Send notification and calculate guest 
 * wait time by organizationId
 * 	
 */

package com.kyobee.rest;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;
import java.util.UUID;

import javax.inject.Qualifier;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.jboss.logging.Logger;
import org.jboss.logging.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bandwidth.sdk.model.events.SmsEvent;
import com.kyobee.dao.impl.UserDAO;
/*import com.google.api.translate.Language;
import com.google.api.translate.Translate;*/
import com.kyobee.dto.GuestDTO;
import com.kyobee.dto.GuestMarketingPreference;
import com.kyobee.dto.GuestPreferencesDTO;
import com.kyobee.dto.GuestWebDTO;
import com.kyobee.dto.LanguageMasterDTO;
import com.kyobee.dto.MarketingPreferenceDTO;
import com.kyobee.dto.OrganizationTemplateDTO;
import com.kyobee.dto.SendSMSWrapper;
import com.kyobee.dto.SmsContentParamDTO;
import com.kyobee.dto.UserDTO;
import com.kyobee.dto.WaitlistMetrics;
import com.kyobee.dto.common.Credential;
import com.kyobee.dto.common.PaginatedResponse;
import com.kyobee.dto.common.PaginationReqParam;
import com.kyobee.dto.common.Response;
import com.kyobee.entity.Guest;
import com.kyobee.entity.GuestNotificationBean;
import com.kyobee.entity.GuestPreferences;
import com.kyobee.entity.MarketingPreference;
import com.kyobee.entity.Organization;
import com.kyobee.entity.User;
import com.kyobee.exception.RsntException;
import com.kyobee.service.ConfigurationService;
import com.kyobee.service.ISecurityService;
import com.kyobee.service.IWaitListService;
import com.kyobee.util.AppInitializer;

import com.kyobee.util.SessionContextUtil;
import com.kyobee.util.common.CommonUtil;
import com.kyobee.util.common.Constants;
import com.kyobee.util.common.LoggerUtil;
import com.kyobee.util.common.NativeQueryConstants;
import com.kyobee.util.common.RealtimefameworkPusher;
import com.kyobee.util.jms.NotificationMessageReceiver;

import com.kyobee.util.PropertyUtility;
import com.stripe.model.Order;
import com.kyobee.dao.impl.*;



@RestController
@RequestMapping("/web/rest/waitlistRestAction")
public class WaitListRestAction {

	//************************************************
	// instance variable declaration
	//*************************************************

	//@Logger
	//private Log log;
	private Logger log = Logger.getLogger(WaitListRestAction.class);
	/*@Autowired
	private ConfigurationService configurationService;*/

	@Autowired
	ISecurityService securityService;

	@Autowired
	ConfigurationService configurationService;

	@Autowired
	private IWaitListService waitListService;

	@Autowired
	SessionContextUtil sessionContextUtil;



	@Autowired
	private NotificationMessageReceiver messageReceiver;


	/**
	 * Send Notification to guests by user preferences
	 * @param guestId
	 * @return String
	 */
	//@GET
	//@Path("/checkinnotify")
	@RequestMapping(value = "/checkinnotify", method = RequestMethod.GET, produces = "application/json")
	public String checkinNotification(@RequestParam("guestid") Long guestId){
		waitListService.notifyGuestByPref(guestId);
		return Constants.RSNT_GUEST_SUCCESS;
	}

	/**
	 * Send Notification to guests by user preferences
	 * @param guestId
	 * @return String
	 */
	//@GET
	//@Path("/waitingListBackupUpdate")
	/*@RequestMapping(value = "/waitingListBackupUpdate", method = RequestMethod.GET, produces = "application/json")
	public String waitingListBackupUpdate(@RequestParam("guestid") Long guestId){
		waitListService.sendWaitListBackupEmail(guestId, true);
		return Constants.RSNT_GUEST_SUCCESS;
	}*/

	/**
	 * Send Notification to guests by user preferences
	 * @param guestId
	 * @return String
	 */
	//@GET
	//@Path("/waitingListBackup")
	/*@RequestMapping(value = "/waitingListBackup", method = RequestMethod.GET, produces = "application/json")
	public String waitingListBackup(@RequestParam("guestid") Long guestId){
		waitListService.sendWaitListBackupEmail(guestId, false);
		return Constants.RSNT_GUEST_SUCCESS;
	}*/

	//@GET
	//@Path("/getguestminrankbyorgid")
	@RequestMapping(value = "/getguestminrankbyorgid", method = RequestMethod.GET, produces = "application/json")
	public String getGuestMinrankByorgid(@RequestParam("orgid") Long orgid){
		//log.info("-----------------------"+waitListService.getGuestRankMin(orgid));
		return waitListService.getGuestRankMin(orgid);
	}

	/**
	 * Mark Guest as Deleted status by Admin
	 * @param guestId
	 * @return Deleted Guestdetails
	 */
	//@GET
	//@Path("/deleted")
	//@Produces(MediaType.APPLICATION_JSON)
	@RequestMapping(value = "/deleted", method = RequestMethod.GET, produces = "application/json")
	public String markGuestAsDeleted(@RequestParam("guestid") long guestId){
		log.info("Entering :: markGuestAsDeleted");
		final Map<String, Object> rootMap = new LinkedHashMap<String, Object>();
		final List<String> errorArray = new ArrayList<String>(0);
		Guest guestEntity =  null;
		try {
			guestEntity =  waitListService.getGuestById(guestId);
			guestEntity.setSeatedTime(new Date());
			//Set the status to deleted
			guestEntity.setStatus(Constants.RSNT_GUEST_DELETED);
			guestEntity.setUpdatedTime(new Date());
			guestEntity = waitListService.saveorUpdateGuest(guestEntity);
			//Trigger the Notification of all following guest which are bumbed up in queue
			log.info("guestEntity.getCalloutCount()---"+guestEntity.getCalloutCount());
			log.info("guestEntity.getIncompleteParty()---"+guestEntity.getIncompleteParty());

			if(checkMarkerForStatusChange(guestEntity)){
				waitListService.notifyUsersOnStatusChange(guestId, guestEntity.getOrganizationID());
			}
			nonCloseParent(guestEntity.getGuestPreferences());

			waitListService.updateTotalWaitTime(guestEntity.getOrganizationID(), false);

			rootMap.put(Constants.RSNT_ERROR, "");
			rootMap.put(Constants.RSNT_GUEST,guestEntity);
			//rootMap.put(Constants.RSNT_GUEST_RANK_MIN, waitListService.getGuestRankMin(guestEntity.getOrganizationID()));
			final JSONObject jsonObject = JSONObject.fromObject(rootMap);
			return jsonObject.toString();
		} catch (RsntException e) {
			e.printStackTrace();
			log.error("markGuestAsSeated() - failed:", e);
			rootMap.put("id", -1);
			rootMap.put(Constants.RSNT_ERROR, "System Error - markGuestAsDeleted failed");
			rootMap.put("fieldErrors", errorArray);
			final JSONObject jsonObject = JSONObject.fromObject(rootMap);
			return jsonObject.toString();
		}

	}
	/**
	 * Convert GuestVo to Guest Entity
	 * @param guest --GuestVO object
	 * @return Guest-Guest Entity
	 */
	public Guest convertGuesVoToEntity(GuestDTO guest) {
		Guest guestObj = new Guest();
		guestObj.setName(guest.getName());
		guestObj.setNote(guest.getNote());
		guestObj.setOrganizationID(guest.getOrganizationID());
		guestObj.setEmail(guest.getEmail());
		guestObj.setSms(guest.getSms());
		guestObj.setOrganizationID(guest.getOrganizationID());
		guestObj.setNoOfChildren(guest.getNoOfChildren()); // change by krupali, line 181 to line 183 and line 185,186 (14/06/2017)
		guestObj.setNoOfAdults(guest.getNoOfAdults());
		guestObj.setNoOfInfants(guest.getNoOfInfants());
		guestObj.setNoOfPeople(guest.getNoOfPeople());
		guestObj.setQuoteTime(guest.getQuoteTime());
		guestObj.setPartyType(guest.getPartyType());
		guestObj.setOptin(guest.isOptin());
		guestObj.setStatus(guest.getStatus());
		guestObj.setPrefType(guest.getPrefType());

		/*
		 * if(null != guest.getGuestPreferences()) { GuestPreferences guestPref
		 * = null; List<GuestPreferences> guestPreferences = new
		 * ArrayList<GuestPreferences>(guest.getGuestPreferences().size()); for
		 * (GuestPreferencesDTO pref : guest.getGuestPreferences()) { guestPref
		 * = new GuestPreferences();
		 * guestPref.setGuestPrefId(pref.getGuestPrefId());
		 * guestPref.setPrefValueId(pref.getPrefValueId());
		 * guestPref.setPrefValue(pref.getPrefValue());
		 * guestPref.setGuest(guestObj); guestPreferences.add(guestPref); }
		 * guestObj.setGuestPreferences(guestPreferences); }
		 */
		// change by sunny for marketing preference line 231 to245 at 2018-07-04

		if (null != guest.getGuestMarketingPreferences()) {
			String marketingPreference = null;
			List<GuestMarketingPreference> guestMarketingPrefList = guest.getGuestMarketingPreferences();
			for (GuestMarketingPreference o : guestMarketingPrefList) {
				if (null == marketingPreference)
					marketingPreference = o.getGuestMarketPrefValueId() + "";
				else
					marketingPreference = marketingPreference + "," + o.getGuestMarketPrefValueId();
			}
			guestObj.setMarketingPreference(marketingPreference);
			guest.setMarketingPreference(marketingPreference);
		}

		if (null != guest.getDeviceId())
			guestObj.setDeviceId(guest.getDeviceId());

		if (null != guest.getDeviceType())
			guestObj.setDeviceType(guest.getDeviceType());


		if(null != guest.getGuestMarketingPreferences()){

			guestObj.setGuestMarketingPreferences(guest.getGuestMarketingPreferences());
		}

		if (null != guest.getGuestPreferences()) {
			String seatingPreference = null;
			List<GuestPreferencesDTO> guestPrefList = guest.getGuestPreferences();
			for (GuestPreferencesDTO o : guestPrefList) {
				if (null == seatingPreference)
					seatingPreference = o.getPrefValueId() + "";
				else
					seatingPreference = seatingPreference + "," + o.getPrefValueId();
			}
			guestObj.setSeatingPreference(seatingPreference);
			guest.setSeatingPreference(seatingPreference);
		}
		if (null != guest.getSeatingPreference())
			guestObj.setSeatingPreference(guest.getSeatingPreference());
		/*
		 * if(null != guest.getLanguagePref())
		 * guestObj.setLanguagePrefID(guest.getLanguagePref());
		 */
		guestObj.setLanguagePrefID(guest.getLanguagePref());
		if (null != guest && null != guest.getGuestID()) {
			guestObj.setUpdatedTime(new Date());
			guestObj.setUuid(guest.getUuid());
			guestObj.setGuestID(guest.getGuestID());
			guestObj.setRank(guest.getRank());
			guestObj.setCalloutCount(guest.getCalloutCount());
		} else {
			guestObj.setUuid(UUID.randomUUID().toString().substring(0, 8));
			guestObj.setCreatedTime(new Date());
			guestObj.setCheckinTime(new Date());
		}
		return guestObj;
	}

	public List<GuestPreferences> convertSeatingPreferencesVotoEntity(List<GuestPreferencesDTO>  prefDto,Guest guest) {
		if(null != prefDto) {
			GuestPreferences guestPref = null;
			List<GuestPreferences> guestPreferences = new ArrayList<GuestPreferences>(prefDto.size());
			for (GuestPreferencesDTO pref : prefDto) {
				guestPref = new GuestPreferences();
				guestPref.setGuestPrefId(pref.getGuestPrefId());
				guestPref.setPrefValueId(pref.getPrefValueId());
				guestPref.setPrefValue(pref.getPrefValue());
				guestPref.setGuest(guest);
				guestPreferences.add(guestPref);
			}
			return guestPreferences;
		}
		return null;
	}
	public void nonCloseParent(List<GuestPreferences> guestPreferences){
		for (GuestPreferences pref : guestPreferences) {
			pref.setGuest(null);
		}

	}
	/**
	 * Convert GuestEntity to Guest Value Object
	 * @param guest -Guest Entity
	 * @return GuestDTO
	 */
	public GuestDTO convertGuesEntityToVo(Guest guest, Map<Integer, String> guestPreferenceMap,
			Map<Integer, String> guestMarketingPreferenceMap) {
		GuestDTO guestObj = null;
		try {
			guestObj = new GuestDTO();
			guestObj.setName(guest.getName());
			guestObj.setNote(guest.getNote());
			guestObj.setOrganizationID(guest.getOrganizationID());
			guestObj.setEmail(guest.getEmail());
			guestObj.setSms(guest.getSms());
			guestObj.setOrganizationID(guest.getOrganizationID());
			if (guest.getNoOfChildren() != null) {
				guestObj.setNoOfChildren(guest.getNoOfChildren());
			}
			if (guest.getNoOfAdults() != null) {
				guestObj.setNoOfAdults(guest.getNoOfAdults());
			}
			if (guest.getNoOfInfants() != null) {
				guestObj.setNoOfInfants(guest.getNoOfInfants());
			}
			guestObj.setNoOfPeople(guest.getNoOfPeople());
			if (guest.getQuoteTime() != null) {
				guestObj.setQuoteTime(guest.getQuoteTime());
			}
			if (guest.getPartyType() != null) {
				guestObj.setPartyType(guest.getPartyType());
			}
			guestObj.setLanguagePref(guest.getLanguagePrefID());

			guestObj.setOptin(guest.isOptin());
			guestObj.setStatus(guest.getStatus());
			guestObj.setPrefType(guest.getPrefType());
			guestObj.setIncompleteParty(guest.getIncompleteParty());
			/*
			 * if(null != guest.getGuestPreferences()) { GuestPreferencesDTO
			 * guestPref = null; List<GuestPreferencesDTO> guestPreferences =
			 * new
			 * ArrayList<GuestPreferencesDTO>(guest.getGuestPreferences().size()
			 * ); for (GuestPreferences pref : guest.getGuestPreferences()) {
			 * guestPref = new GuestPreferencesDTO();
			 * guestPref.setGuestPrefId(pref.getGuestPrefId());
			 * guestPref.setPrefValueId(pref.getPrefValueId());
			 * System.out.println("pref.getPrefValue()"+pref.getPrefValue());
			 * guestPref.setPrefValue(pref.getPrefValue());
			 * //guestPref.setGuest(guestObj); guestPreferences.add(guestPref);
			 * } guestObj.setGuestPreferences(guestPreferences); }
			 */

			String seatingPrefForDTO = "";
			if (null != guest.getSeatingPreference() && !"null".equals(guest.getSeatingPreference())
					&& !"".equals(guest.getSeatingPreference())) {
				try {
					String seatingPrefIdArr[] = guest.getSeatingPreference().split(",");
					for (int i = 0; i < seatingPrefIdArr.length; ++i) {
						String seatingPrefDesc = guestPreferenceMap.get(Integer.parseInt(seatingPrefIdArr[i]));
						if (i == 0)
							seatingPrefForDTO = seatingPrefDesc;
						else
							seatingPrefForDTO = seatingPrefForDTO + "," + seatingPrefDesc;
					}
				} catch (NumberFormatException nfe) {
					LoggerUtil.logError(nfe.getMessage(), nfe);
				}
			}
			guestObj.setSeatingPreference(seatingPrefForDTO);

			// change by sunny for get MarktingPref and customPref line 394 to 416 (2018-07-05)


			String marketingPrefForDTO = "";
			if (null != guest.getMarketingPreference() && !"null".equals(guest.getMarketingPreference())
					&& !"".equals(guest.getMarketingPreference())) {
				try {
					String marketingPrefIdArr[] = guest.getMarketingPreference().split(",");
					for (int i = 0; i < marketingPrefIdArr.length; ++i) {
						String marketingPrefDesc = guestMarketingPreferenceMap.get(Integer.parseInt(marketingPrefIdArr[i]));
						if (i == 0)
							marketingPrefForDTO = marketingPrefDesc;
						else
							marketingPrefForDTO = marketingPrefForDTO + "," + marketingPrefDesc;
					}
				} catch (NumberFormatException nfe) {
					LoggerUtil.logError(nfe.getMessage(), nfe);
				}
			}
			guestObj.setMarketingPreference(marketingPrefForDTO);

			if (null != guest && null != guest.getGuestID()) {
				guestObj.setUpdatedTime(new Date());
				guestObj.setUuid(guest.getUuid());
				guestObj.setGuestID(guest.getGuestID());
				guestObj.setRank(guest.getRank());
				guestObj.setCalloutCount(guest.getCalloutCount());
				guestObj.setIncompleteParty(guest.getIncompleteParty());
				guestObj.setCreatedTime(new Date());
				guestObj.setCheckinTime(guest.getCheckinTime());

			} else {
				guestObj.setUuid(UUID.randomUUID().toString().substring(0, 8));
				guestObj.setCreatedTime(new Date());
				guestObj.setCheckinTime(guest.getCheckinTime());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return guestObj;
	}


	public GuestDTO convertGuesEntityToDTO(Guest guest, Map<Integer, String> guestPreferenceMap, Map<Integer, String> guestMarketingPreferenceMap) {
		GuestDTO guestObj = null;
		try {
			guestObj = new GuestDTO();
			guestObj.setName(guest.getName());
			guestObj.setNote(guest.getNote());
			guestObj.setOrganizationID(guest.getOrganizationID());
			guestObj.setEmail(guest.getEmail());
			guestObj.setSms(guest.getSms());
			guestObj.setOrganizationID(guest.getOrganizationID());
			if (guest.getNoOfChildren() != null) {
				guestObj.setNoOfChildren(guest.getNoOfChildren());
			}
			if (guest.getNoOfAdults() != null) {
				guestObj.setNoOfAdults(guest.getNoOfAdults());
			}
			if (guest.getNoOfInfants() != null) {
				guestObj.setNoOfInfants(guest.getNoOfInfants());
			}
			guestObj.setNoOfPeople(guest.getNoOfPeople());
			if (guest.getQuoteTime() != null) {
				guestObj.setQuoteTime(guest.getQuoteTime());
			}
			if (guest.getPartyType() != null) {
				guestObj.setPartyType(guest.getPartyType());
			}
			if (guest.getLanguagePrefID() != null) {
				guestObj.setLanguagePref(guest.getLanguagePrefID());
			}
			guestObj.setOptin(guest.isOptin());
			guestObj.setStatus(guest.getStatus());
			guestObj.setPrefType(guest.getPrefType());
			guestObj.setIncompleteParty(guest.getIncompleteParty());
			guestObj.setCheckinTime(guest.getCheckinTime());
			/*
			 * if(null != guest.getGuestPreferences()) { GuestPreferencesDTO
			 * guestPref = null; List<GuestPreferencesDTO> guestPreferences =
			 * new
			 * ArrayList<GuestPreferencesDTO>(guest.getGuestPreferences().size()
			 * ); for (GuestPreferences pref : guest.getGuestPreferences()) {
			 * guestPref = new GuestPreferencesDTO();
			 * guestPref.setGuestPrefId(pref.getGuestPrefId());
			 * guestPref.setPrefValueId(pref.getPrefValueId());
			 * System.out.println("pref.getPrefValue()"+pref.getPrefValue());
			 * guestPref.setPrefValue(pref.getPrefValue());
			 * //guestPref.setGuest(guestObj); guestPreferences.add(guestPref);
			 * } guestObj.setGuestPreferences(guestPreferences); }
			 */
			String seatingPrefForDTO = "";
			if (null != guest.getSeatingPreference() && !"".equals(guest.getSeatingPreference())) {
				try {
					String seatingPrefIdArr[] = guest.getSeatingPreference().split(",");
					for (int i = 0; i < seatingPrefIdArr.length; ++i) {
						String seatingPrefDesc = guestPreferenceMap.get(Integer.parseInt(seatingPrefIdArr[i]));
						if (i == 0) {
							guestObj.setGuestPreferences(new ArrayList<GuestPreferencesDTO>());
							GuestPreferencesDTO gPrefDTO = new GuestPreferencesDTO();
							gPrefDTO.setPrefValueId(Long.parseLong(seatingPrefIdArr[i]));
							gPrefDTO.setPrefValue(seatingPrefDesc);
							guestObj.getGuestPreferences().add(gPrefDTO);
						} else {
							GuestPreferencesDTO gPrefDTO = new GuestPreferencesDTO();
							gPrefDTO.setPrefValueId(Long.parseLong(seatingPrefIdArr[i]));
							gPrefDTO.setPrefValue(seatingPrefDesc);
							guestObj.getGuestPreferences().add(gPrefDTO);
						}
					}
				} catch (NumberFormatException nfe) {

				}
			}
			guestObj.setSeatingPreference(seatingPrefForDTO);

			// change by sunny for geting Marketing Preference line 511 to 541 (2018-07-05)

			String marketingPrefDTO = "";
			if (null != guest.getMarketingPreference() && !"".equals(guest.getMarketingPreference())) {
				try {
					String marketingPrefIdArr[] = guest.getMarketingPreference().split(",");
					for (int i = 0; i < marketingPrefIdArr.length; ++i) {
						String marketingPrefDesc = guestMarketingPreferenceMap.get(Integer.parseInt(marketingPrefIdArr[i]));
						if (i == 0) {
							guestObj.setGuestMarketingPreferences(new ArrayList<GuestMarketingPreference>());
							GuestMarketingPreference guestMarketingPref = new GuestMarketingPreference();
							guestMarketingPref.setGuestMarketPrefValueId(Long.parseLong(marketingPrefIdArr[i]));
							guestMarketingPref.setGuestMarketPrefValue(marketingPrefDesc);
							guestObj.getGuestMarketingPreferences().add(guestMarketingPref);
						} else {
							GuestMarketingPreference guestMarketingPref = new GuestMarketingPreference();
							guestMarketingPref.setGuestMarketPrefValueId(Long.parseLong(marketingPrefIdArr[i]));
							guestMarketingPref.setGuestMarketPrefValue(marketingPrefDesc);
							guestObj.getGuestMarketingPreferences().add(guestMarketingPref);
						}
					}
				} catch (NumberFormatException nfe) {

				}
			}
			guestObj.setMarketingPreference(marketingPrefDTO);

			if (null != guest && null != guest.getGuestID()) {
				guestObj.setUpdatedTime(new Date());
				guestObj.setUuid(guest.getUuid());
				guestObj.setGuestID(guest.getGuestID());
				guestObj.setRank(guest.getRank());
				guestObj.setCalloutCount(guest.getCalloutCount());
				guestObj.setIncompleteParty(guest.getIncompleteParty());

			} else {
				guestObj.setUuid(UUID.randomUUID().toString().substring(0, 8));
				guestObj.setCreatedTime(new Date());
				guestObj.setCheckinTime(new Date());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return guestObj;
	}


	/**
	 * Notify top N guests by organization Id
	 * @param numberOfUsers
	 * @param orgid
	 * @return String
	 */
	//@GET
	//@Path("/notify")
	@RequestMapping(value = "/notify", method = RequestMethod.GET, produces = "application/json")
	public String notifyGuests(@RequestParam("numberofusers")  int numberOfUsers,@RequestParam("orgid") Long orgid){
		waitListService.notifyUsers(numberOfUsers,orgid);
		return Constants.RSNT_GUEST_SUCCESS;
	}
	/** 
	 * Change per party wait Time by organizationId
	 * @param time
	 * @param orgid
	 * @return String
	 */
	//@GET
	//@Path("/perpartytime")
	@RequestMapping(value = "/perpartytime", method = RequestMethod.GET, produces = "application/json")
	public Response<Map<String, String>> changePerPartyTime(@RequestParam("time") int time,@RequestParam("orgid") Long orgid){
		int res= waitListService.updateOrganizationWaitTime(time,orgid);
		if(res==1){
			return getTotalWaitTime(orgid);
			//return Constants.RSNT_GUEST_SUCCESS;
		}
		//return Constants.RSNT_GUEST_FAIL;
		// TODO : send errormessage
		return null;
	}
	/**
	 * Get Total Wait Time by organizationiD
	 * @param orgid
	 * @return String-Total wait time metricks
	 */
	//@GET
	//@Path("/totalwaittimemetricks")
	@RequestMapping(value = "/totalwaittimemetricks", method = RequestMethod.GET, produces = "application/json")
	public Response<Map<String, String>> getTotalWaitTime(@RequestParam("orgid") Long orgid){
		Response<Map<String, String>> response = new Response<>();
		Map<String, String> metrciks = waitListService.getTotalPartyWaitTimeMetrics(orgid);
		metrciks.put("imageOrgPath", AppInitializer.orgLogoPathHtml+"/"+orgid+".png");
		response.setServiceResult(metrciks);
		CommonUtil.setWebserviceResponse(response, Constants.SUCCESS, null);
		return response;
		//final JSONObject jsonObject = JSONObject.fromObject(metrciks);
		//return jsonObject.toString();
	}
	/**
	 * Guest Guest Metrics like wait time , number of users a head
	 * @param guestId
	 * @param orgid
	 * @return String - Guest Metrics details
	 */
	//@GET
	//@Path("/usermetricks")
	@RequestMapping(value = "/usermetricks", method = RequestMethod.GET, produces = "application/json")
	public Response<Map<String, String>> getGuestWaitTimeMetricks(@RequestParam("guest") Long guestId,@RequestParam("orgid") Long orgid){
		Response<Map<String, String>> response = new Response<Map<String,String>>();

		try {
			Map<String, String> metrciks = waitListService.getGuesteMetrics(guestId,orgid);
			response.setServiceResult(metrciks);
			CommonUtil.setWebserviceResponse(response, Constants.SUCCESS, null);
		} catch (Exception e){
			CommonUtil.setWebserviceResponse(response, Constants.ERROR, null, null,
					"System Error - usermetriks failed");
		}
		return response;
	}
	/*	*//**
	 * Delete Guest By User
	 * @param guestId
	 * @return
	 *//*
	@GET
	@Path("/deleteguest")
	public String deleteGuest(@QueryParam("guestid") Long guestId){
		int res= waitListService.deleteGuestbyId(guestId);
		if(res==1){
			return Constants.RSNT_GUEST_SUCCESS;
		}
		return Constants.RSNT_GUEST_FAIL;
	}*/
	/**
	 * Reset Guests by Organization iD
	 * @param orgid
	 * @return String
	 */
	//@GET
	//@Path("/reset")
	@RequestMapping(value = "/reset", method = RequestMethod.GET, produces = "application/json")
	public Response<String> resetGuests(@RequestParam(value="orgid", required=true) Long orgid){
		Response<String> response = new Response<String>();
		final Map<String, Object> rootMap = new LinkedHashMap<String, Object>();
		try {
			int res= waitListService.resetOrganizationsByOrgid(orgid);
			if(res==1){
				CommonUtil.setWebserviceResponse(response, Constants.SUCCESS, null);
				rootMap.put("name", "resetOrganizationPusher");
				rootMap.put("orgid", orgid);
				sendPusherMessage(rootMap, AppInitializer.pusherChannelEnv+"_"+orgid);
			}
			else{
				CommonUtil.setWebserviceResponse(response, Constants.ERROR, null, null,
						"System Error - resetOrganization failed");
				//return Constants.RSNT_GUEST_FAIL;
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("fetchCheckinUsers() - failed:", e);
			CommonUtil.setWebserviceResponse(response, Constants.ERROR, null, null,
					"System Error - resetOrganization failed");
			//return Constants.RSNT_GUEST_FAIL;
		}
		return response;
	}
	/**
	 * Fetch Guests by Status of CHECKIN
	 * @param orgid
	 * @return List<GuestDTO> --List of guests
	 */
	//@GET
	//@Path("/checkinusers")
	//@Produces(MediaType.APPLICATION_JSON)
	//changes by krupali line 514,532 (16/06/2017)
	@RequestMapping(value = "/checkinusers", method = RequestMethod.GET, produces = "application/json")
	public Response<PaginatedResponse<GuestDTO>> fetchCheckinUsers(@RequestParam("orgid") Long orgid,
			@RequestParam("partyType") String partyType, @RequestParam String pagerReqParam) {
		log.info("Entering :: fetchCheckinUsers --OrgId::" + orgid);
		Response<PaginatedResponse<GuestDTO>> response = new Response<PaginatedResponse<GuestDTO>>();
		List<GuestDTO> guestDTOs = null;
		PaginatedResponse<GuestDTO> paginatedResponse = new PaginatedResponse<GuestDTO>();
		// final Map<String, Object> rootMap = new LinkedHashMap<String,
		// Object>();
		// final List<String> errorArray = new ArrayList<String>(0);
		Map<Integer, String> guestPreferenceMap;
		// change by sunny for marketing preference (2018-07-05)
		Map<Integer, String> guestMarketingPreferenceMap; 
		List<Guest> guests;

		try {
			ObjectMapper mapper = new ObjectMapper();
			PaginationReqParam paginationReqParam = mapper.readValue(pagerReqParam, PaginationReqParam.class);
			guestPreferenceMap = getGuestSeatingPrefMap();
			// change by sunny for get Marketing PreferenceMap (2018-07-05)
			guestMarketingPreferenceMap = getGuestMarketingPrefMap();

			guests = waitListService.loadAllCheckinUsers(orgid, paginationReqParam.getPageSize(),
					paginationReqParam.getPageNo(), partyType);
			Long guestsTotalCount = waitListService.getAllCheckinUsersCount(orgid);
			if (null != guests && guests.size() > 0) {
				guestDTOs = new ArrayList<GuestDTO>(guests.size());
				for (Guest guest : guests) {
					guestDTOs.add(convertGuesEntityToVo(guest, guestPreferenceMap, guestMarketingPreferenceMap));
				}

				paginatedResponse.setRecords(guestDTOs);
				paginatedResponse.setPageNo(paginationReqParam.getPageNo());
				paginatedResponse.setTotalRecords(guestsTotalCount.intValue());
				response.setServiceResult(paginatedResponse);
				CommonUtil.setWebserviceResponse(response, Constants.SUCCESS, null);

			}

		} catch (RsntException e) {
			e.printStackTrace();
			log.error("fetchCheckinUsers() - failed:", e);
			CommonUtil.setWebserviceResponse(response, Constants.ERROR, null, null,
					"System Error - fetchCheckinUsers failed");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("fetchCheckinUsers() - failed:", e);
			CommonUtil.setWebserviceResponse(response, Constants.ERROR, null, null,
					"System Error - fetchCheckinUsers failed");
		}
		// rootMap.put(Constants.RSNT_ERROR, "");
		// rootMap.put("guests",guestDTOs);
		// final JSONObject jsonObject = JSONObject.fromObject(rootMap);
		return response;
	}

	/**
	 * Search Guests by name
	 * @param orgid
	 * @return List<GuestDTO> --List of guests
	 */
	//@GET
	//@Path("/searchuser")
	//@Produces(MediaType.APPLICATION_JSON)
	//changes by krupali line 514,532 (16/06/2017)
	@RequestMapping(value = "/searchuser", method = RequestMethod.GET, produces = "application/json")
	public Response<PaginatedResponse<GuestDTO>> searchuserGrid(@RequestParam("orgid") Long orgid,
			@RequestParam("partyType") String partyType, @RequestParam String searchName,
			@RequestParam String pagerReqParam) {
		log.info("Entering :: searchuser --OrgId::" + orgid);
		Response<PaginatedResponse<GuestDTO>> response = new Response<PaginatedResponse<GuestDTO>>();
		List<GuestDTO> guestDTOs = null;
		PaginatedResponse<GuestDTO> paginatedResponse = new PaginatedResponse<GuestDTO>();
		// final Map<String, Object> rootMap = new LinkedHashMap<String,
		// Object>();
		// final List<String> errorArray = new ArrayList<String>(0);
		Map<Integer, String> guestPreferenceMap;
		// change by sunny for marketing preference (2018-07-05)
		Map<Integer, String> guestMarketingPreferenceMap; 	

		List<Guest> guests;

		try {
			ObjectMapper mapper = new ObjectMapper();
			PaginationReqParam paginationReqParam = mapper.readValue(pagerReqParam, PaginationReqParam.class);
			guestPreferenceMap = getGuestSeatingPrefMap();
			// change by sunny for get Marketing PreferenceMap (2018-07-05)
			guestMarketingPreferenceMap = getGuestMarketingPrefMap();
			guests = waitListService.searchAllCheckinUsersByName(orgid, paginationReqParam.getPageSize(),
					paginationReqParam.getPageNo(), partyType, searchName);
			Long guestsTotalCount = waitListService.getAllCheckinUsersCountByName(orgid, searchName);
			if (null != guests && guests.size() > 0) {
				guestDTOs = new ArrayList<GuestDTO>(guests.size());
				for (Guest guest : guests) {
					guestDTOs.add(convertGuesEntityToVo(guest, guestPreferenceMap, guestMarketingPreferenceMap));
				}

				paginatedResponse.setRecords(guestDTOs);
				paginatedResponse.setPageNo(paginationReqParam.getPageNo());
				paginatedResponse.setTotalRecords(guestsTotalCount.intValue());
				response.setServiceResult(paginatedResponse);
				CommonUtil.setWebserviceResponse(response, Constants.SUCCESS, null);

			}

		} catch (RsntException e) {
			e.printStackTrace();
			log.error("searchuserGrid() - failed:", e);
			CommonUtil.setWebserviceResponse(response, Constants.ERROR, null, null,
					"System Error - searchuserGrid failed");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("searchuserGrid() - failed:", e);
			CommonUtil.setWebserviceResponse(response, Constants.ERROR, null, null,
					"System Error - searchuserGrid failed");
		}
		// rootMap.put(Constants.RSNT_ERROR, "");
		// rootMap.put("guests",guestDTOs);
		// final JSONObject jsonObject = JSONObject.fromObject(rootMap);
		return response;
	}

	/**
	 * Fetch Guest Details By Id
	 * @param guestid
	 * @return {@link GuestDTO}
	 */
	//@GET
	//@Path("/guest")
	//@Produces(MediaType.APPLICATION_JSON)
	@RequestMapping(value = "/guest", method = RequestMethod.GET, produces = "application/json")
	public Response<GuestDTO> fetchGuestById(@RequestParam("guestid") int guestid) {
		log.info("Entering :: fetchGuestById :: guestId" + guestid);
		// final Map<String, Object> rootMap = new LinkedHashMap<String,
		// Object>();
		// final List<String> errorArray = new ArrayList<String>(0);
		Response<GuestDTO> response = new Response<GuestDTO>();
		Guest guestObject = null;
		try {
			Map<Integer, String> guestPreferenceMap = getGuestSeatingPrefMap();
			// change by sunny for marketing preference (2018-07-05)
			Map<Integer, String> guestMarketingPreferenceMap = getGuestMarketingPrefMap(); 
			guestObject = waitListService.getGuestById(guestid);
			GuestDTO guestDTO = convertGuesEntityToDTO(guestObject, guestPreferenceMap, guestMarketingPreferenceMap);
			response.setServiceResult(guestDTO);
			CommonUtil.setWebserviceResponse(response, Constants.SUCCESS, null);
			// nonCloseParent(guestObject.getGuestPreferences());
		} catch (Exception e) {
			log.error("fetchCheckinUsers() - failed:", e);
			CommonUtil.setWebserviceResponse(response, Constants.ERROR, null, null, "System Error - fetch Guest");
		}
		return response;
	}

	/**
	 * Get the Unique Guest Detail by UUID
	 * @param uuid
	 * @return GuestDTO
	 */
	//@GET
	//@Path("/guestuuid")
	//@Produces(MediaType.APPLICATION_JSON)
	@RequestMapping(value = "/guestuuid", method = RequestMethod.GET, produces = "application/json")
	public Response<Guest> fetchGuestByUUID(@RequestParam("uuid") String uuid){
		log.info("Entering fetchGuestByUUID ::UUID::"+uuid);
		Response<Guest> response = new Response<Guest>();
		Guest guestObject= null;
		try {
			guestObject = waitListService.getGuestByUUID(uuid);
			response.setServiceResult(guestObject);
			CommonUtil.setWebserviceResponse(response, Constants.SUCCESS, null);
			//nonCloseParent(guestObject.getGuestPreferences());
		} catch (Exception e) {
			e.printStackTrace();
			log.error("fetchGuestByUUID() - failed:", e);
			//rootMap.put("id", -1);
			//rootMap.put(Constants.RSNT_ERROR, "System Error - fetchCheckinUsers failed");
			//rootMap.put("fieldErrors", errorArray);
			//final JSONObject jsonObject = JSONObject.fromObject(rootMap);
			//return jsonObject.toString();
			CommonUtil.setWebserviceResponse(response, Constants.ERROR, null, null,
					"System Error - fetch Guest");
		}
		return response;
	}
	//version2 for web language map
	@RequestMapping(value = "/guestuuid/V2", method = RequestMethod.GET, produces = "application/json")
	public Response<GuestWebDTO> fetchGuestByUUIDWeb(@RequestParam("uuid") String uuid){
		log.info("Entering fetchGuestByUUID ::UUID::"+uuid);
		Response<GuestWebDTO> response = new Response<GuestWebDTO>();
		GuestWebDTO guestObject= null;
		try {
			guestObject = waitListService.getGuestByUUIDWeb(uuid);
			response.setServiceResult(guestObject);
			CommonUtil.setWebserviceResponse(response, Constants.SUCCESS, null);
			//nonCloseParent(guestObject.getGuestPreferences());
		} catch (Exception e) {
			e.printStackTrace();
			log.error("fetchGuestByUUID() - failed:", e);
			//rootMap.put("id", -1);
			//rootMap.put(Constants.RSNT_ERROR, "System Error - fetchCheckinUsers failed");
			//rootMap.put("fieldErrors", errorArray);
			//final JSONObject jsonObject = JSONObject.fromObject(rootMap);
			//return jsonObject.toString();
			CommonUtil.setWebserviceResponse(response, Constants.ERROR, null, null,
					"System Error - fetch Guest");
		}
		return response;
	}
	/**
	 * Get the Guests History by organizationId
	 * @param orgid
	 * @return List<GuestDTO>
	 */
	//@GET
	//@Path("/history")
	//@Produces(MediaType.APPLICATION_JSON)
	@RequestMapping(value = "/history", method = RequestMethod.GET, produces = "application/json")
	// changed for history pagination
	// public String fetchGuestsHistory(@QueryParam("orgid") Long orgid){
	public Response<PaginatedResponse<GuestDTO>> fetchGuestsHistory(@RequestParam("orgid") Long orgid,
			@RequestParam("statusOption") String statusOption, @RequestParam("sliderMinTime") Integer sliderMinTime,
			@RequestParam("sliderMaxTime") Integer sliderMaxTime, @RequestParam("clientTimezone") String clientTimezone,
			@RequestParam String pagerReqParam) {
		log.info("Entering :: fetchGuestsHistory ::orgid " + orgid);
		Response<PaginatedResponse<GuestDTO>> response = new Response<PaginatedResponse<GuestDTO>>();
		List<GuestDTO> guestDTOs = null;
		PaginatedResponse<GuestDTO> paginatedResponse = new PaginatedResponse<GuestDTO>();

		// List<GuestDTO> guestDTOs = null;
		// final Map<String, Object> rootMap = new LinkedHashMap<String,
		// Object>();
		// final List<String> errorArray = new ArrayList<String>(0);
		List<Guest> guests;
		Map<Integer, String> guestPreferenceMap;
		// change by sunny for marketing preference (2018-07-05)
		Map<Integer, String> guestMarketingPreferenceMap; 
		try {
			// changed for history pagination
			// guests = waitListService.loadGuestsHistoryByOrg(orgid);

			/*
			 * Calendar c = new
			 * GregorianCalendar(TimeZone.getTimeZone("GMT-05:00"));
			 * c.setTimeInMillis(new Date().getTime()); int EastCoastHourOfDay =
			 * c.get(Calendar.HOUR_OF_DAY); int EastCoastDayOfMonth =
			 * c.get(Calendar.DAY_OF_MONTH); int EastCoastMinuitOfDay =
			 * c.get(Calendar.MINUTE); int offset1 =
			 * TimeZone.getTimeZone("GMT-05:00").getOffset(new
			 * Date().getTime());
			 */

			/*
			 * // Local Time int localHourOfDay =
			 * Calendar.getInstance().get(Calendar.HOUR_OF_DAY); int
			 * localDayOfMonth =
			 * Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
			 */

			// Alaska
			/*
			 * c = new GregorianCalendar(TimeZone.getTimeZone("GMT+05:30"));
			 * c.setTimeInMillis(new Date().getTime()); int AlaskaHourOfDay =
			 * c.get(Calendar.HOUR_OF_DAY); int AlaskaDayOfMonth =
			 * c.get(Calendar.DAY_OF_MONTH); int AlaskaMinuitOfDay =
			 * c.get(Calendar.MINUTE); int offset2 =
			 * TimeZone.getTimeZone("GMT+05:30").getOffset(new
			 * Date().getTime());
			 * 
			 * int hourDifference = AlaskaHourOfDay - EastCoastHourOfDay; int
			 * dayDifference = AlaskaDayOfMonth - EastCoastDayOfMonth; int
			 * minuitDifference = AlaskaMinuitOfDay - EastCoastMinuitOfDay; int
			 * offsetDifference = (offset1-offset2)/1000/60/60;
			 * 
			 * if(minuitDifference == 60){ hourDifference = hourDifference+1; }
			 * if(minuitDifference < 0){ hourDifference = hourDifference-1;
			 * minuitDifference = 60+minuitDifference; } if (dayDifference != 0)
			 * { hourDifference = hourDifference + 24; }
			 * System.out.println(hourDifference+":"+minuitDifference);
			 * if(offset1 >= offset2){ sliderMinTime =
			 * sliderMinTime+hourDifference; sliderMaxTime =
			 * sliderMaxTime+hourDifference; } else{ sliderMinTime =
			 * sliderMinTime-hourDifference; sliderMaxTime =
			 * sliderMaxTime-hourDifference; }
			 */
			ObjectMapper mapper = new ObjectMapper();
			PaginationReqParam paginationReqParam = mapper.readValue(pagerReqParam, PaginationReqParam.class);
			guestPreferenceMap = getGuestSeatingPrefMap();
			// change by sunny for get Marketing PreferenceMap (2018-07-05)
			guestMarketingPreferenceMap = getGuestMarketingPrefMap();
			guests = waitListService.loadGuestsHistoryByOrgRecords(orgid, paginationReqParam.getPageSize(),
					paginationReqParam.getPageNo(), statusOption, sliderMinTime, sliderMaxTime, clientTimezone);

			Long guestsTotalCount = waitListService.getHistoryUsersCountForOrg(orgid, statusOption, sliderMinTime,
					sliderMaxTime, clientTimezone);

			if (null != guests && guests.size() > 0) {
				guestDTOs = new ArrayList<GuestDTO>(guests.size());
				for (Guest guest : guests) {
					guestDTOs.add(convertGuesEntityToVo(guest, guestPreferenceMap, guestMarketingPreferenceMap));
				}
				paginatedResponse.setRecords(guestDTOs);
				paginatedResponse.setPageNo(paginationReqParam.getPageNo());
				paginatedResponse.setTotalRecords(guestsTotalCount.intValue());
				response.setServiceResult(paginatedResponse);
				CommonUtil.setWebserviceResponse(response, Constants.SUCCESS, null);

			}
			/*
			 * changes by vruddhi(when there is no data as defined in condition)
			 */
			else {
				guestDTOs = new ArrayList<GuestDTO>(guests.size());
				paginatedResponse.setRecords(guestDTOs);
				paginatedResponse.setPageNo(paginationReqParam.getPageNo());
				paginatedResponse.setTotalRecords(guestsTotalCount.intValue());
				response.setServiceResult(paginatedResponse);
				CommonUtil.setWebserviceResponse(response, Constants.SUCCESS, null);
			}

		} catch (RsntException e) {
			e.printStackTrace();
			log.error("fetchGuestsHistory() - failed:", e);
			// rootMap.put("id", -1);
			// rootMap.put(Constants.RSNT_ERROR, "System Error -
			// fetchCheckinUsers failed");
			// rootMap.put("fieldErrors", errorArray);
			// final JSONObject jsonObject = JSONObject.fromObject(rootMap);
			// return jsonObject.toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// rootMap.put(Constants.RSNT_ERROR, "");
		// rootMap.put("guests",guestDTOs);
		// final JSONObject jsonObject = JSONObject.fromObject(rootMap);
		// return jsonObject.toString();
		return response;
	}

	/**
	 * Get the Guests History by Name
	 * @param searchName
	 * @param orgid
	 * @return List<GuestDTO>
	 */
	//@GET
	//@Path("/searchhistoryuser")
	//@Produces(MediaType.APPLICATION_JSON)
	@RequestMapping(value = "/searchhistoryuser", method = RequestMethod.GET, produces = "application/json")
	//changed for history pagination
	//public String searchhistoryuser(@QueryParam("orgid") Long orgid){
	public Response<PaginatedResponse<GuestDTO>> searchhistoryuserGrid(@RequestParam("orgid") Long orgid,
			@RequestParam("statusOption") String statusOption, @RequestParam("sliderMinTime") Integer sliderMinTime,
			@RequestParam("sliderMaxTime") Integer sliderMaxTime, @RequestParam("clientTimezone") String clientTimezone,
			@RequestParam("searchName") String searchName, @RequestParam String pagerReqParam) {
		log.info("Entering :: searchhistoryuser ::orgid " + orgid);
		Response<PaginatedResponse<GuestDTO>> response = new Response<PaginatedResponse<GuestDTO>>();
		List<GuestDTO> guestDTOs = null;
		PaginatedResponse<GuestDTO> paginatedResponse = new PaginatedResponse<GuestDTO>();

		// List<GuestDTO> guestDTOs = null;
		// final Map<String, Object> rootMap = new LinkedHashMap<String,
		// Object>();
		// final List<String> errorArray = new ArrayList<String>(0);
		List<Guest> guests;
		Map<Integer, String> guestPreferenceMap;
		// change by sunny for marketing preference (2018-07-05)
		Map<Integer, String> guestMarketingPreferenceMap; 
		try {
			// changed for history pagination
			// guests = waitListService.loadGuestsHistoryByOrg(orgid);
			ObjectMapper mapper = new ObjectMapper();
			PaginationReqParam paginationReqParam = mapper.readValue(pagerReqParam, PaginationReqParam.class);
			guestPreferenceMap = getGuestSeatingPrefMap();
			// change by sunny for get Marketing PreferenceMap (2018-07-05)
			guestMarketingPreferenceMap = getGuestMarketingPrefMap();
			guests = waitListService.loadGuestsHistoryByName(orgid, paginationReqParam.getPageSize(),
					paginationReqParam.getPageNo(), statusOption, sliderMinTime, sliderMaxTime, searchName,
					clientTimezone);

			Long guestsTotalCount = waitListService.getHistoryUsersCountForName(orgid, statusOption, sliderMinTime,
					sliderMaxTime, searchName, clientTimezone);

			if (null != guests && guests.size() > 0) {
				guestDTOs = new ArrayList<GuestDTO>(guests.size());
				for (Guest guest : guests) {
					guestDTOs.add(convertGuesEntityToVo(guest, guestPreferenceMap, guestMarketingPreferenceMap));
				}
				paginatedResponse.setRecords(guestDTOs);
				paginatedResponse.setPageNo(paginationReqParam.getPageNo());
				paginatedResponse.setTotalRecords(guestsTotalCount.intValue());
				response.setServiceResult(paginatedResponse);
				CommonUtil.setWebserviceResponse(response, Constants.SUCCESS, null);

			}
			/*
			 * changes by vruddhi(when there is no data as defined in condition)
			 */
			else {
				guestDTOs = new ArrayList<GuestDTO>(guests.size());
				paginatedResponse.setRecords(guestDTOs);
				paginatedResponse.setPageNo(paginationReqParam.getPageNo());
				paginatedResponse.setTotalRecords(guestsTotalCount.intValue());
				response.setServiceResult(paginatedResponse);
				CommonUtil.setWebserviceResponse(response, Constants.SUCCESS, null);
			}

		} catch (RsntException e) {
			e.printStackTrace();
			log.error("searchhistoryuser() - failed:", e);
			// rootMap.put("id", -1);
			// rootMap.put(Constants.RSNT_ERROR, "System Error -
			// fetchCheckinUsers failed");
			// rootMap.put("fieldErrors", errorArray);
			// final JSONObject jsonObject = JSONObject.fromObject(rootMap);
			// return jsonObject.toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// rootMap.put(Constants.RSNT_ERROR, "");
		// rootMap.put("guests",guestDTOs);
		// final JSONObject jsonObject = JSONObject.fromObject(rootMap);
		// return jsonObject.toString();
		return response;
	}


	/**
	 * Get Event Listener Configuration details 
	 * @return Map<String, String> 
	 */
	//@GET
	//@Path("/pusgerinformation")
	@RequestMapping(value = "/pusgerinformation", method = RequestMethod.GET, produces = "application/json")
	public Response<Map<String, String>> getPushEventsConfigDetails(){
		Response<Map<String, String>> response = new Response<Map<String,String>>();
		Map<String, String> eventConfig = new HashMap<String, String>();
		eventConfig.put(Constants.REALTIME_APPLICATION_KEY, System.getProperty(Constants.REALTIME_APPLICATION_KEY_VAL));
		eventConfig.put(Constants.REALTIME_PRIVATE_KEY, System.getProperty(Constants.REALTIME_PRIVATE_KEY_VAL));
		eventConfig.put(Constants.PUSHER_CHANNEL_ENV, AppInitializer.pusherChannelEnv+"_"+sessionContextUtil.get(Constants.CONST_ORGID));
		eventConfig.put(Constants.QRCODE_VALUE, "123_"+AppInitializer.pusherChannelEnv+"_"+sessionContextUtil.get(Constants.CONST_ORGID));
		eventConfig.put(Constants.FOOTER_MSG, AppInitializer.staticFooterMsg);
		//final JSONObject jsonObject = JSONObject.fromObject(eventConfig);
		//return jsonObject.toString();
		response.setServiceResult(eventConfig);
		CommonUtil.setWebserviceResponse(response, Constants.SUCCESS, null);
		return response;
	}

	/**
	 * Get Event Listener Configuration details 
	 * @return Map<String, String> 
	 */
	//@GET
	//@Path("/orgseatpref")
	//@Produces(MediaType.APPLICATION_JSON)
	@RequestMapping(value = "/orgseatpref", method = RequestMethod.GET, produces = "application/json")
	public Response<List<GuestPreferencesDTO>> getOrganizationSeatingPreferences(@RequestParam("orgid") Long orgid){
		//final Map<String, Object> rootMap = new LinkedHashMap<String, Object>();
		//final List<String> errorArray = new ArrayList<String>(0);
		Response<List<GuestPreferencesDTO>> response = new Response<List<GuestPreferencesDTO>>();
		List<GuestPreferencesDTO> searPref =  null;
		try {
			searPref=waitListService.getOrganizationSeatingPref(orgid);
			response.setServiceResult(searPref);
			CommonUtil.setWebserviceResponse(response, Constants.SUCCESS, null);
			//rootMap.put(Constants.RSNT_ERROR, "");
			//rootMap.put("seatpref",searPref);
			//final JSONObject jsonObject = JSONObject.fromObject(rootMap);
			//return jsonObject.toString();
		} catch (Exception e) {
			log.error("getOrganizationSeatingPreferences() - failed:", e);
			CommonUtil.setWebserviceResponse(response, Constants.ERROR, null, null,
					"System Error - getOrganizationSeatingPreferences failed");
			//rootMap.put("id", -1);
			//rootMap.put(Constants.RSNT_ERROR, "");
			//rootMap.put("fieldErrors", errorArray);
			//final JSONObject jsonObject = JSONObject.fromObject(rootMap);
			//return jsonObject.toString();
		}
		return response;

	}

	/* Get Organization Marketing Preference
	 * Create by sunny (2018-07-05)
	 * */ 
	@RequestMapping(value = "/orgMarketingPref", method = RequestMethod.GET, produces = "application/json")
	public Response<List<GuestMarketingPreference>> getOrganizationMarketingPreferences(@RequestParam("orgid") Long orgid) {
		log.info("Entring :: OPrganization Marketing Preference");
		Response<List<GuestMarketingPreference>> response = new Response<List<GuestMarketingPreference>>();
		List<GuestMarketingPreference> marketingPref = null;
		try {
			marketingPref = waitListService.getOrganizationMarketingPref(orgid);
			response.setServiceResult(marketingPref);
			CommonUtil.setWebserviceResponse(response, Constants.SUCCESS, null);

		} catch (Exception e) {
			log.error("getOrganizationMarketingPreferences() - failed:", e);
			CommonUtil.setWebserviceResponse(response, Constants.ERROR, null, null,
					"System Error - getOrganizationMarketingPreferences failed");

		}


		return response;

	}

	private boolean checkMarkerForStatusChange(Guest guest){
		boolean sendNotification = false;
		if(guest.getCalloutCount() == null && guest.getIncompleteParty() == null)
			sendNotification = true;
		log.info("checkMarkerForStatusChange---------------"+sendNotification);
		return sendNotification;
	}

	/**
	 Save newly added Guest information
	 @param guestJSONStr : Guest object in JSON format
	 @return String - Created Guest Object
	 */
	//@POST
	//@Path("/addGuest")
	//@Produces(MediaType.APPLICATION_JSON)
	//@Consumes(MediaType.APPLICATION_JSON)
	@RequestMapping(value = "/addGuest", method = RequestMethod.POST, produces = "application/json")
	public Response<Map<String, Object>> addGuest (@RequestBody GuestDTO guestDTO) {
		log.info("Entering into addGuest");
		Response<Map<String, Object>> response = new Response<Map<String, Object>>();
		Guest guest = null;
		final Map<String, Object> rootMap = new LinkedHashMap<String, Object>();
		WaitlistMetrics oWaitlistMetrics = null;
		try {

			guest = convertGuesVoToEntity(guestDTO);

			oWaitlistMetrics = waitListService.addGuest(guest);
			String tinyUrl = messageReceiver.buildURL(oWaitlistMetrics.getClientBase(), guest.getUuid());

			rootMap.put(Constants.RSNT_NOW_SERVING_GUEST_ID, oWaitlistMetrics.getNowServingParty());
			rootMap.put(Constants.RSNT_ORG_TOTAL_WAIT_TIME, oWaitlistMetrics.getTotalWaitTime());
			rootMap.put(Constants.RSNT_NEXT_TO_NOTIFY_GUEST_ID, oWaitlistMetrics.getGuestToBeNotified());

			guest.setRank(Long.parseLong(oWaitlistMetrics.getGuestRank()+""));

			rootMap.put("OP", "ADD");
			rootMap.put("totalWaitTime", oWaitlistMetrics.getTotalWaitTime());
			rootMap.put("nowServingParty", oWaitlistMetrics.getNowServingParty());
			//added to accomodate native app
			rootMap.put("totalPartiesWaiting", oWaitlistMetrics.getTotalWaitingGuest());
			//
			rootMap.put("orgid", guest.getOrganizationID());
			rootMap.put("addedGuestId", oWaitlistMetrics.getGuestId());
			rootMap.put("guestUUID", guest.getUuid());
			rootMap.put("tinyURL", tinyUrl);
			rootMap.put("guestRank", oWaitlistMetrics.getGuestRank());	
			rootMap.put("languagePrefID", guest.getLanguagePrefID());
			rootMap.put("partyType", guest.getPartyType());

			//commented for stopping the send sms upon adding the guest(krupali 09/11/2017)
			/*if(guestDTO.getPrefType() != null)
				sendNotification(guest, oWaitlistMetrics, "NORMAL", null);*/

			sendPusherMessage(rootMap, AppInitializer.pusherChannelEnv+"_"+rootMap.get("orgid"));
			response.setServiceResult(rootMap);
			CommonUtil.setWebserviceResponse(response, Constants.SUCCESS, null);

		} catch (Exception e) {
			log.error(e);
			CommonUtil.setWebserviceResponse(response, Constants.ERROR, null, null,
					"System Error - add Guest failed");
		}


		return response;
	}

	private String getGuestNoPrefix(long orgId){
		String prefix = "";
		if(orgId == 15){
			prefix = "A";
		}
		return prefix;
	}

	/*change by sunny for adding how did you hear about us at (1-08-2018)*/

	@RequestMapping(value="/addMarketingPref", method = RequestMethod.POST, produces = "application/json")
	public String addMarketingPref(@RequestBody MarketingPreferenceDTO addMarketingPrefDTO) {
		log.info("Entering into addGuestMarketingPref");
		final Map<String, Object> rootMap = new LinkedHashMap<String, Object>();

		try{
			GuestDTO guestDTO = new GuestDTO();
			Response<GuestDTO> responseDTO = new Response<GuestDTO>();
			MarketingPreference addMarketing = null;
			//addMarketing = convertAddMarketingDtoTOEntity(addMarketingPrefDTO);
			//response =  waitListService.addMarketingPref(addMarketing);
			responseDTO = fetchGuestById(addMarketingPrefDTO.getGuestID());
			guestDTO = responseDTO.getServiceResult();
			if(0 != addMarketingPrefDTO.getGuestMarketingPreference().size() ){
				guestDTO.setGuestMarketingPreferences(addMarketingPrefDTO.getGuestMarketingPreference());
			}else{
				guestDTO.setGuestMarketingPreferences(null);
			}
			updateGuestInfo(guestDTO);

			rootMap.put("status", Constants.SUCCESS);
			rootMap.put("message","Add Marketing Preference Successfully.");
			//CommonUtil.setWebserviceResponse(response, Constants.SUCCESS, "Add Marketing Prefernce Successfully.");
		}catch (Exception e) {
			log.error("AddOrganizationMarketingPreferences() - failed:", e);
			rootMap.put("status", "0");
			rootMap.put("message","Add Marketing Preference Failed.");
		}

		final JSONObject jsonObject = JSONObject.fromObject(rootMap);
		return jsonObject.toString();
	}


	/**
	 Update Guest information
	 @param guestJSONStr : Guest object in JSON format
	 @return String - Updated Guest Object
	 */
	//@POST
	//@Path("/updateGuestInfo")
	//@Produces(MediaType.APPLICATION_JSON)
	//@Consumes(MediaType.APPLICATION_JSON)
	@RequestMapping(value = "/updateGuestInfo", method = RequestMethod.POST, produces = "application/json")
	public Response<Map<String, Object>> updateGuestInfo (@RequestBody GuestDTO guestDTO) {
		log.info("Entering into updateGuestInfo");
		//ObjectMapper objectMapper = new ObjectMapper();
		//GuestDTO guestDTO = null;
		Response<Map<String, Object>> response = new Response<Map<String, Object>>();
		Guest guest = null;
		//JSONObject jsonObject = null;
		final Map<String, Object> rootMap = new LinkedHashMap<String, Object>();
		WaitlistMetrics oWaitlistMetrics = null;
		try {
			//guestDTO = objectMapper.readValue(guestJSONStr, GuestDTO.class);
			guest = convertGuesVoToEntity(guestDTO);
			oWaitlistMetrics = waitListService.updateGuestInfo(guest, Constants.WAITLIST_UPDATE_GUEST);

			rootMap.put(Constants.RSNT_NOW_SERVING_GUEST_ID, oWaitlistMetrics.getNowServingParty());
			rootMap.put(Constants.RSNT_ORG_TOTAL_WAIT_TIME, oWaitlistMetrics.getTotalWaitTime());
			rootMap.put(Constants.RSNT_NEXT_TO_NOTIFY_GUEST_ID, oWaitlistMetrics.getGuestToBeNotified());

			guest.setRank(Long.parseLong(oWaitlistMetrics.getGuestId()+""));
			response.setServiceResult(rootMap);
			CommonUtil.setWebserviceResponse(response, Constants.SUCCESS, null);
			//jsonObject = JSONObject.fromObject(rootMap);

		}catch (Exception e) {
			e.printStackTrace();
			CommonUtil.setWebserviceResponse(response, Constants.ERROR, null, null,
					"System Error - add Guest failed");
		}
		//Issue 29: changed OP=UPD to OP=UpdateGuestInfo to block subscription on guest check in page. Because guest check in page subscribes on UPD status.
		rootMap.put("OP", "UpdageGuestInfo");
		rootMap.put("guestObj", guest.getGuestID());
		rootMap.put("updguest", guest);
		rootMap.put("FROM", "ADMIN");
		rootMap.put("nowServingParty", oWaitlistMetrics.getNowServingParty());
		//rootMap.put("ppwt": $jquery("#perPartyWaitTime").val(),
		rootMap.put("orgid", guest.getOrganizationID());
		rootMap.put("totalWaitTime", oWaitlistMetrics.getTotalWaitTime());
		rootMap.put("partyType", guest.getPartyType());
		//sendNotification(guest, guestCount, totalWaitTime);

		sendPusherMessage(rootMap, AppInitializer.pusherChannelEnv+"_"+rootMap.get("orgid"));

		return response;
	}


	/**
	 Update Guest information
	 @param guestJSONStr : Guest object in JSON format
	 @return String - Updated Guest Object
	 * @throws Exception 
	 */
	//@POST
	//@Path("/deleteGuest")
	@RequestMapping(value = "/deleteGuest", method = RequestMethod.POST, produces = "application/json")
	public Response<Map<String, Object>> deleteGuest (@RequestBody GuestDTO guestDTO, HttpServletRequest request) throws Exception {
		log.info("Entering into deleteGuest");
		Response<Map<String, Object>> response = new Response<Map<String,Object>>();
		Guest guest = new Guest();
		//guest.setGuestID(Long.parseLong(guestId+""));
		//guest.setOrganizationID(Long.parseLong(orgId+""));
		WaitlistMetrics oWaitlistMetrics = null;
		final Map<String, Object> rootMap = new LinkedHashMap<String, Object>();
		Long totalWaitTime = null;
		String smsContent=null;
		long templateId=0;
		int tempLevel=0;
		try {
			guest = convertGuesVoToEntity(guestDTO);
			String seatingPreference = buildSeatingPreference(guestDTO);
			guest.setSeatingPreference(seatingPreference);

			oWaitlistMetrics = waitListService.updateGuestInfo(guest, Constants.WAITLIST_UPDATE_DELETE);

			rootMap.put(Constants.RSNT_NOW_SERVING_GUEST_ID, oWaitlistMetrics.getNowServingParty());
			rootMap.put(Constants.RSNT_ORG_TOTAL_WAIT_TIME, oWaitlistMetrics.getTotalWaitTime());
			rootMap.put(Constants.RSNT_NEXT_TO_NOTIFY_GUEST_ID, oWaitlistMetrics.getGuestToBeNotified());

			guest.setRank(Long.parseLong(oWaitlistMetrics.getGuestId()+""));
			totalWaitTime = Long.parseLong(oWaitlistMetrics.getTotalWaitTime()+"");
			//jsonObject = JSONObject.fromObject(rootMap);
			CommonUtil.setWebserviceResponse(response, Constants.SUCCESS, null);
		} catch (Exception e) {
			e.printStackTrace();
			CommonUtil.setWebserviceResponse(response, Constants.ERROR, null, null,
					"System Error - add Guest failed");
		}

		rootMap.put("OP", "DEL");
		rootMap.put("guestObj", oWaitlistMetrics.getGuestToBeNotified());
		rootMap.put("nowServingParty", oWaitlistMetrics.getNowServingParty());
		rootMap.put("FROM", "ADMIN");
		//rootMap.put("ppwt": $jquery("#perPartyWaitTime").val(),
		rootMap.put("totalWaitTime",totalWaitTime);
		rootMap.put("orgid", guest.getOrganizationID());
		rootMap.put("numberofparties", oWaitlistMetrics.getTotalWaitingGuest());
		rootMap.put("partyType", guest.getPartyType());

		HttpSession sessionObj= request.getSession();
		UserDTO userDto=(UserDTO) sessionObj.getAttribute(Constants.USER_OBJ);
		if(userDto == null){
			userDto=new UserDTO();
			userDto.setSmsRoute(waitListService.getSmsRouteByOrgid(guest.getOrganizationID()));
		}
		if(userDto.getSmsRoute() != null && !userDto.getSmsRoute().equals("")){
			if(oWaitlistMetrics.getGuestToBeNotified() != -1){

				if(guest.getGuestID() <= oWaitlistMetrics.getGuestToBeNotified()) {
					Guest guestToBeNotified = waitListService.getGuestById(oWaitlistMetrics.getGuestToBeNotified());
					/*sendNotification(guestToBeNotified, oWaitlistMetrics, Constants.NOTIF_THRESHOLD_ENTERED, null);*/

					//for fetching smsTemplates
					List<OrganizationTemplateDTO> templates= waitListService.getOrganizationTemplates(guestToBeNotified.getOrganizationID(),guestToBeNotified.getLanguagePrefID().getLangId(),2);
					if(templates.size()>0) {
						smsContent = templates.get(0).getTemplateText();
						smsContent=smsContent.replaceAll("G_name", guestToBeNotified.getName());
						smsContent=smsContent.replaceAll("G_rank", guestToBeNotified.getRank().toString());
						smsContent=smsContent.replaceAll("Turl", messageReceiver.buildURL(oWaitlistMetrics.getClientBase(), guestToBeNotified.getUuid()));
						smsContent=smsContent.replaceAll("P_ahead", Integer.toString(oWaitlistMetrics.getTotalWaitingGuest()-1));
						templateId=templates.get(0).getSmsTemplateID();
						tempLevel=templates.get(0).getLevel();
					}

					SendSMSWrapper smsWrapper = new SendSMSWrapper();
					smsWrapper.setGuestId(guestToBeNotified.getGuestID());
					smsWrapper.setOrgId(guestToBeNotified.getOrganizationID());
					smsWrapper.setTemplateId(templateId);
					smsWrapper.setSmsContent(smsContent);
					smsWrapper.setMetrics(oWaitlistMetrics);
					smsWrapper.setTemplateLevel(tempLevel);

					Response<Map<String, String>> res = sendSMS(smsWrapper);
				}
			}
		}

		sendPusherMessage(rootMap, AppInitializer.pusherChannelEnv+"_"+rootMap.get("orgid"));
		response.setServiceResult(rootMap);
		return response;
	}

	/**
	 * Notify top N guests by organization Id
	 * @param numberOfUsers
	 * @param orgid
	 * @return String
	 */
	//@GET
	//@Path("/changeNotificationThreshold")
	@RequestMapping(value = "/changeNotificationThreshold", method = RequestMethod.GET, produces = "application/json")
	public Response<Map<String, Object>>  changeNotificationThreshold(@RequestParam("numberofusers")  int numberOfUsers,@RequestParam("perPartyWaitTime") int perPartyWaitTime, @RequestParam("orgid") Long orgid){
		Response<Map<String, Object>>  response = new Response<Map<String,Object>>();
		final Map<String, Object> rootMap = new LinkedHashMap<String, Object>();

		try{
			WaitlistMetrics oWaitlistMetrics = waitListService.changeNotificationThreshold(numberOfUsers,perPartyWaitTime, orgid);

			rootMap.put(Constants.RSNT_NOW_SERVING_GUEST_ID, oWaitlistMetrics.getNowServingParty());
			rootMap.put(Constants.RSNT_ORG_TOTAL_WAIT_TIME, oWaitlistMetrics.getTotalWaitTime());
			rootMap.put(Constants.RSNT_NEXT_TO_NOTIFY_GUEST_ID, oWaitlistMetrics.getGuestToBeNotified());

			rootMap.put("OP", "NOTIFY_USER");
			rootMap.put("FROM", "ADMIN");
			rootMap.put("notifyUser", numberOfUsers);
			rootMap.put("totalWaitTime", oWaitlistMetrics.getTotalWaitTime());
			rootMap.put("orgid", orgid);
			response.setServiceResult(rootMap);
			CommonUtil.setWebserviceResponse(response, Constants.SUCCESS, null);
			sendPusherMessage(rootMap, AppInitializer.pusherChannelEnv+"_"+rootMap.get("orgid"));
		} catch(RsntException e){
			CommonUtil.setWebserviceResponse(response, Constants.ERROR, null, null,
					"System Error - add Guest failed");
			LoggerUtil.logError("Error while updating  notification threshold", e);
		}

		return response;
	}

	/** 
	 * Change per party wait Time by organizationId
	 * @param time
	 * @param orgid
	 * @return String
	 */
	//@GET
	//@Path("/changePerPartyWaitTime")
	@RequestMapping(value = "/changePerPartyWaitTime", method = RequestMethod.GET, produces = "application/json")
	public Response<Map<String, Object>>  changePerPartyWaitTime(@RequestParam("numberofusers")  int numberOfUsers, @RequestParam("perPartyWaitTime") int perPartyWaitTime,@RequestParam("orgid") Long orgid){
		Response<Map<String, Object>> response = new Response<Map<String,Object>>();
		final Map<String, Object> rootMap = new LinkedHashMap<String, Object>();

		try {
			WaitlistMetrics oWaitlistMetrics = waitListService.changePerPartyWaitTime(numberOfUsers, perPartyWaitTime,orgid);

			rootMap.put(Constants.RSNT_NOW_SERVING_GUEST_ID, oWaitlistMetrics.getNowServingParty());
			rootMap.put(Constants.RSNT_ORG_TOTAL_WAIT_TIME, oWaitlistMetrics.getTotalWaitTime());
			rootMap.put(Constants.RSNT_NEXT_TO_NOTIFY_GUEST_ID, oWaitlistMetrics.getGuestToBeNotified());

			rootMap.put("OP", "PPT_CHG");
			rootMap.put("FROM", "ADMIN");
			rootMap.put("totalWaitTime", oWaitlistMetrics.getTotalWaitTime());
			//rootMap.put("notifyUser", numberOfUsers);
			rootMap.put("ppwt", perPartyWaitTime);
			rootMap.put("orgid", orgid);
			rootMap.put("nowServingParty", oWaitlistMetrics.getNowServingParty());
			response.setServiceResult(rootMap);
			CommonUtil.setWebserviceResponse(response, Constants.SUCCESS, null);
			sendPusherMessage(rootMap, AppInitializer.pusherChannelEnv+"_"+rootMap.get("orgid"));

		} catch(RsntException e){
			CommonUtil.setWebserviceResponse(response, Constants.ERROR, null, null,
					"System Error - add Guest failed");
			LoggerUtil.logError("Error while updating party wait time", e);
		}

		return response;
	}

	/**
	 Increments Callout Count
	 @param guestJSONStr : Guest object in JSON format
	 @return String - Updated Guest Object
	 */
	//@POST
	//@Path("/incrementCalloutCount")
	//@Produces(MediaType.APPLICATION_JSON)
	//@Consumes(MediaType.APPLICATION_JSON)
	@RequestMapping(value = "/incrementCalloutCount", method = RequestMethod.POST, produces = "application/json")
	public Response<Map<String, Object>> incrementCalloutCount (@RequestBody GuestDTO guestDTO, HttpServletRequest request) {
		log.info("Entering into incrementCalloutCount");
		//JSONObject jsonObject = null;
		//ObjectMapper objectMapper = new ObjectMapper();
		//GuestDTO guestDTO = null;
		Guest guest = null;
		Response<Map<String, Object>> response = new Response<Map<String,Object>>();
		final Map<String, Object> rootMap = new LinkedHashMap<String, Object>();
		WaitlistMetrics oWaitlistMetrics = null;
		Long guestCount = null;
		String smsContent=null;
		long templateId=0;
		int tempLevel=0;
		try {
			//guestDTO = objectMapper.readValue(guestJSONStr, GuestDTO.class);
			guest = convertGuesVoToEntity(guestDTO);
			String seatingPreference = buildSeatingPreference(guestDTO);
			guest.setSeatingPreference(seatingPreference);
			oWaitlistMetrics = waitListService.updateGuestInfo(guest, Constants.WAITLIST_UPDATE_CALLOUT);

			rootMap.put(Constants.RSNT_NOW_SERVING_GUEST_ID, oWaitlistMetrics.getNowServingParty());
			rootMap.put(Constants.RSNT_ORG_TOTAL_WAIT_TIME, oWaitlistMetrics.getTotalWaitTime());
			rootMap.put(Constants.RSNT_NEXT_TO_NOTIFY_GUEST_ID, oWaitlistMetrics.getGuestToBeNotified());

			guestCount = Long.parseLong(oWaitlistMetrics.getTotalWaitingGuest()+"");


			response.setServiceResult(rootMap);
			CommonUtil.setWebserviceResponse(response, Constants.SUCCESS, null);
			//jsonObject = JSONObject.fromObject(rootMap);

		}catch (Exception e) {
			response.setServiceResult(rootMap);
			CommonUtil.setWebserviceResponse(response, Constants.ERROR, null, null,
					"System Error - add Guest failed");
			e.printStackTrace();
		}

		rootMap.put("OP", "UPD");
		rootMap.put("guestObj", guest.getGuestID());
		rootMap.put("updguest", guest);
		rootMap.put("FROM", "ADMIN");
		rootMap.put("ORG_GUEST_COUNT", guestCount);
		rootMap.put("totalWaitTime", oWaitlistMetrics.getTotalWaitTime());
		rootMap.put("orgid", guest.getOrganizationID());
		rootMap.put("partyType", guest.getPartyType());

		HttpSession sessionObj= request.getSession();
		UserDTO userDto=(UserDTO) sessionObj.getAttribute(Constants.USER_OBJ);
		if(userDto.getSmsRoute() != null && !userDto.getSmsRoute().equals("")){
			if(oWaitlistMetrics.getGuestToBeNotified() != -1){
				if(guest.getGuestID() <= oWaitlistMetrics.getGuestToBeNotified()){
					//commented as we are using sendsms API for sending sms as of now
					Guest guestToNotify = waitListService.getGuestById((long)oWaitlistMetrics.getGuestToBeNotified());
					//sendNotification(guestToNotify, oWaitlistMetrics, Constants.NOTIF_THRESHOLD_ENTERED, null);

					//for fetching smsTemplates
					List<OrganizationTemplateDTO> templates= waitListService.getOrganizationTemplates(guestToNotify.getOrganizationID(),guestToNotify.getLanguagePrefID().getLangId(),2);
					if(templates.size()>0) {
						smsContent = templates.get(0).getTemplateText();
						smsContent=smsContent.replaceAll("G_name", guestToNotify.getName());
						smsContent=smsContent.replaceAll("G_rank", guestToNotify.getRank().toString());
						smsContent=smsContent.replaceAll("Turl", messageReceiver.buildURL(oWaitlistMetrics.getClientBase(), guestToNotify.getUuid()));
						smsContent=smsContent.replaceAll("P_ahead", Integer.toString(oWaitlistMetrics.getTotalWaitingGuest()-1));
						templateId=templates.get(0).getSmsTemplateID();
						tempLevel=templates.get(0).getLevel();
					}

					SendSMSWrapper smsWrapper = new SendSMSWrapper();
					smsWrapper.setGuestId(guestToNotify.getGuestID());
					smsWrapper.setOrgId(guestToNotify.getOrganizationID());
					smsWrapper.setTemplateId(templateId);
					smsWrapper.setSmsContent(smsContent);
					smsWrapper.setMetrics(oWaitlistMetrics);
					smsWrapper.setTemplateLevel(tempLevel);

					Response<Map<String, String>> res = sendSMS(smsWrapper);
				}
			}
		}
		sendPusherMessage(rootMap, AppInitializer.pusherChannelEnv+"_"+rootMap.get("orgid"));

		return response;
	}

	/**
	 Marks As Incomplete
	 @param guestJSONStr : Guest object in JSON format
	 @return String - Updated Guest Object
	 */
	//@POST
	//@Path("/markAsIncomplete")
	//@Produces(MediaType.APPLICATION_JSON)
	//@Consumes(MediaType.APPLICATION_JSON)
	@RequestMapping(value = "/markAsIncomplete", method = RequestMethod.POST, produces = "application/json")
	public Response<Map<String, Object>> markAsIncomplete (@RequestBody GuestDTO guestDTO, HttpServletRequest request) {
		log.info("Entering into markAsIncomplete");
		Response<Map<String, Object>> response = new Response<Map<String,Object>>();
		//ObjectMapper objectMapper = new ObjectMapper();
		//GuestDTO guestDTO = null;
		Guest guest = new Guest();
		//JSONObject jsonObject = null;
		WaitlistMetrics oWaitlistMetrics = null;
		final Map<String, Object> rootMap = new LinkedHashMap<String, Object>();
		Long guestCount = null;
		String smsContent=null;
		long templateId=0;
		int tempLevel=0;

		try {
			//guestDTO = objectMapper.readValue(guestJSONStr, GuestDTO.class);
			guest = convertGuesVoToEntity(guestDTO);
			String seatingPreference = buildSeatingPreference(guestDTO);
			guest.setSeatingPreference(seatingPreference);
			oWaitlistMetrics = waitListService.updateGuestInfo(guest, Constants.WAITLIST_UPDATE_INCOMPLETE);

			rootMap.put(Constants.RSNT_NOW_SERVING_GUEST_ID, oWaitlistMetrics.getNowServingParty());
			rootMap.put(Constants.RSNT_ORG_TOTAL_WAIT_TIME, oWaitlistMetrics.getTotalWaitTime());
			rootMap.put(Constants.RSNT_NEXT_TO_NOTIFY_GUEST_ID, oWaitlistMetrics.getGuestToBeNotified());

			guestCount = Long.parseLong(oWaitlistMetrics.getTotalWaitingGuest()+"");
			response.setServiceResult(rootMap);
			CommonUtil.setWebserviceResponse(response, Constants.SUCCESS, null);
			//jsonObject = JSONObject.fromObject(rootMap);

		} catch (Exception e) {
			response.setServiceResult(rootMap);
			CommonUtil.setWebserviceResponse(response, Constants.ERROR, null, null,
					"System Error - add Guest failed");
			e.printStackTrace();
		}

		rootMap.put("OP", "UPD");
		rootMap.put("guestObj", guest.getGuestID());
		rootMap.put("updguest", guest);
		rootMap.put("FROM", "ADMIN");
		rootMap.put("ORG_GUEST_COUNT", guestCount);

		rootMap.put("totalWaitTime", oWaitlistMetrics.getTotalWaitTime());

		rootMap.put("orgid", guest.getOrganizationID());
		rootMap.put("partyType", guest.getPartyType());

		HttpSession sessionObj= request.getSession();
		UserDTO userDto=(UserDTO) sessionObj.getAttribute(Constants.USER_OBJ);
		if(userDto.getSmsRoute() != null && !userDto.getSmsRoute().equals("")){
			if(oWaitlistMetrics.getGuestToBeNotified() != -1){
				if(guest.getGuestID() <= oWaitlistMetrics.getGuestToBeNotified()){
					Guest guestToNotify = waitListService.getGuestById((long)oWaitlistMetrics.getGuestToBeNotified());
					/*sendNotification(guestToNotify, oWaitlistMetrics, Constants.NOTIF_THRESHOLD_ENTERED, null);*/

					//for fetching smsTemplates
					List<OrganizationTemplateDTO> templates= waitListService.getOrganizationTemplates(guestToNotify.getOrganizationID(),guestToNotify.getLanguagePrefID().getLangId(),2);
					if(templates.size()>0) {
						smsContent = templates.get(0).getTemplateText();
						smsContent=smsContent.replaceAll("G_name", guestToNotify.getName());
						smsContent=smsContent.replaceAll("G_rank", guestToNotify.getRank().toString());
						smsContent=smsContent.replaceAll("Turl", messageReceiver.buildURL(oWaitlistMetrics.getClientBase(), guestToNotify.getUuid()));
						smsContent=smsContent.replaceAll("P_ahead", Integer.toString(oWaitlistMetrics.getTotalWaitingGuest()-1));
						templateId=templates.get(0).getSmsTemplateID();
						tempLevel=templates.get(0).getLevel();
					}

					SendSMSWrapper smsWrapper = new SendSMSWrapper();
					smsWrapper.setGuestId(guestToNotify.getGuestID());
					smsWrapper.setOrgId(guestToNotify.getOrganizationID());
					smsWrapper.setTemplateId(templateId);
					smsWrapper.setSmsContent(smsContent);
					smsWrapper.setMetrics(oWaitlistMetrics);
					smsWrapper.setTemplateLevel(tempLevel);

					Response<Map<String, String>> res = sendSMS(smsWrapper);
				}
			}
		}
		sendPusherMessage(rootMap, AppInitializer.pusherChannelEnv+"_"+rootMap.get("orgid"));

		return response;


		//return jsonObject.toString();
	}

	/**
	 Marks As Seated
	 @param guestJSONStr : Guest object in JSON format
	 @return String - Updated Guest Object
	 */
	//@GET
	//@Path("/markAsSeated")
	@RequestMapping(value = "/markAsSeated", method = RequestMethod.GET, produces = "application/json")
	public Response<Map<String, Object>> markAsSeated (@RequestParam("guestId")  int guestId, @RequestParam("orgId")  int orgId,HttpServletRequest request ) {
		log.info("Entering into markAsSeated");
		Response<Map<String, Object>> response = new Response<Map<String,Object>>();
		Guest guestToBeSeated = waitListService.getGuestById(guestId);
		//JSONObject jsonObject = null;
		WaitlistMetrics oWaitlistMetrics = null;
		String smsContent=null;
		long templateId=0;
		int tempLevel=0;
		final Map<String, Object> rootMap = new LinkedHashMap<String, Object>();
		Long guestCount = null;
		try {
			oWaitlistMetrics = waitListService.updateGuestInfo(guestToBeSeated, Constants.WAITLIST_UPDATE_MARK_AS_SEATED);

			rootMap.put(Constants.RSNT_NOW_SERVING_GUEST_ID, oWaitlistMetrics.getNowServingParty());
			rootMap.put(Constants.RSNT_ORG_TOTAL_WAIT_TIME, oWaitlistMetrics.getTotalWaitTime());
			rootMap.put(Constants.RSNT_NEXT_TO_NOTIFY_GUEST_ID, oWaitlistMetrics.getGuestToBeNotified());

			guestCount = Long.parseLong(oWaitlistMetrics.getTotalWaitingGuest()+"");
			//jsonObject = JSONObject.fromObject(rootMap);
			response.setServiceResult(rootMap);
			CommonUtil.setWebserviceResponse(response, Constants.SUCCESS, null);

		} catch (Exception e) {
			response.setServiceResult(rootMap);
			CommonUtil.setWebserviceResponse(response, Constants.ERROR, null, null,
					"System Error - add Guest failed");
			e.printStackTrace();
		}

		rootMap.put("OP", "MARK_AS_SEATED");
		rootMap.put("guestObj", guestId);
		rootMap.put("updguest", guestToBeSeated);
		rootMap.put("FROM", "ADMIN");
		rootMap.put("totalWaitTime", oWaitlistMetrics.getTotalWaitTime());
		rootMap.put("nowServingParty", oWaitlistMetrics.getNowServingParty());
		rootMap.put("ORG_GUEST_COUNT", guestCount);
		rootMap.put("orgid", orgId);
		rootMap.put("partyType", guestToBeSeated.getPartyType());
		//turn off 3rd level notifiction by shruti shah along with history and threshold changes
		//sendNotification(guestToBeSeated, oWaitlistMetrics, Constants.NOTIF_MARK_AS_SEATED);

		long guestIdToBeNotified = (long) oWaitlistMetrics.getGuestToBeNotified();

		HttpSession sessionObj= request.getSession();
		UserDTO userDto=(UserDTO) sessionObj.getAttribute(Constants.USER_OBJ);
		if(userDto.getSmsRoute() != null && !userDto.getSmsRoute().equals("")){
			if(oWaitlistMetrics.getGuestToBeNotified() != -1){
				if(guestToBeSeated.getGuestID() <= guestIdToBeNotified)
				{
					Guest guestToNotify = waitListService.getGuestById(guestIdToBeNotified);
					/*sendNotification(guestToNotify, oWaitlistMetrics, Constants.NOTIF_THRESHOLD_ENTERED,null);*/

					//for fetching smsTemplates
					List<OrganizationTemplateDTO> templates= waitListService.getOrganizationTemplates(guestToNotify.getOrganizationID(),guestToNotify.getLanguagePrefID().getLangId(),2);
					if(templates.size()>0) {
						smsContent = templates.get(0).getTemplateText();
						smsContent=smsContent.replaceAll("G_name", guestToNotify.getName());
						smsContent=smsContent.replaceAll("G_rank", guestToNotify.getRank().toString());
						smsContent=smsContent.replaceAll("Turl", messageReceiver.buildURL(oWaitlistMetrics.getClientBase(), guestToNotify.getUuid()));
						smsContent=smsContent.replaceAll("P_ahead", Integer.toString(oWaitlistMetrics.getTotalWaitingGuest()-1));
						templateId=templates.get(0).getSmsTemplateID();
						tempLevel=templates.get(0).getLevel();
					}

					SendSMSWrapper smsWrapper = new SendSMSWrapper();
					smsWrapper.setGuestId(guestToNotify.getGuestID());
					smsWrapper.setOrgId(guestToNotify.getOrganizationID());
					smsWrapper.setTemplateId(templateId);
					smsWrapper.setSmsContent(smsContent);
					smsWrapper.setMetrics(oWaitlistMetrics);
					smsWrapper.setTemplateLevel(tempLevel);

					Response<Map<String, String>> res = sendSMS(smsWrapper);
				}
			}
		}
		sendPusherMessage(rootMap, AppInitializer.pusherChannelEnv+"_"+rootMap.get("orgid"));

		return response;
	}

	/**
	 Scan QR code
	 @param qrCodeValue : Qr code value
	 @return String - Success or Failure
	 */
	//@GET
	//@Path("/scanqrcode")
	@RequestMapping(value = "/scanqrcode", method = RequestMethod.GET, produces = "application/json")
	public String scanQRCode (@RequestParam("qrcodevalue")  String qrCodeValue) {
		// TODO - uncomment and make below code working. - rohit
		/*log.info("Entering into scanQRCode");
		JSONObject jsonObjectMobile = null;
		JSONObject jsonObjectPublish = null;
		final Map<String, Object> rootMapPublish = new LinkedHashMap<String, Object>();
		final Map<String, Object> rootMapMobile = new LinkedHashMap<String, Object>();
		try {

			//Validate QRcode
			//Generate new QR code and store into the database too against Jsession 
			//Publish newly generated QR code to pusher to QRcode channel to subscribe
			jsonObjectPublish = JSONObject.fromObject(rootMapPublish);
			rootMapMobile.put("success", 1);
			rootMapMobile.put("msg", "Thank you for using waitlist service, Fill up your detail on the device");

			jsonObjectMobile = JSONObject.fromObject(rootMapMobile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		rootMapPublish.put(Constants.QRCODE_VALUE, qrCodeValue);


		jsonObjectPublish = sendPusherMessage(rootMapPublish, qrCodeValue+"_"+AppInitializer.pusherChannelEnv+"_"+Contexts.getSessionContext().get(Constants.CONST_ORGID));

		return jsonObjectMobile.toString();*/
		return null;
	}


	private JSONObject sendPusherMessage(Map<String, Object> rootMap, String channel)
	{
		RealtimefameworkPusher.sendViaRealtimeRestApi(rootMap, channel);

		JSONObject jsonObject = JSONObject.fromObject(rootMap);
		return jsonObject;
	}

	/*private JSONObject sendPusherMessage(final Map<String, Object> rootMap) {
		Ortc api = new Ortc();
		OrtcFactory factory = null;
		try {
			factory = api.loadOrtcFactory("IbtRealtimeSJ");
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		RealtimefameworkPusher.client = factory.createClient();
		RealtimefameworkPusher.client.setClusterUrl(RealtimefameworkPusher.serverUrl);
		RealtimefameworkPusher.client.setConnectionMetadata("UserConnectionMetadata"); 
		RealtimefameworkPusher.client.onConnected = new OnConnected() {
			@Override
			public void run(OrtcClient sender) {
				// Messaging client is connected
				RealtimefameworkPusher.client.send(AppInitializer.pusherChannelEnv,  (JSONObject.fromObject(rootMap)).toString());
				System.out.println("messge send");
			}
		}; 
		RealtimefameworkPusher.client.connect(RealtimefameworkPusher.applicationKey, RealtimefameworkPusher.authenticationToken);
		JSONObject jsonObject = JSONObject.fromObject(rootMap);
		return jsonObject;
	}*/

	private void sendNotification(Guest guestToNotify, WaitlistMetrics oWaitlistMetrics, int tempLevel, String freeTextContent)
	{
		GuestNotificationBean guestNotificationBean = new GuestNotificationBean();
		guestNotificationBean.setEmail(guestToNotify.getEmail());
		if(oWaitlistMetrics != null){
			guestNotificationBean.setGuestCount(Long.parseLong(oWaitlistMetrics.getTotalWaitingGuest()+""));
			guestNotificationBean.setTotalWaitTime(Long.parseLong(oWaitlistMetrics.getTotalWaitTime()+""));
			guestNotificationBean.setGuestNotifiedWaitTime(Long.parseLong(oWaitlistMetrics.getGuestNotifiedWaitTime()+""));
			guestNotificationBean.setGuestNotifiedWaitTime(Long.parseLong(oWaitlistMetrics.getGuestNotifiedWaitTime()+""));
			guestNotificationBean.setClientBase(oWaitlistMetrics.getClientBase());

		}
		guestNotificationBean.setGuestNoPrefix(getGuestNoPrefix(guestToNotify.getOrganizationID()));
		guestNotificationBean.setPrefType(guestToNotify.getPrefType());
		guestNotificationBean.setRank(guestToNotify.getRank());
		//log.info("LOG 1 ::::: $$$$ :::::: "+guestNotificationBean.getRank());
		guestNotificationBean.setSms(guestToNotify.getSms());
		guestNotificationBean.setUuid(guestToNotify.getUuid());
		//guestNotificationBean.setNotificationFlag(notificationFlag);
		guestNotificationBean.setTempLevel(tempLevel);
		guestNotificationBean.setDeviceId(guestToNotify.getDeviceId());
		guestNotificationBean.setDeviceType(guestToNotify.getDeviceType());
		guestNotificationBean.setOrgId(guestToNotify.getOrganizationID());
		guestNotificationBean.setMessage(freeTextContent);
		waitListService.sendNotificationToGuest(guestNotificationBean);
	}

	public Map<Integer, String> getGuestSeatingPrefMap() throws NumberFormatException, RsntException
	{
		List<Object[]> guestPrefLookupObjArr = null;
		guestPrefLookupObjArr = waitListService.getLookupsForLookupType(new Long(Constants.CONT_LOOKUPTYPE_GUEST_PREF));
		Map<Integer, String> guestPreferenceMap = new HashMap<Integer, String>();
		for(Object[] lookupObj : guestPrefLookupObjArr) {
			guestPreferenceMap.put((Integer)lookupObj[0], (String) lookupObj[1]);
		}
		return guestPreferenceMap;
	}

	// change by sunny for getting marketing preference map (2018-07-05)
	public Map<Integer, String> getGuestMarketingPrefMap() throws NumberFormatException, RsntException {
		List<Object[]> guestMarketingPrefLookupObjArr = null;
		guestMarketingPrefLookupObjArr = waitListService
				.getLookupsForLookupType(new Long(Constants.CONT_LOOKUPTYPE_GUEST_MARKETING_PREF));
		Map<Integer, String> guestMarketingPreferenceMap = new HashMap<Integer, String>();

		for (Object[] lookupObj : guestMarketingPrefLookupObjArr) {
			guestMarketingPreferenceMap.put((Integer) lookupObj[0], (String) lookupObj[1]);
		}

		return guestMarketingPreferenceMap;
	}
	private Map<String, Integer> getGuestSeatingPrefMapDescKey() throws NumberFormatException, RsntException
	{
		List<Object[]> guestPrefLookupObjArr = null;
		guestPrefLookupObjArr = waitListService.getLookupsForLookupType(new Long(Constants.CONT_LOOKUPTYPE_GUEST_PREF));
		Map<String, Integer> guestPreferenceMap = new HashMap<String, Integer>();
		for(Object[] lookupObj : guestPrefLookupObjArr) {
			guestPreferenceMap.put((String) lookupObj[1], (Integer)lookupObj[0]);
		}
		return guestPreferenceMap;
	}

	private String buildSeatingPreference(GuestDTO guestDTO) throws NumberFormatException, RsntException {
		// TODO Auto-generated method stub
		if(guestDTO.getSeatingPreference() != null) {
			Map<String, Integer> guestPreferenceMap = getGuestSeatingPrefMapDescKey();
			String [] seatPrefArr = guestDTO.getSeatingPreference().split(",");
			String seatingPreference = "";
			for(String s : seatPrefArr) {
				Integer seatPrefIndex = guestPreferenceMap.get(s);
				if("".equals(seatingPreference)){
					seatingPreference = seatPrefIndex+"";
				} else {
					seatingPreference = seatingPreference + "," + seatPrefIndex;
				}
			}
			return seatingPreference;
		} else {
			return null;
		}
	}

	@RequestMapping(value = "/callbackBW", method = RequestMethod.POST,consumes= "application/json")
	public void callbackBW(
			@RequestBody SmsEvent smsEvent) {

		System.out.println("callback test successful" + smsEvent.getMessageId());
	}

	/*for implementing dynamic footer (krupali 13/07/2017)*/

	/**
	 * Get propetyFile info(footer) 			
	 * @return Map<String, String> 
	 */
	//@GET
	//@Path("/propertyFileInfo")
	@RequestMapping(value = "/propertyFileInfo", method = RequestMethod.GET, produces = "application/json")
	public Response<Map<String, String>> getPropertyFileInfo(){
		String footerMsg = "";
		Response<Map<String, String>> response = new Response<Map<String,String>>();
		Map<String, String> propInfo = new HashMap<String, String>();
		try{
			Properties rsntProperties = new Properties();
			rsntProperties.load(this.getClass().getClassLoader().getResourceAsStream(Constants.RSNTPROPERTIES));
			footerMsg = rsntProperties.getProperty("rsnt.footerMsg");
			propInfo.put(Constants.FOOTER_MSG, footerMsg);
			//final JSONObject jsonObject = JSONObject.fromObject(eventConfig);
			//return jsonObject.toString();
			response.setServiceResult(propInfo);
		}
		catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		CommonUtil.setWebserviceResponse(response, Constants.SUCCESS, null);
		return response;
	}

	/**/

	/**
	 * This endpoint is used to send sms to particular user fron sms button on the frot end. 
	 * Also used to leverage the free form of the sms.
	 */

	@RequestMapping(value = "/sendSMS", method = RequestMethod.POST, produces = "application/json")
	public Response<Map<String, String>> sendSMS(@RequestBody SendSMSWrapper smsWrapper){
		Response<Map<String, String>> response = new Response<Map<String,String>>();
		try{
			Guest guest = waitListService.getGuestById(smsWrapper.getGuestId());
			/*String notificationFlag=null;*/
			int tempLevel=0;
			if(smsWrapper.getTemplateLevel() != null){
				tempLevel = smsWrapper.getTemplateLevel();
			}

			if(smsWrapper.getMetrics() != null){
				Map<String, String> oWaitlistMetrics = waitListService.getTotalPartyWaitTimeMetrics(guest.getOrganizationID());
				smsWrapper.setMetrics(waitListService.convertToObject(oWaitlistMetrics));
			}

			if(tempLevel == 1){
				List<OrganizationTemplateDTO> templates= waitListService.getOrganizationTemplates(guest.getOrganizationID(),guest.getLanguagePrefID().getLangId(),1);
				if(templates.size()>0) {
					String smsStr = templates.get(0).getTemplateText();
					smsStr=smsStr.replaceAll("G_name", guest.getName());
					smsStr=smsStr.replaceAll("G_rank", guest.getRank().toString());
					smsStr=smsStr.replaceAll("P_ahead", String.valueOf(smsWrapper.getMetrics().getTotalWaitingGuest()-1));
					smsStr=smsStr.replaceAll("W_time", String.valueOf(smsWrapper.getMetrics().getTotalWaitTime()));
					smsStr=smsStr.replaceAll("Turl", messageReceiver.buildURL(smsWrapper.getMetrics().getClientBase(), guest.getUuid()));
					smsWrapper.setSmsContent(smsStr);
					smsWrapper.setTemplateId(templates.get(0).getSmsTemplateID());
				}
			}

			sendNotification(guest, smsWrapper.getMetrics(), tempLevel, smsWrapper.getSmsContent());
			waitListService.saveSmsLog(guest,smsWrapper.getOrgId(),smsWrapper.getTemplateId(), smsWrapper.getSmsContent());
			CommonUtil.setWebserviceResponse(response, Constants.SUCCESS, null);
		}
		catch (Exception e) {
			e.printStackTrace();
			CommonUtil.setWebserviceResponse(response, Constants.ERROR, null, null,
					"System Error - usermetriks failed");
			// TODO: handle exception
		}
		return response;
	}

	/**
	 * Fetch organization sms template Details By Id
	 * @param orgId
	 * @return {@link OrganizationTemplateDTO}
	 */
	//@GET
	//@Path("/fetchSMSTemplates")
	//@Produces(MediaType.APPLICATION_JSON)

	@RequestMapping(value = "/fetchSMSTemplates", method = RequestMethod.GET, produces = "application/json")
	public Response<List<OrganizationTemplateDTO>> fetchOrgSMSTemplatesById(@RequestParam("organizationID") Long orgId,@RequestParam("langPrefID") Long langID){


		Response<List<OrganizationTemplateDTO>> response = new Response<List<OrganizationTemplateDTO>>();
		try {
			List<OrganizationTemplateDTO> organizationTemplateDTOs = waitListService.getOrganizationTemplates(orgId,langID,null);
			response.setServiceResult(organizationTemplateDTOs);
			CommonUtil.setWebserviceResponse(response, Constants.SUCCESS, null);
		} catch (Exception e) {
			log.error("fetchOrgSMSTemplatesById(orgId) - failed:", e);
			CommonUtil.setWebserviceResponse(response, Constants.ERROR, null, null,
					"System Error - fetch Organization Templates");
		}

		return response;

	}
	/**
	 * this API is used for fetching SMS contents dynamically
	 * @param orgID,langID,tempLevel
	 * @return { }
	 */
	//@GET
	//@Path("/fetchSMSTemplates")
	//@Produces(MediaType.APPLICATION_JSON)
	@RequestMapping(value = "/fetchSmsContent", method = RequestMethod.POST, produces = "application/json")
	public Response<Map<String, String>> fetchSmsContent(@RequestBody SmsContentParamDTO smsContentParam){
		Response<Map<String, String>> response = new Response<Map<String, String>>();
		try {
			List<OrganizationTemplateDTO> organizationTemplateDTOs = waitListService.getOrganizationTemplates(smsContentParam.getOrgId(),smsContentParam.getLangId(),null);
			Map<String, String> smsContents = new HashMap<String,String>();

			//Map<String, String> oWaitlistMetrics = waitListService.getTotalPartyWaitTimeMetrics(smsContentParam.getOrgId());
			//System.out.println("owaitlistMatrics : "+oWaitlistMetrics);
			//WaitlistMetrics metrics = waitListService.convertToObject(waitListService.getTotalPartyWaitTimeMetrics(smsContentParam.getOrgId()));
			Map<String, String> metrics = waitListService.getGuesteMetrics(smsContentParam.getGuestId(),smsContentParam.getOrgId());
			//System.out.println("usermetrics : "+metrciks);

			for (OrganizationTemplateDTO organizationTemplateDTO : organizationTemplateDTOs) {
				String smsContent = organizationTemplateDTO.getTemplateText();
				System.out.println("smsContent "+smsContent);
				smsContent = smsContent.replaceAll("G_name", smsContentParam.getGuestName());
				smsContent = smsContent.replaceAll("#G_name", smsContentParam.getGuestName()); 
				smsContent = smsContent.replace("G_rank", smsContentParam.getGusetRank().toString());
				smsContent = smsContent.replace("Turl", messageReceiver.buildURL(smsContentParam.getClientBase(),smsContentParam.getGuestUuid()));
				smsContent = smsContent.replace("P_ahead", metrics.get("GUEST_AHEAD_COUNT"));
				smsContent = smsContent.replace("W_time",metrics.get("TOTAL_WAIT_TIME"));
				smsContents.put("smsLevel"+organizationTemplateDTO.getLevel(), smsContent	);
			}
			response.setServiceResult(smsContents);
			CommonUtil.setWebserviceResponse(response, Constants.SUCCESS, null);
		} catch (Exception e) {
			log.error("fetchOrgSMSTemplatesById(orgId) - failed:", e);
			CommonUtil.setWebserviceResponse(response, Constants.ERROR, null, null,
					"System Error - fetch Organization Templates");
		}
		return response;
	}


	//saveOrupdateProfile by Aarshi(15/03/2019)
	@RequestMapping(value = "/saveOrUpdateProfile", method = RequestMethod.POST, produces = "application/json", consumes="application/json")
	public Response<UserDTO> saveOrUpdateProfile(@RequestBody Credential credentials,HttpServletRequest request) throws RsntException{
		Response<UserDTO> response = new Response<UserDTO>();
		Properties oProperties=new Properties();
		try {
			oProperties = PropertyUtility.fetchPropertyFile(this.getClass(),Constants.RSNTPROPERTIES);
		} catch (IOException e) {
			LoggerUtil.logError("Unable to Fetch file");
		}
		try{

			String userExists=securityService.checkIfExistingUser(credentials.getUserId(),credentials.getUsername(),credentials.getEmail());
			if(userExists.equals("FALSE")){
				Boolean statusOfUpdate=waitListService.updateOrSaveProfile(credentials);

				HttpSession sessionObj = request.getSession();
				UserDTO userDTO = prepareUserObj(credentials);
				sessionObj.setAttribute(Constants.USER_OBJ, userDTO);
				response.setServiceResult(userDTO);
				response.setStatus(oProperties.getProperty(Constants.USER_UPDATE_SUCCESS));
				CommonUtil.setWebserviceResponse(response, Constants.SUCCESS, oProperties.getProperty(Constants.USER_UPDATE_SUCCESS));

			}else{
				response.setStatus(oProperties.getProperty(Constants.USER_UPDATE_FAIL));
				CommonUtil.setWebserviceResponse(response, Constants.SUCCESS, oProperties.getProperty(Constants.USER_UPDATE_FAIL));
			}

		}catch(Exception ex){

			response.setStatus(oProperties.getProperty(Constants.USER_UPDATE_FAIL));
			LoggerUtil.logError("Error saveOrUpdateProfile", ex);
			CommonUtil.setWebserviceResponse(response, Constants.FAILURE, "","",(oProperties.getProperty(Constants.USER_UPDATE_FAIL)));

		}
		return response;
	}
	////USERDTO by Aarshi(19/03/2019)
	private UserDTO prepareUserObj(Credential user) {
		UserDTO userDTO = new UserDTO();
		userDTO.setUserId(Long.parseLong(user.getUserId()));
		userDTO.setOrganizationId(Long.parseLong(user.getOrgId()));
		userDTO.setClientBase(user.getClientBase());
		userDTO.setCompanyEmail(user.getCompanyEmail());
		userDTO.setCompanyName(user.getCompanyName());
		userDTO.setPrimaryContactNo(user.getCompanyPrimaryPhone());
		userDTO.setEmail(user.getEmail());
		userDTO.setFirstName(user.getFirstName());
		userDTO.setLastName(user.getLastName());
		userDTO.setUserName(user.getUsername());
		userDTO.setAddress(user.getAddressDTO().toString());
		return userDTO;
	}


	//arjun 	21/11/2019
	/**
	 Get Guest Detail
	 @param contactNumber - Mobile Number of User
	 @param orgID - organization Id of User
	 */
	@RequestMapping(value = "/getGuestDetail", method = RequestMethod.GET, produces = "application/json")
	public Response<List<GuestDTO>> getGuestDetail(@RequestParam("contactNumber") String contactNumber ,@RequestParam("orgID") String orgID) throws RsntException{
		log.info("Entering into getGuestDetail");

		Response<List<GuestDTO>> response = new Response<List<GuestDTO>>();
		List<GuestDTO> guestDTOList = new ArrayList<GuestDTO>();
		List<GuestPreferencesDTO> guestPreferencesList =  null;
		
		GuestDTO guestDTO = new GuestDTO();

		try{

			Object[] result = waitListService.getGuestDetail(contactNumber,orgID); //get USER Details from GuestReset Table
			guestPreferencesList=waitListService.getOrganizationSeatingPref(Long.valueOf(orgID)); //GET Guest Preference By Organization Id
			if(!(result==null)){
				guestDTO.setGuestID(Long.parseLong(result[0].toString())); 			//guestID
				guestDTO.setOrganizationID(Long.parseLong(result[1].toString())); 	//OrganizationID
				guestDTO.setName(result[2].toString());  						 	//name
				guestDTO.setSms(result[3].toString());     							//mobile(SMS)

				String guestPrefArr[] = result[4].toString().split(",");   			//seatingPrefrence
				
				for (String string : guestPrefArr)
					guestPreferencesList.forEach(gl -> {
						if(string.equals(gl.getPrefValueId().toString()))
						gl.setSelected(true); 
					  });
			}
			guestDTO.setGuestPreferences(guestPreferencesList);
			guestDTOList.add(guestDTO);
			response.setServiceResult(guestDTOList);

			CommonUtil.setWebserviceResponse(response, Constants.SUCCESS, null);
		}catch (Exception e) {
			log.error("Error occure while Fetch Guest Details:", e);
			CommonUtil.setWebserviceResponse(response, Constants.ERROR, null, null,
					"Error occure while Fetch Guest Details");
		}
		return response;
	}
	
	@RequestMapping(value = "/refreshLanguage", method = RequestMethod.GET, produces = "application/json")
	public Response<String> refreshLanguage () {
		Response<String> response = new Response<String>();

		Map<String, Object> rootMap = new HashMap<String, Object>();

		try {
			rootMap = waitListService.updateLanguagesPusher();

			if(rootMap.containsKey("updateLanguageFlag"))
			{
				sendPusherMessage(rootMap, AppInitializer.pusherChannelEnv);
				response.setServiceResult("Pusher sent successfully.");
			}
			else 
				response.setServiceResult("No Data Updated.");
		} catch (Exception e) {
			LoggerUtil.logError(e.getMessage(), e);
		}

		return response;
	}	
}