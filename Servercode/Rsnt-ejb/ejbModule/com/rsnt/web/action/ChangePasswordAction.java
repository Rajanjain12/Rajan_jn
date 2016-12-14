package com.rsnt.web.action;

import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

import com.rsnt.common.exception.RsntException;
import com.rsnt.entity.User;
import com.rsnt.service.IStaffService;
import com.rsnt.util.common.AppInitializer;
import com.rsnt.util.common.CommonUtil;
import com.rsnt.util.common.EmailUtil;

@Name("changePasswordAction")
@Scope(ScopeType.PAGE)
public class ChangePasswordAction {

	private String oldPassword;
	private String newPassword;
	private String confirmPassword;
	
	@Logger
	private Log log;
	
	@In
    private User loginUser;
	

	@In
    private Map messages;
	
	
	@In(value=IStaffService.SEAM_NAME,create=true)
	private IStaffService staffServiceImpl;
	
	
	public void changePassword(){
		try{
			String oldPasswordHash = CommonUtil.encryptPassword(this.getOldPassword());
			String newPasswordHash = CommonUtil.encryptPassword(this.getNewPassword());
			
			if(loginUser.getPassword().equals(oldPasswordHash)){
				if(!oldPasswordHash.equals(newPasswordHash)){
					
					staffServiceImpl.updatePassword(newPasswordHash, loginUser.getUserId());
					final FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, messages
		                    .get("changePassword.update.success").toString(), null);
					loginUser.setPassword(this.getNewPassword());
		            FacesContext.getCurrentInstance().addMessage(null, message);
		            
		            String changePasswordHdr = "Hi "+loginUser.getFirstName()+", <br/>This is to confirm that your KyoBee password has been changed.  If you did not authorize this, please contact us right away.<br/>";
		           // String msgBody = changePasswordHdr +AppInitializer.rmsThankYou;
		            
		            /*String appLinkAppleUrl = AppInitializer.appLinkAppleText+" "+"<a href='"+AppInitializer.appLinkApple+"'  target='_blank'>Click Here</a>";
		            String appLinkAndroidUrl = AppInitializer.appLinkAndroidText+" "+"<a href='"+AppInitializer.appLinkAndroid+"'  target='_blank'>Click Here</a>";
					 
		            String residualIncUrl = AppInitializer.residualIncText+" "+"<a href='"+AppInitializer.residualInc+"'  target='_blank'>Click Here</a>";*/
		        	
					 String appLinkAppleUrl = "<a href='"+AppInitializer.appLinkApple+"'  target='_blank'><img src=\""+AppInitializer.domainNameUrl+AppInitializer.appImangeExtPath+"/"+AppInitializer.appLinkImg+"\"/></a>";
					 String appLinkAndroidUrl = "<a href='"+AppInitializer.appLinkAndroid+"'  target='_blank'><img src=\""+AppInitializer.domainNameUrl+AppInitializer.appImangeExtPath+"/"+AppInitializer.androidLinkImg+"\"/></a>";
				
		            
		            String msgBody = changePasswordHdr.toString()+"<br/><br/>"  + appLinkAppleUrl + "<br/><br/>"
		 			+ appLinkAndroidUrl  +"<br/><br/>" +AppInitializer.rmsThankYou;
		            
		            String userEmail = !(AppInitializer.defaultUserEmail.equalsIgnoreCase(""))?AppInitializer.defaultUserEmail:loginUser.getEmail();
		            log.info("Printing msgBody "+msgBody+" to be sent to "+userEmail);	
		            
		            EmailUtil emailUtil = (EmailUtil) Component.getInstance(EmailUtil.class);
					try {
						emailUtil.prepareMessageAndSend(userEmail,AppInitializer.defaultEmailSubject+" Password is updated", msgBody);
					} catch (RsntException e) {
						e.printStackTrace();
					}
					
				}else{
					final FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, messages
		                    .get("changePassword.validate.failed").toString(), null);
		            FacesContext.getCurrentInstance().addMessage(null, message);
				}
			}
			else{
				final FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, messages
	                    .get("changePassword.currentPassword.failed").toString(), null);
	            FacesContext.getCurrentInstance().addMessage(null, message);
			}
		}
		catch(RsntException e){
			
		}
			
			
		
	}
	
	public String getOldPassword() {
		return oldPassword;
	}
	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	public IStaffService getStaffServiceImpl() {
		return staffServiceImpl;
	}
	public void setStaffServiceImpl(IStaffService staffServiceImpl) {
		this.staffServiceImpl = staffServiceImpl;
	}

	public User getLoginUser() {
		return loginUser;
	}

	public void setLoginUser(User loginUser) {
		this.loginUser = loginUser;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	
}
