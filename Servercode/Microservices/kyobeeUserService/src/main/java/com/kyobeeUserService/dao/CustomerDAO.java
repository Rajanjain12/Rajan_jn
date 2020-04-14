package com.kyobeeUserService.dao;

import org.springframework.data.repository.CrudRepository;

import com.kyobeeUserService.entity.Customer;

public interface CustomerDAO extends CrudRepository<Customer, Integer> {

}
