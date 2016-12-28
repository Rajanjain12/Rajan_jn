package com.kyobee.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class SessionContextUtil {

	private Map<String, Object> sessionMap = new HashMap<String, Object>();

	public Object get(String key) {
		return sessionMap.get(key);
	}

	public void put(String key, Object value) {
		sessionMap.put(key, value);
	}

	public void remove(String key) {
		sessionMap.remove(key);
	}
}
