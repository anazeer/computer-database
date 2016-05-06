package com.excilys.cdb.ui;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;

import com.excilys.cdb.dto.implementation.ComputerDTO;
import com.excilys.cdb.mapper.implementation.ComputerMapper;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.User;
import com.excilys.cdb.model.UserRole;
import com.excilys.cdb.pagination.AbstractPage;
import com.excilys.cdb.pagination.util.PageRequest;
import com.excilys.cdb.service.IService;
import com.excilys.cdb.service.implementation.CompanyService;
import com.excilys.cdb.service.implementation.ComputerService;
import com.excilys.cdb.service.implementation.UserService;
import com.excilys.cdb.util.Role;

@Controller
public class Cli {

	// Services
	@Autowired
	private CompanyService companyService;
	@Autowired
	private ComputerService computerService;
	@Autowired
	private UserService userService;

	// Mappers
	@Autowired
	private ComputerMapper computerMapper;

	// Properties messages
	@Autowired
	private MessageSource messageSource;

	// List containing valid inputs for each menu
	private List<Integer> generalInstr = new ArrayList<>();
	private List<Integer> companyInstr = new ArrayList<>();
	private List<Integer> computerInstr = new ArrayList<>();
	private List<Integer> userInstr = new ArrayList<>();
	private List<Integer> roleInstr = new ArrayList<>();
	private List<Character> pageInstr = new ArrayList<>();

	// Number of options for each menu
	private final int countGeneralInstr = 4;
	private final int countCompanyInstr = 3;
	private final int countComputerInstr = 6;
	private final int countUserInstr = 5;
	private final int countRoleInstr = 3;

	// Read inputs
	private Scanner scan;
	
	// User roles map
	private Map<Integer, UserRole> userRoles = new HashMap<>();
	
	/**
	 * Initialize the roles map
	 */
	{
		UserRole roleAdmin = new UserRole();
		roleAdmin.setRole(Role.ADMIN);
		UserRole roleUser = new UserRole();
		roleUser.setRole(Role.USER);
		userRoles.put(1, roleAdmin);
		userRoles.put(2, roleUser);
	}

	/**
	 * Show main menu
	 */
	private void showGeneralInstr() {
		System.out.println("\nComputer database management");
		System.out.println("Choose your database:");
		System.out.println("1. Company");
		System.out.println("2. Computer");
		System.out.println("3. User");
		System.out.println("4. Exit");
	}

	/**
	 * Show company menu
	 */
	private void showCompanyInstr() {
		System.out.println("\nCompany");
		System.out.println("Choose your operation:");
		System.out.println("1. List companies");
		System.out.println("2. Delete company");
		System.out.println("3. Return to menu");
	}

