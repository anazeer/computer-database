package com.excilys.cdb.servlets.computer;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.excilys.cdb.pagination.implementation.ComputerPage;
import com.excilys.cdb.pagination.mapper.PageRequestMapper;
import com.excilys.cdb.pagination.util.PageRequest;
import com.excilys.cdb.service.implementation.ComputerService;

/**
 * Servlet implementation class AddServlet
 */
public class ComputerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// Services
	@Autowired
	private ComputerService computerService;

	// ID for the page
	private static String PAGE_ID = "page";

	/**
	 * Default constructor. 
	 */
	public ComputerServlet() {

	}

	/**
	 * Initialize the spring context for the servlet
	 */
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		WebApplicationContext springContext = WebApplicationContextUtils.getRequiredWebApplicationContext(config.getServletContext());
        AutowireCapableBeanFactory beanFactory = springContext.getAutowireCapableBeanFactory();
        beanFactory.autowireBean(this);
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Get the page request from the request
		PageRequest pageRequest = PageRequestMapper.get(request);

		// Get the computer page from the page request
		ComputerPage computerPage = computerService.getPage(pageRequest);
		
		// Set the page attribute
		request.setAttribute(PAGE_ID, computerPage);

		// Forward to the dashboard
		getServletContext().getRequestDispatcher("/WEB-INF/dashboard.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}