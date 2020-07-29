package com.kyobeeAccountService.dao;

import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kyobeeAccountService.entity.OrganizationType;

@Repository
@Transactional
public interface OrganizationTypeDAO  extends JpaRepository<OrganizationType,Integer>{

}
