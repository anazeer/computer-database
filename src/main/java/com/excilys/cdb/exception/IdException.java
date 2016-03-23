package com.excilys.cdb.exception;

/**
 * Custom exception for miscellaneous id mistakes
 */
public class IdException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new exception with the specified detail message.  The
	 * cause is not initialized, and may subsequently be initialized by
	 * a call to {@link #initCause}.
	 *
	 * @param message the detail message. The detail message is saved for
	 *                later retrieval by the {@link #getMessage()} method.
	 */
	public IdException(String message) {
		super(message);
	}
}