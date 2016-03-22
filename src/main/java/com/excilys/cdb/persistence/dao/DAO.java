package com.excilys.cdb.persistence.dao;

import com.excilys.cdb.persistence.ConnectionSingleton;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Generic DAO for CRUD implementation
 * @author excilys
 *
 * @param <T>
 */
    interface DAO<T> {

    /**
     * The user connection
     */
    Connection conn = ConnectionSingleton.getInstance();

    /**
     * Non implemented method error message
     */
	final String unimplementedException = "Call to non implemented method";
	
	/**
	 * 
	 * @return the list containing all the objects from the rows of the table
	 */
	List<T> findAll();
	
	/**
	 * 
	 * @param filter filter for the research
	 * @return the list containing the filtered rows of the table
	 */
	List<T> findAll(String filter);
	
	/**
	 * 
	 * @param id the row primary key
	 * @return the object representing the table's row referenced by id
	 */
	T findById(Long id);
	
	/**
	 * 
	 * @return the total number of rows in the table
	 */
	int count();
	
	/**
	 * @param filter the research filter
	 * @return the number of rows in the table satisfying the filter
	 */
	int count(String filter);
	
	/**
	 *
	 * @param offset the number of rows we first ignore
	 * @param limit the maximum number of elements
	 * @return the list containing at most limit elements from offset
	 */
	List<T> findPage(int offset, int limit);
	
	/**
	 * 
	 * @param offset the number of rows we first ignore
	 * @param limit the maximum number of elements
	 * @param filter the filter for the research
	 * @return the list containing at most limit elements from offset
	 */
	List<T> findPage(int offset, int limit, String filter);
	
	/**
	 * 
	 * @param obj the object we want to persist
	 * @return the object completed by its fresh id if the object has been successfully inserted in the table
	 */
	default T create(T obj) throws NoSuchMethodError, SQLException {
		throw new NoSuchMethodError(unimplementedException);
	}
	
	/**
	 * Updates the row referenced by the same id as obj, with obj fields
	 * @param obj the object containing the updated fields
	 * @return true if the object has been successfully updated in the table
	 */
	default boolean update(T obj) throws NoSuchMethodError, SQLException {
		throw new NoSuchMethodError(unimplementedException);
	}
	
	/**
	 * Deletes the object referenced by the same id as obj
	 * @param obj the object with the correct id we want to delete
	 * @return true if the object has been successfully deleted from the table
	 */
	default boolean delete(T obj) throws NoSuchMethodError {
		throw new NoSuchMethodError(unimplementedException);
	}
}
