import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class TrainTicket extends HttpServlet 
{
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try 
        {
            // Load Oracle JDBC Driver
            Class.forName("oracle.jdbc.driver.OracleDriver");

            // Establish connection
            System.out.println("before connection");
            conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "sys as sysdba", "root");
            System.out.println("successful connection");

            //source_city=City+A&destination_city=City+B&departure_date=2024-05-27&arrival_date=2024-05-27&seatClass=CC
            // Retrieve parameters
            String sourceCity = request.getParameter("source_city");
            String destinationCity = request.getParameter("destination_city");
            String departureDate = request.getParameter("departure_date");
            String arrivalDate = request.getParameter("arrival_date");
            
            
            // Print the parameters for debugging
            System.out.println("Source City: " + sourceCity);
            System.out.println("Destination City: " + destinationCity);
            System.out.println("Departure Date: " + departureDate);
            System.out.println("Arrival Date: " + arrivalDate);
            
            
            
            // If any parameter is null, send error message
            if (sourceCity == null || destinationCity == null || departureDate == null || arrivalDate == null) 
            {
                out.println("<h1>Invalid input. Please provide all required parameters.</h1>");
                return;
            }

            // Create prepared statement
            String query = "SELECT * FROM train_info WHERE source_city = ? AND destination_city = ? " +
                    "AND departure_date = TO_DATE(?, 'YYYY-MM-DD') AND arrival_date = TO_DATE(?, 'YYYY-MM-DD')";
            ps = conn.prepareStatement(query);
            ps.setString(1, sourceCity);
            ps.setString(2, destinationCity);
            ps.setString(3, departureDate);
            ps.setString(4, arrivalDate);

            // Execute query
            System.out.println("before select");
            rs = ps.executeQuery();
            System.out.println("after select");
            
         // Check if the result set is empty
            if (!rs.isBeforeFirst()) {
                System.out.println("No data found for the given criteria.");
                out.println("<h1>No train information available for the given criteria.</h1>");
                return;
            }

            // Display data in HTML table
            System.out.println("before html body");
            
            out.println("<html><head>");
            out.println("<style>");
            out.println("table { width: 100%; border-collapse: collapse; }");
            out.println("th, td { border: 1px solid black; padding: 8px; text-align: left; }");
            out.println("th { background-color: blue; color: white; }");
            out.println("tr:nth-child(even) { background-color: #f2f2f2; }");  // Optional for alternate row coloring
            out.println("@media screen and (max-width: 600px) {");
            out.println("    table, thead, tbody, th, td, tr { display: block; }");
            out.println("    th { position: absolute; top: -9999px; left: -9999px; }");
            out.println("    td { border: none; position: relative; padding-left: 50%; }");
            out.println("    td:before {");
            out.println("        position: absolute;");
            out.println("        top: 6px; left: 6px;");
            out.println("        width: 45%;");
            out.println("        padding-right: 10px;");
            out.println("        white-space: nowrap;");
            out.println("        content: attr(data-title);");
            out.println("        font-weight: bold;");
            out.println("    }");
            out.println("}");
            out.println("</style>");
            out.println("</head><body>");
            out.println("<h1>Train Information</h1>");
            out.println("<form method='get' action='SelectTrain.html'>");
            out.println("<table>");
            out.println("<thead>");
            out.println("<tr><th>ID</th><th>Train Name</th><th>Seat Class</th><th>Source City</th><th>Source Station</th><th>Departure Date</th><th>Departure Time</th><th>Destination City</th><th>Destination Station</th><th>Arrival Date</th><th>Arrival Time</th><th>Select</th></tr>");
            out.println("</thead>");
            out.println("<tbody>");

            while (rs.next()) {
                System.out.println("inside while");
                out.println("<tr>");
                out.println("<td data-title='ID'>" + rs.getInt("id") + "</td>");
                out.println("<td data-title='Train Name'>" + rs.getString("train_name") + "</td>");
                out.println("<td data-title='Seat Class'>" + rs.getString("seat_class") + "</td>");
                out.println("<td data-title='Source City'>" + rs.getString("source_city") + "</td>");
                out.println("<td data-title='Source Station'>" + rs.getString("source_station") + "</td>");
                out.println("<td data-title='Departure Date'>" + rs.getDate("departure_date") + "</td>");
                out.println("<td data-title='Departure Time'>" + rs.getString("departure_time") + "</td>");
                out.println("<td data-title='Destination City'>" + rs.getString("destination_city") + "</td>");
                out.println("<td data-title='Destination Station'>" + rs.getString("destination_station") + "</td>");
                out.println("<td data-title='Arrival Date'>" + rs.getDate("arrival_date") + "</td>");
                out.println("<td data-title='Arrival Time'>" + rs.getString("arrival_time") + "</td>");
                out.println("<td data-title='Select'><button type='button' onclick='selectTrain(" + rs.getInt("id") + ")'>Select</button></td>");
                out.println("</tr>");
            }
            System.out.println("outside while");

            out.println("</tbody>");
            out.println("</table>");
            out.println("<input type='hidden' id='selectedTrain' name='selected_train' required>");
            out.println("<button type='submit'>Book Now</button>");
            out.println("</form>");

            out.println("<script>");
            out.println("function selectTrain(id) {");
            out.println("    document.getElementById('selectedTrain').value = id;");
            out.println("    alert('Train ' + id + ' selected.');");
            out.println("}");
            out.println("</script>");
            out.println("</body></html>");
            
            System.out.println("after script");



        }
        catch (Exception e) 
        {
            e.printStackTrace(out);
        } 
        finally 
        {
            try 
            { 
                if (rs != null) rs.close(); 
            } 
            catch (Exception e) {}
            
            try 
            { 
                if (ps != null) ps.close(); 
            }
            catch (Exception e) {}
        }
    }
}
           
