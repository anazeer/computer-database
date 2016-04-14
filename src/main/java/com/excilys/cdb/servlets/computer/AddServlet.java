package com.excilys.cdb.servlets.computer;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

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
public class AddServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

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
    public AddServlet() {
    }
    
	/**
	 * Initialize the spring context for the servlet
	 */
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		WebApplicationContext springContext = WebApplicationContextUtils.getRequiredWebApplicationContext(config.getServletContext());
        AutowireCapableBeanFactory beanFactory = springContext.getAutowireCapableBeanFactory();
        beanFactory.autowireBean(this);
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
		Computer computer = computerMapper.getFromDTO(dto);
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