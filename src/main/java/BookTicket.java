import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class BookTicket extends HttpServlet
{
    private static final long serialVersionUID = 1L;

    public void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException
    {
        response.setContentType("text/html");
        PrintWriter out=response.getWriter();

        
        String fname=request.getParameter("fname");
        String lname=request.getParameter("lname");
        String age=request.getParameter("age");
        String num=request.getParameter("seats_to_book");
        String ph=request.getParameter("phone_no");
        
        System.out.println("first name: "+fname);
        System.out.println("last name: "+lname);
        System.out.println("age: "+age);
        System.out.println("number of seats: "+num);
        System.out.println("phone number: "+ph);


        try
        {
            Connection con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "sys as sysdba", "root");

            PreparedStatement ps=con.prepareStatement("insert into register values(?, ?, ?, ?, ?)");

            ps.setString(1,fname);
            ps.setString(2,lname);
            ps.setString(3,age);
            ps.setString(4,num);
            ps.setString(5, ph);

            int i=ps.executeUpdate();
            if(i>0)
            {
                out.print("Registered successfully!!");
                RequestDispatcher rd = request.getRequestDispatcher("deleterow.html");
                rd.forward(request, response);

            }
            else
            {
            	out.print("register unsuccessful");
            }


        }
        catch(Exception d)
        {
        	d.printStackTrace();
        }
        out.close();
        

    }
  
}