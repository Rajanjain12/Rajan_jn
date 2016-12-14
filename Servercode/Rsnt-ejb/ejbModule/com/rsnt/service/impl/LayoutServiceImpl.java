package com.rsnt.service.impl;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.log.Log;

import com.rsnt.common.exception.RsntException;
import com.rsnt.common.service.impl.RsntBaseService;
import com.rsnt.entity.OrganizationLayout;
import com.rsnt.entity.OrganizationLayoutMarkers;
import com.rsnt.entity.OrganizationLayoutOption;
import com.rsnt.service.ILayoutService;
import com.rsnt.util.common.CommonUtil;
import com.rsnt.util.common.Constants;
import com.rsnt.util.common.NativeQueryConstants;


@Stateless(name=ILayoutService.JNDI_NAME)
@Scope(ScopeType.STATELESS)
@Name(ILayoutService.SEAM_NAME)
public class LayoutServiceImpl extends RsntBaseService implements ILayoutService{
	
	@In
	private EntityManager entityManager;
	
	@Logger
	private Log log;
	
	
	public Long getOrgPlanType() throws RsntException{
		try{
			return ((BigInteger)entityManager.createNativeQuery(NativeQueryConstants.GET_ORGANIZATION_PLAN_TYPE).
					setParameter(1, Contexts.getSessionContext().get(Constants.CONST_ORGID).toString()).getSingleResult()).longValue();
		}
		catch(Exception e){
			log.error("LayoutServiceImpl.getOrgPlanType()", e);
			throw new RsntException(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> loadAllOrgLayouts() throws RsntException{
		
		try{
			
			List<Object[]> layoutDataObjList =  entityManager.createNativeQuery(NativeQueryConstants.GET_ALL_LAYOUTS_FOR_ORG).
					setParameter(1, Contexts.getSessionContext().get(Constants.CONST_ORGID)).getResultList();
			return layoutDataObjList;
		}
		catch(Exception e){
			log.error("LayoutServiceImpl.loadLayoutData()", e);
			throw new RsntException("LayoutServiceImpl.loadLayoutData()", e);
		}
	}
	
	//Added by Aditya for Arrange Layout
	@SuppressWarnings("unchecked")
	public List<Object[]> loadDashboardDataUsingByLayoutId(String layoutId) throws RsntException{
		
		try{
			
			List<Object[]> layoutDataObjList =  entityManager.createNativeQuery(NativeQueryConstants.GET_LAYOUTS_BY_LAYOUTID).
					setParameter(1, Contexts.getSessionContext().get(Constants.CONST_ORGID)).
					setParameter(2, layoutId).getResultList();
			return layoutDataObjList;
		}
		catch(Exception e){
			log.error("LayoutServiceImpl.loadLayoutData()", e);
			throw new RsntException("LayoutServiceImpl.loadLayoutData()", e);
		}
	}
	
	public OrganizationLayout fetchLayoutEntity(Long pLayoutId) throws RsntException{
		try{
			return (OrganizationLayout)entityManager.createNamedQuery(OrganizationLayout.FETCH_ORGANIZATION_LAYOUT_OBJ).setParameter(1, pLayoutId).
								setParameter(2, Long.valueOf(Contexts.getSessionContext().get(Constants.CONST_ORGID).toString())).getSingleResult();
			
		}catch(Exception e){
			log.error("LayoutServiceImpl.fetchLayoutEntity()", e);
			throw new RsntException("LayoutServiceImpl.fetchLayoutEntity()", e);
		}
		
		
	}
	
	public OrganizationLayout fetchLayoutEntityWithOptions(Long pLayoutId) throws RsntException{
		try{
			return (OrganizationLayout)entityManager.createNamedQuery(OrganizationLayout.FETCH_ORGANIZATION_LAYOUT_OBJ_OPTIONS).setParameter(1, pLayoutId).
								setParameter(2, Long.valueOf(Contexts.getSessionContext().get(Constants.CONST_ORGID).toString())).getSingleResult();
			
		}catch(Exception e){
			log.error("LayoutServiceImpl.fetchLayoutEntity()", e);
			throw new RsntException("LayoutServiceImpl.fetchLayoutEntity()", e);
		}
		
		
	}
	
	public OrganizationLayoutMarkers fetchLayoutMarkerEntity(Long pOrgLayoutMarkerId) throws RsntException{
		try{
			return (OrganizationLayoutMarkers)entityManager.find(OrganizationLayoutMarkers.class, pOrgLayoutMarkerId);
		}catch(Exception e){
			log.error("LayoutServiceImpl.fetchLayoutMarkerEntity()", e);
			throw new RsntException("LayoutServiceImpl.fetchLayoutMarkerEntity()", e);
		}
		
		
	}
	
	public OrganizationLayout saveOrUpdateOrganizationLayoutEntity(OrganizationLayout orgLayout) throws RsntException{
		try{
			orgLayout.setOrganizationId(Long.valueOf(Contexts.getSessionContext().get(Constants.CONST_ORGID).toString()));
			if(orgLayout.getOrganizationLayoutId()!=null){
				setModifiedAtttributes(orgLayout);
			}
			else{
				setCreatedAtttributes(orgLayout);
			}
			
			//Below needs to be done only for create new Layout.
			if(orgLayout.getOrganizationLayoutId()==null){
				for(OrganizationLayoutMarkers layoutMarker : orgLayout.getOrganizationLayoutMarkerList() ){
					layoutMarker.setOrganizationId(Long.valueOf(Contexts.getSessionContext().get(Constants.CONST_ORGID).toString()));
					layoutMarker.setLayoutIdentificationName(orgLayout.getLayoutIdentificationName());
					
					if(layoutMarker.getOrganizationLayoutMarkerId()!=null){
						setModifiedAtttributes(layoutMarker);
					}
					else{
						layoutMarker.setOrganizationLayout(orgLayout);
						setCreatedAtttributes(layoutMarker);
					}
				}
			}
			
			return (OrganizationLayout)entityManager.merge(orgLayout);
			
		}catch(Exception e){
			log.error("LayoutServiceImpl.createOrganizationLayoutEntity()", e);
			throw new RsntException("LayoutServiceImpl.createOrganizationLayoutEntity()", e);
		}
		
		
	}
	
	@SuppressWarnings("unchecked")
	public List<OrganizationLayoutMarkers> loadLayoutMarkerData(Long pOrganizationLayoutId, boolean fetchActive) throws RsntException{
		try{
			List<OrganizationLayoutMarkers> orgMarkerList = null;
			if(fetchActive){
				orgMarkerList =  entityManager.createNamedQuery(OrganizationLayoutMarkers.GET_ACTIVE_MARKER_DATA_FOR_LAYOUT).setParameter(1, pOrganizationLayoutId).
				setParameter(2, Long.valueOf(Contexts.getSessionContext().get(Constants.CONST_ORGID).toString())).getResultList();
			}else{
				orgMarkerList =  entityManager.createNamedQuery(OrganizationLayoutMarkers.GET_MARKER_DATA_FOR_LAYOUT).setParameter(1, pOrganizationLayoutId).
				setParameter(2, Long.valueOf(Contexts.getSessionContext().get(Constants.CONST_ORGID).toString())).getResultList();
			}
			
			
			return orgMarkerList;
		}
		catch(Exception e){
			log.error("LayoutServiceImpl.loadLayoutTableData()", e);
			throw new RsntException("LayoutServiceImpl.loadLayoutTableData()", e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<OrganizationLayoutMarkers> loadLayoutMarkerForDefaultOrgLayout() throws RsntException{
		try{
			List<OrganizationLayoutMarkers> orgMarkerList =  entityManager.createNamedQuery(OrganizationLayoutMarkers.GET_MARKER_DATA_FOR_DEFAULT_ORG_LAYOUT).
				setParameter(1, Long.valueOf(Contexts.getSessionContext().get(Constants.CONST_ORGID).toString()))
				.getResultList();
			
			return orgMarkerList;
		}
		catch(Exception e){
			log.error("LayoutServiceImpl.loadLayoutMarkerForDefaultOrgLayout()", e);
			throw new RsntException("LayoutServiceImpl.loadLayoutMarkerForDefaultOrgLayout()", e);
		}
	}
	
	public List<Object[]> getOrgLayouts() throws RsntException{
		try{
			List<Object[]> orgLayoutObjArr=  entityManager.createNativeQuery(NativeQueryConstants.GET_ALL_LAYOUTS_FOR_ORG_ACTIVE_FILTER).
				setParameter(1, Long.valueOf(Contexts.getSessionContext().get(Constants.CONST_ORGID).toString()))
				.getResultList();
			
			return orgLayoutObjArr;
		}
		catch(Exception e){
			log.error("LayoutServiceImpl.loadLayoutMarkerForDefaultOrgLayout()", e);
			throw new RsntException("LayoutServiceImpl.loadLayoutMarkerForDefaultOrgLayout()", e);
		}
	}
	
	public void saveLayoutMarkerPositions(String data) throws RsntException{
	
    	OrganizationLayoutMarkers layoutMarker = null;
    	try{
    		if(data.startsWith("{"))
        	{
        		StringBuffer newData = new StringBuffer(data);
        		newData.append("]");
        		newData.insert(0, "[");
        		data = newData.toString();
        	}
    		
    		ObjectMapper mapper = new ObjectMapper(); 
       		TypeReference<List<HashMap<String, String>>> typeRef = new TypeReference<List<HashMap<String,String> >>() {}; 
       		List<HashMap<String, String>> updateData = mapper.readValue(data, typeRef);
       		
       		
       		for(HashMap<String, String> testMap : updateData){
       			
       			layoutMarker = entityManager.find(OrganizationLayoutMarkers.class, Long.valueOf(testMap.get("id").toString()));
       			layoutMarker.setLayoutMarkerPosRowData(Long.valueOf(testMap.get("dataRow").toString()));
       			layoutMarker.setLayoutMarkerPosColData(Long.valueOf(testMap.get("dataCol").toString()));
       			layoutMarker.setLayoutMarkerDataXSize(Long.valueOf(testMap.get("dataSizex").toString()));
       			layoutMarker.setLayoutMarkerDataYSize(Long.valueOf(testMap.get("dataSizey").toString()));
       			layoutMarker.setLayoutMarkerStyleClass(CommonUtil.toStr(testMap.get("styleClass")));
       			layoutMarker.setLayoutMarkerStylePos(CommonUtil.toStr(testMap.get("stylePos")));
    	   		entityManager.flush();
    	   		
       		}
    	}catch(Exception e){
    		log.error("LayoutServiceImpl.saveLayoutTableInfoData()", e);
			throw new RsntException("LayoutServiceImpl.saveLayoutTableInfoData()", e);
       	}
	}
	
	public OrganizationLayoutMarkers saveLayoutMarkerData(OrganizationLayoutMarkers layoutMarker) throws RsntException{
		try{
			
			
			layoutMarker.setOrganizationId(Long.valueOf(Contexts.getSessionContext().get(Constants.CONST_ORGID).toString()));
			if(layoutMarker.getOrganizationLayoutMarkerId()!=null){
				setModifiedAtttributes(layoutMarker);
			}
			else{
				setCreatedAtttributes(layoutMarker);
			}
			return (OrganizationLayoutMarkers)entityManager.merge(layoutMarker);
			
		}
		catch(Exception e){
			log.error("LayoutServiceImpl.saveLayoutTableInfoData()", e);
			throw new RsntException("LayoutServiceImpl.saveLayoutTableInfoData()", e);
		}
	}
	
	public OrganizationLayout updateOrganizationLayoutOptions(OrganizationLayout organizationLayout) throws RsntException{
		try{
			for(OrganizationLayoutOption option : organizationLayout.getOrganizationLayoutOptionList() ){
				option.setOrganizationId(Long.valueOf(Contexts.getSessionContext().get(Constants.CONST_ORGID).toString()));
				option.setLayoutIdentificationName(organizationLayout.getLayoutIdentificationName());
				
				if(option.getOrganizationLayoutOptionId()!=null){
					setModifiedAtttributes(option);
				}
				else{
					option.setOrganizationLayout(organizationLayout);
					setCreatedAtttributes(option);
				}
			}
			
			return entityManager.merge(organizationLayout);
		}
		catch(Exception e){
			log.error("LayoutServiceImpl.updateOrganizationLayout()", e);
			throw new RsntException("LayoutServiceImpl.updateOrganizationLayout()", e);
		}
	}
	
	public List<Object[]> getAllOrganizationOptions() throws RsntException{
		try{
			return entityManager.createNativeQuery(NativeQueryConstants.GET_ORG_ALL_OPTIONS).
					setParameter(1,Long.valueOf(Contexts.getSessionContext().get(Constants.CONST_ORGID).toString())).getResultList();
		}
		catch(Exception e){
			log.error("LayoutServiceImpl.getOrganizationOptions()", e);
			throw new RsntException("LayoutServiceImpl.getOrganizationOptions()", e);
		}
		
		
	}
	
	public List<Object[]> getLayoutOrganizationOptions(Long pOrgLayoutId) throws RsntException{
		try{
			return entityManager.createNativeQuery(NativeQueryConstants.GET_ORG_LAYOUT_SELECTED_OPTIONS).
					setParameter(1,Long.valueOf(Contexts.getSessionContext().get(Constants.CONST_ORGID).toString()))
					.setParameter(2, pOrgLayoutId).getResultList();
		}
		catch(Exception e){
			log.error("LayoutServiceImpl.getOrganizationOptions()", e);
			throw new RsntException("LayoutServiceImpl.getOrganizationOptions()", e);
		}
		
		
	}
	
	/*public void associateLayoutToOptions(Long pOrgLayoutId, List<Integer> orgLayoutOptionsList) throws RsntException{
		
		try{
			OrganizationLayout layout = entityManager.find(OrganizationLayout.class, pOrgLayoutId);
			layout.getOrganizationLayoutOptionList().clear();
			
		}
		catch(Exception e){
			log.error("LayoutServiceImpl.associateLayoutToOptions()", e);
			throw new RsntException("LayoutServiceImpl.associateLayoutToOptions()", e);
		}
		
	}*/
	
	public void resetOrgLayoutDefaultFlags(Long pOrgLayoutId) throws RsntException{
		
		try{
			entityManager.createNativeQuery(NativeQueryConstants.UPDATE_ORG_DEFAULT_FLAGS).setParameter(1, pOrgLayoutId).executeUpdate();
		}
		catch(Exception e){
			log.error("LayoutServiceImpl.resetOrgLayoutDefaultFlags()", e);
			throw new RsntException("LayoutServiceImpl.resetOrgLayoutDefaultFlags()", e);
		}
		
	}
	
	
	
	public Long  getDefaultOrgLayoutCheck(Long pOrgId) throws RsntException{
		
		try{
			return ((BigInteger)entityManager.createNativeQuery(NativeQueryConstants.GET_ORG_LAYOUT_DEFAULT_CHECK_FLAG).setParameter(1, pOrgId).getSingleResult()).longValue();
		}
		catch(Exception e){
			log.error("LayoutServiceImpl.resetOrgLayoutDefaultFlags()", e);
			throw new RsntException("LayoutServiceImpl.resetOrgLayoutDefaultFlags()", e);
		}
	}
	
	public Long getGenerateQRCodeBtnDisplay(Long pOrgLayoutId)throws RsntException{
		try{
			return ((BigInteger)entityManager.createNativeQuery(NativeQueryConstants.GET_QRCODE_BTN_DISPLAY).setParameter(1, pOrgLayoutId).getSingleResult()).longValue();
		}
		catch(Exception e){
			log.error("LayoutServiceImpl.getGenerateQRCodeBtnDisplay()", e);
			throw new RsntException("LayoutServiceImpl.getGenerateQRCodeBtnDisplay()", e);
		}
		
	}
	
	
	@SuppressWarnings("unchecked")
	public List<OrganizationLayout> checkDuplicateLayout(String pLayoutName, Long pOrgLayoutId) {
		try{
			
			if(pOrgLayoutId!=null)
				return entityManager.createNamedQuery(OrganizationLayout.FETCH_ORGANIZATION_LAYOUT_FROM_LAYOUTNAME_AND_ID).setParameter(1, pLayoutName.toLowerCase())
				.setParameter(2,Long.valueOf(Contexts.getSessionContext().get(Constants.CONST_ORGID).toString()))
				.setParameter(3, pOrgLayoutId)
		.		getResultList();
			else
				
				return entityManager.createNamedQuery(OrganizationLayout.FETCH_ORGANIZATION_LAYOUT_FROM_LAYOUTNAME).setParameter(1, pLayoutName.toLowerCase())
				.setParameter(2,Long.valueOf(Contexts.getSessionContext().get(Constants.CONST_ORGID).toString()))
				.getResultList();
				
		}
		catch(Exception e){
			log.error("LayoutServiceImpl.checkDuplicateLayout()", e);
			//throw new RsntException("LayoutServiceImpl.checkDuplicateLayout()", e);
			return null;
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public List<OrganizationLayoutMarkers> checkDuplicateMarker(String pLayoutMarker, Long pOrgMarkerId, Long pOrgLayoutId) {
		try{
			if(pOrgMarkerId!=null)
			return entityManager.createNamedQuery(OrganizationLayoutMarkers.GET_LAYOUT_MARKER_FROM_MARKERNAME_WITHID).setParameter(1, pLayoutMarker.toLowerCase())
					.setParameter(2,Long.valueOf(Contexts.getSessionContext().get(Constants.CONST_ORGID).toString())).setParameter(3, pOrgMarkerId)
					.setParameter(4, pOrgLayoutId)
			.getResultList();
			else
				return entityManager.createNamedQuery(OrganizationLayoutMarkers.GET_LAYOUT_MARKER_FROM_MARKERNAME).setParameter(1, pLayoutMarker.toLowerCase())
				.setParameter(2,Long.valueOf(Contexts.getSessionContext().get(Constants.CONST_ORGID).toString())).setParameter(3, pOrgLayoutId)
		.getResultList();
		}
		catch(Exception e){
			log.error("LayoutServiceImpl.checkDuplicateLayout()", e);
			//throw new RsntException("LayoutServiceImpl.checkDuplicateLayout()", e);
			return null;
		}
		
	}
	
	public List<Object[]> getDashBoardAlertData(Long pOrgLayoutId) throws RsntException{
		
		try{
			return entityManager.createNativeQuery(NativeQueryConstants.GET_DASHBOARD_ALERT_DATA).setParameter(1, pOrgLayoutId).getResultList();
		}
		catch(Exception e){
			log.error("LayoutServiceImpl.getDashBoardAlertData()", e);
			throw new RsntException("LayoutServiceImpl.getDashBoardAlertData()", e);
		}
	
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

}
