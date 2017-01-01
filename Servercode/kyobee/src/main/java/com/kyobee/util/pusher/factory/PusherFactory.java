package com.kyobee.util.pusher.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.kyobee.util.common.Constants;
import com.kyobee.util.pusher.IPusher;

@Component
public class PusherFactory {

	@Autowired
	@Qualifier("applePushMessageGenerator")
	IPusher applePushMessageGenerator;
	
	@Autowired
	@Qualifier("androidPushMessageGenerator")
	IPusher androidPushMessageGenerator;
	
	public IPusher getPushMessageGenerator(String deviceMake){
		
		if(deviceMake.equalsIgnoreCase(Constants.DEVICE_MAKE_ANDROID)){
			return androidPushMessageGenerator;
		}
			
		else if(deviceMake.equalsIgnoreCase(Constants.DEVICE_MAKE_APPLE)){
			return applePushMessageGenerator;
		}
			
		
		return null;
	}
}
