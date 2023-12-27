package sample.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import sample.DOA.DBCountries;
import sample.DOA.DBCustomers;
import sample.DOA.DBFirstLevelDivisions;
import sample.Model.Countries;
import sample.Model.Customers;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;

public class UpdateCustomerController {
    public TextField nameTextField;
    public TextField phoneNumTextField;
    public TextField postalCodeTextField;
    public TextField addressTextField;
    public TextField idTextField;
    public ComboBox<String> stateProvinceComboBox;
    public ComboBox<String> countryComboBox;

    String errorMessage;

    /**
     * Takes the customer selected from the previous screen and loads the text fields with that data.
     * @param selectedCustomer
     */
    public void loadData(Customers selectedCustomer) throws SQLException {

        countryComboBox.setItems(DBCountries.getAllCountries());
        countryComboBox.getSelectionModel().select(selectedCustomer.getCountry());

        stateProvinceComboBox.setItems(DBFirstLevelDivisions.getAllDivisionsByCountry(selectedCustomer.getCountry()));
        stateProvinceComboBox.getSelectionModel().select(selectedCustomer.getDivisionName());

        idTextField.setText(String.valueOf(selectedCustomer.getCustomerId()));
        nameTextField.setText(selectedCustomer.getName());
        phoneNumTextField.setText(selectedCustomer.getPhoneNumber());
        postalCodeTextField.setText(selectedCustomer.getPostalCode());
        addressTextField.setText(selectedCustomer.getAddress());
    }

    /**
     * Ensures textfields are not null before adding information to the database.
     * @param actionEvent
     */
    public void saveBtnClicked(ActionEvent actionEvent) throws IOException, SQLException {

        int customerId = Integer.parseInt(idTextField.getText());
        String name = nameTextField.getText();
        String address = addressTextField.getText();
        String postalCode = postalCodeTextField.getText();
        String phone = phoneNumTextField.getText();
        String stateProvince = stateProvinceComboBox.getValue();
        String country = countryComboBox.getValue();

        try {
            if (name.isBlank() || address.isBlank() || postalCode.isBlank() || phone.isBlank()
                    || stateProvince.isBlank() || country.isBlank()){

                errorMessage = "Please input a value into each field.";
                ButtonType buttonType = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
                Alert invalidInput = new Alert(Alert.AlertType.WARNING, errorMessage, buttonType);
                invalidInput.showAndWait();
            } else {
                DBCustomers.updateCustomer(customerId, name, address, postalCode, phone,
                        DBFirstLevelDivisions.getFLDIdByDivisionName(stateProvince));
                cancelBtnClicked(actionEvent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Launches the main Customer screen.
     * @param actionEvent
     */
    public void cancelBtnClicked(ActionEvent actionEvent) throws IOException {
        URL url = new File("src/sample/View/Customer.fxml").toURI().toURL();
        Parent root = FXMLLoader.load(url);
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 700, 400);
        stage.setTitle("Customers");
        stage.setScene(scene);
        stage.show();
    }
}
