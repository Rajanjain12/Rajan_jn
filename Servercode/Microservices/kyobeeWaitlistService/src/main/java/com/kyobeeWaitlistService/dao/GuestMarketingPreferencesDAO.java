package com.kyobeeWaitlistService.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.kyobeeWaitlistService.entity.GuestMarketingPreferences;

@Repository
public interface GuestMarketingPreferencesDAO extends CrudRepository<GuestMarketingPreferences, Integer>{

	@Modifying
	@Query("delete from GuestMarketingPreferences gm where gm.guest.guestID = :guestId")
	void deleteGuestMarketingPref(@Param("guestId") Integer guestId);
}
