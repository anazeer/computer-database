package com.excilys.cdb.persistence;

import static org.junit.Assert.*;


import java.util.List;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.ComputerDAO;

/**
 * ComputerDAO test class. We assume that the database is not empty
 * @author excilys
 *
 */
public class ComputerDAOTest {
	
	private static ComputerDAO computerDAO;
	private static final String msgId = "id should be not null";
	
	@BeforeClass
	public static void init() {
		computerDAO = new ComputerDAO();
	}
	
	/**
	 * We assume that the database is not empty and contains at least 500 keys from 1 to 500
	 * @throws Exception
	 */
	@Test
	public void testFind() throws Exception {
		Computer computer = computerDAO.findById(52L);
		assertNotNull(computer);
	}
	
	@Test
	public void testFindAll() throws Exception {
		List<Computer> list = computerDAO.findAll();
		assertNotNull(list);
		assert(list.size() > 0);
	}
	
	@Test
	public void testCreate() throws Exception {
		Computer computer = new Computer();
		computer.setName("Sony Cie");
		computer = computerDAO.create(computer);
		assertNotNull(msgId, computer.getId());
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
	
	@Ignore
	@Test
	public void testDelete() {
		Computer computer = computerDAO.findById(603L);
		computerDAO.delete(computer);
		computer = computerDAO.findById(603L);
		assertNull(computer);
	}
	
}
