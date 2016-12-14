package com.rsnt.web.action;



import ibt.ortc.api.Ortc;
import ibt.ortc.extensibility.OnConnected;
import ibt.ortc.extensibility.OrtcClient;
import ibt.ortc.extensibility.OrtcFactory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import net.sf.json.JSONObject;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;

import com.rsnt.common.exception.RsntException;
import com.rsnt.entity.OrganizationLayoutMarkers;
import com.rsnt.service.IClientAlertService;
import com.rsnt.service.ILayoutService;
import com.rsnt.service.IStaffService;
import com.rsnt.util.common.AppInitializer;
import com.rsnt.util.common.Constants;
import com.rsnt.util.pusher.IPusher;
import com.rsnt.util.pusher.factory.PusherFactory;
import com.rsnt.util.transferobject.AdsDataTO;
import com.rsnt.web.util.RealtimefameworkPusher;



@Name("dashBoardAction")

@Scope(ScopeType.PAGE)

public class DashBoardAction {



	@Logger

	private Log log;

	

	@In(value=ILayoutService.SEAM_NAME,create=true)

    private ILayoutService layoutService;

	

	@In(value=IStaffService.SEAM_NAME,create=true)

    private IStaffService staffService;

	

	private List<SelectItem> orgLayoutList = new ArrayList<SelectItem>();

	

	private List<AdsDataTO> orgLayoutOptionList = new ArrayList<AdsDataTO>();



	private List<OrganizationLayoutMarkers> organizationLayoutMarkerList = new ArrayList<OrganizationLayoutMarkers>();

	

	private String selectedLayoutName;

	

	private String selectedOrgLayoutId;

	

	private String selectedOrgLayoutMarkerId;

	

	@In(create=  true)

	private LayoutManagerAction layoutManagerAction;

	

	@In

    private Map messages;

	

	private Long selectedTaskWaiterId;

	private String selectedMarkerOptionId;

	

	private List<SelectItem> orgLayoutWaiters = new ArrayList<SelectItem>();

	private List<SelectItem> orgMarkerOptions= new ArrayList<SelectItem>();

	



	@In(value=IClientAlertService.SEAM_NAME,create=true)

	private IClientAlertService clientAlertService;

	

	/*  public void methodInManagedBean() throws IOException {

		  FacesContext.getCurrentInstance().getExternalContext().redirect("/views/dashboard.xhtml");

	  }

	*/



 	 public String loadDefaultOrgDashBoard(){

		try{

			log.info("Default org Dashboard called for: "+this.getSelectedOrgLayoutId());

			//layoutManagerAction.setDashboardMode(true);

			organizationLayoutMarkerList = new ArrayList<OrganizationLayoutMarkers>();

			loadOrgWaiterData();

			

			orgLayoutList = new ArrayList<SelectItem>();
			//Modified by Aditya
			orgLayoutList.add(new SelectItem(0,"Select.."));
			

			List<Object[]> allLayoutObj =  layoutService.getOrgLayouts();

			for(Object[] obj: allLayoutObj){

				orgLayoutList.add(new SelectItem(obj[0].toString(),obj[2].toString()));

			}

			//orgLayoutList.add(new SelectItem(0,"Select.."));

			

			orgLayoutOptionList = new ArrayList<AdsDataTO>();

			

			organizationLayoutMarkerList = layoutService.loadLayoutMarkerForDefaultOrgLayout();

			if(organizationLayoutMarkerList.size()>0){

				

				setSelectedLayoutName(organizationLayoutMarkerList.get(0).getOrganizationLayout().getLayoutIdentificationName());

				

				

				//setSelectedOrgLayoutId((organizationLayoutMarkerList.get(0).getOrganizationLayout().getOrganizationLayoutId().toString()));
				//MOdified by Aditya
				setSelectedOrgLayoutId("0");
				

				List<Object[]> layoutOrgObjectArr= layoutService.getLayoutOrganizationOptions(organizationLayoutMarkerList.get(0).getOrganizationLayout().getOrganizationLayoutId());

				AdsDataTO dataTO = null;  

				for(Object[] obj : layoutOrgObjectArr){

					dataTO = new AdsDataTO();

					Long lookupTypeId = null;

					if(obj[2]!=null) lookupTypeId = new Long(obj[2].toString());

					dataTO.setAdsId(obj[0].toString());

					dataTO.setDescription(obj[1].toString());

					if(lookupTypeId!=null && lookupTypeId.intValue()==1)

						//Changed by Shruti Shah to fetch the domain name from Properties and removing the hard coding

						dataTO.setImageUrl(AppInitializer.domainNameUrl+"/dboardImg/"+obj[0].toString()+".png");

						//dataTO.setImageUrl("http://ringmyserver.com/dboardImg/"+obj[0].toString()+".png");

					else

						//Changed by Shruti Shah to fetch the domain name from Properties and removing the hard coding

						dataTO.setImageUrl(AppInitializer.domainNameUrl+"/dboardImg/99.png");

						//dataTO.setImageUrl("http://ringmyserver.com/dboardImg/99.png");

					

					//orgLayoutOptionList.add(new SelectItem(obj[0].toString(),obj[1].toString()));

					orgLayoutOptionList.add(dataTO);

				}

			}

			

			else{

				setSelectedOrgLayoutId("0");

				FacesMessages.instance().add("No dashboard found.  Please configure a layout.");		

			}

			

			/*optionsList.add(new SelectItem("cs","Call Server"));

			optionsList.add(new SelectItem("cc","Call Check"));*/

		}

		catch(RsntException e){

			e.printStackTrace();

			FacesMessages.instance().add("Unable to load Default Layout. Please check Default Layout Detail");		

		}

		return "success";

		

	}

	

	

