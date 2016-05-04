package com.excilys.cdb.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.excilys.cdb.model.User;

@Controller
public class LoginController {
	
	@RequestMapping(value = {"/", "/login"}, method = RequestMethod.GET)
	public String loginPage(Model model) {
		model.addAttribute("user", new User());
	    return "login";
	}
	
	/**
	 * Logout the user
	 * @param request the server request
	 * @param response the server response
	 * @return a redirection to the dashboard (public)
	 */
	@RequestMapping(value="/logout", method = RequestMethod.GET)
	public String logout (HttpServletRequest request, HttpServletResponse response) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {    
			new SecurityContextLogoutHandler().logout(request, response, authentication);
			
		}
		return "redirect:/login";
	}
}