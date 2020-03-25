package com.kyobeeWaitlistService.rest;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


import com.kyobeeWaitlistService.dto.AddUpdateGuestDTO;
import com.kyobeeWaitlistService.dto.GuestDTO;
import com.kyobeeWaitlistService.dto.GuestMarketingPreferenceDTO;
import com.kyobeeWaitlistService.dto.GuestMetricsDTO;
import com.kyobeeWaitlistService.dto.GuestResponseDTO;
import com.kyobeeWaitlistService.dto.GuestWebDTO;
import com.kyobeeWaitlistService.dto.ResponseDTO;
import com.kyobeeWaitlistService.service.GuestService;
import com.kyobeeWaitlistService.util.LoggerUtil;
import com.kyobeeWaitlistService.util.WaitListServiceConstants;
import com.kyobeeWaitlistService.util.Exeception.InvalidGuestException;

@RestController
@CrossOrigin
@RequestMapping("/rest/waitlist/guest")
public class GuestController {

	@Autowired
	private GuestService guestService;

	// for fetching matrics data related guest
	@GetMapping(value = "/metrics", produces = "application/vnd.kyobee.v1+json")
	public @ResponseBody ResponseDTO metrics(@RequestParam("guestId") Integer guestId,
			@RequestParam("orgId") Integer orgId) {

		ResponseDTO responseDTO = new ResponseDTO();
		try {
			GuestMetricsDTO metricsDTO = guestService.getGuestMetrics(guestId, orgId);
			responseDTO.setServiceResult(metricsDTO);
			responseDTO.setMessage("Guest metrics fetched successfully");
			responseDTO.setSuccess(WaitListServiceConstants.SUCCESS_CODE);

		} catch (Exception ex) {
			LoggerUtil.logError(ex);
			responseDTO.setServiceResult("Error while fetching guest metrics");
			responseDTO.setMessage("Error while fetching guest metrics");
			responseDTO.setSuccess(WaitListServiceConstants.ERROR_CODE);
		}
		return responseDTO;
	}
    // For reseting guest list of particular organization
	@GetMapping(value = "/resetGuestList", produces = "application/vnd.kyobee.v1+json")
	public @ResponseBody ResponseDTO resetGuests(@RequestParam(value = "orgid") Long orgID) {

		ResponseDTO responseDTO = new ResponseDTO();
		try {
			guestService.resetOrganizationsByOrgId(orgID);
			responseDTO.setServiceResult("Reset Successfully");
			responseDTO.setMessage("Reset Successfully");
			responseDTO.setSuccess(WaitListServiceConstants.SUCCESS_CODE);

		} catch (Exception ex) {
			LoggerUtil.logError(ex);
			responseDTO.setServiceResult("System Error - resetOrganization failed");
			responseDTO.setMessage("System Error - resetOrganization failed");
			responseDTO.setSuccess(WaitListServiceConstants.ERROR_CODE);
		}
		return responseDTO;
	}

	// for fetching guest list , search text will be null for fetching whole data
	@GetMapping(value = "/", consumes = "application/json", produces = "application/vnd.kyobee.v1+json")
	public @ResponseBody ResponseDTO fetchGuestList(@RequestParam Integer orgId, @RequestParam Integer pageSize,
			@RequestParam Integer pageNo, @RequestParam String searchText) {

		ResponseDTO responseDTO = new ResponseDTO();
		try {
			GuestResponseDTO guestList = guestService.fetchGuestList(orgId, pageSize, pageNo, searchText);
			responseDTO.setServiceResult(guestList);
			responseDTO.setMessage("guest list fetched Successfully.");
			responseDTO.setSuccess(WaitListServiceConstants.SUCCESS_CODE);

		} catch (Exception ex) {
			LoggerUtil.logError(ex);
			responseDTO.setServiceResult("System Error - fetchGuestList failed");
			responseDTO.setMessage("System Error - fetchGuestList failed");
			responseDTO.setSuccess(WaitListServiceConstants.ERROR_CODE);
		}
		return responseDTO;
	}

