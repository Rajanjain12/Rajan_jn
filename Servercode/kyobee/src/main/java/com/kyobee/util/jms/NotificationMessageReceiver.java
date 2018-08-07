package com.kyobee.util.jms;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.bandwidth.sdk.BandwidthClient;
import com.bandwidth.sdk.model.ReceiptRequest;
import com.kyobee.entity.GuestNotificationBean;
import com.kyobee.exception.RsntException;
import com.kyobee.util.AppInitializer;
import com.kyobee.util.EmailUtil;
import com.kyobee.util.common.Constants;
import com.kyobee.util.common.LoggerUtil;
import com.kyobee.util.pusher.IPusher;
import com.kyobee.util.pusher.factory.PusherFactory;

/*import com.bandwidth.sdk.*;*/


@Component(value = "notificationMessageReceiver")
public class NotificationMessageReceiver implements MessageListener{

	@Autowired
	PusherFactory pusherFactory;
	
	@Autowired
	EmailUtil emailUtil;
	
	@Value("${sms.api.key}")
	private String smsApiKey;
	
	@Value("${sms.project.id}")
	private String smsProjectId;
	
	@Value("${rsnt.base.updateguest.url}")
	private String baseUrl;
	
	@Value("${rsnt.base.updateguest.url.initial}")
	private String urlInitial;
	
	@Value("${rsnt.base.updateguest.url.suffix}")
	private String urlSuffix;
	
	@Value("${rsnt.base.url.admin}")
	private String adminURL;
	
	@Value("${rsnt.base.url.advantech}")
	private String advantechURL;
	
	@Value("${rsnt.base.url.sweethoneydessert}")
	private String sweethoneydessertURL;
	
	@Value("${rsnt.base.url.rbsushi}")
	private String rbSushiURL;
	
