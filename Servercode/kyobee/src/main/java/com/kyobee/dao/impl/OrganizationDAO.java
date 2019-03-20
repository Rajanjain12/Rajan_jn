package com.kyobee.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kyobee.dao.IOrganizationDAO;
import com.kyobee.entity.Organization;
import com.kyobee.exception.RsntException;
import com.kyobee.util.common.NativeQueryConstants;


/*
 * Aarshi Patel(15/03/2019)
 */
@Repository
public class OrganizationDAO  extends GenericDAOImpl<Organization,Long> implements IOrganizationDAO{

	
}
