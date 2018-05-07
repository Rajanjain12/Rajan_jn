//created by pampaniya shweta for forgot password
package com.kyobee.dao;

import org.springframework.stereotype.Repository;

import com.kyobee.entity.User;
import com.kyobee.exception.RsntException;

@Repository
public interface IUserDAO 
{
	public String getUserAuthCode(long userId) throws RsntException;
	
	public User resetPassword(long userId,String password) throws RsntException; 
}