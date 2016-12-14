package com.rsnt.web.rest;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.xml.ws.ResponseWrapper;
import javax.xml.ws.WebServiceContext;

import net.sf.json.JSONObject;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.core.Manager;
import org.jboss.seam.log.Log;

import com.rsnt.common.exception.RsntException;
import com.rsnt.entity.OrganizationLayoutMarkers;
import com.rsnt.service.ILayoutService;
import com.rsnt.service.ISecurityService;
import com.rsnt.util.common.AppInitializer;
import com.rsnt.util.common.CommonUtil;
import com.rsnt.util.common.Constants;
import com.rsnt.util.transferobject.DashBoardDataTO;
import com.rsnt.util.transferobject.LayoutDataTO;
import com.rsnt.util.transferobject.LayoutTableInfoTO;
import com.rsnt.util.transferobject.MapplicCatagryDataTO;
import com.rsnt.util.transferobject.MapplicLevelDataTO;
import com.rsnt.util.transferobject.MapplicLocaionsDataTO;

@Path("/layoutManagerRestAction")
@Name("layoutManagerRestAction")
public class LayoutManagerRestAction {
	
    @Logger
    private Log log;
    
    @In(value=ILayoutService.SEAM_NAME,create=true)
    private ILayoutService layoutService;
    
	@In(create= true)
	private ISecurityService securityService;
	
	@In
    private Map messages;
	
	
	@Resource
	private WebServiceContext context;
    /*@GET
    @Path("/getLayoutData")
    @Produces(MediaType.APPLICATION_JSON)
    public JSONResult<LayoutDataTO> populateLayoutData(@QueryParam("userId") String userId){
    	
    	log.info("Receeived UserId "+userId);
    	
    	List<LayoutDataTO> layoutDataTOList = new ArrayList<LayoutDataTO>();
    	
    	LayoutDataTO dataTO = new LayoutDataTO();
    	dataTO.setTest("Rest Test");
    	
    	layoutDataTOList.add(dataTO);
    	
    	return new JSONResult<LayoutDataTO>(layoutDataTOList,layoutDataTOList.size());
    }*/
    
  /*  @GET
    @Path("/getLayoutData")
    @Produces(MediaType.APPLICATION_JSON)
    public List<LayoutDataTO> populateLayoutData(@QueryParam("userId") String userId){
    	
    	log.info("Receeived UserId "+userId);
    	
    	List<LayoutDataTO> layoutDataTOList = new ArrayList<LayoutDataTO>();
    	
    	LayoutDataTO dataTO = new LayoutDataTO();
    	dataTO.setTest("Rest Test");
    	
    	layoutDataTOList.add(dataTO);
    	
    	return layoutDataTOList;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/processCallServer")
    public String processCallServer(){
    
    	return "{\"status\": \"success\"}";
    }*/
    /**
     * This method loads all the saved layouts for the Organization from ORGANIZATION_LAYOUT Table.
     */
    @GET
    @Path("/loadLayoutData")
    @Produces(MediaType.APPLICATION_JSON)
    public String loadLayoutData(){
    	
    	List<LayoutDataTO> layoutDataTOList = new ArrayList<LayoutDataTO>();
    	LayoutDataTO dataTO = null;
    	try{
    		
    		List<Object[]> layoutDataObjList = layoutService.loadAllOrgLayouts();
    		
    		for(Object[] obj: layoutDataObjList){
    			
    			dataTO = new LayoutDataTO();
    			
    			dataTO.setOrgLayoutId(obj[0].toString());
    			dataTO.setDescription(obj[2].toString());
    			dataTO.setLayoutCapacity(obj[3].toString());
    			dataTO.setIsDefault(CommonUtil.getYesNoFlag(obj[8]));
    			dataTO.setActiveFlag(CommonUtil.getYesNoFlag(obj[9]));
    			dataTO.setCreatedBy(obj[4].toString());
    			dataTO.setCreatedAt(CommonUtil.convertDateToFormattedString((Date)obj[5], "dd-MMM-yy"));
    			
    			layoutDataTOList.add(dataTO);
    		}
    		
    		final Map<String, Object> rootMap = new LinkedHashMap<String, Object>();
        	final List<String> errorArray = new ArrayList<String>(0);
        	final List<String> dataArray = new ArrayList<String>(0);
        	rootMap.put("id", -1);
        	rootMap.put("error", "");
        	rootMap.put("fieldErrors", errorArray);
        	rootMap.put("data", dataArray);
        	rootMap.put("aaData",layoutDataTOList );
        	
        	final JSONObject jsonObject = JSONObject.fromObject(rootMap);
        	log.info(jsonObject.toString());
        	return jsonObject.toString();
    		
    	}
    	catch(Exception e){
    		e.printStackTrace();
    	}
    	
    	return null;
    	
		
    }
    
