package servlets;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import models.Employee;

/**
 * Employee servlet to handle employee processing.
 *  
 * @author Lawrence
 */
@Path("/business/{buisnessTag}/userName")
public class EmployeeServlet
{
	/**
	 * Getter for getting a employee by its username.
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{userName}")
	public Employee getWorkLog(@PathParam("buisnessTag") String businessTag,
			@PathParam("userName") String userName)
	{	
		Employee employee = new Employee();
		
		return employee;
	}
	
	/**
	 * Post method for creating a new employee.
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response putWorkLog(@PathParam("buisnessTag") String businessTag, 
			Employee employee)
	{
		return Response.status(200).entity("").build();
	}
}
