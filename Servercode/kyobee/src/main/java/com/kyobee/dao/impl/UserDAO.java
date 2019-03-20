//created by pampaniya shweta for forgot password
package com.kyobee.dao.impl;

import java.math.BigInteger;

import javax.management.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.kyobee.dao.IUserDAO;
import com.kyobee.dto.common.Credential;
import com.kyobee.entity.Organization;
import com.kyobee.entity.User;
import com.kyobee.exception.RsntException;
import com.kyobee.util.common.CommonUtil;
import com.kyobee.util.common.LoggerUtil;
import com.kyobee.util.common.NativeQueryConstants;

@Repository
public class UserDAO extends GenericDAOImpl<User,Long> implements IUserDAO
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

    //User Account Activation by Aarshi(11/03/2019)
	@Override
	public Boolean Verifyauthcode(Integer userId, String authCode) {
		// TODO Auto-generated method stub
		BigInteger isVerified =  (BigInteger)sessionFactory.getCurrentSession().createSQLQuery(NativeQueryConstants.CHECK_AUTH_CODE_BY_USERID).setParameter("userId",userId).setParameter("authCode",authCode).uniqueResult();
		if(isVerified.intValue()==1){
			return true;
		}else{
			return false;
		}


	}

	@Override
	public Integer checkExistingAddress(Credential credential) {
		// TODO Auto-generated method stub
		Integer isVerified =  (Integer)sessionFactory.getCurrentSession().createSQLQuery(NativeQueryConstants.FIND_ADDRESS_ID).setParameter("addressLine1",credential.getAddressDTO().getAddressLine1()).setParameter("addressLine2",credential.getAddressDTO().getAddressLine2()).uniqueResult();
		System.out.println(isVerified);
		return isVerified;
	}


	
    



	
	
}