package com.rsnt.util.pusher.factory;

import org.jboss.seam.Component;
import org.jboss.seam.annotations.Name;

import com.rsnt.util.common.Constants;
import com.rsnt.util.pusher.IPusher;
import com.rsnt.util.pusher.impl.AndroidPushMessageGenerator;
import com.rsnt.util.pusher.impl.ApplePushMessageGenerator;

@Name("pusherFactory")
public class PusherFactory {

	public static IPusher getPushMessageGenerator(String deviceMake){
		
		if(deviceMake.equalsIgnoreCase(Constants.DEVICE_MAKE_ANDROID)){
			AndroidPushMessageGenerator generator = (AndroidPushMessageGenerator)
				Component.getInstance(AndroidPushMessageGenerator.class);
			return generator;
		}
			
		else if(deviceMake.equalsIgnoreCase(Constants.DEVICE_MAKE_APPLE)){
			ApplePushMessageGenerator generator = (ApplePushMessageGenerator)
				Component.getInstance(ApplePushMessageGenerator.class);
			return generator;
		}
			
		
		return null;
	}
}
