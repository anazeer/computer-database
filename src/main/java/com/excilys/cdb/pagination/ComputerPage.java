package com.excilys.cdb.pagination;


import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.mapper.MapperFactory;
import com.excilys.cdb.service.Query;
import com.excilys.cdb.service.ServiceFactory;

/**
 * Page implementation for computers
 */
public final class ComputerPage extends AbstractPage<Computer> {

    /**
     *
     * @param query the page query
     * @param currentPage the current page
     */
	public ComputerPage(Query query, int currentPage) {
		super(query, currentPage);
		service = ServiceFactory.getComputerService();
        mapper = MapperFactory.getComputerMapper();
	}
}