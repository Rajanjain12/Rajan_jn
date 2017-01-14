package com.kyobee.dao;

import com.kyobee.entity.Organization;
import com.kyobee.entity.User;
import com.kyobee.exception.RsntException;

public interface OrganizationDAO extends ApplicationDAO<User, Integer>{
	public Organization fetchOrganization(String orgName) throws RsntException;
}
