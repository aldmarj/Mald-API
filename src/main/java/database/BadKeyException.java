/**
 * 
 */
package database;

/**
 * Generic duplicate key exception to mask how the datastore is working.
 * 
 * @author Lawrence
 */
@SuppressWarnings("serial")
public class BadKeyException extends Exception 
{

	/** The key object that caused the exception. **/
	private Object key;
	
    /**
     * CLASS CONSTRUCTOR
     * 
     * @param The key object that caused the exception.
     */
    public BadKeyException(Object key) 
    {
        super();
        this.key = key;
    }

    /**
     * CLASS CONSTRUCTOR
     * 
     * @param A message to store.
     * @param The key object that caused the exception.
     */
    public BadKeyException(String message, Object key) 
    {
        super(message);
        this.key = key;
    }
    
    /**
     * CLASS CONSTRUCTOR
     * 
     * @param A message to store.
     * @param The exception that caused this one.
     * @param The key object that caused the exception.
     */
    public BadKeyException(String message, Throwable cause, Object key) 
    {
        super(message, cause);
        this.key = key;
    }
    
    /**
     * Gets the key value stored that triggered the error.
     * 
     * @return the key.
     */
    public Object getKey()
    {
    	return key;
    }
}
