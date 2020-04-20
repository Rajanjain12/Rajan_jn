package com.kyobeeUserService.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kyobeeUserService.entity.Customer;

public interface CustomerDAO extends JpaRepository<Customer, Integer> {

}
