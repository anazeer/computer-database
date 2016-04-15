package com.excilys.cdb.service.implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.excilys.cdb.mapper.implementation.CompanyMapper;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.pagination.implementation.CompanyPage;
import com.excilys.cdb.pagination.util.PageRequest;
import com.excilys.cdb.persistence.dao.implementation.CompanyDAO;
import com.excilys.cdb.persistence.dao.implementation.ComputerDAO;
import com.excilys.cdb.service.util.Query;

/**
 * Service implementation for companies
 */
@Transactional
public class CompanyService implements com.excilys.cdb.service.IService<Company> {
	
	// Dao's references
	@Autowired
	private CompanyDAO companyDAO;
	@Autowired
	private ComputerDAO computerDAO;
	
	// Mappers
	@Autowired
	private CompanyMapper companyMapper;

	public CompanyService() {
		super();
	}
	
	@Override
	public CompanyPage getPage(PageRequest pageRequest) {
		Query query = pageRequest.getQuery();
		CompanyPage companyPage = new CompanyPage(companyMapper, pageRequest, count(query));
		query.setOffset(companyPage.getOffset());
		List<Company> companies = list(query);
		companyPage.setElements(companies);
		return companyPage;
	}

	@Override
	public List<Company> list(Query query) {
		return companyDAO.find(query);
	}

	@Override
	public int count(Query query) {
		return companyDAO.count(query);
	}

	@Override
	@Transactional(readOnly=false)
	public void delete(Long id) {
		computerDAO.deleteByCompanyId(id);
		companyDAO.delete(id);
	}
}