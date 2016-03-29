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
 * Servlet implementation class EditServlet
 */
public class EditServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    // The services
	private CompanyService companyService;
	private ComputerService computerService;
	
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

    /**
     * Default constructor. 
     */
    public EditServlet() {
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
		// We first get the DTO associated to the id parameter from the URL
		ComputerDTO dto = new ComputerRequestMapper().getFromRequest(request);
		
		// Then we try to retrieve the computer
		Computer computer = computerService.getComputer(dto.getId());

		// The id is not valid so the edit page associated doesn't exist
		if (computer == null) {
			getServletContext().getRequestDispatcher("/WEB-INF/404.jsp").forward(request, response);
		}
        // The id is correct, we retrieve the computer and send its DTO to the JSP
		else {
            dto = (ComputerDTO) MapperFactory.getComputerMapper().getFromModel(computer);
            request.setAttribute(compJSP, dto);
            request.setAttribute(companyList, companyService.list(null));
            getServletContext().getRequestDispatcher("/WEB-INF/editComputer.jsp").forward(request, response);
		}
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

        // If the validation went good, we try to edit the computer
		Computer computer = MapperFactory.getComputerMapper().getFromDTO(dto);
		try {
			computerService.update(computer);
		} catch (DAOException e) {
			request.setAttribute(globalError, e.getMessage());
			request.setAttribute(failure, true);
			doGet(request, response);
			return;
		}

        // If the persistence is successful we print a success message on the page
		request.setAttribute(success, true);
		request.setAttribute(editSuccess, Validator.COMP_SUCCESS);
		getServletContext().getRequestDispatcher("/WEB-INF/addComputer.jsp").forward(request, response);
	}
}
