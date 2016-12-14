package com.rsnt.web.action;

import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.log.Log;
import org.jboss.seam.web.IdentityRequestWrapper;

import com.rsnt.common.exception.RsntException;
import com.rsnt.entity.OrganizationUser;
import com.rsnt.entity.User;
import com.rsnt.entity.UserRole;
import com.rsnt.service.IOrganizationService;
import com.rsnt.service.IStaffService;
import com.rsnt.util.common.AppInitializer;
import com.rsnt.util.common.CommonUtil;
import com.rsnt.util.common.Constants;
import com.rsnt.util.common.EmailUtil;

@Scope(ScopeType.CONVERSATION)
@Name("staffDetailAction")
public class StaffDetailAction {

	@Logger
	private Log log;
	
	@In(value=IStaffService.SEAM_NAME,create=true)
	private IStaffService staffServiceImpl;
	
	@In(value=IOrganizationService.SEAM_NAME,create=true)
	private IOrganizationService orgServiceImpl;
	
	
	@In
    private User loginUser;
	
	private User user;
	
	@In
    private Map messages;
	
	private boolean editMode = false;
	private Long userId;
	
	private boolean addEditStaffValid;
	
	private String password;
	
	private String userType;
	
	public String addStaffData() {
		try {
			final Long orgId = (Long) Contexts.getSessionContext().get(Constants.CONST_ORGID);
			
			if(user.getEmail()!=null && user.getUserId()==null ){
				List<User> userDupList = staffServiceImpl.findUserByUserName(user.getEmail());
				if(userDupList!=null && userDupList.size()>0){
					//FacesMessages.instance().add("User Id already exists.Please choose a different User Id.");	
					final FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, messages
		                    .get("organization.user.duplicate").toString(), null);
		            FacesContext.getCurrentInstance().addMessage(null, message);
		            setAddEditStaffValid(false);
					return null;
				}
			}
			
			user.setUserName(user.getEmail());
			user.setPassword(CommonUtil.encryptPassword(user.getEmail()));//Setting default when user gets created
			
			setAddEditStaffValid(true);
			if(orgId != null) {
				user.setCreatedBy(loginUser.getUserName());
				user.setActive(false);//Once activation is done it will be set to true.
				user.setActivationId(CommonUtil.randomString(50));
				
				OrganizationUser organizationUser = new OrganizationUser();
				
				organizationUser.setCreatedBy(loginUser.getUserName());
				organizationUser.setOrganization(orgServiceImpl.getOrganization(orgId));
				
				organizationUser.setUser(user);
				
				UserRole userRole = new UserRole();
				
				if(userType.equals("Staff")){
					userRole.setRoleId(new Long(Constants.CONT_LOOKUP_ROLE_RSNTSTAFF));
				}else{
					userRole.setRoleId(new Long(Constants.CONT_LOOKUP_ROLE_RSNTMANAGER));
				}
				userRole.setUser(user);
				
				user.setUserRole(userRole);
				
				
				staffServiceImpl.persist(organizationUser);
				
				final IdentityRequestWrapper identityRequestWrapper = (IdentityRequestWrapper) FacesContext
		         .getCurrentInstance().getExternalContext().getRequest();
				 final HttpServletRequest request = (HttpServletRequest) identityRequestWrapper.getRequest();
				 
				 log.info("Check RequestURL: "+request.getRequestURL());
				 
				 String activationLink =  AppInitializer.domainNameUrl+request.getContextPath()+"/external/resetUserPasswordExt.seam?actId="+user.getActivationId();
				 String activationUrl = "<a href='"+activationLink+"'  target='_blank'>Click here </a>";
				 //String msgBody = 	"<br/>"+  activationUrl+ " to set your account password <br/>"+ AppInitializer.rmsThankYou;
				 
				 StringBuffer staffDataHdr = new StringBuffer("Hi "+user.getFirstName()+"<br/>");
				 staffDataHdr.append(AppInitializer.staffActTextPart1);
				 staffDataHdr.append("<br/><br/>"+  activationUrl+ " to set your account password <br/><br/>");
				 staffDataHdr.append(AppInitializer.staffActTextPart2 + "<br/>"+AppInitializer.rmsThankYou);
				 
				/* String appLinkAppleUrl = AppInitializer.appLinkAppleText+" "+"<a href='"+AppInitializer.appLinkApple+"'  target='_blank'>Click Here</a>";
				 String appLinkAndroidUrl = AppInitializer.appLinkAndroidText+" "+"<a href='"+AppInitializer.appLinkAndroid+"'  target='_blank'>Click Here</a>";
				 
				 String residualIncUrl = AppInitializer.residualIncText+" "+"<a href='"+AppInitializer.residualInc+"'  target='_blank'>Click Here</a>";*/
					
				 String appLinkAppleUrl = "<a href='"+AppInitializer.appLinkApple+"'  target='_blank'><img src=\""+AppInitializer.domainNameUrl+AppInitializer.appImangeExtPath+"/"+AppInitializer.appLinkImg+"\"/></a>";
				 String appLinkAndroidUrl = "<a href='"+AppInitializer.appLinkAndroid+"'  target='_blank'><img src=\""+AppInitializer.domainNameUrl+AppInitializer.appImangeExtPath+"/"+AppInitializer.androidLinkImg+"\"/></a>";
					
				 String msgBody = staffDataHdr.toString()+"<br/><br/>"  + appLinkAppleUrl + "<br/><br/>"
		 			+ appLinkAndroidUrl  +"<br/><br/>"+AppInitializer.rmsThankYou;
				    
				 
				 String userEmail = !(AppInitializer.defaultUserEmail.equalsIgnoreCase(""))?AppInitializer.defaultUserEmail:user.getEmail();
				 log.info("Printing msgBody "+msgBody+" to be sent to "+userEmail);	
						
				 EmailUtil emailUtil = (EmailUtil) Component.getInstance(EmailUtil.class);
					try {
						emailUtil.prepareMessageAndSend(userEmail,AppInitializer.defaultEmailSubject+" Staff User Password Update ", msgBody);
					} catch (RsntException e) {
						e.printStackTrace();
					}
					
				
			}
			return "success";
			
		} catch (RsntException re) {
			log.error("StaffDetailAction.addStaffData()", re);
			setAddEditStaffValid(false);
			final FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, messages
                    .get("add.staff.failed").toString(), null);
            FacesContext.getCurrentInstance().addMessage(null, message);
		}
		return null;
	}
	
	public void fetchStaff() {
		try {
			if(userId != null) {
				user = (User) staffServiceImpl.find(userId);
			}
		} catch (RsntException re) {
			log.error("StaffDetailAction.fetchStaffData()", re);
			final FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, messages
                    .get("fetch.staff.failed").toString(), null);
            FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}
	
	public String updateStaffData() {
		try {
			
		/*	if(user.getUserName()!=null){
				User userDup = staffServiceImpl.findUserByUserName(user.getUserName());
				if(userDup!=null){
					//FacesMessages.instance().add("User Id already exists.Please choose a different User Id.");	
					final FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, messages
		                    .get("organization.user.duplicate").toString(), null);
		            FacesContext.getCurrentInstance().addMessage(null, message);
		            setAddEditStaffValid(false);
					return null;
				}
			}*/
			
			if(user != null) {
				staffServiceImpl.merge(user);
			}
			this.setAddEditStaffValid(true);
			return "success";
			
		} catch (RsntException re) {
			log.error("StaffDetailAction.updateStaffData()", re);
			final FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, messages
                    .get("update.staff.failed").toString(), null);
            FacesContext.getCurrentInstance().addMessage(null, message);
            setAddEditStaffValid(false);
		}
		return null;
	}
	
	public void deactivateStaffData() {
		try {
			if(userId != null) {
				user = (User) staffServiceImpl.find(userId);
				if(user.getUserId().intValue() == loginUser.getUserId().intValue()){
					final FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, messages
		                    .get("delete.staff.self").toString(), null);
		            FacesContext.getCurrentInstance().addMessage(null, message);
		            return;
					
				}
				else{
					user.setActive(!user.isActive());
					staffServiceImpl.merge(user);
					final FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, messages
		                    .get("delete.staff.success").toString(), null);
		            FacesContext.getCurrentInstance().addMessage(null, message);
				}
				
			}
		} catch (RsntException re) {
			log.error("StaffDetailAction.deactivateStaffData()", re);
			final FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, messages
                    .get("delete.staff.failed").toString(), null);
            FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}
	
	public void initializeObject() {
		user = new User();
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public IStaffService getStaffServiceImpl() {
		return staffServiceImpl;
	}

	public void setStaffServiceImpl(IStaffService staffServiceImpl) {
		this.staffServiceImpl = staffServiceImpl;
	}

	public boolean isEditMode() {
		return editMode;
	}

	public void setEditMode(boolean editMode) {
		this.editMode = editMode;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public IOrganizationService getOrgServiceImpl() {
		return orgServiceImpl;
	}

	public void setOrgServiceImpl(IOrganizationService orgServiceImpl) {
		this.orgServiceImpl = orgServiceImpl;
	}

	public boolean isAddEditStaffValid() {
		return addEditStaffValid;
	}

	public void setAddEditStaffValid(boolean addEditStaffValid) {
		this.addEditStaffValid = addEditStaffValid;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}
	
	
}
