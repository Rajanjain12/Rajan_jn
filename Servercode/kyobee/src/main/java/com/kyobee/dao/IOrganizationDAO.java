package com.kyobee.dao;

import com.kyobee.entity.Organization;
import com.kyobee.exception.RsntException;

/*
 * Aarshi Patel(15/03/2019)
 */
import org.springframework.stereotype.Repository;
@Repository
public interface IOrganizationDAO  extends  IGenericDAO<Organization, Long> {

}
