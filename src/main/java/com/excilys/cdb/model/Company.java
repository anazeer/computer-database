package com.excilys.cdb.model;

/**
 * Company model
 * @author excilys
 *
 */
public class Company {
	
	private Long id;
	private String name;

	public Company() {
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
		Company other = (Company) obj;
		return id.equals(other.id) && name.equals(other.name);
	}
	
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}
	
	@Override
	public String toString() {
		String result = new String();
		result += name + " (id : " + id + ")";
		return result;
	}
}
