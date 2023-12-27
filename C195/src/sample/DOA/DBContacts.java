package sample.DOA;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBContacts {

    /**
     * Query obtains a list of all contacts in the database.
     * @return allContactNames: a list of all contact names.
     */
    public static ObservableList<String> getAllContactNames (){
        ObservableList<String> allContactNames = FXCollections.observableArrayList();

        try {
            String sql = "SELECT * FROM Contacts";

            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                allContactNames.add(rs.getString("Contact_Name"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return allContactNames;
    }

    /**
     * Query obtains the name of a contact associated with the contact_id passed in.
     * @param id : the id of the contact you want the name of.
     * @return contactName : the contact name.
     */
    public static String getContactNameByContactId(int id){
        String contactName = "";

        try{

            String sql = "SELECT Contact_Name FROM Contacts WHERE Contact_ID = " + id + ";";

            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                contactName = rs.getString("Contact_Name");
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return contactName;
    }

    // return type might need to be Integer?
    /**
     * Query obtains a contact id associated with the contact name passed in.
     * @param contactName : the contact name you want the ID for.
     * @return contactId : a contact id.
     */
    public static int getContactIdByContactName (String contactName) {
        int contactId = 0;

        try{
            String sql = "SELECT Contact_ID FROM Contacts WHERE Contact_Name = '" + contactName + "'";

            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                contactId = rs.getInt("Contact_ID");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return contactId;
    }


}
