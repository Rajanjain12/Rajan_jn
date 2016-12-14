package com.rsnt.web.rest;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

import com.rsnt.service.ILoginAuthService;
import com.rsnt.service.IWaitListService;
import com.rsnt.util.common.CommonUtil;
import com.rsnt.util.common.Constants;
import com.rsnt.web.rest.request.GuestPreferencesDTO;

import net.sf.json.JSONObject;

@Path("/loginAuthRestAction")
@Name("loginAuthRestAction")

public class LoginAuthRestAction {

	//************************************************
	// instance variable declaration
	//*************************************************

	@Logger
	private Log log;
	@In(value=ILoginAuthService.SEAM_NAME, create=true)
	private ILoginAuthService LoginAuthService;
	@In(value=IWaitListService.SEAM_NAME, create=true)
	private IWaitListService waitListService;

	
	@GET
	@Path("/loginCredAuth")
	public String loginCredAuth(@QueryParam("username") String username, @QueryParam("password") String password){
		log.info("Entering :: loginCredAuth");
		final Map<String, Object> rootMap = new LinkedHashMap<String, Object>();
		List<Object[]>  result = LoginAuthService.loginCredAuth(username, password);
   		Object[] loginDetail = result.get(0);
   		
		if(loginDetail[0].toString().equals(CommonUtil.encryptPassword(password))){
			System.out.println("password--"+loginDetail[0].toString());
			System.out.println("OrgId--"+loginDetail[1].toString());
			System.out.println("logofile name--"+loginDetail[2].toString());
			
			rootMap.put("OrgId",loginDetail[1].toString());
			rootMap.put("logofile name",loginDetail[2].toString());

			List<GuestPreferencesDTO> searPref =  null;
			try {
				searPref=	waitListService.getOrganizationSeatingPref(Long.valueOf(loginDetail[1].toString()).longValue());
				rootMap.put("success", "0");
				rootMap.put("seatpref",searPref);
			}catch(Exception e){
				System.out.println(e);
			}
		}else{
			rootMap.put("success", "-1");
			rootMap.put(Constants.RSNT_ERROR, "Invalid Username or Password.");

		}
		
		
		final JSONObject jsonObject = JSONObject.fromObject(rootMap);
		return jsonObject.toString();
	}


}
