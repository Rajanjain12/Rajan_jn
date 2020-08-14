package com.kyobeeAccountService.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kyobeeAccountService.entity.Customer;

public interface CustomerDAO extends JpaRepository<Customer, Integer> {

}
