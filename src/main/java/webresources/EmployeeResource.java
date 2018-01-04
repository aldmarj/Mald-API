package webresources;

import models.Employee;
import models.Password;
import utilities.PasswordUtils;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;

import org.apache.log4j.Logger;

import database.DBEmployeeQueries;
import exceptions.BadKeyException;
import exceptions.DataAccessException;

/**
 * Employee servlet to handle employee processing.
 *  
 * @author Lawrence
 */
@Path("/business/{buisnessTag}/employee")
public class EmployeeResource
{
    /** Logger **/
    private static final Logger LOGGER = Logger.getLogger(EmployeeResource.class);
    
	/**
	 * Getter for getting a employee by its username.
	 * 
	 * @param username - The username of the employee to return.
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{userName}")
	public Employee getEmployee(@PathParam("buisnessTag") String businessTag,
			@PathParam("userName") String userName)
	{	
		Employee employee = new Employee();
		
		try 
		{
			employee = new DBEmployeeQueries().getEmployee(userName, businessTag);
			
			if (employee != null)
			{
				return employee;
			}
			else
			{
				throw new WebApplicationException(Response.Status.NOT_FOUND);
			}
		}
		catch (DataAccessException e) 
		{
			throw new WebApplicationException(Response.Status.BAD_GATEWAY);		
		}
	}
	
	/**
	 * Getter for getting the top employees in a range of time. 
	 * Sorted by those that have worked the most first.
	 * Does not count worklogs that aren't completely in the time period. i.e.
	 * Start and finish in the time period.
	 * 
	 * @param businessTag - The tag of the business to interrogate.
	 * @param startRange - The start of the range of employees to return, 1 for the first. (Non 0 indexed)
	 * @param endRange - The end of the range of employees to return, x for the xth. (Non 0 indexed)
	 * @param startTimeRange - The time in millisecond to start counting from.
	 * @param endTimeRange - The time in millisecond to stop counting from.
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("mostWorked/top/{startRange : \\d+}/{endRange : \\d+}/between/{startTimeRange : \\d+}/{endTimeRange : \\d+}")
	public Collection<Employee> getEmployeebyMostWorkedRange(@PathParam("buisnessTag") String businessTag,
			@PathParam("startRange") int startRange, @PathParam("endRange") int endRange,
			@PathParam("startTimeRange") long startTimeRange, @PathParam("endTimeRange") long endTimeRange)
	{	
		Collection<Employee> employees = new ArrayList<Employee>();
		
		try 
		{
			// Minus one to the given values to account for 0th index.
			employees = 
					new DBEmployeeQueries().getAllEmployeesbyMostWorkedRangeBetweenTimes(
							businessTag, startRange - 1, endRange - 1, startTimeRange, endTimeRange);
			
			if (employees != null)
			{
				return employees;
			}
			else
			{
				throw new WebApplicationException(Response.Status.NOT_FOUND);
			}
		}
		catch (DataAccessException e) 
		{
			throw new WebApplicationException(Response.Status.BAD_GATEWAY);		
		}
	}
	
	/**
	 * Post method for creating a new employee.
	 * 
	 * @param businessTag - The business to add the employee to.
	 * @param securityContext - The session information of the logged in user.
	 * @param employee - The details of the new employee to create.
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public String postEmployee(@PathParam("buisnessTag") String businessTag, 
			@Context SecurityContext securityContext,
			Employee employee)
	{
		String returnMessage;
		
		try 
		{
			employee.setBusinessTag(businessTag);
			
			// Check the requested password is okay
			if (employee.getRequestedPassword() != null 
					&& PasswordUtils.conformsToSecurityRules(employee.getRequestedPassword()))
			{
				// Generate the salted hash for the password
				employee.getAccount().setStoredPassword(
						Password.fromPlainText(employee.getRequestedPassword()));
				
				//Ensure the rest of the employee is valid.
				if (employee.isValid())
				{
					new DBEmployeeQueries().createEmployeeAccount(employee);
					
					return employee.getHoursWorked() == -1 
							? "Successfully added" : "Warning! hours worked cannot be set";
				}
				else
				{
					returnMessage = "Invalid employee supplied";
				}
			}
			else
			{
				returnMessage = "Password needs at least 8 characters; one upper case, one lower case and a digit";
			}
			
            LOGGER.error(returnMessage);
            throw new WebApplicationException(returnMessage, 
            		Response.status(Status.BAD_REQUEST).entity(returnMessage).build());
		}
		catch (final BadKeyException e)
		{
			returnMessage = "Employee with given username already exists";
            LOGGER.error(returnMessage);
            throw new WebApplicationException(returnMessage, e, 
            		Response.status(Status.BAD_REQUEST).entity(returnMessage).build());
		}
		catch (final DataAccessException e)
		{
			returnMessage = "No data store found";
            LOGGER.error(returnMessage, e);
            throw new WebApplicationException(returnMessage, e, 
            		Response.status(Status.SERVICE_UNAVAILABLE).entity(returnMessage).build());
		} 
		catch (NoSuchAlgorithmException e) 
		{
            returnMessage = "Server could not authenticate password";
            LOGGER.error(returnMessage, e);
            throw new WebApplicationException(returnMessage, e, 
            		Response.status(Status.INTERNAL_SERVER_ERROR).entity(returnMessage).build());

		}
	}
}
