package com.excilys.cdb.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.persistence.dao.CompanyDAO;

/**
 * CompanyDAO test class. We assume that the database is not empty and contains 42 elements
 * @author excilys
 *
 */
public class CompanyDAOTest {

	private static CompanyDAO companyDAO;
	
	@BeforeClass
	public static void init() {
		companyDAO = new CompanyDAO();
	}

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
		List<Company> list = companyDAO.findFromOffset(0, 10);
		assertNotNull(list);
		assertEquals(list.size(), 10);
	}
	
	@Test
	public void testCountEntries() {
		assertEquals(companyDAO.countEntries(), 42);
	}
	
	@Test
	public void testFindAll() throws Exception {
		List<Company> list = companyDAO.findAll();
		assertNotNull(list);
		assertEquals(list.size(), 42);
	}
}
