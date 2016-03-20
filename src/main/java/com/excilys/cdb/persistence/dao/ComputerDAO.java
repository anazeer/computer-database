package com.excilys.cdb.persistence.dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.excilys.cdb.persistence.mapper.MapperFactory;
import org.apache.log4j.Logger;

import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.mapper.ComputerMapper;

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
		log = Logger.getLogger(getClass());
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
		try(Statement stmt = conn.createStatement();
			ResultSet result = stmt.executeQuery(query)) {
			listComputer = new ArrayList<>();
			while(result.next())
				listComputer.add(computerMapper.getFromResultSet(result));
			result.close();
			stmt.close();
			log.info("Computers retrieved (" + listComputer.size() + ")");
		}
		catch (SQLException e) {
			log.error(e.getMessage());
		}
		return listComputer;
	}

	@Override
	public List<Computer> findPage(int offset, int limit) {
		List<Computer> listComputer = null;
		String query = "SELECT * FROM computer LIMIT " + offset + ", " + limit;
		try(Statement stmt = conn.createStatement();
			ResultSet result = stmt.executeQuery(query)) {
			listComputer = new ArrayList<>();
			while(result.next())
				listComputer.add(computerMapper.getFromResultSet(result));
			result.close();
			stmt.close();
			log.info("Computers page (" + listComputer.size() + ")");
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
		try(Statement stmt = conn.createStatement();
			ResultSet result = stmt.executeQuery(query)) {
			if(result.next()) {
				computer = computerMapper.getFromResultSet(result);
			}
			result.close();
			stmt.close();
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
		try(Statement stmt = conn.createStatement(); 
			ResultSet result = stmt.executeQuery(query)) {
			if(result.next()) {
				count = result.getInt("entries");
			}
			result.close();
			stmt.close();
			log.info("Computer counted (" + count + ")");
		}
		catch (SQLException e) {
			log.error(e.getMessage());
		}
		return count;
	}

	@Override
	public Computer create(Computer obj) throws SQLException {
		String query = "INSERT INTO computer "
				+ "(name, introduced, discontinued, company_id) VALUES (?, ?, ?, ?)";
		try(PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
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
			log.info("Computer created (id = " + obj.getId() + ")");
		}
		catch (SQLException e) {
			log.error(e.getMessage());
			throw new SQLException(e);
		}
		return obj;
	}
	
	@Override
	public boolean update(Computer obj) {
		String query = "UPDATE computer SET "
				+ "name = ?, "
				+ "introduced = " + obj.getIntroduced() + ", "
				+ "discontinued = " + obj.getDiscontinued() + ", "
				+ "company_id = ? "
				+ "WHERE id = " + obj.getId();
		try(PreparedStatement stmt = conn.prepareStatement(query)) {
			stmt.setString(1, obj.getName());
			Long company_id = obj.getCompany() == null ? null : obj.getCompany().getId();
			if(company_id == null) {
				stmt.setNull(2, Types.NULL);
			}
			else {
				stmt.setLong(2, company_id);
			}
			stmt.executeUpdate();
			stmt.close();
			log.info("Computer updated (id = " + obj.getId() + ")");
			return true;
		}
		catch (SQLException e) {
			log.error(e.getMessage());
		}
		return false;
	}

	@Override
	public boolean delete(Computer obj) {
		String query = "DELETE FROM computer WHERE id = " + obj.getId();
		try(Statement stmt = conn.createStatement()) {
			stmt.executeUpdate(query);
			stmt.close();
			log.info("Computer deleted (id = " + obj.getId() + ")");
			return true;
		}
		catch (SQLException e) {
			log.error(e.getMessage());
		}
		return false;
	}
	
	public boolean delete(Long id) {
		String query = "DELETE FROM computer WHERE id = " + id;
		try(Statement stmt = conn.createStatement()) {
			stmt.executeUpdate(query);
			stmt.close();
			log.info("Computer deleted (id = " + id + ")");
			return true;
		}
		catch (SQLException e) {
			log.error(e.getMessage());
		}
		return false;
	}
}
