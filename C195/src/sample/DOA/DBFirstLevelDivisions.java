package sample.DOA;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBFirstLevelDivisions {


    /**
     * Gets all first level divisions depending on the country.
     * @param country the customer's country.
     * @return the matching divisions based on the country.
     */
    public static ObservableList<String> getAllDivisionsByCountry(String country) throws SQLException {

        ObservableList<String> divisions = FXCollections.observableArrayList();

        String sql = "SELECT countries.Country, countries.Country_ID,  first_level_divisions.Division_ID, " +
                "first_level_divisions.Division FROM countries RIGHT OUTER JOIN first_level_divisions ON " +
                "countries.Country_ID = first_level_divisions.Country_ID WHERE countries.Country = ?;";

        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

        ps.setString(1, country);

        ResultSet rs = ps.executeQuery();

        while(rs.next()){
            divisions.add(rs.getString("Division"));
        }
        return divisions;
    }


    /**
     * Query finds the division ID associated with the divisionName.
     * @param divisionName
     * @return id: the division ID.
     */
    public static int getFLDIdByDivisionName(String divisionName) {
        int id =  0;

        try{
            String sql = "SELECT Division_ID FROM first_level_divisions WHERE Division = ?;";

            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

            ps.setString(1, divisionName);

            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                id = rs.getInt("Division_ID");
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return id;
    }

}
