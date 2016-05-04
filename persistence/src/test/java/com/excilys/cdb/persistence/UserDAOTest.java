package com.excilys.cdb.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.excilys.cdb.dao.implementation.UserDAO;
import com.excilys.cdb.model.User;
import com.excilys.cdb.util.Constraint;

/**
 * UserDAO test class. We assume that the database for testing is not empty and contains 2 elements
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/persistence-context.xml"})
@Transactional
public class UserDAOTest {

	@Autowired
	private UserDAO userDAO;
	
	/**
	 * The test database contains a 24th element that is Nintendo User
	 */
	@Test
	public void testFindById() {
		User user = userDAO.findById(1L);
		assertNotNull(user);
		assertEquals("admin", user.getUsername());
	}
	
	@Test
	public void testFindByUsername() {
		String username = "user";
		User user = userDAO.findByUsername(username);
		assertNotNull(user);
		assertEquals(username, user.getUsername());
	}
	
	@Test
	public void testFindNotOk() {
		User user = userDAO.findById(300L);
		assertNull(user);
	}
	
	@Test
	public void testFindPageQuery() {
		Constraint query = new Constraint.Builder().offset(0).limit(1).build();
		List<User> list = userDAO.find(query);
		assertNotNull(list);
		assertTrue(list.size() == 1);
	}
	
	@Test
	public void testFindFilterQuery() {
		String filter = "in";
		Constraint query = new Constraint.Builder().filter(filter).build();
		List<User> list = userDAO.find(query);
		for (User u : list) {
			assertTrue(u.getUsername().toLowerCase().contains(filter));
		}
	}
	
	@Test
	public void testFindQuery() {
		String filter = "In";
		Constraint query = new Constraint.Builder().offset(0).limit(10).filter(filter).build();
		List<User> list = userDAO.find(query);
		assertTrue(list.size() <= 10);
	}
	
	@Test
	public void testCount() {
		int count = userDAO.count(null);
		assertEquals(2, count);
	}
	
	@Test
	public void testCountFilter() {
		String filter = "a";
		Constraint query = new Constraint.Builder().filter(filter).build();
		List<User> list = userDAO.find(query);
		assertEquals(list.size(), userDAO.count(query));
	}
	
	@Test
	public void testFindAll() throws Exception {
		List<User> list = userDAO.find(null);
		assertNotNull(list);
		assertEquals(2, list.size());
	}
}
