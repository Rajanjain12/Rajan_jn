package com.rsnt.service;

import java.util.List;

import javax.ejb.Local;

import com.rsnt.common.exception.RsntException;
import com.rsnt.entity.OrganizationLayout;
import com.rsnt.entity.OrganizationLayoutMarkers;

@Local
public interface ILayoutService {
	
	public String SEAM_NAME = "layoutService";
	public String JNDI_NAME="LayoutServiceImpl";
	
	public List<Object[]> loadAllOrgLayouts() throws RsntException;
	
	//Added by Aditya for Arrange Layout
	public List<Object[]> loadDashboardDataUsingByLayoutId(String layoutId) throws RsntException;
	
	public OrganizationLayout fetchLayoutEntity(Long pLayoutId) throws RsntException;
	
	public OrganizationLayout fetchLayoutEntityWithOptions(Long pLayoutId) throws RsntException;
	
	public OrganizationLayout saveOrUpdateOrganizationLayoutEntity(OrganizationLayout orgLayout) throws RsntException;
	
	public List<OrganizationLayoutMarkers> loadLayoutMarkerData(Long pOrganizationLayoutId,  boolean fetchActive) throws RsntException;
	
	public OrganizationLayoutMarkers saveLayoutMarkerData(OrganizationLayoutMarkers layoutTableInfo) throws RsntException;
	
	public void saveLayoutMarkerPositions(String data) throws RsntException;
	
	public List<Object[]> getAllOrganizationOptions() throws RsntException;
	
	public Long getOrgPlanType() throws RsntException;
	
	public List<Object[]> getLayoutOrganizationOptions(Long pOrgLayoutId) throws RsntException;
	
	
	public OrganizationLayout updateOrganizationLayoutOptions(OrganizationLayout organizationLayout) throws RsntException;
	
	public OrganizationLayoutMarkers fetchLayoutMarkerEntity(Long pOrgLayoutMarkerId) throws RsntException;
	
	public void resetOrgLayoutDefaultFlags(Long pOrgLayoutId) throws RsntException;
	public Long  getDefaultOrgLayoutCheck(Long pOrgId) throws RsntException;
	public List<OrganizationLayoutMarkers> loadLayoutMarkerForDefaultOrgLayout() throws RsntException;
	
	public List<Object[]> getOrgLayouts() throws RsntException;
	public Long getGenerateQRCodeBtnDisplay(Long pOrgLayoutId)throws RsntException;
	public List<OrganizationLayout> checkDuplicateLayout(String pLayoutName, Long pOrgLayoutId);
	public List<OrganizationLayoutMarkers> checkDuplicateMarker(String pLayoutMarker, Long pOrgMarkerId, Long pOrgLayoutId) ;
	
	public List<Object[]> getDashBoardAlertData(Long pOrgLayoutId) throws RsntException;

}
