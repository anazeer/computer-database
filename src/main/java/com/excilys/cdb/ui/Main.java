package com.excilys.cdb.ui;

import org.springframework.context.support.ClassPathXmlApplicationContext;


public class Main {

	public static void main(String[] args) {
		try (ClassPathXmlApplicationContext context = 
				new ClassPathXmlApplicationContext("spring-config.xml")) {
			Cli cli = context.getBean(Cli.class);
			cli.session();
		}
	}
}