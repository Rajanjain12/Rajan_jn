package com.kyobee.dao.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.kyobee.dao.IGenericDAO;
import com.kyobee.util.common.LoggerUtil;
import com.mysql.jdbc.log.LogUtils;

/**
 * This is the framework DAOImpl class, which holds the basic operation which
 * will be applicable for each DAOImpl we are going to implement. All the
 * DAOImpl should extend this.
 * 
 * @author Aarshi Patel
 *
 * @param <T>
 * @param <PK>
 */
public class GenericDAOImpl<T extends Serializable, PK extends Serializable> implements IGenericDAO<T, PK> {

	@Autowired
	private SessionFactory sessionFactory;

	private Class<T> type;

	@SuppressWarnings("unchecked")
	public GenericDAOImpl() {
		Type t = getClass().getGenericSuperclass();
		ParameterizedType pt = (ParameterizedType) t;
		type = (Class<T>) pt.getActualTypeArguments()[0];
	}

	@SuppressWarnings("unchecked")
	@Override
	public T find(PK id) {
		
		return (T) sessionFactory.getCurrentSession().get(type, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public PK save(T t) {
		return (PK) sessionFactory.getCurrentSession().save(t);
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