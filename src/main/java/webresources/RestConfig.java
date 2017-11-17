/**
 *
 */
package webresources;

import org.glassfish.jersey.server.ResourceConfig;
import webresources.auth.AuthenticationFilter;
import webresources.auth.LoginResource;

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
        register(BusinessResource.class);
        register(WorkLogResource.class);
        register(ClientResource.class);
        register(EmployeeResource.class);
        register(LoginResource.class);
        register(AuthenticationFilter.class);
    }
}
