package com.excilys.cdb.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

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
	private Logger log = LoggerFactory.getLogger(getClass());
	
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
	public boolean delete(Long id) {
		Connection conn = null;
		try {
			conn = DAOFactory.getConnection();
			conn.setAutoCommit(false);
			DAOFactory.getComputerDAO().deleteByCompanyId(id, conn);
			DAOFactory.getCompanyDAO().delete(id, conn);
			conn.commit();
			conn.close();
			return true;
		}
		catch (SQLException e) {
			try {
				if (conn != null) {
					conn.rollback();
					log.warn("Operation failed, rollback done");
				}
			}
			catch( SQLException e1) {
				log.error("Operation failed, rollback failed");
				log.error(e1.getMessage());
				return false;
			}
			log.error(e.getMessage());
			return false;
		}
	}
}