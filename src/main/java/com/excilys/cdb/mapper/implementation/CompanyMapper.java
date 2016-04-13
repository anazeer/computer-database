package com.excilys.cdb.mapper.implementation;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.cdb.dto.IDTO;
import com.excilys.cdb.dto.implementation.CompanyDTO;
import com.excilys.cdb.mapper.IMapper;
import com.excilys.cdb.model.Company;

/**
 * Mapper implementation for companies
 */
public class CompanyMapper implements IMapper<Company> {

	private Logger log = LoggerFactory.getLogger(getClass());

	private CompanyMapper() {
	}

	@Override
	public Company getFromResultSet(ResultSet result) {
		Company company = new Company();
		try {
			company.setId(result.getLong("id"));
			company.setName(result.getString("name"));
			return company;
		} catch (SQLException e) {
			log.error(e.getMessage());
		}
		return null;
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