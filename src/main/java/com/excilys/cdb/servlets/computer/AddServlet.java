
package com.excilys.cdb.servlets.computer;


import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.excilys.cdb.exception.DAOException;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.mapper.ComputerRequestMapper;
import com.excilys.cdb.persistence.mapper.MapperFactory;
import com.excilys.cdb.service.CompanyService;
import com.excilys.cdb.service.ComputerService;
import com.excilys.cdb.service.ServiceFactory;
import com.excilys.cdb.service.dto.ComputerDTO;
import com.excilys.cdb.validation.Validator;

/**
 * Servlet implementation class AddServlet
 */
public class AddServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    // The services
	private CompanyService companyService;
	private ComputerService computerService;

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
    public AddServlet() {
    }
    
    @Override
    public void init() throws ServletException {
    	super.init();
    	companyService = ServiceFactory.getCompanyService();
    	computerService = ServiceFactory.getComputerService();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Get the user inputs
		String name = request.getParameter(nameParam);
		String introduced = request.getParameter(introParam);
		String discontinued = request.getParameter(discontinuedParam);
		
		// Set the companies list for the JSP
		request.setAttribute(companyList, companyService.list(null));
		
		// Set the fields with the user old inputs
		request.setAttribute(oldName, name);
		request.setAttribute(oldIntro, introduced);
		request.setAttribute(oldDiscontinued, discontinued);
		getServletContext().getRequestDispatcher("/WEB-INF/addComputer.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // We first get the DTO from the request
		ComputerDTO dto = new ComputerRequestMapper().getFromRequest(request);
		
        // Then we check the user inputs with the help of Validator class
		Map<String, String> errors = Validator.validateComputer(dto);
		
        // If the inputs are not good, we set some error messages
		if (!errors.isEmpty()) {
			request.setAttribute(globalError, Validator.CREATE_ERROR);
			for (Map.Entry<String, String> entry : errors.entrySet()) {
			    String errorName = entry.getKey();
			    String errorValue = entry.getValue();
			    request.setAttribute(errorName, errorValue);
			    request.setAttribute(failure, true);
			}
			doGet(request, response);
			return;
		}

        // If the validation went good, we try to persist the computer
		Computer computer = MapperFactory.getComputerMapper().getFromDTO(dto);
		try {
			computerService.create(computer);
		} catch (DAOException e) {
			request.setAttribute(globalError, e.getMessage());
			request.setAttribute(failure, true);
			doGet(request, response);
			return;
		}

        // If the persistence is successful we print a success message on the page
		request.setAttribute(success, true);
		request.setAttribute(addSuccess, Validator.COMP_SUCCESS);
		getServletContext().getRequestDispatcher("/WEB-INF/addComputer.jsp").forward(request, response);
	}
}