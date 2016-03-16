package com.excilys.cdb.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.dao.CompanyDAO;
import com.excilys.cdb.persistence.dao.ComputerDAO;
import com.excilys.cdb.service.dto.ComputerDTO;
import com.excilys.cdb.service.dto.DTO;

public class ComputerService implements ServiceOperations<Computer> {

	private ComputerDAO computerDAO;
	private static ComputerService instance;
	
	private ComputerService() {
		super();
		computerDAO = new ComputerDAO();
	}
	
	public static ComputerService getInstance() {
		if(instance == null) {
			instance = new ComputerService();
		}
		return instance;
	}
	
	@Override
	public List<ComputerDTO> list() {
		List<ComputerDTO> listComputer = new ArrayList<>();
		for(Computer computer : computerDAO.findAll()) {
			listComputer.add(createDTO(computer));
		}
		return listComputer;
	}
	
	@Override
	public List<ComputerDTO> listFromOffset(int from, int offset) {
		List<ComputerDTO> computerDTO = new ArrayList<>();
		for(Computer computer : computerDAO.findFromOffset(from, offset)) {
			computerDTO.add(createDTO(computer));
		}
		return computerDTO;
	}
	
	@Override
	public int countEntries() {
		return computerDAO.countEntries();
	}
	
	@Override
	public ComputerDTO createDTO(Computer source) {
		ComputerDTO dto = new ComputerDTO();
		dto.setId(source.getId());
		dto.setName(source.getName());
		if(source.getIntroduced() != null)
			dto.setIntroduced(source.getIntroduced().toString());
		if(source.getDiscontinued() != null)
			dto.setDiscontinued(source.getDiscontinued().toString());
		if(source.getCompany() != null) {
			dto.setCompanyId(source.getCompany().getId());
			dto.setCompanyName(source.getCompany().getName());
		}
		return dto;
	}
	
	@Override
	public Computer getFromDTO(DTO source) {
		ComputerDTO dto = (ComputerDTO) source;
		Computer computer = new Computer(dto.getName());
		computer.setId(dto.getId());
		if(dto.getIntroduced() != null)
			computer.setIntroduced(LocalDate.parse(dto.getIntroduced()));
		if(dto.getDiscontinued() != null)
			computer.setDiscontinued(LocalDate.parse(dto.getDiscontinued()));
		computer.setCompany(new CompanyDAO().findById(dto.getId()));
		return computer;
	}
	
	public Computer getComputer(Long id) {
		Computer computer = computerDAO.findById(id);
		return computer;
	}
	
	public void create(Computer computer) {
		computerDAO.create(computer);
	}
	
	public void update(Computer computer) {
		computerDAO.update(computer);
	}
	
	public void delete(Computer computer) {
		computerDAO.delete(computer);
	}
	
	public void delete(Long id) {
		computerDAO.delete(id);
	}

}
