package com.kyobeeUserService.dao;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kyobeeUserService.entity.SmsTemplateLanguageMapping;

@Repository
@Transactional
public interface SmsTemplateLanguageMappingDAO extends CrudRepository<SmsTemplateLanguageMapping, Integer>{

	@Query("select st from SmsTemplateLanguageMapping st join st.langmaster l  where l.langID=:langId")
	List<SmsTemplateLanguageMapping> fetchSmsTemplate(@Param("langId") Integer langId);
}
