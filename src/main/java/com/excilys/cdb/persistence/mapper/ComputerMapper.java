package com.excilys.cdb.persistence.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.dao.CompanyDAO;
import com.excilys.cdb.persistence.dao.DAOFactory;
import com.excilys.cdb.service.dto.ComputerDTO;
import com.excilys.cdb.service.dto.DTO;
import org.apache.log4j.Logger;

/**
 * Mapper implementation for computers
 */
public class ComputerMapper implements Mapper<Computer> {

	private Logger log = Logger.getLogger(getClass());
    private static ComputerMapper instance;
    private CompanyDAO companyDAO;

    /**
     * ComputerMapper new instance for Computer type object mapping
     */
	private ComputerMapper() {
        companyDAO = DAOFactory.getCompanyDAO();
	}

    /**
     *
     * @return the computer mapper implementation instance
     */
    public static ComputerMapper getInstance() {
        if(instance == null) {
            instance = new ComputerMapper();
        }
        return instance;
    }
	
	@Override
	public Computer getFromResultSet(ResultSet result) {
		try {
			Computer computer = new Computer(result.getString("name"));
			LocalDate introducedDate = result.getDate("introduced") == null ? null : result.getDate("introduced").toLocalDate();
			LocalDate discontinuedDate = result.getDate("discontinued") == null ? null : result.getDate("discontinued").toLocalDate();
			computer.setId(result.getLong("id"));
			computer.setIntroduced(introducedDate);
			computer.setDiscontinued(discontinuedDate);
			computer.setCompany(companyDAO.findById(result.getLong("company_id")));
			return computer;
		} catch (SQLException e) {
            log.error(e.getMessage());
		}
		return null;
	}

    @Override
    public Computer getFromDTO(DTO dto) {
        ComputerDTO computerDTO = (ComputerDTO) dto;
        LocalDate introduced = null;
        LocalDate discontinued = null;
        Company company = companyDAO.findById(computerDTO.getCompanyId());
        if(computerDTO.getIntroduced() != null) {
            introduced = LocalDate.parse(computerDTO.getIntroduced());
        }
        if(computerDTO.getDiscontinued() != null) {
            discontinued = LocalDate.parse(computerDTO.getDiscontinued());
        }
       return new Computer.ComputerBuilder(
                computerDTO.getName())
                .id(computerDTO.getId())
                .introduced(introduced)
                .discontinued(discontinued)
                .company(company)
                .build();
    }

    @Override
    public DTO getFromModel(Computer model) {
        String introduced = null;
        String discontinued = null;
        Long companyId = null;
        String companyName = null;
        if(model.getIntroduced() != null)
             introduced = model.getIntroduced().toString();
        if(model.getDiscontinued() != null)
            discontinued = model.getDiscontinued().toString();
        if(model.getCompany() != null) {
            companyId = model.getCompany().getId();
            companyName = model.getCompany().getName();
        }
        return new ComputerDTO.ComputerDTOBuilder(
                model.getName())
                .id(model.getId())
                .introduced(introduced)
                .discontinued(discontinued)
                .companyId(companyId)
                .companyName(companyName)
                .build();
    }
}
