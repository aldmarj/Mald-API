/**
 * 
 */
package exceptions;

/**
 * Generic no data store connection to mask the datastore backend.
 * 
 * @author Lawrence
 *
 */
@SuppressWarnings("serial")
public class DataAccessException extends Exception {

	/**
	 * CLASS CONSTRUCTOR
	 * 
	 * @param message - a message to describe the error.
	 * @param cause - the original error we are abstracting.
	 */
    public DataAccessException(String message, Throwable cause) 
    {
        super(message, cause);
    }
}
