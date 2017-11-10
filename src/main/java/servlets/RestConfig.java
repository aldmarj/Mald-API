/**
 * 
 */
package servlets;

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
        register(BusinessServlet.class);
        register(WorkLogServlet.class);
        register(ClientServlet.class);
        register(EmployeeServlet.class);
        register(LoginServlet.class);
        register(AuthenticationFilter.class);
    }
}
