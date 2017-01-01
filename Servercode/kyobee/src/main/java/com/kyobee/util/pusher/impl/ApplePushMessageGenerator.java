package com.kyobee.util.pusher.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.kyobee.util.AppInitializer;
import com.kyobee.util.common.CommonUtil;
import com.kyobee.util.pusher.IPusher;

import javapns.Push;
import javapns.communication.exceptions.CommunicationException;
import javapns.communication.exceptions.KeystoreException;
import javapns.notification.PushNotificationPayload;
import javapns.notification.PushedNotification;
import javapns.notification.ResponsePacket;


@Component("applePushMessageGenerator")
public class ApplePushMessageGenerator implements IPusher  {

	public void sendPushMessage(String payloadType, String deviceId,  String message, String optionId, String messageString) {
		try {
			//System.out.println(this.getClass().getResource("/pusherConf/ringmyserver.p12").getPath());
			//System.out.println(Thread.currentThread().getContextClassLoader().getResource("/pusherConf/ringmyserver.p12").getPath());
			 //List<PushedNotification> notifications  = Push.alert(message, AppInitializer.applePusherPath, AppInitializer.appleCertPassword, false, deviceId);
			PushNotificationPayload payload = null;
			
			if(payloadType.equalsIgnoreCase("dashboard"))
				payload = CommonUtil.getJSONPushMessageIphone(message,  optionId, messageString);
			if(payloadType.equalsIgnoreCase("waitlist"))
				payload = CommonUtil.getJSONPushMessageIphone(message, messageString);

			List<PushedNotification> notifications  = Push.payload(payload, AppInitializer.applePusherPath, AppInitializer.appleCertPassword, false, deviceId);
			 System.out.println("payload:::::"+payload);
			 
			 for (PushedNotification notification : notifications) {
                if (notification.isSuccessful()) {
                        /* Apple accepted the notification and should deliver it */  
                        System.out.println("Push notification sent successfully to: " +
                                                        notification.getDevice().getToken());
                        /* Still need to query the Feedback Service regularly */  
                } else {
                        String invalidToken = notification.getDevice().getToken();
                        /* Add code here to remove invalidToken from your database */  

                        /* Find out more about what the problem was */  
                        Exception theProblem = notification.getException();
                        theProblem.printStackTrace();

                        /* If the problem was an error-response packet returned by Apple, get it */  
                        ResponsePacket theErrorResponse = notification.getResponse();
                        if (theErrorResponse != null) {
                                System.out.println(theErrorResponse.getMessage());
                        }
                }
        }
		
		} catch (CommunicationException e) {
			e.printStackTrace();
		} catch (KeystoreException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}

}
