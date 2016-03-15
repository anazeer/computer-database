package com.excilys.cdb.pagination;

import java.util.List;

import com.excilys.cdb.service.ComputerService;
import com.excilys.cdb.service.dto.ComputerDTO;

public class ComputerPagination extends Pagination<ComputerDTO> {
	
	private ComputerService computerService;
	private List<ComputerDTO> listFromOffset;


	public ComputerPagination(int countEntries, int countPerPage) {
		super(countEntries, countPerPage);
		computerService = new ComputerService();
	}
	
	public List<ComputerDTO> getListFromOffset() {
		if(!changed) {
			return listFromOffset;
		}
		int from = (getCurrentPage() - 1) * getCountPerPage();
		int offset = getCountPerPage();
		listFromOffset = computerService.listFromOffset(from, offset);
		changed = false;
		return listFromOffset;
	}		
}