	@Value("${rsnt.base.url.masterkim}")
	private String masterkimURL;
	
	
	/*@Override
	public void onMessage(Message message) {
		ObjectMessage objectMessage = null;
		
		//Lifecycle.beginCall();  
     
		objectMessage = (ObjectMessage) message;
		try {
			GuestNotificationBean guestNotificationBean = (GuestNotificationBean) objectMessage.getObject();
			String emailUrl=urlInitial + guestNotificationBean.getClientBase() + "." + urlSuffix + guestNotificationBean.getUuid();//Added LAter
			String msg1 = "";
			if(guestNotificationBean.getRank() != null && guestNotificationBean.getGuestCount() != null){
				 msg1 = "Guest #"+guestNotificationBean.getRank()+": There are "+ (guestNotificationBean.getGuestCount()-1)+" parties ahead of you w/ approx. wait-time of ";
			}
			
			if (guestNotificationBean.getTotalWaitTime() != null && guestNotificationBean.getTotalWaitTime() > 60) {
				int hr = (int) (guestNotificationBean.getTotalWaitTime() / 60L);
				msg1 = msg1 + hr + " hr " + guestNotificationBean.getTotalWaitTime() % 60L + " min. ";
			} else {
				msg1 = msg1 + guestNotificationBean.getTotalWaitTime() + " min. ";
			}
			
			
			 * emailMsg2 has been added for testing purpose of Email issue not working properly on dev server 
			 * Changed By Vruddhi 
			 
			String emailMsg2 = "For LIVE updates: "+
					emailUrl +"\n\n"+ "- Sent by " + guestNotificationBean.getSmsSignature();
			String msg2 = "For LIVE updates: "+
					buildURL(guestNotificationBean.getClientBase(), guestNotificationBean.getUuid()) +"\n\n"+ "- Sent by " + guestNotificationBean.getSmsSignature();
			
			String subject = "Your estimated wait time ";
			//Wrapper for Marked as Seated
			if(Constants.NOTIF_MARK_AS_SEATED.equals(guestNotificationBean.getNotificationFlag())) {
				if(guestNotificationBean.getPrefType().equalsIgnoreCase(Constants.RSNT_SMS)){
					msg1 = "Your table is ready! Thank you for your patience. Please return to the host area with all your party members, so you may be seated shortly. We hope you enjoy your visit and thank you for using wait-list service by KYOBEE.com";
					msg2 = "";
					emailMsg2="";
					subject = "Your table is ready!";
				} else {
					subject = "Your table is ready!";
					msg1 = "Thank you for your patience. Please return to the host area with all your party members, so you may be seated shortly. We hope you enjoy your visit and thank you for using wait-list service by KYOBEE.com";
					msg2 = "";
					emailMsg2="";
				}
			}
			else if (Constants.NOTIF_THRESHOLD_ENTERED.equals(guestNotificationBean.getNotificationFlag())) {
				if(guestNotificationBean.getPrefType().equalsIgnoreCase("email")) {					
					msg1 = "Guest #"+guestNotificationBean.getRank()+": Your Table is ALMOST ready. Please make your way back to the restaurant with your entire party and "
							+ "wait for your number to be called. Click for updates: " + emailUrl;
					emailMsg2="\n"+"- Sent by " + guestNotificationBean.getSmsSignature()+"\n";
				}else {		
					//Text for message has been changed and made upto 153 characters so that only single message is received
					msg1 = "Guest #"+guestNotificationBean.getRank()+": Table is almost ready. Come back and wait for your name to be called"
						+ "For updates: " + buildURL(guestNotificationBean.getClientBase(), guestNotificationBean.getUuid());
					msg1 = guestNotificationBean.getMessage();
				}
				msg2 = "\n"+"- Sent by " + guestNotificationBean.getSmsSignature()+"\n";
				msg2 = guestNotificationBean.getSmsSignature()+"\n";
				subject = "Your estimated wait time";
			}
			else if (Constants.FREETEXT.equals(guestNotificationBean.getNotificationFlag())){
				msg1 = guestNotificationBean.getMessage();
				msg1 = buildURL(guestNotificationBean.getClientBase(), guestNotificationBean.getUuid());
				msg2 = "\n"+"- Sent by " + guestNotificationBean.getSmsSignature()+"\n";
			}
			
			System.out.println("+get pref type---"+guestNotificationBean.getPrefType());

			if(guestNotificationBean.getPrefType().equalsIgnoreCase(Constants.RSNT_SMS)){
				sendSMStoGuest(guestNotificationBean.getSms(), guestNotificationBean.getUuid(),msg1 + msg2, guestNotificationBean.getOrgId(),guestNotificationBean.getSmsRoute(),guestNotificationBean.getSmsRouteNo());
				//sendSMStoGuest(guest.getSms(), guest.getUuid(),msg2);
			}else if(guestNotificationBean.getPrefType().equalsIgnoreCase("email")){
				sendMail(guestNotificationBean.getEmail(), msg1+emailMsg2,subject);//changed from msg2 to emailMsg2
			}else if (guestNotificationBean.getPrefType().equalsIgnoreCase("PUSH")){
				
				if(guestNotificationBean.getDeviceId()!=null && guestNotificationBean.getDeviceType()!=null){

					IPusher pusher = pusherFactory.getPushMessageGenerator(guestNotificationBean.getDeviceType());

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
		} catch (RsntException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			sessionFactory.getCurrentSession().createSQLQuery(NativeQueryConstants.GET_USER_LANGUAGE_PREF_VALUES).
			setParameter("langId", 1).list();
			System.out.println("done");
		} catch (Exception e) {
			// TODO: handle exception
		}
    }*/
	
