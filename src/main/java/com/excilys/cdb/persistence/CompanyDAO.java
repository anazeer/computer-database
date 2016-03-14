package com.excilys.cdb.persistence;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.persistence.mapper.CompanyMapper;
import com.excilys.cdb.persistence.mapper.DAOMapper;

/**
 * Company DAO implementation
 * @author excilys
 *
 */
public class CompanyDAO implements DAO<Company> {
	
	private Connection conn;
	private Logger log;

	/**
	 * ComputerDAO new instance for Computer type object persistence
	 */
	public CompanyDAO() {
		conn = ConnectionSingleton.getInstance();
		log = Logger.getLogger(getClass());
	}

	@Override
	public List<Company> findAll() {
		List<Company> listCompany = null;
		try(Statement stmt = conn.createStatement();) {
			String query = "SELECT * FROM company";
			ResultSet result = stmt.executeQuery(query);
			DAOMapper<Company> mapper = new CompanyMapper();
			listCompany = new ArrayList<Company>();
			while(result.next())
				listCompany.add(mapper.find(result));
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
	public List<Company> findFromOffset(int from, int offset) {
		List<Company> listCompany = null;
		try(Statement stmt = conn.createStatement();) {
			String query = "SELECT * FROM company LIMIT " + from + ", " + offset;
			ResultSet result = stmt.executeQuery(query);
			DAOMapper<Company> mapper = new CompanyMapper();
			listCompany = new ArrayList<Company>();
			while(result.next())
				listCompany.add(mapper.find(result));
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
	public int countEntries() {
		int count = 0;
		try(Statement stmt = conn.createStatement();) {
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
		try(Statement stmt = conn.createStatement();) {
			ResultSet result = stmt.executeQuery(query);
			DAOMapper<Company> mapper = new CompanyMapper();
			if(result.next()) {
				company = mapper.find(result);
			}
			result.close();
			stmt.close();
		}
		catch (SQLException e) {
			log.error(e.getMessage());
		}
		if(company == null) {
			log.warn("Company not retrieved (id = " + id + ")");
		}
		return company;
	}

}
