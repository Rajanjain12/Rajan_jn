/**
 * @author :ordextechnology
 * Provides rest services for Admin guest module 
 * services offered are: Basic CRUD on Guest Entity , Send notification and calculate guest 
 * wait time by organizationId
 * 	
 */

package com.rsnt.web.rest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import net.sf.json.JSONObject;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.log.Log;

import com.rsnt.common.entity.GuestNotificationBean;
import com.rsnt.common.exception.RsntException;
import com.rsnt.entity.Guest;
import com.rsnt.entity.GuestPreferences;
import com.rsnt.service.IWaitListService;
import com.rsnt.util.common.AppInitializer;
import com.rsnt.util.common.Constants;
import com.rsnt.web.rest.request.GuestDTO;
import com.rsnt.web.rest.request.GuestPreferencesDTO;
import com.rsnt.web.transferobject.WaitlistMetrics;
import com.rsnt.web.util.RealtimefameworkPusher;
@Path("/waitlistRestAction")
@Name("waitlistRestAction")
public class WaitListRestAction {

	//************************************************
	// instance variable declaration
	//*************************************************

	@Logger
	private Log log;
	@In(value=IWaitListService.SEAM_NAME, create=true)
	private IWaitListService waitListService;

	/**
	 * Create guest with provided guest details 
	 * @param guestStr JSON String
	 * @return String -Created Guest Object
	 */
	/*@POST
	@Path("/saveguest")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String saveOrUpdateGuest(String guestStr){
		log.info("Entering into saveOrUpdateGuest");
		final Map<String, Object> rootMap = new LinkedHashMap<String, Object>();
    	final List<String> errorArray = new ArrayList<String>(0);
    	List<GuestPreferences> guestPreferences  = new ArrayList<GuestPreferences>();
    	ObjectMapper objectMapper = new ObjectMapper();
    	GuestDTO guest = null;;
		try {
			guest = objectMapper.readValue(guestStr, GuestDTO.class);
			System.out.println("1");
		} catch (JsonParseException e1) {
			log.error("Error :: saveOrUpdateGuest ", e1.getMessage());
		} catch (JsonMappingException e1) {
			log.error("Error :: saveOrUpdateGuest " , e1.getMessage());
		} catch (IOException e1) {
			log.error("Error :: saveOrUpdateGuest ", e1.getMessage());
		}
		try {
			Guest guestObj = convertGuesVoToEntity(guest);
			Guest guestWithMaxRank =waitListService.getMaxGuestRankByOrgId(guestObj.getOrganizationID());
			Long maxRank = guestWithMaxRank.getRank();
			//Increment by one and set to resetcount
			guestObj.setRank(maxRank+1);
			//guestObj.setResetCount(count+1);
			//guestPreferences.addAll(guestObj.getGuestPreferences());
			//guestObj.getGuestPreferences().clear();
			guestObj = waitListService.saveorUpdateGuest(guestObj);
//			for (GuestPreferences pref : guestPreferences) {
//				pref.setGuest(guestObj);
//				waitListService.saveorUpdateGuestPreferences(pref);
//			}

			waitListService.updateTotalWaitTime(guestObj.getOrganizationID(), true);

        	rootMap.put(Constants.RSNT_ERROR, "");
        	rootMap.put(Constants.RSNT_GUEST,convertGuesEntityToVo(guestObj));
        	//rootMap.put(Constants.RSNT_GUEST_RANK_MIN, waitListService.getGuestRankMin(guestObj.getOrganizationID()));

        	final JSONObject jsonObject = JSONObject.fromObject(rootMap);
        	return jsonObject.toString();
		} catch (RsntException e) {
    		log.error("saveOrUpdateGuest() - failed:", e.getMessage());
    		rootMap.put("id", -1);
        	rootMap.put(Constants.RSNT_ERROR, "System Error - saveOrUpdateGuest failed");
        	rootMap.put("fieldErrors", errorArray);

        	final JSONObject jsonObject = JSONObject.fromObject(rootMap);
        	return jsonObject.toString();
		}catch (Exception e) {
    		log.error("saveOrUpdateGuest() - failed:", e.getMessage());
    		rootMap.put("id", -1);
        	rootMap.put(Constants.RSNT_ERROR, "System Error - saveOrUpdateGuest failed");
        	rootMap.put("fieldErrors", errorArray);

        	final JSONObject jsonObject = JSONObject.fromObject(rootMap);
        	return jsonObject.toString();
		}

	}*/
	/**
	 * Update the guest details
	 * @param guestStr
	 * @return String - updated guest object
	 *//*
	@POST
	@Path("/updateguest")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String updateGuest(String guestStr){
		log.info("Entering :: updateGuest");
		log.info(guestStr);
		final Map<String, Object> rootMap = new LinkedHashMap<String, Object>();
    	final List<String> errorArray = new ArrayList<String>(0);
    	ObjectMapper objectMapper = new ObjectMapper();
    	GuestDTO guest = null;;
		try {
			guest = objectMapper.readValue(guestStr, GuestDTO.class);
		} catch (JsonParseException e1) {
			log.error("Error :: updateGuest ", e1.getMessage());
		} catch (JsonMappingException e1) {
			log.error("Error :: updateGuest ", e1.getMessage());
		} catch (IOException e1) {
			log.error("Error :: updateGuest ", e1.getMessage());
		}
		try {			log.info("------"+guest.getGuestID());

			Guest entity = waitListService.getGuestById(guest.getGuestID());
			log.info("------"+entity);
			entity.setEmail(guest.getEmail());
			entity.setSms(guest.getSms());
			entity.setName(guest.getName());
			entity.setNote(guest.getNote());
			entity.setNoOfPeople(guest.getNoOfPeople());
			entity.setUpdatedTime(new Date());
			entity.setOptin(guest.isOptin());
			entity.setPrefType(guest.getPrefType());
			entity.getGuestPreferences().clear();
			entity.getGuestPreferences().addAll(convertSeatingPreferencesVotoEntity(guest.getGuestPreferences(),entity));
			entity = waitListService.saveorUpdateGuest(entity);
			nonCloseParent(entity.getGuestPreferences());
        	rootMap.put(Constants.RSNT_ERROR, "");
        	rootMap.put(Constants.RSNT_GUEST,entity);

        	final JSONObject jsonObject = JSONObject.fromObject(rootMap);
        	return jsonObject.toString();
		} catch (RsntException e) {
			e.printStackTrace();
			log.error("Error :: updateGuest ", e.getMessage());    		
    		rootMap.put("id", -1);
        	rootMap.put(Constants.RSNT_ERROR, "System Error - saveOrUpdateGuest failed");
        	rootMap.put("fieldErrors", errorArray);

        	final JSONObject jsonObject = JSONObject.fromObject(rootMap);
        	return jsonObject.toString();
		}

	}*/
	/**
	 * Update the selected guest status as Seated
	 * @param guestId--Provided guestId
	 * @return String --Updated Guest details
	 *//*
	@GET
	@Path("/seated")
	@Produces(MediaType.APPLICATION_JSON)
	public String markGuestAsSeated(@QueryParam("guestid") long guestId){
		log.info("Entering :: markGuestAsSeated");
		final Map<String, Object> rootMap = new LinkedHashMap<String, Object>();
    	final List<String> errorArray = new ArrayList<String>(0);
    	Guest guestEntity =  null;
		try {
			guestEntity =  waitListService.getGuestById(guestId);
			guestEntity.setSeatedTime(new Date());
			guestEntity.setStatus(Constants.RSNT_GUEST_SEATED);
			guestEntity = waitListService.saveorUpdateGuest(guestEntity);
			waitListService.readyNotifyGuestByPref(guestId);
			//rootMap.put(Constants.RSNT_GUEST_RANK_MIN, waitListService.getGuestRankMin(guestEntity.getOrganizationID()));

			//Trigger the Notification of all following guest which are bumbed up in queue
			if(checkMarkerForStatusChange(guestEntity))
				waitListService.notifyUsersOnStatusChange(guestId, guestEntity.getOrganizationID());
			nonCloseParent(guestEntity.getGuestPreferences());
        	rootMap.put(Constants.RSNT_ERROR, "");
        	rootMap.put(Constants.RSNT_GUEST,guestEntity);
        	//

        	final JSONObject jsonObject = JSONObject.fromObject(rootMap);
        	return jsonObject.toString();
		} catch (RsntException e) {
			e.printStackTrace();
    		log.error("markGuestAsSeated() - failed:", e.getMessage());
    		rootMap.put("id", -1);
        	rootMap.put(Constants.RSNT_ERROR, "System Error - saveOrUpdateGuest failed");
        	rootMap.put("fieldErrors", errorArray);

        	final JSONObject jsonObject = JSONObject.fromObject(rootMap);
        	return jsonObject.toString();
		}

	}*/
	/**
	 * Increment the Not present out
	 * @param guestId--Provided guestId
	 * @return String --Updated Guest details
	 */
	/*@GET
	@Path("/callout")
	@Produces(MediaType.APPLICATION_JSON)
	public String incrementCalloutCount(@QueryParam("guestid") long guestId){
		log.info("Entering :: incrementCalloutCount");
		final Map<String, Object> rootMap = new LinkedHashMap<String, Object>();
    	final List<String> errorArray = new ArrayList<String>(0);
    	Guest guestEntity =  null;
		try {
			guestEntity =  waitListService.getGuestById(guestId);
			Guest guestEntityReplica = new Guest();
			guestEntityReplica.setIncompleteParty(guestEntity.getIncompleteParty()) ;
			guestEntityReplica.setCalloutCount(guestEntity.getCalloutCount());
			long calloutVal = guestEntity.getCalloutCount()!=null ? guestEntity.getCalloutCount()+1 :1;
			guestEntity.setCalloutCount(calloutVal);
			guestEntity = waitListService.saveorUpdateGuest(guestEntity);
			if(checkMarkerForStatusChange(guestEntityReplica))
				waitListService.notifyUsersOnStatusChange(guestId, guestEntity.getOrganizationID());
        	rootMap.put(Constants.RSNT_ERROR, "");
        	rootMap.put(Constants.RSNT_GUEST,guestEntity);
        	nonCloseParent(guestEntity.getGuestPreferences());
        	final JSONObject jsonObject = JSONObject.fromObject(rootMap);
        	return jsonObject.toString();
		} catch (RsntException e) {
			e.printStackTrace();
    		log.error("incrementCalloutCount() - failed:", e.getMessage());
    		rootMap.put("id", -1);
        	rootMap.put(Constants.RSNT_ERROR, "System Error - incrementCalloutCount failed");
        	rootMap.put("fieldErrors", errorArray);

        	final JSONObject jsonObject = JSONObject.fromObject(rootMap);
        	return jsonObject.toString();
		}catch (Exception e) {
			e.printStackTrace();
			rootMap.put("id", -1);
        	rootMap.put(Constants.RSNT_ERROR, "System Error - incrementCalloutCount failed");
        	rootMap.put("fieldErrors", errorArray);
        	final JSONObject jsonObject = JSONObject.fromObject(rootMap);
        	return jsonObject.toString();
		}

	}*/

