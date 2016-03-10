package com.excilys.cdb.service;

import java.util.List;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.persistence.CompanyDAO;

public class CompanyService implements ServiceOperations {
	
	private CompanyDAO companyDAO;
	
	public CompanyService() {
		super();
		companyDAO = new CompanyDAO();
	}
	
	@Override
	public void list() {
		List<Company> listCompany = companyDAO.findAll();
		System.out.println("---------------------");
		System.out.println("- List of companies -");
		System.out.println("---------------------");
		for(Company company : listCompany) {
			System.out.println(company);
		}
	}
	
	@Override
	public int countEntries() {
		return companyDAO.countEntries();
	}
}