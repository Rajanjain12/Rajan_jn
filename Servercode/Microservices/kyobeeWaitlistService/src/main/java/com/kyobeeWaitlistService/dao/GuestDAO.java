package com.kyobeeWaitlistService.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kyobeeWaitlistService.entity.Guest;

@Repository
@Transactional
public interface GuestDAO extends CrudRepository<Guest, Integer> {

	@Query("select count(*) from Guest where status='CHECKIN' and resetTime is  null and calloutCount is null and incompleteParty is null and guestID <:guestId and organizationID =:orgId")
	Integer getGuestCount(@Param("orgId") Integer orgId, @Param("guestId") Integer guestId);

	// to fetch guest rank for guest
	@Query("select rank from Guest where incompleteParty is null and status in('CHECKIN') and organizationID =:orgId and guestID=:guestId")
	Integer getGuestRank(@Param("orgId") Integer orgId, @Param("guestId") Integer guestId);

		// for fetching guest details by id
	@Query(value = "select g from Guest g join LangMaster l on l.langID=g.langmaster.langID WHERE g.status ='CHECKIN' and g.resetTime is null and g.guestID=:guestId")
	public Guest fetchGuestById(@Param("guestId") Integer guestId);

	// for fetching guest details by uuid
	@Query(value = "select g from Guest g join LangMaster l on l.langID=g.langmaster.langID WHERE g.status ='CHECKIN' and g.resetTime is null and g.uuid=:guestUUID")
	public Guest fetchGuestByUUID(@Param("guestUUID") String guestUUID);

}
