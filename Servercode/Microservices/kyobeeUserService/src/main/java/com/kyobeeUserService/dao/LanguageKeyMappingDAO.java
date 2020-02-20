	package com.kyobeeUserService.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.kyobeeUserService.dto.LanguageMasterDTO;
import com.kyobeeUserService.entity.Languagekeymapping;


@Transactional
public interface LanguageKeyMappingDAO extends CrudRepository<Languagekeymapping, Integer>{

	@Query(value="SELECT new com.kyobeeUserService.dto.LanguageMasterDTO(l.langID,l.langName,l.langIsoCode,lm.keyName,lm.value) FROM OrganizationLang ol " +
			"join LangMaster l on l.langID=ol.langmaster.langID " +
			"join Languagekeymapping lm on l.langIsoCode=lm.langIsoCode " +
			"where ol.organization.organizationID=:orgId order by l.langID")
	List<LanguageMasterDTO> fetchLanguageKeyMapForOrganization(@Param("orgId") Integer orgId);
}
