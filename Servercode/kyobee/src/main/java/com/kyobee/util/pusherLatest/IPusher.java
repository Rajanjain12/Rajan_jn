package com.kyobee.util.pusherLatest;

import java.util.Map;

public interface IPusher{
	 
	//public void sendPusher(Map<String, Object> rootMap, String channel);
	public void sucbscribe();
	public void sendPusher(Object message, String channelName);

}
