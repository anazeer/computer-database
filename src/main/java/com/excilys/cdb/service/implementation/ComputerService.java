package com.excilys.cdb.service.implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.excilys.cdb.exception.DAOException;
import com.excilys.cdb.mapper.implementation.ComputerMapper;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.pagination.implementation.ComputerPage;
import com.excilys.cdb.pagination.util.PageRequest;
import com.excilys.cdb.persistence.dao.implementation.ComputerDAO;
import com.excilys.cdb.service.util.Query;

/**
 * Service implementation for computers
 */
@Service
public class ComputerService implements com.excilys.cdb.service.IService<Computer> {

	@Autowired
	private ComputerDAO computerDAO;
	
	@Autowired
	private ComputerMapper computerMapper;

	private ComputerService() {
		super();
	}
	
	@Override
	public ComputerPage getPage(PageRequest pageRequest) {
		Query query = pageRequest.getQuery();
		ComputerPage computerPage = new ComputerPage(computerMapper, pageRequest, count(query));
		query.setOffset(computerPage.getOffset());
		List<Computer> computers = list(query);
		computerPage.setElements(computers);
		return computerPage;
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
	
	public void create(Computer computer) throws DAOException {
		computerDAO.create(computer);
	}
	
	public void update(Computer computer) throws DAOException {
		computerDAO.update(computer);
	}
	
	public void delete(Computer computer) {
		computerDAO.delete(computer);
	}
	
	@Override
	public void delete(Long id) {
		computerDAO.delete(id);
	}
}
