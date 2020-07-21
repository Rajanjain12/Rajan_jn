package com.kyobeeUserService.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.kyobeeUserService.entity.OrganizationUser;
import com.kyobeeUserService.util.UserServiceConstants;


public interface OrganizationUserDAO extends CrudRepository<OrganizationUser,Integer>{
	@Query("select ou.customer.customerID from OrganizationUser ou where ou.user.userID =:userId and ou.role.roleId="+UserServiceConstants.CUSTOMER_ADMIN_ROLE)
	Integer checkForHighestRole(@Param("userId") Integer userId);
	
	@Query("select ou from OrganizationUser ou where ou.customer.customerID =:customerId")
    List<OrganizationUser> fetchOrganizationUser(@Param("customerId") Integer customerId);
	

}
