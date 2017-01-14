package com.kyobee.dao;

import java.io.Serializable;
import java.util.List;
import org.hibernate.SessionFactory;

/**
 * This is the framework DAO class, which holds the basic operation which will
 * be applicable for each DAO's we are going to implement. All the DAO should
 * extend this.
 * 
 * @author rohit
 *
 * @param <T>
 * @param <PK>
 */
public interface ApplicationDAO<T extends Serializable, PK extends Serializable> {

	public T find(PK id);

	public Long save(T t);

	public void update(T t);

	public void delete(T t);

	public List<T> findAll();
	
	public SessionFactory getSessionFactory();
}
