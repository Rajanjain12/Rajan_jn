package com.kyobeeUserService.dao;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kyobeeUserService.dto.OrganizationTypeDTO;
import com.kyobeeUserService.entity.OrganizationType;

@Repository
@Transactional
public interface OrganizationTypeDAO  extends CrudRepository<OrganizationType,Integer>{
	
	@Query(value="select ot from OrganizationType ot where ot.typeID=:typeId")
	OrganizationType fetchOrganizationType(@Param("typeId") Integer typeId);
	
	@Query(value="select new com.kyobeeUserService.dto.OrganizationTypeDTO(ot.typeID,ot.typeName) from OrganizationType ot")
	List<OrganizationTypeDTO> fetchAllOrganizationType();

}
