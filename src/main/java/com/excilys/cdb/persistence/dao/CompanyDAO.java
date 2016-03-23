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
import com.excilys.cdb.service.Order;

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
		String query = "SELECT * FROM company";
        return executeQuery(query);
	}
	
	@Override
	public List<Company> findAll(String filter) {
        if(filter.isEmpty())
            return findAll();
		String query = "SELECT * FROM company WHERE name LIKE ?";
        return executeQuery(query, filter);
	}
	
	@Override
	public List<Company> findPage(int offset, int limit) {
		return findPage(offset, limit, Order.NOP);
	}
	
	public List<Company> findPage(int offset, int limit, Order order) {
		String orderQuery = getOrderQuery(order);
		String query = "SELECT * FROM company" + orderQuery + "LIMIT " + offset + ", " + limit;
        return executeQuery(query);
	}
	
	@Override
	public List<Company> findPage(int offset, int limit, String filter) {
        if(filter.isEmpty())
            return findPage(offset, limit);
		return findPage(offset, limit, filter, Order.NOP);
	}
	
	public List<Company> findPage(int offset, int limit, String filter, Order order) {
        if(filter.isEmpty())
            return findPage(offset, limit, order);
		String orderQuery = getOrderQuery(order);
		String query = "SELECT * FROM company WHERE name LIKE ?" + orderQuery + "LIMIT " + offset + ", " + limit;
        return executeQuery(query, filter);
	}
	
	@Override
	public int count() {
		String query = "SELECT COUNT(*) as entries FROM company";
		return executeCountQuery(query);
	}
	
	@Override
	public int count(String filter) {
        if(filter.isEmpty())
            return count();
		String query = "SELECT COUNT(*) as entries FROM company WHERE name LIKE ?";
        return executeCountQuery(query, filter);
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

    private List<Company> executeQuery(String query) {
        return executeQuery(query, "");
    }

    private List<Company> executeQuery(String query, String filter) {
        List<Company> listCompany = null;
        try(Connection conn = DAOFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {
            if(!filter.isEmpty()) {
                stmt.setString(1, "%" + filter + "%");
            }
            ResultSet result = stmt.executeQuery();
            listCompany = new ArrayList<>();
            while(result.next())
                listCompany.add(companyMapper.getFromResultSet(result));
            result.close();
            stmt.close();
            conn.close();
            log.info("Companies retrieved ({}), filter = {}, orderBy = ", listCompany.size(), filter);
        }
        catch (SQLException e) {
            log.error(e.getMessage());
        }
        return listCompany;
    }

    private int executeCountQuery(String query) {
        return executeCountQuery(query, "");
    }

    private int executeCountQuery(String query, String filter) {
        int count = 0;
        try(Connection conn = DAOFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {
            if(!filter.isEmpty()) {
                stmt.setString(1, "%" + filter + "%");
            }
            ResultSet result = stmt.executeQuery();
            if(result.next()) {
                count = result.getInt("entries");
            }
            result.close();
            stmt.close();
            conn.close();
            log.info("Companies counted (), filter = {}", count, filter);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

	private String getOrderQuery(Order order) {
        switch(order) {
            case ASC : return " ORDER BY company.name ASC ";
            case DSC : return " ORDER BY company.name DESC ";
            default : return " ";
        }
	}
}
