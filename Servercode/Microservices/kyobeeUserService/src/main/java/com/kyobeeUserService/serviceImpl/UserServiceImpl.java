package com.kyobeeUserService.serviceImpl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kyobeeUserService.dao.OrganizationDAO;
import com.kyobeeUserService.dao.UserDAO;
import com.kyobeeUserService.dto.CredentialsDTO;
import com.kyobeeUserService.dto.LoginUserDTO;
import com.kyobeeUserService.dto.ResetPasswordDTO;
import com.kyobeeUserService.entity.Organization;
import com.kyobeeUserService.entity.User;
import com.kyobeeUserService.service.UserService;
import com.kyobeeUserService.util.CommonUtil;
import com.kyobeeUserService.util.EmailUtil;
import com.kyobeeUserService.util.LoggerUtil;
import com.kyobeeUserService.util.UserServiceConstants;
import com.kyobeeUserService.util.Exception.AccountNotActivatedExeception;
import com.kyobeeUserService.util.Exception.InvalidAuthCodeException;
import com.kyobeeUserService.util.Exception.InvalidLoginException;
import com.kyobeeUserService.util.Exception.UserNotFoundException;

@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	UserDAO userDAO;
	
	@Autowired
	EmailUtil emailUtil;
	
	@Autowired
	OrganizationDAO organizationDAO;

	@Override
	public LoginUserDTO logInCredentialValidate(CredentialsDTO credentialsDTO)
			throws InvalidLoginException, AccountNotActivatedExeception {
		
		LoginUserDTO loginUserDTO;
		User user=userDAO.findByUserNameAndPassword(credentialsDTO.getUserName(),CommonUtil.encryptPassword(credentialsDTO.getPassword()));
		if(user!=null)
		{
			if(user.getActive()==1)
			{
				loginUserDTO=new LoginUserDTO();
				BeanUtils.copyProperties(user, loginUserDTO);
				Organization organization=organizationDAO.fetchOrganizationByUserId(user.getUserID());
				BeanUtils.copyProperties(organization, loginUserDTO);
				System.out.println(organization.getOrganizationName());
				//BeanUtils.copyProperties(user.getOrganizationusers().get(0).getOrganization(), loginUserDTO);
				return loginUserDTO;
			}
			else {
				throw new AccountNotActivatedExeception("Account is not activated.Please Activate your account using code given at registration.");
			}
		}
		else {
			
			throw new InvalidLoginException("Invalid username or password");			
			
		}
		
	}

	@Override
	public String resetPassword(ResetPasswordDTO resetpassword) throws InvalidAuthCodeException {
		String response;
		User user=userDAO.findByUserIDAndAuthCode(resetpassword.getUserId(), resetpassword.getAuthcode());
		if(user!=null)
		{
			user.setPassword(CommonUtil.encryptPassword(resetpassword.getPassword()));
			userDAO.save(user);
			response="password reset successfully.";
		}
		else {
			throw new InvalidAuthCodeException("Invalid Authcode exception");
		}
		return response;
	}

	@Override
	public String forgotPassword(String username) throws UserNotFoundException {
		String response;
		User user=userDAO.findByUserName(username);
		if(user!=null)
		{
			String authcode ;
			if(user.getAuthCode()==null){
				 authcode = CommonUtil.generateRandomToken().toString();
				 user.setAuthCode(authcode);
				 userDAO.save(user);
				}
			else
			{
				authcode=user.getAuthCode();
			}
			String forgotPasswordURL =  UserServiceConstants.KYOBEEHOST +"forgotpassword/"+user.getUserID()+"/"+ authcode ;
			
			StringBuilder htmlContent = new StringBuilder();
			//htmlContent.append("<div style='text-align:center'><img src='" + beeyaURL +"/public/assets/images/logo.png'></img></div>");
			htmlContent.append("<p>Hi " + user.getFirstName() + " " + user.getLastName() + ", </p>");
			htmlContent.append("<p>We received a request to reset your password for your Kyobee account: "+ user.getEmail() +". We are here to help!");
			htmlContent.append("<p>Use the link below to set up a new password for your account.</p>");
			htmlContent.append("<p><a href='"+ forgotPasswordURL +"'>Click here</a></p>");
			htmlContent.append("<p>If you did not request to reset your password, then simply ignore this mail.</p>");
			htmlContent.append("<p>We love hearing from you.</p>");
			htmlContent.append("<p>Email us at "+ UserServiceConstants.KYOBEEMAILID +" if you have any other questions! </p>");
			htmlContent.append("<p>Best,<br/>Kyobee</p>");
			response="password sent successufully to your registered account";
			emailUtil.sendEmail(user.getEmail(),UserServiceConstants.KYOBEEMAILID, "Forgot Passward Email", htmlContent.toString());
		}
		else {
			throw new UserNotFoundException("Please Enter valid email address.");
		}
		return response;
	}

}