	public String loadOrgDashBoardForOrgLayoutId(){

		try{

			

			log.info("Org Dashboard called for: "+this.getSelectedOrgLayoutId());

			organizationLayoutMarkerList = new ArrayList<OrganizationLayoutMarkers>();

			orgLayoutOptionList = new ArrayList<AdsDataTO>();

			if(this.getSelectedOrgLayoutId()==null){

				//request from Arrange Layout

				System.out.println("Param1: "+ FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("param1"));

				setSelectedOrgLayoutId(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("param1"));

				

			}

			

			

			if(this.getSelectedOrgLayoutId().equalsIgnoreCase("0")){

				final FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, messages

	                    .get("organization.layout.selectOrgLayoutId").toString(), null);

	            FacesContext.getCurrentInstance().addMessage(null, message);

			}else{

				organizationLayoutMarkerList = layoutService.loadLayoutMarkerData(Long.valueOf(this.getSelectedOrgLayoutId()), true);

				

				if(organizationLayoutMarkerList.size()>0){

					setSelectedLayoutName(organizationLayoutMarkerList.get(0).getOrganizationLayout().getLayoutIdentificationName());

					setSelectedOrgLayoutId((organizationLayoutMarkerList.get(0).getOrganizationLayout().getOrganizationLayoutId().toString()));

					

					List<Object[]> layoutOrgObjectArr= layoutService.getLayoutOrganizationOptions(organizationLayoutMarkerList.get(0).getOrganizationLayout().getOrganizationLayoutId());

					/*for(Object[] obj : layoutOrgObjectArr){

						orgLayoutOptionList.add(new SelectItem(obj[0].toString(),obj[1].toString()));

					}*/

					AdsDataTO dataTO = null;  

					for(Object[] obj : layoutOrgObjectArr){

						dataTO = new AdsDataTO();

						Long lookupTypeId = null;

						if(obj[2]!=null) lookupTypeId = new Long(obj[2].toString());

						dataTO.setAdsId(obj[0].toString());

						dataTO.setDescription(obj[1].toString());

						if(lookupTypeId!=null && lookupTypeId.intValue()==1)

							//Changed by Shruti Shah to fetch the domain name from Properties and removing the hard coding

							//dataTO.setImageUrl("http://ringmyserver.com/dboardImg/"+obj[0].toString()+".png");

							dataTO.setImageUrl(AppInitializer.domainNameUrl+"/dboardImg/"+obj[0].toString()+".png");

						else

							//Changed by Shruti Shah to fetch the domain name from Properties and removing the hard coding

							//dataTO.setImageUrl("http://ringmyserver.com/dboardImg/99.png");

							dataTO.setImageUrl(AppInitializer.domainNameUrl+"/dboardImg/99.png");



						

						//orgLayoutOptionList.add(new SelectItem(obj[0].toString(),obj[1].toString()));

						orgLayoutOptionList.add(dataTO);

					}

				}

				else{

					setSelectedOrgLayoutId("0");

					final FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, messages

		                    .get("organization.layout.nomarker").toString(), null);

		            FacesContext.getCurrentInstance().addMessage(null, message);

				}

			}

			

			

