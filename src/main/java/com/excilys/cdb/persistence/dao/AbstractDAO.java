package com.excilys.cdb.persistence.dao;

import com.excilys.cdb.service.util.Order;
import com.excilys.cdb.service.util.Query;

/**
 * Abstract implementation of DAO
 */
public abstract class AbstractDAO<T> implements IDAO<T> {
	
	/**
	 * Get the name of the table which the DAO access to
	 * @return the table name
	 */
	protected abstract String getTableName();
	
	/**
	 * Construct the query text for filter
	 * @param query the object containing the query constraints
	 * @return a query text with the filter if not empty, the empty string otherwise
	 */
	protected abstract String getFilterText(Query query);

	/**
	 * Construct the query text for count select
	 * @param query the object containing the query constraints
	 * @return a count select query text completed with the filter constraint
	 */
	protected String getCountQueryText(Query query) {
		String filterText = getFilterText(query);
		String queryText = "SELECT COUNT(*) FROM " + getTableName() + filterText;
		return queryText;
	}

	/**
	 * Construct the query text for limit and offset
	 * @param query the object containing the query constraints
	 * @return a query text with the limit and offset if positives, the empty string otherwise
	 */
	protected String getLimitText(Query query) {
		String result = "";
		if (query == null) {
			return result;
		}
		int offset = query.getOffset();
		int limit = query.getLimit();
		if (offset >= 0 && limit > 0) {
			result = " LIMIT " + offset + ", " + limit;
		}
		return result;
	}
	
	/**
	 * Construct the query text for ordering
	 * @param query the object containing the query constraints
	 * @return a query text with the order for the table columns, the empty string otherwise
	 */
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
	
	/**
	 * Construct the full select query text
	 * @param query the object containing the query constraints
	 * @return a select query text completed by the query constraints
	 */
	protected String getQueryText(Query query) {
		String limitText = getLimitText(query);
		String filterText = getFilterText(query);
		String orderText = getOrderText(query);
		String queryText = "SELECT * FROM " + getTableName() + filterText + orderText + limitText;
		return queryText;
	}
}