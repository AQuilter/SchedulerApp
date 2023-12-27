package sample.Controller;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import sample.DOA.DBCountries;
import sample.DOA.DBCustomers;
import sample.DOA.DBFirstLevelDivisions;

import javafx.event.ActionEvent;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * AddCustomerController.  Handles adding customers to the database.
 */
public class AddCustomerController implements Initializable {
    public TextField nameTextField;
    public TextField phoneNumTextField;
    public TextField postalCodeTextField;
    public TextField addressTextField;
    // public TextField idTextField;
    public ComboBox<String> stateProvinceComboBox;
    public ComboBox<String> countryComboBox;

    String errorMessage;

    /**
     * Populates the state/province with data according to what country the user chooses.
     * Lambda #2: Uses a listener to populate data according to the result of a previous combo box.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        countryComboBox.setItems(DBCountries.getAllCountries());

        // Lambda #2: Uses a listener to populate data according to the result of a previous combo box.
        countryComboBox.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue == null) {
                stateProvinceComboBox.getItems().clear();
                stateProvinceComboBox.setDisable(true);
            } else {
                stateProvinceComboBox.setDisable(false);
                try {
                    stateProvinceComboBox.setItems(DBFirstLevelDivisions.getAllDivisionsByCountry(countryComboBox.getValue()));
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
    }

    /**
     * Ensures textfields are not null before adding information to the database.
     * @param actionEvent
     */
    public void saveBtnClicked(ActionEvent actionEvent) throws IOException, SQLException {

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
                DBCustomers.addCustomer(name, address, postalCode, phone,
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
