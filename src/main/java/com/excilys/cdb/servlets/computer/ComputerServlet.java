package com.excilys.cdb.servlets.computer;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.excilys.cdb.pagination.ComputerPagination;
import com.excilys.cdb.service.ComputerService;

/**
 * Servlet implementation class AddServlet
 */
public class ComputerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private ComputerService computerService;
	private ComputerPagination pagination;
	private static int limit;
	private static int count;
	
	// ID for the delete success message
	private final String deleteMsg = "deleted";
	
	// The delete success message
	private final String deleteMsgText = "Computers successfully deleted !";
	

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
		// We first get the page informations from the URL (method GET)
		String page = request.getParameter("page");
		String noElt = request.getParameter("limit");
		int currentPage = pagination.getCurrentPage();
		int currentElements = pagination.getLimit();
		
		// We check the informations and put them to default values if they're incorrect
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
		
		// We set the outputs in attributes for the JSP
		request.setAttribute("pagination", pagination);
		request.setAttribute("countComputer", count);
		
		// If we come from the delete servlet, we print the delete successful message
		request.setAttribute(deleteMsg, deleteMsgText);
		
		getServletContext().getRequestDispatcher("/WEB-INF/dashboard.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
