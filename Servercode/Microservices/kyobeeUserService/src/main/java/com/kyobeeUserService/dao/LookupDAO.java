package com.kyobeeUserService.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.kyobeeUserService.entity.Lookup;

@Transactional
public interface LookupDAO extends CrudRepository<Lookup, Integer>{

	@Query(value="SELECT distinct * FROM KyobeeTechRevamp.ORGANIZATIONCATEGORY oc " + 
			"inner join KyobeeTechRevamp.LOOKUP l on oc.CategoryTypeID=l.LookupTypeID where (oc.OrganizationID=:orgId and oc.CategoryTypeID in(:seatingLookupId,:marketingLookupId) and oc.CategoryValueID=l.LookupID) ",nativeQuery=true)
	List<Lookup> fetchSeatingAndMarketingPref(@Param("orgId") Integer orgId,@Param("seatingLookupId") Integer seatingPrefLookupId,@Param("marketingLookupId") Integer marketingPrefLookupId);
}
