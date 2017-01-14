package com.kyobee.dao.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.kyobee.dao.ApplicationDAO;

/**
 * This is the framework DAOImpl class, which holds the basic operation which
 * will be applicable for each DAOImpl we are going to implement. All the
 * DAOImpl should extend this.
 * 
 * @author rohit
 *
 * @param <T>
 * @param <PK>
 */
public class ApplicationDAOImpl<T extends Serializable, PK extends Serializable> implements ApplicationDAO<T, PK> {

	@Autowired
	private SessionFactory sessionFactory;

	private Class<T> type;

	@SuppressWarnings("unchecked")
	public ApplicationDAOImpl() {
		Type t = getClass().getGenericSuperclass();
		ParameterizedType pt = (ParameterizedType) t;
		type = (Class<T>) pt.getActualTypeArguments()[0];
	}

	@SuppressWarnings("unchecked")
	@Override
	public T find(PK id) {
		return (T) sessionFactory.getCurrentSession().get(type, id);
	}

	@Override
	public Long save(T t) {
		return (Long) sessionFactory.getCurrentSession().save(t);
	}

	@Override
	public void update(T t) {
		sessionFactory.getCurrentSession().update(t);
	}

	@Override
	public void delete(T t) {
		sessionFactory.getCurrentSession().delete(t);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> findAll() {
		return sessionFactory.getCurrentSession().createQuery("FROM " + type.getName()).list();
	}

	@Override
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	
}
