package com.excilys.cdb.pagination;

import java.util.List;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.persistence.CompanyDAO;

public class CompanyPagination extends Pagination<Company> {
	
	private CompanyDAO companyDAO;
	private List<Company> listCompany;


	public CompanyPagination(int countEntries, int countPerPage) {
		super(countEntries, countPerPage);
		companyDAO = new CompanyDAO();
	}
	
	public List<Company> listFromOffset() {
		if(!changed) {
			return listCompany;
		}
		int from = (getCurrentPage() - 1) * getCountPerPage();
		int offset = getCountPerPage();
		listCompany = companyDAO.findFromOffset(from, offset);
		changed = false;
		return listCompany;
	}
}