	@Override
	public void onMessage(Message message) {
		ObjectMessage objectMessage = null;
		
		//Lifecycle.beginCall();  
     
		objectMessage = (ObjectMessage) message;
		try {
			GuestNotificationBean guestNotificationBean = (GuestNotificationBean) objectMessage.getObject();
			
			String msg1 = "";
			String msg2 = "";
			
			if(guestNotificationBean.getTempLevel() == 0){
				msg1 = guestNotificationBean.getMessage();
				if(guestNotificationBean.getSmsSignature()!=null && !guestNotificationBean.getSmsSignature().equals("") && !guestNotificationBean.getSmsSignature().equals("SweetHoneyDessert")){
				msg2 = " - "+guestNotificationBean.getSmsSignature()+"\n";
				}
			}
			else if(guestNotificationBean.getTempLevel() == 1){
				msg1 = guestNotificationBean.getMessage();
				if(guestNotificationBean.getSmsSignature()!=null && !guestNotificationBean.getSmsSignature().equals("") && !guestNotificationBean.getSmsSignature().equals("SweetHoneyDessert")){
				msg2 = " - "+guestNotificationBean.getSmsSignature()+"\n";
				}
			}
			else if(guestNotificationBean.getTempLevel() == 2){
				msg1 = guestNotificationBean.getMessage();
				if(guestNotificationBean.getSmsSignature()!=null && !guestNotificationBean.getSmsSignature().equals("") && !guestNotificationBean.getSmsSignature().equals("SweetHoneyDessert")){
				msg2 = " - "+guestNotificationBean.getSmsSignature()+"\n";
				}
			}
			
			if(guestNotificationBean.getPrefType().equalsIgnoreCase(Constants.RSNT_SMS)){
				sendSMStoGuest(guestNotificationBean.getSms(), guestNotificationBean.getUuid(),msg1 + msg2, guestNotificationBean.getOrgId(),guestNotificationBean.getSmsRoute(),guestNotificationBean.getSmsRouteNo());
				//sendSMStoGuest(guest.getSms(), guest.getUuid(),msg2);
			}
			else if (guestNotificationBean.getPrefType().equalsIgnoreCase("PUSH")){
				
				if(guestNotificationBean.getDeviceId()!=null && guestNotificationBean.getDeviceType()!=null){

					IPusher pusher = pusherFactory.getPushMessageGenerator(guestNotificationBean.getDeviceType());

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
		} catch (RsntException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*try {
			sessionFactory.getCurrentSession().createSQLQuery(NativeQueryConstants.GET_USER_LANGUAGE_PREF_VALUES).
			setParameter("langId", 1).list();
			System.out.println("done");
		} catch (Exception e) {
			// TODO: handle exception
		}*/
    }
	/**
	 * sendmail method
	 * @param to
	 * @param msg
	 * @param subject
	 * @throws IOException 
	 * @throws RsntException 
	 */

	public void sendMail(String to,String msg,String subject) throws RsntException{

		emailUtil.sendEmail(to, subject, msg);
		
		/*
		  TODO - Uncomment below code and make it working - rohit
		 * Thread t1 = null;
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
		}*/
	}

	public void sendSMStoGuest(String number, String uuid, String msg, Long orgID, String smsRoute,String smsRouteNo) {
		//String PROJECT_API_KEY = System.getProperty("sms.api.key");//"uCa1TCCMti3B9buAZGTIaBYGfOzb7C60";
		//String PROJECT_ID = System.getProperty("sms.project.id");//"PJe726ee67deeb1949";
		if(smsRoute.equalsIgnoreCase("AWS"))
			sendSMSToGuestViaAWS(number,msg);
		else if(smsRoute.equalsIgnoreCase("Bandwidth")) {
			sendSMSToGuestViaBW(number,msg,smsRouteNo);
		}
		/*else {
			
			try {
				TelerivetAPI tr = new TelerivetAPI(smsApiKey);
				Project project = tr.initProjectById(smsProjectId);
				project.sendMessage(Util.options(
						"to_number", number,
						"content", msg,
						"route_id", smsRoute
						));				
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}*/
	}
	
	
	private void sendSMSToGuestViaBW(String toNumber,String msg,String smsRouteNo) {
		// TODO Auto-generated method stub		
		try {//userID,apiToken,apisecret
			
			//BandwidthClient.getInstance().setCredentials("u-64pesbby7g33ad5n7zl3pli", "t-b7p63jg2qqalyjdcmu2xlea", "bgcrgrjx2kuhu44vspfe2mc4ozmaodem7dd7a3q");
			BandwidthClient.getInstance().setCredentials(AppInitializer.bandwidthUserID, AppInitializer.bandwidthApiToken, AppInitializer.bandwidthApiSecretkey);
			ReceiptRequest receiptRequest = ReceiptRequest.ALL;
			
			com.bandwidth.sdk.model.Message message = com.bandwidth.sdk.model.Message.create("+1"+toNumber, smsRouteNo, msg, receiptRequest);
			System.out.println(message);
			//com.bandwidth.sdk.model.Message message = com.bandwidth.sdk.model.Message.create("+1"+toNumber, "+16573314616", msg);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void sendSMSToGuestViaAWS(String phoneNumber, String msg) {
		// TODO Auto-generated method stub
		
		System.out.println(org.apache.http.conn.ssl.SSLConnectionSocketFactory.class.getProtectionDomain().getCodeSource().getLocation().getPath());
		Map<String, MessageAttributeValue> smsAttributes =
		        new HashMap<String, MessageAttributeValue>();
		smsAttributes.put("AWS.SNS.SMS.SenderID", new MessageAttributeValue()
		        .withStringValue("KYOBEE") //The sender ID shown on the device.
		        .withDataType("String"));		
		smsAttributes.put("AWS.SNS.SMS.SMSType", new MessageAttributeValue()
		        .withStringValue("Transactional")
		        .withDataType("String"));
		AWSCredentials awsCredentials=new BasicAWSCredentials(Constants.AWS_ACCESS_KEY_ID, Constants.AWS_SECRET_KEY);
		try {
		// AmazonSNSClient snsClient = new AmazonSNSClient(awsCredential);
			AmazonSNSClient snsClient=new AmazonSNSClient(awsCredentials);
		 PublishResult result = snsClient.publish(( new PublishRequest()
                 .withMessage(msg))
                 .withPhoneNumber("+1"+phoneNumber)
                 .withMessageAttributes(smsAttributes));
		 
		}catch(Exception e) {
			LoggerUtil.logError(e.getMessage(),e);
		}
	}


	public String buildURL(String clientBase, String uuid){
		String url = "";
		
		/*if(urlSuffix.contains("kyobee.com")){
			// Prod
			if("admin".equals(clientBase)){
				url = "https://tinyurl.com/zrpro2s" + "/s?tid=" + uuid;
			} else if ("advantech".equals(clientBase)){
				url = "https://tinyurl.com/h5wmc2t" + "/s?tid=" + uuid;;
			} else {
				url = urlInitial + clientBase + "." + urlSuffix + uuid;
			}
		} else if (urlSuffix.contains("aksharnxdigital.com")){
			// DEV or UAT
			if("admin".equals(clientBase)){
				url = "https://tinyurl.com/jnyg7lx" + "/s?tid=" + uuid;;
			} else if ("advantech".equals(clientBase)){
				url = "https://tinyurl.com/jp5gpm7" + "/s?tid=" + uuid;;
			} else {
				url = urlInitial + clientBase + "." + urlSuffix + uuid;
			}
		} else {
			url = urlInitial + clientBase + "." + urlSuffix + uuid;
		}*/
		
		if("admin".equals(clientBase)){
			url = adminURL + "/s/" + uuid;
		} else if ("advantech".equals(clientBase)){
			url = advantechURL + "/s/" + uuid;
		} else if ("sweethoneydessert".equals(clientBase)){
			url = sweethoneydessertURL + "/s/" + uuid;
		} else if ("rbsushi".equals(clientBase)){
			url = rbSushiURL + "/s/" + uuid;
		} else if ("masterkim".equals(clientBase)){
			url = masterkimURL + "/s/" + uuid;
		}
		else {
			url = urlInitial + clientBase + "." + urlSuffix + uuid;
		}
		
				
		return url;
	}

	

	
}
