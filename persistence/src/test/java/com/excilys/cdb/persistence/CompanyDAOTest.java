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

import com.excilys.cdb.dao.implementation.CompanyDAO;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.util.Constraint;

/**
 * CompanyDAO test class. We assume that the database for testing is not empty and contains 42 elements
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/persistence-context.xml"})
@Transactional
public class CompanyDAOTest {

	@Autowired
	private CompanyDAO companyDAO;
	
	/**
	 * The test database contains a 24th element that is Nintendo company
	 */
	@Test
	public void testFindById() {
		Company company = companyDAO.findById(24L);
		assertNotNull(company);
		assertEquals("Nintendo", company.getName());
	}
	
	@Test
	public void testFindNotOk() {
		Company company = companyDAO.findById(300L);
		assertNull(company);
	}
	
	@Test
	public void testFindPageQuery() {
		Constraint query = new Constraint.Builder().offset(0).limit(10).build();
		List<Company> list = companyDAO.find(query);
		assertNotNull(list);
		assertTrue(list.size() == 10);
	}
	
	@Test
	public void testFindFilterQuery() {
		String filter = "in";
		Constraint query = new Constraint.Builder().filter(filter).build();
		List<Company> list = companyDAO.find(query);
		for (Company c : list) {
			assertTrue(c.getName().toLowerCase().contains(filter));
		}
	}
	
	@Test
	public void testFindQuery() {
		String filter = "In";
		Constraint query = new Constraint.Builder().offset(0).limit(10).filter(filter).build();
		List<Company> list = companyDAO.find(query);
		assertTrue(list.size() <= 10);
	}
	
	@Test
	public void testCount() {
		int count = companyDAO.count(null);
		assertEquals(42, count);
	}
	
	@Test
	public void testCountFilter() {
		String filter = "a";
		Constraint query = new Constraint.Builder().filter(filter).build();
		List<Company> list = companyDAO.find(query);
		assertEquals(list.size(), companyDAO.count(query));
	}
	
	@Test
	public void testFindAll() throws Exception {
		List<Company> list = companyDAO.find(null);
		assertNotNull(list);
		assertEquals(42, list.size());
	}
}
