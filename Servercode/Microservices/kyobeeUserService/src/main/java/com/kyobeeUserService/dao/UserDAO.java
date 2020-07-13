package com.kyobeeUserService.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kyobeeUserService.entity.User;

@Repository
@Transactional
public interface UserDAO extends CrudRepository<User, Integer>{

	
	User findByUserNameAndPassword(String UserName,String password);
	
	User findByUserIDAndAuthCode(Integer userID,String authcode);
	
	User findByUserName(String UserName);
	
	User findByEmail(String email);
	
	User findByUserIDAndActivationCode(Integer userId,String activationCode);
	
	User findByUserID(Integer userId);
	
	@Query(value="select saltString from User where userName =:userName")
	String getSaltString(@Param("userName") String userName);
	
	@Query(value="select authCode from User where userID =:userId")
	String getAuthCode(@Param("userId") Integer userId);
	
	@Query(value="select u from User u where u.password =:password and u.userName =:userName or u.email =:userName ")
	User fetchUser(@Param("userName") String userName,@Param("password") String password);
}
