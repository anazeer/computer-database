package com.excilys.cdb.servlets.computer;


import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.excilys.cdb.exception.DateException;
import com.excilys.cdb.exception.NameException;
import com.excilys.cdb.service.CompanyService;
import com.excilys.cdb.service.ComputerService;
import com.excilys.cdb.service.dto.ComputerDTO;
import com.excilys.cdb.validation.Validator;


/*
 * TODO
 * Servlets : appelle les méthodes de la base de données et récupère par exemple une liste, qu'on va passer à la page JSP
 * JSP : la liste qu'il reçoit il va l'afficher. 
 * web.xml : il faut le modifier pour que la page correspondant au servlet s'affiche effectivement quand on entre la bonne URL
 * dashboard.html : il faut le transformer en jsp et c'est là qu'on fait le traitement avec l'objet JAVA
 * 
 */

/**
 * Servlet implementation class AddServlet
 */
public class AddServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private CompanyService companyService;
	private ComputerService computerService;

    /**
     * Default constructor. 
     */
    public AddServlet() {
        // TODO Auto-generated constructor stub
    }
    
    @Override
    public void init() throws ServletException {
    	super.init();
    	companyService = CompanyService.getInstance();
    	computerService = ComputerService.getInstance();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("companies", companyService.list());
		getServletContext().getRequestDispatcher("/WEB-INF/addComputer.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String name = request.getParameter("computerName");
		String companyId = request.getParameter("companyId");
		String introduced = request.getParameter("introduced");
		introduced = introduced.trim().isEmpty() ? null : introduced.trim();
		String discontinued = request.getParameter("discontinued");
		discontinued = discontinued.trim().isEmpty() ? null : discontinued.trim();
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
		request.setAttribute("vcomputerName", ename);
		request.setAttribute("vintroduced", eintro);
		request.setAttribute("vdiscontinued", ediscontinued);
		request.setAttribute("companies", companyService.list());
		if(good) {
			ComputerDTO dto = new ComputerDTO(name);
			dto.setIntroduced(introduced);
			dto.setDiscontinued(discontinued);
			dto.setCompanyId(Long.parseLong(companyId));
			try {
				computerService.create(dto);
				good = true;
			}
			catch(DateException e) {
				request.setAttribute("vdiscontinued", e.getMessage());
				good = false;
			}
			catch (SQLException e) {
				request.setAttribute("vdate", Validator.COMP_ERROR);
				good = false;
			}
		}
		if(good) {
			request.setAttribute("vsuccess", Validator.COMP_SUCCESS);
			getServletContext().getRequestDispatcher("/WEB-INF/addComputer.jsp").forward(request, response);
		}
		else {
			request.setAttribute("pname", name);
			request.setAttribute("pintro", introduced);
			request.setAttribute("pdiscontinued", discontinued);
			getServletContext().getRequestDispatcher("/WEB-INF/addComputer.jsp").forward(request, response);
		}
	}
}
