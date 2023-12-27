package sample.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.DOA.DBAppointments;
import sample.Model.Appointments;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * AppointmentsController. Displays appointments from the database.
 */
public class AppointmentsController implements Initializable {

    public TableView<Appointments> appointmentsTableView;
    public TableColumn <Appointments, Integer> idCol;
    public TableColumn <Appointments, String> titleCol;
    public TableColumn <Appointments, String> descriptionCol;
    public TableColumn <Appointments, String> locationCol;
    public TableColumn <Appointments, String> typeCol;
    public TableColumn <Appointments, LocalDateTime> startCol;
    public TableColumn <Appointments, LocalDateTime> endCol;
    public TableColumn <Appointments, Integer> customerIdCol;
    public TableColumn <Appointments, Integer> userIdCol;
    public TableColumn <Appointments, Integer> contactIdCol;

    ObservableList<Appointments> appointmentsObservableList = DBAppointments.getAllAppointments();
    String errorMessage;


    /**
     * Initializes the data and populates the table information by grabbing the getAllAppointments() info
     * and assigning it to its proper columns.
     * @param resourceBundle
     * @param url
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //ObservableList<Appointments> appointmentsObservableList = DBAppointments.getAllAppointments();

        for (Appointments app : appointmentsObservableList) {
            appointmentsTableView.setItems(DBAppointments.getAllAppointments());

            idCol.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
            titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
            descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
            locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
            typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
            startCol.setCellValueFactory(new PropertyValueFactory<>("start"));
            endCol.setCellValueFactory(new PropertyValueFactory<>("end"));
            customerIdCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
            userIdCol.setCellValueFactory(new PropertyValueFactory<>("userId"));
            contactIdCol.setCellValueFactory(new PropertyValueFactory<>("contactId"));
        }
    }

    /**
     * Deletes a selected appointment from the appointment table view, after user confirmation.
     * @param actionEvent
     */
    public void deleteBtnClicked(ActionEvent actionEvent){
        Appointments deleteApp = appointmentsTableView.getSelectionModel().getSelectedItem();

        try {
            if (deleteApp != null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete appointment type '" +
                        deleteApp.getType() + "' (ID #: " + deleteApp.getAppointmentId()
                        + ")?", ButtonType.YES, ButtonType.CANCEL);
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.initOwner((Stage) ((Button) actionEvent.getSource()).getScene().getWindow());
                alert.setHeaderText("Delete appointment?");
                Optional<ButtonType> confirmation = alert.showAndWait();
                if (confirmation.get() == ButtonType.YES) {
                    DBAppointments.deleteAppointmentById(deleteApp.getAppointmentId());

                    // refresh list
                    ObservableList<Appointments> appList = DBAppointments.getAllAppointments();
                    appointmentsTableView.setItems(appList);
                }
            } else {
                errorMessage = "Please select an appointment.";
                ButtonType buttonType = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
                Alert invalidInput = new Alert(Alert.AlertType.WARNING, errorMessage, buttonType);
                invalidInput.showAndWait();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Navigates user to the "add appointment" screen.
     * @param actionEvent
     */
    public void addBtnClicked(ActionEvent actionEvent) throws IOException {
        URL url = new File("src/sample/View/AddAppointment.fxml").toURI().toURL();
        Parent root = FXMLLoader.load(url);
        Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 600, 448);
        stage.setTitle("AddAppointment");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Navigates user to the "update appointment" screen. Loads the selected appointment info.
     * @param actionEvent
     */
    public void updateBtnClicked(ActionEvent actionEvent) throws IOException {
        Appointments selectedApp = appointmentsTableView.getSelectionModel().getSelectedItem();

        if (selectedApp != null) {
            URL url = new File("src/sample/View/UpdateAppointment.fxml").toURI().toURL();
            FXMLLoader loader = new FXMLLoader(url);
            Parent parent = loader.load();
            Scene updateAppointment = new Scene(parent);

            UpdateAppointmentController controller = loader.getController();
            controller.loadData(selectedApp);

            Stage window = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
            window.setScene(updateAppointment);
        } else {
            errorMessage = "Please select an appointment.";
            ButtonType buttonType = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
            Alert invalidInput = new Alert(Alert.AlertType.WARNING, errorMessage, buttonType);
            invalidInput.showAndWait();
        }
    }

    /**
     * Allows a user to see all the appointments via respective radio button toggle.
     * @param actionEvent
     */
    public void allRadioBtnSelected(ActionEvent actionEvent){
        ObservableList<Appointments> allApps = DBAppointments.getAllAppointments();
        appointmentsTableView.setItems(allApps);
    }

    /**
     * Allows a user to see the current week's appointments via respective radio button toggle.
     * @param actionEvent
     */
    public void weekRadioBtnSelected(ActionEvent actionEvent){
        ObservableList<Appointments> weekApps = FXCollections.observableArrayList();
        ObservableList<Appointments> allApps = DBAppointments.getAllAppointments();

        LocalDateTime start = LocalDateTime.now().minusWeeks(1);
        LocalDateTime end = LocalDateTime.now().plusWeeks(1);

        for (Appointments apps : allApps) {
            if (apps.getEnd().isAfter(start) && apps.getEnd().isBefore(end)) {
                weekApps.add(apps);
            }
            appointmentsTableView.setItems(weekApps);
        }
    }

    /**
     * Allows a user to see the current month's appointments via respective radio button toggle.
     * @param actionEvent
     */
    public void monthRadioBtnSelected(ActionEvent actionEvent) {
        ObservableList<Appointments> monthApps = FXCollections.observableArrayList();
        ObservableList<Appointments> allApps = DBAppointments.getAllAppointments();

        LocalDateTime start = LocalDateTime.now().minusMonths(1);
        LocalDateTime end = LocalDateTime.now().plusMonths(1);

        for(Appointments apps : allApps){
            if(apps.getEnd().isAfter(start) && apps.getEnd().isBefore(end)){
                monthApps.add(apps);
            }
            appointmentsTableView.setItems(monthApps);
        }
    }

    /**
     * Navigates user back to the Main Screen.
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
