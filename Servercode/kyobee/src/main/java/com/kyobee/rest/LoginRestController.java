package com.kyobee.rest;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.kyobee.dto.UserDTO;
import com.kyobee.dto.common.Credential;
import com.kyobee.dto.common.Response;
import com.kyobee.entity.User;
import com.kyobee.exception.RsntException;
import com.kyobee.service.ISecurityService;
import com.kyobee.util.SessionContextUtil;
import com.kyobee.util.common.CommonUtil;
import com.kyobee.util.common.Constants;
import com.kyobee.util.common.LoggerUtil;

@RestController
@RequestMapping("/rest")
public class LoginRestController {

	@Autowired
	ISecurityService securityService;
	
	@Autowired
	SessionContextUtil sessionContextUtil;

	@RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json")
	public Response<UserDTO> login(@RequestBody Credential credenitals, HttpServletRequest request) {
		Response<UserDTO> response = new Response<UserDTO>();
		User loginUser = null;
		try {
			if (credenitals != null && credenitals.getUsername() != null && credenitals.getPassword() != null) {
				loginUser = securityService.getSecurityUserDetails(credenitals.getUsername());

				if (loginUser != null) {

					if (!loginUser.isActive()) {
						response.setStatus("ERROR");
						CommonUtil.setWebserviceResponse(response, Constants.FAILURE, "", "",
								"Account is active. Please contact customer support.");
						return response;
					}

					HttpSession sessionObj = request.getSession();
					loadOrgContextData(loginUser.getUserId());
					UserDTO userDTO = prepareUserObj(loginUser);
					sessionObj.setAttribute(Constants.USER_OBJ, userDTO);
					response.setServiceResult(userDTO);
					LoggerUtil.logInfo("---- Login successful ----- : " + userDTO.getUserName());
					response.setStatus("SUCCESS");
					CommonUtil.setWebserviceResponse(response, Constants.SUCCESS, "");
				} else {
					response.setStatus("ERROR");
					CommonUtil.setWebserviceResponse(response, Constants.FAILURE, "", "",
							"Login Failed. Invalid Username or Password");
					return response;
				}
			} else {
				response.setStatus("ERROR");
				CommonUtil.setWebserviceResponse(response, Constants.ERROR, "");
			}

		} catch (RsntException e) {
			response.setStatus("ERROR");
			CommonUtil.setWebserviceResponse(response, Constants.ERROR, "");
			LoggerUtil.logError("Error while login", e);
		}
		return response;
	}
	
	@RequestMapping(value = "/userDetails", method = RequestMethod.GET, produces = "application/json")
	public Response<UserDTO> fetchUserDetails(HttpServletRequest request) {
		Response<UserDTO> userDetails = new Response<UserDTO>();
		HttpSession sessionObj = request.getSession();
		UserDTO userDTO = (UserDTO)sessionObj.getAttribute(Constants.USER_OBJ);
		userDetails.setServiceResult(userDTO);
		CommonUtil.setWebserviceResponse(userDetails, Constants.SUCCESS, "");
		return userDetails;
	}
	
	private void loadOrgContextData(Long userId) throws RsntException {
		try {
			securityService.getUserOrganization(userId);
			// Long orgId = new Long(1);
		} catch (Exception e) {
			LoggerUtil.logError("Exception: ", e);
			throw e;
		}

	}
	
	private UserDTO prepareUserObj(User loginUser) {
		UserDTO userDTO = new UserDTO();
		userDTO.setUserId(loginUser.getUserId());
		userDTO.setUserName(loginUser.getUserName());
		userDTO.setFirstName(loginUser.getFirstName());
		userDTO.setLastName(loginUser.getLastName());
		userDTO.setEmail(loginUser.getEmail());
		userDTO.setPrimaryContactNo(loginUser.getPrimaryContactNo());
		userDTO.setAlternateContactNo(loginUser.getAlternateContactNo());
		userDTO.setActivationId(loginUser.getActivationId());
		userDTO.setAddress(loginUser.getAddress());
		userDTO.setActive(loginUser.isActive());
		userDTO.setPermissionList(new ArrayList<String>());
		userDTO.setOrganizationId((Long) sessionContextUtil.get(Constants.CONST_ORGID));
		final List<String> userPermissions = securityService.getUserPermissions(loginUser.getUserId());
		if (userPermissions != null && !userPermissions.isEmpty()) {
			for (final String permission : userPermissions) {
				userDTO.getPermissionList().add(permission);
			}
		}
		return userDTO;
	}

}
