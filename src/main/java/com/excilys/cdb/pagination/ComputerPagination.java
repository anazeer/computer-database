package com.excilys.cdb.pagination;

import java.util.List;

import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.ComputerDAO;

public class ComputerPagination extends Pagination<Computer> {
	
	private ComputerDAO ComputerDAO;
	private List<Computer> listComputer;


	public ComputerPagination(int countEntries, int countPerPage) {
		super(countEntries, countPerPage);
		ComputerDAO = new ComputerDAO();
	}
	
	public List<Computer> listFromOffset() {
		if(!changed) {
			return listComputer;
		}
		int from = (getCurrentPage() - 1) * getCountPerPage();
		int offset = getCountPerPage();
		listComputer = ComputerDAO.findFromOffset(from, offset);
		changed = false;
		return listComputer;
	}
}