package com.rsnt.common.service.impl;

import java.util.Date;

import org.jboss.seam.annotations.In;

import com.rsnt.common.entity.BaseEntity;
import com.rsnt.entity.User;

public abstract class RsntBaseService {

	@In
    private User loginUser;
	
	protected String getCreatedBy()
	{
		return loginUser.getUserName();
		
	}
	
	protected Date getCreatedAt()
	{
		return new Date();
	}
	
	protected String getModifiedBy()
	{
		return loginUser.getUserName();
		
	}
	
	protected Date getModifiedAt()
	{
		return new Date();
	}
	
	protected void setCreatedAtttributes(BaseEntity abstractEntity)
	{
		abstractEntity.setCreatedBy(this.getCreatedBy());
		abstractEntity.setCreatedDate(this.getCreatedAt());
	}
	
	protected void setModifiedAtttributes(BaseEntity abstractEntity)
	{
		abstractEntity.setModifiedBy(getModifiedBy());
		abstractEntity.setModifiedDate(getModifiedAt());
	}

	public User getLoginUser() {
		return loginUser;
	}

	public void setLoginUser(User loginUser) {
		this.loginUser = loginUser;
	}
}