    /**
     * This method loads the list of tables for the selected layout. On create also this creates new tables and returns the list
     * @return
     */
    @GET
    @Path("/loadLayoutMarkerData")
    @Produces(MediaType.APPLICATION_JSON)
    public String loadLayoutMarkerData(@QueryParam("orgLayoutHeaderId") final String orgLayoutHeaderId, 
    		@QueryParam("userId") String userId, @QueryParam("objectName") String objectName){
    	
    	try{
    		List<OrganizationLayoutMarkers> orgMarkerList = layoutService.loadLayoutMarkerData(Long.valueOf(orgLayoutHeaderId), false);
    		
    		Long markerEditAccess = securityService.getRoleProtectedObjectForUser(new Long(userId), objectName);
    		
    		List<LayoutTableInfoTO> orgMarkerTOList = new ArrayList<LayoutTableInfoTO>();
    		LayoutTableInfoTO infoTO=null;
    		
    		for(OrganizationLayoutMarkers marker: orgMarkerList){
    			
    			infoTO = new LayoutTableInfoTO();
    			infoTO.setOrganizationLayoutMarkerId(marker.getOrganizationLayoutMarkerId().toString());
    			infoTO.setTableName(marker.getLayoutMarkerCode());
    			infoTO.setQrCodeGenerated((marker.isLayoutMarkerQRCodeGenerated())? "Y":"N");
    			infoTO.setActiveStatus((marker.isActive())? "Y":"N");
    			infoTO.setAccess(markerEditAccess);
    			orgMarkerTOList.add(infoTO);
    			
    		}
    		
    	
    		
    		final Map<String, Object> rootMap = new LinkedHashMap<String, Object>();
        	final List<String> errorArray = new ArrayList<String>(0);
        	final List<String> dataArray = new ArrayList<String>(0);
        	rootMap.put("id", -1);
        	rootMap.put("error", "");
        	rootMap.put("fieldErrors", errorArray);
        	rootMap.put("data", dataArray);
        	rootMap.put("aaData",orgMarkerTOList );
        	
        	final JSONObject jsonObject = JSONObject.fromObject(rootMap);
        	
        	return jsonObject.toString();
        	
    	}
    	catch(RsntException e){
    		e.printStackTrace();
    		return null;
    	}
    	
    	
    }
   
