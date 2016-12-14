package com.rsnt.web.action;

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
import org.jboss.seam.log.Log;
import org.jboss.seam.web.IdentityRequestWrapper;

import com.rsnt.common.exception.RsntException;
import com.rsnt.entity.User;
import com.rsnt.service.IOrganizationService;
import com.rsnt.service.ISecurityService;
import com.rsnt.util.common.AppInitializer;
import com.rsnt.util.common.CommonUtil;
import com.rsnt.util.common.Constants;
import com.rsnt.util.common.EmailUtil;

@Name("forgotPasswordAction")
@Scope(ScopeType.EVENT)
public class ForgotPasswordAction {

    private User loginUser;
	
	private String emailId;

	@In(create= true)
	private ISecurityService securityService;

	private boolean forgotPasswordValid;
	private boolean updatePasswordValid;
	
	@In(value=IOrganizationService.SEAM_NAME,create=true)
	private IOrganizationService organizationService;
	
	@In
	private Map messages;
	
	@Logger
	private Log log;
	
	private String newPassword;
	
	private String confirmNewPassword;
	
	private String activationId;
	
	public void resetUserPassword(){
		try{
			final IdentityRequestWrapper identityRequestWrapper = (IdentityRequestWrapper) FacesContext
	         .getCurrentInstance().getExternalContext().getRequest();
			 final HttpServletRequest request = (HttpServletRequest) identityRequestWrapper.getRequest();
			 log.info("Printing Parameter actId+"+request.getParameter("actId"));
			 String actId = request.getParameter("actId");
			 setActivationId(actId);
			 //Deactivating the user once the reset password is requested.
			 organizationService.activateUser(actId, 0);
				
		}
		catch(RsntException e){
			e.printStackTrace();
		}
	}
	
