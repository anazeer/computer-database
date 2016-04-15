package com.excilys.cdb.persistence.dao.implementation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.persistence.dao.AbstractDAO;
import com.excilys.cdb.service.util.Query;

/**
 * DAO implementation for companies
 */
@Repository
public final class CompanyDAO extends AbstractDAO<Company> {
	
	// Logger
	private Logger log = LoggerFactory.getLogger(getClass());

	// JDBC template for named parameters
	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	/**
	 * DAO dependency is only injected by spring
	 */
	private CompanyDAO() {
	}

	@Override
	public List<Company> find(Query query) {
	    // Execute the query
		List<Company> listCompany = jdbcTemplate.query(getQueryText(query), new CompanyMapper());
		// Log the result
		log.info("Companies retrieved ({}), filter = {}, orderBy = {}", 
				listCompany.size(), query != null ? query.getFilter() : "", query != null ? query.getOrder() : "");
		return listCompany;
	}
	
	@Override
	public Company findById(Long id) {
		// Build the query text
		String queryText = "SELECT * FROM company WHERE id = :id";
		// Set the filter in the parameters map
		Map<String, Long> namedParameters = Collections.singletonMap("id", id);
		// Execute the query
		List<Company> company = jdbcTemplate.query(queryText, namedParameters, new CompanyMapper());
		// Log the result
		if (company.isEmpty()) {
			if (id > 0) {
				log.warn("Company not retrieved (id = {})", id);
			}
			return null;
		}
		return company.get(0);
	}

	@Override
	public int count(Query query) {
		// Get the filter
		String filter = query != null ? query.getFilter() : "";
		// Set the filter in the parameters map
		Map<String, String> namedParameters = Collections.singletonMap("filter", filter);
		// Execute the query
		int count = jdbcTemplate.queryForObject(getCountQueryText(query), namedParameters, Integer.class);
		// Log the result
		log.info("Companies counted {}, filter = {}", count, filter);
		return count;
	}

	@Override
	public boolean delete(Long id) {
		// Build the query text
		String queryText = "DELETE FROM company WHERE id = :id";
		// Set the filter in the parameters map
		Map<String, Long> namedParameters = Collections.singletonMap("id", id);
		// Execute the query
		int result = jdbcTemplate.update(queryText, namedParameters);
		// Log the result
		log.info("{} compan{} deleted (id = {})", result, result > 1 ? "ies" : "y", id);
		return result > 0;
	}

	@Override
	protected String getFilterText(Query query) {
		String result = "";
		if (query == null) {
			return result;
		}
		String filter = query.getFilter();
		if (filter != null && !filter.trim().isEmpty()) {
			result = " WHERE company.name LIKE :filter";
		}
		return result;
	}
	
	@Override
	protected String getTableName() {
		return "company";
	}

	/**
	 * Map a result set into a company object
	 */
	private static final class CompanyMapper implements RowMapper<Company> {
		public Company mapRow(ResultSet result, int rowNum) throws SQLException {
			Company company = new Company();
			company.setId(result.getLong("id"));
			company.setName(result.getString("name"));
			return company;
		}
	}
}
