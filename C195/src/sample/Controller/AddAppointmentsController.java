package sample.Controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import sample.DOA.DBAppointments;
import sample.DOA.DBContacts;
import sample.DOA.JDBC;
import sample.Model.Appointments;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;

/**
 * AddAppointmentsController. Handles adding appointments to the database.
 */
public class AddAppointmentsController implements Initializable {

    public TextField titleTextField;
    public TextField descriptionTextField;
    public TextField locationTextField;
    public TextField typeTextField;
    public TextField customerIdTextField;
    public TextField userIdTextField;
    public DatePicker datePicker;
    public TextField startTimeTextField;
    public TextField endTimeTextField;
    public ComboBox<String> contactComboBox;
    public Button saveBtn;
    public Label errorLabel;

    public String errorMessage;

    ZoneId userTimeZone = ZoneId.systemDefault();

    /**
     * Populates contactComboBox with contact names and datePicker with calendar dates.
     * Lambda #1: doesn't let users pick dates in the past.
     * @param resourceBundle
     * @param url
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // populates combo box with contact names
        contactComboBox.setItems(DBContacts.getAllContactNames());

        // disables the user from picking weekend or previous days for START DATE
        datePicker.setDayCellFactory(param -> new DateCell(){
            @Override
            public void updateItem(LocalDate date, boolean empty){
                super.updateItem(date, empty);
                setDisable(
                        empty || date.getDayOfWeek() == DayOfWeek.SUNDAY ||
                                date.getDayOfWeek() == DayOfWeek.SATURDAY ||
                                date.isBefore(LocalDate.now()));
            }
        });
    }


    /**
     * Validates the user input before adding new appointment to the database, also makes sure there are no customer
     * appointment overlaps and that start/end times are within business hours.
     * @param actionEvent
     */
    public void saveBtnClicked(ActionEvent actionEvent) throws IOException, SQLException {

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        boolean validStartTime = true;
        boolean validEndTime = true;
        boolean validHours;
        boolean overlap;
        boolean validInputs = true;
        ZonedDateTime zonedEndDateTime = null;
        ZonedDateTime zonedStartDateTime = null;
        LocalDateTime endDateTime = null;
        LocalDateTime startDateTime = null;
        LocalDate appDate = datePicker.getValue();
        int customerId = Integer.parseInt(customerIdTextField.getText());

        // formatting START to HH:mm LocalDateTime
        try{
            startDateTime = LocalDateTime.of(datePicker.getValue(),
                    LocalTime.parse(startTimeTextField.getText(), dateTimeFormatter));
        } catch (DateTimeParseException e) {
            validStartTime = false;
            e.printStackTrace();
        }

        // formatting END to HH:mm LocalDateTime
        try {
            endDateTime = LocalDateTime.of(datePicker.getValue(),
                    LocalTime.parse(endTimeTextField.getText(), dateTimeFormatter));
        } catch (DateTimeParseException e) {
            validEndTime = false;
            e.printStackTrace();
        }

        if (!validStartTime || !validEndTime) {
            errorMessage = "Please enter start and end times in a HH:mm format.";
            ButtonType buttonType = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
            Alert invalidInput = new Alert(Alert.AlertType.WARNING, errorMessage, buttonType);
            invalidInput.showAndWait();
            return;
        }

        // make sure all text fields contain values
        if (titleTextField.getText().isBlank() || descriptionTextField.getText().isBlank() || locationTextField.getText().isBlank()
                || typeTextField.getText().isBlank() || datePicker.getValue() == null || startTimeTextField.getText().isBlank()
                || endTimeTextField.getText().isBlank() || customerIdTextField.getText().isBlank()
                || userIdTextField.getText().isBlank()) {
            //validInputs = false;
            //errorLabel.setText("Please input a value into each field.");

            errorMessage = "Please input a value into each field.";
            ButtonType buttonType = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
            Alert invalidInput = new Alert(Alert.AlertType.WARNING, errorMessage, buttonType);
            invalidInput.showAndWait();
            return;
        }

//        if (!validInputs) {
//            errorLabel.setText("Please input a value into each field.");
//        }

        validHours = validBusinessHours(appDate, startDateTime, endDateTime);
        overlap = checkCustomerOverlapForNewAppt(customerId, startDateTime, endDateTime, appDate);

        if (!validHours) {
            errorMessage = "Please ensure start and end times are between 8am - 10pm EST.";
            ButtonType buttonType = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
            Alert invalidInput = new Alert(Alert.AlertType.WARNING, errorMessage, buttonType);
            invalidInput.showAndWait();
            return;
        }

        if (!overlap) {
            errorMessage = "Customer already has a meeting during specified time.";
            ButtonType buttonType = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
            Alert invalidInput = new Alert(Alert.AlertType.WARNING, errorMessage, buttonType);
            invalidInput.showAndWait();
            return;
        }


        // if endDateTime & startDateTime are in a valid format, convert to ZonedDateTime
        zonedStartDateTime = ZonedDateTime.of(startDateTime, userTimeZone);
        zonedEndDateTime = ZonedDateTime.of(endDateTime, userTimeZone);

        zonedStartDateTime = zonedStartDateTime.withZoneSameInstant(ZoneOffset.UTC);
        zonedEndDateTime = zonedEndDateTime.withZoneSameInstant(ZoneOffset.UTC);

        try {
            // assigning values from text fields
            String contactName = contactComboBox.getValue();
            String title = titleTextField.getText();
            String description = descriptionTextField.getText();
            String location = locationTextField.getText();
            String type = typeTextField.getText();
            ZonedDateTime start = zonedStartDateTime;
            ZonedDateTime end = zonedEndDateTime;
            customerId = Integer.parseInt(customerIdTextField.getText());
            int userId = Integer.parseInt(userIdTextField.getText());
            int contactId = DBContacts.getContactIdByContactName(contactName);

            DBAppointments.addAppointment(title, description, location, type, start, end, customerId, userId, contactId);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        backBtnClicked(actionEvent);
    }


    public static boolean validBusinessHours(LocalDate appDate, LocalDateTime startDateTime, LocalDateTime endDateTime){
        ZoneId userTimeZone = ZoneId.systemDefault();

        ZonedDateTime startZonedDateTime = ZonedDateTime.of(startDateTime, userTimeZone);
        ZonedDateTime endZonedDateTime = ZonedDateTime.of(endDateTime, userTimeZone);

        ZonedDateTime startEST = ZonedDateTime.of(appDate, LocalTime.of(8,0), ZoneId.of("America/New_York"));
        ZonedDateTime endEST = ZonedDateTime.of(appDate, LocalTime.of(22,0), ZoneId.of("America/New_York"));

        // if input hours are outside business hours, return false.
        if (startZonedDateTime.isBefore(startEST) || startZonedDateTime.isAfter(endEST) ||
                endZonedDateTime.isBefore(startEST) || endZonedDateTime.isAfter(endEST) ||
                endZonedDateTime.isBefore(startZonedDateTime)) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Checks whether or not a customer has an appointment overlap.
     * @param inputCustomerID
     * @param startDateTime
     * @param endDateTime
     * @param apptDate
     */
    public static Boolean checkCustomerOverlapForNewAppt(Integer inputCustomerID, LocalDateTime startDateTime,
                                           LocalDateTime endDateTime, LocalDate apptDate) throws SQLException {

        // Retrieves list of appts that may have conflicts
        ObservableList<Appointments> possibleConflicts = DBAppointments.customerAppointmentsByDateForNewAppt(
                apptDate, inputCustomerID
        );

        if (possibleConflicts.isEmpty()) {
            return true;
        }
        else {
            for (Appointments conflictAppt : possibleConflicts) {

                LocalDateTime conflictStart = conflictAppt.getStart();
                LocalDateTime conflictEnd = conflictAppt.getEnd();

                    if (conflictStart.isEqual(startDateTime) || conflictEnd.isEqual(endDateTime)) {
                        return false;
                    }
                    if (conflictStart.isEqual(endDateTime) || conflictEnd.isEqual(startDateTime)){
                        return false;
                    }
                    if (conflictStart.isBefore(startDateTime) && conflictEnd.isAfter(endDateTime)) {
                        return false;
                    }
                    if (conflictStart.isBefore(endDateTime) && conflictStart.isAfter(startDateTime)) {
                        return false;
                    }
                    if (conflictEnd.isBefore(endDateTime) && conflictEnd.isAfter(startDateTime)) {
                        return false;
                    } else {
                        return true;
                    }
            }
        }
        return true;
    }


    //    public static boolean checkCustomerOverlap(int customerId, LocalDateTime start, LocalDateTime end,
//                                        LocalDate date) throws SQLException {
//        ObservableList<Appointments> possibleOverlap = DBAppointments.customerAppointmentsByDate(date, customerId);
//
//
//        if(possibleOverlap.isEmpty()){
//            return true;
//        } else {
//            for (Appointments overlap : possibleOverlap){
//
//                LocalDateTime overlapStart = overlap.getStart();
//                LocalDateTime overlapEnd = overlap.getEnd();
//
//                //checking that appointment is not in conflict with itself.
////                if (overlapStart.isEqual(start) && overlapEnd.isEqual(end)){
////                    return true;
////                }
//                if (overlapStart.isBefore(start) && overlapEnd.isAfter(end)){
//                    return false;
//                }
//                if (overlapStart.isBefore(end) && overlapEnd.isAfter(start)){
//                    return false;
//                }
//                if (overlapEnd.isBefore(end) && overlapEnd.isBefore(start)) {
//                    return false;
//                }
//                else {
//                    return true;
//                }
//            }
//        }
//        return true;
//    }

    /**
     * Launches the main Appointments screen.
     * @param actionEvent
     */
    public void backBtnClicked(ActionEvent actionEvent) throws IOException {
        URL url = new File("src/sample/View/Appointments.fxml").toURI().toURL();
        Parent root = FXMLLoader.load(url);
        Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1046, 564);
        stage.setTitle("Appointments");
        stage.setScene(scene);
        stage.show();
    }
}
