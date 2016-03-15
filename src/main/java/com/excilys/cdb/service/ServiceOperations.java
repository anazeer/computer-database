package com.excilys.cdb.service;

import java.util.List;

import com.excilys.cdb.service.dto.DTO;

public interface ServiceOperations<T> {

	/**
	 * List all the elements
	 * @return
	 */
	public List<? extends DTO> list();
	
	/**
	 * List a given number of elements from an offset
	 * @param from
	 * @param offset
	 * @return
	 */
	public List<? extends DTO> listFromOffset(int from, int offset);
	
	/**
	 * Count the number of row in the table
	 * @return
	 */
	public int countEntries();
	
	/**
	 * Create a DTO from the source model
	 * @param source
	 * @return
	 */
	DTO createDTO(T source);
	
	/**
	 * Get the relevant object from the DTO
	 * @param source
	 * @return
	 */
	T getFromDTO(DTO source);
}
