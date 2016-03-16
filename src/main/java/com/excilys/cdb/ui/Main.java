package com.excilys.cdb.ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import com.excilys.cdb.model.Computer;
import com.excilys.cdb.pagination.CompanyPagination;
import com.excilys.cdb.pagination.ComputerPagination;
import com.excilys.cdb.service.CompanyService;
import com.excilys.cdb.service.ComputerService;
import com.excilys.cdb.service.dto.ComputerDTO;

public class Main {
	
	// Services
	static ComputerService computerService = ComputerService.getInstance();
	static CompanyService companyService = CompanyService.getInstance();
	
	// List containing valid inputs for each menu
	static List<Integer> generalInstr = new ArrayList<Integer>();
	static List<Integer> companyInstr = new ArrayList<Integer>();
	static List<Integer> computerInstr = new ArrayList<Integer>();
	static List<Character> paginatorInstr = new ArrayList<Character>();
	
	// Number of options for each menu
	static final int countGeneralInstr = 3;
	static final int countCompanyInstr = 2;
	static final int countComputerInstr = 6;
	
	// Read inputs
	static Scanner scan;
	
	// Maximum elements per page
	static int entriesPerPage = 10;
	
	/**
	 * Show main menu
	 */
	static void showGeneralInstr() {
		System.out.println("\nComputer database management");
		System.out.println("Choose your database:");
		System.out.println("1. Company");
		System.out.println("2. Computer");
		System.out.println("3. Exit");
	}
	
	/**
	 * Show company menu
	 */
	static void showCompanyInstr() {
		System.out.println("\nCompany");
		System.out.println("Choose your operation:");
		System.out.println("1. List companies");
		System.out.println("2. Return to menu");
	}
	
	/**
	 * Show computer menu
	 */
	static void showComputerInstr() {
		System.out.println("\nComputer");
		System.out.println("Choose your operation:");
		System.out.println("1. List computers");
		System.out.println("2. Computer details");
		System.out.println("3. Create computer");
		System.out.println("4. Update computer");
		System.out.println("5. Delete computer");
		System.out.println("6. Return to menu");
	}
	
	/**
	 * Show pagination menu
	 */
	static void showPage(List<?> list) {
		for(Object o : list) {
			System.out.println(o);
		}
		System.out.print("\nNext page : n. ");
		System.out.print("Previous page : p. ");
		System.out.println("End : q.");
	}
	
	
	/**
	 * Read the option input depending on the current menu
	 * @param step the current menu number (from 0 to 2)
	 * @return the selected option
	 */
	static int readOption(int step) {
		int entry = -1;
		try {
			entry = Integer.parseInt(scan.nextLine());
			if(step == 0 && !generalInstr.contains(entry)) {
				throw new IllegalArgumentException();
			}
			else if(step == 1 && !companyInstr.contains(entry)) {
				throw new IllegalArgumentException();
			}
			else if(step == 2 && !computerInstr.contains(entry)) {
				throw new IllegalArgumentException();
			}
		}
		catch(IllegalArgumentException e) {
			System.err.println("Select a valid operation.");
			entry = -1;
		}
		return entry;
	}
	
	/**
	 * Read a valid ID input
	 * @return
	 */
	static Long readId() {
		Long id = null;
		while(id == null) {
			System.out.println("Enter the ID:");
			try {
				id = Long.parseLong(scan.nextLine());
			}
			catch(NumberFormatException e) {
				System.err.println("Select a valid operation.");
			}
		 }
		return id;
	}
	
	/**
	 * Read a valid name (not empty)
	 * @return
	 */
	static String readName() {
		String entry = null;
		while(entry == null) {
			System.out.println("Select the name:");
			entry = scan.nextLine().trim();
			if(entry.isEmpty()) {
				entry = null;
				System.err.println("Enter a valid name");
			}
		}
		return entry;
	}
	
	/**
	 * Read a Date input
	 * @return
	 */
	static Date readDate() {
		Date date = null;
		String entry = null;
		//String parse = "yyyy/mm/dd";
		String parse = "dd/mm/yyyy";
		while(date == null) {
			try {
				System.out.println("Select date " + parse + ": (If unknown, enter u)");
				entry = scan.next("((0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/((19|20)\\d\\d))|u");
				//entry = scan.next("(((19|20)\\d\\d)/(0?[1-9]|1[012])/(0?[1-9]|[12][0-9]|3[01]))|u");
			    date = new SimpleDateFormat(parse).parse(entry);
			}
			catch (ParseException e) {
				if("u".equals(entry)) {
					break;
				}
				else {
					e.printStackTrace();
				}
			}
			catch(InputMismatchException e) {
				date = null;
				System.err.println(("Enter a correct date " + parse + " or u to ignore it"));
			}
			finally {
				scan.nextLine(); // consume new line left-over
			}
		}
		return date;
	}
	