    @GET
    @Path("/getDashboardMapplicObject")
    @Produces(MediaType.APPLICATION_JSON)
    public @ResponseWrapper String loadDashboardDataUsingMaplicAPI(@QueryParam("layoutId") String layoutId){
    	final Map<String, Object> rootMap = new LinkedHashMap<String, Object>();
    	final String Filename =  Contexts.getSessionContext().get(Constants.CONST_ORGNAME).toString().concat("-").concat(Contexts.getSessionContext().get(Constants.CONST_ORGID).toString());
    	//String ctxPath = ctx.getContextPath();
    	//String viewpath = Contexts.getApplicationContext().get("servletContext").toString(); 
    	final String filePath = AppInitializer.importPath+"/"+Filename+".json";
    	System.out.println("filepath"+filePath);
    	 //String rootPath = ServletContextEvent.getServletContext().getRealPath("/");  
         //System.out.println("##################" + rootPath);  
    	
    	
    	String left;
    	String top;
    	double leftinit = 5;
    	double topinit = 5;
    	int factor = 10;
    	int markersInARow = 8;
    	DashBoardDataTO mapplicObject;
    	 mapplicObject = new DashBoardDataTO();
		 mapplicObject.setMapheight(String.valueOf(1250));
		 mapplicObject.setMapwidth(String.valueOf(1250));
		
		//catagory
			List<MapplicCatagryDataTO> mapplicCataogryObjList =  new ArrayList<MapplicCatagryDataTO>();
			MapplicCatagryDataTO mapplicCataogryObj = new MapplicCatagryDataTO();
			mapplicCataogryObj.setId("TableMarkets");
			mapplicCataogryObj.setTitle("TableMarkets");
			mapplicCataogryObj.setColor("#4cd3b8");
			mapplicCataogryObj.setShow("true");
			mapplicCataogryObjList.add(mapplicCataogryObj);
			mapplicObject.setCategories(mapplicCataogryObjList);
			
			//level
			List<MapplicLevelDataTO> mapplicLevelObjList = new ArrayList<MapplicLevelDataTO>();
			MapplicLevelDataTO mapplicLevelObj;
			List<MapplicLocaionsDataTO> mapplicLocationObjList = null;
			MapplicLocaionsDataTO mapplicLocationObj = new MapplicLocaionsDataTO();
			List<OrganizationLayoutMarkers> orgMarkerList;
			
    	try{
    		List<Object[]> layoutDataObjList = layoutId == null ? layoutService.loadAllOrgLayouts() : layoutService.loadDashboardDataUsingByLayoutId(layoutId);;
	         for(Object[] obj: layoutDataObjList){
	        	 mapplicLevelObj =  new MapplicLevelDataTO();
					mapplicLevelObj.setId(obj[0].toString());
				
					mapplicLevelObj.setName(obj[2].toString());
					mapplicLevelObj.setTitle(obj[2].toString());
					mapplicLevelObj.setMap("");
					mapplicLevelObj.setMinimap("");
	        	 orgMarkerList = new ArrayList<OrganizationLayoutMarkers>();
	        	 mapplicLocationObjList = new ArrayList<MapplicLocaionsDataTO>();
	        	 orgMarkerList = layoutService.loadLayoutMarkerData(Long.valueOf(obj[0].toString()), false);
	        		if(orgMarkerList.size() > 0)
					{
	        			int index = 0;
						for(OrganizationLayoutMarkers layoutmarkers:orgMarkerList)
						{

							mapplicLocationObj =  new MapplicLocaionsDataTO();
							mapplicLocationObj.setId(String.valueOf(layoutmarkers.getOrganizationLayoutMarkerId()));
							mapplicLocationObj.setTitle(layoutmarkers.getLayoutMarkerCode());
							mapplicLocationObj.setAbout(layoutmarkers.getLayoutMarkerCode());
							mapplicLocationObj.setCategory("TableMarkets");
							mapplicLocationObj.setDescription(layoutmarkers.getLayoutMarkerCode());
							
							System.out.println("stylepos::"+layoutmarkers.getLayoutMarkerStylePos());
							if(layoutmarkers.getLayoutMarkerStylePos() != null)
							{
							String[] tokens = layoutmarkers.getLayoutMarkerStylePos().split(";");
							String[] toptoken = tokens[0].split(":");
							String[] lefttoken = tokens[1].split(":");
							
							//Modified by Aditya
							int indexOfTopToken = toptoken[1].indexOf("%") > 0 ? toptoken[1].indexOf("%") : toptoken[1].indexOf("p");
							int indexOfLeftToken = lefttoken[1].indexOf("%") > 0 ? lefttoken[1].indexOf("%") : lefttoken[1].indexOf("p");
							
							top = toptoken[1].substring(0,indexOfTopToken);
							left = lefttoken[1].substring(0,indexOfLeftToken);
							//End Modified by Aditya

							mapplicLocationObj.setX(Double.parseDouble(left));
							mapplicLocationObj.setY(Double.parseDouble(top));
							}
							else
							{ 
									/*width = leftinit + 20;
									height = topinit + 20;*/
									
									mapplicLocationObj.setY(leftinit+(index/markersInARow) * factor);	//(((height * (index++/16)))/1250);
									mapplicLocationObj.setX(topinit+(index%markersInARow)* factor);	//(((width * (index++%16)))/1250);
									index++;
								
								
							}
							mapplicLocationObjList.add(mapplicLocationObj);
						}
						mapplicLevelObj.setLocations(mapplicLocationObjList);
						mapplicLevelObjList.add(mapplicLevelObj);
					}
	        		 
    		         
	         
    	         
	        		
			} 
	         //Modified by Aditya
	         mapplicObject.setLevels(mapplicLevelObjList);
	       
	    /*    
	        File file = new File(filePath);
		     
     		if (!file.exists()) {
 				file.createNewFile();
 				
 			}
     		else
     		{
     			file.delete();
     			file.createNewFile();
     		}
	            FileWriter fw = new FileWriter(file.getAbsoluteFile());
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(JSONObject.fromObject(mapplicObject).toString());
				bw.close();*/

				
	         rootMap.put("id", -1);
	         
	     	rootMap.put("Filepath",filePath);
	     	rootMap.put("Filename",Filename);
	     	log.info("Done"+ JSONObject.fromObject(mapplicObject).toString());
	      	return JSONObject.fromObject(mapplicObject).toString();
    			
    		}
    	

        	
    	
    	catch(Exception e){
    		e.printStackTrace();
    		return null;
    	}
    	
    	
    	
    }
    
