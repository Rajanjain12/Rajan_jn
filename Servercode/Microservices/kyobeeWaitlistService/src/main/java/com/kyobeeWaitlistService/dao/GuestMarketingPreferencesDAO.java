package com.kyobeeWaitlistService.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.kyobeeWaitlistService.entity.GuestMarketingPreferences;

@Repository
public interface GuestMarketingPreferencesDAO extends CrudRepository<GuestMarketingPreferences, Integer>{

}
