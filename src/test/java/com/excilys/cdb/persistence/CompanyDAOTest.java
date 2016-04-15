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

import com.excilys.cdb.model.Company;
import com.excilys.cdb.persistence.dao.implementation.CompanyDAO;
import com.excilys.cdb.service.util.Query;

/**
 * CompanyDAO test class. We assume that the database is not empty and contains 42 elements
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/spring-context.xml"})
public class CompanyDAOTest {

	@Autowired
	private static CompanyDAO companyDAO;
	
	/**
	 * We assume that the database is not empty and contains exactly 42 keys from 1 to 42
	 * @throws Exception
	 */
	@Test
	public void testFindOk() {
		Company company = companyDAO.findById(40L);
		assertNotNull(company);
	}
	
	@Test
	public void testFindNotOk() {
		Company company = companyDAO.findById(300L);
		assertNull(company);
	}
	
	@Test
	public void testFindFromOffset() {
		Query query = new Query.Builder().offset(0).limit(10).build();
		List<Company> list = companyDAO.find(query);
		assertNotNull(list);
		assertTrue(list.size() <= 10);
	}
	
	@Test
	public void testFindFilter() {
		String filter = "in";
		Query query = new Query.Builder().filter(filter).build();
		List<Company> list = companyDAO.find(query);
		for(Company c : list) {
			assertTrue(c.getName().toLowerCase().contains(filter));
		}
	}
	
	@Test
	public void testPageFilter() {
		String filter = "In";
		Query query = new Query.Builder().offset(0).limit(10).filter(filter).build();
		List<Company> list = companyDAO.find(query);
		assertTrue(list.size() <= 10);
	}
	
	@Test
	public void testCountFilter() {
		String filter = "a";
		Query query = new Query.Builder().filter(filter).build();
		List<Company> list = companyDAO.find(query);
		assertEquals(list.size(), companyDAO.count(query));
	}
	
	@Test
	public void testCountEntries() {
		Query query = new Query.Builder().filter("  ").build();
		assertEquals(companyDAO.count(null), companyDAO.count(query));
	}
	
	@Test
	public void testFindAll() throws Exception {
		List<Company> list = companyDAO.find(null);
		assertNotNull(list);
		assertEquals(list.size(), companyDAO.count(null));
	}
}
