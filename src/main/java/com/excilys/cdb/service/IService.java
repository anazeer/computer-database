package com.excilys.cdb.service;

import java.util.List;

import com.excilys.cdb.exception.DAOException;
import com.excilys.cdb.pagination.AbstractPage;
import com.excilys.cdb.pagination.util.PageRequest;
import com.excilys.cdb.service.util.Query;

/**
 * Generic service for accessing services 
 * @param <T>
 */
public interface IService<T> {
	
	/**
	 * 
	 * @param query the object containing the query constraints 
	 * @param currentPage the number of the requested page
	 * @return the list containing the result
	 */
	public AbstractPage<T> getPage(PageRequest page);
	
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
	 * @throws DAOException 
	 */
	void delete(Long id) throws DAOException;
}