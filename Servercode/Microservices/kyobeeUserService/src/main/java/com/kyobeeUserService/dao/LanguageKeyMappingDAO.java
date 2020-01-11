package com.kyobeeUserService.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.kyobeeUserService.dto.LanguageMasterDTO;
import com.kyobeeUserService.entity.Languagekeymapping;



public interface LanguageKeyMappingDAO extends CrudRepository<Languagekeymapping, Integer>{

	@Query(value="SELECT l.LangID,l.LangIsoCode,l.LangName,lm.keyName,lm.Value FROM ORGANIZATIONLANG  ol " + 
			"join LANGMASTER l on l.LangID=ol.LanguageID " + 
			"join LANGUAGEKEYMAPPING lm on l.LangIsoCode=lm.LangIsoCode " + 
			"where OrganizationID=:orgId order by l.LangID",nativeQuery=true)
	List<LanguageMasterDTO> fetchLanguageKeyMapForOrganization(@Param("orgId") Integer orgId);
}
