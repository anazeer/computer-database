package com.excilys.cdb.mapper.implementation;

import com.excilys.cdb.dto.IDTO;
import com.excilys.cdb.dto.implementation.CompanyDTO;
import com.excilys.cdb.mapper.IMapper;
import com.excilys.cdb.model.Company;

/**
 * Mapper implementation for companies
 */
public class CompanyMapper implements IMapper<Company> {

	private CompanyMapper() {
	}

    @Override
    public Company getFromDTO(IDTO dto) {
        CompanyDTO companyDTO = (CompanyDTO) dto;
        Company company = new Company();
        company.setId(companyDTO.getId());
        company.setName(companyDTO.getName());
        return company;
    }

    @Override
    public IDTO getFromModel(Company model) {
        CompanyDTO dto = new CompanyDTO();
        dto.setId(model.getId());
        dto.setName(model.getName());
        return dto;
    }
}