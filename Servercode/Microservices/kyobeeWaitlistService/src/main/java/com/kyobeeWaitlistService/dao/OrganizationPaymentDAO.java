package com.kyobeeWaitlistService.dao;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kyobeeWaitlistService.entity.OrganizationPayment;

@Repository
@Transactional
public interface OrganizationPaymentDAO extends JpaRepository<OrganizationPayment, Integer> {

}