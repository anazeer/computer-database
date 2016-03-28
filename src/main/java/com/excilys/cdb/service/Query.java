package com.excilys.cdb.service;

/**
 * Object containing the query parameters
 * @author excilys
 *
 */
public class Query {
	
	private final int offset;
	private final int limit;
	private final String filter;
	private final Order order;
	
	private Query(int offset, int limit, String filter, Order order) {
		super();
		this.offset = offset;
		this.limit = limit;
		this.filter = filter;
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
	
	public static class Builder {
		
		private int offset;
		private int limit;
		private String filter;
		private Order order;
		
		public Builder() {
		}
		
		public Builder offset(int offset) {
			if (offset < 0) {
				throw new IllegalStateException("The offset should be positive");
			}
			this.offset = offset;
			return this;
		}
		
		public Builder limit(int limit) {
			if (limit <= 0) {
				throw new IllegalStateException("The limit should be positive or equals to zero");
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
		
		public Query build() {
			return new Query(offset, limit, filter, order);
		}
	}
}