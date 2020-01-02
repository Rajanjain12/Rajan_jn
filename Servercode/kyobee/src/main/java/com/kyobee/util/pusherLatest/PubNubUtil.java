package com.kyobee.util.pusherLatest;

import java.util.Arrays;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.kyobee.util.AppInitializer;
import com.pubnub.api.*;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;

@Component
public class PubNubUtil implements IPusher{

	private static PubNub pubnub ;
	public PubNubUtil() {
		
		PNConfiguration pnConfiguration = new PNConfiguration()
				.setSubscribeKey(AppInitializer.pubnubSubscribeKey)
				.setPublishKey(AppInitializer.pubnubPublishKey)
				.setSecretKey(AppInitializer.pubnubSecretkey)
				.setSecure(true);
		
		 pubnub = new PubNub(pnConfiguration);
	}
	

	@Override
	public void sendPusher(Map<String, Object> message,String channelName) {
		 
		  try {
			pubnub.publish()
			      .message(message)
			    .channel(channelName)
			    .async(new PNCallback<PNPublishResult>() {
			        @Override
			        public void onResponse(PNPublishResult result, PNStatus status) {
			            // handle publish result, status always present, result if successful
			            // status.isError to see if error happened
			        	System.out.println("Message send");
			        }
			    });
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		    /*.async(new PNCallback<PNPublishResult>() {
		        @Override
		        public void onResponse(PNPublishResult result, PNStatus status) {
		        	System.out.println("Done");
		        }

		    });*/
	}

	@Override
	public void sucbscribe() {
			
	}
	
}
