package com.kyobee.dao;

import com.kyobee.entity.User;
import com.kyobee.exception.RsntException;

/**
 * User DAO will be used to perform all the DB operations for the users table.
 * @author rohit
 *
 */
public interface UserDAO extends ApplicationDAO<User, Integer> {

	public User fetchUserByUserName(String username) throws RsntException;
}
