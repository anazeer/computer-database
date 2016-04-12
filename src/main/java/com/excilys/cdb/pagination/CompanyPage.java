package com.excilys.cdb.pagination;


import com.excilys.cdb.model.Company;
import com.excilys.cdb.persistence.mapper.MapperFactory;
import com.excilys.cdb.service.Query;
import com.excilys.cdb.service.ServiceFactory;

/**
 * Page implementation for companies
 */
public final class CompanyPage extends AbstractPage<Company> {
	
	/**
	 *
	 * @param query the page query
	 * @param currentPage the current page
	 */
	public CompanyPage(Query query, int currentPage) {
		super(ServiceFactory.getCompanyService(), query, currentPage);
        mapper = MapperFactory.getCompanyMapper();
	}
}