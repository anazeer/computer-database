package com.excilys.cdb.servlets.computer;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


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
	

    /**
     * Default constructor. 
     */
    public AddServlet() {
        // TODO Auto-generated constructor stub
    }
    
    @Override
    public void init() throws ServletException {
    	super.init();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		getServletContext().getRequestDispatcher("/resources/views/addComputer.jsp").forward(request, response);
		System.out.println(request.getParameter("computerName"));
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
