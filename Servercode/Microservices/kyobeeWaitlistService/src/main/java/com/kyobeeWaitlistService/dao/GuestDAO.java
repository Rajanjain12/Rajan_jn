package com.kyobeeWaitlistService.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.kyobeeWaitlistService.entity.Guest;

public interface GuestDAO extends CrudRepository<Guest,Integer>{
	
	@Query(value="SELECT * FROM GUEST g join LANGMASTER l on l.langID=g.languagePrefID WHERE g.status ='CHECKIN' and g.resetTime is null and g.OrganizationID=:orgId order by g.rank asc limit :pageSize OFFSET :startIndex",nativeQuery=true)
	public List<Guest> fetchCheckinGuestList(@Param("orgId") Integer orgId,@Param("pageSize") Integer pageSize,@Param("startIndex") Integer startIndex); 
}
