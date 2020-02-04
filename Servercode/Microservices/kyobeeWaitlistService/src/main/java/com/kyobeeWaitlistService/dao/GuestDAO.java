package com.kyobeeWaitlistService.dao;

import java.util.Map;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.kyobeeWaitlistService.entity.Guest;

@Repository
public interface GuestDAO extends CrudRepository<Guest, Integer> {

	@Query("select count(*) from Guest where status='CHECKIN' and resetTime is  null and calloutCount is null and incompleteParty is null and guestID <:guestId and organizationID =:orgId")
	Integer getGuestCount(@Param("orgId") Integer orgId,@Param("guestId") Integer guestId);
	
	@Query("select rank from Guest where incompleteParty is null and status in('CHECKIN') and organizationID =:orgId and guestID=:guestId")
	Integer getGuestRank(@Param("orgId") Integer orgId,@Param("guestId") Integer guestId);
	
	@Procedure(name = "CALCHEADERMETRICS")
	Map<String, Object> getOrganizationMetrics(@Param("ORGID") Integer orgid);
	
	
}
