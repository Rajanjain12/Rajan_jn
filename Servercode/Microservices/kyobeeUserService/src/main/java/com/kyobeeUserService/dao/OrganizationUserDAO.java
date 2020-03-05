package com.kyobeeUserService.dao;

import org.springframework.data.repository.CrudRepository;

import com.kyobeeUserService.entity.OrganizationUser;

public interface OrganizationUserDAO extends CrudRepository<OrganizationUser,Integer>{

}
