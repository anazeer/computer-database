package com.excilys.cdb.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.excilys.cdb.exception.DAOException;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.dao.implementation.ComputerDAO;
import com.excilys.cdb.service.util.Query;

/**
 * ComputerDAO test class. We assume that the database is not empty and contains more than 500 elements
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring-context.xml"})
public class ComputerDAOTest {
	
	@Autowired
	private ComputerDAO computerDAO;
	private final String msgId = "id should be not null";
	
	/**
	 * We assume that the database is not empty and contains at least 500 keys from 1 to 500
	 */
	@Test
	public void testFindOk() {
		Computer computer = computerDAO.findById(52L);
		assertNotNull(computer);
	}
	
	@Test
	public void testFindNotOk() {
		Computer computer = computerDAO.findById(2500L);
		assertNull(computer);
	}
	
	@Test
	public void testFindFromOffset() {
		Query query = new Query.Builder().offset(0).limit(10).build();
		List<Computer> list = computerDAO.find(query);
		assertNotNull(list);
		assertTrue(list.size() <= 10);
	}
	
	@Test
	public void testFindAll() throws Exception {
		List<Computer> list = computerDAO.find(null);
		assertNotNull(list);
		assert(list.size() > 0);
	}
	
	@Test
	public void testCountFilter() {
		String filter = "a";
		Query query = new Query.Builder().filter(filter).build();
		List<Computer> list = computerDAO.find(query);
		assertTrue(list.size() <= computerDAO.count(query));
	}
	
	@Test
	public void testCountEntries() {
		List<Computer> list = computerDAO.find(null);
		assertEquals(list.size(), computerDAO.count(null));
	}
	
	@Test
	public void testCreate() throws Exception {
		Computer computer = new Computer.Builder("Sony Cie").build();
		computer.setIntroduced(LocalDate.now());
		computer.setDiscontinued(LocalDate.now().plusDays(1));
		computer.setCompany(new Company());
		computer = computerDAO.create(computer);
		assertNotNull(msgId, computer.getId());
		assertNull(computer.getCompany().getId());
	}
	
	@Test(expected = IllegalStateException.class)
	public void testBadCreate() {
		Computer computer = new Computer.Builder("    ").build();
		computer.setIntroduced(LocalDate.now());
		computer.setDiscontinued(LocalDate.now().plusDays(1));
		computer.setCompany(new Company());
		try {
			computer = computerDAO.create(computer);
			assertNotNull(msgId, computer.getId());
			assertNull(computer.getCompany().getId());
		} catch(DAOException e) {
		}
	}
	
	@Test(expected = DateTimeParseException.class)
	public void testBadDateCreate() {
		Computer computer = new Computer.Builder("Computer z").build();
		computer.setIntroduced(LocalDate.parse("1111/11/11"));
		computer.setDiscontinued(LocalDate.now().plusDays(1));
		computer.setCompany(new Company());
		try {
			computer = computerDAO.create(computer);
			assertNotNull(msgId, computer.getId());
			assertNull(computer.getCompany().getId());
		} catch(DAOException e) {
		}
	}
	
	@Test
	public void testCreateWithCompany() throws Exception {
		Computer computer = new Computer.Builder("All").build();
		Company company = new Company();
		company.setId(40L);
		computer.setName("Sony Cie");
		computer.setCompany(company);
		computer = computerDAO.create(computer);
		assertNotNull(msgId, computer.getId());
		assertEquals(computer.getCompany().getId(), (Long) 40L);
	}
	
	@Test
	public void testCreateAndDelete() throws Exception {
		Computer computer = new Computer.Builder("Sony Cie").build();
		computer = computerDAO.create(computer);
		assertNotNull(msgId, computer.getId());
		computerDAO.delete(computer.getId());
		computer = computerDAO.findById(computer.getId());
		assertNull(computer);
	}
	
	@Test
	public void testUpdate() {
		Computer computer = computerDAO.findById(50L);
		String newName = computer.getName() + "a";
		assertNotNull(computer);
		computer.setName(newName);
		try {
			boolean bool = computerDAO.update(computer);
			assertTrue(bool);
			Computer computerUpdated = computerDAO.findById(50L);
			assertEquals(newName, computerUpdated.getName());
		} catch(DAOException e) {
        }
	}
	
	@Test
	public void testUpdate2() {
		Computer computer = computerDAO.findById(55L);
		try {
			assertTrue(computerDAO.update(computer));
		} catch (DAOException e) {
		}
	}
	
	@Ignore
	@Test
	public void testDelete() {
		Computer computer = computerDAO.findById(603L);
		try {
			computerDAO.delete(computer.getId());
		} catch (DAOException e) {	
		}
		computer = computerDAO.findById(603L);
		assertNull(computer);
	}
}
