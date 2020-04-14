package com.kyobeeUserService.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kyobeeUserService.entity.LangMaster;

@Repository
@Transactional
public interface LangMasterDAO extends CrudRepository<LangMaster, Integer> {

	@Query(value = "select lm from LangMaster lm where lm.langID=:langID")
	LangMaster fetchLang(@Param("langID") Integer langID);

}
