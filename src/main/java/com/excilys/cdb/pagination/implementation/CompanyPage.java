package com.excilys.cdb.pagination.implementation;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.pagination.AbstractPage;
import com.excilys.cdb.pagination.util.PageRequest;

/**
 * Page implementation for companies
 */
public final class CompanyPage extends AbstractPage<Company> {
	
	/**
	 * Company pagination object
	 * @param pageRequest the page request
	 * @param totalCount the total number of elements
	 */
	public CompanyPage(PageRequest pageRequest, int totalCount) {
		super(pageRequest, totalCount);
	}
}