	// for fetching guest list , search text will be null for fetching whole data
	@GetMapping(value = "/fetchGuestHistory", consumes = "application/json", produces = "application/vnd.kyobee.v1+json")
	public @ResponseBody ResponseDTO fetchGuestHistoryList(@RequestParam Integer orgId, @RequestParam Integer pageSize,
			@RequestParam Integer pageNo, @RequestParam String searchText,
			@RequestParam("clientTimezone") String clientTimezone, @RequestParam Integer sliderMaxTime,
			@RequestParam Integer sliderMinTime, @RequestParam String statusOption) {
		ResponseDTO responseDTO = new ResponseDTO(); 
		try {
			LoggerUtil.logInfo(clientTimezone);
			GuestResponseDTO guestList = guestService.fetchGuestHistoryList(orgId, pageSize, pageNo, searchText,
					clientTimezone, sliderMaxTime, sliderMinTime, statusOption);
			responseDTO.setServiceResult(guestList);
			responseDTO.setMessage("guest history list fetched Successfully.");
			responseDTO.setSuccess(WaitListServiceConstants.SUCCESS_CODE);

		} catch (Exception ex) {
			LoggerUtil.logError(ex);
			responseDTO.setServiceResult("System Error - fetchGuestHistoryList failed");
			responseDTO.setMessage("System Error - fetchGuestHistoryList failed");
			responseDTO.setSuccess(WaitListServiceConstants.ERROR_CODE);
		}
		return responseDTO;
	}

	// for add guest according to guestId
	@PostMapping(value = "/", consumes = "application/json", produces = "application/vnd.kyobee.v1+json")
	public @ResponseBody ResponseDTO addGuestDetails(@RequestBody GuestDTO guestDTO) {

		ResponseDTO responseDTO = new ResponseDTO();
		try {
			AddUpdateGuestDTO addUpdateGuestDTO = guestService.addGuest(guestDTO);
			
			responseDTO.setServiceResult(addUpdateGuestDTO);
			responseDTO.setMessage("guest added Successfully.");
			responseDTO.setSuccess(WaitListServiceConstants.SUCCESS_CODE);

		} catch (Exception ex) {
			LoggerUtil.logError("add " + Arrays.toString(ex.getStackTrace()));
			responseDTO.setServiceResult("System Error - addGuestDetails failed");
			responseDTO.setMessage("System Error - addGuestDetails failed");
			responseDTO.setSuccess(WaitListServiceConstants.ERROR_CODE);
		}
		return responseDTO;
	}

	// for updating guest details
	@PutMapping(value = "/", consumes = "application/json", produces = "application/vnd.kyobee.v1+json")
	public @ResponseBody ResponseDTO updateGuestDetails(@RequestBody GuestDTO guestDTO) {

		ResponseDTO responseDTO = new ResponseDTO();
		try {
			AddUpdateGuestDTO addUpdateGuestDTO = guestService.updateGuestDetails(guestDTO);
			responseDTO.setServiceResult(addUpdateGuestDTO);
			responseDTO.setMessage("guest details updated Successfully.");
			responseDTO.setSuccess(WaitListServiceConstants.SUCCESS_CODE);

		} catch (Exception ex) {
			LoggerUtil.logError("update detail method ERROR " + Arrays.toString(ex.getStackTrace()));
			responseDTO.setServiceResult("System Error - updateGuestDetails failed");
			responseDTO.setMessage("System Error - updateGuestDetails failed");
			responseDTO.setSuccess(WaitListServiceConstants.ERROR_CODE);
		}
		return responseDTO;
	}

	// for updating guest status
	@PutMapping(value = "/status", consumes = "application/json", produces = "application/vnd.kyobee.v1+json")
	public @ResponseBody ResponseDTO updateGuestStatus(@RequestParam Integer guestId, @RequestParam Integer orgId,
			@RequestParam String status) {

		ResponseDTO responseDTO = new ResponseDTO();
		try {
			guestService.updateGuestStatus(guestId, orgId, status);
			responseDTO.setServiceResult("Guest status updated successfully");
			responseDTO.setMessage("Guest status updated successfully");
			responseDTO.setSuccess(WaitListServiceConstants.SUCCESS_CODE);

		} catch (Exception ex) {
			LoggerUtil.logError("update status method ERROR " + Arrays.toString(ex.getStackTrace()));
			responseDTO.setServiceResult("System Error - updateGuestStatus failed");
			responseDTO.setMessage("System Error - updateGuestStatus failed");
			responseDTO.setSuccess(WaitListServiceConstants.ERROR_CODE);
		}
		return responseDTO;
	}

