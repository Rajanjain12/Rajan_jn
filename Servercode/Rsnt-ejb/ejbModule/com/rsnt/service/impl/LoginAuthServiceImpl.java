package com.rsnt.service.impl;

import java.math.BigDecimal;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

import com.rsnt.service.ILoginAuthService;
import com.rsnt.util.common.CommonUtil;
import com.rsnt.util.common.Constants;
import com.rsnt.util.common.NativeQueryConstants;

@Stateless(name=ILoginAuthService.JNDI_NAME)
@Scope(ScopeType.STATELESS)
@Name(ILoginAuthService.SEAM_NAME)

public class LoginAuthServiceImpl implements ILoginAuthService{
	@Logger
	private Log log;

	@In
	private EntityManager entityManager;
	@Override
	public List<Object[]> loginCredAuth(String userName, String password) {
		List<Object[]> result = entityManager.createNativeQuery(NativeQueryConstants.GET_USER_LOGIN_AUTH).setParameter(1, userName.toLowerCase()).getResultList();
   		Object[] loginDetail = result.get(0);
   		
		System.out.println(loginDetail[0].toString());
		System.out.println(loginDetail[1].toString());
		System.out.println(loginDetail[2].toString());
		//new BigDecimal(fdbackQtnrData[1].toString()).longValue()
		
		return result;
	}


}
