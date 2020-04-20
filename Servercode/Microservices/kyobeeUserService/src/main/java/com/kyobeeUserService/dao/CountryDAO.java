package com.kyobeeUserService.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kyobeeUserService.entity.Country;

@Repository
@Transactional
public interface CountryDAO extends JpaRepository<Country, Integer> {
	
	Country findByCountryName(String country);
	
	@Query("select c.countryName from Country c")
	public List<String> fetchCountryList();

}
