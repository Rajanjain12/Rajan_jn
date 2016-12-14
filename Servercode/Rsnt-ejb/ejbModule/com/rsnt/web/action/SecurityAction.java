package com.rsnt.web.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.model.SelectItem;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.log.Log;

import com.rsnt.common.exception.RsntException;
import com.rsnt.common.service.ILookupService;
import com.rsnt.entity.User;
import com.rsnt.service.ISecurityService;
import com.rsnt.util.common.Constants;

@Name("securityAction")
@Scope(ScopeType.PAGE)
public class SecurityAction {

	
	@Logger
	private Log log;
	
	@In
    private User loginUser;
	

	@In
    private Map messages;
	
	@In(value=ISecurityService.SEAM_NAME,create=true)
	private ISecurityService securityService;

	@In(value=ILookupService.SEAM_NAME,create=true)
	private ILookupService lookupService;
	
	private String selectedRoleId;
	private List<SelectItem> roleList = new ArrayList<SelectItem>();
	
	private List<SelectItem> allProtectedObjList = new ArrayList<SelectItem>();

	private List<String> roleProtectedObjList = new ArrayList<String>();
	
	public void initialize(){
		try{
			roleList = new ArrayList<SelectItem>();
			
			List<Object[]> allLayoutObj =  lookupService.getLookupsForLookupType(new Long(Constants.CONT_LOOKUPTYPE_ROLE));
			for(Object[] obj: allLayoutObj){
				if(!obj[0].toString().equalsIgnoreCase(String.valueOf((Constants.CONT_LOOKUP_ROLE_PRDADMIN)))){
					roleList.add(new SelectItem(obj[0].toString(),obj[1].toString()));
				}
				
			}
			roleList.add(new SelectItem(0,"Select.."));
			setSelectedRoleId("0");
			
			List<Object[]> allProtObj = securityService.getAllProtectedObject();
			for(Object[] obj: allProtObj){
				allProtectedObjList.add(new SelectItem(obj[0].toString(),obj[1].toString()));
			}
			
		}
		catch(RsntException e){
			e.printStackTrace();
		}
		
	}
	
	public void loadProtectedObjectForRole(){
		try{
			if(!this.getSelectedRoleId().equalsIgnoreCase("0")){
				log.info("loadProtectedObject() called for : "+this.getSelectedRoleId());
				roleProtectedObjList = new ArrayList<String>();
				List<Object[]> poList =  securityService.getRoleProtectedObjectForRole(new Long(this.getSelectedRoleId()));
				for(Object[] obj: poList){
					roleProtectedObjList.add(new String(obj[0].toString()));
				}
			}
		}
		catch(RsntException e){
			e.printStackTrace();
		}
		
	}
	
	public void updateRoleProtectedObjectMapping(){
		
		try{
			if(this.getSelectedRoleId()!=null && roleProtectedObjList!=null){
				securityService.deleteRoleProtectedObjectMapping(new Long(this.getSelectedRoleId()));
				securityService.addRoleProtectedObjMapping(new Long(this.getSelectedRoleId()), roleProtectedObjList, loginUser.getUserName());
				
				final FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, messages
	                    .get("rolepermissions.update.success").toString(), null);
	            FacesContext.getCurrentInstance().addMessage(null, message);
			}
			
		}
		catch(RsntException e){
			log.error("SecurityAction.updateRoleProtectedObjectMapping()", e);
			final FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, messages
                    .get("rolepermissions.update.fail").toString(), null);
            FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}
	
	public void navigateForUserRole() {
	
        try {
        	Long roleId= (Long)Contexts.getSessionContext().get(Constants.CONST_USERROLEID);
        	log.info("navigate called for roleID: "+roleId);
        	String redirectPage = null;
        	if(roleId.intValue() == Constants.CONT_LOOKUP_ROLE_RSNTADMIN || roleId.intValue() == Constants.CONT_LOOKUP_ROLE_RSNTSTAFF
        			|| roleId.intValue() == Constants.CONT_LOOKUP_ROLE_RSNTMANAGER)
        		redirectPage = "dashboard.seam";
        	else if(roleId.intValue() == Constants.CONT_LOOKUP_ROLE_PRDADMIN)
        		redirectPage = "adminHome.seam";
        	
        	FacesContext.getCurrentInstance().getExternalContext().redirect(redirectPage);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
	}
	  
	

	public User getLoginUser() {
		return loginUser;
	}


	public void setLoginUser(User loginUser) {
		this.loginUser = loginUser;
	}



	public ISecurityService getSecurityService() {
		return securityService;
	}



	public void setSecurityService(ISecurityService securityService) {
		this.securityService = securityService;
	}



	public ILookupService getLookupService() {
		return lookupService;
	}



	public void setLookupService(ILookupService lookupService) {
		this.lookupService = lookupService;
	}



	public List<SelectItem> getRoleList() {
		return roleList;
	}



	public void setRoleList(List<SelectItem> roleList) {
		this.roleList = roleList;
	}



	public String getSelectedRoleId() {
		return selectedRoleId;
	}



	public void setSelectedRoleId(String selectedRoleId) {
		this.selectedRoleId = selectedRoleId;
	}

	public List<String> getRoleProtectedObjList() {
		return roleProtectedObjList;
	}

	public void setRoleProtectedObjList(List<String> roleProtectedObjList) {
		this.roleProtectedObjList = roleProtectedObjList;
	}

	public List<SelectItem> getAllProtectedObjList() {
		return allProtectedObjList;
	}

	public void setAllProtectedObjList(List<SelectItem> allProtectedObjList) {
		this.allProtectedObjList = allProtectedObjList;
	}
	
	
}
