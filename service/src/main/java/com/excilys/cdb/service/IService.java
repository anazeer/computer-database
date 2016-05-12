package com.excilys.cdb.service;

import java.util.List;

import com.excilys.cdb.pagination.AbstractPage;
import com.excilys.cdb.pagination.util.PageRequest;
import com.excilys.cdb.util.Constraint;

/**
 * Generic service for accessing services 
 * @param <T>
 */
public interface IService<T> {
	
	/**
	 *
	 * @param id the object reference
	 * @return the object referenced by id
	 */
	T findById(Long id);
	
	/**
	 * 
	 * @param query the object containing the query constraints 
	 * @param currentPage the number of the requested page
	 * @return the list containing the result
	 */
	AbstractPage<T> getPage(PageRequest page);
	
	/**
	 * Get all the objects satisfying the query constraints
	 * @param query the object containing the query constraints 
	 * @return the list containing the result
	 */
	List<T> list(Constraint constraint);
	
	/**
	 * 
	 * @param query the object containing the query constraints
	 * @return the number of elements satisfying the query constraints
	 */
	int count(Constraint constraint);
	
	/**
	 * Persist the object
	 * @param obj the object to persist
	 * @return the object with the new id
	 */
	default T create(T obj) {
		throw new NoSuchMethodError("Call to non implemented method");
	}
	
	/**
	 * Update the object
	 * @param obj the object with an existing id and the new state
	 */
	default void update(T obj) {
		throw new NoSuchMethodError("Call to non implemented method");
	}
	
	/**
	 * Delete the row referenced by id
	 * @param id the row reference
	 * @return true if the object have been successfully deleted
	 */
	boolean delete(Long id);
}