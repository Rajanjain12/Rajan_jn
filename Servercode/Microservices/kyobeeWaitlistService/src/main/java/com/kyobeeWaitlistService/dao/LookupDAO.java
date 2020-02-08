package com.kyobeeWaitlistService.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.kyobeeWaitlistService.entity.Lookup;

public interface LookupDAO extends CrudRepository<Lookup, Integer>{
	
	@Query("FROM Lookup where LookupID in :lookupId")
	List<Lookup> fetchLookup(@Param("lookupId") String[] lookupId);
	
	@Query("select l from GuestMarketingPreferences gmp join gmp.lookup l where gmp.guest.guestID=:guestId")
	List<Lookup> fetchLookupForGuest(@Param("guestId") Integer guestId);

}
