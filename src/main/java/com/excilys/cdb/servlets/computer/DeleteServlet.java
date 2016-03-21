package com.excilys.cdb.servlets.computer;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.excilys.cdb.service.ComputerService;
import com.excilys.cdb.service.ServiceFactory;

/**
 * Servlet implementation class DeleteServlet
 */
public class DeleteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	// ID from the POST form
	private final String selection = "selection";
	
	// ID for the dashboard delete success message
	private final String deleteSuccess = "deleted";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String[] ids = request.getParameter(selection).split(",");
		ComputerService computerService = ServiceFactory.getComputerService();
		for(String s : ids) {
			computerService.delete(Long.parseLong(s));
		}
		request.setAttribute(deleteSuccess, true);
		getServletContext().getRequestDispatcher("/computer").forward(request, response);
	}

}
