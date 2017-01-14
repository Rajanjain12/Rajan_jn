package com.kyobee.dao.impl;

import org.hibernate.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import com.kyobee.dao.OrganizationDAO;
import com.kyobee.entity.Organization;
import com.kyobee.entity.User;
import com.kyobee.exception.RsntException;

@Component
@Service
public class OrganizationDAOImpl extends ApplicationDAOImpl<User, Integer> implements OrganizationDAO {

	@Override
	public Organization fetchOrganization(String orgName) throws RsntException {
		String queryStr = "SELECT u FROM Organization u WHERE u.organizationName=:orgName";
		Query query = getSessionFactory().getCurrentSession().createQuery(queryStr);
		query.setParameter("orgName", orgName);
		Organization org = (Organization) query.uniqueResult();
		return org;
	}
}
