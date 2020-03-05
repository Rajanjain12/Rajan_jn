package com.kyobeeUserService.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kyobeeUserService.entity.Role;

@Repository
@Transactional
public interface RoleDAO extends CrudRepository<Role, Integer>{

	@Query("select r from Role r where r.roleId=:roleId")
	Role fetchRole(@Param("roleId") Integer roleId);
}
