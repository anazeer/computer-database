package com.excilys.cdb.service;

import java.sql.SQLException;
import java.util.List;

import com.excilys.cdb.exception.DateException;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.dao.ComputerDAO;
import com.excilys.cdb.persistence.dao.DAOFactory;

/**
 * Service implementation for computers
 */
public class ComputerService implements Service<Computer> {
	
	// TODO : cache implementation

	private ComputerDAO computerDAO;
	private static ComputerService instance;

	private ComputerService() {
		super();
		computerDAO = DAOFactory.getComputerDAO();
	}
	
	public static ComputerService getInstance() {
		if(instance == null) {
			instance = new ComputerService();
		}
		return instance;
	}
	
	@Override
	public List<Computer> listAll() {
		return computerDAO.findAll();
	}
	
	@Override
	public List<Computer> listPage(int offset, int limit) {
		return computerDAO.findPage(offset, limit);
	}
	
	@Override
	public int count() {
		return computerDAO.count();
	}
	
	public Computer getComputer(Long id) {
		return computerDAO.findById(id);
	}
	
	public void create(Computer computer) throws SQLException, DateException {
		if(computer.getIntroduced() != null && computer.getDiscontinued() != null
                && computer.getIntroduced().isAfter(computer.getDiscontinued())) {
			throw new DateException("discontinued date should be after introduced date");
		}
		computerDAO.create(computer);
	}
	
	public void update(Computer computer) throws SQLException, DateException {
		if(computer.getIntroduced() != null && computer.getDiscontinued() != null
                && computer.getIntroduced().isAfter(computer.getDiscontinued())) {
			throw new DateException("discontinued date should be after introduced date");
		}
		computerDAO.update(computer);
	}
	
	public void delete(Computer computer) {
		computerDAO.delete(computer);
	}
	
	public void delete(Long id) {
		computerDAO.delete(id);
	}
}
