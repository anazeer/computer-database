package com.excilys.cdb.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.Filters;
import org.hibernate.annotations.ParamDef;

/**
 * Computer model
 */
@Entity
@Table(name="computer")
@FilterDef(name="filter", parameters={
		@ParamDef(name="filter", type="string")
})
@Filters( {
	@Filter(name="filter", condition="name LIKE :filter")
} )
public class Computer {
		
	@Id
    @Column
    @GeneratedValue
	private Long id;
	
	@Column
	private String name;
	
	@Column
	private LocalDate introduced;
	
	@Column
	private LocalDate discontinued;
	
	@ManyToOne
	@JoinColumn
	@Fetch(FetchMode.SELECT)
	private Company company;

	public Computer() {
	}

    private Computer(Long id, String name, LocalDate introduced, LocalDate discontinued, Company company) {
        this.id = id;
        this.name = name;
        this.introduced = introduced;
        this.discontinued = discontinued;
        this.company = company;
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
    public static class Builder {
        private Long id;
        private String name;
        private LocalDate introduced;
        private LocalDate discontinued;
        private Company company;

        /**
         * Computer builder to build a custom computer
         * @param name the computer name
         */
        public Builder(String name) {
            this.name = name;
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder introduced(LocalDate introduced) {
            this.introduced = introduced;
            return this;
        }

        public Builder discontinued(LocalDate discontinued) {
            this.discontinued = discontinued;
            return this;
        }

        public Builder company(Company company) {
            this.company = company;
            return this;
        }

        public Computer build() {
            if (name.trim().isEmpty()) {
                throw new IllegalStateException("Name shouldn't be empty");
            }
            return new Computer(id, name, introduced, discontinued, company);
        }
    }
}
