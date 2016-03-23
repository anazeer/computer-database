package com.excilys.cdb.persistence.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.persistence.mapper.CompanyMapper;
import com.excilys.cdb.persistence.mapper.MapperFactory;

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
		log = LoggerFactory.getLogger(getClass());
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
		String query = "SELECT * FROM company";
		try(Connection conn = DAOFactory.getConnection();
			Statement stmt = conn.createStatement()) {
			ResultSet result = stmt.executeQuery(query);
			listCompany = new ArrayList<>();
			while(result.next())
				listCompany.add(companyMapper.getFromResultSet(result));
			result.close();
			stmt.close();
			conn.close();
			log.info("Companies retrieved (" + listCompany.size() + ")");
		}
		catch (SQLException e) {
			log.error(e.getMessage());
		}
		return listCompany;
	}
	
	@Override
	public List<Company> findAll(String filter) {
		List<Company> listCompany = null;
		String query = "SELECT * FROM company WHERE name LIKE ?";
		try(Connection conn = DAOFactory.getConnection();
			PreparedStatement stmt = conn.prepareStatement(query)) {
			stmt.setString(1, "%" + filter + "%");
			ResultSet result = stmt.executeQuery();
			listCompany = new ArrayList<>();
			while(result.next())
				listCompany.add(companyMapper.getFromResultSet(result));
			result.close();
			stmt.close();
			conn.close();
			log.info("Companies retrieved (" + listCompany.size() + ", filter = " + filter + ")");
		}
		catch (SQLException e) {
			log.error(e.getMessage());
		}
		return listCompany;
	}
	
	@Override
	public List<Company> findPage(int offset, int limit) {
		List<Company> listCompany = null;
		String query = "SELECT * FROM company LIMIT " + offset + ", " + limit;
		try(Connection conn = DAOFactory.getConnection();
			Statement stmt = conn.createStatement()) {
			ResultSet result = stmt.executeQuery(query);
			listCompany = new ArrayList<>();
			while(result.next())
				listCompany.add(companyMapper.getFromResultSet(result));
			result.close();
			stmt.close();
			conn.close();
			log.info("Computers page (" + listCompany.size() + ")");
		}
		catch (SQLException e) {
			log.error(e.getMessage());
		}
		return listCompany;
	}
	
	@Override
	public List<Company> findPage(int offset, int limit, String filter) {
		List<Company> listCompany = null;
		String query = "SELECT * FROM company WHERE name LIKE ? LIMIT " + offset + ", " + limit;
		try(Connection conn = DAOFactory.getConnection();
			PreparedStatement stmt = conn.prepareStatement(query)) {
			stmt.setString(1, "%" + filter + "%");
			ResultSet result = stmt.executeQuery();
			listCompany = new ArrayList<>();
			while(result.next())
				listCompany.add(companyMapper.getFromResultSet(result));
			result.close();
			stmt.close();
			conn.close();
			log.info("Companies retrieved (" + listCompany.size() + ", filter = " + filter + ")");
		}
		catch (SQLException e) {
			log.error(e.getMessage());
		}
		return listCompany;
	}
	
	@Override
	public int count() {
		int count = 0;
		String query = "SELECT COUNT(*) as entries FROM company";
		try(Connection conn = DAOFactory.getConnection();
			Statement stmt = conn.createStatement()) {
			ResultSet result = stmt.executeQuery(query);
			if(result.next()) {
				count = result.getInt("entries");
			}
			result.close();
			stmt.close();
			conn.close();
			log.info("Companies counted (" + count + ")");
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}
	
	@Override
	public int count(String filter) {
		int count = 0;
		String query = "SELECT COUNT(*) as entries FROM company WHERE name LIKE ?";
		try(Connection conn = DAOFactory.getConnection();
			PreparedStatement stmt = conn.prepareStatement(query)) {
			stmt.setString(1, "%" + filter + "%");
			ResultSet result = stmt.executeQuery();
			if(result.next()) {
				count = result.getInt("entries");
			}
			result.close();
			stmt.close();
			conn.close();
			log.info("Companies counted (" + count + ", filter = " + filter + ")");
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
		try(Connection conn = DAOFactory.getConnection();
			Statement stmt = conn.createStatement()) {
			ResultSet result = stmt.executeQuery(query);
			if(result.next()) {
				company = companyMapper.getFromResultSet(result);
			}
			result.close();
			stmt.close();
			conn.close();
		}
		catch (SQLException e) {
			log.error(e.getMessage());
		}
		if(company == null && id != 0) {
			log.warn("Company not retrieved (id = " + id + ")");
		}
		return company;
	}
	
	@Override
	public boolean delete(Long id) throws NoSuchMethodError {
		String query = "DELETE FROM company WHERE id = " + id;
		try(Connection conn = DAOFactory.getConnection();
			Statement stmt = conn.createStatement()) {
			stmt.executeUpdate(query);
			stmt.close();
			conn.close();
			log.info("Computer deleted (id = " + id + ")");
			return true;
		}
		catch (SQLException e) {
			log.error(e.getMessage());
			return false;
		}
	}
	
	public void delete(Long id, Connection conn) throws SQLException {
		String query = "DELETE FROM company WHERE id = " + id;
		try(Statement stmt = conn.createStatement()) {
			stmt.executeUpdate(query);
			stmt.close();
			log.info("Computer deleted (id = " + id + ")");
		}
		catch (SQLException e) {
			log.error(e.getMessage());
			throw new SQLException(e);
		}
	}
}
