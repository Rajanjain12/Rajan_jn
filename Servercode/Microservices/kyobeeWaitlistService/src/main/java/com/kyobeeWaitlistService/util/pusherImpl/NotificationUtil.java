package com.kyobeeWaitlistService.util.pusherImpl;

import com.kyobeeWaitlistService.util.pusher.Pusher;
import org.springframework.stereotype.Component;

@Component
public class NotificationUtil {

	static Pusher pusher;

	static {
		pusher = new PubNubUtil();
	}

	public static void sendMessage(Object rootMap, String channel) {
		pusher.publish(rootMap, channel);
	}

}
