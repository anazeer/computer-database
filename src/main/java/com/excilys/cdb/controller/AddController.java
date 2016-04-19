package com.excilys.cdb.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.excilys.cdb.dto.implementation.ComputerDTO;
import com.excilys.cdb.exception.DAOException;
import com.excilys.cdb.mapper.ComputerRequestMapper;
import com.excilys.cdb.mapper.implementation.ComputerMapper;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.service.implementation.CompanyService;
import com.excilys.cdb.service.implementation.ComputerService;
import com.excilys.cdb.validation.Validator;

/**
 * Servlet implementation class AddServlet
 */
@Controller
public class AddController {

    // Services
	@Autowired
	private CompanyService companyService;
	@Autowired
	private ComputerService computerService;
	
	// Mappers
	@Autowired
	private ComputerMapper computerMapper;

    // ID from the POST form
    private final String nameParam = "computerName";
    private final String introParam = "introduced";
    private final String discontinuedParam = "discontinued";

    // ID for the addComputer JSP for error messages
    private final String globalError = "vfailure";

    // ID for the addComputer JSP for companies listing
    private final String companyList = "companies";

    // ID for the addComputer JSP boolean for an adding success
    private final String success = "success";
    
    // ID for the addComputer JSP boolean for an adding failure
    private final String failure = "failure";

    // ID for the addComputer JSP for an adding success message
    private final String addSuccess = "vsuccess";

    // ID for the addComputer JSP to retrieves the previous user inputs after an adding failure
    private final String oldName = "pname";
    private final String oldIntro = "pintro";
    private final String oldDiscontinued = "pdiscontinued";

    /**
     * Default constructor. 
     */
    private AddController() {
    }
    
	@RequestMapping(path = "/addComputer", method = RequestMethod.GET)
	public ModelAndView getAdd(@RequestParam Map<String, String> request, ModelAndView mav) {
		
		// ModelAndView isn't null if we come from the POST method
		if (mav == null) {
			mav = new ModelAndView("addComputer");
		}
		
		// Get the user inputs
		String name = request.get(nameParam);
		String introduced = request.get(introParam);
		String discontinued = request.get(discontinuedParam);
		
		// Set the companies list for the JSP
		mav.addObject(companyList, companyService.list(null));
		
		// Set the fields with the user old inputs
		mav.addObject(oldName, name);
		mav.addObject(oldIntro, introduced);
		mav.addObject(oldDiscontinued, discontinued);
		
		// Forward to the add computer page with the new attributes
		return mav;
	}

	@RequestMapping(path = "/addComputer", method = RequestMethod.POST)
	public ModelAndView postAdd(@RequestParam Map<String, String> request) {
        // We first get the DTO from the request
		ComputerDTO dto = new ComputerRequestMapper().getFromRequest(request);
		
        // Then we check the user inputs with the help of Validator class
		Map<String, String> errors = Validator.validateComputer(dto);
		
		// Construct the forward object
		ModelAndView mav = new ModelAndView("addComputer");
		
        // If the inputs are not good, we set some error messages
		if (!errors.isEmpty()) {
			mav.addObject(globalError, Validator.CREATE_ERROR);
			mav.addAllObjects(errors);
			return getAdd(request, mav);
		}

        // If the validation went good, we try to persist the computer
		Computer computer = computerMapper.getFromDTO(dto);
		try {
			computerService.create(computer);
		} catch (DAOException e) {
			mav.addObject(globalError, e.getMessage());
			mav.addObject(failure, "true");
			return getAdd(request, mav);
		}

        // If the persistence is successful we print a success message on the page
		mav.addObject(success, true);
		mav.addObject(addSuccess, Validator.COMP_SUCCESS);
		
		// Forward to the add computer page
		return mav;
	}
}