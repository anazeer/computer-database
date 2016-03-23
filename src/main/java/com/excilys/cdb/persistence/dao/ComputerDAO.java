package com.excilys.cdb.persistence.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.mapper.ComputerMapper;
import com.excilys.cdb.persistence.mapper.MapperFactory;
import com.excilys.cdb.service.Order;

/**
 * DAO implementation for computers
 * @author excilys
 *
 */
public final class ComputerDAO implements DAO<Computer> {
	
	private Logger log;
    private static ComputerDAO instance;
    private ComputerMapper computerMapper;

	/**
	 * ComputerDAO new instance for Computer type object persistence
	 */
	private ComputerDAO() {
		log = LoggerFactory.getLogger(getClass());
        computerMapper = MapperFactory.getComputerMapper();
	}

    /**
     *
     * @return the computer DAO implementation instance
     */
	public static ComputerDAO getInstance() {
        if(instance == null) {
            instance = new ComputerDAO();
        }
        return instance;
    }

	@Override
	public List<Computer> findAll() {
		List<Computer> listComputer = null;
		String query = "SELECT * FROM computer";
		try(Connection conn = DAOFactory.getConnection();
			Statement stmt = conn.createStatement()) {
			ResultSet result = stmt.executeQuery(query);
			listComputer = new ArrayList<>();
			while(result.next())
				listComputer.add(computerMapper.getFromResultSet(result));
			result.close();
			stmt.close();
			conn.close();
			log.info("Computers retrieved (" + listComputer.size() + ")");
		}
		catch (SQLException e) {
			log.error(e.getMessage());
		}
		return listComputer;
	}
	
	@Override
	public List<Computer> findAll(String filter) {
		List<Computer> listComputer = null;
		String query = 
				"SELECT * FROM computer "
				+ "INNER JOIN company "
				+ "ON computer.company_id = company.id "
				+ "WHERE computer.name LIKE ? "
				+ "OR company.name LIKE ?"
				+ "OR computer.introduced LIKE ? "
				+ "OR computer.discontinued LIKE ? ";
		try(Connection conn = DAOFactory.getConnection();
			PreparedStatement stmt = conn.prepareStatement(query)) {
			stmt.setString(1, "%" + filter + "%");
			stmt.setString(2, "%" + filter + "%");
			stmt.setString(3, "%" + filter + "%");
			stmt.setString(4, "%" + filter + "%");
			ResultSet result = stmt.executeQuery();
			listComputer = new ArrayList<>();
			while(result.next())
				listComputer.add(computerMapper.getFromResultSet(result));
			result.close();
			stmt.close();
			conn.close();
			log.info("Computers retrieved (" + listComputer.size() + ", filter = " + filter + ")");
		}
		catch (SQLException e) {
			log.error(e.getMessage());
		}
		return listComputer;
	}
	
	@Override
	public List<Computer> findPage(int offset, int limit) {
		return findPage(offset, limit, Order.NOP);
	}
	
	public List<Computer> findPage(int offset, int limit, Order order) {
		List<Computer> listComputer = null;
		String orderQuery = null;
		switch(order) {
			case ASC : orderQuery = " ORDER BY computer.name ASC "; break;
			case DSC : orderQuery = " ORDER BY computer.name DESC "; break;
			case NOP : orderQuery = " "; break;
		}
		String query = "SELECT * FROM computer" + orderQuery + "LIMIT " + offset + ", " + limit;
		try(Connection conn = DAOFactory.getConnection();
			Statement stmt = conn.createStatement()) {
			ResultSet result = stmt.executeQuery(query);
			listComputer = new ArrayList<>();
			while(result.next())
				listComputer.add(computerMapper.getFromResultSet(result));
			result.close();
			stmt.close();
			conn.close();
			log.info("Computers page (" + listComputer.size() + ")");
		}
		catch (SQLException e) {
			log.error(e.getMessage());
		}
		return listComputer;
	}
	
	@Override
	public List<Computer> findPage(int offset, int limit, String filter) {
		return findPage(offset, limit, filter, Order.NOP);
	}
	
	public List<Computer> findPage(int offset, int limit, String filter, Order order) {
		List<Computer> listComputer = null;
		String orderQuery = new String();
		switch(order) {
			case ASC : orderQuery = " ORDER BY computer.name ASC "; break;
			case DSC : orderQuery = " ORDER BY computer.name DESC "; break;
			case NOP : orderQuery = " "; break;
		}
		String query = 
				"SELECT * FROM computer "
				+ "INNER JOIN company "
				+ "ON computer.company_id = company.id "
				+ "WHERE computer.name LIKE ? "
				+ "OR company.name LIKE ? "
				+ "OR computer.introduced LIKE ? "
				+ "OR computer.discontinued LIKE ?"
				+ orderQuery
				+ "LIMIT " + offset + ", " + limit;
		try(Connection conn = DAOFactory.getConnection();
			PreparedStatement stmt = conn.prepareStatement(query)) {
			stmt.setString(1, "%" + filter + "%");
			stmt.setString(2, "%" + filter + "%");
			stmt.setString(3, "%" + filter + "%");
			stmt.setString(4, "%" + filter + "%");
			ResultSet result = stmt.executeQuery();
			listComputer = new ArrayList<>();
			while(result.next())
				listComputer.add(computerMapper.getFromResultSet(result));
			result.close();
			stmt.close();
			conn.close();
			log.info("Computers page (" + listComputer.size() + ", filter = " + filter + ")");
		}
		catch (SQLException e) {
			log.error(e.getMessage());
		}
		return listComputer;
	}

