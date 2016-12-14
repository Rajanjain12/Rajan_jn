package com.rsnt.web.rest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import net.sf.json.JSONObject;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

import com.rsnt.common.exception.RsntException;
import com.rsnt.entity.Organization;
import com.rsnt.service.IOrganizationService;
import com.rsnt.util.common.AppInitializer;
import com.rsnt.util.common.CommonUtil;
import com.rsnt.util.transferobject.AdsDataTO;

@Path("/organizationRestAction")
@Name("organizationRestAction")
public class OrganizationRestAction {

	@Logger
	private Log log;
	
	@In(value=IOrganizationService.SEAM_NAME, create = true)
	private IOrganizationService organizationService;
	
	
	/**
	 * Get Logo Organization 
	 * @param orgid JSON Long
	 * @return Response - imgOrganization
	 */
	
	@GET
	@Path("/imgOrganization/{orgid}")
	@Produces({ "image/jpg" })
	public Response imgOrganization(@PathParam("orgid") Long orgid) {
		System.out.println("organization id: " + orgid);
		byte[] ca = null;
		Response.ResponseBuilder builder = null;
		Organization organization =  null;
		try {
			organization =  organizationService.getOrganization(orgid);
			
			//setup logo file system
			byte[] logoFileCheck = null;
			String orgPath = AppInitializer.orgLogoPath;
			System.out.println("orgPath: " + orgPath);
			File fileLogo = null;
			try {
				if(!organization.getLogoFileName().equals(null) || !organization.getLogoFileName().equals("")){
					fileLogo = new File(orgPath + File.separator + organization.getOrganizationId()+".png");
				}else{
					fileLogo = new File(orgPath + File.separator + "default-logo.png");
				}
				System.out.println("Path File Logo: " + fileLogo.getAbsolutePath());
				logoFileCheck = readBytesFromFile(fileLogo);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				System.out.println("Error in getLogoFile: " + e.getMessage());
				//e.printStackTrace();
				try {
					fileLogo = new File(orgPath + File.separator + "default-logo.png");
					logoFileCheck = readBytesFromFile(fileLogo);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					return null;
				}
			}
			ca = logoFileCheck;
			
			
			//setup logo column table (database)
			//ca = organization.getLogo();
			
			
			builder = Response.status(Response.Status.OK).entity(ca)
					.header("Accept-Ranges", "byte").header("Age", 0)
					.header("Connection", "close")
					.header("Expires", "Sat, 07 Mar 2016 17:42:36 GMT");
			
		}catch (Exception e) {
			e.printStackTrace();
        	log.error("Error imgOrganization : " + e.getMessage(), e);
			builder = Response.status(Response.Status.NOT_FOUND);
		}
		return builder.build();
	}
	

	public static byte[] readBytesFromFile(File file) throws IOException {
	      InputStream is = new FileInputStream(file);
	      
	      // Get the size of the file
	      long length = file.length();
	  
	      // You cannot create an array using a long type.
	      // It needs to be an int type.
	      // Before converting to an int type, check
	      // to ensure that file is not larger than Integer.MAX_VALUE.
	      if (length > Integer.MAX_VALUE) {
	        throw new IOException("Could not completely read file " + file.getName() + " as it is too long (" + length + " bytes, max supported " + Integer.MAX_VALUE + ")");
	      }
	  
	      // Create the byte array to hold the data
	      byte[] bytes = new byte[(int)length];
	  
	      // Read in the bytes
	      int offset = 0;
	      int numRead = 0;
	      while (offset < bytes.length && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
	          offset += numRead;
	      }
	  
	      // Ensure all the bytes have been read in
	      if (offset < bytes.length) {
	          throw new IOException("Could not completely read file " + file.getName());
	      }
	  
	      // Close the input stream and return bytes
	      is.close();
	      return bytes;
	}
	
	@GET
    @Path("/loadOrgReportData")
    @Produces(MediaType.APPLICATION_JSON)
    public String loadOrgReportData(@QueryParam("createdOrgs") String createdOrgFlag){
		final Map<String, Object> rootMap = new LinkedHashMap<String, Object>();
    	final List<String> errorArray = new ArrayList<String>(0);
    	final List<String> dataArray = new ArrayList<String>(0);
    	
    	try {
    		List<Object[]> orgList = null;
    		if(createdOrgFlag!=null && createdOrgFlag.equalsIgnoreCase("Y")){
    			orgList = organizationService.loadOrgReportData(true);
    		}
    		else{
    			orgList = organizationService.loadOrgReportData(false);
    		}
    		
    		AdsDataTO adDataTO= null;
    		List<AdsDataTO> adDataTOList = new ArrayList<AdsDataTO>();
    		
    		for(Object[] obj: orgList){
    			adDataTO = new AdsDataTO();
    			adDataTO.setAdsId(obj[0].toString());
    			adDataTO.setTitle(obj[1].toString());
    			Date date  = (Date)obj[2];
    			adDataTO.setEndDate(CommonUtil.convertDateToFormattedString(date,"yyyy-MM-dd"));
    			adDataTOList.add(adDataTO);
    		}
    		
    		rootMap.put("id", -1);
        	rootMap.put("error", "");
        	rootMap.put("fieldErrors", errorArray);
        	rootMap.put("data", dataArray);
        	rootMap.put("aaData",adDataTOList);
        	
        	final JSONObject jsonObject = JSONObject.fromObject(rootMap);
        	return jsonObject.toString();
        	
    	}catch (RsntException e) {
    		e.printStackTrace();
    		log.error("AdsDetailRestAction.loadAdsData() - failed:", e.getMessage());
    		
    		rootMap.put("id", -1);
        	rootMap.put("error", "System Error - load Ads data failed");
        	rootMap.put("fieldErrors", errorArray);
        	rootMap.put("data", dataArray);
        	
        	final JSONObject jsonObject = JSONObject.fromObject(rootMap);
        	return jsonObject.toString();
    	}
	}


	public IOrganizationService getOrganizationService() {
		return organizationService;
	}


	public void setOrganizationService(IOrganizationService organizationService) {
		this.organizationService = organizationService;
	}
	
}
