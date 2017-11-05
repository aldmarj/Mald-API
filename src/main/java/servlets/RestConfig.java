/**
 * 
 */
package servlets;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
/**
 * @author Lawrence
 *
 */
@ApplicationPath("/*")
public class RestConfig extends Application {

    public RestConfig() {
    }

    @Override
    public Set<Class<?>> getClasses() {
        final Set<Class<?>> returnValue = new HashSet<Class<?>>();
        returnValue.add(BusinessServlet.class);
        returnValue.add(WorkLogServlet.class);
        returnValue.add(ClientServlet.class);
        returnValue.add(EmployeeServlet.class);
        return returnValue;
    }
}