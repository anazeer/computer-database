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
 * Servlet implementation class EditServlet
 */
public class EditServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    // The services
	private CompanyService companyService;
	private ComputerService computerService;
	
    // ID from the POST form
    private final String nameParam = "computerName";
    private final String introParam = "introduced";
    private final String discontinuedParam = "discontinued";
    private final String companyIdParam = "companyId";
	
    // ID for the editComputer JSP for error messages
    private final String nameError= "vcomputerName";
    private final String introError = "vintroduced";
    private final String discontinuedError = "vdiscontinued";
    private final String globalError = "vglobal";

    // ID for the editComputer JSP for companies listing
    private final String companyList = "companies";

    // ID for the editComputer JSP boolean for an editing success
    private final String success = "success";

    // ID for the editComputer JSP for an editing success message
    private final String editSuccess = "vsuccess";

    // ID for the editComputer JSP to retrieves the previous user inputs after an editing failure
    private final String oldName = "pname";
    private final String oldIntro = "pintro";
    private final String oldDiscontinued = "pdiscontinued";
    
	// ID for the URL parameters
	private final String idParam = "id";
	
	// ID for the editComputer JSP for the edited computer
	private final String compJSP = "computer";
	
	// The computer we want to edit
	private Computer computer;

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
		// We first get id parameter from the URL
		String computerId = request.getParameter(idParam);
		computerId = computerId.trim().isEmpty() ? null : computerId.trim();
		// We check that the parameter is valid
		boolean good = true;
		try {
			Validator.computerIdValidator(computerId);
		}
		catch(IdException e) {
			good = false;
		}
		
		// The id is not valid so the edit page associated doesn't exist
		if(!good) {
			getServletContext().getRequestDispatcher("/WEB-INF/404.jsp").forward(request, response);
		}
		// The id is correct, we retrieve the computer and send its DTO to the JSP
		else {
			computer = computerService.getComputer(Long.parseLong(computerId));
			ComputerDTO dto = (ComputerDTO) MapperFactory.getComputerMapper().getFromModel(computer);
			request.setAttribute(compJSP, dto);
			request.setAttribute(companyList, companyService.listAll());
			getServletContext().getRequestDispatcher("/WEB-INF/editComputer.jsp").forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // We first get the parameters from the POST form
		String name = request.getParameter(nameParam);
		String id = request.getParameter(idParam);
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
			Validator.companyIdValidator(companyId);
		}
		catch(IdException e) {
			request.setAttribute(globalError, Validator.ILLEGAL_ID);
			good = false;
		}
		
		computer = computerService.getComputer(Long.parseLong(id));
		ComputerDTO dto = (ComputerDTO) MapperFactory.getComputerMapper().getFromModel(computer);
		request.setAttribute(compJSP, dto);
		
        // If the inputs are not good, we set some error messages
		request.setAttribute(nameError, ename);
		request.setAttribute(introError, eintro);
		request.setAttribute(discontinuedError, ediscontinued);
		request.setAttribute(companyList, companyService.listAll());

        // If the validation went good, we try to edit the computer (some more checking are done in the lower layout)
		if(good) {
			dto = new ComputerDTO
				.ComputerDTOBuilder(name)
				.id(computer.getId())
				.introduced(introduced)
				.discontinued(discontinued)
				.companyId(Long.parseLong(companyId))
				.build();
			try {
                Computer updatedComputer = MapperFactory.getComputerMapper().getFromDTO(dto);
				computerService.update(updatedComputer);
			}
			catch(DateException e) {
				request.setAttribute(discontinuedError, e.getMessage());
				good = false;
			}
			catch (SQLException e) {
				request.setAttribute(globalError, Validator.COMP_ERROR);
				good = false;
			}
		}
				
        // If the persistence is successful we print a success message on the page
		if(good) {
			request.setAttribute(success, true);
			request.setAttribute(editSuccess, Validator.COMP_SUCCESS);
			getServletContext().getRequestDispatcher("/WEB-INF/editComputer.jsp").forward(request, response);
		}
        // Otherwise something went wrong at the validation or the persistence, we keep users inputs and show error messages
		else {
			request.setAttribute(oldName, name);
			request.setAttribute(oldIntro, introduced);
			request.setAttribute(oldDiscontinued, discontinued);
			getServletContext().getRequestDispatcher("/WEB-INF/editComputer.jsp").forward(request, response);
		}
	}
	

}
