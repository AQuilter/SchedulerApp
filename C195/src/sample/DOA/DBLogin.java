package sample.DOA;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.Model.Users;

import javax.xml.transform.Result;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBLogin{


    /**
     * Query checks whether or not the passed in userName and password match in the database.
     * If so, it returns true. If not, it returns false.
     * @param password
     * @param userName
     * @return true or false
     */
    public static boolean validateLogin(String userName, String password){

        try {
            String sql = "SELECT * FROM users WHERE User_Name = '" + userName + "'AND Password = '" + password + "'";

            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                if (rs.getString("User_Name").equals(userName) && rs.getString("Password").equals(password)) {
                    return true;
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

}
