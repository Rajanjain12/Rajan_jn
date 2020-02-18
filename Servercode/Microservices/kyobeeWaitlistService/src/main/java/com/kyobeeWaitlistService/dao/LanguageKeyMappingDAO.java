package com.kyobeeWaitlistService.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import com.kyobeeWaitlistService.dto.LanguageMasterDTO;
import com.kyobeeWaitlistService.entity.Languagekeymapping;

@Repository
@Transactional
public interface LanguageKeyMappingDAO extends CrudRepository<Languagekeymapping, Integer> {

	@Query("select count(*) from Languagekeymapping where updateFlag=:updateFlag")
	Long getUpdateFlagCount(@Param("updateFlag") Integer updateFlag);
	
	@Modifying
	@Query("update Languagekeymapping set updateFlag = :updateFlag where updateFlag = 1")
	void resetUpdateFlagCount(@Param("updateFlag") Integer updateFlag);
	
	@Query(value="SELECT new com.kyobeeUserService.dto.LanguageMasterDTO(l.langID,l.langName,l.langIsoCode,lm.keyName,lm.value) FROM Organization o " +
			"join LangMaster l on l.langID=o.defaultLangId " +
			"join Languagekeymapping lm on l.langIsoCode=lm.langIsoCode " +
			"where o.organizationID=:orgId order by l.langID")
	List<LanguageMasterDTO> fetchLanguageKeyMapForOrganization(@Param("orgId") Integer orgId);
		
}
