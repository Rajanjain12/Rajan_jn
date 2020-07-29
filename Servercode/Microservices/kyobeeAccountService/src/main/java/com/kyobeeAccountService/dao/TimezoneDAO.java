package com.kyobeeAccountService.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.kyobeeAccountService.entity.Timezone;

@Repository
@Transactional
public interface TimezoneDAO extends JpaRepository<Timezone, Integer> {

}
