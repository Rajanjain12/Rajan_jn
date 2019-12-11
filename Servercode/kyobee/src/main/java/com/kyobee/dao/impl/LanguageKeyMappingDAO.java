package com.kyobee.dao.impl;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kyobee.dao.ILanguageKeyMappingDAO;
import com.kyobee.util.AppTransactional;
import com.kyobee.util.common.NativeQueryConstants;

@Repository
@AppTransactional
public class LanguageKeyMappingDAO implements ILanguageKeyMappingDAO{

	@Autowired
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getKeyFromLanguageKeyMappingByValue(Long orgId, String lookUpTypeId)
	{

		List<Object[]> resultList = sessionFactory.openSession().createSQLQuery(NativeQueryConstants.GET_ORG_MARKETING_PREF_VALUES_WITH_KEY)
				.setParameter("orgId", orgId)
				.setParameter("catTypeId", lookUpTypeId)
				.list();
		System.out.println("dao" + resultList.size());
		sessionFactory.close();
		return resultList;
	}


	/*public LanguageKeyMapping getKeyFromLanguageKeyMappingByValue(String prefValue) {

		Criteria criteria=sessionFactory.getCurrentSession().createCriteria(LanguageKeyMapping.class);
		criteria.add(Restrictions.eq("value", prefValue));
		criteria.add(Restrictions.eq("langIsoCode", "en"));

		List<LanguageKeyMapping> lanKeyMappingList = criteria.list();

		if(lanKeyMappingList.size()!=0)
			return lanKeyMappingList.get(0);
		else
			return null;
	}*/
}
