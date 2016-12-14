package com.rsnt.service;

import java.util.List;

import javax.ejb.Local;

import com.rsnt.common.exception.RsntException;

@Local
public interface IOptionService {

	public String SEAM_NAME = "optionService";
	public String JNDI_NAME="OptionServiceImpl";
	
	//public Object persist(Object object) throws RsntException;
	
	public Object merge(Object object) throws RsntException;
	
	public Object find(Long orgOptionId) throws RsntException;
	
	public List<Object[]> loadOptionData(Long pOrgId) throws RsntException;
	
	public void deleteOption(Object object) throws RsntException;
	
	public int findDuplicateOption(String optionName,Long orgOptionId) throws RsntException;
	
}
