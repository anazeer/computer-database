package com.excilys.cdb.persistence.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.dao.CompanyDAO;

public class ComputerMapper implements DAOMapper<Computer> {

	public ComputerMapper() {
		super();
	}
	
	@Override
	public Computer find(ResultSet result) {
		try {
			Computer computer = new Computer(result.getString("name"));
			LocalDate introducedDate = result.getDate("introduced") == null ? null : result.getDate("introduced").toLocalDate();
			LocalDate discontinuedDate = result.getDate("discontinued") == null ? null : result.getDate("discontinued").toLocalDate();
			computer.setId(result.getLong("id"));
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
