package com.excilys.cdb.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import com.excilys.cdb.persistence.dao.DAOFactory;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.dao.ComputerDAO;

/**
 * ComputerDAO test class. We assume that the database is not empty and contains more than 500 elements
 * @author excilys
 *
 */
public class ComputerDAOTest {
	
	private static ComputerDAO computerDAO;
	private static final String msgId = "id should be not null";
	
	@BeforeClass
	public static void init() {
		computerDAO = DAOFactory.getComputerDAO();
	}
	
	/**
	 * We assume that the database is not empty and contains at least 500 keys from 1 to 500
	 * @throws Exception
	 */
	@Test
	public void testFindOk() throws Exception {
		Computer computer = computerDAO.findById(52L);
		assertNotNull(computer);
	}
	
	@Test
	public void testFindNotOk() throws Exception {
		Computer computer = computerDAO.findById(2500L);
		assertNull(computer);
	}
	
	@Test
	public void testFindFromOffset() {
		List<Computer> list = computerDAO.findPage(0, 10);
		assertNotNull(list);
		assertTrue(list.size() <= 10);
	}
	
	@Test
	public void testFindAll() throws Exception {
		List<Computer> list = computerDAO.findAll();
		assertNotNull(list);
		assert(list.size() > 0);
	}
	
	@Test
	public void testFindFilter() {
		String filter = "apple";
		List<Computer> list = computerDAO.findAll(filter);
		for(Computer c : list) {
			boolean contains = 
					   c.getName().toLowerCase().contains(filter) 
					|| (c.getIntroduced() != null && c.getIntroduced().toString().toLowerCase().contains(filter))
					|| (c.getDiscontinued() != null && c.getDiscontinued().toString().toLowerCase().contains(filter))
					|| (c.getCompany() != null && DAOFactory.getCompanyDAO().findById(c.getCompany().getId()).getName().toLowerCase().contains(filter));
			assertTrue(contains);
		}
	}
	
	@Test
	public void testCountFilter() {
		String filter = "a";
		List<Computer> list = computerDAO.findAll(filter);
		assertTrue(list.size() <= computerDAO.count(filter));
	}
	
	@Test
	public void testCountEntries() {
		List<Computer> list = computerDAO.findAll();
		assertEquals(list.size(), computerDAO.count());
	}
	
	@Test
	public void testCreate() throws Exception {
		Computer computer = new Computer("Sony Cie");
		computer.setIntroduced(LocalDate.now());
		computer.setDiscontinued(LocalDate.now().plusDays(1));
		computer.setCompany(new Company());
		computer = computerDAO.create(computer);
		assertNotNull(msgId, computer.getId());
		assertNull(computer.getCompany().getId());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testBadCreate() {
		Computer computer = new Computer("    ");
		computer.setIntroduced(LocalDate.now());
		computer.setDiscontinued(LocalDate.now().plusDays(1));
		computer.setCompany(new Company());
		try {
			computer = computerDAO.create(computer);
			assertNotNull(msgId, computer.getId());
			assertNull(computer.getCompany().getId());
		}
		catch(SQLException e) {
		}
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testBadDateCreate() {
		Computer computer = new Computer("    ");
		computer.setIntroduced(LocalDate.parse("1111/11/11"));
		computer.setDiscontinued(LocalDate.now().plusDays(1));
		computer.setCompany(new Company());
		try {
			computer = computerDAO.create(computer);
			assertNotNull(msgId, computer.getId());
			assertNull(computer.getCompany().getId());
		}
		catch(SQLException e) {
		}
	}
	
	@Test
	public void testCreateWithCompany() throws Exception {
		Computer computer = new Computer("All");
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
		Computer computer = new Computer("Sony Cie");
		computer = computerDAO.create(computer);
		assertNotNull(msgId, computer.getId());
		computerDAO.delete(computer);
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
		}
        catch(SQLException e) {
        }
	}
	
	@Test
	public void testUpdate2() {
		Computer computer = computerDAO.findById(55L);
		try {
			assertTrue(computerDAO.update(computer));
		}
		catch (SQLException e) {
		}
	}
	
	@Ignore
	@Test
	public void testDelete() {
		Computer computer = computerDAO.findById(603L);
		computerDAO.delete(computer);
		computer = computerDAO.findById(603L);
		assertNull(computer);
	}
	
}