    @POST
    @Path("/saveLayoutPositionData")
    public void saveLayoutPositionData(@QueryParam("cid") String conversationId, @FormParam("layoutDataList[]") List<String> testObj){
    	
    	String data = testObj.toString();
    	try{
    		 Manager.instance().setCurrentConversationId(conversationId);
    		layoutService.saveLayoutMarkerPositions(data);
    		 Manager.instance().leaveConversation();
    		
    	}
    	catch(Exception e){
    		e.printStackTrace();
    	}
    }
    
    @GET
    @Path("/getDashboardAlertsForLayout")
    @Produces(MediaType.APPLICATION_JSON)
    public String getDashboardAlertsForLayout(@QueryParam("orgLayoutId") String pOrgLayoutId){
    
    	final Map<String, Object> rootMap = new LinkedHashMap<String, Object>();
    	List<String> btnIdList = new ArrayList<String>();
    	try{
    		List<Object[]> alertDataList = layoutService.getDashBoardAlertData(new Long(pOrgLayoutId));
    		
    		
    		for(Object[] inn: alertDataList){
    			//btnIdList.add(obj[0].toString()+"-"+obj[1].toString());
    			btnIdList.add(inn[0]+"-"+inn[1]);
    		}    		
    	}
    	catch(RsntException e){    		
    		e.printStackTrace();
    	}        
    	rootMap.put("id", -1);
    	rootMap.put("aaData",btnIdList );    	
    	System.out.println("rootmapdata::"+rootMap);    	    	             
    	System.out.println("rootmapdata::"+rootMap);
    	
    	final JSONObject jsonObject = JSONObject.fromObject(rootMap);
    	return jsonObject.toString();
    }
    
    
    /**
	 * Get Event Listener Configuration details 
	 * @return Map<String, String> 
	 */
	@GET
	@Path("/pusgerinformation")
	public String getPushEventsConfigDetails(){
		Map<String, String> eventConfig = new HashMap<String, String>();
		eventConfig.put(Constants.REALTIME_APPLICATION_KEY, System.getProperty(Constants.REALTIME_APPLICATION_KEY_VAL));
		eventConfig.put(Constants.REALTIME_PRIVATE_KEY, System.getProperty(Constants.REALTIME_PRIVATE_KEY_VAL));
		eventConfig.put(Constants.PUSHER_CHANNEL_ENV, AppInitializer.dashBoardChannel+"_"+Contexts.getSessionContext().get(Constants.CONST_ORGID));
		final JSONObject jsonObject = JSONObject.fromObject(eventConfig);
		return jsonObject.toString();
	}
     
   /* @POST
    @Path("/saveLayoutJSON2")
    public void saveLayoutJSON2(TableWrapperForm tableWrapperForm){
    	log.info(tableWrapperForm);
    }

    @POST
    @Path("/saveLayoutString2")
    @Produces(MediaType.APPLICATION_JSON)
    public String saveLayoutString2(@Form TableWrapperForm tableWrapperForm){
    	log.info(tableWrapperForm);
    	
    	return "success";
    }*/
	public ILayoutService getLayoutService() {
		return layoutService;
	}

	public void setLayoutService(ILayoutService layoutService) {
		this.layoutService = layoutService;
	}

	public ISecurityService getSecurityService() {
		return securityService;
	}

	public void setSecurityService(ISecurityService securityService) {
		this.securityService = securityService;
	}
	
	

}
