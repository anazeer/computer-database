package com.excilys.cdb.servlets.computer;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.excilys.cdb.pagination.ComputerPagination;
import com.excilys.cdb.service.ComputerService;

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
public class ListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private ComputerService computerService;
	private ComputerPagination pagination;
	private static int entriesPerPage;
	private static int count;

    /**
     * Default constructor. 
     */
    public ListServlet() {
        // TODO Auto-generated constructor stub
    }
    
    @Override
    public void init() throws ServletException {
    	super.init();
    	entriesPerPage = 10;
    	computerService = new ComputerService();
    	count = computerService.countEntries();
    	pagination = new ComputerPagination(count, entriesPerPage);
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		if(request.getParameter("page") == null) {
			pagination.setCurrentPage(1);
		}
		else {
			try {
				pagination.setCurrentPage(Integer.parseInt(request.getParameter("page")));
			}
			catch(NumberFormatException e) {
			}
		}
		request.setAttribute("pagination", pagination);
		request.setAttribute("countComputer", count);
		getServletContext().getRequestDispatcher("/resources/views/dashboard.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
