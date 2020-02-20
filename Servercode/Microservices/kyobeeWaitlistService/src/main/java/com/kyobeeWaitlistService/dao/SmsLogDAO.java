package com.kyobeeWaitlistService.dao;

import org.springframework.data.repository.CrudRepository;

import com.kyobeeWaitlistService.entity.SmsLog;

public interface SmsLogDAO extends CrudRepository<SmsLog, Integer> {

}
