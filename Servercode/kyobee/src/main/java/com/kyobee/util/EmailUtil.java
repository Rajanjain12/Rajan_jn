package com.kyobee.util;

import java.util.Date;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.kyobee.dto.common.Credential;
import com.kyobee.exception.RsntException;
import com.kyobee.rest.WaitListRestAction;
import com.kyobee.util.common.CommonUtil;
import com.kyobee.util.common.Constants;
import com.kyobee.util.common.LoggerUtil;



/**
 * EmailUtil is the utility to send the email. It uses the spring MailSender to
 * send all the messages. We need to configure this in spring config.xml or
 * dispacther-servlet.xml in our case.
 * 
 * @author rohit
 *
 */
@Component
public class EmailUtil {
	
	private Logger log = Logger.getLogger(EmailUtil.class);

	@Autowired
	private JavaMailSender mailSender;
	
	@Value("${rsnt.mail.username}")
	private String from;
	
	//Pampaniya Shweta For Adding Client Base Url..
	@Value("${rsnt.base.forgotpassword.url.initial}")
	private String forgotpassInitialUrl;
	
	@Value("${rsnt.base.forgotpassword.url.suffix}")
    private String forgotpassSuffixUrl;


	
	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	public void sendEmail(String to, String subject, String content) throws RsntException {
		try {
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			mimeMessage.setFrom(new InternetAddress(from));
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
			helper.setTo(to);
			helper.setFrom(from);
			helper.setText(content,false);
			helper.setSubject(subject);
			helper.setSentDate(new Date());			
			mailSender.send(mimeMessage);
		} catch (Exception e) {
			LoggerUtil.logError(e.getMessage(),e);
			throw new RsntException(e);
		}
	}
	public void sendWelcomeEmail(String emailTo, String name) throws RsntException {
		try {
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			
			mimeMessage.setFrom(new InternetAddress(from));
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
			helper.setTo(emailTo);
			helper.setFrom(from);
			helper.setSubject("Welcome Email");
			helper.setSentDate(new Date());

			StringBuilder htmlContent = new StringBuilder();
			//htmlContent.append("<div style='text-align:center'><img src='" + beeyaURL +"/public/assets/images/logo.png'></img></div>");
			htmlContent.append("<p>Hi " + name + ", </p>");
			htmlContent.append("<p>Welcome to Kyobee Waitlist.</p>");
			htmlContent.append("<p>With Kyobee Waitlist at your business, you will have a self check-in "
					+ "hostess this is easy for your customers to sign-in with. It's your smart helper !</p>");
			
			htmlContent.append("<p>Thank You</p>");
			htmlContent.append("<p>Kyobee Team</p>");

			helper.setText(htmlContent.toString(), true);

			mailSender.send(mimeMessage);
		} catch (Exception e) {
			throw new RsntException(e);
		}
	}
//Pampaniya Shweta for Sending Forgot Password Mail.....
	
	public void sendForgotPasswardEmail(String emailTo, String firstName,String lastName,String clientbase, String authcode ,Long userId) throws RsntException {
		try {
			MimeMessage mimeMessage = mailSender.createMimeMessage();	
			mimeMessage.setFrom(new InternetAddress(from));
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
			
			helper.setTo(emailTo);
			helper.setFrom(from);
			helper.setSubject("Forgot Passward Email");
			helper.setSentDate(new Date());
			
			String forgotPasswordURL = forgotpassInitialUrl +clientbase+ "." + forgotpassSuffixUrl + userId + "/" + authcode ;
			System.out.println(forgotPasswordURL);
			
			StringBuilder htmlContent = new StringBuilder();
			//htmlContent.append("<div style='text-align:center'><img src='" + beeyaURL +"/public/assets/images/logo.png'></img></div>");
			htmlContent.append("<p>Hi " + firstName + " " + lastName + ", </p>");
			htmlContent.append("<p>We received a request to reset your password for your Kyobee account: "+ emailTo +". We are here to help!");
			htmlContent.append("<p>Use the link below to set up a new password for your account.</p>");
			htmlContent.append("<p><a href='"+ forgotPasswordURL +"'>Click here</a></p>");
			htmlContent.append("<p>If you did not request to reset your password, then simply ignore this mail.</p>");
			htmlContent.append("<p>We love hearing from you.</p>");
			htmlContent.append("<p>Email us at "+ from +" if you have any other questions! </p>");
			htmlContent.append("<p>Best,<br/>Kyobee</p>");

			helper.setText(htmlContent.toString(), true);
			mailSender.send(mimeMessage);
		}
		catch (Exception e) {
			throw new RsntException(e);
		
		}
	}

}
