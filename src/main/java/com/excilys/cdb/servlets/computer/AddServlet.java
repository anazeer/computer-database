
package com.excilys.cdb.servlets.computer;


import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.excilys.cdb.exception.DateException;
import com.excilys.cdb.exception.IdException;
import com.excilys.cdb.exception.NameException;
import com.excilys.cdb.model.Computer;
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
    private final String companyIdParam = "companyId";

    // ID for the addComputer JSP for error messages
    private final String nameError= "vcomputerName";
    private final String introError = "vintroduced";
    private final String discontinuedError = "vdiscontinued";
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
		request.setAttribute("companies", companyService.list(null));
		getServletContext().getRequestDispatcher("/WEB-INF/addComputer.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // We first get the parameters from the POST form
		String name = request.getParameter(nameParam);
		String companyId = request.getParameter(companyIdParam);
		String introduced = request.getParameter(introParam);
		introduced = introduced.trim().isEmpty() ? null : introduced.trim();
		String discontinued = request.getParameter(discontinuedParam);
		discontinued = discontinued.trim().isEmpty() ? null : discontinued.trim();

        // Then we check the user inputs with the help of Validator class (name and dates)
		String ename = "";
		String eintro = "";
		String ediscontinued = "";
		boolean good = true;
		try {
			Validator.nameValidator(name);
		}
		catch(NameException e) {
			ename = e.getMessage();
			good = false;
		}
		try {
			Validator.dateValidator(introduced);
			Validator.dateValidator(discontinued);
		}
		catch(DateException e) {
			eintro = e.getMessage();
			good = false;
		}
		try {
			Validator.dateValidator(discontinued);
		}
		catch(DateException e) {
			ediscontinued = e.getMessage();
			good = false;
		}
		try {
			Validator.idValidator(companyId);
		}
		catch(IdException e) {
			request.setAttribute(globalError, Validator.ILLEGAL_ID);
			request.setAttribute(failure, true);
			good = false;
		}
        // If the inputs are not good, we set some error messages
		request.setAttribute(nameError, ename);
		request.setAttribute(introError, eintro);
		request.setAttribute(discontinuedError, ediscontinued);
		request.setAttribute(companyList, companyService.list(null));

        // If the validation went good, we try to persist the computer (some more checking are done in the lower layout)
		if(good) {
			ComputerDTO dto = new ComputerDTO
					.Builder(name)
					.introduced(introduced)
					.discontinued(discontinued)
					.companyId(Long.parseLong(companyId))
					.build();
			try {
                Computer computer = MapperFactory.getComputerMapper().getFromDTO(dto);
				computerService.create(computer);
			}
			catch(DateException e) {
				request.setAttribute(discontinuedError, e.getMessage());
				good = false;
			}
			catch (SQLException e) {
				request.setAttribute(globalError, Validator.COMP_ERROR);
				request.setAttribute(failure, true);
				good = false;
			}
		}

        // If the persistence is successful we print a success message on the page
		if(good) {
			request.setAttribute(success, true);
			request.setAttribute(addSuccess, Validator.COMP_SUCCESS);
			getServletContext().getRequestDispatcher("/WEB-INF/addComputer.jsp").forward(request, response);
		}
        // Otherwise something went wrong at the validation or the persistence, we keep users inputs and show error messages
		else {
			request.setAttribute(oldName, name);
			request.setAttribute(oldIntro, introduced);
			request.setAttribute(oldDiscontinued, discontinued);
			getServletContext().getRequestDispatcher("/WEB-INF/addComputer.jsp").forward(request, response);
		}
	}
}