package webresources;

import models.Employee;
import models.users.Password;
import utils.PasswordUtils;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.log4j.Logger;

import database.BadKeyException;
import database.DBEmployeeQueries;
import database.NoDataStoreConnectionException;

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
		catch (NoDataStoreConnectionException e) 
		{
			throw new WebApplicationException(Response.Status.BAD_GATEWAY);		
		}
	}
	
	/**
	 * Getter for getting a employees in a range those that have worked the most first.
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("mostWorked/top/{startRange : \\d+}/{endRange : \\d+}/between/{startTimeRange : \\d+}/{endTimeRange : \\d+}")
	public Collection<Employee> getEmployeebyMostWorkedRange(@PathParam("buisnessTag") String businessTag,
			@PathParam("startRange") int startRange, @PathParam("endRange") int endRange,
			@PathParam("startTimeRange") long startTimeRange, @PathParam("endTimeRange") int endTimeRange)
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
		catch (NoDataStoreConnectionException e) 
		{
			throw new WebApplicationException(Response.Status.BAD_GATEWAY);		
		}
	}
	
	/**
	 * Post method for creating a new employee.
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public String putEmployee(@PathParam("buisnessTag") String businessTag, 
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
            throw new WebApplicationException(returnMessage, Response.Status.BAD_REQUEST);
		}
		catch (final BadKeyException e)
		{
			returnMessage = "Worklog of given id already exists";
            LOGGER.error(returnMessage);
            throw new WebApplicationException(returnMessage, e, Response.Status.BAD_REQUEST);
		}
		catch (final NoDataStoreConnectionException e)
		{
			returnMessage = "No data store found";
            LOGGER.error(returnMessage, e);
            throw new WebApplicationException(returnMessage, e, Response.Status.SERVICE_UNAVAILABLE);
		} 
		catch (NoSuchAlgorithmException e) 
		{
            returnMessage = "Server could not authenticate password";
            LOGGER.error(returnMessage, e);
            throw new WebApplicationException(returnMessage, e, Response.Status.INTERNAL_SERVER_ERROR);
		}
	}
}
