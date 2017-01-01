package com.kyobee.util.pusher.impl;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Sender;
import com.kyobee.util.AppInitializer;
import com.kyobee.util.common.CommonUtil;
import com.kyobee.util.pusher.IPusher;



@Component("androidPushMessageGenerator")
public class  AndroidPushMessageGenerator implements IPusher {

	public void sendPushMessage(String payloadType, String deviceId, String messagePass, String optionId, String messageString) {
		String message = "";
		
		if(payloadType.equalsIgnoreCase("dashboard"))
				 message = CommonUtil.getJSONPushMessageAndroid(messagePass,  optionId, messageString);
		if(payloadType.equalsIgnoreCase("waitlist"))
				 message = CommonUtil.getJSONPushMessageAndroid(messagePass, messageString);

		Sender sender = new Sender(AppInitializer.googleAPIKey); 
		//This is the API key created for the project in GCM Console
		
		System.out.println("Sending message to device: "+deviceId+" message: "+message );
		Message pushMessage = new Message.Builder().addData("Test",message).build();
		System.out.println("pushMessage:::::"+pushMessage);
	    try {
			sender.send(pushMessage, deviceId, 5);
			System.out.println("pushMessage:::::"+pushMessage);
			 System.out.println("Push notification sent successfully to: " +
                     deviceId);
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
	}
	

	

}
