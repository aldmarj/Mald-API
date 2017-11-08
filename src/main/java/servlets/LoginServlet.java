package servlets;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("/login")
public class LoginServlet
{


    @GET
    public boolean isAuthenticated()
    {
        return false;
    }

    @POST
    public String login(@FormParam("u") String username, @FormParam("p") String password)
    {
        return null;
    }
}
