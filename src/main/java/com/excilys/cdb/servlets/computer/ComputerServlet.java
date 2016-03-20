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
public class ComputerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private ComputerService computerService;
	private ComputerPagination pagination;
	private static int limit;
	private static int count;

    /**
     * Default constructor. 
     */
    public ComputerServlet() {
        // TODO Auto-generated constructor stub
    }
    
    @Override
    public void init() throws ServletException {
    	super.init();
    	limit = 10;
    	computerService = ComputerService.getInstance();
    	count = computerService.count();
    	pagination = new ComputerPagination(count, limit);
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String page = request.getParameter("page");
		String noElt = request.getParameter("limit");
		int currentPage = pagination.getCurrentPage();
		int currentElements = pagination.getLimit();
		if(page == null) {
			pagination.setCurrentPage(currentPage);
		}
		else {
			try {
				int i = Integer.parseInt(page);
				if(i > pagination.getLastPage()) {
					pagination.setCurrentPage(currentPage);
				}
				else if(i < 0) {
					pagination.setCurrentPage(1);
				}
				else {
					pagination.setCurrentPage(i);
				}
			}
			catch(NumberFormatException e) {
				pagination.setCurrentPage(currentPage);
			}
		}
		if(noElt == null) {
			pagination.setLimit(currentElements);
		}
		else {
			try {
				int i = Integer.parseInt(noElt);
				if(i == 10 || i == 20 || i == 50)
					pagination.setLimit(i);
				else
					pagination.setLimit(currentElements);
			}
			catch(NumberFormatException e) {
				pagination.setLimit(currentElements);
			}
		}
		request.setAttribute("pagination", pagination);
		request.setAttribute("countComputer", count);
		getServletContext().getRequestDispatcher("/WEB-INF/dashboard.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println(request.getParameter("selection"));
		doGet(request, response);
	}

}
