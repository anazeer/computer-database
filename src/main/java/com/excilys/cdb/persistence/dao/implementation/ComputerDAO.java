package com.excilys.cdb.persistence.dao.implementation;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;

import com.excilys.cdb.exception.DAOException;
import com.excilys.cdb.mapper.implementation.ComputerMapper;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.dao.AbstractDAO;
import com.excilys.cdb.service.util.Order;
import com.excilys.cdb.service.util.Query;

/**
 * DAO implementation for computers
 */
@Repository
public final class ComputerDAO extends AbstractDAO<Computer> {

	@Autowired
	private DataSource dataSource;
	@Autowired
	private ComputerMapper computerMapper;

	private Logger log = LoggerFactory.getLogger(getClass());

	private ComputerDAO() {
	}

	@Override
	public List<Computer> find(Query query) {
		List<Computer> listComputer = null;
		try (Connection conn = dataSource.getConnection();
				PreparedStatement stmt = createPreparedStatement(conn, query);
				ResultSet result = stmt.executeQuery()) {
			listComputer = new ArrayList<>();
			while (result.next()) {
				listComputer.add(computerMapper.getFromResultSet(result));
			}
			log.info("Computers retrieved ({}), filter = {}, order = {}",
					listComputer.size(), query != null ? query.getFilter() : "", query != null ? query.getOrder() : "");
		} catch (SQLException e) {
			log.error(e.getMessage());
		}
		return listComputer;
	}

	@Override
	public Computer findById(Long id) {
		Computer computer = null;
		String queryText = "SELECT * FROM computer WHERE id = " + id;
		try (Connection conn = dataSource.getConnection();
				PreparedStatement stmt = conn.prepareStatement(queryText);
				ResultSet result = stmt.executeQuery(queryText)) {
			if (result.next()) {
				computer = computerMapper.getFromResultSet(result);
			}
			log.info("Computer retrieved (id = {})", id);
		} catch (SQLException e) {
			log.error(e.getMessage());
		}
		return computer;
	}

	@Override
	public int count(Query query) {
		int count = 0;
		try (Connection conn = dataSource.getConnection();
				PreparedStatement stmt = createCountPreparedStatement(conn, query);
				ResultSet result = stmt.executeQuery()) {
			if (result.next()) {
				count = result.getInt("entries");
			}
			log.info("Computer counted ({}), filter = {}",
					count, query != null ? query.getFilter() : "", query != null ? query.getOrder() : "");
		} catch (SQLException e) {
			log.error(e.getMessage());
		}
		return count;
	}

	@Override
	public Computer create(Computer obj) throws DAOException {
		String queryText = "INSERT INTO computer (name, introduced, discontinued, company_id) VALUES (?, ?, ?, ?)";
		try (Connection conn = dataSource.getConnection();
				PreparedStatement stmt = createUpdatePreparedStatement(conn, obj, queryText)) {
			int result = stmt.executeUpdate();
			ResultSet resultSet = stmt.getGeneratedKeys();
			resultSet.next();
			obj.setId((long) resultSet.getInt(1));
			resultSet.close();
			log.info("Computer {} (id = {})", result > 0 ? "created" : "not created", obj.getId());
		}
		catch (SQLException e) {
			throw new DAOException(e);
		}
		return obj;
	}

	@Override
	public boolean update(Computer obj) throws DAOException {
		String queryText = "UPDATE computer SET "
				+ "name = ?, "
				+ "introduced = ?, "
				+ "discontinued = ?, "
				+ "company_id = ? "
				+ "WHERE id = " + obj.getId();
		try (Connection conn = dataSource.getConnection();
				PreparedStatement stmt = createUpdatePreparedStatement(conn, obj, queryText)) {
			int result = stmt.executeUpdate();
			boolean success = result > 0;
			log.info("Computer {} (id = {})", success ? "updated" : "not found", obj.getId());
			return success;
		} catch (SQLException e) {
			throw new DAOException(e);
		}
	}

	@Override
	public boolean delete(Long id) throws DAOException {
		String query = "DELETE FROM computer WHERE id = " + id;
		try(Statement stmt = dataSource.getConnection().createStatement()) {
			int result = stmt.executeUpdate(query);
			boolean success = result > 0;
			log.info("Computer {} (id = {})", success ? "deleted" : "not found", id);
			return success;
		} catch (SQLException e) {
			throw new DAOException(e);
		}
	}

	public boolean delete(Computer obj) throws DAOException {
		return delete(obj.getId());
	}

