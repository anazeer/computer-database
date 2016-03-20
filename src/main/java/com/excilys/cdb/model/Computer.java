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

    private Computer(ComputerBuilder computerBuilder) {
        this.id = computerBuilder.id;
        this.name = computerBuilder.name;
        this.introduced = computerBuilder.introduced;
        this.discontinued = computerBuilder.discontinued;
        this.company = computerBuilder.company;
    }

	public Computer(String name) {
		setName(name);
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

    /**
     * The name is required and should not be empty
     * @param name the computer name
     */
	public void setName(String name) {
		if(name.trim().isEmpty())
			throw new IllegalArgumentException("Name should'nt be empty");
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
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Computer other = (Computer) obj;
		if (company == null) {
			if (other.company != null)
				return false;
		} else if (!company.equals(other.company))
			return false;
		if (discontinued == null) {
			if (other.discontinued != null)
				return false;
		} else if (!discontinued.equals(other.discontinued))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (introduced == null) {
			if (other.introduced != null)
				return false;
		} else if (!introduced.equals(other.introduced))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((company == null) ? 0 : company.hashCode());
		result = prime * result + ((discontinued == null) ? 0 : discontinued.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((introduced == null) ? 0 : introduced.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
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

    /**
     * Computer builder
     */
    public static class ComputerBuilder {
        private Long id;
        private String name;
        private LocalDate introduced;
        private LocalDate discontinued;
        private Company company;

        /**
         * Computer builder to build a custom computer
         * @param name the computer name
         */
        public ComputerBuilder(String name) {
            this.name = name;
        }

        public ComputerBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public ComputerBuilder introduced(LocalDate introduced) {
            this.introduced = introduced;
            return this;
        }

        public ComputerBuilder discontinued(LocalDate discontinued) {
            this.discontinued = discontinued;
            return this;
        }

        public ComputerBuilder company(Company company) {
            this.company = company;
            return this;
        }

        public Computer build() {
            Computer computer = new Computer(this);
            if(computer.getName().trim().isEmpty())
                throw new IllegalStateException("Name should'nt be empty");
            return computer;
        }
    }
}
