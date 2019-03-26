package com.kyobee.util;

import java.io.IOException;
import java.util.Properties;

public class PropertyUtility {
	public static Properties fetchPropertyFile(Class oClass, String propertyFileName) throws IOException{
		Properties oProperties = new Properties();
		oProperties.load(oClass.getClassLoader().getResourceAsStream(propertyFileName));
		return oProperties;

}
}