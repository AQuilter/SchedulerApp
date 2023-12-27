package sample.DOA;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.Model.Appointments;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class DBReports {

    /**
     * Returns a StringBuilder that displays the total amount of appointments by each type and month.
     * @return StringBuilder sb
     */
    public static String getCustomerAppointments() throws SQLException {

        StringBuilder sb = new StringBuilder();
        sb.append("Total amount of appointments by each type and month. \n");

        String sql = "SELECT Type, MONTHNAME(Start) as \"Month\", count(*) AS \"Total\" FROM Appointments GROUP BY Type, MONTH(Start) ORDER BY month;";

        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

        ResultSet rs = ps.executeQuery();

        String headerType = "Type:";
        String headerMonth = "Month:";
        String headerTotal = "Total:";


        String header = String.format("\n %-20S %1S %20S \n", headerType, headerMonth, headerTotal);
        String header2 = "----------------------------------------------\n";
        sb.append(header).append(header2);

        while (rs.next()) {
            String type = rs.getString("Type");
            String month = rs.getString("Month");
            String total = rs.getString("Total");

            String string = String.format("%-20s %1s %20s \n", type, month, total);
            sb.append(string);
        }
        return sb.toString();
    }

    /**
     * Returns a StringBuilder that displays the appointment schedule for each contact.
     * @return StringBuilder sb
     */
    public static String getContactSchedule() throws SQLException {

        StringBuilder sb = new StringBuilder();

        String sql = "SELECT a.Contact_ID, Title, Type, Description, Appointment_ID, start, end, customer_id " +
                "FROM appointments AS a JOIN contacts AS c ON a.Contact_ID = c.Contact_ID ORDER BY a.Contact_ID;";

        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

        ResultSet rs = ps.executeQuery();

        String header = "Appointment schedule for each contact: \n\n";
        sb.append(header);

        while(rs.next()){
            String contact_id = "Contact ID: " + rs.getString("Contact_ID") + "\n";
            String string = "--------------\n";
            String title = "Title: "  + rs.getString("Title") + "\n";
            String type = "Type: " + rs.getString("Type") + "\n";
            String description = "Description: " + rs.getString("Description") + "\n";
            String appId = "Appointment ID: " + rs.getInt("Appointment_ID") + "\n";
            String start = "Start: " + rs.getTimestamp("Start").toLocalDateTime() + "\n";
            String end = "End: " + rs.getTimestamp("End").toLocalDateTime() + "\n";
            String cusId = "Customer ID: " + rs.getString("Customer_ID") + "\n\n\n";

            sb.append(contact_id).append(string).append(title).append(type).append(description).append(appId)
                    .append(start).append(end).append(cusId);

        }
        return sb.toString();
    }


    /**
     * Returns a StringBuilder that displays the total amount of appointments associated for each contact.
     * @return StringBuilder sb
     */
    public static String getTotalAppsPerContact() throws SQLException {

        StringBuilder sb = new StringBuilder();

        String sql = "SELECT contact_ID AS 'Contact ID', COUNT(contact_ID) as Appointments " +
                "FROM appointments GROUP BY contact_ID ORDER BY appointments DESC";

        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

        ResultSet rs = ps.executeQuery();

        String header = "Number of appointments per contact): \n";
        String string = "---------------------------\n";
        String title = "Contact ID:      Total:\n";

        sb.append(header).append(string).append(title);

        while(rs.next()){
            String contactId = rs.getString("Contact ID");
            String app = rs.getString("Appointments");

            String input = String.format("%10s %16s\n", contactId, app);
            //sb.append(contactId).append("  ").append(app).append("\n");
            sb.append(input);
        }
        return sb.toString();
    }

}
