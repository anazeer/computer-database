package com.excilys.cdb.ui;

import java.sql.SQLException;
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

import com.excilys.cdb.exception.DateException;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.pagination.CompanyPage;
import com.excilys.cdb.pagination.ComputerPage;
import com.excilys.cdb.pagination.Page;
import com.excilys.cdb.persistence.mapper.MapperFactory;
import com.excilys.cdb.service.CompanyService;
import com.excilys.cdb.service.ComputerService;
import com.excilys.cdb.service.dto.ComputerDTO;

public class Main {
	
	// Services
	private static ComputerService computerService = ComputerService.getInstance();
	private static CompanyService companyService = CompanyService.getInstance();
	
	// List containing valid inputs for each menu
	private static List<Integer> generalInstr = new ArrayList<>();
	private static List<Integer> companyInstr = new ArrayList<>();
	private static List<Integer> computerInstr = new ArrayList<>();
	private static List<Character> pageInstr = new ArrayList<>();
	
	// Number of options for each menu
	private static final int countGeneralInstr = 3;
	private static final int countCompanyInstr = 3;
	private static final int countComputerInstr = 6;
	
	// Read inputs
	private static Scanner scan;
	
	/**
	 * Show main menu
	 */
	private static void showGeneralInstr() {
		System.out.println("\nComputer database management");
		System.out.println("Choose your database:");
		System.out.println("1. Company");
		System.out.println("2. Computer");
		System.out.println("3. Exit");
	}
	
	/**
	 * Show company menu
	 */
	private static void showCompanyInstr() {
		System.out.println("\nCompany");
		System.out.println("Choose your operation:");
		System.out.println("1. List companies");
		System.out.println("2. Delete company");
		System.out.println("3. Return to menu");
	}
	
	/**
	 * Show computer menu
	 */
	private static void showComputerInstr() {
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
	private static void showPage(List<?> list) {
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
	private static int readOption(int step) {
		int entry;
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
	 * @return the input ID
	 */
	private static Long readId() {
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
	 * @return the input name
	 */
	private static String readName() {
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
	 * @return the input date
	 */
	private static Date readDate() {
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
	 * @return the input char
	 */
	private static char readPage() {
		Character c = null;
		while(c == null) {
			System.out.println("Enter an option:");
			try {
				c = scan.nextLine().charAt(0);
				if(!pageInstr.contains(c)) {
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

    private static void navigatePage(Page<?> page) {
        boolean end = false;
        while(!end) {
            showPage(page.getElements());
            switch(readPage()) {
                case 'n' : page.next(); break;
                case 'p' : page.previous(); break;
                case 'q' : end = true; break;
            }
        }
    }
	
	/**
	 * Initialize the list containing the valid options
	 */
	private static void initLists() {
		for(int i = 1; i <= countGeneralInstr; i++) {
			generalInstr.add(i);
		}
		for(int i = 1; i <= countCompanyInstr; i++) {
			companyInstr.add(i);
		}
		for(int i = 1; i <= countComputerInstr; i++) {
			computerInstr.add(i);
		}
		pageInstr.add('n');
		pageInstr.add('p');
		pageInstr.add('q');
	}
	
	/**
	 * Construct a new computer in line command
	 * @return the created computer
	 */
	private static ComputerDTO constructComputer() {
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
		
		Instant instant;
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
		if(introduced != null) {
            computer.setIntroduced(introduced.toString());
        }
        if(discontinued != null) {
            computer.setDiscontinued(discontinued.toString());
        }
		computer.setCompanyId(company_id);
		return computer;
	}
	
	/**
	 * The main UI
	 */
	private static void session() {
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
						CompanyPage page = new CompanyPage(companyService.count(null), 10);
						System.out.println("---------------------");
						System.out.println("- List of companies -");
						System.out.println("---------------------");
                        navigatePage(page);
						break;
					case 2 : 
						Long id = readId();
						boolean bool = companyService.delete(id);
						if(bool) {
							System.out.println("Company successfully deleted");
						}
					case 3 : step = 0;
                        break;
				}
			}
			else {
                Computer computer;
                ComputerDTO computerDTO;
				switch(entry) {
					case 1 :
						ComputerPage page = new ComputerPage(computerService.count(null), 10);
						System.out.println("---------------------");
						System.out.println("- List of computers -");
						System.out.println("---------------------");
                        navigatePage(page);
						break;
					case 2 :
						Long id = readId();
						computer = computerService.getComputer(id);
						if(computer == null) {
							System.out.println("No computer is referenced by id " + id);
						}
						else {
							System.out.println(computer.toDetailedString());
						}
						break;
					case 3 :
                        computerDTO = constructComputer();
                        try {
                            computer = MapperFactory.getComputerMapper().getFromDTO(computerDTO);
                            computerService.create(computer);
                            break;
                        }
                        catch(SQLException e) {
                            System.err.println("Error on computer creation");
                            break;
                        }
                        catch (DateException e) {
                            System.err.println("The introduced date should be earlier than the discontinued date");
                            e.printStackTrace();
                        }
					case 4 :
                        computerDTO = constructComputer();
                        Long updateId = readId();
                        computerDTO.setId(updateId);
                        try {
	                        computer = MapperFactory.getComputerMapper().getFromDTO(computerDTO);
	                        computerService.update(computer);                     
	                        break;
                        }
                        catch(SQLException e) {
                            System.err.println("Error on computer creation");
                            break;
                        }
                        catch (DateException e) {
                            System.err.println("The introduced date should be earlier than the discontinued date");
                            e.printStackTrace();
                        }
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
