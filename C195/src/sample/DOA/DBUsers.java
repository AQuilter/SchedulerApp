package sample.DOA;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.Model.Users;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBUsers {

    // might not need this method
    public ObservableList<Users> getAllUsers () {

        ObservableList<Users> allUsers = FXCollections.observableArrayList();

        try {
            String sql = "SELECT * FROM users";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                int userId = rs.getInt("User_ID");
                String userName = rs.getString("User_Name");
                String userPassword = rs.getString("Password");

                Users user = new Users(userId, userName, userPassword);
                allUsers.add(user);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return allUsers;
    }

    public static int getUserIdByName(String inputName){
        int userId = 0;

        try {
            String sql = "SELECT User_ID FROM Users WHERE User_Name = '" + inputName + "';";

            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                userId = rs.getInt("User_ID");
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return userId;
    }

}
