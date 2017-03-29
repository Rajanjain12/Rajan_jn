package com.kyobee.service.impl;





import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.jdbc.ReturningWork;
import org.hibernate.jdbc.Work;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kyobee.entity.Organization;
import com.kyobee.entity.User;
import com.kyobee.exception.RsntException;
import com.kyobee.service.IOrganizationService;
import com.kyobee.util.AppTransactional;
import com.kyobee.util.SessionContextUtil;
import com.kyobee.util.common.Constants;
import com.kyobee.util.common.NativeQueryConstants;
import com.telerivet.Project;
import com.telerivet.TelerivetAPI;
import com.telerivet.Util;


@AppTransactional
@Repository
public class OrganizationServiceImpl implements IOrganizationService {

	@Autowired
	SessionContextUtil sessionContextUtil;
	
	@Autowired
    private SessionFactory sessionFactory;
	
	private Logger log = Logger.getLogger(OrganizationServiceImpl.class);
	
	@Override
	public Organization getOrganizationById(Long pOrgId)  {
		// TODO Auto-generated method stub
		try {
			return (Organization) sessionFactory.getCurrentSession().getNamedQuery(Organization.GET_ORGANIZATION_BY_ID)
					.setParameter("organizationId", (Long) sessionContextUtil.get(Constants.CONST_ORGID))
					.uniqueResult();
		}
		catch(Exception e) {
			log.error(e);
			return null;
		}
		
	}


}
