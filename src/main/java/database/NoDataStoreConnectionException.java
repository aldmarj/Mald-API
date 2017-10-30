/**
 * 
 */
package database;

/**
 * Generic no data store connection to mask the datastore backend.
 * 
 * @author Lawrence
 *
 */
@SuppressWarnings("serial")
public class NoDataStoreConnectionException extends Exception {

	/**
	 * CLASS CONSTRUCTOR
	 * 
	 * @param message - a message to describe the error.
	 * @param cause - the original error we are abstracting.
	 */
    public NoDataStoreConnectionException(String message, Throwable cause) 
    {
        super(message, cause);
    }
}
