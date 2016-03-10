package com.excilys.cdb.model;

import java.time.LocalDate;

/**
 * Computer model
 * @author excilys
 *
 */
public class Computer {
	
	private Long id;
	private String name;
	private LocalDate introduced;
	private LocalDate discontinued;
	private Company company;

	public Computer() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDate getIntroduced() {
		return introduced;
	}

	public void setIntroduced(LocalDate introduced) {
		this.introduced = introduced;
	}

	public LocalDate getDiscontinued() {
		return discontinued;
	}

	public void setDiscontinued(LocalDate discontinued) {
		this.discontinued = discontinued;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) {
			return true;
		}
		if(obj == null) {
			return false;
		}
		if(getClass() != obj.getClass()) {
			return false;
		}
		Computer other = (Computer) obj;
		return id.equals(other.id)
				&& name.equals(other.name) 
				&& introduced.equals(other.introduced)
				&& discontinued.equals(other.discontinued)
				&& company.equals(other.company);
	}
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}
	
	@Override
	public String toString() {
		return name + " (id: " + id + ")";
	}
	
	public String toDetailedString() {
		String result = toString() + "\n";
		result += introduced != null ? "Introduced: " + introduced +"\n" : "";
		result += discontinued != null ? "Discontinued: " + discontinued + "\n" : "";
		result += company != null ? "Company: " + company + "\n" : "";
		return result;
	}
}
