package com.excilys.cdb.service;

import java.util.List;


public interface Service<T> {

	/**
	 *
	 * @return the list containing all the objects from the rows of the table
	 */
	List<T> listAll();

	/**
     * @param offset the number of rows we first ignore
     * @param limit the maximum number of elements
     * @return the list containing at most limit elements from offset
	 */
	List<T> listPage(int offset, int limit);
	
	/**
	 * Count the number of rows in the table
	 * @return the total number of rows in the table
	 */
	int count();
}