package com.excilys.cdb.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.excilys.cdb.dao.implementation.ComputerDao;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.util.Constraint;
import com.excilys.cdb.util.Order;

/**
 * ComputerDAO test class. We assume that the database is not empty and contains more than 500 elements
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/persistence-context.xml"})
@Transactional
public class ComputerDaoTest {

	@Autowired
	private ComputerDao computerDAO;
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
		Constraint query = new Constraint.Builder().offset(0).limit(10).build();
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
		Constraint query = new Constraint.Builder().filter(filter).build();
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
		computer = computerDAO.create(computer);
		assertNotNull(msgId, computer.getId());
		assertNull(computer.getCompany().getId());

	}

	@Test(expected = DateTimeParseException.class)
	public void testBadDateCreate() {
		Computer computer = new Computer.Builder("Computer z").build();
		computer.setIntroduced(LocalDate.parse("1111/11/11"));
		computer.setDiscontinued(LocalDate.now().plusDays(1));
		computer.setCompany(new Company());
		computer = computerDAO.create(computer);
		assertNotNull(msgId, computer.getId());
		assertNull(computer.getCompany().getId());
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
		boolean bool = computerDAO.update(computer);
		assertTrue(bool);
		Computer computerUpdated = computerDAO.findById(50L);
		assertEquals(newName, computerUpdated.getName());

	}

	@Test
	public void testUpdate2() {
		Computer computer = computerDAO.findById(55L);
		assertTrue(computerDAO.update(computer));
	}

	@Test
	public void testDelete() {
		Computer computer = computerDAO.create(new Computer.Builder("Test").build());
		Long id = computer.getId();
		assertNotNull(id);
		computerDAO.delete(id);
		computer = computerDAO.findById(id);
		assertNull(computer);
	}

	@Test
	public void testOrderByNameAsc() {
		Constraint query = new Constraint.Builder().order(Order.NAME_ASC).build();
		List<Computer> list = computerDAO.find(query);
		for (int i = 0; i < list.size() - 1; i++) {
			Computer comp1 = list.get(i);
			Computer comp2 = list.get(i + 1);
			assertTrue(comp1.getName().toLowerCase().compareTo(comp2.getName().toLowerCase()) <= 0);
		}
	}

	@Test
	public void testOrderByNameDesc() {
		Constraint query = new Constraint.Builder().order(Order.NAME_DSC).build();
		List<Computer> list = computerDAO.find(query);
		for (int i = 0; i < list.size() - 1; i++) {
			Computer comp1 = list.get(i);
			Computer comp2 = list.get(i + 1);
			assertTrue(comp1.getName().toLowerCase().compareTo(comp2.getName().toLowerCase()) >= 0);
		}
	}

	@Test
	public void testOrderByCompanyNameAsc() {
		Constraint query = new Constraint.Builder().order(Order.COMPANY_ASC).build();
		List<Computer> list = computerDAO.find(query);
		for (int i = 0; i < list.size() - 1; i++) {
			Company comp1 = list.get(i).getCompany();
			Company comp2 = list.get(i + 1).getCompany();
			if (comp1 != null && comp2 != null) {
				assertTrue(comp1.getName().toLowerCase().compareTo(comp2.getName().toLowerCase()) <= 0);
			}
		}
	}

	@Test
	public void testOrderByCompanyNameDesc() {
		Constraint query = new Constraint.Builder().order(Order.COMPANY_DSC).build();
		List<Computer> list = computerDAO.find(query);
		for (int i = 0; i < list.size() - 1; i++) {
			Company comp1 = list.get(i).getCompany();
			Company comp2 = list.get(i + 1).getCompany();
			if (comp1 != null && comp2 != null) {
				assertTrue(comp1.getName().toLowerCase().compareTo(comp2.getName().toLowerCase()) >= 0);
			}
		}
	}
}
