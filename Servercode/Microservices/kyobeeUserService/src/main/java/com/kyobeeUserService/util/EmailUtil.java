package com.kyobeeUserService.util;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class EmailUtil {
	
	@Autowired
    private JavaMailSender javaMailSender;
	
	public void sendEmail( String toEmail, String subject, String body){
        
	    
		try{
	
			  MimeMessage msg = javaMailSender.createMimeMessage();

		        // true = multipart message
		        MimeMessageHelper helper = new MimeMessageHelper(msg, true);
				
		        helper.setTo(toEmail);

		        helper.setSubject(subject);

		        // true = text/html
		        helper.setText(body, true);
	       

	        javaMailSender.send(msg);

        } catch (MessagingException e) {
            LoggerUtil.logError(e);
        }
		
}
}
