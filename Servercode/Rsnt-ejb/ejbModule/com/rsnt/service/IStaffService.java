package com.rsnt.service;

import java.util.List;

import javax.ejb.Local;

import com.rsnt.common.exception.RsntException;
import com.rsnt.entity.User;

@Local
public interface IStaffService {
	
	public String SEAM_NAME = "staffService";
	public String JNDI_NAME="StaffServiceImpl";
	
	public List<Object[]> loadStaffData(Long orgId) throws RsntException;
	public void persist(Object object) throws RsntException;
	public Object merge(Object object) throws RsntException;
	public Object find(Long id) throws RsntException;
	public List<User> findUserByUserName(String userName);
	public void updatePassword(String newPassword, Long userId) throws RsntException;
}