	public void deleteByCompanyId(Long id) {
		String query = "DELETE FROM computer WHERE company_id = " + id;
		Connection conn = DataSourceUtils.getConnection(dataSource);
		try (Statement stmt = conn.createStatement()) {
			int result = stmt.executeUpdate(query);
			log.info("{} computer{} deleted (company id = {})", result, result > 1 ? "s" : "", id);
		} catch (SQLException e) {
			log.error(e.getMessage());
			throw new RuntimeException(e);
		}
	}

	@Override
	protected PreparedStatement createPreparedStatement(Connection conn, Query query) throws SQLException {
		String limitText = getLimitText(query);
		String filterText = getFilterText(query);
		String orderText = getOrderText(query);
		String joinText = !filterText.isEmpty() || !orderText.isEmpty() ? 
				" LEFT JOIN company ON computer.company_id = company.id" : "";
		String queryText = "SELECT * FROM computer" + joinText + filterText + orderText + limitText;
		PreparedStatement stmt = conn.prepareStatement(queryText);
		if (log.isDebugEnabled()) {
			log.debug(queryText);
		}
		if (!filterText.isEmpty()) {
			stmt.setString(1, "%" + query.getFilter() + "%");
			stmt.setString(2, "%" + query.getFilter() + "%");
			stmt.setString(3, "%" + query.getFilter() + "%");
			stmt.setString(4, "%" + query.getFilter() + "%");
		}
		return stmt;
	}

	/**
	 * 
	 * @param conn the connection
	 * @param obj the computer object containing the update information
	 * @param queryText the update query text
	 * @return a prepared statement ready to update
	 * @throws SQLException
	 */
	private PreparedStatement createUpdatePreparedStatement(Connection conn, Computer obj, String queryText) throws SQLException {
		PreparedStatement stmt = conn.prepareStatement(queryText, Statement.RETURN_GENERATED_KEYS);
		Date introducedDate = obj.getIntroduced() == null ? null : Date.valueOf(obj.getIntroduced().atTime(0, 0, 0).toLocalDate());
		Date discontinuedDate = obj.getDiscontinued() == null ? null : Date.valueOf(obj.getDiscontinued().atTime(0, 0, 0).toLocalDate());
		stmt.setString(1, obj.getName());
		stmt.setDate(2, introducedDate);
		stmt.setDate(3, discontinuedDate);
		Long company_id = obj.getCompany() == null ? null : obj.getCompany().getId();
		if (company_id == null) {
			stmt.setNull(4, Types.NULL);
		} else {
			stmt.setLong(4, company_id);
		}
		return stmt;
	}

	/**
	 * 
	 * @param conn the connection
	 * @param query the object containing the query constraints 
	 * @return a prepared statement with the filter set
	 * @throws SQLException
	 */
	private PreparedStatement createCountPreparedStatement(Connection conn, Query query) throws SQLException {
		String filterText = getFilterText(query);
		String joinText = !filterText.isEmpty() ? 
				" LEFT JOIN company ON computer.company_id = company.id" : "";
		String queryText = "SELECT COUNT(*) as entries FROM computer" + joinText + filterText;
		PreparedStatement stmt = conn.prepareStatement(queryText);
		if (!filterText.isEmpty()) {
			stmt.setString(1, "%" + query.getFilter() + "%");
			stmt.setString(2, "%" + query.getFilter() + "%");
			stmt.setString(3, "%" + query.getFilter() + "%");
			stmt.setString(4, "%" + query.getFilter() + "%");
		}
		return stmt;
	}

	@Override
	protected String getOrderText(Query query) {
		String result = "";
		if (query == null) {
			return result;
		}
		Order order = query.getOrder();
		if (order == null) {
			return result;
		}
		switch(order) {
		case NAME_ASC : result = " ORDER BY computer.name ASC"; break;
		case NAME_DSC : result = " ORDER BY computer.name DESC"; break;
		case INTRODUCED_ASC : result = " ORDER BY computer.introduced ASC"; break;
		case INTRODUCED_DSC : result = " ORDER BY computer.introduced DESC"; break;
		case DISCONTINUED_ASC : result = " ORDER BY computer.discontinued ASC"; break;
		case DISCONTINUED_DSC : result = " ORDER BY computer.discontinued DESC"; break;
		case COMPANY_ASC : result = " ORDER BY company.name ASC"; break;
		case COMPANY_DSC : result = " ORDER BY company.name DESC"; break;
		default : result = "";
		}
		return result;
	}

	@Override
	protected String getFilterText(Query query) {
		String result = "";
		if (query == null) {
			return result;
		}
		String filter = query.getFilter();
		if (filter != null && !filter.trim().isEmpty()) {
			result =  " WHERE computer.name LIKE ?"
					+ " OR company.name LIKE ?"
					+ " OR computer.introduced LIKE ?"
					+ " OR computer.discontinued LIKE ?";
		}
		return result;
	}
}