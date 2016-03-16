package com.excilys.cdb.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class PageTag extends SimpleTagSupport {
	
	private int number;
	
	@Override
	public void doTag() throws JspException, IOException {
		super.doTag();
		if(number != 0) {
			
		}
	}
	
	public void setNumber(int number) {
		this.number = number;
	}

}
