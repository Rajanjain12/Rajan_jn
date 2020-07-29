package com.kyobeeAccountService.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kyobeeAccountService.entity.User;

@Repository
@Transactional
public interface UserDAO extends CrudRepository<User, Integer>{
	
	User findByUserID(Integer userId);	
}
