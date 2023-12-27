package sample.DOA;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.Model.Countries;

import javax.xml.transform.Result;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class DBCountries {

    /**
     * Query returns a list of all the countries within the database.
     * @return countryList : list of countries
     */
    public static ObservableList<String> getAllCountries () {
        // create a List to return
        ObservableList<String> countryList = FXCollections.observableArrayList();

        try {
            // set up the sql
            String sql = "SELECT * FROM countries";

            // make the prepared statement
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

            // make the query ==> result set
            ResultSet rs = ps.executeQuery();

            // cycle through the result
            while (rs.next()) {
                countryList.add(rs.getString("Country"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        // return the list
        return countryList;
    }

    // for testing purposes
    public static void checkDateConversion() {
        System.out.println("CREATE DATE TEST");
        String sql = "SELECT Create_Date FROM countries";
        try {
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Timestamp ts = rs.getTimestamp("Create_Date");
                System.out.println("CD: " + ts.toLocalDateTime().toString()); // DB is in UTC, but when pulled it converts to system time zone.
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}