	/**
	 * Show computer menu
	 */
	private void showComputerInstr() {
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
	 * Show user menu
	 */
	private void showUserInstr() {
		System.out.println("\nUser");
		System.out.println("Choose your operation:");
		System.out.println("1. List users");
		System.out.println("2. User details");
		System.out.println("3. Create user");
		System.out.println("4. Delete user");
		System.out.println("5. Return to menu");
	}
	
	/**
	 * Show available roles
	 */
	private void showRoleInstr() {
		System.out.println("\nRole");
		System.out.println("Choose the role:");
		System.out.println("1. Admin");
		System.out.println("2. User");
		System.out.println("End : 0");
	}

	/**
	 * Show pagination menu
	 */
	private void showPage(List<?> list) {
		for (Object o : list) {
			System.out.println(o);
		}
		System.out.print("\nNext page : n. ");
		System.out.print("Previous page : p. ");
		System.out.println("End : q.");
	}

	/**
	 * Initialize the list containing the valid options
	 */
	private void initLists() {
		for (int i = 1; i <= countGeneralInstr; i++) {
			generalInstr.add(i);
		}
		for (int i = 1; i <= countCompanyInstr; i++) {
			companyInstr.add(i);
		}
		for (int i = 1; i <= countComputerInstr; i++) {
			computerInstr.add(i);
		}
		for (int i = 1; i <= countUserInstr; i++) {
			userInstr.add(i);
		}
		for (int i = 1; i <= countRoleInstr; i++) {
			roleInstr.add(i);
		}
		pageInstr.add('n');
		pageInstr.add('p');
		pageInstr.add('q');
	}


	/**
	 * Read the option input depending on the current menu
	 * @param step the current menu number (from 0 to 2)
	 * @return the selected option
	 */
	private int readOption(int step) {
		int entry;
		try {
			entry = Integer.parseInt(scan.nextLine());
			if (step == 0 && !generalInstr.contains(entry)) {
				throw new IllegalArgumentException();
			} else if (step == 1 && !companyInstr.contains(entry)) {
				throw new IllegalArgumentException();
			} else if (step == 2 && !computerInstr.contains(entry)) {
				throw new IllegalArgumentException();
			} else if (step == 3 && ! userInstr.contains(entry)) {
				throw new IllegalArgumentException();
			}
		} catch (IllegalArgumentException e) {
			System.err.println("Select a valid operation.");
			entry = -1;
		}
		return entry;
	}

	/**
	 * Read a valid ID input
	 * @return the input ID
	 */
	private Long readId() {
		Long id = null;
		while (id == null) {
			System.out.println("Enter the ID:");
			try {
				id = Long.parseLong(scan.nextLine());
			} catch(NumberFormatException e) {
				System.err.println("Select a valid operation.");
			}
		}
		return id;
	}

	/**
	 * Read a non empty String
	 * @param text the instruction text for the user
	 * @return the correct input String
	 */
	private String readString(String text) {
		String entry = null;
		while (entry == null) {
			System.out.println(text);
			entry = scan.nextLine().trim();
			if (entry.isEmpty()) {
				entry = null;
				System.err.println("The string should not be empty");
			}
		}
		return entry;
	}

	/**
	 * Read a Date input
	 * @return the input date
	 */
	private LocalDate readDate() {
		LocalDate date = null;
		String entry = null;
		String pattern = messageSource.getMessage("util.date.format", null, LocaleContextHolder.getLocale());
		while (date == null) {
			try {
				System.out.println("Select date " + pattern + ": (If unknown, enter u)");
				entry = scan.next("((0?[1-9]|[12][0-9]|3[01])-(0?[1-9]|1[012])-((19|20)\\d\\d))|u");
				//entry = scan.next("(((19|20)\\d\\d)/(0?[1-9]|1[012])/(0?[1-9]|[12][0-9]|3[01]))|u");
				date = LocalDate.parse(entry, DateTimeFormatter.ofPattern(pattern));
			} catch (DateTimeParseException e) {
				if ("u".equals(entry)) {
					break;
				}
				else {
					e.printStackTrace();
				}
			}
			catch (InputMismatchException e) {
				date = null;
				System.err.println(("Enter a correct date " + pattern + " or u to ignore it"));
			} finally {
				scan.nextLine(); // consume new line left-over
			}
		}
		return date;
	}

	/**
	 * Read a character input for pagination
	 * @return the input char
	 */
	private char readPage() {
		Character c = null;
		while (c == null) {
			System.out.println("Enter an option:");
			try {
				c = scan.nextLine().charAt(0);
				if (!pageInstr.contains(c)) {
					c = null;
					throw new IllegalArgumentException();
				}
			} catch(IllegalArgumentException | StringIndexOutOfBoundsException e) {
				System.err.println("Select a valid option.");
			}
		}
		return c;
	}
	
	private Set<UserRole> readRole() {
		boolean end = false;
		Set<UserRole> roles = new HashSet<>();
		while (!end) {
			showRoleInstr();
			try {
				int option = Integer.parseInt(scan.nextLine());
				if (option == 0) {
					end = true;
				} else if (!roleInstr.contains(option)) {
					throw new IllegalArgumentException();
				} else {
					roles.add(userRoles.get(option));
				}
			} catch(IllegalArgumentException e) {
				System.err.println("Select a valid operation.");
			}
		}
		return roles;
	}

	/**
	 * Navigate through the object pages
	 * @param service the service of the paginated object
	 */
	private void navigatePage(IService<?> service) {
		int currentPage = 1;
		boolean end = false;
		while (!end) {
			PageRequest pageRequest = new PageRequest(null, currentPage);
			AbstractPage<?> page = service.getPage(pageRequest);
			showPage(page.getElements());
			switch (readPage()) {
			case 'n' : page.next(); break;
			case 'p' : page.previous(); break;
			case 'q' : end = true; break;
			}
			currentPage = page.getCurrentPage();
		}
	}

	/**
	 * Construct a new computer in line command
	 * @return the created computer
	 */
	private ComputerDTO constructComputer() {
		String name = readString("Choose the name:");

		LocalDate introduced;
		LocalDate discontinued;
		do {
			introduced = readDate();
			discontinued = readDate();
			if (introduced == null || discontinued == null) {
				break;
			}
		}
		while (!introduced.isBefore(discontinued));

		System.out.println("Select company id");
		Long company_id = readId();
		ComputerDTO computer = new ComputerDTO(name);
		String pattern = messageSource.getMessage("util.date.format", null, LocaleContextHolder.getLocale());
		DateTimeFormatter format = DateTimeFormatter.ofPattern(pattern);
		if (introduced != null) {
			computer.setIntroduced(introduced.format(format));
		}
		if (discontinued != null) {
			computer.setDiscontinued(discontinued.format(format));
		}
		computer.setCompanyId(company_id);
		return computer;
	}
	
	/**
	 * Construct a new user in line command
	 * @return the new user
	 */
	private User constructUser() {
		User user = new User();
		String name = readString("Choose the name:");
		String pwd = readString("Choose the password:");
		Set<UserRole> roles = readRole();
		user.setUsername(name);
		user.setPassword(pwd);
		user.setUserRole(roles);
		return user;
	}

	/**
	 * The main UI
	 */
	public void session() {
		scan = new Scanner(System.in);
		initLists();
		boolean exit = false;
		int step = 0;
		while (!exit) {
			int entry = -1;
			while (entry == -1) {
				switch (step) {
				case 0 : showGeneralInstr(); break;
				case 1 : showCompanyInstr(); break;
				case 2 : showComputerInstr(); break;
				case 3 : showUserInstr(); break;
				}
				entry = readOption(step);
			}
			if (step == 0) {
				switch (entry) {
				case 1 : step = 1; break;
				case 2 : step = 2; break;
				case 3 : step = 3; break;
				case 4 : exit = true; break;
				}
			} else if (step == 1) {
				switch (entry) {
				case 1 : 
					System.out.println("---------------------");
					System.out.println("- List of companies -");
					System.out.println("---------------------");
					navigatePage(companyService);
					break;
				case 2 : 
					Long id = readId();
					companyService.delete(id);
					System.out.println("Company successfully deleted");
				case 3 : step = 0;
				break;
				}
			} else if (step == 2) {
				Computer computer;
				ComputerDTO computerDTO;
				switch (entry) {
				case 1 :
					System.out.println("---------------------");
					System.out.println("- List of computers -");
					System.out.println("---------------------");
					navigatePage(computerService);
					break;
				case 2 :
					Long id = readId();
					computer = computerService.getComputer(id);
					if (computer == null) {
						System.out.println("No computer is referenced by id " + id);
					} else {
						System.out.println(computer.toDetailedString());
					}
					break;
				case 3 :
					computerDTO = constructComputer();
					computer = computerMapper.getFromDTO(computerDTO);
					computerService.create(computer);
					break;
				case 4 :
					Long updateId = readId();
					computerDTO = constructComputer();
					computerDTO.setId(updateId);
					computer = computerMapper.getFromDTO(computerDTO);
					computerService.update(computer);                     
					break;
				case 5 : Long delId = readId(); computerService.delete(delId); break;
				case 6 : step = 0; break;
				}
			} else {
				User user;
				Long id;
				switch (entry) {
				case 1:
					System.out.println("---------------------");
					System.out.println("- List of users -");
					System.out.println("---------------------");
					navigatePage(userService);
					break;
				case 2:
					id = readId();
					user = userService.findById(id);
					if (user == null) {
						System.out.println("No user is referenced by id " + id);
					} else {
						System.out.println(user);
					}
					break;
				case 3:
					user = constructUser();
					userService.create(user);
					break;
				case 4:
					id = readId();
					userService.delete(id);
					break;
				case 5:
					step = 0;
					break;
				}
			}
		}
		scan.close();
	}
}
