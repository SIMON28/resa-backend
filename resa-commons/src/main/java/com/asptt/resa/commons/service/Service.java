package com.asptt.resa.commons.service;

import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

/**
 *
 */
public interface Service<R, S> {

	String getId(R resource);
	
	void setId(R resource);

	R create(R resource);

	R get(String id);

	List<R> find();

	List<R> find(MultivaluedMap<String, String> criteria);

	R update(R resource);

	void delete(String id);

	List<S> findSousResource(String id);

}
