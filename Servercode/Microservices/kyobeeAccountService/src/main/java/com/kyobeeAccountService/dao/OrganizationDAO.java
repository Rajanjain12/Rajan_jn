package com.kyobeeAccountService.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kyobeeAccountService.entity.Organization;

@Repository
@Transactional
public interface OrganizationDAO extends JpaRepository<Organization, Integer>{

}
