package com.excilys.cdb.pagination;


import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.mapper.MapperFactory;
import com.excilys.cdb.service.ServiceFactory;

/**
 * Pagination implementation for computers
 */
public final class ComputerPagination extends Pagination<Computer> {

    /**
     *
     * @param count the total number of elements
     * @param limit the maximum number of elements per page
     */
	public ComputerPagination(int count, int limit) {
		super(count, limit);
		service = ServiceFactory.getComputerService();
        mapper = MapperFactory.getComputerMapper();
	}
}