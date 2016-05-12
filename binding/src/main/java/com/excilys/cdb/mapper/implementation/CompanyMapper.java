package com.excilys.cdb.mapper.implementation;

import com.excilys.cdb.dto.IDto;
import com.excilys.cdb.dto.implementation.CompanyDto;
import com.excilys.cdb.mapper.IMapper;
import com.excilys.cdb.model.Company;

/**
 * Mapper implementation for companies
 */
public class CompanyMapper implements IMapper<Company> {

	private CompanyMapper() {
	}

    @Override
    public Company getFromDTO(IDto dto) {
        CompanyDto companyDTO = (CompanyDto) dto;
        Company company = new Company();
        company.setId(companyDTO.getId());
        company.setName(companyDTO.getName());
        return company;
    }

    @Override
    public IDto getFromModel(Company model) {
        CompanyDto dto = new CompanyDto();
        dto.setId(model.getId());
        dto.setName(model.getName());
        return dto;
    }
}