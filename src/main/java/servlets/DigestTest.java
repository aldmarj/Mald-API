package servlets;

import models.users.Password;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

@WebServlet("/digest")
public class DigestTest extends HttpServlet
{
    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException
    {
        resp.setContentType("text/plain");

        try
        {
            resp.getWriter().print(Password.fromPlainText(req.getParameter("pw")));
        }
        catch (final NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
    }
}
