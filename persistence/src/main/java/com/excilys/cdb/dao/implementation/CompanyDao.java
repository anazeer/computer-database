package com.excilys.cdb.dao.implementation;

import java.util.List;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.excilys.cdb.dao.AbstractDao;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.util.Constraint;
import com.excilys.cdb.util.Order;

/**
 * DAO implementation for companies
 */
@Repository
public final class CompanyDao extends AbstractDao<Company> {
	
	// Logger
	private Logger log = LoggerFactory.getLogger(getClass());

	/**
	 * DAO dependency is only injected by spring
	 */
	private CompanyDao() {
	}
	
	@Override
	public List<Company> find(Constraint constraint) {
		// Build the query
		String hql = "FROM Company c";
		Query query = getQuery(constraint, hql, true);
		// Execute the query
		@SuppressWarnings("unchecked")
		List<Company> listCompany = query.list();
		// Log the result
		log.info("Companies retrieved ({}), filter = {}, orderBy = {}", 
				listCompany.size(), constraint != null ? constraint.getFilter() : null, constraint != null ? constraint.getOrder() : null);
		return listCompany;
	}

	@Override
	public int count(Constraint constraint) {
		// Build the query
		String hql = "SELECT count(*) FROM Company c";
		Query query = getQuery(constraint, hql, false);
		// Execute the query
		int result = ((Long) (query.iterate().next())).intValue();
		// Log the result
		log.info("Companies counted {}, filter = {}", result, constraint != null ? constraint.getFilter() : "");
		return result;
	}
	
	@Override
	protected String getTableName() {
		return "Company";
	}
	
	@Override
	protected Logger getLogger() {
		return log;
	}
	
	/**
	 * Construct the query text for ordering
	 * @param query the object containing the query constraints
	 * @return a query text with the order for the table columns, the empty string otherwise
	 */
	@Override
	protected String getOrderText(Constraint constraint) {
		String result = "";
		if (constraint == null) {
			return result;
		}
		Order order = constraint.getOrder();
		if (order == null) {
			return result;
		}
		switch(order) {
			case COMPANY_ASC : result = " ORDER BY company.name ASC"; break;
			case COMPANY_DSC : result = " ORDER BY company.name DESC"; break;
		default : result = "";
		}
		return result;
	}
	
}
