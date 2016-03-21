package com.excilys.cdb.persistence.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.excilys.cdb.persistence.mapper.MapperFactory;
import org.apache.log4j.Logger;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.persistence.mapper.CompanyMapper;

/**
 * DAO implementation for companies
 * @author excilys
 *
 */
public final class CompanyDAO implements DAO<Company> {
	
	private Logger log;
	private static CompanyDAO instance;
    private CompanyMapper companyMapper;

	/**
	 * CompanyDAO new instance for Company type object persistence
	 */
	private CompanyDAO() {
		log = Logger.getLogger(getClass());
        companyMapper = MapperFactory.getCompanyMapper();
	}

    /**
     *
     * @return the company DAO implementation instance
     */
    public static CompanyDAO getInstance() {
        if(instance == null) {
            instance = new CompanyDAO();
        }
        return instance;
    }

	@Override
	public List<Company> findAll() {
		List<Company> listCompany = null;
		try(Statement stmt = conn.createStatement()) {
			String query = "SELECT * FROM company";
			ResultSet result = stmt.executeQuery(query);
			listCompany = new ArrayList<>();
			while(result.next())
				listCompany.add(companyMapper.getFromResultSet(result));
			result.close();
			stmt.close();
			log.info("Companies retrieved (" + listCompany.size() + ")");
		}
		catch (SQLException e) {
			log.error(e.getMessage());
		}
		return listCompany;
	}
	
	@Override
	public List<Company> findPage(int offset, int limit) {
		List<Company> listCompany = null;
		try(Statement stmt = conn.createStatement()) {
			String query = "SELECT * FROM company LIMIT " + offset + ", " + limit;
			ResultSet result = stmt.executeQuery(query);
			listCompany = new ArrayList<>();
			while(result.next())
				listCompany.add(companyMapper.getFromResultSet(result));
			result.close();
			stmt.close();
			log.info("Computers page (" + listCompany.size() + ")");
		}
		catch (SQLException e) {
			log.error(e.getMessage());
		}
		return listCompany;
	}
	
	@Override
	public int count() {
		int count = 0;
		try(Statement stmt = conn.createStatement()) {
			String query = "SELECT COUNT(*) as entries FROM company";
			ResultSet result = stmt.executeQuery(query);
			if(result.next()) {
				count = result.getInt("entries");
			}
			result.close();
			stmt.close();
			log.info("Companies counted (" + count + ")");
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	@Override
	public Company findById(Long id) {
		Company company = null;
		String query = "SELECT * FROM company WHERE company.id = " + id;
		try(Statement stmt = conn.createStatement()) {
			ResultSet result = stmt.executeQuery(query);
			if(result.next()) {
				company = companyMapper.getFromResultSet(result);
			}
			result.close();
			stmt.close();
		}
		catch (SQLException e) {
			log.error(e.getMessage());
		}
		if(company == null && id != 0) {
			log.warn("Company not retrieved (id = " + id + ")");
		}
		return company;
	}
}
