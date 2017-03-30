/**
 * EJB Local interface provides Guest interaction methods
 */
package com.kyobee.service;

import java.util.List;
import java.util.Map;

import com.kyobee.dto.GuestPreferencesDTO;
import com.kyobee.dto.GuestWrapper;
import com.kyobee.dto.WaitlistMetrics;
import com.kyobee.entity.Guest;
import com.kyobee.entity.GuestNotificationBean;
import com.kyobee.entity.GuestPreferences;
import com.kyobee.entity.Organization;
import com.kyobee.exception.RsntException;


public interface IOrganizationService {
	public String SEAM_NAME = "waitListService";
	public String JNDI_NAME="waitListServiceImpl";
	
	public Organization getOrganizationById(Long pOrgId) ;
}
