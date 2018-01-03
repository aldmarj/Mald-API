/**
 *
 */

import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;

/**
 * Config of the resources to add to the web interface.
 *
 * @author Lawrence
 */
@ApplicationPath("/*")
public class RestConfig extends ResourceConfig
{
	/**
	 * List of resources to add to the web front end.
	 */
    public RestConfig()
    {
    	// Add all resources in package webresources and its children
        this.packages("webresources"); //NON-NLS
    }
}
