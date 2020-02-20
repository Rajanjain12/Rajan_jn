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

	// for fetching guest check in
	@Query(value = "SELECT * FROM GUEST g join LANGMASTER l on l.langID=g.languagePrefID WHERE g.status ='CHECKIN' and g.resetTime is null and g.OrganizationID=:orgId order by g.rank asc limit :pageSize OFFSET :startIndex", nativeQuery = true)
	public List<Guest> fetchCheckinGuestList(@Param("orgId") Integer orgId, @Param("pageSize") Integer pageSize,
			@Param("startIndex") Integer startIndex);

	// for fetching guest related details by search Text
	@Query(value = "SELECT * FROM GUEST g join LANGMASTER l on l.langID=g.languagePrefID WHERE g.status ='CHECKIN' and g.resetTime is null and g.OrganizationID=:orgId and ( g.name like :searchText or g.sms like :searchText) order by g.rank asc limit :pageSize OFFSET :startIndex", nativeQuery = true)
	public List<Guest> fetchCheckinGuestBySearchText(@Param("orgId") Integer orgId, @Param("pageSize") Integer pageSize,
			@Param("startIndex") Integer startIndex, @Param("searchText") String searchText);

	// for fetching guest details by id
	@Query(value = "select g from Guest g join LangMaster l on l.langID=g.langmaster.langID WHERE g.status ='CHECKIN' and g.resetTime is null and g.guestID=:guestId")
	public Guest fetchGuestById(@Param("guestId") Integer guestId);

	// for fetching guest details by uuid
	@Query(value = "select g from Guest g join LangMaster l on l.langID=g.langmaster.langID WHERE g.status ='CHECKIN' and g.resetTime is null and g.uuid=:guestUUID")
	public Guest fetchGuestByUUID(@Param("guestUUID") String guestUUID);
	

}