			//setSelectedOrgLayoutId((organizationLayoutMarkerList.get(0).getOrganizationLayout().getOrganizationLayoutId().toString()));

			/*optionsList.add(new SelectItem("cs","Call Server"));

			optionsList.add(new SelectItem("cc","Call Check"));*/

		}

		catch(RsntException e){

			e.printStackTrace();

		}

		return "success";

		

	}

	

	public boolean processAlertRequest(){
		System.out.println("Select marker option id is :::::"+this.getSelectedMarkerOptionId());
		log.info("processAlertRequest() for MarkerOption "+this.getSelectedMarkerOptionId());

		log.info("processAlertRequest() for Waiter "+this.getSelectedTaskWaiterId());

		//String [] test = this.getSelectedMarkerOptionId().split("-");

		try {

			List<Object[]> deviceData = clientAlertService.getDeviceDetail(new Long(this.getSelectedOrgLayoutMarkerId().substring(4)),Long.parseLong(this.getSelectedMarkerOptionId()));

			String deviceId=null;

			String deviceMake=null; 

			if(deviceData!=null && deviceData.size()>0){

				Object[] data = deviceData.get(0);

				deviceId= data[0].toString();

				deviceMake = data[1].toString();
				System.out.println(deviceId);
				System.out.println(deviceMake);

			}

			clientAlertService.processDashBoardAlertRequest(new Long(this.getSelectedOrgLayoutMarkerId().substring(4)),new Long(this.getSelectedMarkerOptionId()),

					new Long(this.getSelectedTaskWaiterId()));
			
			sendPusherMessage(deviceId,deviceMake);
			
			//Added by Aditya
			boolean activeMarkersPresent = clientAlertService.checkActiveMarkerPresent(new Long(this.getSelectedOrgLayoutMarkerId().substring(4)));
			final Map<String, Object> rootMap = new LinkedHashMap<String, Object>();
			if(activeMarkersPresent)
			{
				rootMap.put("stopMarker", "false");
			}
			else
			{
				rootMap.put("stopMarker", "true");
			}
			rootMap.put("success", 1);
			rootMap.put("markerId",selectedOrgLayoutMarkerId);

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
					RealtimefameworkPusher.client.send(AppInitializer.dashBoardChannel,  (JSONObject.fromObject(rootMap)).toString());
					System.out.println("messge send");
				}
			}; 

			RealtimefameworkPusher.client.connect(RealtimefameworkPusher.applicationKey, RealtimefameworkPusher.authenticationToken);
			//End Added by Aditya

		} catch (RsntException e) {

			e.printStackTrace();

		}

		return true;

		

		

	}

	

	private void sendPusherMessage(String deviceId,String deviceMake){

		try{

				System.out.println("Deviceid : "+deviceId+"DeviceMake:"+deviceMake);

				if(deviceId!=null && deviceMake!=null){

					IPusher pusher = PusherFactory.getPushMessageGenerator(deviceMake);

					//PushNotificationPayload payload = CommonUtil.getJSONPushMessage(getOrgMarkerOptionDetail(this.getSelectedMarkerOptionId()),  this.getSelectedMarkerOptionId());

					//pusher.sendPushMessage(deviceId, payload);
						
					pusher.sendPushMessage("dashboard", deviceId, getOrgMarkerOptionDetail(this.getSelectedMarkerOptionId()), this.getSelectedMarkerOptionId(),
							  "Your request:\n\""+getOrgMarkerOptionDetail(this.getSelectedMarkerOptionId())+"\" \nhas been received");

				}

				

			}

						

		catch(RsntException e){

			e.printStackTrace();

		}

	}

	

	public void loadMarkerAlerts(){

		orgMarkerOptions = new ArrayList<SelectItem>();

		try{

			List<Object[]> markerData = clientAlertService.getMarkerAlerts(new Long(this.getSelectedOrgLayoutMarkerId().substring(4)));

			for(Object[] obj: markerData){

				orgMarkerOptions.add(new SelectItem(obj[0].toString(),obj[1].toString()));

			}

		}

		catch(RsntException e){

			e.printStackTrace();

		}

		

	}

	

	private String getOrgMarkerOptionDetail(String optionId){

		for(SelectItem itm: orgMarkerOptions){

			if(itm.getValue().toString().equalsIgnoreCase(optionId)){

				return itm.getLabel();

			}

		}

		

		return null;

	}

	

	public void loadOrgWaiterData(){

		orgLayoutWaiters = new ArrayList<SelectItem>();

		try{

			final Long orgId = (Long) Contexts.getSessionContext().get(Constants.CONST_ORGID);

			List<Object[]> staffData = staffService.loadStaffData(orgId);

			for(Object[] obj: staffData){

				orgLayoutWaiters.add(new SelectItem(obj[0].toString(),obj[3].toString()+" "+obj[4].toString()));

			}

		}

		catch(RsntException e){

			e.printStackTrace();

		}

		

	}

	

	public ILayoutService getLayoutService() {

		return layoutService;

	}



	public void setLayoutService(ILayoutService layoutService) {

		this.layoutService = layoutService;

	}







	public List<OrganizationLayoutMarkers> getOrganizationLayoutMarkerList() {

		return organizationLayoutMarkerList;

	}







	public void setOrganizationLayoutMarkerList(

			List<OrganizationLayoutMarkers> organizationLayoutMarkerList) {

		this.organizationLayoutMarkerList = organizationLayoutMarkerList;

	}







	public List<SelectItem> getOrgLayoutList() {

		return orgLayoutList;

	}







	public void setOrgLayoutList(List<SelectItem> orgLayoutList) {

		this.orgLayoutList = orgLayoutList;

	}







	public String getSelectedLayoutName() {

		return selectedLayoutName;

	}







	public void setSelectedLayoutName(String selectedLayoutName) {

		this.selectedLayoutName = selectedLayoutName;

	}







	public String getSelectedOrgLayoutId() {

		log.info("Returning selectedOrgLayoutId value: "+selectedOrgLayoutId);

		return selectedOrgLayoutId;

	}







	public void setSelectedOrgLayoutId(String pselectedOrgLayoutId) {

		log.info("setSelectedOrgLayoutId "+pselectedOrgLayoutId);

		this.selectedOrgLayoutId = pselectedOrgLayoutId;

	}



	public LayoutManagerAction getLayoutManagerAction() {

		return layoutManagerAction;

	}



	public void setLayoutManagerAction(LayoutManagerAction layoutManagerAction) {

		this.layoutManagerAction = layoutManagerAction;

	}



	public List<AdsDataTO> getOrgLayoutOptionList() {

		return orgLayoutOptionList;

	}



	public void setOrgLayoutOptionList(List<AdsDataTO> orgLayoutOptionList) {

		this.orgLayoutOptionList = orgLayoutOptionList;

	}





	public Long getSelectedTaskWaiterId() {

		return selectedTaskWaiterId;

	}





	public void setSelectedTaskWaiterId(Long selectedTaskWaiterId) {

		this.selectedTaskWaiterId = selectedTaskWaiterId;

	}





	public List<SelectItem> getOrgLayoutWaiters() {

		return orgLayoutWaiters;

	}





	public void setOrgLayoutWaiters(List<SelectItem> orgLayoutWaiters) {

		this.orgLayoutWaiters = orgLayoutWaiters;

	}





	public IStaffService getStaffService() {

		return staffService;

	}





	public void setStaffService(IStaffService staffService) {

		this.staffService = staffService;

	}





	public String getSelectedMarkerOptionId() {

		return selectedMarkerOptionId;

	}





	public void setSelectedMarkerOptionId(String selectedMarkerOptionId) {

		log.info("setSelectedMarkerOptionId() called with: "+selectedMarkerOptionId);

		this.selectedMarkerOptionId = selectedMarkerOptionId;

	}





	public IClientAlertService getClientAlertService() {

		return clientAlertService;

	}





	public void setClientAlertService(IClientAlertService clientAlertService) {

		this.clientAlertService = clientAlertService;

	}





	public String getSelectedOrgLayoutMarkerId() {

		return selectedOrgLayoutMarkerId;

	}





	public void setSelectedOrgLayoutMarkerId(String selectedOrgLayoutMarkerId) {

		this.selectedOrgLayoutMarkerId = selectedOrgLayoutMarkerId;

	}





	public List<SelectItem> getOrgMarkerOptions() {

		return orgMarkerOptions;

	}





	public void setOrgMarkerOptions(List<SelectItem> orgMarkerOptions) {

		this.orgMarkerOptions = orgMarkerOptions;

	}

	

}

