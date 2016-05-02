package com.excilys.cdb.util;

/**
 * Object containing the query parameters.
 * By default, order is set to NOP and limit is set to 10
 */
public class Constraint {
	
	private int offset;
	private int limit;
	private String filter;
	private Order order;
	
	public Constraint() {
	}
	
	public Constraint(int offset, int limit, String filter, Order order) {
		this.offset = offset;
		this.limit = limit;
		this.filter = filter;
		this.order = order;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public int getOffset() {
		return offset;
	}

	public int getLimit() {
		return limit;
	}

	public String getFilter() {
		return filter;
	}

	public Order getOrder() {
		return order;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((filter == null) ? 0 : filter.hashCode());
		result = prime * result + limit;
		result = prime * result + offset;
		result = prime * result + ((order == null) ? 0 : order.hashCode());
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
		Constraint other = (Constraint) obj;
		if (filter == null) {
			if (other.filter != null)
				return false;
		} else if (!filter.equals(other.filter))
			return false;
		if (limit != other.limit)
			return false;
		if (offset != other.offset)
			return false;
		if (order != other.order)
			return false;
		return true;
	}

	public static class Builder {
		
		private int offset;
		private int limit;
		private String filter;
		private Order order;
		
		public Builder() {
			order = Order.NOP;
		}
		
		public Builder offset(int offset) {
			if (offset < 0) {
				throw new IllegalStateException("The offset should be positive");
			}
			this.offset = offset;
			return this;
		}
		
		public Builder limit(int limit) {
			if (limit < 0) {
				throw new IllegalStateException("The limit should be positive");
			}
			this.limit = limit;
			return this;
		}
		
		public Builder filter(String filter) {
			this.filter = filter;
			return this;
		}
		
		public Builder order(Order order) {
			this.order = order;
			return this;
		}
		
		public Constraint build() {
			return new Constraint(offset, limit, filter, order);
		}
	}
}