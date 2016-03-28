package com.excilys.cdb.service;

import java.util.List;

import com.excilys.cdb.exception.DAOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
		if (instance == null) {
			instance = new CompanyService();
		}
		return instance;
	}

	@Override
	public List<Company> list(Query query) {
		return companyDAO.find(query);
	}

	@Override
	public int count(Query query) {
		return companyDAO.count(query);
	}

	@Override
	public void delete(Long id) {
		try {
            DAOFactory.initTransaction();
			DAOFactory.getComputerDAO().deleteByCompanyId(id);
			DAOFactory.getCompanyDAO().delete(id);
			DAOFactory.commitTransaction();
		} catch (DAOException e) {
			DAOFactory.rollbackTransaction();
		}
	}
}