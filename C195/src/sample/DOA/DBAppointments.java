package sample.DOA;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import sample.Model.Appointments;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public abstract class DBAppointments {

    /**
     * Executes a query to obtain all appointments and adds them to an ObservableList, appList.
     * @return appList: the list of all appointments.
     */
    public static ObservableList<Appointments> getAllAppointments() {

        ObservableList<Appointments> appList = FXCollections.observableArrayList();

        try {

            String sql = "SELECT * FROM appointments";

            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()){

                int appointmentId = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
                LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
                int customerId = rs.getInt("Customer_ID");
                int userId = rs.getInt("User_ID");
                int contactId = rs.getInt("Contact_ID");

                Appointments apps = new Appointments(appointmentId, title, description, location, type,
                        start, end, customerId, userId, contactId);

                appList.add(apps);
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return appList;
    }

    /**
     * Takes info passed in from text fields and executes query to add a new appointment to the database. Used by the
     * 'saveBtnClicked' method within AppointmentsController.
     * @param title
     * @param description
     * @param location
     * @param type
     * @param start
     * @param end
     * @param customerId
     * @param userId
     * @param contactId
     */
    public static void addAppointment (String title, String description, String location, String type, ZonedDateTime start,
                                ZonedDateTime end, int customerId, int userId, int contactId){

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String finalStart = start.format(dateTimeFormatter);
        String finalEnd = end.format(dateTimeFormatter);

        try{

            String sql = "INSERT INTO appointments (Title, Description, Location, Type, Start, End, " +
                    "Customer_ID, User_ID, Contact_ID) VALUES (?,?,?,?,?,?,?,?,?)";

            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);


            ps.setString(1, title);
            ps.setString(2, description);
            ps.setString(3, location);
            ps.setString(4, type);
            ps.setString(5, finalStart);
            ps.setString(6, finalEnd);
            ps.setInt(7, customerId);
            ps.setInt(8, userId);
            ps.setInt(9, contactId);

            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Takes info passed in from text fields and executes query to add a new appointment to the database. Used by the
     * 'saveBtnClicked' method within UpdateAppointmentController.
     * @param title
     * @param description
     * @param location
     * @param type
     * @param start
     * @param end
     * @param customerId
     * @param userId
     * @param contactId
     */
    public static void updateAppointment(int id, String title, String description, String location, String type, ZonedDateTime start,
                                  ZonedDateTime end, int customerId, int userId, int contactId){

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String finalStart = start.format(dateTimeFormatter);
        String finalEnd = end.format(dateTimeFormatter);

        try{

            String sql = "UPDATE appointments SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, " +
                    "Customer_ID = ?, User_ID = ?, Contact_ID = ? WHERE Appointment_ID = ?";

            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

           ps.setString(1, title);
           ps.setString(2, description);
           ps.setString(3, location);
           ps.setString(4, type);
           ps.setString(5, finalStart);
           ps.setString(6, finalEnd);
           ps.setInt(7, customerId);
           ps.setInt(8, userId);
           ps.setInt(9, contactId);
           ps.setInt(10, id);

           ps.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    /**
     * Executes query to delete an appointment (using the appointmentId) from the database.
     * @param appointmentId
     */
    public static void deleteAppointmentById(int appointmentId) {
        try{

            String sql = "DELETE FROM appointments WHERE Appointment_ID = ?";

            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

            ps.setInt(1, appointmentId);

            ps.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Executes query to delete an appointment (using the customerId) from the database. This is used for a cascade
     * delete so that a customer can be deleted. Must delete all customer associated appointments before you can delete
     * the customer.
     * @param customerId
     */
    public static void deleteAppointmentsByCustomerId (int customerId) {

        try{

            String sql = "DELETE FROM appointments WHERE Customer_ID = ?";

            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

            ps.setInt(1, customerId);

            ps.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    /**
     * Query checks to see if the customer has overlapping appointments.
     * @param date
     * @param customerIdInput
     * @return customerApps
     */
    public static ObservableList<Appointments> customerAppointmentsByDateForNewAppt (LocalDate date, int customerIdInput) throws SQLException {

        ObservableList<Appointments> customerApps = FXCollections.observableArrayList();

         String sql = "SELECT * FROM appointments as a LEFT OUTER JOIN contacts as c ON a.Contact_ID = c.Contact_ID " +
                 "WHERE datediff (a.Start, ?) = 0 AND Customer_ID = ?;";

         PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

         ps.setString(1, date.toString());
         ps.setInt(2, customerIdInput);

        ResultSet rs = ps.executeQuery();

        while(rs.next()) {

            int id = rs.getInt("Appointment_ID");
            String title = rs.getString("Title");
            String description = rs.getString("Description");
            String location = rs.getString("Location");
            String type = rs.getString("Type");
            LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
            LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
            int customerId = rs.getInt("Customer_ID");
            int userId = rs.getInt("User_ID");
            int contactId = rs.getInt("Contact_ID");

            Appointments finalCustomerApps = new Appointments(id, title, description, location, type, start,
                    end, customerId, userId, contactId);

            customerApps.add(finalCustomerApps);
        }
        return customerApps;
    }


    public static ObservableList<Appointments> customerAppointmentsByDateForUpdateAppt (LocalDate date, int customerIdInput, int apptId) throws SQLException {

        ObservableList<Appointments> customerApps = FXCollections.observableArrayList();

        String sql = "SELECT * FROM appointments as a LEFT OUTER JOIN contacts as c ON a.Contact_ID = c.Contact_ID " +
                "WHERE datediff (a.Start, ?) = 0 AND Customer_ID = ? AND NOT Appointment_ID = ?;";

        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

        ps.setString(1, date.toString());
        ps.setInt(2, customerIdInput);
        ps.setInt(3, apptId);

        ResultSet rs = ps.executeQuery();

        while(rs.next()) {

            int id = rs.getInt("Appointment_ID");
            String title = rs.getString("Title");
            String description = rs.getString("Description");
            String location = rs.getString("Location");
            String type = rs.getString("Type");
            LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
            LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
            int customerId = rs.getInt("Customer_ID");
            int userId = rs.getInt("User_ID");
            int contactId = rs.getInt("Contact_ID");

            Appointments finalCustomerApps = new Appointments(id, title, description, location, type, start,
                    end, customerId, userId, contactId);

            customerApps.add(finalCustomerApps);
        }
        return customerApps;
    }

    public static ObservableList<Appointments> getAppointmentsByUserID(int userId){
        ObservableList<Appointments> userApps = FXCollections.observableArrayList();

        try{

            String sql = "SELECT * FROM appointments WHERE User_ID = ?;";

            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                int appointmentId = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
                LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
                int customerId = rs.getInt("Customer_ID");
                userId = rs.getInt("User_ID");
                int contactId = rs.getInt("Contact_ID");

                Appointments apps = new Appointments(appointmentId, title, description, location, type,
                        start, end, customerId, userId, contactId);

                userApps.add(apps);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return userApps;
    }

}
