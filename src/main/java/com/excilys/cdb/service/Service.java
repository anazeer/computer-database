package com.excilys.cdb.service;

import java.util.List;


public interface Service<T> {

	/**
	 *
	 * @return the list containing all the objects from the rows of the table
	 */
	List<T> listAll();
	
	/**
	 * 
	 * @param filter the filter for searching
	 * @return the list containing all the objects from the rows of the table
	 */
	List<T> listAll(String filter);

	/**
     * @param offset the number of rows we first ignore
     * @param limit the maximum number of elements
     * @return the list containing at most limit elements from offset
	 */
	List<T> listPage(int offset, int limit);
	
	/**
     * @param offset the number of rows we first ignore
     * @param limit the maximum number of elements
     * @param order the result order
     * @return the list containing at most limit elements from offset
	 */
	List<T> listPage(int offset, int limit, Order order);
	
	/**
	 * 
     * @param offset the number of rows we first ignore
     * @param limit the maximum number of elements
	 * @param filter the filter for searching
	 * @return the list containing at most limit elements from offset
	 */
	List<T> listPage(int offset, int limit, String filter);
	
	/**
	 * 
     * @param offset the number of rows we first ignore
     * @param limit the maximum number of elements
	 * @param filter the filter for searching
     * @param order the result order
	 * @return the list containing at most limit elements from offset
	 */
	List<T> listPage(int offset, int limit, String filter, Order order);
	
	/**
	 * Count the number of rows in the table
	 * @return the total number of rows in the table
	 */
	int count();
	
	/**
	 * Count the number of rows in the table
	 * @param filter the research filter
	 * @return the total number of rows in the table
	 */
	int count(String filter);
	
	/**
	 * Delete the row referenced by id
	 * @param id the row reference
	 * @return
	 */
	boolean delete(Long id);
}