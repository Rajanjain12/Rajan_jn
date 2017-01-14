package com.kyobee.dao.impl;

import org.hibernate.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import com.kyobee.dao.UserDAO;
import com.kyobee.entity.User;
import com.kyobee.exception.RsntException;

@Component
@Service
public class UserDAOImpl extends ApplicationDAOImpl<User, Integer> implements UserDAO {

	@Override
	public User fetchUserByUserName(String userName) throws RsntException {
		String queryStr = "SELECT u FROM User u WHERE u.userName=:userName";
		Query query = getSessionFactory().getCurrentSession().createQuery(queryStr);
		query.setParameter("userName", userName);
		User user = (User) query.uniqueResult();
		return user;
	}
}
