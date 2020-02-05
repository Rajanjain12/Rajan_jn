package com.kyobeeWaitlistService.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.kyobeeWaitlistService.entity.Lookup;

public interface LookupDAO extends CrudRepository<Lookup, Integer>{
	
	@Query("FROM Lookup where LookupID in :lookupId")
	List<Lookup> fetchLookup(@Param("lookupId") String[] lookupId);

}
