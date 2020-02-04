package com.kyobeeWaitlistService.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import com.kyobeeWaitlistService.entity.Languagekeymapping;

@Repository
@Transactional
public interface LanguageKeyMappingDAO extends CrudRepository<Languagekeymapping, Integer> {

	@Query("select count(*) from Languagekeymapping where updateFlag=:updateFlag")
	Long getUpdateFlagCount(@Param("updateFlag") Integer updateFlag);
	
	@Modifying
	@Query("update Languagekeymapping set updateFlag = :updateFlag where updateFlag = 1")
	void resetUpdateFlagCount(@Param("updateFlag") Integer updateFlag);
		
}
