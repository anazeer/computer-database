package com.excilys.cdb.pagination;


import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.mapper.MapperFactory;
import com.excilys.cdb.service.ServiceFactory;

/**
 * Page implementation for computers
 */
public final class ComputerPage extends Page<Computer> {

    /**
     *
     * @param count the total number of elements
     * @param limit the maximum number of elements per page
     */
	public ComputerPage(int count, int limit) {
		super(count, limit);
		service = ServiceFactory.getComputerService();
        mapper = MapperFactory.getComputerMapper();
	}
}