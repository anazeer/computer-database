package com.excilys.cdb.service.implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.excilys.cdb.dao.implementation.ComputerDAO;
import com.excilys.cdb.mapper.implementation.ComputerMapper;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.pagination.implementation.ComputerPage;
import com.excilys.cdb.pagination.util.PageRequest;
import com.excilys.cdb.util.Constraint;

/**
 * Service implementation for computers
 */
@Transactional
@Service
public class ComputerService implements com.excilys.cdb.service.IService<Computer> {

	@Autowired
	private ComputerDAO computerDAO;
	
	@Autowired
	private ComputerMapper computerMapper;

	public ComputerService() {
	}
	
	@Override
	public Computer findById(Long id) {
		return computerDAO.findById(id);
	}
	
	@Override
	public ComputerPage getPage(PageRequest pageRequest) {
		Constraint query = pageRequest.getQuery();
		ComputerPage computerPage = new ComputerPage(computerMapper, pageRequest, count(query));
		query.setOffset(computerPage.getOffset());
		List<Computer> computers = list(query);
		computerPage.setElements(computers);
		return computerPage;
	}

	@Override
	public List<Computer> list(Constraint constraint) {
		return computerDAO.find(constraint);
	}
	
	@Override
	public int count(Constraint constraint) {
		return computerDAO.count(constraint);
	}
	
	public Computer getComputer(Long id) {
		return computerDAO.findById(id);
	}
	
	@Override
	public Computer create(Computer computer) {
		return computerDAO.create(computer);
	}
	
	@Override
	public void update(Computer computer) {
		computerDAO.update(computer);
	}
	
	@Override
	public void delete(Long id) {
		computerDAO.delete(id);
	}
}
