package com.excilys.cdb.service;

import java.util.List;

import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.ComputerDAO;

public class ComputerService implements ServiceOperations {

	private ComputerDAO computerDAO;
	
	public ComputerService() {
		super();
		computerDAO = new ComputerDAO();
	}
	
	@Override
	public void list() {
		List<Computer> listComputer = computerDAO.findAll();
		System.out.println("---------------------");
		System.out.println("- List of computers -");
		System.out.println("---------------------");
		for(Computer computer : listComputer) {
			System.out.println(computer);
		}
	}
	
	@Override
	public int countEntries() {
		return computerDAO.countEntries();
	}
	
	public void showDetails(Long id) {
		Computer computer = computerDAO.findById(id);
		if(computer == null) {
			System.out.println("No computer is referenced by id " + id);
		}
		else {
			System.out.println(computer.toDetailedString());
		}
	}
	
	public void create(Computer computer) {
		computerDAO.create(computer);
	}
	
	public void update(Computer computer) {
		computerDAO.update(computer);
	}
	
	public void delete(Computer computer) {
		computerDAO.delete(computer);
	}
	
	public void delete(Long id) {
		computerDAO.delete(id);
	}
}
