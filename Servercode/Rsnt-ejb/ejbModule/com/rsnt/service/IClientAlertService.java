package com.rsnt.service;

import java.util.List;

import javax.ejb.Local;

import com.rsnt.common.exception.RsntException;
import com.rsnt.entity.Ads;
import com.rsnt.entity.OrganizationLayoutMarkerDeviceMap;
import com.rsnt.util.transferobject.AdsDataTO;

@Local
public interface IClientAlertService {

	public String SEAM_NAME = "clientAlertService";
	public String JNDI_NAME="ClientAlertServiceImpl";
	
	
	public void processOrgMarkerDashBoardAlert(Long pOrgLayoutMarkerId, Long pAlertId, String deviceId, String deviceMake) throws RsntException;
	
	public List<Object[]> getOrgCords(Long pOrgMarkerId)throws RsntException;
	
	public void checkDuplicateDashBoardAlert(Long pOrgMarkerId, Long pAlertId, String deviceId) throws RsntException;
	
	public void processDashBoardAlertRequest(Long pOrgMarkerId, Long pAlertId, Long pWaiterId) throws RsntException;
	
	public List<Object[]> getMarkerAlerts(Long pOrgMarkerId)throws RsntException;
	
	public List<AdsDataTO> getSpecialAdList(Long pOrgMarkerId, int dayOfWeek, String date, String time)throws RsntException;
	
	public Ads getSpecialAdDetail(Long pAdId) throws RsntException;
	
	public void registerDevice(OrganizationLayoutMarkerDeviceMap layoutMarkerDeviceMap)throws RsntException;
	
	public List<Object[]> getDeviceDetail(Long pOrgLayoutMarkerId,Long pAlertId)throws RsntException;
	
	public void saveAdAnalytics(Long orgLayoutMarkerId, Long adId, Long pOrgId) throws RsntException;
	
	public List<Ads> getAdDetails(List<Long> pAdListId) throws RsntException;
	public List<Object[]> loadFeedbackMobileData(Long pOrgLayoutMarkerId)  throws RsntException;
	public List<Object[]> loadFeedbackData(Long pOrgLayoutMarkerId) throws RsntException;
	public void saveFeedbackCustomerDetail(String cusData,String qtnDetailData, Long orgLayoutMarkerId)throws RsntException;
	public void saveFeedbackCustomerDetailV2(String data,Long orgLayoutMarkerId)throws RsntException;
	/* new call for realtime for dashboard  */
	public List<Object[]> getDashBoardAlertData(Long pOrgLayoutId) throws RsntException;

	//Added by Aditya
	public boolean checkActiveMarkerPresent(Long orgMarkerId);
}
