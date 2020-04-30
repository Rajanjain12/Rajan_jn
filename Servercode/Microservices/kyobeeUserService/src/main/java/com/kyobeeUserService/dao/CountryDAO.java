package com.kyobeeUserService.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kyobeeUserService.dto.CountryDTO;
import com.kyobeeUserService.entity.Country;

@Repository
@Transactional
public interface CountryDAO extends JpaRepository<Country, Integer> {
	
	Country findByCountryName(String country);
	
	@Query("select new com.kyobeeUserService.dto.CountryDTO(c.countryName,c.ISOCode) from Country c order by c.countryID")
	public List<CountryDTO> fetchCountryList();
	
	

}
