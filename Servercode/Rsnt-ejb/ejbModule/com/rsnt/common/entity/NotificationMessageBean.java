package com.rsnt.common.entity;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.persistence.EntityManager;

import org.jfree.util.Log;

import com.rsnt.common.exception.RsntException;
import com.rsnt.util.common.Constants;
import com.rsnt.util.common.NativeQueryConstants;
import com.rsnt.util.pusher.IPusher;
import com.rsnt.util.pusher.factory.PusherFactory;
import com.telerivet.Project;
import com.telerivet.TelerivetAPI;
import com.telerivet.Util;

import org.jboss.seam.annotations.In;
import org.jboss.seam.contexts.Lifecycle;  

@MessageDriven(
		name = "NotificationMessageHandler",
		activationConfig = {
				@ActivationConfigProperty( propertyName = "destinationType", 
						propertyValue = "javax.jms.Queue"),
						@ActivationConfigProperty( propertyName = "destination", 
						propertyValue ="/queue/NotificationQueue")
		}
		)
public class NotificationMessageBean implements MessageListener {
	   
	public NotificationMessageBean(){        
	}

	public void onMessage(javax.jms.Message message) {
		ObjectMessage objectMessage = null;
		
		Lifecycle.beginCall();  
     
		objectMessage = (ObjectMessage) message;
		try {
			GuestNotificationBean guestNotificationBean = (GuestNotificationBean) objectMessage.getObject();
			String msg1 = "Guest #"+guestNotificationBean.getRank()+": There are "+ (guestNotificationBean.getGuestCount()-1)+" parties ahead of you w/ approx. wait-time of "+					guestNotificationBean.getTotalWaitTime()+" min. ";
			String msg2 = "For LIVE updates: "+
					System.getProperty("rsnt.base.upadteguest.url")+guestNotificationBean.getUuid()+"\n\n"+
					"- Sent by " + guestNotificationBean.getSmsSignature();
			
			String subject = "Your estimated wait time ";
			//Wrapper for Marked as Seated
			if(Constants.NOTIF_MARK_AS_SEATED.equals(guestNotificationBean.getNotificationFlag())) {
				if(guestNotificationBean.getPrefType().equalsIgnoreCase(Constants.RSNT_SMS)){
					msg1 = "Your table is ready! Thank you for your patience. Please return to the host area with all your party members, so you may be seated shortly. We hope you enjoy your visit and thank you for using wait-list service by KYOBEE.com";
					msg2 = "";
					subject = "Your table is ready!";
				} else {
					subject = "Your table is ready!";
					msg1 = "Thank you for your patience. Please return to the host area with all your party members, so you may be seated shortly. We hope you enjoy your visit and thank you for using wait-list service by KYOBEE.com";
					msg2 = "";
				}
			}
			else if (Constants.NOTIF_THRESHOLD_ENTERED.equals(guestNotificationBean.getNotificationFlag())) {
				msg1 = "Guest #"+guestNotificationBean.getRank()+": Your Table is ALMOST ready. Please make your way back to the restaurant with your entire party and wait for your number to be called. Click for updates: "+System.getProperty("rsnt.base.upadteguest.url")+guestNotificationBean.getUuid();
				msg2 = "\n"+"- Sent by " + guestNotificationBean.getSmsSignature()+"\n";
				subject = "Your estimated wait time";
			}
			
			System.out.println("+get pref type---"+guestNotificationBean.getPrefType());

			if(guestNotificationBean.getPrefType().equalsIgnoreCase(Constants.RSNT_SMS)){
				sendSMStoGuest(guestNotificationBean.getSms(), guestNotificationBean.getUuid(),msg1 + msg2, guestNotificationBean.getOrgId(),guestNotificationBean.getSmsRoute());
				//sendSMStoGuest(guest.getSms(), guest.getUuid(),msg2);
			}else if(guestNotificationBean.getPrefType().equalsIgnoreCase("email")){
				sendMail(guestNotificationBean.getEmail(), msg1+msg2,subject);
			}else if (guestNotificationBean.getPrefType().equalsIgnoreCase("PUSH")){
				
				if(guestNotificationBean.getDeviceId()!=null && guestNotificationBean.getDeviceType()!=null){

					IPusher pusher = PusherFactory.getPushMessageGenerator(guestNotificationBean.getDeviceType());

					//PushNotificationPayload payload = CommonUtil.getJSONPushMessage(getOrgMarkerOptionDetail(this.getSelectedMarkerOptionId()),  this.getSelectedMarkerOptionId());

					//pusher.sendPushMessage(deviceId, payload);
					msg2 = "";
					pusher.sendPushMessage("waitlist", guestNotificationBean.getDeviceId(), "Kyobee WaitList", null,
							msg1+msg2);

				}
			}
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RsntException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}  

	/**
	 * sendmail method
	 * @param to
	 * @param msg
	 * @param subject
	 * @throws IOException 
	 */

	public void sendMail(String to,String msg,String subject) throws IOException{

		Thread t1 = null;
		if(t1==null){
			t1= new Thread();
		}
		Properties extProperties = new Properties();
		extProperties.load(this.getClass().getClassLoader().getResourceAsStream("rsnt.properties"));

		Properties properties = new Properties();
		properties.put("mail.smtp.auth", extProperties.getProperty("rsnt.mail.smtp.auth"));
		properties.put("mail.smtp.starttls.enable", extProperties.getProperty("rsnt.mail.smtp.starttls.enable"));
		properties.put("mail.smtp.host", extProperties.getProperty("rsnt.mail.smtp.host"));
		properties.put("mail.smtp.port", extProperties.getProperty("rsnt.mail.smtp.port"));
		Session session = Session.getInstance(properties,
				new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(extProperties.getProperty("rsnt.mail.username"), extProperties.getProperty("rsnt.mail.password"));
			}
		});