	public void emailPasswordLink(){
		try {

			setForgotPasswordValid(false);
			
			EmailUtil emailUtil = (EmailUtil) Component.getInstance(EmailUtil.class);
			User user = null;
			if(this.getEmailId()!=null){
				user = securityService.getUserFromEmail(this.getEmailId());
				if(user!=null){
					
					final IdentityRequestWrapper identityRequestWrapper = (IdentityRequestWrapper) FacesContext
			         .getCurrentInstance().getExternalContext().getRequest();
					 String newActId = CommonUtil.randomString(50);
					organizationService.updateUserActivationId(newActId, this.getEmailId());
					final HttpServletRequest request = (HttpServletRequest) identityRequestWrapper.getRequest();
					 log.info("Check RequestURL: "+request.getRequestURL());
					 String activationLink =  AppInitializer.domainNameUrl +request.getContextPath()+"/external/resetUserPasswordExt.seam?actId="+newActId;
					 String activationUrl = "<a href='"+activationLink+"'  target='_blank'>Click here</a>";
					 String forgotPasswordHdr = "Hi "+user.getFirstName()+", <br/>As requested, below is the link to reset your password.  This link can only be used once.<br/>";
					 String forgotPasswordFtr ="<br/><br/> If you did not request a password reset please contact us right away.<br/><br/>";
					// String msgBody = 	forgotPasswordHdr + activationUrl+forgotPasswordFtr+AppInitializer.rmsThankYou;
					 
					 String appLinkAppleUrl = "<a href='"+AppInitializer.appLinkApple+"'  target='_blank'><img src=\""+AppInitializer.domainNameUrl+AppInitializer.appImangeExtPath+"/"+AppInitializer.appLinkImg+"\"/></a>";
					 String appLinkAndroidUrl = "<a href='"+AppInitializer.appLinkAndroid+"'  target='_blank'><img src=\""+AppInitializer.domainNameUrl+AppInitializer.appImangeExtPath+"/"+AppInitializer.androidLinkImg+"\"/></a>";
					
					 
					 String msgBody = forgotPasswordHdr + activationUrl+forgotPasswordFtr+"<br/><br/>"  + appLinkAppleUrl + "<br/><br/>"
		 				+ appLinkAndroidUrl +"<br/><br/>" +AppInitializer.rmsThankYou;
	 			
						
					 String userEmail = !(AppInitializer.defaultUserEmail.equalsIgnoreCase(""))?AppInitializer.defaultUserEmail:user.getEmail();
					 log.info("Printing msgBody "+msgBody+" to be sent to "+userEmail);	
					
					
					emailUtil.prepareMessageAndSend(userEmail,AppInitializer.defaultEmailSubject+ " Password Reset Link", msgBody);
					setForgotPasswordValid(true);
				}
				else{
					final FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, messages
		                    .get("forgotPassword.error").toString(), null);
		            FacesContext.getCurrentInstance().addMessage(null, message);
		            setForgotPasswordValid(false);
				}
				
			}
			
		} catch (RsntException e) {
			final FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, messages
                    .get("forgotPassword.error").toString(), null);
            FacesContext.getCurrentInstance().addMessage(null, message);
			e.printStackTrace();
			 setForgotPasswordValid(false);
		}
	}

	public String updatePassword(){
		
		setUpdatePasswordValid(false);
		try{
			 final IdentityRequestWrapper identityRequestWrapper = (IdentityRequestWrapper) FacesContext
	         			.getCurrentInstance().getExternalContext().getRequest();
			 final HttpServletRequest request = (HttpServletRequest) identityRequestWrapper.getRequest();
			 log.info("Printing actId+"+this.getActivationId());

			//String activationLink =  "http://ringmyserver.com"+request.getContextPath()+"/external/activationResult.seam?user=vish.riyer@gmail.com";
			//String activationUrl = "<a href='"+activationLink+"'  target='_blank'>Click here </a>";
			//String msgBody = 	AppInitializer.rmsThankYou + AppInitializer.rmsActSuccess;
		
			 String changePasswordHdr = "Hi "+", <br/>This is to confirm that your KyoBee password has been changed.  If you did not authorize this, please contact us right away.<br/>";
	           // String msgBody = changePasswordHdr +AppInitializer.rmsThankYou;
	            
			/* String appLinkAppleUrl = AppInitializer.appLinkAppleText+" "+"<a href='"+AppInitializer.appLinkApple+"'  target='_blank'>Click Here</a>";
			 String appLinkAndroidUrl = AppInitializer.appLinkAndroidText+" "+"<a href='"+AppInitializer.appLinkAndroid+"'  target='_blank'>Click Here</a>";
			 
			 String residualIncUrl = AppInitializer.residualIncText+" "+"<a href='"+AppInitializer.residualInc+"'  target='_blank'>Click Here</a>";*/
			 
			 String appLinkAppleUrl = "<a href='"+AppInitializer.appLinkApple+"'  target='_blank'><img src=\""+AppInitializer.domainNameUrl+AppInitializer.appImangeExtPath+"/"+AppInitializer.appLinkImg+"\"/></a>";
			 String appLinkAndroidUrl = "<a href='"+AppInitializer.appLinkAndroid+"'  target='_blank'><img src=\""+AppInitializer.domainNameUrl+AppInitializer.appImangeExtPath+"/"+AppInitializer.androidLinkImg+"\"/></a>";
					 				
			String msgBody = changePasswordHdr.toString()+"<br/><br/>"  + appLinkAppleUrl + "<br/><br/>"
 				+ appLinkAndroidUrl +"<br/><br/>" +AppInitializer.rmsThankYou;
	            
			
			if(this.getActivationId()!=null){
				String userEmailDB = organizationService.getUserEmailFromActId(this.getActivationId());
				
				if(userEmailDB!=null && this.getNewPassword()!=null){
					organizationService.updatePassword(CommonUtil.encryptPassword(this.getNewPassword()), this.getActivationId());
					organizationService.activateUser(this.getActivationId(),1);
					
					 String newActId = CommonUtil.randomString(50); //Resetting the Activation link so that it cannot be used again.
					organizationService.updateUserActivationId(newActId, userEmailDB);
						
					String userEmail = !(AppInitializer.defaultUserEmail.equalsIgnoreCase(""))?AppInitializer.defaultUserEmail:userEmailDB;
					log.info("Printing msgBody "+msgBody+" to be sent to "+userEmail);	
					EmailUtil emailUtil = (EmailUtil) Component.getInstance(EmailUtil.class);
					emailUtil.prepareMessageAndSend(userEmail,AppInitializer.defaultEmailSubject+" Password is Updated successfully", msgBody);
					setUpdatePasswordValid(true);
				}
				
			}
			
			 return "success";
		}
		catch(RsntException e){
			e.printStackTrace();
			log.error(e);
		}
		return null;

	}
	
	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
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

	public boolean isForgotPasswordValid() {
		log.info("Returning forgotPasswordValid flag "+forgotPasswordValid);
		return forgotPasswordValid;
	}

	public void setForgotPasswordValid(boolean forgotPasswordValidVar) {
		log.info("Setting forgotPasswordValid flag "+forgotPasswordValidVar);
		this.forgotPasswordValid = forgotPasswordValidVar;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getActivationId() {
		return activationId;
	}

	public void setActivationId(String activationId) {
		this.activationId = activationId;
	}

	public boolean isUpdatePasswordValid() {
		log.info("Returning updatePasswordValid flag "+updatePasswordValid);
		return updatePasswordValid;
	}

	public void setUpdatePasswordValid(boolean updatePasswordValidVar) {
		log.info("Setting updatePasswordValid flag "+updatePasswordValidVar);
		this.updatePasswordValid = updatePasswordValidVar;
	}

	public String getConfirmNewPassword() {
		return confirmNewPassword;
	}

	public void setConfirmNewPassword(String confirmNewPassword) {
		this.confirmNewPassword = confirmNewPassword;
	}

}
