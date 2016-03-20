package com.excilys.cdb.service;

import java.util.List;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.persistence.dao.CompanyDAO;
import com.excilys.cdb.persistence.dao.DAOFactory;

/**
 * Service implementation for companies
 */
public class CompanyService implements Service<Company> {
	
	private CompanyDAO companyDAO;
	private static CompanyService instance;
	
	private CompanyService() {
		super();
		companyDAO = DAOFactory.getCompanyDAO();
	}
	
	public static CompanyService getInstance() {
		if(instance == null) {
			instance = new CompanyService();
		}
		return instance;
	}
	
	@Override
	public List<Company> listAll() {
		return companyDAO.findAll();
	}
	
	@Override
	public List<Company> listPage(int offset, int limit) {
		return companyDAO.findPage(offset, limit);
	}
	
	@Override
	public int count() {
		return companyDAO.count();
	}
}