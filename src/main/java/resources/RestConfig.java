/**
 *
 */
package resources;

import org.glassfish.jersey.server.ResourceConfig;
import resources.auth.AuthenticationFilter;
import resources.auth.LoginResource;

import javax.ws.rs.ApplicationPath;
/**
 * @author Lawrence
 *
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
