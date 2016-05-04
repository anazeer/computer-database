package com.excilys.cdb.dao.implementation;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.Filters;
import org.hibernate.annotations.ParamDef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.excilys.cdb.dao.AbstractDAO;
import com.excilys.cdb.model.User;
import com.excilys.cdb.util.Constraint;

/**
 * DAO implementation for users
 */
@Repository
@FilterDef(name="filter", parameters={
		@ParamDef(name="filter", type="string")
})
@Filters( {
	@Filter(name="filter", condition="name like :filter")
} )
public class UserDAO extends AbstractDAO<User> {

	private Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public int count(Constraint constraint) {
		// Build the query
		String hql = "SELECT count(*) FROM User as user";
		Query query = getQuery(constraint, hql, false);
		// Execute the query
		int result = ((Long) (query.iterate().next())).intValue();
		// Log the result
		log.info("Users counted {}, filter = {}", result, constraint != null ? constraint.getFilter() : "");
		return result;
	}

	@Override
	public List<User> find(Constraint constraint) {
		// Build the query
		String hql = "FROM User as user LEFT JOIN fetch user.userRole as role";
		Query query = getQuery(constraint, hql, true);
		// Execute the query
		@SuppressWarnings("unchecked")
		List<User> listUser = query.list();
		// Log the result
		log.info("Users retrieved ({}), filter = {}, order = {}",
				listUser.size(), constraint != null ? constraint.getFilter() : "", constraint != null ? constraint.getOrder() : "");
		return listUser;
	}
	
	/**
	 * Retrieve an user from the database with his username
	 * @param username the user username
	 * @return the user object if retrieved, null otherwise
	 */
	public User findByUsername(String username) {
		// Get the hibernate session
		Session session = sessionFactory.getCurrentSession();
		// Build the query
		String hql = "FROM User as user LEFT JOIN fetch user.userRole as role where username = :username";
		Query query = session.createQuery(hql).setParameter("username", username);
		// Execute the query
		@SuppressWarnings("unchecked")
		List<User> listUser = query.list();
		// Log the result
		User user = listUser.isEmpty() ? null : listUser.get(0);
		log.info("Username {} {} (id = {})", username, user == null ? " not found" : "found", user == null ? "/" : user.getId());
		return user;
	}

	@Override
	public User create(User obj) {
		// Get the hibernate session
		Session session = sessionFactory.getCurrentSession();
		// Persist the object
		session.persist(obj);
		// Log the result
		log.info("User {} (id = {})", obj.getId() > 0 ? "created" : "not created", obj.getId());
		return obj;
	}

	@Override
	protected String getTableName() {
		return "User";
	}

	@Override
	protected String getOrderText(Constraint constraint) {
		return "";
	}

	@Override
	protected Logger getLogger() {
		return log;
	}
}
