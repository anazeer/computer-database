package com.excilys.cdb.persistence.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.CompanyDAO;

public class ComputerMapper implements DAOMapper<Computer> {

	public ComputerMapper() {
		super();
	}
	
	@Override
	public Computer find(ResultSet result) {
		try {
			Computer computer = new Computer();
			LocalDate introducedDate = result.getDate("introduced") == null ? null : result.getDate("introduced").toLocalDate();
			LocalDate discontinuedDate = result.getDate("introduced") == null ? null : result.getDate("introduced").toLocalDate();
			computer.setId(result.getLong("id"));
			computer.setName(result.getString("name"));
			computer.setIntroduced(introducedDate);
			computer.setDiscontinued(discontinuedDate);
			computer.setCompany(new CompanyDAO().findById(result.getLong("company_id")));
			return computer;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
