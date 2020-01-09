package com.kyobeeUserService.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.kyobeeUserService.entity.User;

@Repository
public interface UserDAO extends CrudRepository<User, Long>{

}
