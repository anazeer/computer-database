package com.excilys.cdb.persistence.dao;


import java.util.List;

import com.excilys.cdb.exception.DAOException;
import com.excilys.cdb.service.util.Query;

/**
 * Generic DAO for CRUD implementation
 * @param <T>
 */
    interface IDAO<T> {

    /**
     * Non implemented method error message
     */
	String unimplementedException = "Call to non implemented method";
	
	/**
	 * 
	 * @param query the object containing the query constraints
	 * @return the number of rows in the table satisfying the query constraints
	 */
	int count(Query query);
	
	/**
	 * Get all the objects from the database satisfying the query constraints
	 * @param query the object containing the query constraints 
	 * @return the list containing the result
	 */
	List<T> find(Query query);
	
	/**
	 * 
	 * @param id the row primary key
	 * @return the object representing the table's row referenced by id
	 */
	T findById(Long id);
		
	/**
	 * 
	 * @param obj the object we want to persist
	 * @return the object completed by its fresh id if the object has been successfully inserted in the table
	 */
	default T create(T obj) throws NoSuchMethodError, DAOException {
		throw new NoSuchMethodError(unimplementedException);
	}
	
	/**
	 * Updates the row referenced by the same id as obj, with obj fields
	 * @param obj the object containing the updated fields
	 * @return true if the object has been successfully updated in the table
	 */
	default boolean update(T obj) throws NoSuchMethodError, DAOException {
		throw new NoSuchMethodError(unimplementedException);
	}
	
	/**
	 * Deletes the object referenced by id
	 * @param id the reference we want to delete
	 * @return true if the object has been successfully deleted from the table
	 */
	default boolean delete(Long id) throws NoSuchMethodError, DAOException {
		throw new NoSuchMethodError(unimplementedException);
	}
}