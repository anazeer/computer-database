package com.excilys.cdb.controller;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.excilys.cdb.dto.implementation.ComputerDto;
import com.excilys.cdb.mapper.implementation.ComputerMapper;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.pagination.implementation.ComputerPage;
import com.excilys.cdb.pagination.mapper.PageRequestMapper;
import com.excilys.cdb.pagination.util.PageRequest;
import com.excilys.cdb.service.implementation.CompanyService;
import com.excilys.cdb.service.implementation.ComputerService;

/**
 * Dashboard controller
 */
@Controller
public class ComputerController {

    // Services
	@Autowired
	private CompanyService companyService;
	@Autowired
	private ComputerService computerService;
	
	// Mapper
	@Autowired
	private ComputerMapper computerMapper;
	
    // ID for companies listing
    private static final String COMPANIES = "companies";

    // ID for the boolean for an adding/editing success
    private static final String SUCCESS = "success";
    
    // ID for boolean for an adding/editing failure
    private static final String FAILURE = "failure";
    
    // ID for the DTO object for the JSP
    private static final String DTO = "computerDTO";

	// ID for the page
	private static final String PAGE = "page";
	
	// ID from the POST form for deletion
	private static final String selection = "selection";
	
	// Id for the user login
	private static final String USER = "user";

	/**
	 * Default constructor. 
	 */
	private ComputerController() {
	}

	/**
	 * Dashboards GET (view computers)
	 * @param request the request object
	 * @return the name of the result JSP (result view)
	 */
	@RequestMapping(path = "computer", method = RequestMethod.GET)
	public ModelAndView dashboard(@RequestParam Map<String, String> request) {
		// Get the page request from the request
		PageRequest pageRequest = PageRequestMapper.get(request);

		// Get the computer page from the page request
		ComputerPage computerPage = computerService.getPage(pageRequest);
		
		// Construct the forward object
		ModelAndView mav = new ModelAndView("dashboard");
		mav.addObject(USER, getPrincipal());
		
		// Add the page attribute
		mav.addObject(PAGE, computerPage);

		// Forward to the dashboard with the new page
		return mav;
	}
	
	/**
	 * Dashboard POST (delete computers)
	 * @param request the request object
	 * @return the name of the result JSP (result view)
	 */
	@RequestMapping(path = {"delete"}, method = RequestMethod.POST)
	public String delete(@RequestParam Map<String, String> request) {
		// Retrieve the list of id's
		String[] ids = request.get(selection).split(",");
		// Delete all the computers referenced by id
		for (String s : ids) {
			computerService.delete(Long.parseLong(s));
		}
		// Return to the dashboard
		return "redirect:/computer";
	}
	
	/**
	 * Add GET
	 * @param model the model containing all the attributes
	 * @return the name of the result JSP (result view)
	 */
	@RequestMapping(path = "/addComputer", method = RequestMethod.GET)
	public String getAdd(Model model) {
		
		// Set the companies list for the JSP
		model.addAttribute(COMPANIES, companyService.list(null));
		model.addAttribute(DTO, new ComputerDto());
		
		// Forward to the add computer page with the new attributes
		return "addComputer";
	}

	/**
	 * Add POST
	 * @param model the model containing all the attributes
	 * @param dto the computer's DTO
	 * @param result the binding result (for errors)
	 * @return the name of the result JSP (result view)
	 */
	@RequestMapping(path = "/addComputer", method = RequestMethod.POST)
	public String postAdd(Model model, @Valid ComputerDto dto, BindingResult result) {
		model.addAttribute(COMPANIES, companyService.list(null));
		// If the validation have some errors, we print it
		if (result.hasErrors()) {
			model.addAttribute(FAILURE, true);
			return "addComputer";
		}
        // The validation went good, we can persist the computer
		Computer computer = computerMapper.getFromDTO(dto);
		computerService.create(computer);
		model.addAttribute(SUCCESS, true);
		return "addComputer";
	}
	
	/**
	 * Edit GET
	 * @param id the computer id
	 * @param model the model containing all the attributes
	 * @return the name of the result JSP (result view)
	 */
	@RequestMapping(path="/editComputer{id}", method = RequestMethod.GET)
	public String doGet(@RequestParam("id") String id, Model model) {
		// Check the URL id parameter
		Long longId;
		try {
			longId = Long.parseLong(id);
		} catch(NumberFormatException e) {
			// The ID is incorrect, the page doesn't exist
			return "error/404";
		}
		// Get the computer associated to the id parameter from the URL
		Computer computer = computerService.findById(longId);
		if (computer == null) {
			// The id is not valid so the edit page associated doesn't exist
			return "error/404";
		}
        // The id is correct, we retrieve the computer and send its DTO to the JSP
		ComputerDto dto = (ComputerDto) computerMapper.getFromModel(computer);
		model.addAttribute(DTO, dto);
		// The JSP also need the list of the companies
		model.addAttribute(COMPANIES, companyService.list(null));
		return "editComputer";
	}

	/**
	 * Edit POST
	 * @param model the model containing all the attributes
	 * @param dto the computer's DTO
	 * @param result the binding result (for errors)
	 * @return the name of the result JSP (result view)
	 */
	@RequestMapping(path="/editComputer", method = RequestMethod.POST)
	public String doPost(Model model, @Valid ComputerDto dto, BindingResult result) {
		model.addAttribute(DTO, dto);
		model.addAttribute(COMPANIES, companyService.list(null));
		// The validation have some errors,
		if (result.hasErrors()) {
			model.addAttribute(FAILURE, true);
			return "editComputer";
		}
		
        // The validation went good, we can update the computer
		Computer computer = computerMapper.getFromDTO(dto);
		computerService.update(computer);
		model.addAttribute(SUCCESS, true);
		return "editComputer";
	}
	
	private String getPrincipal() {
		String userName = null;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (principal instanceof UserDetails) {
			userName = ((UserDetails) principal).getUsername();
		} else {
			userName = principal.toString();
		}
		return userName;
	}
}