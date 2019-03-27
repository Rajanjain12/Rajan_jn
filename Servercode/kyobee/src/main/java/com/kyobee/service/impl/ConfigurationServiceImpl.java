package com.kyobee.service.impl;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kyobee.dao.ConfigurationDao;
import com.kyobee.service.ConfigurationService;

@Service
public class ConfigurationServiceImpl implements ConfigurationService {
 
	@Autowired
    private ConfigurationDao configurationDAO;
    
	@Transactional
	public String getItemValue(String itemKey) {
		 return configurationDAO.getItemValue(itemKey);
		
	}

	@Transactional
	public void resetConfigurationMap() throws Exception {
		try
		{
	  configurationDAO.resetConfigurationMap();
		}
		catch(Exception e)
		{
			new Exception("Occur During Reset ConfigurationMap in ConfigurationService",e);
		}
	    }
	
}
