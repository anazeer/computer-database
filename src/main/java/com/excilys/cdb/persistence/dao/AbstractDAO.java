package com.excilys.cdb.persistence.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.excilys.cdb.service.util.Constraint;

/**
 * Abstract implementation of DAO
 */
@Repository
public abstract class AbstractDAO<T> implements IDAO<T> {
	
	@Autowired
	protected SessionFactory sessionFactory;
	
	/**
	 * Get the name of the table which the DAO access to
	 * @return the table name
	 */
	protected abstract String getTableName();
	
	/**
	 * Construct the query text for ordering
	 * @param query the object containing the query constraints
	 * @return a query text with the order for the table columns, the empty string otherwise
	 */
	protected abstract String getOrderText(Constraint constraint);
	
	/**
	 * 
	 * @return the dao implementation class logger
	 */
	protected abstract Logger getLogger();
	

	/**
	 * Set the offset and the limit of the query (if apply)
	 * @param query the query that should be paginated
	 * @param constraint the object containing the offset and limit constraints
	 */
	protected void paginateQuery(Query query, Constraint constraint) {
		int first, last;
		if (constraint != null && (first = constraint.getOffset()) >= 0 && (last = constraint.getLimit()) > 0) {
			query.setFirstResult(first);
			query.setMaxResults(last);
		}
	}
	
	/**
	 * Activate the filter of the session (if apply)
	 * @param session the session that should activate the filter
	 * @param constraint the object containing the filter constraint
	 */
	protected void activateFilter(Session session, Constraint constraint) {
		if (constraint != null && constraint.getFilter() != null) {
			session.enableFilter("filter").setParameter("filter", '%' +  constraint.getFilter() + '%');
		}
	}
	
	/**
	 * Build the query object depending on the constraints
	 * @param constraint the object containing the query constraints
	 * @param baseQuery the shortest query that should be executed if there're no constraints
	 * @param isRow whether the query result is a row, or the result of an operation (as count)
	 * @return the query object ready to be executed
	 */
	protected Query getQuery(Constraint constraint, String baseQuery, boolean isRow) {
		// Get the hibernate session
		Session session = sessionFactory.getCurrentSession();
		// Enable the filter if the constraint exist
		activateFilter(session, constraint);
		// Append the order by constraint if it exists
		String queryText = baseQuery;
		if (isRow) {
			queryText = baseQuery + getOrderText(constraint);
		}
		// Build the query
		Query query = session.createQuery(queryText);
		// Enable the pagination if requested
		if (isRow) {
			paginateQuery(query, constraint);
		}
		return query;
	}
	
	@Override
	public T findById(Long id) {
		// Get the hibernate session
        Session session = sessionFactory.getCurrentSession();
        // Build the query
        String tableName = getTableName();
        String hql = "FROM " + tableName + " WHERE id = :id";
		Query query = session.createQuery(hql).setLong("id", id);
		// Execute the query
		@SuppressWarnings("unchecked")
		List<T> list = query.list();
		// Log the result
		Logger log = getLogger();
		if (list.isEmpty()) {
			if (id != null && id > 0) {
				log.warn("{} not retrieved (id = {})", tableName, id);
			}
			return null;
		}
		log.info("{} retrieved (id = {})", tableName, id);
		return list.get(0);
	}
}