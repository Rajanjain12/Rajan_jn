package com.kyobeeUserService.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kyobeeUserService.entity.PromotionalCode;

@Repository
@Transactional
public interface PromotionalCodeDAO extends JpaRepository<PromotionalCode, Integer>{
	PromotionalCode findByPromoCode(String PromoCode);
}