		try {

			//
			// This HTML mail have to 2 part, the BODY and the embedded image
			//
			MimeMultipart multipart = new MimeMultipart("related");

			// first part (the html)
			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setContent(msg, "text/html");

			// add it
			multipart.addBodyPart(messageBodyPart);

			MimeMessage messsg = new MimeMessage(session);
			// put everything together
			messsg.setContent(multipart);

			messsg.setSubject(subject);
			messsg.setFrom(new InternetAddress(System.getProperty("rsnt.mail.username")));
			messsg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));

			Transport.send(messsg);

		} catch (MessagingException e) {
			//throw new RuntimeException(e);
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendSMStoGuest(String number, String uuid, String msg, Long orgID, String smsRoute) {
		String PROJECT_API_KEY = System.getProperty("sms.api.key");//"uCa1TCCMti3B9buAZGTIaBYGfOzb7C60";
		String PROJECT_ID = System.getProperty("sms.project.id");//"PJe726ee67deeb1949";
		
		try {
			Properties extProperties = new Properties();
			extProperties.load(this.getClass().getClassLoader().getResourceAsStream("rsnt.properties"));

			TelerivetAPI tr = new TelerivetAPI(PROJECT_API_KEY);

			Project project = tr.initProjectById(PROJECT_ID);
		
			project.sendMessage(Util.options(
					"to_number", number,
					"content", msg,
					"route_id", smsRoute
					));	
			/*if(extProperties.getProperty("smsRouteId") != null && extProperties.getProperty("smsRouteOrgId") !=null ){
				String[] ordIds = extProperties.getProperty("smsRouteOrgId").split(",");
				
			
				 List<Long> orgidList = Arrays.asList(extProperties.getProperty("smsRouteOrgId").split(",")).stream()
                 .map(String::trim)
                 .map(Long::parseLong)
                 .collect(Collectors.toList());
				
					if(orgidList.contains(orgID))
						project.sendMessage(Util.options(
								"to_number", number,
								"content", msg,
								"route_id", extProperties.getProperty("smsRouteId")
								));	
					else
						project.sendMessage(Util.options(
								"to_number", number,
								"content", msg
								));	
				
			}else{
				project.sendMessage(Util.options(
						"to_number", number,
						"content", msg
						));
			}*/
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	

}