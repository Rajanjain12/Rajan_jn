package com.kyobeeWaitlistService.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kyobeeWaitlistService.dto.LanguageKeyMappingDTO;
import com.kyobeeWaitlistService.entity.LangMaster;

@Repository
@Transactional
public interface LangMasterDAO extends CrudRepository<LangMaster, Integer> {

	@Query("select new com.kyobeeWaitlistService.dto.LanguageKeyMappingDTO(l.langID,l.langName,l.langIsoCode) from LangMaster l ")
	public List<LanguageKeyMappingDTO> getLanguageDetails();
}
