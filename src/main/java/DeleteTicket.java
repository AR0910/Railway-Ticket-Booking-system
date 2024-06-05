

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class DeleteTicket
 */
public class DeleteTicket extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
   
    public DeleteTicket() 
    {
        super();
        // TODO Auto-generated constructor stub
    }



	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		// TODO Auto-generated method stub
		response.setContentType("text/html");
        PrintWriter out=response.getWriter();

        
        String fname=request.getParameter("fname");
        String lname=request.getParameter("lname");
        String ph=request.getParameter("phone_no");
        
        System.out.println("inside delete");
        System.out.println("first name: "+fname);
        System.out.println("last name: "+lname);
        System.out.println("phone number: "+ph);   
        
        try
        {
            Connection con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "sys as sysdba", "root");

            PreparedStatement ps=con.prepareStatement("delete from register where fname= ? and lname= ? and phonee= ? ");

           /* ps.setString(1,fname);
            ps.setString(2,lname);
            ps.setString(5, ph);   */

            int i=ps.executeUpdate();
            if(i>0)
            {
                out.print("deleted successfully!!");
            }
            else
            {
            	out.print("delete unsuccessful, enter correct details");
            }

        }
        catch(Exception d)
        {
        	d.printStackTrace();
        }
        out.close();
    }
	
	
	/* protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
		
    	response.setContentType("text/html");
        PrintWriter out=response.getWriter();

        
        String fname=request.getParameter("fname");
        String lname=request.getParameter("lname");
        String ph=request.getParameter("phone_no");
        
        System.out.println("inside delete");
        System.out.println("first name: "+fname);
        System.out.println("last name: "+lname);
        System.out.println("phone number: "+ph);   
        
        try
        {
            Connection con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "sys as sysdba", "root");

            PreparedStatement ps=con.prepareStatement("delete from register where fname= ? and lname= ? and phonee= ? ");

            ps.setString(1,fname);
            ps.setString(2,lname);
            ps.setString(5, ph);   

            int i=ps.executeUpdate();
            if(i<=0)
            {
                out.print("deleted successfully!!");
            }
            else
            {
            	out.print("delete unsuccessful, enter correct details");
            }

        }
        catch(Exception d)
        {
        	d.printStackTrace();
        }
        out.close();
    }      */

}
