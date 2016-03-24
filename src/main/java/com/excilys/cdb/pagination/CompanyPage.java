package com.excilys.cdb.pagination;


import com.excilys.cdb.model.Company;
import com.excilys.cdb.persistence.mapper.MapperFactory;
import com.excilys.cdb.service.ServiceFactory;

/**
 * Page implementation for companies
 */
public final class CompanyPage extends Page<Company> {

    /**
     *
     * @param count the total number of elements
     * @param limit the maximum number of elements per page
     */
	public CompanyPage(int count, int limit) {
		super(count, limit);
		service = ServiceFactory.getCompanyService();
        mapper = MapperFactory.getCompanyMapper();
	}
}