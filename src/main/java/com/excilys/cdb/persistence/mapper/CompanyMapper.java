package com.excilys.cdb.persistence.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.excilys.cdb.model.Company;

/**
 * 
 * @author excilys
 *
 */
public class CompanyMapper implements DAOMapper<Company> {

	public CompanyMapper() {
		super();
	}

	@Override
	public Company find(ResultSet result) {
		Company company = new Company();
		try {
			company.setId(result.getLong("id"));
			company.setName(result.getString("name"));
			return company;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}