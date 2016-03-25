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
	public List<Computer> list(Query query) {
		return computerDAO.find(query);
	}
	
	@Override
	public int count(Query query) {
		return computerDAO.count(query);
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
	
	@Override
	public boolean delete(Long id) {
		return computerDAO.delete(id);
	}
}
