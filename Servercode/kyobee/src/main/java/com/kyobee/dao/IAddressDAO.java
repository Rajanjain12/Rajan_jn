package com.kyobee.dao;

import java.math.BigInteger;

import org.springframework.stereotype.Repository;

import com.kyobee.entity.Address;


@Repository
public interface IAddressDAO  extends  IGenericDAO<Address, Integer> {

	public BigInteger AddressIdCount(Integer addressId);
}
