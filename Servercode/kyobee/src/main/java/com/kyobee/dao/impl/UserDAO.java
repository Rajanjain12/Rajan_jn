//created by pampaniya shweta for forgot password
package com.kyobee.dao.impl;

import javax.management.Query;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kyobee.dao.IUserDAO;
import com.kyobee.entity.User;
import com.kyobee.exception.RsntException;
import com.kyobee.util.common.CommonUtil;
import com.kyobee.util.common.NativeQueryConstants;

@Repository
public class UserDAO implements IUserDAO 
{
	@Autowired
    private SessionFactory sessionFactory;
	
	@Override
	public String getUserAuthCode(long userId)
	{
		String authcode =  (String) sessionFactory.getCurrentSession().createSQLQuery(NativeQueryConstants.GET_AUTH_CODE_BY_USERID).setParameter("userId",userId).uniqueResult();
		return authcode;
	}
	
	@Override
	public User resetPassword(long userId,String password)
	{
		  User user = (User) sessionFactory.getCurrentSession().createQuery(NativeQueryConstants.HQL_GET_USER_BY_ID).setParameter("userId", userId).uniqueResult();
		  user.setPassword(CommonUtil.encryptPassword(password));
		  user.setAuthcode(null);
		  sessionFactory.getCurrentSession().saveOrUpdate(user);
		  return user;
		
	}
}
