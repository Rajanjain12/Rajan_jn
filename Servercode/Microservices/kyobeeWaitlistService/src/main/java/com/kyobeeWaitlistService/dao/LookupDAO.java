package com.kyobeeWaitlistService.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.kyobeeWaitlistService.entity.Lookup;

@Transactional
public interface LookupDAO extends CrudRepository<Lookup, Integer>{
	
	@Query("FROM Lookup where LookupID in :lookupId")
	List<Lookup> fetchLookup(@Param("lookupId") String[] lookupId);
	
	@Query("select l from GuestMarketingPreferences gmp join gmp.lookup l where gmp.guest.guestID=:guestId")
	List<Lookup> fetchLookupForGuest(@Param("guestId") Integer guestId);
	
	@Query(value="SELECT distinct * FROM KyobeeTechRevamp.ORGANIZATIONCATEGORY oc " + 
			"inner join KyobeeTechRevamp.LOOKUP l on oc.CategoryTypeID=l.LookupTypeID where (oc.OrganizationID=:orgId and oc.CategoryTypeID in(:seatingLookupId,:marketingLookupId) and oc.CategoryValueID=l.LookupID) ",nativeQuery=true)
	List<Lookup> fetchOrgSeatingAndMarketingPref(@Param("orgId") Integer orgId,@Param("seatingLookupId") Integer seatingPrefLookupId,@Param("marketingLookupId") Integer marketingPrefLookupId);
	
	List<Lookup> findByLookupIDIn(List<Integer> lookupIDs);
	
	@Query(value="SELECT distinct * FROM KyobeeTechRevamp.LOOKUP l  " + 
			"inner join KyobeeTechRevamp.LOOKUPTYPE lt on l.LookupTypeID=lt.LookupTypeID where (l.LookupTypeID in(:seatingLookupId,:marketingLookupId) and l.LookupTypeID=lt.LookupTypeID);",nativeQuery=true)
	List<Lookup> fetchSeatingAndMarketingPref(@Param("seatingLookupId") Integer seatingPrefLookupId,@Param("marketingLookupId") Integer marketingPrefLookupId);

}
