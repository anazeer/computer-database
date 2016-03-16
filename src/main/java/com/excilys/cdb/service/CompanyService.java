package com.excilys.cdb.service;

import java.util.ArrayList;
import java.util.List;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.persistence.dao.CompanyDAO;
import com.excilys.cdb.service.dto.CompanyDTO;
import com.excilys.cdb.service.dto.DTO;

public class CompanyService implements ServiceOperations<Company> {
	
	private CompanyDAO companyDAO;
	private static CompanyService instance;
	
	private CompanyService() {
		super();
		companyDAO = new CompanyDAO();
	}
	
	public static CompanyService getInstance() {
		if(instance == null) {
			instance = new CompanyService();
		}
		return instance;
	}
	
	@Override
	public List<CompanyDTO> list() {
		List<CompanyDTO> listCompany = new ArrayList<>();
		for(Company company :  companyDAO.findAll()) {
			listCompany.add(createDTO(company));
		}
		return listCompany;
	}
	
	@Override
	public List<CompanyDTO> listFromOffset(int from, int offset) {
		List<CompanyDTO> companyDTO = new ArrayList<>();
		for(Company company : companyDAO.findFromOffset(from, offset)) {
			companyDTO.add(createDTO(company));
		}
		return companyDTO;
	}
	
	@Override
	public int countEntries() {
		return companyDAO.countEntries();
	}
	
	@Override
	public CompanyDTO createDTO(Company source) {
		CompanyDTO dto = new CompanyDTO();
		dto.setId(source.getId());
		dto.setName(source.getName());
		return dto;
	}

	@Override
	public Company getFromDTO(DTO source) {
		CompanyDTO dto = (CompanyDTO) source;
		Company company = new Company();
		company.setId(dto.getId());
		company.setName(dto.getName());
		return company;
	}
	
}