	/**
	 * Send ready notification email or Sms to
	 * guest
	 * @param guestId
	 * @return String
	 *//*
	@GET
	@Path("/markasincomplete")
	public String markAsIncompleteParty(@QueryParam("guestid") Long guestId){
		log.info("Entering :: markAsIncompleteParty");
		final Map<String, Object> rootMap = new LinkedHashMap<String, Object>();
    	final List<String> errorArray = new ArrayList<String>(0);
    	Guest guestEntity =  null;
		try {
			guestEntity =  waitListService.getGuestById(guestId);
			Guest guestEntityReplica = new Guest();
			guestEntityReplica.setIncompleteParty(guestEntity.getIncompleteParty()) ;
			guestEntityReplica.setCalloutCount(guestEntity.getCalloutCount());
			//long calloutVal = guestEntity.getCalloutCount()!=null ? guestEntity.getCalloutCount()+1 :1;
			guestEntity.setIncompleteParty(new Long(1));
			guestEntity = waitListService.saveorUpdateGuest(guestEntity);
			if(checkMarkerForStatusChange(guestEntityReplica)){
				waitListService.notifyUsersOnStatusChange(guestId, guestEntity.getOrganizationID());
			}
        	rootMap.put(Constants.RSNT_ERROR, "");
        	rootMap.put(Constants.RSNT_GUEST,guestEntity);
        	nonCloseParent(guestEntity.getGuestPreferences());
        	final JSONObject jsonObject = JSONObject.fromObject(rootMap);
        	return jsonObject.toString();
		} catch (RsntException e) {
			e.printStackTrace();
    		log.error("markAsIncompleteParty() - failed:", e.getMessage());
    		rootMap.put("id", -1);
        	rootMap.put(Constants.RSNT_ERROR, "System Error - markAsIncompleteParty failed");
        	rootMap.put("fieldErrors", errorArray);

        	final JSONObject jsonObject = JSONObject.fromObject(rootMap);
        	return jsonObject.toString();
		}catch (Exception e) {
			e.printStackTrace();
			rootMap.put("id", -1);
        	rootMap.put(Constants.RSNT_ERROR, "System Error - markAsIncompleteParty failed");
        	rootMap.put("fieldErrors", errorArray);
        	final JSONObject jsonObject = JSONObject.fromObject(rootMap);
        	return jsonObject.toString();
		}
	}*/
	/**
	 * Send ready notification email or Sms to
	 * guest
	 * @param guestId
	 * @return String
	 *//*
	@GET
	@Path("/readynotify")
	public String readyNotification(@QueryParam("guestid") Long guestId){
		waitListService.readyNotifyGuestByPref(guestId);
		return Constants.RSNT_GUEST_SUCCESS;
	}*/
	/**
	 * Send Notification to guests by user preferences
	 * @param guestId
	 * @return String
	 */
	@GET
	@Path("/checkinnotify")
	public String checkinNotification(@QueryParam("guestid") Long guestId){
		waitListService.notifyGuestByPref(guestId);
		return Constants.RSNT_GUEST_SUCCESS;
	}

