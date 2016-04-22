package com.excilys.cdb.service;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.excilys.cdb.model.Computer;
import com.excilys.cdb.service.implementation.ComputerService;
import com.excilys.cdb.service.util.Query;

/**
 * ComputerService test class. We assume that the database contains more than 50 elements
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring-context.xml"})
public class ComputerServiceTest {

	@Autowired
	private ComputerService computerService;

	@Test
	public void list() {
		String filter = "apple";
		Query query = new Query.Builder().filter(filter).build();
		List<Computer> list = computerService.list(query);
		for (Computer c : list) {
			String companyName = c.getCompany() == null ? null : c.getCompany().getName();
			System.out.println(c + " aaa " + companyName);
			boolean contains =
					c.getName().toLowerCase().contains(filter) 
					|| (c.getIntroduced() != null && c.getIntroduced().toString().toLowerCase().contains(filter))
					|| (c.getDiscontinued() != null && c.getDiscontinued().toString().toLowerCase().contains(filter))
					|| (companyName != null && companyName.toLowerCase().contains(filter));
			assertTrue(contains);
		}
	}
}