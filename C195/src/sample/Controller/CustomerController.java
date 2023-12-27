package sample.Controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.DOA.DBAppointments;
import sample.DOA.DBCustomers;
import sample.Model.Appointments;
import sample.Model.Customers;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;

import javax.swing.*;

/**
 * CustomerController. Displays customers from the database.
 */
public class CustomerController implements Initializable {


    public TableView<Customers> customersTableView;
    public TableColumn<Customers, Integer> idCol;
    public TableColumn<Customers, String> nameCol;
    public TableColumn<Customers, String> addressCol;
    public TableColumn<Customers, String> divisionCol;
    public TableColumn<Customers, String> countryCol;
    public TableColumn<Customers, String> postalCodeCol;
    public TableColumn<Customers, String> phoneCol;

    //public TableColumn <Customers, Integer> divisionIdCol;


    ObservableList<Customers> customersObservableList = DBCustomers.getAllCustomers();
    String errorMessage;

    /**
     * Initializes the table with all customer information.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        for (Customers customers : customersObservableList) {
            customersTableView.setItems(customersObservableList);

            idCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
            nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
            addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
            divisionCol.setCellValueFactory(new PropertyValueFactory<>("divisionName"));
            countryCol.setCellValueFactory(new PropertyValueFactory<>("country"));
            postalCodeCol.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
            phoneCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        }
    }


    /**
     * Launches the Add Customer screen.
     * @param actionEvent
     */
    public void addCustomerBtnClicked(ActionEvent actionEvent) throws IOException {
        URL url = new File("src/sample/View/AddCustomer.fxml").toURI().toURL();
        Parent root = FXMLLoader.load(url);
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 492, 425);
        stage.setTitle("Add Customer");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Ensures a customer from the table has been selected, then launches the Add Customer screen
     * with that selected customer information.
     * @param actionEvent
     */
    public void updateCustomerBtnClicked(ActionEvent actionEvent) throws IOException, SQLException {

        Customers selectedCustomer = customersTableView.getSelectionModel().getSelectedItem();

        if (selectedCustomer != null) {
            URL url = new File("src/sample/View/UpdateCustomer.fxml").toURI().toURL();
            FXMLLoader loader = new FXMLLoader(url);
            Parent parent = loader.load();
            Scene updateCustomer = new Scene(parent);

            UpdateCustomerController controller = loader.getController();
            controller.loadData(selectedCustomer);

            Stage window = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            window.setScene(updateCustomer);
        } else {
            errorMessage = "Please select a customer.";
            ButtonType buttonType = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
            Alert invalidInput = new Alert(Alert.AlertType.WARNING, errorMessage, buttonType);
            invalidInput.showAndWait();
        }
    }

    /**
     * Ensures a customer from the table is selected, then it deletes that customer from the table and database.
     * @param actionEvent
     */
    public void deleteBtnClicked(ActionEvent actionEvent) {

        Customers selectedCustomer = customersTableView.getSelectionModel().getSelectedItem();

        try {
            if (selectedCustomer != null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete " + selectedCustomer.getName()
                        + " and their appointments?", ButtonType.YES, ButtonType.NO);
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.initOwner((Stage) ((Button) actionEvent.getSource()).getScene().getWindow());
                alert.setHeaderText("Delete customer?");
                Optional<ButtonType> confirmation = alert.showAndWait();
                if (confirmation.get() == ButtonType.YES) {
                    DBAppointments.deleteAppointmentsByCustomerId(selectedCustomer.getCustomerId());
                    DBCustomers.deleteCustomer(selectedCustomer.getCustomerId());

                    // refresh list
                    ObservableList<Customers> refreshList = DBCustomers.getAllCustomers();
                    customersTableView.setItems(refreshList);
                }
            } else {
                errorMessage = "Please select a customer.";
                ButtonType buttonType = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
                Alert invalidInput = new Alert(Alert.AlertType.WARNING, errorMessage, buttonType);
                invalidInput.showAndWait();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Launches the Main Screen.
     * @param actionEvent
     */
    public void backBtnClicked(ActionEvent actionEvent) throws IOException {
        URL url = new File("src/sample/View/MainScreen.fxml").toURI().toURL();
        Parent root = FXMLLoader.load(url);
        Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 375, 377);
        //stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }

}
