package com.kyobeeWaitlistService.rest;

import java.util.Arrays;

import org.hibernate.mapping.Array;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.kyobeeWaitlistService.dto.AddUpdateGuestDTO;
import com.kyobeeWaitlistService.dto.GuestDTO;
import com.kyobeeWaitlistService.dto.GuestHistoryRequestDTO;
import com.kyobeeWaitlistService.dto.GuestMetricsDTO;
import com.kyobeeWaitlistService.dto.GuestRequestDTO;
import com.kyobeeWaitlistService.dto.GuestResponseDTO;
import com.kyobeeWaitlistService.dto.ResponseDTO;
import com.kyobeeWaitlistService.service.GuestService;
import com.kyobeeWaitlistService.util.LoggerUtil;
import com.kyobeeWaitlistService.util.WaitListServiceConstants;

@RestController
@CrossOrigin
@RequestMapping("/rest/guest")
public class GuestController {

	@Autowired
	GuestService guestService;

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
	public @ResponseBody ResponseDTO fetchGuestList(@RequestBody GuestRequestDTO guestRequest) {

		ResponseDTO responseDTO = new ResponseDTO();
		try {
			GuestResponseDTO guestList = guestService.fetchGuestList(guestRequest.getOrgId(),
					guestRequest.getPageSize(), guestRequest.getPageNo(), guestRequest.getSearchText());
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
	public @ResponseBody ResponseDTO fetchGuestHistoryList(@RequestBody GuestHistoryRequestDTO guestRequest) {
		ResponseDTO responseDTO = new ResponseDTO();
		try {
			GuestResponseDTO guestList = guestService.fetchGuestHistoryList(guestRequest);
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

	// for add or update of guest according to guestId
	@PostMapping(value = "/", consumes = "application/json", produces = "application/vnd.kyobee.v1+json")
	public @ResponseBody ResponseDTO addGuestDetails(@RequestBody GuestDTO guestDTO) {

		ResponseDTO responseDTO = new ResponseDTO();
		try {
			AddUpdateGuestDTO addUpdateGuestDTO = guestService.addGuest(guestDTO);
			responseDTO.setServiceResult(addUpdateGuestDTO);
			responseDTO.setMessage("guest added Successfully.");
			responseDTO.setSuccess(WaitListServiceConstants.SUCCESS_CODE);

		} catch (Exception ex) {
			LoggerUtil.logError("add "+ Arrays.toString(ex.getStackTrace()));
			responseDTO.setServiceResult("System Error - addGuestDetails failed");
			responseDTO.setMessage("System Error - addGuestDetails failed");
			responseDTO.setSuccess(WaitListServiceConstants.ERROR_CODE);
		}
		return responseDTO;
	}
	
	@PutMapping(value = "/", consumes = "application/json", produces = "application/vnd.kyobee.v1+json")
	public @ResponseBody ResponseDTO updateGuestDetails(@RequestBody GuestDTO guestDTO) {

		ResponseDTO responseDTO = new ResponseDTO();
		try {
			AddUpdateGuestDTO addUpdateGuestDTO = guestService.updateGuestDetails(guestDTO);
			responseDTO.setServiceResult(addUpdateGuestDTO);
			responseDTO.setMessage("guest details updated Successfully.");
			responseDTO.setSuccess(WaitListServiceConstants.SUCCESS_CODE);

		} catch (Exception ex) {
			LoggerUtil.logError("update detail method ERROR "+Arrays.toString(ex.getStackTrace()));
			responseDTO.setServiceResult("System Error - updateGuestDetails failed");
			responseDTO.setMessage("System Error - updateGuestDetails failed");
			responseDTO.setSuccess(WaitListServiceConstants.ERROR_CODE);
		}
		return responseDTO;
	}
	
	@PutMapping(value = "/status", consumes = "application/json", produces = "application/vnd.kyobee.v1+json")
	public @ResponseBody ResponseDTO updateGuestStatus(@RequestParam Integer guestId,@RequestParam Integer orgId,@RequestParam String status) {

		ResponseDTO responseDTO = new ResponseDTO();
		try {
			guestService.updateGuestStatus(guestId, orgId, status);
			responseDTO.setServiceResult("Status updated");
			responseDTO.setMessage("Guest status updated successfully");
			responseDTO.setSuccess(WaitListServiceConstants.SUCCESS_CODE);

		} catch (Exception ex) {
			LoggerUtil.logError("update status method ERROR "+Arrays.toString(ex.getStackTrace()));
			responseDTO.setServiceResult("System Error - updateGuestStatus failed");
			responseDTO.setMessage("System Error - updateGuestStatus failed");
			responseDTO.setSuccess(WaitListServiceConstants.ERROR_CODE);
		}
		return responseDTO;
	}
}
