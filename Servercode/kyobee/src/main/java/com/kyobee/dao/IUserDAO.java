// created by pampaniya shweta for forgot password
package com.kyobee.dao;

import com.kyobee.entity.User;
import com.kyobee.exception.RsntException;

public interface IUserDAO 
{
	public String getUserAuthCode(long userId) throws RsntException;
	
	public User resetPassword(long userId,String password) throws RsntException; 
}
