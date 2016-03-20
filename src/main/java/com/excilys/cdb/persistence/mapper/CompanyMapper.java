package com.excilys.cdb.persistence.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.service.dto.CompanyDTO;
import com.excilys.cdb.service.dto.DTO;
import org.apache.log4j.Logger;

/**
 * Mapper implementation for companies
 * @author excilys
 *
 */
public class CompanyMapper implements Mapper<Company> {

    private static CompanyMapper instance;
	private Logger log = Logger.getLogger(getClass());

    /**
     * CompanyMapper new instance for Company type object mapping
     */
	private CompanyMapper() {
	}

    /**
     *
     * @ the computer mapper implementation instance
     */
    public static CompanyMapper getInstance() {
        if(instance == null) {
            instance = new CompanyMapper();
        }
        return instance;
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
    public Company getFromDTO(DTO dto) {
        CompanyDTO companyDTO = (CompanyDTO) dto;
        Company company = new Company();
        company.setId(companyDTO.getId());
        company.setName(companyDTO.getName());
        return company;
    }

    @Override
    public DTO getFromModel(Company model) {
        CompanyDTO dto = new CompanyDTO();
        dto.setId(model.getId());
        dto.setName(model.getName());
        return dto;
    }
}