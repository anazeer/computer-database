package com.excilys.cdb.pagination;

import java.util.List;

import com.excilys.cdb.service.CompanyService;
import com.excilys.cdb.service.dto.CompanyDTO;

public class CompanyPagination extends Pagination<CompanyDTO> {
	
	private CompanyService companyService;
	private List<CompanyDTO> listFromOffset;


	public CompanyPagination(int countEntries, int countPerPage) {
		super(countEntries, countPerPage);
		companyService = CompanyService.getInstance();
	}
	
	public List<CompanyDTO> getListFromOffset() {
		if(!changed) {
			return listFromOffset;
		}
		int from = (getCurrentPage() - 1) * getCountPerPage();
		int offset = getCountPerPage();
		listFromOffset = companyService.listFromOffset(from, offset);
		changed = false;
		return listFromOffset;
	}
}
