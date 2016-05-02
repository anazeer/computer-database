package com.excilys.cdb.pagination.implementation;

import com.excilys.cdb.mapper.implementation.ComputerMapper;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.pagination.AbstractPage;
import com.excilys.cdb.pagination.util.PageRequest;

/**
 * Page implementation for computers
 */
public final class ComputerPage extends AbstractPage<Computer> {

	/**
	 * Computer pagination object
	 * @param pageRequest the page request
	 * @param totalCount the total number of elements
	 */
	public ComputerPage(ComputerMapper computerMapper, PageRequest pageRequest, int totalCount) {
		super(computerMapper, pageRequest, totalCount);
	}
}