	/**
	 * Read a character input for pagination
	 * @return
	 */
	static char readPage() {
		Character c = null;
		while(c == null) {
			System.out.println("Enter an option:");
			try {
				c = scan.nextLine().charAt(0);
				if(!paginatorInstr.contains(c)) {
					c = null;
					throw new IllegalArgumentException();
				}
			}
			catch(IllegalArgumentException | StringIndexOutOfBoundsException e) {
				System.err.println("Select a valid option.");
			}
		}
		return c;
	}
	
	/**
	 * Initialize the list containing the valid options
	 */
	static void initLists() {
		for(int i = 1; i <= countGeneralInstr; i++) {
			generalInstr.add(i);
		}
		for(int i = 1; i <= countCompanyInstr; i++) {
			companyInstr.add(i);
		}
		for(int i = 1; i <= countComputerInstr; i++) {
			computerInstr.add(i);
		}
		paginatorInstr.add('n');
		paginatorInstr.add('p');
		paginatorInstr.add('q');
	}
	
	/**
	 * Construct a new computer in line command
	 * @return
	 */
	static ComputerDTO constructComputer() {
		String name = readName();
		
		Date introducedDate;
		Date discontinuedDate;
		do {
			introducedDate = readDate();
			discontinuedDate = readDate();
			if(introducedDate == null || discontinuedDate == null) {
				break;
			}
		}
		while(!introducedDate.before(discontinuedDate));
		
		Instant instant = null;
	    LocalDate introduced = null;
	    LocalDate discontinued = null;
		if(introducedDate != null) {
			instant = Instant.ofEpochMilli(introducedDate.getTime());
			introduced = LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate();
		}
		if(discontinuedDate != null) {
			instant = Instant.ofEpochMilli(discontinuedDate.getTime());
			discontinued = LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate();
		}
		
		System.out.println("Select company id");
		Long company_id = readId();
		
		ComputerDTO computer = new ComputerDTO(name);
		computer.setIntroduced(introduced.toString());
		computer.setDiscontinued(discontinued.toString());
		computer.setCompanyId(company_id);
		return computer;
	}
	
	/**
	 * The main UI
	 */
	static void session() {
		initLists();
		boolean exit = false;
		int step = 0;
		while(!exit) {
			int entry = -1;
			while(entry == -1) {
				switch(step) {
					case 0 : showGeneralInstr(); entry = readOption(step); break;
					case 1 : showCompanyInstr(); entry = readOption(step); break;
					case 2 : showComputerInstr(); entry = readOption(step); break;
				}
			}
			if(step == 0) {
				switch(entry) {
					case 1 : step = 1; break;
					case 2 : step = 2; break;
					case 3 : exit = true; break;
				}
			}
			else if(step == 1) {
				switch(entry) {
					case 1 : 
						CompanyPagination page = new CompanyPagination(companyService.countEntries(), entriesPerPage);
						System.out.println("---------------------");
						System.out.println("- List of companies -");
						System.out.println("---------------------");
						boolean end = false;
						while(!end) {
							showPage(page.getListFromOffset());
							switch(readPage()) {
								case 'n' : page.next(); break;
								case 'p' : page.previous(); break;
								case 'q' : end = true; break;
							}
						}
						break;
					case 2 : step = 0; break;
				}
			}
			else if(step == 2) {
				switch(entry) {
					case 1 :
						ComputerPagination page = new ComputerPagination(computerService.countEntries(), entriesPerPage);
						System.out.println("---------------------");
						System.out.println("- List of computers -");
						System.out.println("---------------------");
						boolean end = false;
						while(!end) {
							showPage(page.getListFromOffset());
							switch(readPage()) {
								case 'n' : page.next(); break;
								case 'p' : page.previous(); break;
								case 'q' : end = true; break;
							}
						}
						break;
					case 2 :
						Long id = readId();
						Computer computer = computerService.getComputer(id);
						if(computer == null) {
							System.out.println("No computer is referenced by id " + id);
						}
						else {
							System.out.println(computer.toDetailedString());
						}
						break;
					case 3 : ComputerDTO comp = constructComputer(); computerService.create(comp); break;
					case 4 : ComputerDTO updateComputer = constructComputer(); Long updateId = readId(); updateComputer.setId(updateId);computerService.update(updateComputer); break;
					case 5 : Long delId = readId(); computerService.delete(delId); break;
					case 6 : step = 0; break;
				}
			}
		}
	}
	
	public static void main(String[] args) {
		scan = new Scanner(System.in);
		session();
		scan.close();
	}

}
