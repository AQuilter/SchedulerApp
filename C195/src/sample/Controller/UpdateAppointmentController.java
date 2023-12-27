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
import sample.Model.Appointments;
import sample.Model.Contacts;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;

public class UpdateAppointmentController implements Initializable{

    public TextField idTextField;
    public TextField titleTextField;
    public TextField descriptionTextField;
    public TextField locationTextField;
    public TextField typeTextField;
    public ComboBox contactComboBox;
    public DatePicker datePicker;
    public TextField startTimeTextField;
    public TextField endTimeTextField;
    public TextField customerIdTextField;
    public TextField userIdTextField;
    public Button saveBtn;

    public String errorMessage;

    ZoneId userTimeZone = ZoneId.systemDefault();

    /**
     * Takes the appointment selected from the previous screen and loads the text fields with that data.
     * @param selectedAppointment
     */
    // loads the data into the textfields
    public void loadData(Appointments selectedAppointment) {
        contactComboBox.setItems(DBContacts.getAllContactNames());
        contactComboBox.getSelectionModel().select(DBContacts.getContactNameByContactId(selectedAppointment.getContactId()));

        idTextField.setText(String.valueOf(selectedAppointment.getAppointmentId()));
        titleTextField.setText(selectedAppointment.getTitle());
        descriptionTextField.setText(selectedAppointment.getDescription());
        locationTextField.setText(selectedAppointment.getLocation());
        typeTextField.setText(selectedAppointment.getType());
        customerIdTextField.setText(String.valueOf(selectedAppointment.getCustomerId()));
        userIdTextField.setText(String.valueOf(selectedAppointment.getUserId()));
        datePicker.setValue(selectedAppointment.getStart().toLocalDate());
        startTimeTextField.setText(String.valueOf(selectedAppointment.getStart().toLocalTime()));
        endTimeTextField.setText(String.valueOf(selectedAppointment.getEnd().toLocalTime()));
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
        boolean overlap = true;
        boolean validInputs = true;
        ZonedDateTime zonedEndDateTime = null;
        ZonedDateTime zonedStartDateTime = null;
        LocalDateTime endDateTime = null;
        LocalDateTime startDateTime = null;
        LocalDate appDate = datePicker.getValue();


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

        int customerId = Integer.parseInt(customerIdTextField.getText());
        int apptId = Integer.parseInt(idTextField.getText());
        validHours = AddAppointmentsController.validBusinessHours(appDate, startDateTime, endDateTime);
        overlap = checkCustomerOverlapForUpdateAppt(customerId, startDateTime, endDateTime, appDate, apptId);

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
            String contactName = (String) contactComboBox.getValue();

            Integer id = Integer.parseInt(idTextField.getText());
            //String contactName = contactComboBox.getSelectionModel().getSelectedItem().toString(); // casted to String????
            String title = titleTextField.getText();
            String description = descriptionTextField.getText();
            String location = locationTextField.getText();
            String type = typeTextField.getText();
            ZonedDateTime start = zonedStartDateTime;
            ZonedDateTime end = zonedEndDateTime;
            customerId = Integer.parseInt(customerIdTextField.getText());
            int userId = Integer.parseInt(userIdTextField.getText());
            int contactId = DBContacts.getContactIdByContactName(contactName);

            DBAppointments.updateAppointment(id, title, description, location, type, start, end, customerId, userId, contactId);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        backBtnClicked(actionEvent);
    }

    public static Boolean checkCustomerOverlapForUpdateAppt(Integer inputCustomerID, LocalDateTime startDateTime,
                                                         LocalDateTime endDateTime, LocalDate apptDate, int apptId) throws SQLException {

        // Get list of appointments that might have conflicts
        ObservableList<Appointments> possibleConflicts = DBAppointments.customerAppointmentsByDateForUpdateAppt(apptDate, inputCustomerID, apptId);

        if (possibleConflicts.isEmpty()) {
            return true;
        }
        else {
            for (Appointments conflictAppt : possibleConflicts) {

                LocalDateTime conflictStart = conflictAppt.getStart();
                LocalDateTime conflictEnd = conflictAppt.getEnd();

                if(apptId != conflictAppt.getAppointmentId()) {

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
        }
        return true;
    }

    public void backBtnClicked(ActionEvent actionEvent) throws IOException {
        URL url = new File("src/sample/View/Appointments.fxml").toURI().toURL();
        Parent root = FXMLLoader.load(url);
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1046, 564);
        stage.setTitle("Appointments");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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
}



