package com.kyobeeAccountService.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kyobeeAccountService.entity.User;
import com.kyobeeAccountService.util.AccountServiceConstants;

@Repository
@Transactional
public interface UserDAO extends CrudRepository<User, Integer>{
	
	User findByUserID(Integer userId);
	
	@Modifying
	@Query("update User set active="+ AccountServiceConstants.INACTIVE_USER+" where userID =:userId")
	void deleteUser(@Param("userId") Integer userId);
}
