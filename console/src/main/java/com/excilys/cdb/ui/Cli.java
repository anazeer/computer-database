package com.excilys.cdb.ui;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;

import com.excilys.cdb.dto.implementation.ComputerDTO;
import com.excilys.cdb.mapper.implementation.ComputerMapper;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.pagination.AbstractPage;
import com.excilys.cdb.pagination.util.PageRequest;
import com.excilys.cdb.service.IService;
import com.excilys.cdb.service.implementation.CompanyService;
import com.excilys.cdb.service.implementation.ComputerService;

@Controller
public class Cli {

	// Services
	@Autowired
	private CompanyService companyService;
	@Autowired
	private ComputerService computerService;

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
	private List<Character> pageInstr = new ArrayList<>();

	// Number of options for each menu
	private final int countGeneralInstr = 3;
	private final int countCompanyInstr = 3;
	private final int countComputerInstr = 6;

	// Read inputs
	private Scanner scan;

	/**
	 * Show main menu
	 */
	private void showGeneralInstr() {
		System.out.println("\nComputer database management");
		System.out.println("Choose your database:");
		System.out.println("1. Company");
		System.out.println("2. Computer");
		System.out.println("3. Exit");
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
			}
			else if (step == 1 && !companyInstr.contains(entry)) {
				throw new IllegalArgumentException();
			}
			else if (step == 2 && !computerInstr.contains(entry)) {
				throw new IllegalArgumentException();
			}
		} catch(IllegalArgumentException e) {
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
	 * Read a valid name (not empty)
	 * @return the input name
	 */
	private String readName() {
		String entry = null;
		while (entry == null) {
			System.out.println("Select the name:");
			entry = scan.nextLine().trim();
			if (entry.isEmpty()) {
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

	private void navigatePage(IService<?> service) {
		int currentPage = 1;
		boolean end = false;
		while(!end) {
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
		String name = readName();

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
				case 0 : showGeneralInstr(); entry = readOption(step); break;
				case 1 : showCompanyInstr(); entry = readOption(step); break;
				case 2 : showComputerInstr(); entry = readOption(step); break;
				}
			}
			if (step == 0) {
				switch (entry) {
				case 1 : step = 1; break;
				case 2 : step = 2; break;
				case 3 : exit = true; break;
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
			} else {
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
			}
		}
		scan.close();
	}
}
