/**
 * 
 */
package servlets;

import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

/**
 * @author Lawrence
 * 
 * Simple test servlet of which to base future servlets on.
 *
 */
@SuppressWarnings("serial")
@WebServlet("/helloWorld")
public class HelloWorldServlet extends HttpServlet {
	
	private String message;
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
	   // Set response content type
	   response.setContentType("text/html");
	
	   // Actual logic goes here.
	   PrintWriter out = response.getWriter();
	   out.println("<h1>" + message + "</h1>");
	}
}