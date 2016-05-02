package com.excilys.cdb.dto.implementation;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import com.excilys.cdb.dto.IDTO;
import com.excilys.cdb.dto.validation.ConsistentDate;
import com.excilys.cdb.dto.validation.DateFormat;

/**
 * DTO implementation for computers
 */
@ConsistentDate
public class ComputerDTO implements IDTO {

	private Long id;
	@NotNull(message="{error.name.null}")
	@NotBlank(message="{error.name.empty}")
	@Size(max=80, message="{error.name.size")
	@Pattern(regexp = "^([\\p{L} 0123456789.'+-/]|[(]|[)])*$", message = "{error.name.content}")
	private String name;
	@DateFormat
	private String introduced;
	@DateFormat
	private String discontinued;
	private Long companyId;
	private String companyName;

	public ComputerDTO() {
	}

	private ComputerDTO(Long id, String name, String introduced, String discontinued, Long companyId, String companyName) {
		this.id = id;
		this.name = name;
		this.introduced = introduced;
		this.discontinued = discontinued;
		this.companyId = companyId;
		this.companyName = companyName;
	}

	public ComputerDTO(String name) {
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

	public void setName(String name) {
		this.name = name;
	}

	public String getIntroduced() {
		return introduced;
	}

	public void setIntroduced(String introduced) {
		this.introduced = introduced;
	}

	public String getDiscontinued() {
		return discontinued;
	}

	public void setDiscontinued(String discontinued) {
		this.discontinued = discontinued;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((companyId == null) ? 0 : companyId.hashCode());
		result = prime * result + ((companyName == null) ? 0 : companyName.hashCode());
		result = prime * result + ((discontinued == null) ? 0 : discontinued.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((introduced == null) ? 0 : introduced.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ComputerDTO other = (ComputerDTO) obj;
		if (companyId == null) {
			if (other.companyId != null)
				return false;
		} else if (!companyId.equals(other.companyId))
			return false;
		if (companyName == null) {
			if (other.companyName != null)
				return false;
		} else if (!companyName.equals(other.companyName))
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
	public String toString() {
		return name + " (id: " + id + ")";
	}

	public static class Builder {
		private Long id;
		private String name;
		private String introduced;
		private String discontinued;
		private Long companyId;
		private String companyName;

		public Builder(String name) {
			this.name = name;
		}

		public Builder id(Long id) {
			this.id = id;
			return this;
		}

		public Builder introduced(String introduced) {
			this.introduced = introduced;
			return this;
		}

		public Builder discontinued(String discontinued) {
			this.discontinued = discontinued;
			return this;
		}

		public Builder companyId(Long companyId) {
			this.companyId = companyId;
			return this;
		}

		public Builder companyName(String companyName) {
			this.companyName = companyName;
			return this;
		}

		public ComputerDTO build() {
			return new ComputerDTO(id, name, introduced, discontinued, companyId, companyName);
		}
	}
}
