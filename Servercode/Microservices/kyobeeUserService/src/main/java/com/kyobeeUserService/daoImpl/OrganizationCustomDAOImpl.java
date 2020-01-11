package com.kyobeeUserService.daoImpl;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.kyobeeUserService.dao.OrganizationCustom;
import com.kyobeeUserService.entity.Organization;

public class OrganizationCustomDAOImpl  implements OrganizationCustom{

	 @Autowired
	 SessionFactory sessionFactory;
	 
	/*
	 * @Override public Organization fetchOrganizationByUserId() {
	 * 
	 * String queryStr = "Select u " +
	 * "from User u left join fetch u.userRoles ur left join fetch ur.role r " +
	 * "left join fetch r.roleProtectedObjects rpo left join fetch rpo.protectedobject po "
	 * + "where u.username=:username and u.password =:password  " +
	 * "order by ur.createddate"; Query query =
	 * sessionFactory.getCurrentSession().createQuery(queryStr);
	 * 
	 * System.out.println(query);
	 * 
	 * return null; }
	 */
	

}
