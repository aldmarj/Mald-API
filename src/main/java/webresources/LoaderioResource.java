package webresources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/loaderio-{id}")
public class LoaderioResource {
    @GET
    public String getLoaderId() {
      return "loaderio-34eb3ce65eb9aa3f8fd85d3abc5e4402";
    }
}
