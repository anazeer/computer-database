package com.excilys.cdb.mapper.implementation;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.excilys.cdb.dao.implementation.CompanyDAO;
import com.excilys.cdb.dto.IDTO;
import com.excilys.cdb.dto.implementation.ComputerDTO;
import com.excilys.cdb.mapper.IMapper;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;

/**
 * Mapper implementation for computers
 */
@Component
@Transactional
public class ComputerMapper implements IMapper<Computer> {

    @Autowired
    private CompanyDAO companyDAO;
    
    @Autowired
    private MessageSource messageSource;
    
    @Override
    public Computer getFromDTO(IDTO dto) {
        ComputerDTO computerDTO = (ComputerDTO) dto;
        String name = computerDTO.getName();
        Long id = computerDTO.getId();
        DateTimeFormatter formatter = getFormat();
        String introducedString = computerDTO.getIntroduced();
        LocalDate introduced = introducedString == null || introducedString.trim().isEmpty() ? null : LocalDate.parse(introducedString, formatter);
        String discontinuedString = computerDTO.getDiscontinued();
        LocalDate discontinued = discontinuedString == null || discontinuedString.trim().isEmpty() ? null : LocalDate.parse(computerDTO.getDiscontinued(), formatter);
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
        DateTimeFormatter formatter = getFormat();
        LocalDate introducedDate = model.getIntroduced();
        String introduced = introducedDate == null ? null : introducedDate.format(formatter).toString();
        LocalDate discontinuedDate = model.getDiscontinued();
        String discontinued = discontinuedDate == null ? null : discontinuedDate.format(formatter).toString();
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
    
    /**
     * Retrieve the current locale and return the associated date format
     * @return the date format
     */
    private DateTimeFormatter getFormat() {
    	String pattern = messageSource.getMessage("util.date.format", null, LocaleContextHolder.getLocale());
    	return DateTimeFormatter.ofPattern(pattern);
    }
}
