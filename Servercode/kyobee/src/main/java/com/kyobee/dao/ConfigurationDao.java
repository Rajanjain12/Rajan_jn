package com.kyobee.dao;
//kanaiya 25/03/2019 for fetch data from configuration table
import com.kyobee.entity.Configuration;

public interface ConfigurationDao extends  IGenericDAO <Configuration, Integer> {
	
	public String getItemValue(String itemKey);
	public void resetConfigurationMap() throws Exception;
}
