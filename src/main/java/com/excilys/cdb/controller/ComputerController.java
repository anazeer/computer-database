package com.excilys.cdb.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.excilys.cdb.exception.DAOException;
import com.excilys.cdb.pagination.implementation.ComputerPage;
import com.excilys.cdb.pagination.mapper.PageRequestMapper;
import com.excilys.cdb.pagination.util.PageRequest;
import com.excilys.cdb.service.implementation.ComputerService;

/**
 * Dashboard controller
 */
@Controller
public class ComputerController {
	
	// Logger
	private Logger log = LoggerFactory.getLogger(getClass());

	// Service
	@Autowired
	private ComputerService computerService;

	// ID for the page
	private static String PAGE_ID = "page";
	// ID from the POST form for deletion
	private static final String selection = "selection";

	/**
	 * Default constructor. 
	 */
	private ComputerController() {
	}

	@RequestMapping(path = {"/", "computer"}, method = RequestMethod.GET)
	public ModelAndView dashboard(@RequestParam Map<String, String> request) {
		// Get the page request from the request
		PageRequest pageRequest = PageRequestMapper.get(request);

		// Get the computer page from the page request
		ComputerPage computerPage = computerService.getPage(pageRequest);
		
		// Construct the forward object
		ModelAndView mav = new ModelAndView("dashboard");
		
		// Add the page attribute
		mav.addObject(PAGE_ID, computerPage);

		// Forward to the dashboard with the new page
		return mav;
	}
	
	@RequestMapping(path = {"/", "computer"}, method = RequestMethod.POST)
	public String delete(@RequestParam Map<String, String> request) {
		// Retrieve the list of id's
		String[] ids = request.get(selection).split(",");
		// Delete all the computers referenced by id
		for (String s : ids) {
			try {
			computerService.delete(Long.parseLong(s));
			} catch(DAOException e) {
				log.error(e.getMessage());
			}
		}
		// Return to the dashboard
		return "redirect:/computer";
	}
}