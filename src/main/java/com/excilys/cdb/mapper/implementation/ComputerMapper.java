package com.excilys.cdb.mapper.implementation;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.excilys.cdb.dto.IDTO;
import com.excilys.cdb.dto.implementation.ComputerDTO;
import com.excilys.cdb.mapper.IMapper;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.dao.implementation.CompanyDAO;

/**
 * Mapper implementation for computers
 */
@Component
public class ComputerMapper implements IMapper<Computer> {

    @Autowired
    private CompanyDAO companyDAO;
    
	private Logger log = LoggerFactory.getLogger(getClass());

	private ComputerMapper() {
	}
	
	@Override
	public Computer getFromResultSet(ResultSet result) {
		try {
            String name = result.getString("name");
            Long id = result.getLong("id");
            Date introducedDate = result.getDate("introduced");
			LocalDate introduced = introducedDate == null ? null : introducedDate.toLocalDate();
            Date discontinuedDate = result.getDate("discontinued");
			LocalDate discontinued = discontinuedDate == null ? null : discontinuedDate.toLocalDate();
            Company company = companyDAO.findById(result.getLong("company_id"));
            return new Computer.Builder(name)
                    .id(id)
                    .introduced(introduced)
                    .discontinued(discontinued)
                    .company(company)
                    .build();
		} catch (SQLException e) {
            log.error(e.getMessage());
		}
		return null;
	}

    @Override
    public Computer getFromDTO(IDTO dto) {
        ComputerDTO computerDTO = (ComputerDTO) dto;
        String name = computerDTO.getName();
        Long id = computerDTO.getId();
        String introducedString = computerDTO.getIntroduced();
        LocalDate introduced = introducedString == null ? null : LocalDate.parse(introducedString);
        String discontinuedString = computerDTO.getDiscontinued();
        LocalDate discontinued = discontinuedString == null ? null : LocalDate.parse(computerDTO.getDiscontinued());
        Company company = companyDAO.findById(computerDTO.getCompanyId());
        return new Computer.Builder(name)
                .id(id)
                .introduced(introduced)
                .discontinued(discontinued)
                .company(company)
                .build();
    }

    @Override
    public IDTO getFromModel(Computer model) {
        String name =  model.getName();
        Long id = model.getId();
        LocalDate introducedDate = model.getIntroduced();
        String introduced = introducedDate == null ? null : introducedDate.toString();
        LocalDate discontinuedDate = model.getDiscontinued();
        String discontinued = discontinuedDate == null ? null : discontinuedDate.toString();
        Company company = model.getCompany();
        Long companyId = null;
        String companyName = null;
        if (company != null) {
            companyId = company.getId();
            companyName = company.getName();
        }
        return new ComputerDTO.Builder(name)
                .id(id)
                .introduced(introduced)
                .discontinued(discontinued)
                .companyId(companyId)
                .companyName(companyName)
                .build();
    }
}
