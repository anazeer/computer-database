package com.excilys.cdb.persistence.mapper;

import java.sql.ResultSet;

/**
 * 
 * @author excilys
 *
 * @param <T>
 */
public interface DAOMapper<T> {

	public T find(ResultSet result);

}
