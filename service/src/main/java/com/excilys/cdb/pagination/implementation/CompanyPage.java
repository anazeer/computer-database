package com.excilys.cdb.pagination.implementation;

import com.excilys.cdb.mapper.implementation.CompanyMapper;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.pagination.AbstractPage;
import com.excilys.cdb.pagination.util.PageRequest;

/**
 * Page implementation for companies
 */
public final class CompanyPage extends AbstractPage<Company> {
	
	/**
	 * Company pagination object
	 * @param companyMapper the company mapper
	 * @param pageRequest the page request
	 * @param totalCount the total number of elements
	 */
	public CompanyPage(CompanyMapper companyMapper, PageRequest pageRequest, int totalCount) {
		super(companyMapper, pageRequest, totalCount);
	}
}