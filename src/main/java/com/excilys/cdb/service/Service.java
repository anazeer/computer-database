package com.excilys.cdb.service;

import java.util.List;

/**
 * Generic service for accessing services 
 * @author excilys
 *
 * @param <T>
 */
public interface Service<T> {
	
	/**
	 * 
	 * Get all the objects satisfying the query constraints
	 * @param query the object containing the query constraints 
	 * @return the list containing the result
	 */
	List<T> list(Query query);
	
	/**
	 * 
	 * @param query the object containing the query constraints
	 * @return the number of elements satisfying the query constraints
	 */
	int count(Query query);
	
	/**
	 * Delete the row referenced by id
	 * @param id the row reference
	 */
	void delete(Long id);
}