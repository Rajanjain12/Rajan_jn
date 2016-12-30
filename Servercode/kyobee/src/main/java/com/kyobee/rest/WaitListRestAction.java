/**
 * @author :ordextechnology
 * Provides rest services for Admin guest module 
 * services offered are: Basic CRUD on Guest Entity , Send notification and calculate guest 
 * wait time by organizationId
 * 	
 */

package com.kyobee.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.sf.json.JSONObject;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kyobee.dto.GuestDTO;
import com.kyobee.dto.GuestPreferencesDTO;
import com.kyobee.dto.WaitlistMetrics;
import com.kyobee.dto.common.Response;
import com.kyobee.entity.Guest;
import com.kyobee.entity.GuestNotificationBean;
import com.kyobee.entity.GuestPreferences;
import com.kyobee.exception.RsntException;
import com.kyobee.service.IWaitListService;
import com.kyobee.util.AppInitializer;
import com.kyobee.util.SessionContextUtil;
import com.kyobee.util.common.CommonUtil;
import com.kyobee.util.common.Constants;
import com.kyobee.util.common.RealtimefameworkPusher;



@RestController
@RequestMapping("/web/rest/waitlistRestAction")
public class WaitListRestAction {

	//************************************************
	// instance variable declaration
	//*************************************************

	//@Logger
	//private Log log;
	private Logger log = Logger.getLogger(WaitListRestAction.class);
	
	@Autowired
	private IWaitListService waitListService;
	
	@Autowired
	SessionContextUtil sessionContextUtil;

	
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
	@RequestMapping(value = "/waitingListBackupUpdate", method = RequestMethod.GET, produces = "application/json")
	public String waitingListBackupUpdate(@RequestParam("guestid") Long guestId){
		waitListService.sendWaitListBackupEmail(guestId, true);
		return Constants.RSNT_GUEST_SUCCESS;
	}

