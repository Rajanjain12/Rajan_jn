//kanaiya 25/03/2019 for fetch data from configuration table

package com.kyobee.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kyobee.dao.ConfigurationDao;
import com.kyobee.entity.Configuration;
import com.kyobee.util.common.LoggerUtil;


@Repository("ConfigurationDAO")
@Transactional
public class ConfigurationDaoImpl extends GenericDAOImpl<Configuration, Integer> implements ConfigurationDao {
	private static Map<String, String> configurationMap = null;

    private void loadAll() {
        try {
            configurationMap = new HashMap<String, String>();
            List<Configuration> findAll = findAll();
            for (Configuration configuration : findAll) {
                configurationMap.put(configuration.getItemkey(), configuration.getItemvalue());
            }
        } catch (Exception e) {
        	LoggerUtil.logError("Error while fetching configuration data");
            e.printStackTrace();
        }
    }


	@Override
	public String getItemValue(String itemKey){
		 if (configurationMap == null) {
	            loadAll();
	        }
	        String itemValue = configurationMap.get(itemKey);
	        if (itemValue == null) {
	            itemValue = getValueByKeyFromDB(itemKey);
	        }
	        return itemValue;
	    }


	private String getValueByKeyFromDB(String keyStr) {
		 String valueStr = null;
	        Criteria crit = getSessionFactory().getCurrentSession().createCriteria(Configuration.class);
	        crit.add(Restrictions.eq("itemkey", keyStr));
	        List<Configuration> list = crit.list();
	        if (list != null && !list.isEmpty()) {
	            valueStr = list.get(0).getItemvalue();
	        }
	        return valueStr;
	    }
	
		public void resetConfigurationMap() throws Exception {
			try
			{
			configurationMap=null;
			}
			catch(Exception e)
			{
				new Exception("During Reset Configuration Exception Occur", e);
			}
		}  
	
	
}
