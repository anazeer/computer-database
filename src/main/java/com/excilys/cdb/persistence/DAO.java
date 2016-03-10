package com.excilys.cdb.persistence;

import java.util.List;

import com.excilys.cdb.exception.UnimplementedException;

/**
 * Generic DAO for CRUD implementation
 * @author excilys
 *
 * @param <T>
 */
public interface DAO<T> {

	final String unimplementedException = "Call to non inplemented method";
	
	/**
	 * 
	 * @return a list with all table's elements
	 */
	List<T> findAll();
	
	/**
	 * 
	 * @param id
	 * @return the object referenced by id
	 */
	T findById(Long id);
	
	/**
	 * 
	 * @return the number of rows in the table
	 */
	int countEntries();
	
	/**
	 * Retrieves offset number of rows after the from number row
	 * @param from
	 * @param to
	 * @return
	 */
	List<T> findFromOffset(int from, int offset);
	
	/**
	 * 
	 * @param obj the object we want to persist
	 * @return true if the object has been successfully inserted in the table
	 */
	default public T create(T obj) throws UnimplementedException {
		throw new UnimplementedException(unimplementedException);
	}
	
	/**
	 * 
	 * @param obj 
	 * @return true if the object has been successfully updated in the table
	 */
	default public boolean update(T obj) throws UnimplementedException {
		throw new UnimplementedException(unimplementedException);
	}
	
	/**
	 * 
	 * @param obj
	 * @return true if the object has been successfully deleted from the table
	 */
	default public boolean delete(T obj) throws UnimplementedException {
		throw new UnimplementedException(unimplementedException);
	}
	
}
