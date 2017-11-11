/**
 *
 */
package resources;

import org.glassfish.jersey.server.ResourceConfig;
import servlets.auth.AuthenticationFilter;
import servlets.auth.LoginServlet;

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
        register(LoginServlet.class);
        register(AuthenticationFilter.class);
    }
}
