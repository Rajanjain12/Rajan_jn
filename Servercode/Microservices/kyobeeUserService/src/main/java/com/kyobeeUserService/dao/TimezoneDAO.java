package com.kyobeeUserService.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kyobeeUserService.dto.TimezoneDTO;
import com.kyobeeUserService.entity.Timezone;

@Repository
@Transactional
public interface TimezoneDAO extends JpaRepository<Timezone, Integer> {

	@Query("select new com.kyobeeUserService.dto.TimezoneDTO(t.timezoneId,t.timezoneName,t.offset) from Timezone t order by t.timezoneId")
	public List<TimezoneDTO> fetchTimezoneList();
}
