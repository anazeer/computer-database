package com.excilys.cdb.servlets.computer;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.excilys.cdb.pagination.ComputerPage;
import com.excilys.cdb.service.ComputerService;
import com.excilys.cdb.service.Order;
import com.excilys.cdb.service.Query;

/**
 * Servlet implementation class AddServlet
 */
public class ComputerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private ComputerService computerService;
	private ComputerPage pagination;
	private static int limit;
	private static int count;

    // ID for the pagination object
    private final String pageId = "pagination";

    // If for the count of computers
    private final String countId = "countComputer";
    
    // Id for the search form
    private final String searchId = "search";
    
    // Id for the order parameter
    private final String orderId = "order";

    /**
     * Default constructor. 
     */
    public ComputerServlet() {
    }
    
    @Override
    public void init() throws ServletException {
    	super.init();
    	limit = 10;
    	computerService = ComputerService.getInstance();
        count = computerService.count(null);
        pagination = new ComputerPage(count, limit);
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// We first get the page information from the URL (method GET)
		String page = request.getParameter("page");
		String noElt = request.getParameter("limit");
		String search = request.getParameter(searchId);
		String order = request.getParameter(orderId);
		
        pagination = new ComputerPage(count, limit);
		
        // Update elements if some were added, deleted, or filtered
		if(search == null || search.trim().isEmpty()) {
			count = computerService.count(null);
			pagination.setFilter("");
		}
		else {
			count = computerService.count(new Query.Builder().filter(search).build());
			pagination.setFilter(search);
		}
		
		// Set the order
		if(order != null && !order.trim().isEmpty()) {
			Order ord;
			if(order.equals("name_asc")) {
				ord = Order.NAME_ASC;
			}
			else if(order.equals("name_dsc")) {
				ord = Order.NAME_DSC;
			}
			else if(order.equals("introduced_asc")) {
				ord = Order.INTRODUCED_ASC;
			}
			else if(order.equals("introduced_dsc")) {
				ord = Order.INTRODUCED_DSC;
			}
			else if(order.equals("discontinued_asc")) {
				ord = Order.DISCONTINUED_ASC;
			}
			else if(order.equals("discontinued_dsc")) {
				ord = Order.DISCONTINUED_DSC;
			}
			else if(order.equals("company_asc")) {
				ord = Order.COMPANY_ASC;
			}
			else if(order.equals("company_dsc")) {
				ord = Order.COMPANY_DSC;
			}
			else {
				ord = null;
			}
			pagination.setOrder(ord);
		}

		pagination.setTotalCount(count);
        
		int currentPage = pagination.getCurrentPage();
		int currentElements = pagination.getLimit();
		
		// We check the information and put them to default values if they're incorrect
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
		request.setAttribute(pageId, pagination);
		request.setAttribute(countId, count);
		request.setAttribute(searchId, search);
		request.setAttribute(orderId, order);
		
		getServletContext().getRequestDispatcher("/WEB-INF/dashboard.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
