package com.excilys.cdb.dao.implementation;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.excilys.cdb.dao.AbstractDao;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.util.Constraint;
import com.excilys.cdb.util.Order;

/**
 * DAO implementation for computers
 */
@Repository
public final class ComputerDao extends AbstractDao<Computer> {

	// Logger
	private Logger log = LoggerFactory.getLogger(getClass());
	
	/**
	 * DAO dependency is only injected by spring
	 */
	private ComputerDao() {
	}

	@Override
	public List<Computer> find(Constraint constraint) {
		// Build the query
		String hql = "FROM Computer as computer"; ;
		if (constraint != null && constraint.getFilter() != null) {
			hql += " LEFT JOIN fetch computer.company as company";
		}
		Query query = getQuery(constraint, hql, true);
		// Execute the query
		@SuppressWarnings("unchecked")
		List<Computer> listComputer = query.list();
		// Log the result
		log.info("Computers retrieved ({}), filter = {}, order = {}",
				listComputer.size(), constraint != null ? constraint.getFilter() : "", constraint != null ? constraint.getOrder() : "");
		return listComputer;
	}

	@Override
	public int count(Constraint constraint) {
		// Build the query
		String hql = "SELECT count(*) FROM Computer as computer";
		Query query = getQuery(constraint, hql, false);
		// Execute the query
		int result = ((Long) (query.iterate().next())).intValue();
		// Log the result
		log.info("Computers counted {}, filter = {}", result, constraint != null ? constraint.getFilter() : "");
		return result;
	}

	@Override
	public Computer create(Computer obj) {
		// Get the hibernate session
		Session session = sessionFactory.getCurrentSession();
		// Persist the object
		session.persist(obj);
		// Log the result
		log.info("Computer {} (id = {})", obj.getId() > 0 ? "created" : "not created", obj.getId());
		return obj;
	}

	@Override
	public boolean update(Computer obj) {
		// Get the hibernate session
		Session session = sessionFactory.getCurrentSession();
        // Build the query
		String hql = "UPDATE Computer SET "
				+ "name = :name, "
				+ "introduced = :introduced, "
				+ "discontinued = :discontinued, "
				+ "company_id = :company_id "
				+ "WHERE id = :id";
		Object company = obj.getCompany() != null ? obj.getCompany().getId() : null;
		Query query = session
				.createQuery(hql)
				.setString("name", obj.getName())
				.setParameter("introduced", obj.getIntroduced())
				.setParameter("discontinued", obj.getDiscontinued())
				.setParameter("company_id", company)
				.setLong("id", obj.getId());
		// Execute the query
		int result = query.executeUpdate();
		boolean success = result > 0;
		// Log the result
		log.info("Computer {} (id = {})", success ? "updated" : "not found", obj.getId());
		return success;
	}

	public void deleteByCompanyId(Long id) {
		// Get the hibernate session
		Session session = sessionFactory.getCurrentSession();
		// Build the query text
		String hql = "DELETE FROM Computer WHERE company_id = :id";
		Query query = session.createQuery(hql).setParameter("id", id);
		// Execute the query
		int result = query.executeUpdate();
		// Log the result
		log.info("{} computer{} deleted (company id = {})", result, result > 1 ? "s" : "", id);
	}

	@Override
	protected String getTableName() {
		return "Computer";
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
}