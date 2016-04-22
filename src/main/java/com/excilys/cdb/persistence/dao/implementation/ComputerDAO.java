package com.excilys.cdb.persistence.dao.implementation;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.excilys.cdb.exception.DAOException;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.dao.AbstractDAO;
import com.excilys.cdb.service.util.Query;

/**
 * DAO implementation for computers
 */
@Repository
public final class ComputerDAO extends AbstractDAO<Computer> {

	// Logger
	private Logger log = LoggerFactory.getLogger(getClass());

	// JDBC template for named parameters
	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	/**
	 * DAO dependency is only injected by spring
	 */
	private ComputerDAO() {
	}

	@Override
	public List<Computer> find(Query query) {
		// Get the query text
		String queryText = getQueryText(query);
		// Get the filter
		String filter = query != null ? query.getFilter() : "";
		// Join company for filtering and ordering
		queryText = queryText.replaceFirst("computer", "computer LEFT JOIN company ON computer.company_id = company.id");
		// Set the filter in the parameters map
		Map<String, String> namedParameters = Collections.singletonMap("filter", '%' + filter + '%');
		// Execute the query
		List<Computer> listComputer = jdbcTemplate.query(queryText, namedParameters, new ComputerMapper());
		// Log the result
		log.info("Computers retrieved ({}), filter = {}, order = {}",
				listComputer.size(), query != null ? query.getFilter() : "", query != null ? query.getOrder() : "");
		return listComputer;
	}

	@Override
	public Computer findById(Long id) {
		// Build the query text
		String queryText = "SELECT * FROM computer WHERE id = :id";
		// Set the filter in the parameters map
		Map<String, Long> namedParameters = Collections.singletonMap("id", id);
		// Execute the query
		List<Computer> computer = jdbcTemplate.query(queryText, namedParameters, new ComputerMapper());
		// Log the result
		if (computer.isEmpty()) {
			if (id != null && id > 0) {
				log.warn("Computer not retrieved (id = {})", id);
			}
			return null;
		}
		log.info("Computer retrieved (id = {})", id);
		return computer.get(0);
	}

	@Override
	public int count(Query query) {
		// Get the query text
		String queryText = getCountQueryText(query);
		// Get the filter
		String filter = query != null ? query.getFilter() : "";
		// Join company for filtering
		if (filter != null && !filter.isEmpty()) {
			queryText = queryText.replaceFirst("computer", "computer LEFT JOIN company ON computer.company_id = company.id");
		}
		// Set the filter in the parameters map
		Map<String, String> namedParameters = Collections.singletonMap("filter", '%' + filter + '%');
		// Execute the query
		int count = jdbcTemplate.queryForObject(queryText, namedParameters, Integer.class);

		// Log the result
		log.info("Computers counted {}, filter = {}", count, filter);
		return count;
	}

	@Override
	public Computer create(Computer obj) throws DAOException {
		// Get the company ID
		String id = obj.getCompany() != null ? obj.getCompany().getId() + "" : "NULL";
		// Build the query text
		String queryText = "INSERT INTO computer (name, introduced, discontinued, company_id)"
				+ "VALUES (:name, :introduced, :discontinued, " + id + ")";
		// Map the computer attributes based on the class name attributes and set it to the parameters map
		SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(obj);
		// Use to retrieve the auto-generated key returned by JDBC
		KeyHolder holder = new GeneratedKeyHolder();
		// Execute the query
		int result = jdbcTemplate.update(queryText, namedParameters, holder);
		// Get the auto-generated key and set it to the object
		obj.setId(holder.getKey().longValue());
		// Log the result
		log.info("Computer {} (id = {})", result > 0 ? "created" : "not created", obj.getId());
		return obj;
	}

	@Override
	public boolean update(Computer obj) throws DAOException {
		// Get the company ID
		String id = obj.getCompany() != null ? obj.getCompany().getId() + "" : "NULL";
		// Build the query text
		String queryText = "UPDATE computer SET "
				+ "name = :name, "
				+ "introduced = :introduced, "
				+ "discontinued = :discontinued, "
				+ "company_id = " + id + " "
				+ "WHERE id = " + obj.getId();
		// Map the computer attributes based on the class name attributes and set it to the parameters map
		SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(obj);
		// Execute the query
		int result = jdbcTemplate.update(queryText, namedParameters);
		boolean success = result > 0;
		// Log the result
		log.info("Computer {} (id = {})", success ? "updated" : "not found", obj.getId());
		return success;
	}

	@Override
	public boolean delete(Long id) throws DAOException {
		// Build the query text
		String queryText = "DELETE FROM computer WHERE id = :id";
		// Set the filter in the parameters map
		Map<String, Long> namedParameters = Collections.singletonMap("id", id);
		// Execute the query
		int result = jdbcTemplate.update(queryText, namedParameters);
		boolean success = result > 0;
		// Log the result
		log.info("Computer {} (id = {})", success ? "deleted" : "not found", id);
		return success;
	}

	public void deleteByCompanyId(Long id) {
		// Build the query text
		String queryText = "DELETE FROM computer WHERE company_id = :id";
		// Set the filter in the parameters map
		Map<String, Long> namedParameters = Collections.singletonMap("id", id);
		// Execute the query
		int result = jdbcTemplate.update(queryText, namedParameters);
		log.info("{} computer{} deleted (company id = {})", result, result > 1 ? "s" : "", id);
	}

	@Override
	protected String getFilterText(Query query) {
		String result = "";
		if (query == null) {
			return result;
		}
		String filter = query.getFilter();
		if (filter != null && !filter.trim().isEmpty()) {
			result =  " WHERE computer.name LIKE :filter"
					+ " OR company.name LIKE :filter"
					+ " OR computer.introduced LIKE :filter"
					+ " OR computer.discontinued LIKE :filter";
		}
		return result;
	}

	@Override
	protected String getTableName() {
		return "computer";
	}

	/**
	 * Map a result set into a computer object
	 */
	private static final class ComputerMapper implements RowMapper<Computer> {
		public Computer mapRow(ResultSet result, int rowNum) throws SQLException {
			String name = result.getString("name");
			Long id = result.getLong("id");
			Date introducedDate = result.getDate("introduced");
			LocalDate introduced = introducedDate == null ? null : introducedDate.toLocalDate();
			Date discontinuedDate = result.getDate("discontinued");
			LocalDate discontinued = discontinuedDate == null ? null : discontinuedDate.toLocalDate();
			Company company = new Company();
			company.setId(result.getLong("company_id"));
			return new Computer.Builder(name)
					.id(id)
					.introduced(introduced)
					.discontinued(discontinued)
					.company(company)
					.build();
		}
	}
}