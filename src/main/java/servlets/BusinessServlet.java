/**
 * 
 */
package servlets;

import java.io.*;
import java.util.ArrayList;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import org.json.JSONArray;

import database.DBBusinessQueries;
import database.BadKeyException;
import database.NoDataStoreConnectionException;
import models.Business;

/**
 * @author Lawrence
 * 
 * Business servlet to handle business processing.
 *
 */
@SuppressWarnings("serial")
@WebServlet("/business")
public class BusinessServlet extends HttpServlet {
	
	/**
	 * Get a business from the API, will return the business for the given tag
	 * or all businesses if not tag is given.
	 * 
	 * @throws ServletException if a general servlet exception has occurred.
	 * @throws IOException if some I/O failure or interruption has occurred.
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{	   
		response.setContentType("application/json");

		final String businessTag = request.getParameter("businessTag");
		
		try
		{
			PrintWriter out = response.getWriter();

			// If there is no supplied business, return them all.
			if (businessTag == null)
			{
				JSONArray jsArray = getAllBusinessesAsJSONArray();
				
				out.println(jsArray.toString());
			}
			else
			{
				Business result = DBBusinessQueries.getBusiness(businessTag);
				
				if (result != null)
				{
					// Returns the json object to the caller.
					out.println(result.getJsonString());
				}
				else
				{
					response.sendError(HttpServletResponse.SC_NOT_FOUND, "Could not find the selected entity");
				}
			}
		}
		catch (NoDataStoreConnectionException e) 
		{
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Could not find output stream");
		}
	}
	
	/**
	 * Create a new business with the given business tag and business name.
	 * 
	 * @throws ServletException if a general servlet exception has occurred.
	 * @throws IOException if some I/O failure or interruption has occurred.
	 */
	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		response.setContentType("text/html");
		
		final String businessTag = request.getParameter("businessTag");
		final String businessName = request.getParameter("businessName");

		try 
		{
			DBBusinessQueries.createBusiness(businessTag, businessName);
			
			response.setStatus(HttpServletResponse.SC_OK);
		}
		catch (BadKeyException e)
		{
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Key already exists in data store: " + e.getKey());
		}
		catch (NoDataStoreConnectionException e)
		{
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Could not find output stream");
		}
	}
	
	/**
	 * Utility function to get all businesses as a json array.
	 * 
	 * @return returns all the businesses as a json array. 
	 * @throws NoDataStoreConnectionException if a connection cannot be made.
	 */
	private static JSONArray getAllBusinessesAsJSONArray() throws NoDataStoreConnectionException
	{
		ArrayList<Business> businesses = DBBusinessQueries.getAllBusinesses();
		JSONArray jsonArray = new JSONArray();
		
		for (Business business : businesses)
		{
			jsonArray.put(business.getJsonObject());
		}
		
		return jsonArray;
	}
}