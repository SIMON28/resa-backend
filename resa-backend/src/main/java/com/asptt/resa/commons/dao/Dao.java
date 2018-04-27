package com.asptt.resa.commons.dao;

import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

/**
 *
 * @author deyb6792
 */
public interface Dao<T> {

	T create(T resource);

	T get(String id);

	List<T> find();

	List<T> find(MultivaluedMap<String, String> criteria);

	T update(T resource);

	void delete(String id);

}
