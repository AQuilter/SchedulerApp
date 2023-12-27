package sample.DOA;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import sample.Model.Customers;

import javafx.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBCustomers {


    /**
     * Query returns a list of all the customers within the database.
     * @return customersList: a list of all customers
     */
    public static ObservableList<Customers> getAllCustomers () {
        ObservableList<Customers> customersList = FXCollections.observableArrayList();

        try {
            String sql = "SELECT customers.Customer_ID, customers.Customer_Name, " +
                    "customers.Address, customers.Postal_Code, customers.Phone, customers.Division_ID, " +
                    "first_level_divisions.Division, first_level_divisions.Country_ID, countries.Country " +
                    "FROM customers INNER JOIN  first_level_divisions ON customers.Division_ID = first_level_divisions.Division_ID " +
                    "INNER JOIN countries ON first_level_divisions.Country_ID = countries.Country_ID";

            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                int customerId = rs.getInt("Customer_ID");
                String name = rs.getString("Customer_Name");
                String address = rs.getString("Address");
                String postalCode = rs.getString("Postal_Code");
                String phoneNumber = rs.getString("Phone");
                String country = rs.getString("Country");
                int divisionId = rs.getInt("Division_ID");
                String divisionName = rs.getString("Division");

                Customers customers = new Customers(customerId, name, address, postalCode, phoneNumber,
                        country, divisionId, divisionName);

                customersList.add(customers);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return customersList;
    }

    /**
     * Query updates adds existing customer to the database with the information passed in. Does not need the customerID
     * since this is auto-incremented anyway.
     * @param name
     * @param address
     * @param postalCode
     * @param phoneNumber
     * @param divisionId
     */
    public static void addCustomer (String name, String address, String postalCode, String phoneNumber,
                                    int divisionId) {

        try {

            String sql = "INSERT INTO customers (Customer_Name, Address, Postal_Code, Phone, " +
                    "Division_ID) VALUES (?,?,?,?,?);";

            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

            ps.setString(1, name);
            ps.setString(2, address);
            ps.setString(3, postalCode);
            ps.setString(4, phoneNumber);
            ps.setInt(5, divisionId);

            ps.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Query updates an existing customer with the information passed in.
     * @param customerId
     * @param name
     * @param address
     * @param postalCode
     * @param phoneNumber
     * @param division
     */
    public static void updateCustomer (int customerId, String name, String address, String postalCode, String phoneNumber,
                                       int division){

        try {

            String sql = "UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, " +
                    "Division_ID = ? WHERE Customer_ID = ?";

            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);


            ps.setString(1, name);
            ps.setString(2, address);
            ps.setString(3, postalCode);
            ps.setString(4, phoneNumber);
            ps.setInt(5, division);
            ps.setInt(6, customerId);

            ps.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Query deletes the customer with the associated customerId from the database.
     * @param customerId
     */
    public static void deleteCustomer (int customerId) {
        try{

            String sql = "DELETE FROM customers WHERE Customer_ID = ?";

            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

            ps.setInt(1, customerId);

            ps.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}


//    String sql = "SELECT customers.Customer_ID, customers.Customer_Name, " +
//            "customers.Address, customers.Postal_Code, customers.Phone, customers.Division_ID, " +
//            "first_level_divisions.Division from customers INNER JOIN  first_level_divisions " +
//            "ON customers.Division_ID = first_level_divisions.Division_ID";