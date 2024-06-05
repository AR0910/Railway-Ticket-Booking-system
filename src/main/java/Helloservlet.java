import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Servlet implementation class Helloservlet
 */
public class Helloservlet extends HttpServlet 
{
    private static final long serialVersionUID = 1L;

    public Helloservlet() 
    {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {
        Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        PrintWriter pw = response.getWriter();
        
        try 
        {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            c = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "sys as sysdba", "root");

            if (c != null && !c.isClosed()) 
            {
                System.out.println("Connection successful");
            }
            else 
            {
                System.out.println("Connection failed");
                pw.println("<h1>Database connection failed</h1>");
                return;
            }
            
            System.out.println("before input \n");
            
            String u = request.getParameter("txtuser");
            String p = request.getParameter("txtpwd");

            String query = "SELECT * FROM User_Login WHERE user_name = ? AND user_password = ?";
            ps = c.prepareStatement(query);
            ps.setString(1, u);
            ps.setString(2, p);
            
            System.out.println("after select \n");

            rs = ps.executeQuery();

            if (rs.next()) 
            {
            	System.out.println("inside if");
                RequestDispatcher rd = request.getRequestDispatcher("BookTicket.html");
                //pw.println("<h1>Welcome, " + u + "!</h1>");      
                rd.forward(request, response);
            } 
            else 
            {
            	System.out.println("inside else");
                pw.println("<h1>Login failed</h1><br>");
                pw.println("<a href='LoginFile.html'>Try again</a>");
            }
        } 
        catch (Exception e) 
        {
            System.out.println(e);
            throw new ServletException(e);
        }
        finally
        {
            try 
            {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (c != null) c.close();
            } 
            catch (Exception e) 
            {
                System.out.println(e);
            }
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {
        doGet(request, response);
    }
}