	/**
	 * Send Notification to guests by user preferences
	 * @param guestId
	 * @return String
	 */
	@GET
	@Path("/waitingListBackupUpdate")
	public String waitingListBackupUpdate(@QueryParam("guestid") Long guestId){
		waitListService.sendWaitListBackupEmail(guestId, true);
		return Constants.RSNT_GUEST_SUCCESS;
	}

	/**
	 * Send Notification to guests by user preferences
	 * @param guestId
	 * @return String
	 */
	@GET
	@Path("/waitingListBackup")
	public String waitingListBackup(@QueryParam("guestid") Long guestId){
		waitListService.sendWaitListBackupEmail(guestId, false);
		return Constants.RSNT_GUEST_SUCCESS;
	}

	@GET
	@Path("/getguestminrankbyorgid")
	public String getGuestMinrankByorgid(@QueryParam("orgid") Long orgid){
		//log.info("-----------------------"+waitListService.getGuestRankMin(orgid));
		return waitListService.getGuestRankMin(orgid);
	}

	/**
	 * Mark Guest as Deleted status by Admin
	 * @param guestId
	 * @return Deleted Guestdetails
	 */
	@GET
	@Path("/deleted")
	@Produces(MediaType.APPLICATION_JSON)
	public String markGuestAsDeleted(@QueryParam("guestid") long guestId){
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
			log.error("markGuestAsSeated() - failed:", e.getMessage());
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
	@GET
	@Path("/notify")
	public String notifyGuests(@QueryParam("numberofusers")  int numberOfUsers,@QueryParam("orgid") Long orgid){
		waitListService.notifyUsers(numberOfUsers,orgid);
		return Constants.RSNT_GUEST_SUCCESS;
	}
	/** 
	 * Change per party wait Time by organizationId
	 * @param time
	 * @param orgid
	 * @return String
	 */
	@GET
	@Path("/perpartytime")
	public String changePerPartyTime(@QueryParam("time") int time,@QueryParam("orgid") Long orgid){
		int res= waitListService.updateOrganizationWaitTime(time,orgid);
		if(res==1){
			return getTotalWaitTime(orgid);
			//return Constants.RSNT_GUEST_SUCCESS;
		}
		return Constants.RSNT_GUEST_FAIL;
	}
	/**
	 * Get Total Wait Time by organizationiD
	 * @param orgid
	 * @return String-Total wait time metricks
	 */
	@GET
	@Path("/totalwaittimemetricks")
	public String getTotalWaitTime(@QueryParam("orgid") Long orgid){
		Map<String, String> metrciks = waitListService.getTotalPartyWaitTimeMetrics(orgid);
		metrciks.put("imageOrgPath", AppInitializer.orgLogoPathHtml+"/"+orgid+".png");
		final JSONObject jsonObject = JSONObject.fromObject(metrciks);
		return jsonObject.toString();
	}
	/**
	 * Guest Guest Metrics like wait time , number of users a head
	 * @param guestId
	 * @param orgid
	 * @return String - Guest Metrics details
	 */
	@GET
	@Path("/usermetricks")
	public String getGuestWaitTimeMetricks(@QueryParam("guest") Long guestId,@QueryParam("orgid") Long orgid){
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
	@GET
	@Path("/reset")
	public String resetGuests(@QueryParam("orgid") Long orgid){
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
	@GET
	@Path("/checkinusers")
	@Produces(MediaType.APPLICATION_JSON)
	public String fetchCheckinUsers(@QueryParam("orgid") Long orgid, 
			@QueryParam("recordsPerPage") int recordsPerPage, @QueryParam("pageNumber") int pageNumber){
		log.info("Entering :: fetchCheckinUsers --OrgId::"+orgid);
		List<GuestDTO> guestDTOs = null;
		final Map<String, Object> rootMap = new LinkedHashMap<String, Object>();
		final List<String> errorArray = new ArrayList<String>(0);
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

			}

		} catch (RsntException e) {
			e.printStackTrace();
			log.error("fetchCheckinUsers() - failed:", e.getMessage());
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
	 * Fetch Guest Details By Id
	 * @param guestid
	 * @return {@link GuestDTO}
	 */
	@GET
	@Path("/guest")
	@Produces(MediaType.APPLICATION_JSON)
	public String fetchGuestById(@QueryParam("guestid") int guestid){
		log.info("Entering :: fetchGuestById :: guestId"+guestid);
		final Map<String, Object> rootMap = new LinkedHashMap<String, Object>();
		final List<String> errorArray = new ArrayList<String>(0);
		Guest guestObject= null;
		try {
			guestObject = waitListService.getGuestById(guestid);
			//nonCloseParent(guestObject.getGuestPreferences());
		} catch (Exception e) {
			e.printStackTrace();
			log.error("fetchCheckinUsers() - failed:", e.getMessage());
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
	@GET
	@Path("/guestuuid")
	@Produces(MediaType.APPLICATION_JSON)
	public String fetchGuestByUUID(@QueryParam("uuid") String uuid){
		log.info("Entering fetchGuestByUUID ::UUID::"+uuid);
		final Map<String, Object> rootMap = new LinkedHashMap<String, Object>();
		final List<String> errorArray = new ArrayList<String>(0);
		Guest guestObject= null;
		try {
			guestObject = waitListService.getGuestByUUID(uuid);
			//nonCloseParent(guestObject.getGuestPreferences());
		} catch (Exception e) {
			e.printStackTrace();
			log.error("fetchGuestByUUID() - failed:", e.getMessage());
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
	@GET
	@Path("/history")
	@Produces(MediaType.APPLICATION_JSON)
	//changed for history pagination
	//public String fetchGuestsHistory(@QueryParam("orgid") Long orgid){
	public String fetchGuestsHistory(@QueryParam("orgid") Long orgid	, 
			@QueryParam("recordsPerPage") int recordsPerPage, @QueryParam("pageNumber") int pageNumber) {
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
			log.error("fetchGuestsHistory() - failed:", e.getMessage());
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
	@GET
	@Path("/pusgerinformation")
	public String getPushEventsConfigDetails(){
		Map<String, String> eventConfig = new HashMap<String, String>();
		eventConfig.put(Constants.REALTIME_APPLICATION_KEY, System.getProperty(Constants.REALTIME_APPLICATION_KEY_VAL));
		eventConfig.put(Constants.REALTIME_PRIVATE_KEY, System.getProperty(Constants.REALTIME_PRIVATE_KEY_VAL));
		eventConfig.put(Constants.PUSHER_CHANNEL_ENV, AppInitializer.pusherChannelEnv+"_"+Contexts.getSessionContext().get(Constants.CONST_ORGID));
		eventConfig.put(Constants.QRCODE_VALUE, "123_"+AppInitializer.pusherChannelEnv+"_"+Contexts.getSessionContext().get(Constants.CONST_ORGID));
		eventConfig.put(Constants.FOOTER_MSG, AppInitializer.staticFooterMsg);
		final JSONObject jsonObject = JSONObject.fromObject(eventConfig);
		return jsonObject.toString();
	}

	/**
	 * Get Event Listener Configuration details 
	 * @return Map<String, String> 
	 */
	@GET
	@Path("/orgseatpref")
	@Produces(MediaType.APPLICATION_JSON)
	public String getOrganizationSeatingPreferences(@QueryParam("orgid") Long orgid){
		final Map<String, Object> rootMap = new LinkedHashMap<String, Object>();
		final List<String> errorArray = new ArrayList<String>(0);
		List<GuestPreferencesDTO> searPref =  null;
		try {
			searPref=	waitListService.getOrganizationSeatingPref(orgid);
			rootMap.put(Constants.RSNT_ERROR, "");
			rootMap.put("seatpref",searPref);
			final JSONObject jsonObject = JSONObject.fromObject(rootMap);
			return jsonObject.toString();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("getOrganizationSeatingPreferences() - failed:", e.getMessage());
			rootMap.put("id", -1);
			rootMap.put(Constants.RSNT_ERROR, "System Error - getOrganizationSeatingPreferences failed");
			rootMap.put("fieldErrors", errorArray);
			final JSONObject jsonObject = JSONObject.fromObject(rootMap);
			return jsonObject.toString();
		}


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
	@POST
	@Path("/addGuest")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String addGuest (String guestJSONStr) {
		log.info("Entering into addGuest");
		log.info("guestJSONStr-->"+guestJSONStr);
		ObjectMapper objectMapper = new ObjectMapper();
		GuestDTO guestDTO = null;
		Guest guest = null;
		JSONObject jsonObject = null;
		final Map<String, Object> rootMap = new LinkedHashMap<String, Object>();
		WaitlistMetrics oWaitlistMetrics = null;
		try {
			guestDTO = objectMapper.readValue(guestJSONStr, GuestDTO.class);
			guest = convertGuesVoToEntity(guestDTO);
			//Added this for demo of QRcode
			/*guest.setPrefType("PUSH");
			guest.setDeviceType("AN");
			guest.setDeviceId("APA91bGPrj8J9PYsiV6sGtDu7w1vFpGGf4hHB2a1-P0-DaClvmSXJmNZW2Ajli1VFplwmE8va9u6fU3eRh33_gTZ7WzLik3-Sb5Yu9J8H2zt-qNHsBdKOL8");*/
			oWaitlistMetrics = waitListService.addGuest(guest);

			rootMap.put(Constants.RSNT_NOW_SERVING_GUEST_ID, oWaitlistMetrics.getNowServingParty());
			rootMap.put(Constants.RSNT_ORG_TOTAL_WAIT_TIME, oWaitlistMetrics.getTotalWaitTime());
			rootMap.put(Constants.RSNT_NEXT_TO_NOTIFY_GUEST_ID, oWaitlistMetrics.getGuestToBeNotified());
			
			guest.setRank(Long.parseLong(oWaitlistMetrics.getGuestRank()+""));

			/*required params for the mobile app
			Total Wait Time
			Total Parties Waiting
			Now Serving Party
			Organization ID
			Guest ID of added Guest
			Guest Rank of added Guest
			Guest UUID of added Guest
			*/
			
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
			jsonObject = sendPusherMessage(rootMap, AppInitializer.pusherChannelEnv+"_"+rootMap.get("orgid"));


		} catch (JsonParseException e) {
			log.error("Error :: saveOrUpdateGuest ", e.getMessage());
		} catch (JsonMappingException e) {
			log.error("Error :: saveOrUpdateGuest ", e.getMessage());
		} catch (IOException e) {
			log.error("Error :: saveOrUpdateGuest ", e.getMessage());
		}
		catch (Exception e) {
			e.printStackTrace();
		}finally{
			rootMap.put("id", -1);
			rootMap.put(Constants.RSNT_ERROR, "System Error - addGuest failed");
			jsonObject = JSONObject.fromObject(rootMap);
		}
		

		return jsonObject.toString();
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
	@POST
	@Path("/updateGuestInfo")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String updateGuestInfo (String guestJSONStr) {
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
			log.error("Error :: saveOrUpdateGuest ", e.getMessage());
		} catch (JsonMappingException e) {
			log.error("Error :: saveOrUpdateGuest ", e.getMessage());
		} catch (IOException e) {
			log.error("Error :: saveOrUpdateGuest ", e.getMessage());
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
	@GET
	@Path("/deleteGuest")
	public String deleteGuest (@QueryParam("guestId") String guestId, @QueryParam("orgId")  int orgId) {
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
	@GET
	@Path("/changeNotificationThreshold")
	public String changeNotificationThreshold(@QueryParam("numberofusers")  int numberOfUsers,@QueryParam("perPartyWaitTime") int perPartyWaitTime, @QueryParam("orgid") Long orgid){
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
	@GET
	@Path("/changePerPartyWaitTime")
	public String changePerPartyWaitTime(@QueryParam("numberofusers")  int numberOfUsers, @QueryParam("perPartyWaitTime") int perPartyWaitTime,@QueryParam("orgid") Long orgid){
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
	@POST
	@Path("/incrementCalloutCount")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String incrementCalloutCount (String guestJSONStr) {
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
	@POST
	@Path("/markAsIncomplete")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String markAsIncomplete (String guestJSONStr) {
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
	@GET
	@Path("/markAsSeated")
	public String markAsSeated (@QueryParam("guestId")  int guestId, @QueryParam("orgId")  int orgId) {
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
	@GET
	@Path("/scanqrcode")
	public String scanQRCode (@QueryParam("qrcodevalue")  String qrCodeValue) {
		log.info("Entering into scanQRCode");
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
	
		return jsonObjectMobile.toString();
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
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
		
		waitListService.sendNotificationToGuest(guestNotificationBean);
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
