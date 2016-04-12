package com.excilys.cdb.persistence.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.excilys.cdb.exception.DAOException;
import org.slf4j.LoggerFactory;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.persistence.mapper.MapperFactory;
import com.excilys.cdb.service.Order;
import com.excilys.cdb.service.Query;

/**
 * DAO implementation for companies
 * @author excilys
 *
 */
public final class CompanyDAO extends AbstractDAO<Company> {

    private static CompanyDAO instance;

	/**
	 * CompanyDAO new instance for Company type object persistence
	 */
	private CompanyDAO() {
		log = LoggerFactory.getLogger(getClass());
		mapper = MapperFactory.getCompanyMapper();
	}

	/**
	 *
	 * @return the company DAO implementation instance
	 */
	public static CompanyDAO getInstance() {
		if (instance == null) {
			instance = new CompanyDAO();
		}
		return instance;
	}

	@Override
	public List<Company> find(Query query) {
		List<Company> listCompany = null;
		try (Connection conn = DAOFactory.getConnection();
				PreparedStatement stmt = createPreparedStatement(conn, query);
				ResultSet result = stmt.executeQuery()) {
			listCompany = new ArrayList<>();
			while (result.next()) {
				listCompany.add(mapper.getFromResultSet(result));
			}
			log.info("Companies retrieved ({}), filter = {}, orderBy = ", 
					listCompany.size(), query != null ? query.getFilter() : "", query != null ? query.getOrder() : "");
		}
		catch (SQLException e) {
			log.error(e.getMessage());
		}
		return listCompany;
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
			log.info("Companies counted {}, filter = {}", count, query != null ? query.getFilter() : "");
		}
		catch (SQLException e) {
			log.error(e.getMessage());
		}
		return count;
	}

	@Override
	public Company findById(Long id) {
		Company company = null;
		String queryText = "SELECT * FROM company WHERE id = " + id;
		try (Connection conn = DAOFactory.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet result = stmt.executeQuery(queryText)) {
			if (result.next()) {
				company = mapper.getFromResultSet(result);
			}
		}
		catch (SQLException e) {
			log.error(e.getMessage());
		}
		if (company == null && id != 0) {
			log.warn("Company not retrieved (id = " + id + ")");
		}
		return company;
	}

    @Override
	public boolean delete(Long id) throws DAOException {
		String query = "DELETE FROM company WHERE id = " + id;
		try (Statement stmt = DAOFactory.getCurrentTransaction().createStatement()) {
			stmt.executeUpdate(query);
			log.info("Company deleted (id = " + id + ")");
            return true;
		} catch (SQLException e) {
            throw new DAOException(e);
        }
	}

	@Override
	protected PreparedStatement createPreparedStatement(Connection conn, Query query) throws SQLException {
		String limitText = getLimitText(query);
		String filterText = getFilterText(query);
		String orderText = getOrderText(query);
		String queryText = "SELECT * FROM company" + filterText + orderText + limitText;
		log.error(queryText);
		PreparedStatement stmt = conn.prepareStatement(queryText);
		if (!filterText.isEmpty()) {
			stmt.setString(1, "%" + query.getFilter() + "%");
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
		String queryText = "SELECT COUNT(*) as entries FROM company" + filterText;
		PreparedStatement stmt = conn.prepareStatement(queryText);
		if (!filterText.isEmpty()) {
			stmt.setString(1, "%" + query.getFilter() + "%");
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
		switch (order) {
		case NAME_ASC : result = " ORDER BY company.name ASC "; break;
		case NAME_DSC : result = " ORDER BY company.name DESC "; break;
		default: break;
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
			result = " WHERE name LIKE ? ";
		}
		return result;
	}
}
