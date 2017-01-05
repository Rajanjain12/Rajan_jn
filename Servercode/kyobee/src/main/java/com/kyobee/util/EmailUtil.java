package com.kyobee.util;

import java.util.Date;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.kyobee.exception.RsntException;



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

	@Autowired
	private JavaMailSender mailSender;

	@Value("${rsnt.mail.username}")
	private String from;
	
	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	public void sendEmail(String to, String subject, String content) throws RsntException {
		try {
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
			helper.setTo(to);
			helper.setFrom(from);
			helper.setText(content);
			helper.setSubject(subject);
			helper.setSentDate(new Date());
			mailSender.send(mimeMessage);
		} catch (Exception e) {
			throw new RsntException(e);
		}
	}
	
}
