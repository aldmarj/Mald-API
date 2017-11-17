/**
 * 
 */
package webresources;

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
        returnValue.add(HealthResource.class);
        returnValue.add(BusinessResource.class);
        returnValue.add(WorkLogResource.class);
        returnValue.add(ClientResource.class);
        returnValue.add(EmployeeResource.class);
        return returnValue;
    }
}