	// for fetching guest details by guest id
	@GetMapping(value = "/{id}", produces = "application/vnd.kyobee.v1+json")
	public @ResponseBody ResponseDTO fetchGuestById(@PathVariable("id") Integer guestId) {

		ResponseDTO responseDTO = new ResponseDTO();
		try {
			GuestDTO guest = guestService.fetchGuestDetails(guestId, null);
			responseDTO.setServiceResult(guest);
			responseDTO.setMessage("guest details fetched Successfully.");
			responseDTO.setSuccess(WaitListServiceConstants.SUCCESS_CODE);

		} catch (Exception ex) {
			LoggerUtil.logError(ex);
			responseDTO.setServiceResult("Error while fetching guest details");
			responseDTO.setMessage("Error while fetching guest details");
			responseDTO.setSuccess(WaitListServiceConstants.ERROR_CODE);
		}
		return responseDTO;
	}

	// for fetching guest details by guest uuid
	@GetMapping(value = "/uuid/{uuid}", produces = "application/vnd.kyobee.v1+json")
	public @ResponseBody ResponseDTO fetchGuestById(@PathVariable("uuid") String guestUUID) {

		ResponseDTO responseDTO = new ResponseDTO();
		try {
			GuestDTO guest = guestService.fetchGuestDetails(null, guestUUID);
			GuestWebDTO guestDTO=guestService.addLanguageKeyMap(guest);		
			responseDTO.setServiceResult(guestDTO);
			responseDTO.setMessage("guest details fetched Successfully.");
			responseDTO.setSuccess(WaitListServiceConstants.SUCCESS_CODE);

		}
		catch (InvalidGuestException e) {
			LoggerUtil.logError(e);
			responseDTO.setServiceResult(e.getMessage());
			responseDTO.setMessage(e.getMessage());
			responseDTO.setSuccess(WaitListServiceConstants.ERROR_CODE);
		}
		catch (Exception ex) {
			LoggerUtil.logError(ex);
			responseDTO.setServiceResult("Error while fetching guest details");
			responseDTO.setMessage("Error while fetching guest details");
			responseDTO.setSuccess(WaitListServiceConstants.ERROR_CODE);
		}
		return responseDTO;
	}

	// for fetching guest details by contact no and organization id
	@GetMapping(value = "/guestDetails", produces = "application/vnd.kyobee.v1+json")
	public @ResponseBody ResponseDTO fetchGuestByContact(@RequestParam("orgID") Integer orgID,
			@RequestParam("contactNo") String contactNo) {

		ResponseDTO responseDTO = new ResponseDTO();
		try {
			GuestDTO guest = guestService.fetchGuestByContact(orgID, contactNo);
			responseDTO.setServiceResult(guest);
			responseDTO.setMessage("guest details fetched Successfully.");
			responseDTO.setSuccess(WaitListServiceConstants.SUCCESS_CODE);

		} catch (Exception ex) {
			LoggerUtil.logError(ex);
			responseDTO.setServiceResult("Error while fetching guest details");
			responseDTO.setMessage("Error while fetching guest details");
			responseDTO.setSuccess(WaitListServiceConstants.ERROR_CODE);
		}
		return responseDTO;
	}

	// for adding marketing pref from mobile side
	@PostMapping(value = "/marketingPref", produces = "application/vnd.kyobee.v1+json")
	public @ResponseBody ResponseDTO addMarketingPref(@RequestBody GuestMarketingPreferenceDTO marketingPrefDTO) {

		ResponseDTO responseDTO = new ResponseDTO();
		try {
			guestService.addMarketingPref(marketingPrefDTO);
			responseDTO.setServiceResult("Marketing Preference added successfully");
			responseDTO.setMessage("Marketing Preference added successfully.");
			responseDTO.setSuccess(WaitListServiceConstants.SUCCESS_CODE);

		} catch (Exception ex) {
			LoggerUtil.logError(ex);
			responseDTO.setServiceResult("Error while adding marketing preference");
			responseDTO.setMessage("Error while adding marketing preference");
			responseDTO.setSuccess(WaitListServiceConstants.ERROR_CODE);
		}
		return responseDTO;
	}

}