	@Override
	public Computer findById(Long id) {
		Computer computer = null;
		String query = "SELECT * FROM computer WHERE computer.id = " + id;
		try(Connection conn = DAOFactory.getConnection();
			PreparedStatement stmt = conn.prepareStatement(query)) {
			ResultSet result = stmt.executeQuery(query);
			if(result.next()) {
				computer = computerMapper.getFromResultSet(result);
			}
			result.close();
			stmt.close();
			conn.close();
			log.info("Computer retrieved (id = " + id + ")");
		}
		catch (SQLException e) {
			log.error(e.getMessage());
		}
		return computer;
	}
	
	@Override
	public int count() {
		int count = 0;
		String query = "SELECT COUNT(*) as entries FROM computer";
		try(Connection conn = DAOFactory.getConnection();
			Statement stmt = conn.createStatement()) {
			ResultSet result = stmt.executeQuery(query);
			if(result.next()) {
				count = result.getInt("entries");
			}
			result.close();
			stmt.close();
			conn.close();
			log.info("Computer counted (" + count + ")");
		}
		catch (SQLException e) {
			log.error(e.getMessage());
		}
		return count;
	}
	
	@Override
	public int count(String filter) {
		int count = 0;
		String query = 
		"SELECT COUNT(*) as entries FROM computer  "
		+ "INNER JOIN company "
		+ "ON computer.company_id = company.id "
		+ "WHERE computer.name LIKE ? "
		+ "OR company.name LIKE ?"
		+ "OR computer.introduced LIKE ? "
		+ "OR computer.discontinued LIKE ? ";;
		try(Connection conn = DAOFactory.getConnection();
			PreparedStatement stmt = conn.prepareStatement(query)) {
			stmt.setString(1, "%" + filter + "%");
			stmt.setString(2, "%" + filter + "%");
			stmt.setString(3, "%" + filter + "%");
			stmt.setString(4, "%" + filter + "%");
			ResultSet result = stmt.executeQuery();
			if(result.next()) {
				count = result.getInt("entries");
			}
			result.close();
			stmt.close();
			conn.close();
			log.info("Computer counted (" + count + ", filter = " + filter + ")");
		}
		catch (SQLException e) {
			log.error(e.getMessage());
		}
		return count;
	}

	@Override
	public Computer create(Computer obj) throws SQLException {
		String query = "INSERT INTO computer (name, introduced, discontinued, company_id) VALUES (?, ?, ?, ?)";
		try(Connection conn = DAOFactory.getConnection();
			PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
			Date introducedDate = obj.getIntroduced() == null ? null : Date.valueOf(obj.getIntroduced().atTime(0, 0, 0).toLocalDate());
			Date discontinuedDate = obj.getDiscontinued() == null ? null : Date.valueOf(obj.getDiscontinued().atTime(0, 0, 0).toLocalDate());
			Long company_id = obj.getCompany() == null ? null : obj.getCompany().getId();
			stmt.setString(1, obj.getName());
			stmt.setDate(2, introducedDate);
			stmt.setDate(3, discontinuedDate);
			if(company_id == null) {
				stmt.setNull(4, Types.NULL);
			}
			else {
				stmt.setLong(4, company_id);
			}
			stmt.executeUpdate();
			ResultSet result = stmt.getGeneratedKeys();
			result.next();
			obj.setId((long) result.getInt(1));
			stmt.close();
			conn.close();
			log.info("Computer created (id = " + obj.getId() + ")");
		}
		catch (SQLException e) {
			log.error(e.getMessage());
			throw new SQLException(e);
		}
		return obj;
	}
	
	@Override
	public boolean update(Computer obj) throws SQLException {
		String query = "UPDATE computer SET "
				+ "name = ?, "
				+ "introduced = ?, "
				+ "discontinued = ?, "
				+ "company_id = ? "
				+ "WHERE id = " + obj.getId();
		try(Connection conn = DAOFactory.getConnection();
			PreparedStatement stmt = conn.prepareStatement(query)) {
			Date introducedDate = obj.getIntroduced() == null ? null : Date.valueOf(obj.getIntroduced().atTime(0, 0, 0).toLocalDate());
			Date discontinuedDate = obj.getDiscontinued() == null ? null : Date.valueOf(obj.getDiscontinued().atTime(0, 0, 0).toLocalDate());
			stmt.setString(1, obj.getName());
			stmt.setDate(2, introducedDate);
			stmt.setDate(3, discontinuedDate);
			Long company_id = obj.getCompany() == null ? null : obj.getCompany().getId();
			if(company_id == null) {
				stmt.setNull(4, Types.NULL);
			}
			else {
				stmt.setLong(4, company_id);
			}
			stmt.executeUpdate();
			stmt.close();
			conn.close();
			log.info("Computer updated (id = " + obj.getId() + ")");
			return true;
		}
		catch (SQLException e) {
			log.error(e.getMessage());
			System.out.println(e.getMessage());
			throw new SQLException(e);
		}
	}
	
	@Override
	public boolean delete(Long id) {
		String query = "DELETE FROM computer WHERE id = " + id;
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
		}
		return false;
	}

	public boolean delete(Computer obj) {
		String query = "DELETE FROM computer WHERE id = " + obj.getId();
		try(Connection conn = DAOFactory.getConnection();
			Statement stmt = conn.createStatement()) {
			stmt.executeUpdate(query);
			stmt.close();
			conn.close();
			log.info("Computer deleted (id = " + obj.getId() + ")");
			return true;
		}
		catch (SQLException e) {
			log.error(e.getMessage());
		}
		return false;
	}

	public void deleteByCompanyId(Long id, Connection conn) throws SQLException {
		String query = "DELETE FROM computer WHERE company_id = " + id;
		try(Statement stmt = conn.createStatement()) {
			stmt.executeUpdate(query);
			stmt.close();
			log.info("Computer deleted (company id = " + id + ")");
		}
		catch (SQLException e) {
			throw new SQLException(e);
		}
	}
}
