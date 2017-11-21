/**
 *
 */
package webresources;

import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;

/**
 * config of resources to add to the application.
 *
 * @author Lawrence
 */
@ApplicationPath("/*")
public class RestConfig extends ResourceConfig
{
    public RestConfig()
    {
        this.packages("webresources"); //NON-NLS
    }
}
