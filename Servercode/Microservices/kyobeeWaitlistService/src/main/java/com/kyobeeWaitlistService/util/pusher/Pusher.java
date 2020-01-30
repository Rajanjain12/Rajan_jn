package com.kyobeeWaitlistService.util.pusher;

public interface Pusher {

	public void subscribe();
	public void publish(Object message, String channelName);
}
