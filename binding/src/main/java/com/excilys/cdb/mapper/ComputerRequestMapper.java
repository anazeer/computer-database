package com.excilys.cdb.mapper;

import java.util.Map;

import com.excilys.cdb.dto.implementation.ComputerDTO;

/**
 * Computer mapper for request
 */
public class ComputerRequestMapper {

	// ID from the POST form
	private final String nameParam = "computerName";
	private final String introParam = "introduced";
	private final String discontinuedParam = "discontinued";
	private final String companyIdParam = "companyId";
	
	// ID for the URL parameter id
	private final String idParam = "id";

	public ComputerRequestMapper() {
	}

	/**
	 * Analyze the user request and construct a Computer DTO
	 * @param request the user request
	 * @return the DTO depending on the user inputs
	 */
	public ComputerDTO getFromRequest(Map<String, String> request) {
		
		// We first get the parameters from the POST form
		String computerId = request.get(idParam);
		computerId = computerId == null || computerId.trim().isEmpty() ? null : computerId.trim();
		String name = request.get(nameParam);
		String companyId = request.get(companyIdParam);
		companyId = companyId == null || companyId.trim().isEmpty() ? null : companyId.trim();
		String introduced = request.get(introParam);
		introduced = introduced == null || introduced.trim().isEmpty() ? null : introduced.trim();
		String discontinued = request.get(discontinuedParam);
		discontinued = discontinued == null || discontinued.trim().isEmpty() ? null : discontinued.trim();
		Long idComputer = null;
		Long idCompany = null;
		try {
			idComputer = Long.parseLong(computerId);
		} catch (NumberFormatException e) {
			idComputer = 0L;
		}
		try {
			idCompany = Long.parseLong(companyId);
		} catch (NumberFormatException e) {
			idCompany = 0L;
		}
		
		// We build and return the DTO with those information
		ComputerDTO dto = new ComputerDTO
				.Builder(name)
				.id(idComputer)
				.introduced(introduced)
				.discontinued(discontinued)
				.companyId(idCompany)
				.build();
		return dto;
	}
}
