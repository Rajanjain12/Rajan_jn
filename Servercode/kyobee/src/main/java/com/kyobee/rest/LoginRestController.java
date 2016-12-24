package com.kyobee.rest;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.kyobee.dto.UserDTO;
import com.kyobee.dto.common.Credential;
import com.kyobee.dto.common.Response;
import com.kyobee.entity.User;

@RestController
@RequestMapping("/rest")
public class LoginRestController {

	@RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json")
	public Response<UserDTO> login(@RequestBody Credential credenitals,HttpServletRequest request) {
		Response<UserDTO> reponse = new Response<UserDTO>();
		User loginUser = null;
		/*try {			
			if(credenitals != null && credenitals.getUsername() != null && credenitals.getPassword() != null){
				loginUser = userService.loginUser(credenitals.getUsername(), credenitals.getPassword());
				
				if(loginUser !=null ){
					
					if (loginUser.getActivationCompleted().longValue() == 0) {
						CommonUtil.setWebserviceResponse(reponse, BeeyaConstants.FAILURE, "", "",
								"Account is not verified. Please check your email & complete verification process.");
						return reponse;
					}
					
					if (CommonUtil.isNullOrEmpty(loginUser.getUserRoles())) {
						CommonUtil.setWebserviceResponse(reponse, BeeyaConstants.FAILURE, "", "",
								"Login Failed. No UserRoles Defined. Please contact support");
						return reponse;
					} else {
						boolean active = false;
						for (UserRole uRole : loginUser.getUserRoles()) {
							if (uRole.getActive() == 1) {
								active = true;
								break;
							}
						}
						if (!active) {
							CommonUtil.setWebserviceResponse(reponse, BeeyaConstants.FAILURE, "", "",
									"Login Failed. User is not active. Please contact support");
							return reponse;
						}
					}
					HttpSession sessionObj = request.getSession();
					UserDTO userDTO = prepareUserObject(loginUser,false);
					sessionObj.setAttribute(BeeyaConstants.USER_OBJ, userDTO);
					reponse.setServiceResult(userDTO);
					LoggerUtil.logInfo("---- Login successful ----- : " + userDTO.getUsername());					
					CommonUtil.setWebserviceResponse(reponse, BeeyaConstants.SUCCESS, "");
				}else{
					CommonUtil.setWebserviceResponse(reponse, BeeyaConstants.FAILURE, "", "", "Login Failed. Invalid Username or Password");
					return reponse;
				}
			}else{
				CommonUtil.setWebserviceResponse(reponse, BeeyaConstants.ERROR, "");
			}

		} catch (ApplicationException e) {
			CommonUtil.setWebserviceResponse(reponse, BeeyaConstants.ERROR, "");
			LoggerUtil.logError("Error while login", e);
		}*/
		return reponse;
	}
	
}
