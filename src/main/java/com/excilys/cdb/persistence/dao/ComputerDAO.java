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

import com.excilys.cdb.exception.DAOException;
import org.slf4j.LoggerFactory;

import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.mapper.MapperFactory;
import com.excilys.cdb.service.Order;
import com.excilys.cdb.service.Query;

/**
 * DAO implementation for computers
 *
 * @author excilys
 */
public final class ComputerDAO extends AbstractDAO<Computer> {

    private static ComputerDAO instance;

	/**
	 * ComputerDAO new instance for Computer type object persistence
	 */
	private ComputerDAO() {
		log = LoggerFactory.getLogger(getClass());
		mapper = MapperFactory.getComputerMapper();
	}

	/**
	 * @return the computer DAO implementation instance
	 */
	public static ComputerDAO getInstance() {
		if (instance == null) {
			instance = new ComputerDAO();
		}
		return instance;
	}

	@Override
	public List<Computer> find(Query query) {
		List<Computer> listComputer = null;
		try (Connection conn = DAOFactory.getConnection();
				PreparedStatement stmt = createPreparedStatement(conn, query);
				ResultSet result = stmt.executeQuery()) {
			listComputer = new ArrayList<>();
			while (result.next()) {
				listComputer.add(mapper.getFromResultSet(result));
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
		try (Connection conn = DAOFactory.getConnection();
				PreparedStatement stmt = conn.prepareStatement(queryText);
				ResultSet result = stmt.executeQuery(queryText)) {
			if (result.next()) {
				computer = mapper.getFromResultSet(result);
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
		try (Connection conn = DAOFactory.getConnection();
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
		try (Connection conn = DAOFactory.getConnection();
				PreparedStatement stmt = createUpdatePreparedStatement(conn, obj, queryText)) {
			stmt.executeUpdate();
			ResultSet result = stmt.getGeneratedKeys();
			result.next();
			obj.setId((long) result.getInt(1));
			result.close();
			log.info("Computer created (id = {})", obj.getId());
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
		try (Connection conn = DAOFactory.getConnection();
				PreparedStatement stmt = createUpdatePreparedStatement(conn, obj, queryText)) {
			stmt.executeUpdate();
			log.info("Computer updated (id = {})", obj.getId());
			return true;
		} catch (SQLException e) {
            throw new DAOException(e);
        }
	}

	@Override
	public boolean delete(Long id) {
		String query = "DELETE FROM computer WHERE id = " + id;
		try (Connection conn = DAOFactory.getConnection();
				Statement stmt = conn.createStatement()) {
			stmt.executeUpdate(query);
			log.info("Computer deleted (id = {})", id);
			return true;
		} catch (SQLException e) {
			log.error(e.getMessage());
		}
		return false;
	}

	public boolean delete(Computer obj) {
		return delete(obj.getId());
	}

	public void deleteByCompanyId(Long id) throws DAOException {
		String query = "DELETE FROM computer WHERE company_id = " + id;
		try (Statement stmt = DAOFactory.getCurrentTransaction().createStatement()) {
			stmt.executeUpdate(query);
			log.info("Computer deleted (company id = {})", id);
		} catch (SQLException e) {
			throw new DAOException(e);
		}
	}

    @Override
	protected PreparedStatement createPreparedStatement(Connection conn, Query query) throws SQLException {
		String limitText = getLimitText(query);
		String filterText = getFilterText(query);
		String orderText = getOrderText(query);
		String joinText = !filterText.isEmpty() || !orderText.isEmpty() ? 
				" INNER JOIN company ON computer.company_id = company.id " : "";
		String queryText = "SELECT * FROM computer" + joinText + filterText + orderText + limitText;
		PreparedStatement stmt = conn.prepareStatement(queryText);
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
				" INNER JOIN company ON computer.company_id = company.id " : "";
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
			case NAME_ASC : result = " ORDER BY computer.name ASC "; break;
			case NAME_DSC : result = " ORDER BY computer.name DESC "; break;
			case INTRODUCED_ASC : result = " ORDER BY computer.introduced ASC "; break;
			case INTRODUCED_DSC : result = " ORDER BY computer.introduced DESC "; break;
			case DISCONTINUED_ASC : result = " ORDER BY computer.discontinued ASC "; break;
			case DISCONTINUED_DSC : result = " ORDER BY computer.discontinued DESC "; break;
			case COMPANY_ASC : result = " ORDER BY company.name ASC "; break;
			case COMPANY_DSC : result = " ORDER BY company.name DESC "; break;
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
			result =  " WHERE computer.name LIKE ? "
                    + "OR company.name LIKE ?"
                    + "OR computer.introduced LIKE ? "
                    + "OR computer.discontinued LIKE ? ";
		}
		return result;
	}
}