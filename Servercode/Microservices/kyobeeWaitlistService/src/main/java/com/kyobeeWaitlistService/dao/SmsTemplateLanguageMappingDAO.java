package com.kyobeeWaitlistService.dao;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.kyobeeWaitlistService.entity.SmsTemplateLanguageMapping;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
@Transactional
public interface SmsTemplateLanguageMappingDAO extends CrudRepository<SmsTemplateLanguageMapping, Integer>{

	@Query("select st from SmsTemplateLanguageMapping st join st.langmaster l  where l.langID=:langId")
	List<SmsTemplateLanguageMapping> fetchSmsTemplate(@Param("langId") Integer langId);
}
