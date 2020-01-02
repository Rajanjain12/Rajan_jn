package com.kyobee.util.pusherLatest;

import org.springframework.stereotype.Component;

import com.kyobee.util.AppInitializer;

@Component
public class NotificationUtil {

	static IPusher ipusher;
	
	public NotificationUtil() {
		if(AppInitializer.realtimeOrPubNub.equalsIgnoreCase("pubnub")) {
        	ipusher=new PubNubUtil();
			}
        else {
        	ipusher=new Realtimefamework();
        }
	}
	
	
	public static void sendMessage(Object rootMap,String channel) {
	   ipusher.sendPusher(rootMap, channel);		
	}
}
