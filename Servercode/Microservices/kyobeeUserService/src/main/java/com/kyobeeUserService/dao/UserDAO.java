package com.kyobeeUserService.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kyobeeUserService.entity.User;

@Repository
@Transactional
public interface UserDAO extends CrudRepository<User, Integer>{

	
	User findByUserNameAndPassword(String UserName,String password);
	
	User findByUserIDAndAuthCode(Integer userID,String authcode);
	
	User findByUserName(String UserName);
}
