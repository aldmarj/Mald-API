package webresources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/")
public class HealthResource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getHealth() {
      return "{\"status\":\"UP\"}";
    }
}
