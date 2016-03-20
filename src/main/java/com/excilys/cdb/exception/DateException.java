/**
 * 
 */
package com.excilys.cdb.exception;


/**
 * Custom exception for miscellaneous dates mistakes
 * @author excilys
 *
 */
public class DateException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public DateException(String message) {
        super(message);
    }
}
