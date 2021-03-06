package com.excilys.cdb.service.implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.excilys.cdb.dao.implementation.CompanyDao;
import com.excilys.cdb.dao.implementation.ComputerDao;
import com.excilys.cdb.mapper.implementation.CompanyMapper;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.pagination.implementation.CompanyPage;
import com.excilys.cdb.pagination.util.PageRequest;
import com.excilys.cdb.util.Constraint;

/**
 * Service implementation for companies
 */
@Transactional
@Service
public class CompanyService implements com.excilys.cdb.service.IService<Company> {
	
	// Dao's references
	@Autowired
	private CompanyDao companyDAO;
	@Autowired
	private ComputerDao computerDAO;
	
	// Mappers
	@Autowired
	private CompanyMapper companyMapper;

	public CompanyService() {
	}
	
	@Cacheable(value = "company")
	@Override
	public Company findById(Long id) {
		return companyDAO.findById(id);
	}
	
	@Override
	public CompanyPage getPage(PageRequest pageRequest) {
		Constraint query = pageRequest.getQuery();
		CompanyPage companyPage = new CompanyPage(companyMapper, pageRequest, count(query));
		query.setOffset(companyPage.getOffset());
		List<Company> companies = list(query);
		companyPage.setElements(companies);
		return companyPage;
	}

	@Cacheable(value = "company")
	@Override
	public List<Company> list(Constraint constraint) {
		return companyDAO.find(constraint);
	}

	@Override
	public int count(Constraint constraint) {
		return companyDAO.count(constraint);
	}

	@Override
	@Transactional(readOnly=false)
	public boolean delete(Long id) {
		computerDAO.deleteByCompanyId(id);
		return companyDAO.delete(id);
	}
}