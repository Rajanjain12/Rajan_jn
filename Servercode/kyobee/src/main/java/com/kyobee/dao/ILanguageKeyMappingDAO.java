package com.kyobee.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.kyobee.util.AppTransactional;

/*
created by arjun for language key mapping table (11/12/2019)

*/
@Repository
@AppTransactional
public interface ILanguageKeyMappingDAO {

	List<Object[]> getKeyFromLanguageKeyMappingByValue(Long orgId, String lookUpTypeId);

}
