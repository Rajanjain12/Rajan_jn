package com.kyobee.dao.impl;

import java.math.BigInteger;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kyobee.dao.IAddressDAO;
import com.kyobee.dao.IOrganizationDAO;
import com.kyobee.entity.Address;
import com.kyobee.entity.Organization;
import com.kyobee.util.common.NativeQueryConstants;
@Repository
public class AddressDAO extends GenericDAOImpl<Address,Integer> implements IAddressDAO {
	
	@Autowired
 private SessionFactory sessionFactory;
	
	//Count AddresID by Aarshi(20/03/2019)
	@Override
	public BigInteger AddressIdCount(Integer addressId) {
		// TODO Auto-generated method stub
		return (BigInteger) sessionFactory.getCurrentSession().createSQLQuery(NativeQueryConstants.COUNT_ADDRESS_ID).setParameter("addressId",addressId).uniqueResult();
	}

}
