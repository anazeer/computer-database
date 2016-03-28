package com.excilys.cdb.servlets.computer;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.excilys.cdb.pagination.AbstractPage;
import com.excilys.cdb.pagination.PageRequestMapper;

/**
 * Servlet implementation class AddServlet
 */
public class ComputerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    // ID for the page
    private static String PAGE_ID = "page";

    /**
     * Default constructor. 
     */
    public ComputerServlet() {
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Get the page from the request
		AbstractPage resultPage = PageRequestMapper.get(request);

		// Set the page attribute
		request.setAttribute(PAGE_ID, resultPage);

		// Forward to the dashboard
		getServletContext().getRequestDispatcher("/WEB-INF/dashboard.jsp").forward(request, response);
	}
}