	/**
	 * Send Notification to guests by user preferences
	 * @param guestId
	 * @return String
	 */
	//@GET
	//@Path("/waitingListBackup")
	@RequestMapping(value = "/waitingListBackup", method = RequestMethod.GET, produces = "application/json")
	public String waitingListBackup(@RequestParam("guestid") Long guestId){
		waitListService.sendWaitListBackupEmail(guestId, false);
		return Constants.RSNT_GUEST_SUCCESS;
	}

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
	public Guest convertGuesVoToEntity(GuestDTO guest){
		Guest guestObj = new Guest();
		guestObj.setName(guest.getName());
		guestObj.setNote(guest.getNote());
		guestObj.setOrganizationID(guest.getOrganizationID());
		guestObj.setEmail(guest.getEmail());
		guestObj.setSms(guest.getSms());
		guestObj.setOrganizationID(guest.getOrganizationID());
		guestObj.setNoOfPeople(guest.getNoOfPeople());
		guestObj.setOptin(guest.isOptin());
		guestObj.setStatus(guest.getStatus());
		guestObj.setPrefType(guest.getPrefType());
		/*if(null != guest.getGuestPreferences()) {
			GuestPreferences guestPref = null;
			List<GuestPreferences> guestPreferences = new ArrayList<GuestPreferences>(guest.getGuestPreferences().size());
			for (GuestPreferencesDTO pref : guest.getGuestPreferences()) {
				guestPref = new GuestPreferences();
				guestPref.setGuestPrefId(pref.getGuestPrefId());
				guestPref.setPrefValueId(pref.getPrefValueId());
				guestPref.setPrefValue(pref.getPrefValue());
				guestPref.setGuest(guestObj);
				guestPreferences.add(guestPref);
			}
			guestObj.setGuestPreferences(guestPreferences);
		}*/
		if(null != guest.getDeviceId())
			guestObj.setDeviceId(guest.getDeviceId());
		if(null != guest.getDeviceType())
			guestObj.setDeviceType(guest.getDeviceType());
		if(null != guest.getGuestPreferences()) {
			String seatingPreference = null;
			List<GuestPreferencesDTO> guestPrefList = guest.getGuestPreferences();
			for(GuestPreferencesDTO o : guestPrefList) {
					if(null == seatingPreference)
						seatingPreference = o.getPrefValueId()+"";
					else
						seatingPreference = seatingPreference + "," + o.getPrefValueId();
			}
			guestObj.setSeatingPreference(seatingPreference);
		}
		if(null != guest.getSeatingPreference())
			guestObj.setSeatingPreference(guest.getSeatingPreference());
		if(null != guest && null != guest.getGuestID()) {
			guestObj.setUpdatedTime(new Date());
			guestObj.setUuid(guest.getUuid());
			guestObj.setGuestID(guest.getGuestID());
			guestObj.setRank(guest.getRank());
			guestObj.setCalloutCount(guest.getCalloutCount());
		}else{
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
	public GuestDTO convertGuesEntityToVo(Guest guest, Map<Integer, String> guestPreferenceMap){
		GuestDTO guestObj = null;
		try{
			guestObj= new GuestDTO();
			guestObj.setName(guest.getName());
			guestObj.setNote(guest.getNote());
			guestObj.setOrganizationID(guest.getOrganizationID());
			guestObj.setEmail(guest.getEmail());
			guestObj.setSms(guest.getSms());
			guestObj.setOrganizationID(guest.getOrganizationID());
			guestObj.setNoOfPeople(guest.getNoOfPeople());
			guestObj.setOptin(guest.isOptin());
			guestObj.setStatus(guest.getStatus());
			guestObj.setPrefType(guest.getPrefType());
			guestObj.setIncompleteParty(guest.getIncompleteParty());
			/*if(null != guest.getGuestPreferences()) {
			GuestPreferencesDTO guestPref = null;
			List<GuestPreferencesDTO> guestPreferences = new ArrayList<GuestPreferencesDTO>(guest.getGuestPreferences().size());
			for (GuestPreferences pref : guest.getGuestPreferences()) {
				guestPref = new GuestPreferencesDTO();
				guestPref.setGuestPrefId(pref.getGuestPrefId());
				guestPref.setPrefValueId(pref.getPrefValueId());
				System.out.println("pref.getPrefValue()"+pref.getPrefValue());
				guestPref.setPrefValue(pref.getPrefValue());
				//guestPref.setGuest(guestObj);
				guestPreferences.add(guestPref);
			}
			guestObj.setGuestPreferences(guestPreferences);
		}*/
			String seatingPrefForDTO = "";
			if(null != guest.getSeatingPreference() && !"".equals(guest.getSeatingPreference())) {
				try {
				String seatingPrefIdArr[] = guest.getSeatingPreference().split(",");
				for(int i = 0; i < seatingPrefIdArr.length; ++i) {
					String seatingPrefDesc = guestPreferenceMap.get(Integer.parseInt(seatingPrefIdArr[i]));
					if(i == 0)
						seatingPrefForDTO = seatingPrefDesc;
					else 
						seatingPrefForDTO = seatingPrefForDTO + "," + seatingPrefDesc;
				}
				} catch ( NumberFormatException nfe) {
					
				}
			}
			guestObj.setSeatingPreference(seatingPrefForDTO);
			if(null != guest && null != guest.getGuestID()) {
				guestObj.setUpdatedTime(new Date());
				guestObj.setUuid(guest.getUuid());
				guestObj.setGuestID(guest.getGuestID());
				guestObj.setRank(guest.getRank());
				guestObj.setCalloutCount(guest.getCalloutCount());
				guestObj.setIncompleteParty(guest.getIncompleteParty());

			}else{
				guestObj.setUuid(UUID.randomUUID().toString().substring(0, 8));
				guestObj.setCreatedTime(new Date());
				guestObj.setCheckinTime(new Date());
			}
		} catch (Exception e){
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
	public String getGuestWaitTimeMetricks(@RequestParam("guest") Long guestId,@RequestParam("orgid") Long orgid){
		Map<String, String> metrciks = waitListService.getGuesteMetrics(guestId,orgid);
		final JSONObject jsonObject = JSONObject.fromObject(metrciks);
		return jsonObject.toString();
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
	public String resetGuests(@RequestParam("orgid") Long orgid){
		int res= waitListService.resetOrganizationsByOrgid(orgid);
		if(res==1){
			return Constants.RSNT_GUEST_SUCCESS;
		}

		return Constants.RSNT_GUEST_FAIL;
	}
	/**
	 * Fetch Guests by Status of CHECKIN
	 * @param orgid
	 * @return List<GuestDTO> --List of guests
	 */
	//@GET
	//@Path("/checkinusers")
	//@Produces(MediaType.APPLICATION_JSON)
	@RequestMapping(value = "/checkinusers", method = RequestMethod.GET, produces = "application/json")
	public Response<List<GuestDTO>> fetchCheckinUsers(@RequestParam("orgid") Long orgid, 
			@RequestParam("recordsPerPage") int recordsPerPage, @RequestParam("pageNumber") int pageNumber){
		log.info("Entering :: fetchCheckinUsers --OrgId::"+orgid);
		Response<List<GuestDTO>> response = new Response<List<GuestDTO>>();
		List<GuestDTO> guestDTOs = null;
		//final Map<String, Object> rootMap = new LinkedHashMap<String, Object>();
		//final List<String> errorArray = new ArrayList<String>(0);
		Map<Integer, String> guestPreferenceMap;
		List<Guest> guests;
		try {

			guestPreferenceMap = getGuestSeatingPrefMap();

			guests = waitListService.loadAllCheckinUsers(orgid, recordsPerPage, pageNumber);
			if(null != guests && guests.size()>0){
				guestDTOs = new ArrayList<GuestDTO>(guests.size());
				for (Guest guest : guests) {
					guestDTOs.add(convertGuesEntityToVo(guest, guestPreferenceMap));
				}
				response.setServiceResult(guestDTOs);
				CommonUtil.setWebserviceResponse(response, Constants.SUCCESS, null);

			}

		} catch (RsntException e) {
			e.printStackTrace();
			log.error("fetchCheckinUsers() - failed:", e);
			//rootMap.put("id", -1);
			//rootMap.put(Constants.RSNT_ERROR, "System Error - fetchCheckinUsers failed");
			//rootMap.put("fieldErrors", errorArray);
			
			//final JSONObject jsonObject = JSONObject.fromObject(rootMap);
			//return jsonObject.toString();
			CommonUtil.setWebserviceResponse(response, Constants.ERROR, null, null,
					"System Error - fetchCheckinUsers failed");
		}
		//rootMap.put(Constants.RSNT_ERROR, "");
		//rootMap.put("guests",guestDTOs);
		//final JSONObject jsonObject = JSONObject.fromObject(rootMap);
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
	public String fetchGuestById(@RequestParam("guestid") int guestid){
		log.info("Entering :: fetchGuestById :: guestId"+guestid);
		final Map<String, Object> rootMap = new LinkedHashMap<String, Object>();
		final List<String> errorArray = new ArrayList<String>(0);
		Guest guestObject= null;
		try {
			guestObject = waitListService.getGuestById(guestid);
			//nonCloseParent(guestObject.getGuestPreferences());
		} catch (Exception e) {
			e.printStackTrace();
			log.error("fetchCheckinUsers() - failed:", e);
			rootMap.put("id", -1);
			rootMap.put(Constants.RSNT_ERROR, "System Error - fetchGuestById failed");
			rootMap.put("fieldErrors", errorArray);
			//nonCloseParent(guestObject.getGuestPreferences());
			final JSONObject jsonObject = JSONObject.fromObject(rootMap);
			return jsonObject.toString();
		}
		rootMap.put(Constants.RSNT_ERROR, "");
		rootMap.put("guests",guestObject);
		final JSONObject jsonObject = JSONObject.fromObject(rootMap);
		return jsonObject.toString();
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
	public String fetchGuestByUUID(@RequestParam("uuid") String uuid){
		log.info("Entering fetchGuestByUUID ::UUID::"+uuid);
		final Map<String, Object> rootMap = new LinkedHashMap<String, Object>();
		final List<String> errorArray = new ArrayList<String>(0);
		Guest guestObject= null;
		try {
			guestObject = waitListService.getGuestByUUID(uuid);
			//nonCloseParent(guestObject.getGuestPreferences());
		} catch (Exception e) {
			e.printStackTrace();
			log.error("fetchGuestByUUID() - failed:", e);
			rootMap.put("id", -1);
			rootMap.put(Constants.RSNT_ERROR, "System Error - fetchCheckinUsers failed");
			rootMap.put("fieldErrors", errorArray);
			final JSONObject jsonObject = JSONObject.fromObject(rootMap);
			return jsonObject.toString();
		}
		rootMap.put(Constants.RSNT_ERROR, "");
		rootMap.put("guests",guestObject);
		final JSONObject jsonObject = JSONObject.fromObject(rootMap);
		return jsonObject.toString();
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
	//changed for history pagination
	//public String fetchGuestsHistory(@QueryParam("orgid") Long orgid){
	public String fetchGuestsHistory(@RequestParam("orgid") Long orgid	, 
			@RequestParam("recordsPerPage") int recordsPerPage, @RequestParam("pageNumber") int pageNumber) {
		log.info("Entering :: fetchGuestsHistory ::orgid "+orgid);
		List<GuestDTO> guestDTOs = null;
		final Map<String, Object> rootMap = new LinkedHashMap<String, Object>();
		final List<String> errorArray = new ArrayList<String>(0);
		List<Guest> guests;
		Map<Integer, String> guestPreferenceMap;
		try {
			//changed for history pagination
			//guests = waitListService.loadGuestsHistoryByOrg(orgid);
			guestPreferenceMap = getGuestSeatingPrefMap();
			guests = waitListService.loadGuestsHistoryByOrgRecords(orgid,recordsPerPage, pageNumber);
			if(null != guests && guests.size()>0){
				guestDTOs = new ArrayList<GuestDTO>(guests.size());
				for (Guest guest : guests) {
					guestDTOs.add(convertGuesEntityToVo(guest, guestPreferenceMap));
				}

			}

		} catch (RsntException e) {
			e.printStackTrace();
			log.error("fetchGuestsHistory() - failed:", e);
			rootMap.put("id", -1);
			rootMap.put(Constants.RSNT_ERROR, "System Error - fetchCheckinUsers failed");
			rootMap.put("fieldErrors", errorArray);
			final JSONObject jsonObject = JSONObject.fromObject(rootMap);
			return jsonObject.toString();
		}
		rootMap.put(Constants.RSNT_ERROR, "");
		rootMap.put("guests",guestDTOs);
		final JSONObject jsonObject = JSONObject.fromObject(rootMap);
		return jsonObject.toString();
	}
	/**
	 * Get Event Listener Configuration details 
	 * @return Map<String, String> 
	 */
	//@GET
	//@Path("/pusgerinformation")
	@RequestMapping(value = "/pusgerinformation", method = RequestMethod.GET, produces = "application/json")
	public String getPushEventsConfigDetails(){
		// TODO - Uncomment this below section and replace with alternative for context.getSession - rohit
		Map<String, String> eventConfig = new HashMap<String, String>();
		eventConfig.put(Constants.REALTIME_APPLICATION_KEY, System.getProperty(Constants.REALTIME_APPLICATION_KEY_VAL));
		eventConfig.put(Constants.REALTIME_PRIVATE_KEY, System.getProperty(Constants.REALTIME_PRIVATE_KEY_VAL));
		eventConfig.put(Constants.PUSHER_CHANNEL_ENV, AppInitializer.pusherChannelEnv+"_"+sessionContextUtil.get(Constants.CONST_ORGID));
		eventConfig.put(Constants.QRCODE_VALUE, "123_"+AppInitializer.pusherChannelEnv+"_"+sessionContextUtil.get(Constants.CONST_ORGID));
		eventConfig.put(Constants.FOOTER_MSG, AppInitializer.staticFooterMsg);
		final JSONObject jsonObject = JSONObject.fromObject(eventConfig);
		return jsonObject.toString();
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
			searPref=	waitListService.getOrganizationSeatingPref(orgid);
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
	public Response<String> addGuest (@RequestBody GuestDTO guestDTO) {
		log.info("Entering into addGuest");
		Response<String> response = new Response<String>();
		Guest guest = null;
		final Map<String, Object> rootMap = new LinkedHashMap<String, Object>();
		WaitlistMetrics oWaitlistMetrics = null;
		try {
			guest = convertGuesVoToEntity(guestDTO);
			
			oWaitlistMetrics = waitListService.addGuest(guest);

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
			rootMap.put("guestRank", oWaitlistMetrics.getGuestRank());

			sendNotification(guest, oWaitlistMetrics, "NORMAL");
			sendPusherMessage(rootMap, AppInitializer.pusherChannelEnv+"_"+rootMap.get("orgid"));
			response.setServiceResult("");
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
	public String updateGuestInfo (@RequestBody String guestJSONStr) {
		log.info("Entering into updateGuestInfo");
		ObjectMapper objectMapper = new ObjectMapper();
		GuestDTO guestDTO = null;
		Guest guest = null;
		JSONObject jsonObject = null;
		final Map<String, Object> rootMap = new LinkedHashMap<String, Object>();
		WaitlistMetrics oWaitlistMetrics = null;
		try {
			guestDTO = objectMapper.readValue(guestJSONStr, GuestDTO.class);
			guest = convertGuesVoToEntity(guestDTO);
			oWaitlistMetrics = waitListService.updateGuestInfo(guest, Constants.WAITLIST_UPDATE_GUEST);

			rootMap.put(Constants.RSNT_NOW_SERVING_GUEST_ID, oWaitlistMetrics.getNowServingParty());
			rootMap.put(Constants.RSNT_ORG_TOTAL_WAIT_TIME, oWaitlistMetrics.getTotalWaitTime());
			rootMap.put(Constants.RSNT_NEXT_TO_NOTIFY_GUEST_ID, oWaitlistMetrics.getGuestToBeNotified());

			guest.setRank(Long.parseLong(oWaitlistMetrics.getGuestId()+""));

			jsonObject = JSONObject.fromObject(rootMap);

		} catch (JsonParseException e) {
			log.error("Error :: saveOrUpdateGuest ", e);
		} catch (JsonMappingException e) {
			log.error("Error :: saveOrUpdateGuest ", e);
		} catch (IOException e) {
			log.error("Error :: saveOrUpdateGuest ", e);
		}
		catch (Exception e) {
			e.printStackTrace();
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
		
		//sendNotification(guest, guestCount, totalWaitTime);

		jsonObject = sendPusherMessage(rootMap, AppInitializer.pusherChannelEnv+"_"+rootMap.get("orgid"));

		return jsonObject.toString();
	}


	/**
	 Update Guest information
	 @param guestJSONStr : Guest object in JSON format
	 @return String - Updated Guest Object
	 */
	//@GET
	//@Path("/deleteGuest")
	@RequestMapping(value = "/deleteGuest", method = RequestMethod.GET, produces = "application/json")
	public String deleteGuest (@RequestParam("guestId") String guestId, @RequestParam("orgId")  int orgId) {
		log.info("Entering into deleteGuest");
		Guest guest = new Guest();
		guest.setGuestID(Long.parseLong(guestId+""));
		guest.setOrganizationID(Long.parseLong(orgId+""));
		JSONObject jsonObject = null;
		WaitlistMetrics oWaitlistMetrics = null;
		final Map<String, Object> rootMap = new LinkedHashMap<String, Object>();
		Long totalWaitTime = null;
		try {
			oWaitlistMetrics = waitListService.updateGuestInfo(guest, Constants.WAITLIST_UPDATE_DELETE);

			rootMap.put(Constants.RSNT_NOW_SERVING_GUEST_ID, oWaitlistMetrics.getNowServingParty());
			rootMap.put(Constants.RSNT_ORG_TOTAL_WAIT_TIME, oWaitlistMetrics.getTotalWaitTime());
			rootMap.put(Constants.RSNT_NEXT_TO_NOTIFY_GUEST_ID, oWaitlistMetrics.getGuestToBeNotified());

			guest.setRank(Long.parseLong(oWaitlistMetrics.getGuestId()+""));
			totalWaitTime = Long.parseLong(oWaitlistMetrics.getTotalWaitTime()+"");
			jsonObject = JSONObject.fromObject(rootMap);

		} catch (Exception e) {
			e.printStackTrace();
		}

		rootMap.put("OP", "DEL");
		rootMap.put("guestObj", oWaitlistMetrics.getGuestToBeNotified());
		rootMap.put("nowServingParty", oWaitlistMetrics.getNowServingParty());
		rootMap.put("FROM", "ADMIN");
		//rootMap.put("ppwt": $jquery("#perPartyWaitTime").val(),
		rootMap.put("totalWaitTime",totalWaitTime);
		rootMap.put("orgid", orgId);
		rootMap.put("numberofparties", oWaitlistMetrics.getTotalWaitingGuest());

		
		if(oWaitlistMetrics.getGuestToBeNotified() != -1){
			
			if(guest.getGuestID() <= oWaitlistMetrics.getGuestToBeNotified()) {
				Guest guestToBeNotified = waitListService.getGuestById(oWaitlistMetrics.getGuestToBeNotified());
				sendNotification(guestToBeNotified, oWaitlistMetrics, Constants.NOTIF_THRESHOLD_ENTERED);
			}
		}
		jsonObject = sendPusherMessage(rootMap, AppInitializer.pusherChannelEnv+"_"+rootMap.get("orgid"));

		return jsonObject.toString();
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
	public String changeNotificationThreshold(@RequestParam("numberofusers")  int numberOfUsers,@RequestParam("perPartyWaitTime") int perPartyWaitTime, @RequestParam("orgid") Long orgid){
		final Map<String, Object> rootMap = new LinkedHashMap<String, Object>();
		WaitlistMetrics oWaitlistMetrics = waitListService.changeNotificationThreshold(numberOfUsers,perPartyWaitTime, orgid);

		rootMap.put(Constants.RSNT_NOW_SERVING_GUEST_ID, oWaitlistMetrics.getNowServingParty());
		rootMap.put(Constants.RSNT_ORG_TOTAL_WAIT_TIME, oWaitlistMetrics.getTotalWaitTime());
		rootMap.put(Constants.RSNT_NEXT_TO_NOTIFY_GUEST_ID, oWaitlistMetrics.getGuestToBeNotified());

		JSONObject jsonObject = JSONObject.fromObject(rootMap);

		rootMap.put("OP", "NOTIFY_USER");
		rootMap.put("FROM", "ADMIN");
		rootMap.put("notifyUser", numberOfUsers);
		rootMap.put("totalWaitTime", oWaitlistMetrics.getTotalWaitTime());
		rootMap.put("orgid", orgid);

		jsonObject = sendPusherMessage(rootMap, AppInitializer.pusherChannelEnv+"_"+rootMap.get("orgid"));

		return jsonObject.toString();
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
	public String changePerPartyWaitTime(@RequestParam("numberofusers")  int numberOfUsers, @RequestParam("perPartyWaitTime") int perPartyWaitTime,@RequestParam("orgid") Long orgid){
		final Map<String, Object> rootMap = new LinkedHashMap<String, Object>();
		WaitlistMetrics oWaitlistMetrics = waitListService.changePerPartyWaitTime(numberOfUsers, perPartyWaitTime,orgid);

		rootMap.put(Constants.RSNT_NOW_SERVING_GUEST_ID, oWaitlistMetrics.getNowServingParty());
		rootMap.put(Constants.RSNT_ORG_TOTAL_WAIT_TIME, oWaitlistMetrics.getTotalWaitTime());
		rootMap.put(Constants.RSNT_NEXT_TO_NOTIFY_GUEST_ID, oWaitlistMetrics.getGuestToBeNotified());

		JSONObject jsonObject = JSONObject.fromObject(rootMap);

		rootMap.put("OP", "PPT_CHG");
		rootMap.put("FROM", "ADMIN");
		rootMap.put("totalWaitTime", oWaitlistMetrics.getTotalWaitTime());
		//rootMap.put("notifyUser", numberOfUsers);
		rootMap.put("ppwt", perPartyWaitTime);
		rootMap.put("orgid", orgid);
		rootMap.put("nowServingParty", oWaitlistMetrics.getNowServingParty());

		jsonObject = sendPusherMessage(rootMap, AppInitializer.pusherChannelEnv+"_"+rootMap.get("orgid"));
		return jsonObject.toString();
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
	public String incrementCalloutCount (@RequestBody String guestJSONStr) {
		log.info("Entering into incrementCalloutCount");
		JSONObject jsonObject = null;
		ObjectMapper objectMapper = new ObjectMapper();
		GuestDTO guestDTO = null;
		Guest guest = null;
		final Map<String, Object> rootMap = new LinkedHashMap<String, Object>();
		WaitlistMetrics oWaitlistMetrics = null;
		Long guestCount = null;

		try {
			guestDTO = objectMapper.readValue(guestJSONStr, GuestDTO.class);
			guest = convertGuesVoToEntity(guestDTO);
			String seatingPreference = buildSeatingPreference(guestDTO);
			guest.setSeatingPreference(seatingPreference);
			oWaitlistMetrics = waitListService.updateGuestInfo(guest, Constants.WAITLIST_UPDATE_CALLOUT);

			rootMap.put(Constants.RSNT_NOW_SERVING_GUEST_ID, oWaitlistMetrics.getNowServingParty());
			rootMap.put(Constants.RSNT_ORG_TOTAL_WAIT_TIME, oWaitlistMetrics.getTotalWaitTime());
			rootMap.put(Constants.RSNT_NEXT_TO_NOTIFY_GUEST_ID, oWaitlistMetrics.getGuestToBeNotified());

			guestCount = Long.parseLong(oWaitlistMetrics.getTotalWaitingGuest()+"");

			jsonObject = JSONObject.fromObject(rootMap);

		}catch (Exception e) {
			e.printStackTrace();
		}

		rootMap.put("OP", "UPD");
		rootMap.put("guestObj", guest.getGuestID());
		rootMap.put("updguest", guest);
		rootMap.put("FROM", "ADMIN");
		rootMap.put("ORG_GUEST_COUNT", guestCount);
		rootMap.put("totalWaitTime", oWaitlistMetrics.getTotalWaitTime());

		//rootMap.put("ppwt": $jquery("#perPartyWaitTime").val(),
		rootMap.put("orgid", guest.getOrganizationID());

		if(oWaitlistMetrics.getGuestToBeNotified() != -1){
			if(guest.getGuestID() <= oWaitlistMetrics.getGuestToBeNotified()){
				Guest guestToNotify = waitListService.getGuestById((long)oWaitlistMetrics.getGuestToBeNotified());
				sendNotification(guestToNotify, oWaitlistMetrics, Constants.NOTIF_THRESHOLD_ENTERED);
			}
		}
		
		jsonObject = sendPusherMessage(rootMap, AppInitializer.pusherChannelEnv+"_"+rootMap.get("orgid"));

		return jsonObject.toString();
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
	public String markAsIncomplete (@RequestBody String guestJSONStr) {
		log.info("Entering into markAsIncomplete");
		ObjectMapper objectMapper = new ObjectMapper();
		GuestDTO guestDTO = null;
		Guest guest = new Guest();
		JSONObject jsonObject = null;
		WaitlistMetrics oWaitlistMetrics = null;
		final Map<String, Object> rootMap = new LinkedHashMap<String, Object>();
		Long guestCount = null;

		try {
			guestDTO = objectMapper.readValue(guestJSONStr, GuestDTO.class);
			guest = convertGuesVoToEntity(guestDTO);
			String seatingPreference = buildSeatingPreference(guestDTO);
			guest.setSeatingPreference(seatingPreference);
			oWaitlistMetrics = waitListService.updateGuestInfo(guest, Constants.WAITLIST_UPDATE_INCOMPLETE);

			rootMap.put(Constants.RSNT_NOW_SERVING_GUEST_ID, oWaitlistMetrics.getNowServingParty());
			rootMap.put(Constants.RSNT_ORG_TOTAL_WAIT_TIME, oWaitlistMetrics.getTotalWaitTime());
			rootMap.put(Constants.RSNT_NEXT_TO_NOTIFY_GUEST_ID, oWaitlistMetrics.getGuestToBeNotified());

			guestCount = Long.parseLong(oWaitlistMetrics.getTotalWaitingGuest()+"");

			jsonObject = JSONObject.fromObject(rootMap);

		} catch (Exception e) {
			e.printStackTrace();
		}

		rootMap.put("OP", "UPD");
		rootMap.put("guestObj", guest.getGuestID());
		rootMap.put("updguest", guest);
		rootMap.put("FROM", "ADMIN");
		rootMap.put("ORG_GUEST_COUNT", guestCount);
		rootMap.put("totalWaitTime", oWaitlistMetrics.getTotalWaitTime());

		rootMap.put("orgid", guest.getOrganizationID());
		
		if(oWaitlistMetrics.getGuestToBeNotified() != -1){
			if(guest.getGuestID() <= oWaitlistMetrics.getGuestToBeNotified()){
				Guest guestToNotify = waitListService.getGuestById((long)oWaitlistMetrics.getGuestToBeNotified());
				sendNotification(guestToNotify, oWaitlistMetrics, Constants.NOTIF_THRESHOLD_ENTERED);
			}
		}
		
		jsonObject = sendPusherMessage(rootMap, AppInitializer.pusherChannelEnv+"_"+rootMap.get("orgid"));

		return jsonObject.toString();
	}

	/**
	 Marks As Seated
	 @param guestJSONStr : Guest object in JSON format
	 @return String - Updated Guest Object
	 */
	//@GET
	//@Path("/markAsSeated")
	@RequestMapping(value = "/markAsSeated", method = RequestMethod.GET, produces = "application/json")
	public String markAsSeated (@RequestParam("guestId")  int guestId, @RequestParam("orgId")  int orgId) {
		log.info("Entering into markAsSeated");
		Guest guestToBeSeated = waitListService.getGuestById(guestId);
		/*guest.setGuestID(Long.parseLong(guestId+""));
		guest.setOrganizationID((long)orgId);*/
		JSONObject jsonObject = null;
		WaitlistMetrics oWaitlistMetrics = null;
		final Map<String, Object> rootMap = new LinkedHashMap<String, Object>();
		Long guestCount = null;
		try {
			oWaitlistMetrics = waitListService.updateGuestInfo(guestToBeSeated, Constants.WAITLIST_UPDATE_MARK_AS_SEATED);

			rootMap.put(Constants.RSNT_NOW_SERVING_GUEST_ID, oWaitlistMetrics.getNowServingParty());
			rootMap.put(Constants.RSNT_ORG_TOTAL_WAIT_TIME, oWaitlistMetrics.getTotalWaitTime());
			rootMap.put(Constants.RSNT_NEXT_TO_NOTIFY_GUEST_ID, oWaitlistMetrics.getGuestToBeNotified());

			guestCount = Long.parseLong(oWaitlistMetrics.getTotalWaitingGuest()+"");
			jsonObject = JSONObject.fromObject(rootMap);

		} catch (Exception e) {
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
		
		//turn off 3rd level notifiction by shruti shah along with history and threshold changes
		//sendNotification(guestToBeSeated, oWaitlistMetrics, Constants.NOTIF_MARK_AS_SEATED);
		
		long guestIdToBeNotified = (long) oWaitlistMetrics.getGuestToBeNotified();
		
		if(oWaitlistMetrics.getGuestToBeNotified() != -1){
			if(guestToBeSeated.getGuestID() <= guestIdToBeNotified)
			{
				Guest guestToNotify = waitListService.getGuestById(guestIdToBeNotified);
				sendNotification(guestToNotify, oWaitlistMetrics, Constants.NOTIF_THRESHOLD_ENTERED);
			}
		}
		jsonObject = sendPusherMessage(rootMap, AppInitializer.pusherChannelEnv+"_"+rootMap.get("orgid"));

		return jsonObject.toString();
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

	private void sendNotification(Guest guestToNotify, WaitlistMetrics oWaitlistMetrics, String notificationFlag)
	{
		GuestNotificationBean guestNotificationBean = new GuestNotificationBean();
		guestNotificationBean.setEmail(guestToNotify.getEmail());
		guestNotificationBean.setGuestCount(Long.parseLong(oWaitlistMetrics.getTotalWaitingGuest()+""));
		guestNotificationBean.setGuestNoPrefix(getGuestNoPrefix(guestToNotify.getOrganizationID()));
		guestNotificationBean.setPrefType(guestToNotify.getPrefType());
		guestNotificationBean.setRank(guestToNotify.getRank());
		//log.info("LOG 1 ::::: $$$$ :::::: "+guestNotificationBean.getRank());
		guestNotificationBean.setSms(guestToNotify.getSms());
		guestNotificationBean.setTotalWaitTime(Long.parseLong(oWaitlistMetrics.getTotalWaitTime()+""));
		guestNotificationBean.setUuid(guestToNotify.getUuid());
		guestNotificationBean.setNotificationFlag(notificationFlag);
		guestNotificationBean.setGuestNotifiedWaitTime(Long.parseLong(oWaitlistMetrics.getGuestNotifiedWaitTime()+""));
		guestNotificationBean.setDeviceId(guestToNotify.getDeviceId());
		guestNotificationBean.setDeviceType(guestToNotify.getDeviceType());
		guestNotificationBean.setOrgId(guestToNotify.getOrganizationID());
		// TODO - Complete the JMS Configuration and uncomment the below section. - rohit
		//waitListService.sendNotificationToGuest(guestNotificationBean);
	}
	
	private Map<Integer, String> getGuestSeatingPrefMap() throws NumberFormatException, RsntException
	{
		List<Object[]> guestPrefLookupObjArr = null;
		guestPrefLookupObjArr = waitListService.getLookupsForLookupType(new Long(Constants.CONT_LOOKUPTYPE_GUEST_PREF));
		Map<Integer, String> guestPreferenceMap = new HashMap<Integer, String>();
		for(Object[] lookupObj : guestPrefLookupObjArr) {
			guestPreferenceMap.put((Integer)lookupObj[0], (String) lookupObj[1]);
		}
		return guestPreferenceMap;
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


}
