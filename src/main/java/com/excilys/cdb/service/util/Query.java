package com.excilys.cdb.service.util;

/**
 * Object containing the query parameters
 */
public class Query {
	
	private int offset;
	private int limit;
	private String filter;
	private Order order;
	
	public Query() {
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
		Query other = (Query) obj;
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
		
		private Query query;
		
		public Builder() {
			query = new Query();
		}
		
		public Builder offset(int offset) {
			if (offset < 0) {
				throw new IllegalStateException("The offset should be positive");
			}
			query.offset = offset;
			return this;
		}
		
		public Builder limit(int limit) {
			if (limit < 0) {
				throw new IllegalStateException("The limit shouldn't be negattive");
			}
			query.limit = limit;
			return this;
		}
		
		public Builder filter(String filter) {
			query.filter = filter;
			return this;
		}
		
		public Builder order(Order order) {
			query.order = order;
			return this;
		}
		
		public Query build() {
			return query;
		}
	}
}