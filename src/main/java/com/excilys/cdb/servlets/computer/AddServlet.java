package com.excilys.cdb.servlets.computer;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.excilys.cdb.service.CompanyService;
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
	private CompanyService computerService;
	private static final String dateError = "incorrect date";

    /**
     * Default constructor. 
     */
    public AddServlet() {
        // TODO Auto-generated constructor stub
    }
    
    @Override
    public void init() throws ServletException {
    	super.init();
    	companyService = new CompanyService();
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
		//String companyId = request.getParameter("companyId");
		String introduced = request.getParameter("introduced");
		introduced = introduced.trim().isEmpty() ? null : introduced;
		String discontinued = request.getParameter("discontinued");
		discontinued = discontinued.trim().isEmpty() ? null : discontinued;
		boolean vname = Validator.nameValidator(name);
		boolean vintro = Validator.dateValidator(introduced);
		boolean vdiscontinued = Validator.dateValidator(discontinued);
		String ename = vname ? "" : "name should not be empty";
		String eintro = vintro ? "" : dateError;
		String ediscontinued = vdiscontinued ? "" : dateError;
		request.setAttribute("vcomputerName", ename);
		request.setAttribute("vintroduced", eintro);
		request.setAttribute("vdiscontinued", ediscontinued);
		request.setAttribute("companies", companyService.list());
		if(!vname || !vintro || !vdiscontinued) {
			getServletContext().getRequestDispatcher("/WEB-INF/addComputer.jsp").forward(request, response);
		}
		else {
			getServletContext().getRequestDispatcher("/ComputerServlet").forward(request, response);
		}
	}

}
