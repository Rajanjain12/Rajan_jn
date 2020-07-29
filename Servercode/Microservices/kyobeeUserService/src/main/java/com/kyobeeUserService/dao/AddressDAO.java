package com.kyobeeUserService.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kyobeeUserService.entity.Address;

public interface AddressDAO extends JpaRepository<Address, Integer> {

}
