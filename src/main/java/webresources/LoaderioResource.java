package webresources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * Resource for validating the load tester. The load tester suite
 * checks for the following id before load testing it to prevent DDOS.
 * 
 * @author Lawrence
 */
@Path("/loaderio-{id}")
public class LoaderioResource {
	
	/**
	 * GET request for the load tester key.
	 * 
	 * @return the load tester unique id.
	 */
    @GET
    public String getLoaderId() {
      return "loaderio-34eb3ce65eb9aa3f8fd85d3abc5e4402";
    }
}
