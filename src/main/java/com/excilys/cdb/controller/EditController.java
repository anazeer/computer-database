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
 * Computer editing controller
 */
@Controller
public class EditController {
	
    // Services
	@Autowired
	private CompanyService companyService;
	@Autowired
	private ComputerService computerService;
	
	// Mappers
	@Autowired
	private ComputerMapper computerMapper;
	
    // ID for the editComputer JSP for error messages
    private final String globalError = "vglobal";

    // ID for the editComputer JSP for companies listing
    private final String companyList = "companies";

    // ID for the editComputer JSP boolean for an editing success
    private final String success = "success";
    
    // ID for the addComputer JSP boolean for an editing failure
    private final String failure = "failure";

    // ID for the editComputer JSP for an editing success message
    private final String editSuccess = "vsuccess";
	
	// ID for the editComputer JSP for the edited computer
	private final String compJSP = "computer";
	
	// ID for the URL parameter id
	private final String idParam = "id";

    /**
     * Default constructor. 
     */
    private EditController() {
    }
    
	@RequestMapping(path="/editComputer", method = RequestMethod.GET)
	public ModelAndView doGet(@RequestParam Map<String, String> request, ModelAndView mav) {
		// We first get the DTO associated to the id parameter from the URL
		ComputerDTO dto = new ComputerRequestMapper().getFromRequest(request);
		
		// Then we try to retrieve the computer
		Computer computer = computerService.getComputer(dto.getId());
		
		// ModelAndView isn't null if we come from post
		if (mav == null) {
			mav = new ModelAndView();
		}

		// The id is not valid so the edit page associated doesn't exist
		if (computer == null) {
			mav.setViewName("404");
			return mav;
		}
        // The id is correct, we retrieve the computer and send its DTO to the JSP
		else {
			mav.setViewName("editComputer");
            dto = (ComputerDTO) computerMapper.getFromModel(computer);
            mav.addObject(compJSP, dto);
            mav.addObject(companyList, companyService.list(null));
            mav.addObject(idParam, request.get(idParam));
            return mav;
		}
	}

	@RequestMapping(path="/editComputer", method = RequestMethod.POST)
	public ModelAndView doPost(@RequestParam(value = "id") Long id, @RequestParam Map<String, String> request) {
        // We first get the DTO from the request
		ComputerDTO dto = new ComputerRequestMapper().getFromRequest(request);
		
        // Then we check the user inputs with the help of Validator class
		Map<String, String> errors = Validator.validateComputer(dto);
		
		ModelAndView mav = new ModelAndView("editComputer");
		
        // If the inputs are not good, we set some error messages
		if (!errors.isEmpty()) {
			mav.addObject(globalError, Validator.CREATE_ERROR);
			for (Map.Entry<String, String> entry : errors.entrySet()) {
			    String errorName = entry.getKey();
			    String errorValue = entry.getValue();
			    mav.addObject(errorName, errorValue);
			    mav.addObject(failure, true);
			}
			return doGet(request, mav);
			//return "editComputer";
		}

        // If the validation went good, we try to edit the computer
		Computer computer = computerMapper.getFromDTO(dto);
		try {
			computerService.update(computer);
		} catch (DAOException e) {
			mav.addObject(globalError, e.getMessage());
			mav.addObject(failure, true);
			return doGet(request, mav);
		}

        // If the persistence is successful we print a success message on the page
		mav.addObject(success, true);
		mav.addObject(editSuccess, Validator.COMP_SUCCESS);
		return doGet(request, mav);
	}
}
